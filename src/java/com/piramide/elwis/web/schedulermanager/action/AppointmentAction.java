package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.utils.SchedulerPermissionUtil;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.schedulermanager.form.AppointmentForm;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @author yumi
 * @version 4.4
 */

public class AppointmentAction extends AppointmentGeneralAction {

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        return null;
    }

    @Override
    protected boolean accessRightsValidatorCondition(Byte publicAppPermission,
                                                     Byte privateAppPermission,
                                                     DefaultForm defaultForm,
                                                     HttpServletRequest request) {
        return SchedulerPermissionUtil.hasPermissions(publicAppPermission)
                || SchedulerPermissionUtil.hasPermissions(privateAppPermission);
    }

    @Override
    protected ActionForward getAccessRightsFailureActionForward(ActionMapping mapping, HttpServletRequest request) {
        return new ActionForwardParameters()
                .add("schedulerUserId", getUserId(request).toString())
                .forward(mapping.findForward("fail"));
    }

    @Override
    protected ActionForward getEntriesLimitFailureActionForward(ActionMapping mapping, HttpServletRequest request) {
        return new ActionForwardParameters()
                .add("simple", "true")
                .forward(mapping.findForward("redirect"));
    }

    @Override
    protected boolean isEnabledEntriesLimitValidation(DefaultForm defaultForm, HttpServletRequest request) {
        return "true".equals(request.getParameter("create"));
    }

    @Override
    protected void preProcessDefaultForm(DefaultForm defaultForm, HttpServletRequest request) {
        AppointmentForm appointmentForm = (AppointmentForm) defaultForm;

        if ("true".equals(request.getParameter("create"))) {
            appointmentForm.getDtoMap().put("reminderType", "1");
            appointmentForm.setDto("reminder", new Boolean(false));
            appointmentForm.setDto("participa", new Boolean(true));
            appointmentForm.setDto("isPrivate", new Boolean(false));
            appointmentForm.setDto("exceptionValue", new ArrayList(0));
            appointmentForm.setDto("rangeType", new Integer(1));
            appointmentForm.setDto("recurEveryDay", new Integer(1));
            appointmentForm.setDto("recurEveryWeek", new Integer(1));
            appointmentForm.setDto("recurEveryMonth", new Integer(1));
            appointmentForm.setDto("recurEveryYear", new Integer(1));
            appointmentForm.setDto("disablePicker", true);
            appointmentForm.setDto("isAllDay", new Boolean(false));

            request.setAttribute("jsLoad",
                    "onLoad=\"generalDisable(" + false + "," + false + "," + appointmentForm.getDto("rangeType") + ")\"");

            request.setAttribute("currentDateTimeZone", getDateTimeZone(request));
        }

        if ("true".equals(request.getParameter("fail"))) {
            boolean allDay = "on".equals(request.getParameter("dto(isAllDay)"));
            boolean reminderRange = SchedulerConstants.TRUE_VALUE.equals(request.getParameter("dto(reminder)"));

            if ("on".equals(request.getParameter("dto(isRecurrence)"))) {
                appointmentForm.setDto("isRecurrence", new Boolean(true));
            }

            if ("1".equals(request.getParameter("dto(reminderType)"))) {
                appointmentForm.setDto("timeBefore_1", request.getParameter("dto(timeBefore_1)"));
            } else {
                appointmentForm.setDto("timeBefore_2", request.getParameter("dto(timeBefore_2)"));
            }

            appointmentForm.setDto("reminderType", request.getParameter("dto(reminderType)"));
            request.setAttribute("jsLoad",
                    "onLoad=\"generalDisable(" + allDay + "," + reminderRange + "," + appointmentForm.getDto("rangeType") + ")\"");
        }
    }
}