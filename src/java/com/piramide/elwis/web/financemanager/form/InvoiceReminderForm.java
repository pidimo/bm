package com.piramide.elwis.web.financemanager.form;

import com.piramide.elwis.cmd.financemanager.InvoiceReminderCmd;
import com.piramide.elwis.cmd.financemanager.ReminderLevelCmd;
import com.piramide.elwis.dto.financemanager.ReminderLevelDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceReminderForm extends DefaultForm {
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        if (!Functions.existsInvoice(getDto("invoiceId"))) {
            return new ActionErrors();
        }

        ActionErrors errors = super.validate(mapping, request);
        if ("create".equals(getDto("op"))) {
            ActionError levelError = validateLastReminderLevel(request, errors);
            if (null != levelError) {
                errors.add("levelError", levelError);
            }
        }


        //this always to the end
        if (errors.isEmpty() && showGenerateConfirmation(request)) {
            errors.add("confirmation", new ActionError("InvoiceReminder.generate.confirmation"));
        }
        return errors;
    }

    private ActionError validateLastReminderLevel(HttpServletRequest request, ActionErrors errors) {
        if (!errors.isEmpty()) {
            return null;
        }

        Integer invoiceId = Integer.valueOf(getDto("invoiceId").toString());
        Integer reminderLevelId = Integer.valueOf(getDto("reminderLevelId").toString());

        ReminderLevelCmd reminderLevelCmd = new ReminderLevelCmd();

        //read actual level
        reminderLevelCmd.setOp("read");
        reminderLevelCmd.putParam("reminderLevelId", reminderLevelId);
        Integer actualLevel = 0;

        Integer invoiceLevel = 0;

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(reminderLevelCmd, request);
            actualLevel = (Integer) resultDTO.get("level");
        } catch (AppLevelException e) {
            log.error("-> Execute " + ReminderLevelCmd.class.getName() + " FAIL", e);
        }

        //read next reminder level for invoice
        reminderLevelCmd = new ReminderLevelCmd();
        reminderLevelCmd.putParam("invoiceId", invoiceId);
        reminderLevelCmd.setOp("getNextReminderLevel");
        ResultDTO resultDTO;
        ReminderLevelDTO reminderLevelDTO = new ReminderLevelDTO();
        try {
            resultDTO = BusinessDelegate.i.execute(reminderLevelCmd, request);
            invoiceLevel = (Integer) resultDTO.get("invoiceLevel");
        } catch (AppLevelException e) {
            log.error("-> Execute " + ReminderLevelCmd.class.getName() + " FAIL", e);
        }

        if (actualLevel < invoiceLevel) {
            return new ActionError("ReminderLevel.nextLevelChanged", invoiceLevel);
        }

        return null;
    }

    private ActionError validateLevel(HttpServletRequest request) {

        User user = RequestUtils.getUser(request);

        Integer invoiceId = Integer.valueOf(getDto("invoiceId").toString());

        InvoiceReminderCmd invoiceReminderCmd = new InvoiceReminderCmd();
        invoiceReminderCmd.putParam("invoiceId", invoiceId);
        invoiceReminderCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        invoiceReminderCmd.putParam("level", Integer.valueOf(getDto("level").toString()));
        invoiceReminderCmd.setOp("hasTopLevelReminders");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoiceReminderCmd, request);
            boolean hasTopLevelReminders = (Boolean) resultDTO.get("hasTopLevelReminders");
            if (hasTopLevelReminders) {
                return new ActionError("InvoiceReminder.error.hasTopLevelReminders");
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoiceReminderCmd.class.getName() + " FAIL", e);
        }

        return null;
    }

    private boolean isGenerateButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("generate");
    }

    /**
     * verifiy if already exist document generated related to this
     *
     * @param request
     * @return boolean
     */
    private boolean showGenerateConfirmation(HttpServletRequest request) {
        boolean res = false;
        if (getDto("reGenerate") == null) {
            if (isGenerateButtonPressed(request) && !GenericValidator.isBlankOrNull((String) getDto("documentId"))) {
                res = true;
                request.setAttribute("msgConfirmation", true);
            }
        }
        return res;
    }

}
