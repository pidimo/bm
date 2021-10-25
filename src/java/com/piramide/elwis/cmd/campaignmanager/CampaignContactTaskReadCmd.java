package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.CampaignActivity;
import com.piramide.elwis.domain.campaignmanager.CampaignContact;
import com.piramide.elwis.domain.campaignmanager.CampaignContactPK;
import com.piramide.elwis.domain.schedulermanager.Task;
import com.piramide.elwis.domain.schedulermanager.TaskHome;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignContactDTO;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 * cmd to verif if all activity campaign contact has created an task
 *
 * @author Miky
 * @version $Id: CampaignContactTaskReadCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignContactTaskReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CampaignContactTaskReadCmd......... " + paramDTO);

        if ("readAll".equals(getOp())) {
            readIfAllContactsHaveTask();
        } else {
            readIfThisContactHaveTask();
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void readIfThisContactHaveTask() {
        Integer activityId = new Integer((String) paramDTO.get("activityId"));
        CampaignActivity activity = (CampaignActivity) ExtendedCRUDDirector.i.read(new CampaignActivityDTO(activityId), new ResultDTO(), false);

        if (activity != null) {
            Integer campContactId = paramDTO.getAsInt("campaignContactId");
            Integer campaignId = paramDTO.getAsInt("campaignId");

            CampaignContactPK pk = new CampaignContactPK(campContactId, campaignId);
            CampaignContactDTO campContactDTO = new CampaignContactDTO();
            campContactDTO.setPrimKey(pk);

            CampaignContact campContact = (CampaignContact) ExtendedCRUDDirector.i.read(campContactDTO, new ResultDTO(), false);

            if (campContact != null) {
                TaskHome taskHome = (TaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_TASK);
                Collection activityTasks;
                try {
                    activityTasks = taskHome.findByActivityId(activity.getActivityId());
                } catch (FinderException e) {
                    activityTasks = new ArrayList();
                }

                boolean thisHastask = false;

                if (campContact.getActive() != null && campContact.getActive() && campContact.getUserId() != null) {

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
                }

                if (thisHastask) {
                    resultDTO.put("withTask", "true");
                }
            }
        }
    }

    private void readIfAllContactsHaveTask() {
        Integer activityId = new Integer((String) paramDTO.get("activityId"));
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

            boolean allHasTask = true;
            for (Iterator iterator = campContacts.iterator(); iterator.hasNext();) {
                CampaignContact campContact = (CampaignContact) iterator.next();

                if (campContact.getActive() != null && campContact.getActive() && campContact.getUserId() != null) {
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
                        allHasTask = false;
                        break;
                    }
                }
            }
            if (allHasTask) {
                resultDTO.put("allWithTask", "true");
            }
        }
    }

}
