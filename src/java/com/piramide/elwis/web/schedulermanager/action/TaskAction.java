package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.cmd.common.UserInfoCmd;
import com.piramide.elwis.cmd.schedulermanager.TaskCreateCmd;
import com.piramide.elwis.dto.schedulermanager.TaskNotificationMessage;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.schedulermanager.form.TaskForm;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Mauren Carrasco
 */

public class TaskAction extends com.piramide.elwis.web.schedulermanager.action.TaskManagerAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping action, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if (isCancelled(request)) {
            log.debug("Is Cancel");
            return (action.findForward("Cancel"));
        }
        TaskForm taskForm = (TaskForm) form;

        ActionForward forward = action.findForward("Fail");

        if ("create".equals(request.getParameter("dto(op)"))
                && request.getParameter("dto(save)") != null) {
            TaskCreateCmd cmd = new TaskCreateCmd();
            cmd.putParam(taskForm.getDtoMap());

            BusinessDelegate.i.execute(cmd, request);

            ActionErrors aErrors = MessagesUtil.i.convertToActionErrors(action, request, cmd.getResultDTO());
            saveErrors(request, aErrors);
            if (!cmd.getResultDTO().isFailure()) {
                request.setAttribute("taskId", cmd.getResultDTO().get("taskId"));
                forward = action.findForward("Success");
            }
        } else if ("update".equals(taskForm.getDto("op")) && request.getParameter("dto(save)") != null) {
            User user = RequestUtils.getUser(request);
            processNotificationMessageI18nNames(taskForm);//here is needed the userId of the creator.
            taskForm.setDto("userId", user.getValue("userId"));

            return super.execute(action, taskForm, request, response);
        } else if (request.getParameter("dto(save)") == null) {
            if (request.getParameter("dto(processId)").equals("") && taskForm.getDto("clear").equals("true")) {
                taskForm.setDto("clear", "false");
            } else if (!request.getParameter("dto(processId)").equals("")) {
                taskForm.setDto("clear", "true");
            }
            forward = action.findForward("Redirect");
        }

        return forward;
    }

    /**
     * Process notification message, recovering the message titles which are used in the mail body and header
     * on the notification mail message.
     *
     * @param taskForm the task form.
     */
    private void processNotificationMessageI18nNames(DefaultForm taskForm) {
        log.debug("process notification I18N");
        log.debug("owner userId = " + taskForm.getDto("userId")); //creator user Id

        /**
         * Only send a notification when the status has been changed. If the status is the same, does not send
         * a notification, it doesn't make sense.
         */
        if (taskForm.getDto("oldAssignedStatus") != null &&
                !taskForm.getDto("oldAssignedStatus").equals(taskForm.getDto("statusId"))) {
            log.debug("Modification of status did it");
            try {
                UserInfoCmd userCmd = new UserInfoCmd();
                userCmd.putParam("userId", new Integer(taskForm.getDto("userId").toString()));
                BusinessDelegate.i.execute(userCmd, null);
                Locale locale = new Locale((String) userCmd.getResultDTO().get("language"));//get the owner locale

                TimeZone ownerTimeZone = TimeZone.getDefault();
                String timeZoneId = (String) userCmd.getResultDTO().get("timeZone");
                if (timeZoneId != null) {
                    ownerTimeZone = DateTimeZone.forID(timeZoneId).toTimeZone();
                }
                Map statusValues = new HashMap();
                statusValues.put(SchedulerConstants.NOTSTARTED, JSPHelper.getMessage(locale, "Task.notInit"));
                statusValues.put(SchedulerConstants.INPROGRESS, JSPHelper.getMessage(locale, "Task.InProgress"));
                statusValues.put(SchedulerConstants.CONCLUDED, JSPHelper.getMessage(locale, "Scheduler.Task.Concluded"));
                statusValues.put(SchedulerConstants.DEFERRED, JSPHelper.getMessage(locale, "Task.Deferred"));
                statusValues.put(SchedulerConstants.CHECK, JSPHelper.getMessage(locale, "Task.ToCheck"));
                TaskNotificationMessage message = new TaskNotificationMessage(JSPHelper.getMessage(locale,
                        "Task.Notification.subject"),
                        JSPHelper.getMessage(locale, "Task.Notification.title"),
                        JSPHelper.getMessage(locale, "Task.taskName"),
                        JSPHelper.getMessage(locale, "Task.startDate"),
                        JSPHelper.getMessage(locale, "Task.expireDate"),
                        JSPHelper.getMessage(locale, "Task.AssignedUserStatus"),
                        JSPHelper.getMessage(locale, "Task.priority"),
                        JSPHelper.getMessage(locale, "Common.modifiedBy"),
                        JSPHelper.getMessage(locale, "Task.notes"),
                        JSPHelper.getMessage(locale, "Task.description"),
                        JSPHelper.getMessage(locale, "Task.NoDueDate"),
                        statusValues,
                        JSPHelper.getMessage(locale, "dateTimePattern"),
                        JSPHelper.getMessage(locale, "datePattern"), ownerTimeZone);

                taskForm.setDto("notificationMessage", message);
            } catch (Exception e) {
                log.debug("So, the task was deleted...nothing to do, the exception and messages will thrown in the command");
            }
        }
    }
}
