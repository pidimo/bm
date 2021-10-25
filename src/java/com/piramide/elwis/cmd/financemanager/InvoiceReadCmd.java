package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.domain.contactmanager.Customer;
import com.piramide.elwis.domain.contactmanager.CustomerHome;
import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.dto.financemanager.InvoiceDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(InvoiceReadCmd.class);

    public void executeInStateless(SessionContext ctx) {
        Integer invoiceId = getValueAsInteger("invoiceId");
        boolean withReferences = null != paramDTO.get("withReferences")
                && "true".equals(paramDTO.get("withReferences").toString());

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.put("invoiceId", invoiceId);
        invoiceDTO.put("number", paramDTO.get("number"));
        invoiceDTO.put("withReferences", withReferences);

        Invoice invoice =
                (Invoice) ExtendedCRUDDirector.i.read(invoiceDTO, resultDTO, withReferences);

        //invoice was deleted by another user
        if (null == invoice) {
            return;
        }

        resultDTO.put("totalAmountNet", invoice.getTotalAmountNet());
        resultDTO.put("totalAmountGross", invoice.getTotalAmountGross());
        resultDTO.put("openAmount", invoice.getOpenAmount());
        resultDTO.put("addressName", readAddressName(invoice.getAddressId(), ctx));

        //use in ui to store entity invoice date
        resultDTO.put("entityInvoiceDate", invoice.getInvoiceDate());

        //use in ui to rebuild invoice number
        try {
            Customer customer = searchCustomer(invoice.getAddressId());
            resultDTO.put("customerNumber", customer.getNumber());
        } catch (FinderException e) {
            log.debug("->Read Customer [addressId=" + invoice.getAddressId() + "] FAIL");
            resultDTO.put("customerNumber", null);
        }

        //use in ui to show invoice associated when actual credit note
        if (FinanceConstants.InvoiceType.CreditNote.equal(invoice.getType())) {
            resultDTO.put("hasRelation", false);

            if (null != invoice.getCreditNoteOfId()) {
                InvoiceDTO dto = getCreditNoteOf(invoice.getCreditNoteOfId());

                //used in ui to show related invoice number of credit note
                resultDTO.put("relatedInvoiceNumber", dto.get("number"));
                resultDTO.put("hasRelation", true);

                if (withReferences) {
                    resultDTO.addResultMessage("CreditNote.realedinvoice.warn", dto.get("number"));
                }
            }
        }

        //Invoice type in invoices can be changed only if this payments not have relations with creditnotes
        if (FinanceConstants.InvoiceType.Invoice.equal(invoice.getType())) {
            resultDTO.put("canUpdateInvoiceType", canUpdateInvoiceType(invoice));
        }


        if (null != invoice.getNotesId()) {
            resultDTO.put("notes", readFinanceFreeText(invoice));
        }
    }

    private String readAddressName(Integer addressId, SessionContext ctx) {
        LightlyAddressCmd addressCmd = new LightlyAddressCmd();
        addressCmd.putParam("addressId", addressId);

        addressCmd.executeInStateless(ctx);

        ResultDTO customResultDTO = addressCmd.getResultDTO();
        return (String) customResultDTO.get("addressName");
    }

    private String readFinanceFreeText(Invoice invoice) {
        FinanceFreeTextHome financeFreeTextHome =
                (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);

        try {
            FinanceFreeText financeFreeText = financeFreeTextHome.findByPrimaryKey(invoice.getNotesId());
            return new String(financeFreeText.getValue());
        } catch (FinderException e) {
            log.debug("-> Read SalesFreeText freeTextId=" + invoice.getNotesId() + " FAIL");
        }
        return "";
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

    private InvoiceDTO getCreditNoteOf(Integer creditNoteOfId) {

        InvoiceHome invoiceHome =
                (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
        try {
            Invoice invoice = invoiceHome.findByPrimaryKey(creditNoteOfId);
            InvoiceDTO invoiceDTO = new InvoiceDTO();
            DTOFactory.i.copyToDTO(invoice, invoiceDTO);
            return invoiceDTO;
        } catch (FinderException e) {
            log.error("-> Execute InvoiceHome.findByPrimaryKey(" +
                    creditNoteOfId + ") FAIL, InvoiceReadCmd.getCreditNoteOf() Method", e);
        }
        return null;
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

    private Boolean canDeleteInvoicePositions(Invoice invoice, SessionContext ctx) {

        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.setOp("canDeleteInvoicePositionFromCreditNote");
        invoicePositionCmd.putParam("invoiceId", invoice.getInvoiceId());
        invoicePositionCmd.putParam("companyId", invoice.getCompanyId());
        invoicePositionCmd.executeInStateless(ctx);

        return (Boolean) invoicePositionCmd.getResultDTO().get("canDeleteInvoicePositionFromCreditNote");
    }

    private Customer searchCustomer(Integer addressId) throws FinderException {
        CustomerHome customerHome =
                (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);
        return customerHome.findByPrimaryKey(addressId);
    }

    public boolean isStateful() {
        return false;
    }
}
