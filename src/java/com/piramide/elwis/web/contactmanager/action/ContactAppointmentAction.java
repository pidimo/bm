package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.contactmanager.el.Functions;
import com.piramide.elwis.web.schedulermanager.action.AppointmentReminderAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ivan Alban
 * @version 4.3.7
 */
public class ContactAppointmentAction extends AppointmentReminderAction {

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        if (!Functions.existsAddress(request.getParameter("contactId"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("general", new ActionError("Address.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        ActionForward superValidationForward = super.validateElementExistence(request, mapping);
        if (null != superValidationForward) {
            return mapping.findForward("Fail");
        }

        return superValidationForward;
    }

    @Override
    protected void preProcessDefaultForm(DefaultForm defaultForm, HttpServletRequest request) {
        defaultForm.setDto("userId", findSchedulerUserId(request));
        super.preProcessDefaultForm(defaultForm, request);
    }

    @Override
    protected Integer getSchedulerUserId(HttpServletRequest request) {
        return findSchedulerUserId(request);
    }

    private Integer findSchedulerUserId(HttpServletRequest request) {
        DefaultForm defaultForm = (DefaultForm) request.getAttribute("appointmentForm");
        if (isCreate(request)) {
            if (null != defaultForm.getDto("calendarOf")) {
                return Integer.valueOf(defaultForm.getDto("calendarOf").toString());
            }

            return getUserId(request);
        } else {
            return Integer.valueOf(defaultForm.getDto("userId").toString());
        }
    }
}
