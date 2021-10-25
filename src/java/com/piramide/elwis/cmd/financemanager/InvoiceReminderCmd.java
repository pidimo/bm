package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.dto.financemanager.InvoiceReminderDTO;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * @author: ivan
 * <p/>
 * Jatun S.R.L
 */
public class InvoiceReminderCmd extends EJBCommand {
    private Log log = LogFactory.getLog(InvoiceReminderCmd.class);

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;
        if ("create".equals(getOp())) {
            isRead = false;
            create(getInvoiceReminderDTO());
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update(getInvoiceReminderDTO());
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(getInvoiceReminderDTO());
        }
        if ("existsInvoiceReminders".equals(getOp())) {
            isRead = false;
            Integer companyId = (Integer) paramDTO.get("companyId");
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            existsInvoiceReminders(invoiceId, companyId);
        }
        if ("hasTopLevelReminders".equals(getOp())) {
            isRead = false;
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            Integer level = (Integer) paramDTO.get("level");
            hasTopLevelReminders(invoiceId, companyId, level);
        }
        if (isRead) {
            isRead = false;
            boolean checkReferences = null != paramDTO.get("withReferences") &&
                    "true".equals(paramDTO.get("withReferences").toString().trim());
            read(getInvoiceReminderDTO(), checkReferences);
        }
    }

    private boolean existsInvoiceReminders(Integer invoiceId, Integer companyId) {
        InvoiceReminderHome invoiceReminderHome =
                (InvoiceReminderHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEREMINDER);
        boolean result = false;
        try {
            Collection elements = invoiceReminderHome.findByInvoiceId(invoiceId, companyId);
            result = null != elements && !elements.isEmpty();
        } catch (FinderException e) {
            log.debug("-> Read InvoiceReminders invoiceId=" + invoiceId + " FAIL");
        }
        resultDTO.put("existsInvoiceReminders", result);
        return result;
    }

    private void delete(InvoiceReminderDTO invoiceReminderDTO) {
        InvoiceReminder invoiceReminder =
                (InvoiceReminder) ExtendedCRUDDirector.i.read(invoiceReminderDTO, resultDTO, false);
        if (null == invoiceReminder) {
            resultDTO.setForward("Fail");
            return;
        }

        Integer invoiceId = invoiceReminder.getInvoiceId();
        Integer companyId = invoiceReminder.getCompanyId();

        if (hasTopLevelReminders(invoiceReminder.getInvoiceId(),
                invoiceReminder.getCompanyId(), invoiceReminder.getReminderLevel().getLevel())) {
            resultDTO.addResultMessage("InvoiceReminder.error.hasTopLevelReminders");
            resultDTO.setForward("Fail");
            return;
        }

        ExtendedCRUDDirector.i.delete(invoiceReminderDTO, resultDTO, true, "Fail");

        InvoiceReminder lastInvoiceReminder = getLastInvoiceReminder(invoiceId, companyId);
        if (null != lastInvoiceReminder) {
            Invoice invoice = lastInvoiceReminder.getInvoice();
            //update invoice reminderLevel field
            invoice.setReminderLevel(lastInvoiceReminder.getReminderLevel().getLevel());
            //update invoce reminderDate field
            invoice.setReminderDate(InvoiceUtil.i.updateReminderDate(invoice,
                    lastInvoiceReminder.getDate(),
                    lastInvoiceReminder.getReminderLevel().getNumberOfDays()));
            log.debug("-> Update Invoice with level=" + lastInvoiceReminder.getReminderLevel().getLevel() + " OK");
        } else {
            try {
                Invoice invoice = getInvoice(invoiceId);
                //update invoice reminderLevel null because has not reminders
                invoice.setReminderLevel(null);
                //update invoce reminderDate field
                invoice.setReminderDate(InvoiceUtil.i.getReminderDate(invoice.getPaymentDate()));
            } catch (FinderException e) {
                log.error("-> Read Invoice invoiceId=" + invoiceReminder.getInvoiceId() + " FAIL", e);
            }
        }

    }

    private void create(InvoiceReminderDTO invoiceReminderDTO) {
        String text = (String) paramDTO.get("text");

        FinanceFreeText financeFreeText = null;
        if (null != text &&
                !"".equals(text.trim())) {
            financeFreeText = createFreeText(text,
                    (Integer) invoiceReminderDTO.get("companyId"));
        }

        if (null != financeFreeText) {
            invoiceReminderDTO.put("descriptionId", financeFreeText.getFreeTextId());
        }

        InvoiceReminder invoiceReminder =
                (InvoiceReminder) ExtendedCRUDDirector.i.create(invoiceReminderDTO, resultDTO, false);

        try {
            Invoice invoice = getInvoice(invoiceReminder.getInvoiceId());
            //update invoice reminderLevel field
            invoice.setReminderLevel(invoiceReminder.getReminderLevel().getLevel());
            //update invoce reminderDate field
            invoice.setReminderDate(InvoiceUtil.i.updateReminderDate(invoice,
                    invoiceReminder.getDate(),
                    invoiceReminder.getReminderLevel().getNumberOfDays()));
        } catch (FinderException e) {
            log.error("-> Read Invoice invoiceId=" + invoiceReminder.getInvoiceId() + " FAIL", e);
        }

        //add reminderId in result dto
        resultDTO.put("reminderId", invoiceReminder.getReminderId());

    }

    private void read(InvoiceReminderDTO invoiceReminderDTO, boolean checkReferences) {
        InvoiceReminder invoiceReminder =
                (InvoiceReminder) ExtendedCRUDDirector.i.read(invoiceReminderDTO, resultDTO, checkReferences);
        if (null == invoiceReminder) {
            return;
        }

        if (null != invoiceReminder.getDescriptionId()) {
            resultDTO.put("text", new String(invoiceReminder.getDescription().getValue()));
        }

        //for topLevel InvoiceReminders validation 
        resultDTO.put("level", invoiceReminder.getReminderLevel().getLevel());

        if (checkReferences &&
                hasTopLevelReminders(invoiceReminder.getInvoiceId(),
                        invoiceReminder.getCompanyId(), invoiceReminder.getReminderLevel().getLevel())) {
            resultDTO.addResultMessage("InvoiceReminder.error.hasTopLevelReminders");
            resultDTO.setResultAsFailure();
        }
    }

    private InvoiceReminder getLastInvoiceReminder(Integer invoiceId, Integer companyId) {
        InvoiceReminderHome invoiceReminderHome =
                (InvoiceReminderHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEREMINDER);
        InvoiceReminder lastInvoiceReminder = null;
        try {
            Collection invoiceReminders = invoiceReminderHome.findByInvoiceId(invoiceId, companyId);
            for (Object object : invoiceReminders) {
                InvoiceReminder invoiceReminder = (InvoiceReminder) object;
                if (null == lastInvoiceReminder) {
                    lastInvoiceReminder = invoiceReminder;
                    continue;
                }

                if (invoiceReminder.getReminderLevel().getLevel() > lastInvoiceReminder.getReminderLevel().getLevel()) {
                    lastInvoiceReminder = invoiceReminder;
                }
            }
        } catch (FinderException e) {
            log.debug("-> Read InvoiceReminders invoiceId=" + invoiceId + " FAIL");
        }

        return lastInvoiceReminder;
    }

    private boolean hasTopLevelReminders(Integer invoiceId, Integer companyId, Integer level) {
        InvoiceReminderHome invoiceReminderHome =
                (InvoiceReminderHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEREMINDER);
        boolean result;
        try {
            Collection topLevelReminders = invoiceReminderHome.findByReminderLevel(invoiceId, companyId, level);
            result = null != topLevelReminders && !topLevelReminders.isEmpty();
        } catch (FinderException e) {
            log.debug("-> Read Top level level=" + level + " FAIL");
            result = false;
        }

        resultDTO.put("hasTopLevelReminders", result);
        return result;
    }


    private void update(InvoiceReminderDTO invoiceReminderDTO) {
        InvoiceReminder invoiceReminder =
                (InvoiceReminder) ExtendedCRUDDirector.i.update(invoiceReminderDTO, resultDTO, false, true, true, "Fail");

        //InvoiceReminder was deleted by another user
        if (null == invoiceReminder) {
            return;
        }

        //version error
        if (resultDTO.isFailure()) {
            if (null != invoiceReminder.getDescriptionId()) {
                resultDTO.put("text", new String(invoiceReminder.getDescription().getValue()));
            }
        }

        //update freeText
        String text = (String) paramDTO.get("text");
        if (null == invoiceReminder.getDescriptionId()) {
            if (null != text && !"".equals(text.trim())) {
                FinanceFreeText financeFreeText = createFreeText(text, invoiceReminder.getCompanyId());
                invoiceReminder.setDescriptionId(financeFreeText.getFreeTextId());
            }
        } else if (null != text) {
            invoiceReminder.getDescription().setValue(text.getBytes());
        }
    }

    private InvoiceReminderDTO getInvoiceReminderDTO() {
        InvoiceReminderDTO dto = new InvoiceReminderDTO();
        dto.put("reminderId", getValueAsInteger("reminderId"));
        dto.put("reminderLevelId", getValueAsInteger("reminderLevelId"));
        dto.put("date", getValueAsInteger("date"));
        dto.put("withReferences", paramDTO.get("withReferences"));
        dto.put("companyId", paramDTO.get("companyId"));
        dto.put("invoiceId", getValueAsInteger("invoiceId"));
        dto.put("version", paramDTO.get("version"));
        dto.put("documentId", getValueAsInteger("documentId"));

        //use addNotFoundMsgTo(ResultDTO resultDTO) in InvoiceReminderDTO
        dto.put("remindelLevelName", paramDTO.get("remindelLevelName"));
        log.debug("->Work on " + dto + " OK");
        return dto;
    }

    private FinanceFreeText createFreeText(String text, Integer companyId) {
        FinanceFreeTextHome financeFreeTextHome =
                (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
        FinanceFreeText freeText = null;
        try {
            freeText = financeFreeTextHome.create(text.getBytes(), companyId, FreeTextTypes.FREETEXT_INVOICEREMINDER);
        } catch (CreateException e) {
            log.error("-> Execute FinanceFreeTextHome.create() Fail", e);
        }
        return freeText;
    }

    private Invoice getInvoice(Integer invoiceId) throws FinderException {
        InvoiceHome invoiceHome = getInvoiceHome();
        return invoiceHome.findByPrimaryKey(invoiceId);
    }

    private InvoiceHome getInvoiceHome() {
        return (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
    }

    public boolean isStateful() {
        return false;
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
}
