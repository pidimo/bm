package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.common.UserInfoCmd;
import com.piramide.elwis.cmd.schedulermanager.TaskCreateCmd;
import com.piramide.elwis.cmd.schedulermanager.TaskUpdateCmd;
import com.piramide.elwis.dto.schedulermanager.TaskNotificationMessage;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.schedulermanager.form.TaskForm;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Jul 19, 2005
 * Time: 11:01:45 AM
 * To change this template use File | Settings | File Templates.
 */

public class TaskAction extends com.piramide.elwis.web.contactmanager.action.ContactManagerAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping action, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug(".........   taskAction    execute  .........");
        TaskForm taskForm = (TaskForm) form;

        TaskCreateCmd cmd = new TaskCreateCmd();
        TaskUpdateCmd updateCmd = new TaskUpdateCmd();
        ActionForward forward = action.findForward("Fail");
        ActionForward cancelForward = action.findForward("Cancel");

        if (isCancelled(request)) {
            log.debug("Is Cancel");
            if ("contacts".equals(taskForm.getDto("from"))) {
                cancelForward = action.findForward("cancelComm");
            }
            return cancelForward;
        }

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        if ("create".equals(request.getParameter("dto(op)")) && request.getParameter("dto(save)") != null) {
            ActionErrors aErrors = new ActionErrors();
            cmd.putParam(taskForm.getDtoMap());
            BusinessDelegate.i.execute(cmd, request);
            aErrors = MessagesUtil.i.convertToActionErrors(action, request, cmd.getResultDTO());
            saveErrors(request, aErrors);
            if (!cmd.getResultDTO().isFailure()) {
                request.setAttribute("taskId", cmd.getResultDTO().get("taskId"));
                forward = action.findForward("Success");
                if ("contacts".equals(taskForm.getDto("from"))) {
                    forward = action.findForward("Comm");
                }
            }
        } else if ("update".equals(taskForm.getDto("op")) && request.getParameter("dto(save)") != null) {
            String id;
            ActionErrors errors = new ActionErrors();
            if (request.getParameter("taskId") != null && !"".equals(request.getParameter("taskId"))) {
                id = request.getParameter("taskId");
            } else {
                id = request.getParameter("dto(taskId)");
            }

            errors = ForeignkeyValidator.i.validate(SchedulerConstants.TABLE_TASK, "taskid",
                    id, errors, new ActionError("msg.NotFound",
                            request.getParameter("dto(title)")));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return action.findForward("TaskList");
            }
            processNotificationMessageI18nNames(taskForm);//here is needed the userId of the creator.
            updateCmd.putParam(taskForm.getDtoMap());
            taskForm.setDto("userId", user.getValue("userId"));
            BusinessDelegate.i.execute(updateCmd, request);
            errors = MessagesUtil.i.convertToActionErrors(action, request, updateCmd.getResultDTO());
            saveErrors(request, errors);
            request.setAttribute("taskId", taskForm.getDto("taskId"));
            if (updateCmd.getResultDTO().isFailure()) {
                request.setAttribute("taskId", updateCmd.getResultDTO().get("taskId"));
                request.setAttribute("noSch", SchedulerConstants.TRUE_VALUE);
                taskForm.getDtoMap().putAll(updateCmd.getResultDTO());
                forward = action.findForward("Redirect");
            }
        } else if ("update".equals(taskForm.getDto("op")) && request.getParameter("dto(save)") == null) {
            request.setAttribute("noSch", SchedulerConstants.TRUE_VALUE);
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

        /*
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
