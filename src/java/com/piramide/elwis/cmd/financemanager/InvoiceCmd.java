package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.financemanager.Invoice;
import com.piramide.elwis.domain.financemanager.InvoiceHome;
import com.piramide.elwis.domain.financemanager.InvoicePayment;
import com.piramide.elwis.domain.financemanager.InvoicePositionHome;
import com.piramide.elwis.dto.financemanager.InvoiceDTO;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.math.BigDecimal;
import java.util.Collection;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceCmd extends EJBCommand {
    private Log log = LogFactory.getLog(InvoiceCmd.class);

    public void executeInStateless(SessionContext ctx) {
        if ("isCreditNote".equals(getOp())) {
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            isCreditNote(invoiceId);
        }
        if ("getInvoice".equals(getOp())) {
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            getInvoiceDTO(invoiceId);
        }

        if ("findInvoiceByNumber".equals(getOp())) {
            String number = (String) paramDTO.get("number");
            Integer companyId = (Integer) paramDTO.get("companyId");
            findInvoiceByNumber(number, companyId);
        }

        if ("canChangeNetGross".equals(getOp())) {
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            Integer netGross = (Integer) paramDTO.get("netGross");
            Boolean validatePayments = (Boolean) paramDTO.get("validatePayments");
            canChangeNetGross(invoiceId, companyId, netGross, validatePayments);
        }

        if ("isInvoiceRelatedToCreditNote".equals(getOp())) {
            Integer invoiceId = EJBCommandUtil.i.getValueAsInteger(this, "invoiceId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            isInvoiceRelatedToCreditNote(invoiceId, companyId);
        }
    }

    private void isInvoiceRelatedToCreditNote(Integer invoiceId, Integer companyId) {
        Invoice invoice = getInvoice(invoiceId);
        Boolean result = false;

        if (FinanceConstants.InvoiceType.Invoice.equal(invoice.getType())) {
            InvoiceHome invoiceHome =
                    (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);

            try {
                Collection creditNotes = invoiceHome.findByCreditNoteOfId(invoice.getInvoiceId(), companyId);
                result = null != creditNotes && !creditNotes.isEmpty();
            } catch (FinderException e) {
                //
            }
        }
        resultDTO.put("isInvoiceRelatedToCreditNote", result);
    }

    private void canChangeNetGross(Integer invoiceId,
                                   Integer companyId,
                                   Integer netGross,
                                   boolean validatePayments) {
        InvoicePositionHome invoicePositionHome =
                (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);

        Collection elements = null;

        if (FinanceConstants.NetGrossFLag.NET.equal(netGross)) {
            try {

                elements = invoicePositionHome.findByUnitPriceIsNull(invoiceId, companyId);
            } catch (FinderException e) {
                log.debug("-> Execute InvoicePositionHome.findByUnitPriceIsNull FAIL");
            }
        }

        if (FinanceConstants.NetGrossFLag.GROSS.equal(netGross)) {
            try {
                elements = invoicePositionHome.findByUnitPriceGrossIsNull(invoiceId, companyId);
            } catch (FinderException e) {
                log.debug("-> Execute InvoicePositionHome.findByUnitPriceIsNull FAIL");
            }
        }

        if (null != elements && !elements.isEmpty()) {
            resultDTO.put("errorKey", "Invoice.netGrossChange.invalidInvoicePosition.error");
            resultDTO.setResultAsFailure();
            return;
        }

        if (!validatePayments) {
            return;
        }

        Invoice invoice = getInvoice(invoiceId);
        BigDecimal totalPaid = InvoiceUtil.i.getTotalPaid(invoiceId, companyId);
        BigDecimal totalAmountGross = new BigDecimal(0);

        if (FinanceConstants.NetGrossFLag.NET.equal(netGross)) {
            totalAmountGross = InvoiceUtil.i.calculateTotalAmountGrossForNetPrices(invoice);
        }

        if (FinanceConstants.NetGrossFLag.GROSS.equal(netGross)) {
            totalAmountGross = InvoiceUtil.i.calculateTotalAmountGrossForGrossPrices(invoice);
        }

        if (totalPaid.compareTo(totalAmountGross) == 1) {
            resultDTO.put("errorKey", "Invoice.netGrossChange.invoicePayments.error");
            resultDTO.setResultAsFailure();
        }


    }

    private void getInvoiceDTO(Integer invoiceId) {
        Invoice invoice = getInvoice(invoiceId);
        if (null == invoice) {
            return;
        }

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        DTOFactory.i.copyToDTO(invoice, invoiceDTO);
        invoiceDTO.put("haveInvoicePayments",
                null != invoice.getInvoicePayment() && !invoice.getInvoicePayment().isEmpty());
        resultDTO.put("getInvoice", invoiceDTO);

        if (FinanceConstants.InvoiceType.CreditNote.equal(invoice.getType()) &&
                null != invoice.getCreditNoteOfId()) {
            Collection creditNotePayments = invoice.getInvoicePayment();
            if (creditNotePayments.size() == 0) {
                return;
            }

            InvoicePayment creditNotePayment = (InvoicePayment) creditNotePayments.toArray()[0];
            invoiceDTO.put("actualCreditNotePayment", creditNotePayment.getAmount());
        }
    }

    private void isCreditNote(Integer invoiceId) {
        Invoice invoice = getInvoice(invoiceId);
        if (null == invoice) {
            log.debug("-> Execute  getInvoice(" +
                    invoiceId +
                    ") FAIL, Related invoice to creditNote not Found");
            resultDTO.setResultAsFailure();
            return;
        }

        if (FinanceConstants.InvoiceType.Invoice.equal(invoice.getType())) {
            resultDTO.put("isCreditNote", false);
            InvoiceDTO invoiceDTO = new InvoiceDTO();
            DTOFactory.i.copyToDTO(invoice, invoiceDTO);
            resultDTO.put("invoiceDTO", invoiceDTO);
            return;
        }

        if (null != invoice.getCreditNoteOfId()) {
            Invoice relatedInvoice = getInvoice(invoice.getCreditNoteOfId());
            if (null != relatedInvoice) {
                InvoiceDTO invoiceDTO = new InvoiceDTO();
                DTOFactory.i.copyToDTO(relatedInvoice, invoiceDTO);
                resultDTO.put("invoiceDTO", invoiceDTO);
            }
        }


        InvoiceDTO creditNoteDTO = new InvoiceDTO();
        DTOFactory.i.copyToDTO(invoice, creditNoteDTO);

        resultDTO.put("isCreditNote", true);
        resultDTO.put("creditNoteDTO", creditNoteDTO);
    }

    private Invoice getInvoice(Integer invoiceId) {
        InvoiceHome invoiceHome =
                (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
        try {
            return invoiceHome.findByPrimaryKey(invoiceId);
        } catch (FinderException e) {
            log.debug("-> Execute InvoiceHome.findByPrimaryKey(" + invoiceId + ") FAIL");
        }

        return null;
    }

    private InvoiceDTO findInvoiceByNumber(String number, Integer companyId) {
        InvoiceHome invoiceHome =
                (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
        try {
            Invoice invoice = invoiceHome.findByNumber(number, companyId);
            InvoiceDTO invoiceDTO = new InvoiceDTO();
            DTOFactory.i.copyToDTO(invoice, invoiceDTO);
            invoiceDTO.put("haveInvoicePayments",
                    null != invoice.getInvoicePayment() && !invoice.getInvoicePayment().isEmpty());
            resultDTO.put("findInvoiceByNumber", invoiceDTO);
            return invoiceDTO;
        } catch (FinderException e) {
            log.debug("-> findByNumber number=" + number + " companyId=" + companyId + " FAIL");
        }
        resultDTO.put("findInvoiceByNumber", null);
        return null;
    }

    public boolean isStateful() {
        return false;
    }
}
