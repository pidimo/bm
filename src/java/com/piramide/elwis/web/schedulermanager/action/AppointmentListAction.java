package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.dto.schedulermanager.AppointmentDTO;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.utils.SchedulerPermissionUtil;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.schedulermanager.el.Functions;
import com.piramide.elwis.web.schedulermanager.form.ParticipantForm;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.struts.action.*;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 29, 2005
 * Time: 11:17:38 AM
 * To change this template use File | Settings | File Templates.
 */

public class AppointmentListAction extends com.piramide.elwis.web.common.action.ListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if ("Cancel".equals(request.getParameter("dto(cancel)"))) {
            log.debug("cancel  ....  AppointmentListAction");
            return mapping.findForward("Cancel");
        }
        log.debug("Appointment list action execution...");
        //cheking if working address was not deleted by other user.

        log.debug("AppointmentId for user session  = " + request.getParameter("appointmentId"));
        ActionErrors errors = new ActionErrors();
        ParticipantForm listForm = (ParticipantForm) form;
        String appointmentId = null;
        if (request.getParameter("appointmentId") != null && !"".equals(request.getParameter("appointmentId"))) {
            appointmentId = request.getParameter("appointmentId");
        } else {
            appointmentId = request.getParameter("dto(appointmentId)");
        }

        if (appointmentId != null) {
            AppointmentDTO appointmentDTO = new AppointmentDTO();
            appointmentDTO.setPrimKey(appointmentId);
            try {
                EJBFactory.i.findEJB(appointmentDTO); //appointment already exists
            } catch (EJBFactoryException e) {
                if (e.getCause() instanceof FinderException) {
                    log.debug("The appointment was deleted by other user... show errors and " +
                            "return to appointmentList search page");
                    errors.add("appointment.NotFound", new ActionError("appointment.NotFound"));
                    saveErrors(request, errors);
                    return new ActionForwardParameters().add("simple", "true")
                            .forward(mapping.findForward("MainSearch"));
                }
            }
        } else { //if appointmentId not found
            errors.add("appointment.NotFound", new ActionError("appointment.NotFound"));
            saveErrors(request, errors);
            return new ActionForwardParameters().add("simple", "true")
                    .forward(mapping.findForward("MainSearch"));
        }

        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue("userId");
        Integer schedulerUserId = (Integer) user.getValue("schedulerUserId");
        String paramSchedulerUserId = request.getParameter("schedulerUserId");

        log.debug("UserId:" + userId);
        log.debug("schedulerUserId:" + schedulerUserId);
        log.debug("ParamschedulerUserId:" + paramSchedulerUserId);

        if (schedulerUserId == null) {//Para la primera vez que entra...
            schedulerUserId = userId;
            user.setValue("schedulerUserId", userId);
        } else if (paramSchedulerUserId != null) { // Cuando se este viendo el calendario de otro usuario
            log.debug("Is other user");
            try {
                schedulerUserId = new Integer((String) paramSchedulerUserId);
            } catch (NumberFormatException e) {
                schedulerUserId = userId;
            }
            if (userId.equals(schedulerUserId)) {
                user.setValue("schedulerUserId", schedulerUserId);
            }
        }
        if (!userId.equals(schedulerUserId)) {
            log.debug("Permission from user:" + schedulerUserId);
            Byte publicAppPermission = Functions.getPublicPermission(schedulerUserId, userId);
            Byte privateAppPermission = Functions.getPrivatePermission(schedulerUserId, userId);

            if (SchedulerPermissionUtil.hasPermissions(publicAppPermission)
                    || SchedulerPermissionUtil.hasPermissions(privateAppPermission)) {
                request.setAttribute("publicSchedulerUserPermission", publicAppPermission);
                request.setAttribute("privateSchedulerUserPermission", privateAppPermission);
                //Only set SchedulerUserId if change user with select box
                if (schedulerUserId.toString().equals(paramSchedulerUserId)) {
                    user.setValue("schedulerUserId", schedulerUserId);
                }
            } else {
                user.setValue("schedulerUserId", userId);
                errors = new ActionErrors();
                errors.add("VirtualProperty", new ActionError("scheduler.errors.hasNotPermission"));
                saveErrors(request, errors);
                return mapping.findForward("fail");
            }
        }

        log.debug("Final AddSeach:");
        listForm.getParams().put("appointmentId", appointmentId);
        addFilter("viewerUserId", schedulerUserId.toString());
        addFilter("appointmentId", appointmentId);
        addFilter("companyId", user.getValue("companyId").toString());
        log.debug("PARAMS!!!:" + listForm.getParams());

        if (!"on".equals(request.getParameter("parameter(userType)"))) {
            addFilter("externalUserAlso", SchedulerConstants.TRUE_VALUE);
        } else {
            addFilter("externalUserAlso", SchedulerConstants.FALSE_VALUE);
        }
        return super.execute(mapping, listForm, request, response);
    }
}
