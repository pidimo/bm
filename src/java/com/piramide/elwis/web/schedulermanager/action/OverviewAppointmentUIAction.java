package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.cmd.schedulermanager.LoadOverviewAppointmentsCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 * Action to manage appointments overview of other user
 *
 * @author Miky
 * @version $Id: OverviewAppointmentUIAction.java 9996 2010-10-11 18:37:10Z ivan $
 */
public class OverviewAppointmentUIAction extends AbstractAppointmentUIAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue(Constants.USERID);
        user.setValue("schedulerUserId", userId);

        //validate configuration params to overview
        Map userSchedulerMap = (Map) user.getValue("overviewSchedulerMap");
        if (userSchedulerMap == null) {
            ActionErrors errors = new ActionErrors();
            errors.add("expired", new ActionError("Scheduler.overviewCalendar.expired"));
            saveErrors(request, errors);
            return new ActionForwardParameters().add("schedulerUserId", userId.toString())
                    .forward(mapping.findForward("fail"));
        }

        ActionForward forward = super.execute(mapping, form, request, response);
        if (forward != null && !"fail".equals(forward.getName())) {
            request.setAttribute("viewSharedCalendar", SchedulerConstants.OVERVIEW_SHAREDCALENDARS);
        }

        return forward;
    }

    protected Map<String, Object> getAppointmentData(DateTime startRangeDate, DateTime endRangeDate, DateTimeZone timeZone, boolean isYearlyView, Integer schedulerUserId, HttpServletRequest request) throws Exception {

        String appointmentTypeId = null;
        List overviewUserIdList = new ArrayList();

        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue("userId");
        Map userSchedulerMap = (Map) user.getValue("overviewSchedulerMap");
        if (userSchedulerMap != null) {
            overviewUserIdList = (List) userSchedulerMap.get("overviewUserList");
            if (userSchedulerMap.containsKey("appointmentTypeId")) {
                appointmentTypeId = (String) userSchedulerMap.get("appointmentTypeId");
            }
        }

        LoadOverviewAppointmentsCmd loadOverviewAppCmd = new LoadOverviewAppointmentsCmd(isYearlyView);
        loadOverviewAppCmd.putParam("startRangeDate", startRangeDate);
        loadOverviewAppCmd.putParam("endRangeDate", endRangeDate);
        loadOverviewAppCmd.putParam("userId", userId);
        loadOverviewAppCmd.putParam("overviewUserIdList", overviewUserIdList);
        if (appointmentTypeId != null) {
            loadOverviewAppCmd.putParam("appointmentTypeId", appointmentTypeId);
        }

        BusinessDelegate.i.execute(loadOverviewAppCmd, null);

        Map<String, Object> appointmentDataMap = new HashMap<String, Object>();
        appointmentDataMap.put(APPOINTMENT_IN_RANGE, loadOverviewAppCmd.getAppointmentsByRange());
        appointmentDataMap.put(APPOINTMENTS_DATA, loadOverviewAppCmd.getAppointmentList());
        appointmentDataMap.put(APPOINTMENTS_YEAR, loadOverviewAppCmd.getAppointments());

        return appointmentDataMap;
    }
}
