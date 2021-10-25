package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.cmd.schedulermanager.AppointmentReadCmd;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.schedulermanager.form.AppointmentForm;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Sep 7, 2005
 * Time: 3:04:06 PM
 * To change this template use File | Settings | File Templates.
 */

public class AppointmentForwardDeleteAction extends DefaultAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("--------  AppointmentForwardDeleteAction executeFunction  --------");

        AppointmentForm appointmentForm = (AppointmentForm) form;
        ActionForward forward = new ActionForward();
        String type = request.getParameter("type");//tipo de interface que llega: day, week...
        String calendar = request.getParameter("calendar");//fecha en la que se encuentra al appointment.
        String operation = request.getParameter("operation");
        String currentDate = request.getParameter("currentDate");
        String cancel = request.getParameter("redirect");
        ActionForward returnSuccess = mapping.findForward("Cancel");

        if (SchedulerConstants.TRUE_VALUE.equals(cancel)) {
            if (type != null) {
                if (type.equals("1")) {
                    returnSuccess = mapping.findForward("ReturnDay");
                } else if (type.equals("2")) {
                    returnSuccess = mapping.findForward("ReturnWeek");
                } else if (type.equals("3")) {
                    returnSuccess = mapping.findForward("ReturnMonth");
                } else if (type.equals("4")) {
                    returnSuccess = mapping.findForward("ReturnYear");
                }
            }
            return new ActionForwardParameters().add("calendar", calendar)
                    .add("oldType", type)
                    .forward(returnSuccess);
        }

        if (SchedulerConstants.TRUE_VALUE.equals(request.getParameter("isRecurrent"))) {// IU de pregunta, si quiere eliminar la actual recurrencia? o toda la serie

            User user = RequestUtils.getUser(request);
            AppointmentReadCmd cmd = new AppointmentReadCmd();

            if (user.getValue("schedulerUserId") != null) {
                cmd.putParam("viewerUserId", user.getValue("schedulerUserId").toString());
            } else {
                cmd.putParam("viewerUserId", user.getValue("userId"));
            }

            ActionErrors aErrors = new ActionErrors();
            cmd.putParam("appointmentId", new Integer(((DefaultForm) appointmentForm).getDto("appointmentId").toString()));
            cmd.putParam("userSessionId", user.getValue("userId"));
            BusinessDelegate.i.execute(cmd, request);
            aErrors = MessagesUtil.i.convertToActionErrors(mapping, request, cmd.getResultDTO());
            saveErrors(request, aErrors);
            Long startDateTime = null;
            Long endDateTime = null;
            String root = "false";

            if (!cmd.getResultDTO().isFailure()) {
                DateTime dateCurrent = DateUtils.integerToDateTime(new Integer(request.getParameter("currentDate").toString()), (DateTimeZone) user.getValue("dateTimeZone"));
                DateTime start_DateTime = DateUtils.integerToDateTime(new Integer(cmd.getResultDTO().get("startDate").toString()), (DateTimeZone) user.getValue("dateTimeZone"));
                DateTime end_DateTime = DateUtils.integerToDateTime(new Integer(cmd.getResultDTO().get("endDate").toString()), (DateTimeZone) user.getValue("dateTimeZone"));
                Long current_Date = new Long(dateCurrent.getMillis());
                startDateTime = new Long(start_DateTime.getMillis());
                endDateTime = new Long(end_DateTime.getMillis());

                if (current_Date.longValue() >= startDateTime.longValue() && current_Date.longValue() <= endDateTime.longValue()) {
                    root = "true";
                }
            }
            Date d = DateUtils.integerToDate(new Integer(request.getParameter("currentDate").toString()));
            return new ActionForwardParameters().add("appointmentId", ((DefaultForm) appointmentForm).getDto("appointmentId").toString())
                    .add("calendar", calendar)
                    .add("currentDate", currentDate)
                    .add("type", type)
                    .add("operation", operation)
                    .add("root", root)
                    .add("date", DateUtils.parseDate(d, JSPHelper.getMessage((HttpServletRequest) request, "datePattern")))
                    .forward(mapping.findForward("Recurrence"));
        } else if (SchedulerConstants.FALSE_VALUE.equals(request.getParameter("isRecurrent"))) {//a forward delete
            return new ActionForwardParameters().add("appointmentId", ((DefaultForm) appointmentForm).getDto("appointmentId").toString())
                    .add("calendar", calendar)
                    .add("currentDate", currentDate)
                    .add("type", type)
                    .add("operation", operation)
                    .forward(mapping.findForward("noRecurrence"));
        }
        return forward;
    }
}
