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
import com.piramide.elwis.dto.financemanager.InvoicePositionDTO;
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
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.3
 */
public class InvoiceCreateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(InvoiceCreateCmd.class);

    public void executeInStateless(SessionContext ctx) {
        Integer addressId = getValueAsInteger("addressId");
        Integer payConditionId = getValueAsInteger("payConditionId");
        Integer invoiceDate = (Integer) paramDTO.get("invoiceDate");
        Integer serviceDate = (Integer) paramDTO.get("serviceDate");

        String notes = (String) paramDTO.get("notes");

        Integer companyId = getValueAsInteger("companyId");

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.put("type", getValueAsInteger("type"));
        invoiceDTO.put("addressId", addressId);
        invoiceDTO.put("payConditionId", payConditionId);
        invoiceDTO.put("contactPersonId", getValueAsInteger("contactPersonId"));
        invoiceDTO.put("templateId", getValueAsInteger("templateId"));
        invoiceDTO.put("currencyId", getValueAsInteger("currencyId"));
        invoiceDTO.put("invoiceDate", invoiceDate);
        invoiceDTO.put("serviceDate", serviceDate);
        invoiceDTO.put("companyId", companyId);
        invoiceDTO.put("title", paramDTO.get("title"));
        EJBCommandUtil.i.setValueAsInteger(this, invoiceDTO, "creditNoteOfId");
        EJBCommandUtil.i.setValueAsInteger(this, invoiceDTO, "netGross");
        EJBCommandUtil.i.setValueAsInteger(this, invoiceDTO, "sentAddressId");
        EJBCommandUtil.i.setValueAsInteger(this, invoiceDTO, "sentContactPersonId");
        EJBCommandUtil.i.setValueAsInteger(this, invoiceDTO, "additionalAddressId");

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

        Integer notesId = null;
        if (null != notes && !"".equals(notes.trim())) {
            FinanceFreeText financeFreeText = createFreeText(notes, companyId);
            notesId = financeFreeText.getFreeTextId();
        }
        invoiceDTO.put("notesId", notesId);

        Integer paymentDate = InvoiceUtil.i.getPaymentDate(invoiceDate, payConditionId, companyId);
        invoiceDTO.put("paymentDate", paymentDate);

        Integer reminderDate = InvoiceUtil.i.getReminderDate(paymentDate);
        invoiceDTO.put("reminderDate", reminderDate);

        SequenceRule sequenceRule =
                InvoiceUtil.i.getSequenceRule(Integer.valueOf(paramDTO.get("sequenceRuleId").toString()));

        String number = InvoiceUtil.i.getInvoiceNumber(sequenceRule, invoiceDate, customerNumber);
        invoiceDTO.put("sequenceRuleId", sequenceRule.getNumberId());
        invoiceDTO.put("ruleFormat", sequenceRule.getFormat());
        invoiceDTO.put("ruleNumber", sequenceRule.getLastNumber());
        invoiceDTO.put("number", number);

        invoiceDTO.put("totalAmountNet", new BigDecimal(0));
        invoiceDTO.put("totalAmountGross", new BigDecimal(0));
        invoiceDTO.put("openAmount", new BigDecimal(0));
        Invoice targetInvoice =
                (Invoice) ExtendedCRUDDirector.i.create(invoiceDTO, resultDTO, false);
        copyInvoice(targetInvoice, ctx);

        if (FinanceConstants.InvoiceType.CreditNote.equal(targetInvoice.getType())) {
            createPaymentsForCreditNote(targetInvoice, ctx);

            //used to detect the next page, with credit notes the nex page is a list of invoice positions associated
            //to credit note invoice.
            resultDTO.put("creditNoteInvoiceHaveInvoicePositions",
                    creditNoteInvoiceHaveInvoicePositions(targetInvoice, ctx));
        }
    }

    /**
     * Verifies if the <code>Invoice</code> does have <code>InvoicePosition</code> associated.
     *
     * @param invoice <code>Invoice</code> object to check if have or not <code>InvoicePosition</code> objects.
     * @param ctx     <code>SessionContext</code> object for general purposes.
     * @return true if the invoice does have <code>InvoicePosition</code> associated, false in other case.
     */
    private Boolean creditNoteInvoiceHaveInvoicePositions(Invoice invoice, SessionContext ctx) {
        if (null == invoice.getCreditNoteOfId()) {
            return false;
        }

        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.setOp("getInvoicePositions");
        invoicePositionCmd.putParam("invoiceId", invoice.getCreditNoteOfId());
        invoicePositionCmd.putParam("companyId", invoice.getCompanyId());
        invoicePositionCmd.executeInStateless(ctx);
        List<InvoicePositionDTO> invoicePositions =
                (List<InvoicePositionDTO>) invoicePositionCmd.getResultDTO().get("getInvoicePositions");

        return null != invoicePositions && !invoicePositions.isEmpty();
    }

    private void createPaymentsForCreditNote(Invoice invoice, SessionContext ctx) {

        //Credit Note No relation with invoice
        if (null == invoice.getCreditNoteOfId()) {
            return;
        }

        InvoicePaymentCmd invoicePaymentCmd = new InvoicePaymentCmd();
        invoicePaymentCmd.setOp("create");
        invoicePaymentCmd.putParam("companyId", invoice.getCompanyId());
        invoicePaymentCmd.putParam("invoiceId", invoice.getInvoiceId());
        invoicePaymentCmd.putParam("amount", new BigDecimal(0));
        invoicePaymentCmd.putParam("payDate", invoice.getInvoiceDate());
        invoicePaymentCmd.putParam("creditNoteId", invoice.getCreditNoteOfId());
        invoicePaymentCmd.putParam("text", paramDTO.get("creditNotePaymentMessage"));
        invoicePaymentCmd.putParam("invoicePaymentMessage",
                paramDTO.get("invoicePaymentMessage") + " " + invoice.getNumber());

        invoicePaymentCmd.executeInStateless(ctx);
    }

    private void copyInvoice(Invoice targetInvoice, SessionContext ctx) {
        if (FinanceConstants.InvoiceType.CreditNote.equal(targetInvoice.getType())) {
            return;
        }

        Integer sourceInvoiceId = null;
        if (null != paramDTO.get("copyOfInvoiceId") &&
                !"".equals(paramDTO.get("copyOfInvoiceId").toString().trim())) {
            try {
                sourceInvoiceId = Integer.valueOf(paramDTO.get("copyOfInvoiceId").toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse copyOfInvoiceId=" + paramDTO.get("copyOfInvoiceId") + " FAIL", e);
            }
        }

        if (null == sourceInvoiceId) {
            return;
        }

        InvoiceHome invoiceHome =
                (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);

        Invoice sourceInvoice = null;
        try {
            sourceInvoice = invoiceHome.findByPrimaryKey(sourceInvoiceId);
        } catch (FinderException e) {
            log.debug("->Read Invoice invoiceId=" + sourceInvoiceId + " FAIL, Cannot copy invoice information");
            return;
        }

        copyInvoicePositions(sourceInvoice, targetInvoice, ctx);
    }

    private void copyInvoicePositions(Invoice sourceInvoice,
                                      Invoice targetInvoice,
                                      SessionContext ctx) {
        Collection sourceInvoicePositions = sourceInvoice.getInvoicePositions();
        if (null != sourceInvoicePositions) {
            for (Object object : sourceInvoicePositions) {
                InvoicePosition sourcePosition = (InvoicePosition) object;
                InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
                invoicePositionCmd.putParam("accountId", sourcePosition.getAccountId());
                invoicePositionCmd.putParam("companyId", sourcePosition.getCompanyId());
                invoicePositionCmd.putParam("number", sourcePosition.getNumber());
                invoicePositionCmd.putParam("productId", sourcePosition.getProductId());
                invoicePositionCmd.putParam("quantity", sourcePosition.getQuantity());
                invoicePositionCmd.putParam("unitPrice", sourcePosition.getUnitPrice());
                invoicePositionCmd.putParam("unitPriceGross", sourcePosition.getUnitPriceGross());
                invoicePositionCmd.putParam("discount", sourcePosition.getDiscount());
                invoicePositionCmd.putParam("unit", sourcePosition.getUnit());
                invoicePositionCmd.putParam("vatId", sourcePosition.getVatId());
                invoicePositionCmd.putParam("invoiceId", targetInvoice.getInvoiceId());
                if (null != sourcePosition.getFreetextId()) {
                    invoicePositionCmd.putParam("text", new String(sourcePosition.getFinanceFreeText().getValue()));
                }

                invoicePositionCmd.setOp("create");

                invoicePositionCmd.executeInStateless(ctx);
            }
        }
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

    public boolean isStateful() {
        return false;
    }
}
