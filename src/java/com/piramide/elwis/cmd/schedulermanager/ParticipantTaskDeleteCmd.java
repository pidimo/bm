package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.domain.schedulermanager.ScheduledUser;
import com.piramide.elwis.domain.schedulermanager.UserTask;
import com.piramide.elwis.domain.schedulermanager.UserTaskHome;
import com.piramide.elwis.dto.schedulermanager.ScheduledUserDTO;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 * @version $Id: ParticipantTaskDeleteCmd.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class ParticipantTaskDeleteCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ParticipantTaskDeleteCmd........");
        String op = paramDTO.get("op").toString();
        if (op.equals("deleteGroup")) {
            deleteGroup(paramDTO.get("userGroupId").toString(), paramDTO.get("taskId").toString());
        } else {
            ScheduledUserDTO dto = new ScheduledUserDTO(paramDTO);
            ScheduledUser scheduledUser = null;
            UserTask userTask = null;
            UserTaskHome userTaskHome = (UserTaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_USERTASK);
            scheduledUser = (ScheduledUser) EJBFactory.i.findEJB(dto);
            try {
                userTask = userTaskHome.findByPrimaryKey(scheduledUser.getScheduledUserId());
                userTask.remove();
                scheduledUser.remove();
            } catch (FinderException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (RemoveException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
                userTask.remove();
                scheduledUser.remove();
            } catch (javax.ejb.RemoveException ex) {
            } catch (FinderException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
