package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.cmd.schedulermanager.LoadAppointmentsCmd;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.utils.SchedulerPermissionUtil;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.schedulermanager.el.Functions;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * The UI appointment view manager
 * <p/>
 *
 * @author alejandro
 */

public class AppointmentUIAction extends AbstractAppointmentUIAction {
    private String viewAppointment = SchedulerConstants.PROCESS_ALL_APP;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if ("true".equals(request.getParameter("invalid"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("VirtualProperty", new ActionError("scheduler.errors.hasNotPermission"));
            return mapping.findForward("daily");
        }

        return super.execute(mapping, form, request, response);
    }

    @Override
    protected Map<String, Object> getAppointmentData(DateTime startRangeDate,
                                                     DateTime endRangeDate,
                                                     DateTimeZone timeZone,
                                                     boolean isYearlyView,
                                                     Integer schedulerUserId,
                                                     HttpServletRequest request) throws Exception {

        LoadAppointmentsCmd loadAppointmentsCmd = new LoadAppointmentsCmd(isYearlyView);
        if (isYearlyView) {
            loadAppointmentsCmd.initialize(true);
        }

        loadAppointmentsCmd.putParam("startRangeDate", startRangeDate);
        loadAppointmentsCmd.putParam("endRangeDate", endRangeDate);
        loadAppointmentsCmd.putParam("userId", schedulerUserId);
        loadAppointmentsCmd.putParam("timeZone", timeZone);
        loadAppointmentsCmd.putParam("viewAppointment", viewAppointment);
        BusinessDelegate.i.execute(loadAppointmentsCmd, request);

        Map<String, Object> appointmentDataMap = new HashMap<String, Object>();
        appointmentDataMap.put(APPOINTMENT_IN_RANGE, loadAppointmentsCmd.getAppointmentsByRange());
        appointmentDataMap.put(APPOINTMENTS_DATA, loadAppointmentsCmd.getAppointmentList());
        appointmentDataMap.put(APPOINTMENTS_YEAR, loadAppointmentsCmd.getAppointments());

        return appointmentDataMap;
    }

    @Override
    protected void initializeSchedulerUserId(HttpServletRequest request) {
        String paramSchedulerUserId = request.getParameter("schedulerUserId");
        Integer userId = getUserId(request);
        Integer schedulerUserId = getSchedulerUserId(request);

        //Process the calendar view type
        String typeStr = request.getParameter("type");

        if (schedulerUserId == null || typeStr == null) {//for the first execution
            schedulerUserId = userId;
        } else if (paramSchedulerUserId != null) {//when seen the calendar of another user
            log.debug("We are viewing the calendar of the user: " + paramSchedulerUserId);
            try {
                schedulerUserId = new Integer(paramSchedulerUserId);
            } catch (NumberFormatException e) {
                schedulerUserId = userId;
            }
        }

        User user = RequestUtils.getUser(request);
        user.setValue("schedulerUserId", schedulerUserId);
    }

    @Override
    protected ActionForward accessRightsValidator(ActionMapping mapping, ActionForm form, HttpServletRequest request) {
        Integer userId = getUserId(request);
        Integer schedulerUserId = getSchedulerUserId(request);

        viewAppointment = SchedulerConstants.PROCESS_ALL_APP;

        if (!userId.equals(schedulerUserId)) {
            Byte publicAppPermission = Functions.getPublicPermission(schedulerUserId, userId);
            Byte privateAppPermission = Functions.getPrivatePermission(schedulerUserId, userId);

            if (SchedulerPermissionUtil.hasPermissions(publicAppPermission)
                    && SchedulerPermissionUtil.hasPermissions(privateAppPermission)) {
                viewAppointment = SchedulerConstants.PROCESS_ALL_APP;
            } else if (SchedulerPermissionUtil.hasPermissions(publicAppPermission)) {
                viewAppointment = SchedulerConstants.PROCESS_ONLYPUBLIC_APP;
            } else if (SchedulerPermissionUtil.hasPermissions(privateAppPermission)) {
                viewAppointment = SchedulerConstants.PROCESS_ONLYPRIVATE_APP;
            } else {
                //The logged user has not permission to view the calendar of other user
                ActionErrors errors = new ActionErrors();
                errors.add("VirtualProperty", new ActionError("scheduler.errors.hasNotPermission"));
                saveErrors(request, errors);

                return new ActionForwardParameters().add("schedulerUserId", userId.toString())
                        .forward(mapping.findForward("fail"));
            }
        }

        return null;
    }
}
