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
 * read campaign activity sender employees to generate document and send emails
 *
 * @author Miky
 * @version $Id: ActivitySenderEmployeeReadCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ActivitySenderEmployeeReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing ActivitySenderEmployeeReadCmd..... " + paramDTO);

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
            resultDTO.put("activityEmployeeName", activityResponsibleAddress.getName());
            resultDTO.put("activityEmployeeId", activityResponsibleAddress.getAddressId());

            //read campaign responsible
            Campaign campaign = campaignHome.findByPrimaryKey(activity.getCampaignId());
            Employee campaignResponsibleEmployee = employeeHome.findByPrimaryKey(campaign.getEmployeeId());
            resultDTO.put("campaignEmployeeName", campaignResponsibleEmployee.getAddress().getName());
            resultDTO.put("campaignEmployeeId", campaignResponsibleEmployee.getEmployeeId());

            //read current user, only if this is internal
            User currentUser = userHome.findByPrimaryKey(currentUserId);
            if (AdminConstants.INTERNAL_USER.equals(currentUser.getType().toString())) {
                Employee userAsEmployee = employeeHome.findByPrimaryKey(currentUser.getAddressId());
                resultDTO.put("userEmployeeName", userAsEmployee.getAddress().getName());
                resultDTO.put("userEmployeeId", userAsEmployee.getEmployeeId());
            }

        } catch (FinderException e) {
            log.debug("FinderException: ", e);
            resultDTO.setResultAsFailure();
        }
    }

    public boolean isStateful() {
        return false;
    }
}
