package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.dto.financemanager.InvoicePaymentDTO;
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

/**
 * @author: ivan
 * <p/>
 * Jatun S.R.L
 */
public class InvoicePaymentCmd extends EJBCommand {

    private Log log = LogFactory.getLog(InvoicePaymentCmd.class);

    public void executeInStateless(SessionContext sessionContext) {
        boolean isRead = true;
        if ("create".equals(getOp())) {
            isRead = false;
            create(getInvoicePaymentDTO());
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update(getInvoicePaymentDTO());
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(getInvoicePaymentDTO());
        }
        if ("canPayInvoice".equals(getOp())) {
            isRead = false;
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            canPayInvoice(invoiceId);
        }
        if ("hasZeroOpenAmount".equals(getOp())) {
            isRead = false;
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            hasZeroOpenAmount(invoiceId);
        }
        if ("getTotalPaid".equals(getOp())) {
            isRead = false;
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            getTotalPaid(invoiceId, companyId);
        }
        if ("haveInvoicePayments".equals(getOp())) {
            isRead = false;
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            haveInvoicePayments(invoiceId, companyId);
        }
        if ("updateCreditNotePayments".equals(getOp())) {
            isRead = false;
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            updateCreditNotePayments(invoiceId);
        }
        if ("deleteCreditNotePayments".equals(getOp())) {
            isRead = false;
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            deleteCreditNotePayments(invoiceId);
        }
        if (isRead) {
            InvoicePaymentDTO invoicePaymentDTO = getInvoicePaymentDTO();
            boolean checkReferences = null != paramDTO.get("withReferences") &&
                    "true".equals(paramDTO.get("withReferences").toString().trim());
            read(invoicePaymentDTO, checkReferences);
        }
    }

    private void delete(InvoicePaymentDTO invoicePaymentDTO) {
        InvoicePayment invoicePayment =
                (InvoicePayment) ExtendedCRUDDirector.i.read(invoicePaymentDTO, resultDTO, true);

        if (null == invoicePayment) {
            resultDTO.setForward("Fail");
            return;
        }

        Integer invoiceId = invoicePayment.getInvoiceId();
        ExtendedCRUDDirector.i.delete(invoicePaymentDTO, resultDTO, true, "Fail");

        updateOpenAmount(invoiceId);
    }

    private void update(InvoicePaymentDTO invoicePaymentDTO) {
        InvoicePayment invoicePayment =
                (InvoicePayment) ExtendedCRUDDirector.i.update(
                        invoicePaymentDTO, resultDTO, false, true, true, "Fail"
                );
        if (null == invoicePayment) {
            return;
        }

        if (resultDTO.isFailure()) {
            //version error, put new freetext
            if (null != invoicePayment.getFreetextId()) {
                resultDTO.put("text", new String(invoicePayment.getFinanceFreeText().getValue()));
            }
            return;
        }

        String text = (String) invoicePaymentDTO.get("text");
        if (null == invoicePayment.getFreetextId()) {
            if (null != text && !"".equals(text.trim())) {
                FinanceFreeText financeFreeText = createFreeText(text, (Integer) invoicePaymentDTO.get("companyId"));
                invoicePayment.setFreetextId(financeFreeText.getFreeTextId());
            }
        } else if (null != text) {
            invoicePayment.getFinanceFreeText().setValue(text.getBytes());
        }

        updateOpenAmount(invoicePayment.getInvoiceId());
    }

    private void read(InvoicePaymentDTO invoicePaymentDTO, boolean checkReferences) {
        InvoicePayment invoicePayment =
                (InvoicePayment) ExtendedCRUDDirector.i.read(invoicePaymentDTO, resultDTO, checkReferences);

        if (null == invoicePayment) {
            return;
        }

        if (null != invoicePayment.getFreetextId()) {
            resultDTO.put("text", new String(invoicePayment.getFinanceFreeText().getValue()));
        }
    }

    private void create(InvoicePaymentDTO invoicePaymentDTO) {
        String text = (String) invoicePaymentDTO.get("text");
        if (null != text && !"".equals(text.trim())) {
            FinanceFreeText financeFreeText = createFreeText(text, (Integer) invoicePaymentDTO.get("companyId"));
            invoicePaymentDTO.put("freetextId", financeFreeText.getFreeTextId());
        }

        InvoicePayment invoicePayment =
                (InvoicePayment) ExtendedCRUDDirector.i.create(invoicePaymentDTO, resultDTO, false);

        updateOpenAmount(invoicePayment.getInvoiceId());

        if (FinanceConstants.InvoiceType.CreditNote.equal(invoicePayment.getInvoice().getType())) {
            createPaymentForRelatedInvoice(invoicePayment);
        }
    }

