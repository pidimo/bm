package com.piramide.elwis.web.schedulermanager.taglib;

import com.piramide.elwis.dto.schedulermanager.AppointmentCompountView;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.schedulermanager.el.Functions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.util.HashMap;
import java.util.Map;

/**
 * Jatun S.R.L.
 * Appointment AJAX response to year daily view
 *
 * @author Miky
 * @version $Id: DailyOverviewAJAXResponseTag.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class DailyOverviewAJAXResponseTag extends DailyAJAXResponseTag {
    private Map<Integer, Byte[]> userPermisionsMap;

    public int doStartTag() throws JspException {
        userPermisionsMap = new HashMap<Integer, Byte[]>();
        return super.doStartTag();
    }

    protected void renderAppointmentXML(StringBuffer xml, AppointmentCompountView appointmentView, boolean allday) {
        log.debug(" ... renderAppointmentXML function execute ... ");

        String tag = !allday ? "appointment" : "alldayappointment";
        xml.append("\n<").append(tag).append(" ");
        xml.append("time=\"").append(appointmentView.getTimeLabel(true)).append("\" ");
        xml.append("allday=\"").append(appointmentView.isAllDay()).append("\" ");
        xml.append("reminder=\"").append(appointmentView.isReminder()).append("\" ");
        xml.append("remindertype=\"").append(appointmentView.getReminderType()).append("\" ");
        xml.append("remindervalue=\"").append(appointmentView.getTimeBefore()).append("\" ");
        xml.append("recurrent=\"").append(appointmentView.isRecurrent()).append("\" ");
        xml.append("owner=\"").append(appointmentView.isOwner()).append("\" ");
        xml.append("public=\"").append(appointmentView.isPublic()).append("\">\n");

        xml.append(com.piramide.elwis.web.common.el.Functions.ajaxResponseFilter("<strong>[" + appointmentView.getOwnerUserName() + "]</strong>"));
        if (hasReadPermission(appointmentView)) {
            xml.append(com.piramide.elwis.web.common.el.Functions.ajaxResponseFilter(" " + appointmentView.getTitle()));
        }
        xml.append("\n</").append(tag).append(">\n");
    }

    private boolean hasReadPermission(AppointmentCompountView appointment) {
        //read app owner user permission
        Byte publicPermission;
        Byte privatePermission;
        if (userPermisionsMap.containsKey(appointment.getUserId())) {
            publicPermission = userPermisionsMap.get(appointment.getUserId())[0];
            privatePermission = userPermisionsMap.get(appointment.getUserId())[1];

        } else {
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            User user = RequestUtils.getUser(request);
            Integer userId = (Integer) user.getValue("userId");
            publicPermission = Functions.getPublicPermission(appointment.getUserId(), userId);
            privatePermission = Functions.getPrivatePermission(appointment.getUserId(), userId);

            userPermisionsMap.put(appointment.getUserId(), new Byte[]{publicPermission, privatePermission});
        }

        return (Functions.hasPermissions(publicPermission, privatePermission, appointment.isPublic())
                && Functions.hasReadAppointmentPermission(publicPermission, privatePermission, appointment.isPublic()));
    }

}
