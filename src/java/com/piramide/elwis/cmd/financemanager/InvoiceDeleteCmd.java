package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.dto.financemanager.InvoiceDTO;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceDeleteCmd extends EJBCommand {
    private Log log = LogFactory.getLog(InvoiceDeleteCmd.class);

    public void executeInStateless(SessionContext ctx) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.put("invoiceId", getValueAsInteger("invoiceId"));
        invoiceDTO.put("number", paramDTO.get("number"));

        Invoice invoice = (Invoice) ExtendedCRUDDirector.i.read(invoiceDTO, resultDTO, true);
        if (null == invoice) {
            resultDTO.setForward("Fail");
            return;
        }

        deleteInvoicePositions(invoice, ctx);
        deleteInvoiceVats(invoice);
        deleteInvoicePayments(invoice);
        deleteInvoiceReminders(invoice);
        deleteCreditNotePayments(invoice, ctx);

        if (null != invoice.getSequenceRuleId()) {
            SequenceRule sequenceRule = InvoiceUtil.i.getSequenceRule(invoice.getSequenceRuleId());
            if (null != sequenceRule) {
                InvoiceUtil.i.setInvoiceFreeNumber(sequenceRule, invoice.getInvoiceDate(), invoice.getRuleNumber());
            }
        }

        try {
            invoice.remove();
        } catch (RemoveException e) {
            log.error("-> Delete Invoice invoiceId=" + invoice.getInvoiceId() + " FAIL", e);
        }

    }

    private void deleteCreditNotePayments(Invoice invoice, SessionContext ctx) {
        InvoicePaymentCmd invoicePaymentCmd = new InvoicePaymentCmd();
        invoicePaymentCmd.setOp("deleteCreditNotePayments");
        invoicePaymentCmd.putParam("invoiceId", invoice.getInvoiceId());
        invoicePaymentCmd.putParam("invoicePaymentMessage", paramDTO.get("invoicePaymentMessage"));
        invoicePaymentCmd.executeInStateless(ctx);
    }

    private void deleteInvoiceReminders(Invoice invoice) {
        InvoiceReminderHome invoiceReminderHome =
                (InvoiceReminderHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEREMINDER);

        Collection invoiceReminders = new ArrayList();
        try {
            invoiceReminders = invoiceReminderHome.findByInvoiceId(invoice.getInvoiceId(), invoice.getCompanyId());
        } catch (FinderException e) {
            log.debug("-> Read InvoiceReminders invoiceId=" + invoice.getInvoiceId() + " FAIL");
        }

        for (Object object : invoiceReminders) {
            InvoiceReminder invoiceReminder = (InvoiceReminder) object;
            try {
                invoiceReminder.remove();
            } catch (RemoveException e) {
                log.error("-> Delete InvoiceReminder reminderId=" +
                        invoiceReminder.getReminderId() + " FAIL");
            }
        }
    }

    private void deleteInvoiceVats(Invoice invoice) {
        InvoiceVatHome invoiceVatHome =
                (InvoiceVatHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEVAT);

        Collection invoiceVats = new ArrayList();
        try {
            invoiceVats =
                    invoiceVatHome.findByInvoiceId(invoice.getInvoiceId(), invoice.getCompanyId());
        } catch (FinderException e) {
            log.debug("-> Read InvoiceVats invoiceId=" + invoice.getInvoiceId() + " FAIL");
        }

        for (Object object : invoiceVats) {
            InvoiceVat invoiceVat = (InvoiceVat) object;
            try {
                invoiceVat.remove();
            } catch (RemoveException e) {
                log.error("-> Delete InvoiceVat invoiceId=" +
                        invoice.getInvoiceId() + " FAIL");
            }

        }
    }

    private void deleteInvoicePositions(Invoice invoice, SessionContext ctx) {
        Integer companyId = invoice.getCompanyId();

        InvoicePositionHome invoicePositionHome =
                (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);

        List invoicePositions = new ArrayList();
        try {
            invoicePositions =
                    (List) invoicePositionHome.findByInvoiceId(
                            invoice.getInvoiceId(),
                            companyId);

        } catch (FinderException e) {
            log.debug("-> Read InvoicePositions invoiceId=" + invoice.getInvoiceId() + " FAIL");
        }

        List<Integer> contractIdToUpdate = new ArrayList<Integer>();
        for (int i = 0; i < invoicePositions.size(); i++) {
            InvoicePosition invoicePosition = (InvoicePosition) invoicePositions.get(i);
            Integer contractId = invoicePosition.getContractId();
            try {
                invoicePosition.remove();
                if (null != contractId) {
                    contractIdToUpdate.add(contractId);
                }
            } catch (RemoveException e) {
                log.error("Cannot delete InvoicePosition invoicePositionId=" +
                        invoicePosition.getPositionId(), e);
            }
        }

        log.debug("Update Open Amount for the next ProductContracts:\n" + contractIdToUpdate);
        for (Integer contractId : contractIdToUpdate) {
            updateProductContract(contractId, companyId, ctx);
        }
    }

    private void updateProductContract(Integer contractId, Integer companyId, SessionContext ctx) {
        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.setOp("updateProductContractOpenAmount");
        invoicePositionCmd.putParam("contractId", contractId);
        invoicePositionCmd.putParam("companyId", companyId);
        invoicePositionCmd.executeInStateless(ctx);
    }

    private void deleteInvoicePayments(Invoice invoice) {
        InvoicePaymentHome invoicePaymentHome =
                (InvoicePaymentHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPAYMENT);
        Collection invoicePayments = new ArrayList();
        try {
            invoicePayments = invoicePaymentHome.findByInvoiceId(invoice.getInvoiceId(), invoice.getCompanyId());
        } catch (FinderException e) {
            log.debug("-> Read InvoicePayments invoiceId=" + invoice.getInvoiceId() + " FAIL");
        }

        for (Object object : invoicePayments) {
            InvoicePayment invoicePayment = (InvoicePayment) object;
            try {
                invoicePayment.remove();
            } catch (RemoveException e) {
                log.error("-> Delete InvoicePayment paymentId=" +
                        invoicePayment.getPaymentId() + " FAIL");
            }
        }
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

    private Boolean canDeleteInvoicePositions(Invoice invoice, SessionContext ctx) {
        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.setOp("canDeleteInvoicePositionFromCreditNote");
        invoicePositionCmd.putParam("invoiceId", invoice.getInvoiceId());
        invoicePositionCmd.putParam("companyId", invoice.getCompanyId());
        invoicePositionCmd.executeInStateless(ctx);

        return (Boolean) invoicePositionCmd.getResultDTO().get("canDeleteInvoicePositionFromCreditNote");
    }

    public boolean isStateful() {
        return false;
    }
}
