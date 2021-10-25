package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.contactmanager.el.Functions;
import com.piramide.elwis.web.schedulermanager.action.AppointmentReadAction;
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
public class ContactAppointmentReadAction extends AppointmentReadAction {
    @Override
    protected Integer getSchedulerUserId(HttpServletRequest request) {
        DefaultForm defaultForm = (DefaultForm) request.getAttribute("appointmentForm");

        Object value = defaultForm.getDto("userId");
        if (null == value) {
            value = request.getParameter("dto(userId)");
        }

        return Integer.valueOf(value.toString());
    }

    @Override
    protected Integer getViewerUserId(HttpServletRequest request) {
        DefaultForm defaultForm = (DefaultForm) request.getAttribute("appointmentForm");

        Object value = defaultForm.getDto("userId");
        if (null == value) {
            value = request.getParameter("dto(userId)");
        }

        return Integer.valueOf(value.toString());
    }

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
}
