package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.campaignmanager.Campaign;
import com.piramide.elwis.domain.campaignmanager.CampaignActivity;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityHome;
import com.piramide.elwis.domain.campaignmanager.CampaignHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.contactmanager.Employee;
import com.piramide.elwis.domain.contactmanager.EmployeeHome;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 * Cmd to read user info (name and user id) to current user, activity responsible, campaign responsible. If campaign responsible is not
 * an user, from this only read your name.
 * <p/>
 * This user be to create task to activity responsibles.
 *
 * @author Miky
 * @version $Id: ActivityTaskCreateUsersReadCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ActivityTaskCreateUsersReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing ActivityTaskCreateUsersReadCmd..... " + paramDTO);

        Integer activityId = new Integer(paramDTO.get("activityId").toString());
        Integer currentUserId = new Integer(paramDTO.get("userId").toString());

        CampaignActivityHome activityHome = (CampaignActivityHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_ACTIVITY);
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        CampaignHome campaignHome = (CampaignHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN);
        EmployeeHome employeeHome = (EmployeeHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_EMPLOYEE);

        try {
            //read activity responsible
            CampaignActivity activity = activityHome.findByPrimaryKey(activityId);
            User activityResponsibleUser = userHome.findByPrimaryKey(activity.getUserId());
            Address activityResponsibleAddress = addressHome.findByPrimaryKey(activityResponsibleUser.getAddressId());
            resultDTO.put("activityResponsibleName", activityResponsibleAddress.getName());
            resultDTO.put("activityUserId", activityResponsibleUser.getUserId());

            //read campaign responsible, only if this is internal user and active, put user id in resultDTO
            Campaign campaign = campaignHome.findByPrimaryKey(activity.getCampaignId());
            Employee campaignResponsibleEmployee = employeeHome.findByPrimaryKey(campaign.getEmployeeId());
            resultDTO.put("campaignResponsibleName", campaignResponsibleEmployee.getAddress().getName());
            try {
                User campaignEmployeeAsUser = userHome.findByCompanyIdAddressIdAndUserType(campaign.getCompanyId(), campaign.getEmployeeId(), new Integer(AdminConstants.INTERNAL_USER));
                if (campaignEmployeeAsUser.getActive()) {
                    resultDTO.put("campaignUserId", campaignEmployeeAsUser.getUserId());
                }
            } catch (FinderException e) {
                log.debug("User not found... campaign employee not is internal user");
            }

            //read current user name
            User currentUser = userHome.findByPrimaryKey(currentUserId);
            Address currentUserAsAddress = addressHome.findByPrimaryKey(currentUser.getAddressId());
            resultDTO.put("currentUserName", currentUserAsAddress.getName());
            resultDTO.put("currentUserId", currentUser.getUserId());

        } catch (FinderException e) {
            log.debug("FinderException: ", e);
            resultDTO.setResultAsFailure();
        }
    }

    public boolean isStateful() {
        return false;
    }
}
