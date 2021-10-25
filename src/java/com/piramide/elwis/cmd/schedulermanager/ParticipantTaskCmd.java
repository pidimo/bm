package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.schedulermanager.*;
import com.piramide.elwis.dto.schedulermanager.ScheduledUserDTO;
import com.piramide.elwis.dto.schedulermanager.UserTaskDTO;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;

/**
 * AlfaCentauro Team
 * This class executes the operation of delete for a scheduledUser and a deletes users from his
 * userGroupId and his taskId
 *
 * @author Alvaro
 * @version $Id: ParticipantTaskCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class ParticipantTaskCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }


    public void executeInStateless(SessionContext ctx) {

        log.debug(" .... Executing ParticipantTaskCmd........");
        String group = "";
        UserTask uTask = null;
        UserTask userTask = null;

        if (getOp().equals("")) {
            userTask = (UserTask) ExtendedCRUDDirector.i.read(new UserTaskDTO(paramDTO), resultDTO, false);
            if (userTask != null && !resultDTO.isFailure()) {
                if (userTask.getSchedulerFreeText() != null) {
                    resultDTO.put("noteValue", new String(userTask.getSchedulerFreeText().getValue()));
                }
                return;
            } else {
                resultDTO.setResultAsFailure();
                resultDTO.addResultMessage("customMsg.NotFound", paramDTO.get("participantName"));
                resultDTO.setForward("Fail");
                return;
            }
        } else if (getOp().equals("update")) {
            uTask = updateParticipant(new UserTaskDTO(paramDTO));
            if (uTask == null) {
                resultDTO.setResultAsFailure();
                resultDTO.addResultMessage("customMsg.NotFound", paramDTO.get("participantName"));
                resultDTO.setForward("Fail");
                return;
            }
            return;
        } else {
            group = paramDTO.get("group").toString();
        }

        if (SchedulerConstants.TRUE_VALUE.equals(group)) {
            deleteGroup(paramDTO.get("userGroupId").toString(), paramDTO.get("taskId").toString());
        } else if (SchedulerConstants.FALSE_VALUE.equals(group) || "".equals(paramDTO.get("taskGroupName"))) {
            ScheduledUser scheduledUser = null;
            UserTaskHome userTaskHome = (UserTaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_USERTASK);
            ScheduledUserHome scheduledUserHome = (ScheduledUserHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULEDUSER);
            try {
                scheduledUser = scheduledUserHome.findByPrimaryKey(new Integer(paramDTO.get("scheduledUserId").toString()));
                userTask = userTaskHome.findByPrimaryKey(scheduledUser.getScheduledUserId());
            } catch (FinderException e) {
                log.debug("Cannot find userTask ..." + e);
                resultDTO.setResultAsFailure();
                resultDTO.addResultMessage("customMsg.NotFound", paramDTO.get("participantName"));
                resultDTO.setForward("Fail");
                return;
            }
            if (userTask != null && scheduledUser != null) {
                try {
                    userTask.remove();
                    scheduledUser.remove();
                } catch (RemoveException e) {
                }
            }
        }
    }

    public void deleteGroup(String userGroupId, String taskId) {
        ScheduledUser scheduledUser = null;
        UserTask userTask = null;
        UserTaskHome userTaskHome = (UserTaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_USERTASK);
        Object params[] = {new Integer(userGroupId), new Integer(taskId)};
        ArrayList users = new ArrayList((Collection) EJBFactory.i.callFinder(new ScheduledUserDTO(), "findByUserGroupIdAndTaskId", params));
        for (int i = 0; i < users.size(); i++) {
            try {
                scheduledUser = (ScheduledUser) users.get(i);
                userTask = userTaskHome.findByPrimaryKey(scheduledUser.getScheduledUserId());
                if (userTask != null) {
                    userTask.remove();
                    scheduledUser.remove();
                }
            } catch (javax.ejb.RemoveException ex) {
            } catch (FinderException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public UserTask updateParticipant(UserTaskDTO userTaskDTO) {
        SchedulerFreeTextHome frHome = (SchedulerFreeTextHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULER_FREETEXT);
        UserTask userTask = (UserTask) ExtendedCRUDDirector.i.update(userTaskDTO, resultDTO, false, true, false, "Fail");
        SchedulerFreeText freeText = null;
        if (userTask != null && !resultDTO.isFailure()) {
            if (userTask.getSchedulerFreeText() != null && userTaskDTO.get("noteValue") != null) {
                try {
                    freeText = frHome.findByPrimaryKey(userTask.getNoteId());
                    freeText.setValue(userTaskDTO.get("noteValue").toString().getBytes());
                } catch (FinderException e) {
                }
            } else if (!"".equals(userTaskDTO.get("noteValue")) && userTaskDTO.get("noteValue") != null) {
                try {
                    freeText = frHome.create(userTaskDTO.get("noteValue").toString().getBytes(),
                            new Integer(userTaskDTO.get("companyId").toString()),
                            new Integer(FreeTextTypes.FREETEXT_SCHEDULER));
                } catch (CreateException e) {
                    log.error("Error creating the noteValue ...", e);
                }
                freeText.setValue(userTaskDTO.get("noteValue").toString().getBytes());
                userTask.setNoteId(freeText.getFreeTextId());
            }

            /** Process the updating of the general task status.
             * If the task has been assigned to more users and+ every user do modify the status of his
             * assigned task, so the status of the general task must be updated with the respective value
             * depending of the status of the another users assigned to the same task.
             */

            UserTaskHome userTaskHome = (UserTaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_USERTASK);
            try {
                processTaskUpdatingGeneralStatus(userTaskHome, userTask.getScheduledUser().getTask());
            } catch (FinderException e) {
                resultDTO.setResultAsFailure();
            }
        } else {
            userTask = (UserTask) CRUDDirector.i.read(userTaskDTO, resultDTO);
            if (userTask != null) {
                if (userTask.getSchedulerFreeText() != null) {
                    resultDTO.put("noteValue", new String(userTask.getSchedulerFreeText().getValue()));
                }
                if (userTask.getStatusId() != null) {
                    resultDTO.put("statusId", userTask.getStatusId());
                }
            }
            resultDTO.setResultAsFailure();
        }
        return userTask;
    }

    /**
     * Process the updating of the general task status.
     * If the task has been assigned to more users and every user do modify the status of his
     * assigned task, so the status of the general task must be updated with the respective value
     * depending of the status of the another users assigned to the same task.
     *
     * @param userTaskHome the usertask home
     * @param task         the task bean
     * @throws FinderException if there is no found status to compare and update.
     */

    public void processTaskUpdatingGeneralStatus(UserTaskHome userTaskHome, Task task) throws FinderException {
        int notStarted = userTaskHome.selectCountByStatus(task.getTaskId(),
                new Integer(SchedulerConstants.NOTSTARTED)).intValue();
        int inProgress = userTaskHome.selectCountByStatus(task.getTaskId(),
                new Integer(SchedulerConstants.INPROGRESS)).intValue();
        int concluded = userTaskHome.selectCountByStatus(task.getTaskId(),
                new Integer(SchedulerConstants.CONCLUDED)).intValue();
        int deferred = userTaskHome.selectCountByStatus(task.getTaskId(),
                new Integer(SchedulerConstants.DEFERRED)).intValue();
        int toCheck = userTaskHome.selectCountByStatus(task.getTaskId(),
                new Integer(SchedulerConstants.CHECK)).intValue();
        log.debug("There are (" + notStarted + ") tasks not started");
        log.debug("There are (" + inProgress + ") tasks in progress");
        log.debug("There are (" + concluded + ") tasks concluded");
        log.debug("There are (" + deferred + ") tasks deferred");
        log.debug("There are (" + toCheck + ") tasks to check");

        if (inProgress > 0 || toCheck > 0 || deferred > 0) //there are at least one, so whole task is in progress
        {
            task.setStatus(new Integer(SchedulerConstants.INPROGRESS));
        }

        if (notStarted == 0 && inProgress == 0 && toCheck == 0 && deferred == 0 && concluded > 0)//all concluded, so task has been finished
        {
            task.setStatus(new Integer(SchedulerConstants.CONCLUDED));
        }

        if (notStarted > 0 && inProgress == 0 && concluded == 0 && toCheck == 0 && deferred == 0)//all was changed to not started, so change the status.
        {
            task.setStatus(new Integer(SchedulerConstants.NOTSTARTED));
        }

        if (notStarted == 0 && inProgress == 0 && toCheck == 0 && deferred > 0 && concluded == 0)//all deferred, so task must change to deferred
        {
            task.setStatus(new Integer(SchedulerConstants.DEFERRED));
        }

        if (notStarted == 0 && inProgress == 0 && toCheck > 0 && deferred == 0 && concluded == 0)//all toCheck, so task must change to to check
        {
            task.setStatus(new Integer(SchedulerConstants.CHECK));
        }
    }
}