    private void deleteCreditNotePayments(Integer invoiceId) {
        Invoice invoice;
        try {
            invoice = getInvoice(invoiceId);
        } catch (FinderException e) {
            log.debug("-> Cannot Read Invoice " + invoiceId + " updateCredidNotPayments FAIL ");
            return;
        }

        if (FinanceConstants.InvoiceType.Invoice.equal(invoice.getType())) {
            return;
        }

        if (null == invoice.getCreditNoteOfId()) {
            return;
        }

        Collection invoicePayments = null;
        InvoicePaymentHome invoicePaymentHome =
                (InvoicePaymentHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPAYMENT);
        try {
            invoicePayments = invoicePaymentHome.findByCreditNoteId(invoiceId, invoice.getCompanyId());
        } catch (FinderException e) {
            log.debug("-> Cannot read payment associated to Credit Note findByCreditNoteId Fail");
        }

        if (null == invoicePayments || invoicePayments.size() > 1 || invoicePayments.isEmpty()) {
            return;
        }

        String invoicePaymentMessage = paramDTO.getAsString("invoicePaymentMessage");

        InvoicePayment invoicePayment = (InvoicePayment) invoicePayments.toArray()[0];
        invoicePayment.setAmount(new BigDecimal(0));
        invoicePayment.setCreditNoteId(null);
        if (null != invoicePaymentMessage && !"".equals(invoicePaymentMessage.trim())) {
            invoicePayment.getFinanceFreeText().setValue(invoicePaymentMessage.getBytes());
        }

        updateOpenAmount(invoicePayment.getInvoiceId());
    }

    private void updateCreditNotePayments(Integer invoiceId) {
        Invoice invoice;
        try {
            invoice = getInvoice(invoiceId);
        } catch (FinderException e) {
            log.debug("-> Cannot Read Invoice " + invoiceId + " updateCredidNotPayments FAIL ");
            return;
        }

        if (FinanceConstants.InvoiceType.Invoice.equal(invoice.getType())) {
            return;
        }

        if (null == invoice.getCreditNoteOfId()) {
            return;
        }

        Collection creditNotePayments = invoice.getInvoicePayment();
        if (null == creditNotePayments || creditNotePayments.size() > 1) {
            return;
        }

        Collection invoicePayments = null;
        InvoicePaymentHome invoicePaymentHome =
                (InvoicePaymentHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPAYMENT);
        try {
            invoicePayments = invoicePaymentHome.findByCreditNoteId(invoiceId, invoice.getCompanyId());
        } catch (FinderException e) {
            log.debug("-> Cannot read payment associated to Credit Note findByCreditNoteId Fail");
        }

        if (null == invoicePayments || invoicePayments.size() > 1) {
            return;
        }

        InvoicePayment creditNotePayment = (InvoicePayment) creditNotePayments.toArray()[0];
        creditNotePayment.setAmount(invoice.getTotalAmountGross());
        updateOpenAmount(creditNotePayment.getInvoiceId());

        InvoicePayment invoicePayment = (InvoicePayment) invoicePayments.toArray()[0];
        invoicePayment.setAmount(invoice.getTotalAmountGross());
        updateOpenAmount(invoicePayment.getInvoiceId());

    }

    private void createPaymentForRelatedInvoice(InvoicePayment invoicePayment) {
        Integer creditNoteOfId = invoicePayment.getInvoice().getCreditNoteOfId();
        if (null == creditNoteOfId) {
            return;
        }

        Invoice invoice;
        try {
            invoice = getInvoice(creditNoteOfId);
        } catch (FinderException e) {
            log.debug("-> Cannot read invoice related to creditNote,  InvoicePaymentCmd.getInvoice(" +
                    creditNoteOfId + ") FAIL");
            return;
        }

        InvoicePaymentDTO invoicePaymentDTO = new InvoicePaymentDTO();
        invoicePaymentDTO.put("companyId", invoicePayment.getCompanyId());
        invoicePaymentDTO.put("invoiceId", invoice.getInvoiceId());
        invoicePaymentDTO.put("amount", invoicePayment.getAmount());
        invoicePaymentDTO.put("payDate", invoicePayment.getPayDate());
        invoicePaymentDTO.put("creditNoteId", invoicePayment.getInvoiceId());

        invoicePaymentDTO.put("text", paramDTO.get("invoicePaymentMessage"));

        create(invoicePaymentDTO);
    }

