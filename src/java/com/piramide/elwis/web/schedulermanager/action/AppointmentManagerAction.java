package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.AbstractDefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.schedulermanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 28, 2005
 * Time: 2:13:40 PM
 * To change this template use File | Settings | File Templates.
 */

public class AppointmentManagerAction extends AbstractDefaultAction {

    /**
     * Check if appointmentId exists, if exists continue, otherwise cancel operation and return appointment page
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("AppointmentManagerAction execution  ...");
        ActionErrors errors = new ActionErrors();

        if (isCancelled(request)) {
            log.debug("action was canceled");
            return (mapping.findForward("Cancel"));
        }
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userId = (Integer) user.getValue("userId");
        Integer schedulerUserId = (Integer) user.getValue("schedulerUserId");

        log.debug("schedulerUserId:" + schedulerUserId);
        if (schedulerUserId == null) {
            schedulerUserId = userId;
        }

        errors = new ActionErrors();
        String appointmentId = "";

        if (request.getParameter("appointmentId") != null && !"".equals(request.getParameter("appointmentId"))) {
            appointmentId = request.getParameter("appointmentId");
        } else {
            appointmentId = request.getParameter("dto(appointmentId)");
        }

        errors = ForeignkeyValidator.i.validate(SchedulerConstants.TABLE_APPOINTMENT, "appointmentid",
                appointmentId, errors, new ActionError("msg.NotFound",
                        request.getParameter("dto(title)")));
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return new ActionForwardParameters().add("simple", "true")
                    .forward(mapping.findForward("MainSearch"));
        }

        //check user permission if is necessary
        if (!userId.equals(schedulerUserId)) {
            if (!Functions.hasEditAppointmentPermission(schedulerUserId, userId, new Integer(appointmentId))) {
                errors = new ActionErrors();
                errors.add("permission", new ActionError("scheduler.permission.error", JSPHelper.getMessage(request, "Scheduler.grantAccess.edit")));
                saveErrors(request.getSession(), errors);
                return new ActionForwardParameters().add("simple", "true")
                        .forward(mapping.findForward("MainSearch"));
            }
        }


        ((DefaultForm) form).setDto("companyId", user.getValue(Constants.COMPANYID).toString());
        ((DefaultForm) form).setDto("appointmentId", appointmentId);

        ActionForward forward = super.execute(mapping, form, request, response);
        String backViewType = request.getParameter("back");

        if ("Success".equals(forward.getName()) && backViewType != null && backViewType.length() > 0) {
            forward = new ActionForward("/AppointmentView.do?type=" + backViewType);
        }

        return forward;
    }
}