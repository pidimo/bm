package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.catalogmanager.PriorityCmd;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.cmd.utils.Email;
import com.piramide.elwis.cmd.utils.SchedulerUtil;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.salesmanager.SalesProcess;
import com.piramide.elwis.domain.salesmanager.SalesProcessHome;
import com.piramide.elwis.domain.schedulermanager.*;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.schedulermanager.TaskDTO;
import com.piramide.elwis.dto.schedulermanager.TaskNotificationMessage;
import com.piramide.elwis.dto.schedulermanager.UserTaskDTO;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Date;
import java.util.Iterator;

/**
 * @author Mauren Carrasco
 */

public class TaskUpdateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing update task command");
        log.debug("Operation = " + getOp());
        SchedulerFreeTextHome frHome = (SchedulerFreeTextHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULER_FREETEXT);
        SalesProcessHome processHome = (SalesProcessHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALESPROCESS);
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        ScheduledUserHome scheduledUserHome = (ScheduledUserHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULEDUSER);
        ScheduledUser scheduledUser = null;
        Task task = null;

        SchedulerFreeText freeText = null;
        StringBuffer expireTime = new StringBuffer();
        StringBuffer startTime = new StringBuffer();

        DateTimeZone zone = SchedulerUtil.i.getUserDateTimeZone(new Integer(paramDTO.get("userId").toString()));

        int startYearMonthDay[] = DateUtils.getYearMonthDay(new Integer(paramDTO.get("startDate").toString()));
        DateTime startDateTime = new DateTime(startYearMonthDay[0], startYearMonthDay[1], startYearMonthDay[2],
                Integer.parseInt(paramDTO.get("startHour").toString()), Integer.parseInt(paramDTO.get("startMin").toString()), 0, 0, zone);
        startTime.append(paramDTO.getAsString("startHour")).append(":").append(paramDTO.getAsString("startMin"));
        paramDTO.put("startTime", startTime.toString());
        paramDTO.put("startDateTime", new Long(startDateTime.getMillis()));
        //if have expireDate
        if (SchedulerConstants.TRUE_VALUE.equals(paramDTO.get("date"))) {
            expireTime.append(paramDTO.getAsString("expireHour")).append(":").append(paramDTO.getAsString("expireMin"));
            paramDTO.put("expireTime", expireTime.toString());
            int yearMonthDay[] = DateUtils.getYearMonthDay(new Integer(paramDTO.get("expireDate").toString()));
            DateTime resultDateTime = new DateTime(yearMonthDay[0], yearMonthDay[1], yearMonthDay[2],
                    Integer.parseInt(paramDTO.get("expireHour").toString()), Integer.parseInt(paramDTO.get("expireMin").toString()), 0, 0, zone);
            paramDTO.put("expireDateTime", new Long(resultDateTime.getMillis()));
        } else {
            paramDTO.put("expireTime", null);
            paramDTO.put("expireDate", null);
            paramDTO.put("expireDateTime", null);
        }

        TaskDTO taskDTO = new TaskDTO(paramDTO);
        taskDTO.put("notification", new Boolean("on".equals(paramDTO.getAsString("notification"))));

        resultDTO.putAll(paramDTO);
        SalesProcess process = null;
        Address address = null;

        if (!SchedulerConstants.TRUE_VALUE.equals(paramDTO.get("noEdit"))) {
            task = (Task) ExtendedCRUDDirector.i.update(taskDTO, resultDTO, false, true, false, "Fail");
        } else {
            task = (Task) ExtendedCRUDDirector.i.read(taskDTO, resultDTO, false);
            if (task.getSchedulerFreeText() != null) {
                paramDTO.put("descriptionText", new String(task.getSchedulerFreeText().getValue()));
            }
        }

        if (resultDTO.isFailure()) {
            if (task.getProcessId() != null) {
                try {
                    process = processHome.findByPrimaryKey(task.getProcessId());
                } catch (FinderException e) {
                    log.debug("process not found ...");
                }
                resultDTO.put("processName", process.getProcessName());
                resultDTO.put("processId", process.getProcessId());
            } else {
                resultDTO.put("processName", null);
                resultDTO.put("processId", null);
            }
            if (task.getAddressId() != null) {
                try {
                    address = addressHome.findByPrimaryKey(task.getAddressId());
                } catch (FinderException e) {
                    log.debug("contact not found ...");
                }
                StringBuffer addressName = new StringBuffer();
                addressName.append(address.getName1());
                if (address.getName2() != null && address.getName2() != "") {
                    addressName.append(", ").append(address.getName2());
                }
                resultDTO.put("addressId", address.getAddressId());
                resultDTO.put("contact", addressName.toString());
                if (task.getProcessId() != null) {
                    resultDTO.put("clear", SchedulerConstants.TRUE_VALUE);
                }
            } else {
                resultDTO.put("addressId", null);
                resultDTO.put("contact", null);
            }
            if (task.getSchedulerFreeText() != null) {
                resultDTO.put("descriptionText", new String(task.getSchedulerFreeText().getValue()));
            } else {
                resultDTO.put("descriptionText", null);
            }

            UserTaskHome userTaskHome = (UserTaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_USERTASK);
            UserTask userTask = null;
            try {
                scheduledUser = (ScheduledUser) scheduledUserHome.findByUserIdAndTaskId(new Integer(paramDTO.get("recordUserId").toString()), task.getTaskId());
                userTask = (UserTask) userTaskHome.findByPrimaryKey(scheduledUser.getScheduledUserId());
            } catch (FinderException e) {
                log.debug("SchedulerUser or userTask notFound ... ");
            }
            if (userTask != null) {
                resultDTO.put("statusId", userTask.getStatusId());
                resultDTO.put("oldAssignedStatus", userTask.getStatusId());  //to check if the status has been checked on update, for assigned
                resultDTO.put("participantVersion", userTask.getVersion());
                if (userTask.getSchedulerFreeText() != null) {
                    resultDTO.put("noteValue", new String(userTask.getSchedulerFreeText().getValue()));
                }
            }
            resultDTO.put("taskId", task.getTaskId());
        }

        if (task != null && !resultDTO.isFailure()) {

            task.setUpdateDateTime(DateUtils.createDate((new Date()).getTime(), zone.getID()).getMillis());

            if (task.getSchedulerFreeText() != null) {
                try {
                    freeText = frHome.findByPrimaryKey(task.getFreeTextId());
                    freeText.setValue(paramDTO.get("descriptionText").toString().getBytes());
                } catch (FinderException e) {
                }
            } else if (!"".equals(paramDTO.get("descriptionText"))) {
                try {
                    freeText = frHome.create(paramDTO.get("descriptionText").toString().getBytes(),
                            new Integer(paramDTO.getAsInt("companyId")),
                            new Integer(FreeTextTypes.FREETEXT_SCHEDULER));
                } catch (CreateException e) {
                    log.error("Error creating the description", e);
                }
                freeText.setValue(paramDTO.get("descriptionText").toString().getBytes());
                task.setFreeTextId(freeText.getFreeTextId());
            }
            try {
                scheduledUser = scheduledUserHome.findByUserIdAndTaskId(new Integer(paramDTO.get("recordUserId").toString()), task.getTaskId());
                ParticipantTaskCmd cmd = new ParticipantTaskCmd();
                UserTaskDTO userTaskDTO = new UserTaskDTO();
                if (paramDTO.get("participant") != null) {
                    userTaskDTO.put("statusId", paramDTO.get("statusId"));
                } else {
                    userTaskDTO.put("statusId", task.getStatus());
                }

                userTaskDTO.put("version", paramDTO.get("participantVersion"));
                userTaskDTO.put("noteValue", paramDTO.get("noteValue"));
                userTaskDTO.put("companyId", paramDTO.get("companyId"));
                userTaskDTO.put("scheduledUserId", scheduledUser.getScheduledUserId());
                UserTaskHome userTaskHome = (UserTaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_USERTASK);
                UserTask userTask = cmd.updateParticipant(userTaskDTO);
                cmd.processTaskUpdatingGeneralStatus(userTaskHome, task);

                if (cmd.getResultDTO().isFailure()) {
                    resultDTO.putAll(cmd.getResultDTO());
                    Iterator iterator = cmd.getResultDTO().getResultMessages();
                    while (iterator.hasNext()) {
                        resultDTO.addResultMessage((ResultMessage) iterator.next());
                    }
                    resultDTO.putAll(cmd.getResultDTO());
                    resultDTO.setResultAsFailure();
                    return;
                }
                /**
                 * Process notification logic.
                 * Only send a notification if the user that change the status not is the owner of the task.
                 */

                if (!task.getUserId().equals(scheduledUser.getUserId()) && paramDTO.get("notificationMessage") != null
                        && task.getNotification() != null && task.getNotification().booleanValue()) {
                    log.debug("Process notification messaging...");
                    sendNotificationMessageOnStatusChange((TaskNotificationMessage)
                            paramDTO.get("notificationMessage"), task, userTask, scheduledUser.getUserId(), ctx);
                }
            } catch (FinderException e) {
            }
            resultDTO.put("taskId", task.getTaskId());
        } else {
            resultDTO.setForward("Fail");
            resultDTO.setResultAsFailure();
            return;
        }
    }

    public boolean isStateful() {
        return false;
    }

    /**
     * Send the notification of status task changing.
     *
     * @param message      the message with label text defined
     * @param task         the task bean
     * @param userTask     the user task info which modify
     * @param modifyUserId the userId which did modify
     * @param ctx          the session context.
     */

    private void sendNotificationMessageOnStatusChange(TaskNotificationMessage message, Task task,
                                                       UserTask userTask, Integer modifyUserId, SessionContext ctx) {
        try {

            String mailFrom = ConfigurationFactory.getConfigurationManager().getValue("elwis.system.fromEmailSender"); //default sender
            User fromUser = (User) EJBFactory.i.findEJB(new UserDTO(modifyUserId)); //the changer user
            Integer fromUserAddressId = fromUser.getAddressId();

            /** processing mail from **/
            if (!fromUser.getSchedulerTaskNotificationEmails().isEmpty()) {
                mailFrom = (String) fromUser.getSchedulerTaskNotificationEmails().get(0);//get the first one as sender
            }

            User toUser = (User) EJBFactory.i.findEJB(new UserDTO(task.getUserId()));//the owner task userid

            LightlyAddressCmd addressCmd = new LightlyAddressCmd();
            addressCmd.putParam("addressId", fromUserAddressId);
            addressCmd.executeInStateless(ctx);
            ResultDTO addressResultDTO = addressCmd.getResultDTO();

            String priorityName = "";
            if (task.getPriorityId() != null) {
                PriorityCmd priorityCmd = new PriorityCmd();
                priorityCmd.putParam("priorityId", task.getPriorityId());
                priorityCmd.executeInStateless(ctx);
                ResultDTO priorityResultDTO = priorityCmd.getResultDTO();

                priorityName = (String) priorityResultDTO.get("priorityName");
            }

            String messageBody = message.getMessageBody(task.getTitle(), task.getStartDate(), task.getExpireDate(),
                    task.getExpireTime(), userTask.getStatusId(), priorityName,
                    (String) addressResultDTO.get("addressName"),
                    userTask.getSchedulerFreeText() != null ? new String(userTask.getSchedulerFreeText().getValue()) :
                            "", task.getSchedulerFreeText() != null ?
                            new String(task.getSchedulerFreeText().getValue()) : "");
            Email email = null;
            for (Iterator iterator = toUser.getSchedulerTaskNotificationEmails().iterator(); iterator.hasNext();) {
                email = new Email((String) iterator.next(), mailFrom, message.getMessageSubject(),
                        messageBody, "text/plain", message.getTimeZone());
                email.setFromPersonal((String) addressResultDTO.get("addressName"));
                email.setUserId(fromUser.getUserId());
                JMSUtil.sendToJMSQueue(WebMailConstants.JNDI_SENDSIMPLEMAILMDB, email, false);
            }
        } catch (Exception e) {
            log.error("Error trying to notify of the status task changing, it seems the users has not a predetermined" +
                    " telecom of type EMAIL", e);
        }
    }
}

