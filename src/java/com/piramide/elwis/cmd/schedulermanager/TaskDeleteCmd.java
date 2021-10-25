package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.domain.schedulermanager.*;
import com.piramide.elwis.dto.schedulermanager.TaskDTO;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 8, 2005
 * Time: 3:51:42 PM
 * To change this template use File | Settings | File Templates.
 */

public class TaskDeleteCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing delete task command");

        TaskDTO taskDTO = new TaskDTO(paramDTO);
        ScheduledUserHome home = (ScheduledUserHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULEDUSER);
        UserTaskHome userTaskHome = (UserTaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_USERTASK);
        Collection users = new ArrayList(0);
        UserTask userTask = null;

        IntegrityReferentialChecker.i.check(taskDTO, resultDTO);
        if (resultDTO.isFailure()) { //is referenced
            return;
        }

        Task task = null;
        try {
            task = (Task) EJBFactory.i.findEJB(taskDTO);
            log.debug(" ... task ... " + task);
            if (task != null) {
                users = home.findByTaskId(task.getTaskId());
                for (Iterator iterator = users.iterator(); iterator.hasNext();) {
                    ScheduledUser scheduledUser = (ScheduledUser) iterator.next();
                    userTask = userTaskHome.findByPrimaryKey(scheduledUser.getScheduledUserId());
                    userTask.remove();
                    scheduledUser.remove();
                }
                if (task.getSchedulerFreeText() != null) {
                    SchedulerFreeText freeText = task.getSchedulerFreeText();
                    task.setFreeTextId(null);
                    if (freeText != null) {
                        freeText.remove();
                    }
                }
            }
            try {
                task.remove();  //remove below the additional not CMR relationships
            } catch (RemoveException e) {
                ctx.setRollbackOnly();
                log.error("Error removing task", e);
                taskDTO.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
            }
        } catch (EJBFactoryException e) {
            log.debug("Task to delete cannot be found...");
            ctx.setRollbackOnly();//invalid the transaction
            taskDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Fail");
        } catch (RemoveException e) {
            log.debug(" ... error remove freetext ... ");
        } catch (FinderException e) {
            log.debug(" ... not found userTask ... ");
        }
    }

    public boolean isStateful() {
        return false;
    }
}
