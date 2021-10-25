package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.schedulermanager.TaskCreateCmd;
import com.piramide.elwis.domain.campaignmanager.CampaignActivity;
import com.piramide.elwis.domain.campaignmanager.CampaignContact;
import com.piramide.elwis.domain.schedulermanager.Task;
import com.piramide.elwis.domain.schedulermanager.TaskHome;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityDTO;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 * cmd to create task for all campaign contact with responsible, only if these have not created
 *
 * @author Miky
 * @version $Id: CampaignTaskCreateCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignTaskCreateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CampaignTaskCreateCmd................" + paramDTO);
        Integer activityId = new Integer((String) paramDTO.get("activityId"));
        Integer taskCreateUserId = new Integer((String) paramDTO.get("taskCreateUserId"));
        CampaignActivity activity = (CampaignActivity) ExtendedCRUDDirector.i.read(new CampaignActivityDTO(activityId), resultDTO, false);

        if (activity != null) {
            Collection campContacts = activity.getCampaignContacts();

            TaskHome taskHome = (TaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_TASK);
            Collection activityTasks;
            try {
                activityTasks = taskHome.findByActivityId(activity.getActivityId());
            } catch (FinderException e) {
                activityTasks = new ArrayList();
            }


            for (Iterator iterator = campContacts.iterator(); iterator.hasNext();) {
                CampaignContact campContact = (CampaignContact) iterator.next();

                if (campContact.getActive() != null && campContact.getActive() && campContact.getUserId() != null) {
                    //verif if has task
                    boolean thisHastask = false;
                    for (Iterator iterator2 = activityTasks.iterator(); iterator2.hasNext();) {
                        Task task = (Task) iterator2.next();
                        if (campContact.getAddressId().equals(task.getAddressId())) {
                            if ((campContact.getContactPersonId() != null && campContact.getContactPersonId().equals(task.getContactPersonId()))
                                    || (campContact.getContactPersonId() == null && task.getContactPersonId() == null)) {
                                thisHastask = true;
                                break;
                            }
                        }
                    }
                    if (!thisHastask) {
                        //create if has not task
                        TaskCreateCmd taskCreateCmd = new TaskCreateCmd();
                        taskCreateCmd.putParam(paramDTO);
                        taskCreateCmd.putParam("op", "create");
                        taskCreateCmd.putParam("addressId", campContact.getAddressId());
                        taskCreateCmd.putParam("contactPersonId", campContact.getContactPersonId());
                        taskCreateCmd.putParam("userTaskId", campContact.getUserId());
                        taskCreateCmd.putParam("notification", "on");
                        taskCreateCmd.putParam("userId", taskCreateUserId);

                        log.debug("exeuting taskCreateCmd......" + taskCreateCmd.getParamDTO());

                        taskCreateCmd.executeInStateless(ctx);
                    }
                }
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}