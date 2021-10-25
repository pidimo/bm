package com.piramide.elwis.web.schedulermanager.util;

import com.piramide.elwis.cmd.common.SendEmailCmd;
import com.piramide.elwis.cmd.schedulermanager.AppointmentNotificationReadCmd;
import com.piramide.elwis.cmd.schedulermanager.AppointmentNotificationUserEmailInfoCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.template.TemplateFactory;
import com.piramide.elwis.web.common.template.TemplateResources;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Util to sen appointment notifications
 * @author Miguel A. Rojas Cardenas
 * @version 3.4
 */
public class AppointmentNotificationUtil {
    private static Log log = LogFactory.getLog(AppointmentNotificationUtil.class);

    public static void sendNotificationEmail(Integer toUserId, Integer appointmentId, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer userSessionId = Integer.valueOf(user.getValue("userId").toString());

        AppointmentNotificationUserEmailInfoCmd userEmailInfoCmd = new AppointmentNotificationUserEmailInfoCmd();
        userEmailInfoCmd.putParam("userId", toUserId);
        userEmailInfoCmd.putParam("userSessionId", userSessionId);

        Map userEmailMap = null;
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(userEmailInfoCmd, request);
            if (!resultDTO.isFailure()) {
                userEmailMap = (Map) resultDTO.get("userEmailInfoMap");
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd...", e);
        }

        if (userEmailMap != null && !userEmailMap.isEmpty()) {
            List<Map> userEmailParamList = new ArrayList<Map>();
            userEmailParamList.add(userEmailMap);

            sendNotificationEmails(userEmailParamList, appointmentId, request);
        }
    }


    /**
     * Send notification emails to participants in this appointments
     *
     * @param userNotifParamsList
     * @param appointmentId
     * @param request
     */
    public static void sendNotificationEmails(List<Map> userNotifParamsList, Integer appointmentId, HttpServletRequest request) {
        log.debug("Send appointment notification emails..." + userNotifParamsList);

        User user = RequestUtils.getUser(request);
        for (Map userEmailMap : userNotifParamsList) {

            Map appointmentMap = new HashMap();
            AppointmentNotificationReadCmd readCmd = new AppointmentNotificationReadCmd();
            readCmd.putParam("appointmentId", appointmentId);
            readCmd.putParam("viewerUserId", userEmailMap.get("userId"));
            readCmd.putParam("userSessionId", user.getValue("userId"));
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(readCmd, request);
                if (!resultDTO.isFailure()) {
                    appointmentMap.putAll(resultDTO);

                    //calculate reminder
                    if (resultDTO.get("reminder") != null && Boolean.parseBoolean(resultDTO.get("reminder").toString())) {
                        StringBuffer reminderText = new StringBuffer();
                        String reminderType = resultDTO.get("reminderType").toString();
                        if (SchedulerConstants.REMINDER_TYPE_MINUTES.equals(reminderType)) {
                            reminderText.append(resultDTO.get("timeBefore_1"));
                        } else {
                            reminderText.append(resultDTO.get("timeBefore_2"));
                        }

                        for (Iterator iterator = JSPHelper.getReminderTypeList(request).iterator(); iterator.hasNext();) {
                            LabelValueBean labelValueBean = (LabelValueBean) iterator.next();
                            if (labelValueBean.getValue().equals(reminderType)) {
                                reminderText.append(" ").append(labelValueBean.getLabel());
                                break;
                            }
                        }

                        appointmentMap.put("reminderText", reminderText.toString());
                    }

                    //set recurrence text
                    boolean isRecurrence = resultDTO.get("isRecurrence") != null && Boolean.valueOf(resultDTO.get("isRecurrence").toString());
                    if (isRecurrence) {
                        String recurrenceText = "";
                        for (Iterator iterator = JSPHelper.getRecurTypeList(request).iterator(); iterator.hasNext();) {
                            LabelValueBean labelValueBean = (LabelValueBean) iterator.next();
                            if (labelValueBean.getValue().equals(String.valueOf(resultDTO.get("ruleType")))) {
                                recurrenceText = labelValueBean.getLabel();
                            }
                        }
                        appointmentMap.put("recurrenceText", recurrenceText);
                    }
                }
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd...", e);
            }

            Map templateParametersMap = new HashMap();
            templateParametersMap.put("appt", appointmentMap);

            String message = TemplateFactory.getTemplateManager().mergeTemplate("templates/email/AppointmentNotification.vm", templateParametersMap, new TemplateResources(request));
            String subject = JSPHelper.getMessage(request, "Appointment.notification.subject", appointmentMap.get("title"));

            SendEmailCmd sendEmailCmd = new SendEmailCmd();
            sendEmailCmd.putParam(userEmailMap);
            sendEmailCmd.putParam("subject", subject);
            sendEmailCmd.putParam("message", message);
            sendEmailCmd.putParam("timeZone", user.getValue("dateTimeZone"));
            sendEmailCmd.putParam("userId", user.getValue(Constants.USERID));

            try {
                BusinessDelegate.i.execute(sendEmailCmd, request);
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd...", e);
            }
        }

    }
}