    private void updateOpenAmount(Integer invoiceId) {
        try {
            Invoice invoice = getInvoice(invoiceId);
            BigDecimal newOpenAmount = InvoiceUtil.i.calculateOpenAmount(invoice);
            invoice.setOpenAmount(newOpenAmount);
        } catch (FinderException e) {
            log.debug("-> Read Invoice invoiceId=" + invoiceId + " FAIL");
        }
    }

    private boolean haveInvoicePayments(Integer invoiceId, Integer companyId) {
        InvoicePaymentHome invoicePaymentHome = getInvoicePaymentHome();
        boolean result = false;
        try {
            Collection invoicePayments = invoicePaymentHome.findByInvoiceId(invoiceId, companyId);
            result = !invoicePayments.isEmpty();
        } catch (FinderException e) {
            log.debug("-> Read InvoicePayment invoiceId=" + invoiceId + " FAIL");
        }

        resultDTO.put("haveInvoicePayments", result);
        return result;
    }

    private FinanceFreeText createFreeText(String text, Integer companyId) {
        FinanceFreeTextHome financeFreeTextHome =
                (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
        FinanceFreeText freeText = null;
        try {
            freeText = financeFreeTextHome.create(text.getBytes(), companyId, FreeTextTypes.FREETEXT_INVOICEPAYMENT);
        } catch (CreateException e) {
            log.error("-> Execute FinanceFreeTextHome.create() Fail", e);
        }
        return freeText;
    }

    private InvoicePaymentDTO getInvoicePaymentDTO() {
        InvoicePaymentDTO dto = new InvoicePaymentDTO();
        dto.put("paymentId", getValueAsInteger("paymentId"));
        dto.put("companyId", paramDTO.get("companyId"));
        dto.put("amount", paramDTO.get("amount"));
        dto.put("invoiceId", getValueAsInteger("invoiceId"));
        dto.put("payDate", getValueAsInteger("payDate"));
        dto.put("version", paramDTO.get("version"));
        dto.put("withReferences", paramDTO.get("withReferences"));
        EJBCommandUtil.i.setValueAsInteger(this, dto, "creditNoteId");
        dto.put("text", paramDTO.get("text"));
        dto.put("bankAccountId", getValueAsInteger("bankAccountId"));
        log.debug("-> Work on" + dto + " OK");

        return dto;
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

    private boolean canPayInvoice(Integer invoiceId) {
        boolean result = false;
        BigDecimal openAmount = new BigDecimal(0);
        BigDecimal totalAmountGross = new BigDecimal(0);
        try {
            Invoice invoice = getInvoice(invoiceId);
            result = (new BigDecimal(0)).compareTo(invoice.getOpenAmount()) == -1;
            if (!result && !FinanceConstants.InvoiceType.CreditNote.equal(invoice.getType())) { //if open amount is not zero and it's a credit note
                result = true;
            }
            openAmount = invoice.getOpenAmount();
            totalAmountGross = invoice.getTotalAmountGross();
        } catch (FinderException e) {
            log.debug("-> Read InvoicePayments invoiceId=" + invoiceId + " FAIL");
        }
        resultDTO.put("canPayInvoice", result);
        resultDTO.put("openAmount", openAmount);
        resultDTO.put("totalAmountGross", totalAmountGross);
        return result;
    }

    private boolean hasZeroOpenAmount(Integer invoiceId) {
        boolean result;
        try {
            Invoice invoice = getInvoice(invoiceId);
            result = (new BigDecimal(0)).compareTo(invoice.getOpenAmount()) == 0;
            resultDTO.put("hasZeroOpenAmount", result);
            return result;
        } catch (FinderException e) {
            return false;
        }
    }

    private BigDecimal getTotalPaid(Integer invoiceId, Integer companyId) {
        InvoicePaymentHome invoicePaymentHome = getInvoicePaymentHome();
        BigDecimal result = new BigDecimal(0);

        Collection invoicePayments;
        try {
            invoicePayments = invoicePaymentHome.findByInvoiceId(invoiceId, companyId);
        } catch (FinderException e) {
            resultDTO.put("getTotalPaid", result);
            return result;
        }

        for (Object object : invoicePayments) {
            InvoicePayment invoicePayment = (InvoicePayment) object;
            result = result.add(invoicePayment.getAmount());
        }
        resultDTO.put("getTotalPaid", result);
        return result;
    }

    private Invoice getInvoice(Integer invoiceId) throws FinderException {
        InvoiceHome invoiceHome = getInvoiceHome();
        return invoiceHome.findByPrimaryKey(invoiceId);
    }

    private InvoiceHome getInvoiceHome() {
        return (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
    }

    private InvoicePaymentHome getInvoicePaymentHome() {
        return (InvoicePaymentHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPAYMENT);
    }

    public boolean isStateful() {
        return false;
    }
}
