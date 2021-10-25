package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.campaignmanager.CampaignActivity;
import com.piramide.elwis.domain.campaignmanager.CampaignContact;
import com.piramide.elwis.domain.campaignmanager.CampaignContactPK;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.schedulermanager.Task;
import com.piramide.elwis.domain.schedulermanager.TaskHome;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignContactDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.utils.SchedulerConstants;
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
 * only to read, update and delete actions
 *
 * @author Miky
 * @version $Id: ActivityCampaignContactCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ActivityCampaignContactCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ActivityCampaignContactCmd................" + paramDTO);

        Integer campaignId = new Integer((String) paramDTO.get("campaignId"));
        Integer campContactId = new Integer((String) paramDTO.get("campaignContactId"));

        CampaignContactPK pk = new CampaignContactPK(campContactId, campaignId);

        paramDTO.remove("campaignId"); //this invalid copy of primary key into Bean in update operation
        paramDTO.remove("campaignContactId"); //this invalid copy of primary key into Bean in update operation
        CampaignContactDTO campContactDTO = new CampaignContactDTO(paramDTO);
        campContactDTO.setPrimKey(pk);

        if (paramDTO.get("active") != null) {
            campContactDTO.put("active", new Boolean(true));
        } else {
            campContactDTO.put("active", new Boolean(false));
        }

        //execute cmd
        super.setOp(this.getOp());
        super.checkDuplicate = false;
        super.isClearingForm = false;
        CampaignContact campContact = (CampaignContact) super.execute(ctx, campContactDTO);

        if (campContact != null) {
            readAddressName(campContact.getAddressId(), "contactName");
            if (campContact.getContactPersonId() != null) {
                readAddressName(campContact.getContactPersonId(), "contactPersonName");
            }
            if (campContact.getUserId() != null) {
                User user = (User) EJBFactory.i.findEJB(new UserDTO(campContact.getUserId()));
                readAddressName(user.getAddressId(), "userName");

                //user task
                boolean hasTask = false;
                TaskHome taskHome = (TaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_TASK);
                Collection activityTasks;
                try {
                    activityTasks = taskHome.findByActivityId(campContact.getActivityId());
                } catch (FinderException e) {
                    activityTasks = new ArrayList();
                }

                //verif if this has task
                for (Iterator iterator = activityTasks.iterator(); iterator.hasNext();) {
                    Task task = (Task) iterator.next();
                    if (campContact.getAddressId().equals(task.getAddressId())) {
                        if ((campContact.getContactPersonId() != null && campContact.getContactPersonId().equals(task.getContactPersonId()))
                                || (campContact.getContactPersonId() == null && task.getContactPersonId() == null)) {
                            hasTask = true;
                            break;
                        }
                    }
                }
                resultDTO.put("hasTask", new Boolean(hasTask));
            }
        }

        if ("delete".equals(this.getOp())) {
            Integer activityId = new Integer(paramDTO.get("activityId").toString());
            CampaignActivity activity = (CampaignActivity) ExtendedCRUDDirector.i.read(new CampaignActivityDTO(activityId), new ResultDTO(), false);
            if (activity != null) {
                //update numberContact field in Activity
                activity.setNumberContact(activity.getCampaignContacts().size());
            }
        }

        log.debug("RESULTTTTTTTTTTTTTTTTTTTTTTTTTTTTT:" + resultDTO);
    }

    private void readAddressName(Integer addressId, String field) {
        Address address = (Address) ExtendedCRUDDirector.i.read(new AddressDTO(addressId), new ResultDTO(), false);
        if (address != null) {
            resultDTO.put(field, address.getName());
        }
    }
}
