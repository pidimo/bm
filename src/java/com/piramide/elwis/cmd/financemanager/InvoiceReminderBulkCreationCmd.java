package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.dto.financemanager.ReminderLevelDTO;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 * Cmd to create bulk invoice reminders and generate your document for this
 *
 * @author Miky
 * @version $Id: InvoiceReminderBulkCreationCmd.java 04-nov-2008 18:23:25 $
 */
public class InvoiceReminderBulkCreationCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing InvoiceReminderBulkCreationCmd..........." + paramDTO);

        List invoiceIds = (List) paramDTO.get("idsSelected");
        InvoiceHome invoiceHome = (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
        Map levelSummaryMap = new HashMap();
        int totalCreated = 0;
        String reminderDocumentIds = null;

        for (Iterator iterator = invoiceIds.iterator(); iterator.hasNext();) {
            Integer invoiceId = new Integer((String) iterator.next());
            Invoice invoice = null;
            try {
                invoice = invoiceHome.findByPrimaryKey(invoiceId);
            } catch (FinderException e) {
                log.debug("Not found invoice......" + invoiceId, e);
                continue;
            }

            Integer reminderLevelId = readNextReminderLevelId(invoice, ctx);
            if (reminderLevelId != null) {
                InvoiceReminder invoiceReminder = createInvoiceReminder(invoice, reminderLevelId, ctx);

                if (invoiceReminder != null) {
                    Integer documentId = generateReminderDocument(invoice, invoiceReminder.getReminderId(), ctx);
                    if (documentId != null) {
                        //add in list summary information
                        if (levelSummaryMap.containsKey(reminderLevelId)) {
                            Map map = (Map) levelSummaryMap.get(reminderLevelId);
                            map.put("total", Integer.parseInt(map.get("total").toString()) + 1);
                        } else {
                            Map map = new HashMap();
                            map.put("levelName", invoiceReminder.getReminderLevel().getName());
                            map.put("total", new Integer(1));

                            levelSummaryMap.put(reminderLevelId, map);
                        }

                        if (reminderDocumentIds == null) {
                            reminderDocumentIds = documentId.toString();
                        } else {
                            reminderDocumentIds += "," + documentId;
                        }
                        totalCreated++;

                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                addFailResultErrorMessage(invoice, null, ctx);
                resultDTO.addResultMessage("Reminder.bulkCreation.hasNotReminderLevel");
                return;
            }
        }

        //add summary in result dto
        resultDTO.put("reminderSummary", levelSummaryMap.values());
        resultDTO.put("totalCreated", totalCreated);
        resultDTO.put("reminderDocumentIds", reminderDocumentIds);
    }

    /**
     * Read the next reminder level for this invoice
     *
     * @param invoice the invoice
     * @param ctx     SessionContext
     * @return Integer, reminder level id
     */
    private Integer readNextReminderLevelId(Invoice invoice, SessionContext ctx) {
        Integer reminderLevelId = null;

        ReminderLevelCmd reminderLevelCmd = new ReminderLevelCmd();
        reminderLevelCmd.putParam("invoiceId", invoice.getInvoiceId());
        reminderLevelCmd.setOp("getNextReminderLevel");

        reminderLevelCmd.executeInStateless(ctx);
        ResultDTO resultDTO = reminderLevelCmd.getResultDTO();
        ReminderLevelDTO reminderLevelDTO = (ReminderLevelDTO) resultDTO.get("getNextReminderLevel");
        if (reminderLevelDTO != null && reminderLevelDTO.get("reminderLevelId") != null) {
            reminderLevelId = new Integer(reminderLevelDTO.get("reminderLevelId").toString());
        } else {
            ReminderLevelHome reminderLevelHome = (ReminderLevelHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_REMINDERLEVEL);
            try {
                ReminderLevel reminderLevel = reminderLevelHome.findByLevel(invoice.getReminderLevel(), invoice.getCompanyId());
                reminderLevelId = reminderLevel.getReminderLevelId();
            } catch (FinderException e) {
                log.debug("Error in find reminder level..." + invoice.getReminderLevel(), e);
            }
        }
        return reminderLevelId;
    }

    /**
     * Create an reminder for this invoice, return null if fail creation and
     * add result error message
     *
     * @param invoice         the invoice
     * @param reminderLevelId remider level id
     * @param ctx             SessionContext
     * @return InvoiceReminder
     */
    private InvoiceReminder createInvoiceReminder(Invoice invoice, Integer reminderLevelId, SessionContext ctx) {
        InvoiceReminder invoiceReminder = null;

        InvoiceReminderCmd invoiceReminderCmd = new InvoiceReminderCmd();
        invoiceReminderCmd.setOp("create");
        invoiceReminderCmd.putParam("reminderLevelId", reminderLevelId);
        invoiceReminderCmd.putParam("date", paramDTO.get("date"));
        invoiceReminderCmd.putParam("companyId", invoice.getCompanyId());
        invoiceReminderCmd.putParam("invoiceId", invoice.getInvoiceId());

        invoiceReminderCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = invoiceReminderCmd.getResultDTO();

        if (myResultDTO.isFailure()) {
            addFailResultErrorMessage(invoice, myResultDTO, ctx);
        } else {
            Integer reminderId = new Integer(myResultDTO.get("reminderId").toString());
            InvoiceReminderHome invoiceReminderHome = (InvoiceReminderHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEREMINDER);
            try {
                invoiceReminder = invoiceReminderHome.findByPrimaryKey(reminderId);
            } catch (FinderException e) {
                log.debug("Not found invoice reminder:" + reminderId, e);
                addFailResultErrorMessage(invoice, null, ctx);
            }
        }
        return invoiceReminder;
    }

    /**
     * Generate reminder document for this invoice, return null if generation is fail
     * and add result message
     *
     * @param invoice    the invoice
     * @param reminderId remider id
     * @param ctx        SessionContext
     * @return documentId, generated document id
     */
    private Integer generateReminderDocument(Invoice invoice, Integer reminderId, SessionContext ctx) {
        Integer documentId = null;

        InvoiceReminderDocumentCmd reminderDocumentCmd = new InvoiceReminderDocumentCmd();
        reminderDocumentCmd.putParam("reminderId", reminderId);
        reminderDocumentCmd.putParam("userId", paramDTO.get("userId"));
        reminderDocumentCmd.putParam("companyId", invoice.getCompanyId());
        reminderDocumentCmd.putParam("userAddressId", paramDTO.get("userAddressId"));
        reminderDocumentCmd.putParam("requestLocale", paramDTO.get("requestLocale"));
        reminderDocumentCmd.putParam("reminderLabel", paramDTO.get("reminderLabel"));

        reminderDocumentCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = reminderDocumentCmd.getResultDTO();
        log.debug("RES:" + myResultDTO);

        if (myResultDTO.isFailure()) {
            addFailResultErrorMessage(invoice, myResultDTO, ctx);
        } else {
            documentId = new Integer(myResultDTO.get("documentId").toString());
        }

        return documentId;
    }

    private void addFailResultErrorMessage(Invoice invoice, ResultDTO myResultDTO, SessionContext ctx) {
        resultDTO.setResultAsFailure();
        resultDTO.setForward("Fail");

        resultDTO.addResultMessage("Invoice.bulkReminder.createError", invoice.getNumber());

        if (myResultDTO != null && myResultDTO.hasResultMessage()) {
            for (Iterator iterator = myResultDTO.getResultMessages(); iterator.hasNext();) {
                ResultMessage resultMessage = (ResultMessage) iterator.next();
                resultDTO.addResultMessage(resultMessage);
            }
        }

        //set rollback
        ctx.setRollbackOnly();
    }

    public boolean isStateful() {
        return false;
    }

}
