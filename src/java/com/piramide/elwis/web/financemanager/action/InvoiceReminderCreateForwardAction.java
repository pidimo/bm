package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.cmd.financemanager.ReminderLevelCmd;
import com.piramide.elwis.dto.financemanager.ReminderLevelDTO;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceReminderCreateForwardAction extends ForwardAction {

    private Log log = LogFactory.getLog(InvoiceReminderCreateForwardAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm defaultForm = (DefaultForm) form;
        String invoiceIdAsString = request.getParameter("invoiceId");

        Integer invoiceId = null;
        if (null != invoiceIdAsString &&
                !"".equals(invoiceIdAsString.trim())) {
            try {
                invoiceId = Integer.valueOf(invoiceIdAsString);
            } catch (NumberFormatException e) {
                log.debug("->Parse invoiceId=" + invoiceIdAsString + " FAIL");
            }
        }

        if (null != invoiceId) {
            //read next reminder level for invoice
            readNextReminderLevel(request, defaultForm, invoiceId);

            //build message when InvoiceReminder is valid
            buildReminderMessage(request, invoiceId);
        }

        return super.execute(mapping, defaultForm, request, response);
    }

    private void readNextReminderLevel(HttpServletRequest request,
                                       DefaultForm form,
                                       Integer invoiceId) {
        ReminderLevelCmd reminderLevelCmd = new ReminderLevelCmd();
        reminderLevelCmd.putParam("invoiceId", invoiceId);
        reminderLevelCmd.setOp("getNextReminderLevel");
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(reminderLevelCmd, request);
            ReminderLevelDTO reminderLevelDTO = (ReminderLevelDTO) resultDTO.get("getNextReminderLevel");
            if (null != reminderLevelDTO) {
                form.setDto("reminderLevelId", reminderLevelDTO.get("reminderLevelId"));
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + ReminderLevelCmd.class.getName() + " FAIL", e);
        }
    }

    private void buildReminderMessage(HttpServletRequest request,
                                      Integer invoiceId) {
        ReminderLevelCmd reminderLevelCmd = new ReminderLevelCmd();
        reminderLevelCmd.putParam("invoiceId", invoiceId);
        reminderLevelCmd.setOp("getReminderLevel");

        try {
            Integer actualDate = DateUtils.dateToInteger(new Date());

            ResultDTO resultDTO = BusinessDelegate.i.execute(reminderLevelCmd, request);
            ReminderLevelDTO reminderLevelDTO = (ReminderLevelDTO) resultDTO.get("getReminderLevel");
            Integer reminderDate = (Integer) resultDTO.get("reminderDate");
            Integer invoiceDate = (Integer) resultDTO.get("invoiceDate");

            if (null == invoiceDate && null == reminderDate) {
                return;
            }

            if (reminderDate > actualDate) {
                String invoiceDateFormatted = DateUtils.parseDate(DateUtils.integerToDate(invoiceDate),
                        JSPHelper.getMessage(request, "datePattern"));
                String reminderDateFormatted = DateUtils.parseDate(DateUtils.integerToDate(reminderDate),
                        JSPHelper.getMessage(request, "datePattern"));

                if (null != reminderLevelDTO) {
                    String reminderLevelName = (String) reminderLevelDTO.get("name");
                    ActionErrors errors = new ActionErrors();
                    errors.add("ReminderDateMsg", new ActionError("InvoiceReminder.theDateNotMet",
                            invoiceDateFormatted, reminderLevelName, reminderDateFormatted));
                    saveErrors(request, errors);
                } else {
                    ActionErrors errors = new ActionErrors();
                    errors.add("ReminderDateMsg", new ActionError("InvoiceReminder.theDateNotMetForFirstReminder",
                            invoiceDateFormatted, reminderDateFormatted));
                    saveErrors(request, errors);
                }
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + ReminderLevelCmd.class.getName() + " FAIL", e);
        }
    }
}
