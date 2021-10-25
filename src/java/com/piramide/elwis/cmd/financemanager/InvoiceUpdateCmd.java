package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.contactmanager.Customer;
import com.piramide.elwis.domain.contactmanager.CustomerHome;
import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import com.piramide.elwis.dto.financemanager.InvoiceDTO;
import com.piramide.elwis.utils.CodeUtil;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceUpdateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(InvoiceUpdateCmd.class);

    public void executeInStateless(SessionContext ctx) {
        Integer addressId = getValueAsInteger("addressId");
        Integer payConditionId = getValueAsInteger("payConditionId");
        Integer invoiceDate = (Integer) paramDTO.get("invoiceDate");
        String notes = (String) paramDTO.get("notes");

        Integer companyId = getValueAsInteger("companyId");
        Integer invoiceId = EJBCommandUtil.i.getValueAsInteger(this, "invoiceId");
        Integer uiType = EJBCommandUtil.i.getValueAsInteger(this, "type");


        if (!hasValidInvoiceTypeChange(invoiceId, uiType)) {
            InvoiceReadCmd invoiceReadCmd = new InvoiceReadCmd();
            invoiceReadCmd.putParam("invoiceId", invoiceId);
            invoiceReadCmd.putParam("number", paramDTO.get("number"));
            invoiceReadCmd.executeInStateless(ctx);
            resultDTO.putAll(invoiceReadCmd.getResultDTO());
            return;
        }

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.put("type", getValueAsInteger("type"));
        invoiceDTO.put("addressId", addressId);
        invoiceDTO.put("payConditionId", payConditionId);
        invoiceDTO.put("contactPersonId", getValueAsInteger("contactPersonId"));
        invoiceDTO.put("templateId", getValueAsInteger("templateId"));
        invoiceDTO.put("currencyId", getValueAsInteger("currencyId"));
        invoiceDTO.put("invoiceDate", invoiceDate);
        invoiceDTO.put("serviceDate", getValueAsInteger("serviceDate"));
        invoiceDTO.put("companyId", companyId);
        invoiceDTO.put("invoiceId", invoiceId);
        invoiceDTO.put("version", paramDTO.get("version"));
        invoiceDTO.put("number", paramDTO.get("number"));
        invoiceDTO.put("title", paramDTO.get("title"));
        EJBCommandUtil.i.setValueAsInteger(this, invoiceDTO, "creditNoteOfId");
        EJBCommandUtil.i.setValueAsInteger(this, invoiceDTO, "netGross");
        EJBCommandUtil.i.setValueAsInteger(this, invoiceDTO, "sentAddressId");
        EJBCommandUtil.i.setValueAsInteger(this, invoiceDTO, "sentContactPersonId");
        EJBCommandUtil.i.setValueAsInteger(this, invoiceDTO, "additionalAddressId");

        Invoice invoice =
                (Invoice) ExtendedCRUDDirector.i.update(invoiceDTO, resultDTO, false, true, true, "Fail");

        //invoice was deleted
        if (null == invoice) {
            return;
        }

        //version error
        if (resultDTO.isFailure()) {
            InvoiceReadCmd invoiceReadCmd = new InvoiceReadCmd();
            invoiceReadCmd.putParam("invoiceId", invoice.getInvoiceId());
            invoiceReadCmd.putParam("number", invoice.getNumber());
            invoiceReadCmd.executeInStateless(ctx);
            resultDTO.putAll(invoiceReadCmd.getResultDTO());
            return;
        }

        //contact the customer becomes
        String customerNumber = null;
        try {
            Customer customer = searchCustomer(addressId);
            customerNumber = customer.getNumber();
        } catch (FinderException e) {
            log.debug("-> Read Customer addressId=" + addressId + " FAIL");
            Address address = getAddress(addressId);
            if (null != address && !CodeUtil.isCustomer(address.getCode())) {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.put(CustomerDTO.KEY_CUSTOMERID, address.getAddressId());
                customerDTO.put("companyId", address.getCompanyId());
                Customer customer = (Customer) EJBFactory.i.createEJB(customerDTO);
                String newCustomerNumber = InvoiceUtil.i.getCustomerNumber(customer.getCompanyId());
                if (null != newCustomerNumber) {
                    customer.setNumber(newCustomerNumber);
                }
                address.setCode((byte) (address.getCode() + CodeUtil.customer));
                customerNumber = customer.getNumber();
            }
        }

        //update invoiceVats for invoice
        InvoiceVatCmd invoiceVatCmd = new InvoiceVatCmd();
        invoiceVatCmd.putParam("companyId", invoice.getCompanyId());
        invoiceVatCmd.putParam("invoiceId", invoice.getInvoiceId());
        invoiceVatCmd.setOp("update");
        invoiceVatCmd.executeInStateless(ctx);

        //calculate total amount net
        invoice.setTotalAmountNet(InvoiceUtil.i.calculateTotalAmountNet(invoice));

        //calculate total amount gross
        invoice.setTotalAmountGross(InvoiceUtil.i.calculateTotalAmountGross(invoice));

        //calculate open amount, it always must be done at the end because the totals may have changed
        invoice.setOpenAmount(InvoiceUtil.i.calculateOpenAmount(invoice));

        //calculate new paymentdate
        invoice.setPaymentDate(InvoiceUtil.i.getPaymentDate(invoiceDate, payConditionId, companyId));

        //calculate new reminder reminder date only if not reminder level assigned
        if (null == invoice.getReminderLevel()) {
            invoice.setReminderDate(InvoiceUtil.i.getReminderDate(invoice.getPaymentDate()));
        }

        String format = invoice.getRuleFormat();
        Integer number = invoice.getRuleNumber();
        String newNumber = InvoiceUtil.i.buildInvoiceNumber(format, number, customerNumber, invoice.getInvoiceDate());
        log.debug("-> Update InvoiceNumber from " + invoice.getNumber() + " To " + newNumber + " OK");
        invoice.setNumber(newNumber);

        //update freetext
        if (null == invoice.getNotesId()) {
            if (null != notes && !"".equals(notes.trim())) {
                FinanceFreeText financeFreeText = createFreeText(notes, companyId);
                invoice.setNotesId(financeFreeText.getFreeTextId());
            }
        } else if (null != notes) {
            invoice.getFinanceFreeText().setValue(notes.getBytes());
        }

        //create creditNote payments
        createPaymentsForCreditNote(invoice, ctx);
    }

    private Boolean hasValidInvoiceTypeChange(Integer invoiceId, Integer uiType) {
        Invoice actualInvoice = getInvoice(invoiceId);

        if (null == actualInvoice) {
            return true;
        }

        if (actualInvoice.getType().equals(uiType)) {
            return true;
        }

        if (FinanceConstants.InvoiceType.CreditNote.equal(actualInvoice.getType())) {
            if (null != actualInvoice.getCreditNoteOfId()) {
                Invoice relatedInvoice = getInvoice(actualInvoice.getCreditNoteOfId());

                resultDTO.addResultMessage("CreditNote.relaterInvoice.error", relatedInvoice.getNumber());
                return false;
            }
        }

        if (FinanceConstants.InvoiceType.Invoice.equal(actualInvoice.getType())) {
            if (!canUpdateInvoiceType(actualInvoice)) {
                resultDTO.addResultMessage("Invoice.paymentsRelated.error");
                return false;
            }
        }

        return true;
    }

    private void updateCreditNotePayments(Integer invoiceId, SessionContext ctx) {
        InvoicePaymentCmd invoicePaymentCmd = new InvoicePaymentCmd();
        invoicePaymentCmd.setOp("updateCreditNotePayments");
        invoicePaymentCmd.putParam("invoiceId", invoiceId);

        invoicePaymentCmd.executeInStateless(ctx);
    }

    private void createPaymentsForCreditNote(Invoice invoice, SessionContext ctx) {
        if (FinanceConstants.InvoiceType.Invoice.equal(invoice.getType())) {
            return;
        }

        //Credit Note No relation with invoice
        if (null == invoice.getCreditNoteOfId()) {
            return;
        }

        //credit note already payments and update associated payments
        if (null != invoice.getInvoicePayment() && invoice.getInvoicePayment().size() > 0) {
            updateCreditNotePayments(invoice.getInvoiceId(), ctx);
            return;
        }


        InvoicePaymentCmd invoicePaymentCmd = new InvoicePaymentCmd();
        invoicePaymentCmd.setOp("create");
        invoicePaymentCmd.putParam("companyId", invoice.getCompanyId());
        invoicePaymentCmd.putParam("invoiceId", invoice.getInvoiceId());
        invoicePaymentCmd.putParam("amount", invoice.getTotalAmountGross());
        invoicePaymentCmd.putParam("payDate", invoice.getInvoiceDate());
        invoicePaymentCmd.putParam("creditNoteId", invoice.getCreditNoteOfId());
        invoicePaymentCmd.putParam("text", paramDTO.get("creditNotePaymentMessage"));
        invoicePaymentCmd.putParam("invoicePaymentMessage",
                paramDTO.get("invoicePaymentMessage") + invoice.getNumber());

        invoicePaymentCmd.executeInStateless(ctx);
    }

    private FinanceFreeText createFreeText(String text, Integer companyId) {
        FinanceFreeTextHome financeFreeTextHome =
                (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
        FinanceFreeText freeText = null;
        try {
            freeText = financeFreeTextHome.create(text.getBytes(), companyId, FreeTextTypes.FREETEXT_INVOICE);
        } catch (CreateException e) {
            log.error("-> Execute FinanceFreeTextHome.create() Fail", e);
        }
        return freeText;
    }

    private Integer getValueAsInteger(String key) {
        Integer value = null;
        if (null != paramDTO.get(key) &&
                !"".equals(paramDTO.get(key).toString().trim())) {
            try {
                value = Integer.valueOf(paramDTO.get(key).toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse " + key + "=" + paramDTO.get(key) + " FAIL");
            }
        }

        return value;
    }

    private Customer searchCustomer(Integer addressId) throws FinderException {
        CustomerHome customerHome =
                (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);
        return customerHome.findByPrimaryKey(addressId);
    }

    private Address getAddress(Integer addressId) {
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        try {
            return addressHome.findByPrimaryKey(addressId);
        } catch (FinderException e) {
            log.error("-> Read address addressId=" + addressId + " FAIL");
        }

        return null;
    }

    private Invoice getInvoice(Integer invoiceId) {
        InvoiceHome invoiceHome =
                (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);

        try {
            return invoiceHome.findByPrimaryKey(invoiceId);
        } catch (FinderException e) {
            return null;
        }
    }

    private Boolean canUpdateInvoiceType(Invoice invoice) {
        InvoicePaymentHome paymentHome
                = (InvoicePaymentHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPAYMENT);
        try {
            List payments =
                    (List) paymentHome.findByInvoiceId(invoice.getInvoiceId(), invoice.getCompanyId());

            for (Object object : payments) {
                InvoicePayment payment = (InvoicePayment) object;
                if (null != payment.getCreditNoteId()) {
                    return false;
                }
            }

            return true;
        } catch (FinderException e) {
            return true;
        }
    }

    public boolean isStateful() {
        return false;
    }
}
