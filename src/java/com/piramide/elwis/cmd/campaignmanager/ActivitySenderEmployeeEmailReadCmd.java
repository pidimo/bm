package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.campaignmanager.Campaign;
import com.piramide.elwis.domain.campaignmanager.CampaignActivity;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityHome;
import com.piramide.elwis.domain.campaignmanager.CampaignHome;
import com.piramide.elwis.domain.contactmanager.Telecom;
import com.piramide.elwis.domain.contactmanager.TelecomHome;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L.
 * get telecom emails by telecomTypeId, this to company, activity responsible, campaign responsible and current user
 *
 * @author Miky
 * @version $Id: ActivitySenderEmployeeEmailReadCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ActivitySenderEmployeeEmailReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing ActivitySenderEmployeeEmailReadCmd..... " + paramDTO);

        Integer currentUserId = new Integer(paramDTO.get("userId").toString());
        Integer senderEmployeeId = new Integer(paramDTO.get("senderEmployeeId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer telecomTypeId = new Integer(paramDTO.get("telecomTypeId").toString());

        CampaignActivityHome activityHome = (CampaignActivityHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_ACTIVITY);
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        CampaignHome campaignHome = (CampaignHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN);

        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

        List resultList = new ArrayList();

        try {

            //get company email telecom
            Collection companyTelecoms = telecomHome.findAllAddressTelecomsByTypeId(companyId, telecomTypeId);
            addInResultList(resultList, companyTelecoms, false);

            if (CampaignConstants.CONTACT_RESPONSIBLE.equals(senderEmployeeId.toString())) {
                //read activity responsible
                Integer activityId = new Integer(paramDTO.get("activityId").toString());
                CampaignActivity activity = activityHome.findByPrimaryKey(activityId);
                User activityResponsibleUser = userHome.findByPrimaryKey(activity.getUserId());

                Collection activityResponsibleTelecoms = telecomHome.findAllContactPersonTelecomsByTypeId(companyId, activityResponsibleUser.getAddressId(), telecomTypeId);
                Collection campaignResponsibleTelecoms = new ArrayList();
                Collection currentUserTelecoms = new ArrayList();

                //read campaign responsible
                Campaign campaign = campaignHome.findByPrimaryKey(activity.getCampaignId());
                if (!campaign.getEmployeeId().equals(activityResponsibleUser.getAddressId())) {
                    campaignResponsibleTelecoms = telecomHome.findAllContactPersonTelecomsByTypeId(companyId, campaign.getEmployeeId(), telecomTypeId);
                }

                //read current user, only if this is internal
                User currentUser = userHome.findByPrimaryKey(currentUserId);
                if (AdminConstants.INTERNAL_USER.equals(currentUser.getType().toString())) {
                    if (!currentUser.getAddressId().equals(campaign.getEmployeeId()) && !currentUser.getAddressId().equals(activityResponsibleUser.getAddressId())) {
                        currentUserTelecoms = telecomHome.findAllContactPersonTelecomsByTypeId(companyId, currentUser.getAddressId(), telecomTypeId);
                    }
                }

                //add email telecoms in result
                addInResultList(resultList, activityResponsibleTelecoms, false);
                addInResultList(resultList, campaignResponsibleTelecoms, false);
                addInResultList(resultList, currentUserTelecoms, false);
            } else {
                Collection senderEmployeetelecoms = telecomHome.findAllContactPersonTelecomsByTypeId(companyId, senderEmployeeId, telecomTypeId);
                addInResultList(resultList, senderEmployeetelecoms, true);
            }

        } catch (FinderException e) {
            log.debug("FinderException: ", e);
            resultDTO.setResultAsFailure();
        }

        resultDTO.put("emailTelecomList", resultList);
    }

    private void addInResultList(List resultList, Collection telecoms, boolean addPredetermined) {
        for (Object telecom1 : telecoms) {
            Telecom telecom = (Telecom) telecom1;
            if (!alreadyAddedInResultList(resultList, telecom)) {
                Map telecomMap = new HashMap();
                telecomMap.put("telecomNumber", telecom.getData());
                if (addPredetermined && telecom.getPredetermined()) {
                    telecomMap.put("predetermined", "true");
                } else {
                    telecomMap.put("predetermined", "false");
                }
                resultList.add(telecomMap);
            }
        }
    }

    private boolean alreadyAddedInResultList(List resultList, Telecom telecom) {
        for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
            Map telecomMap = (Map) iterator.next();
            if (telecom.getData().equals(telecomMap.get("telecomNumber"))) {
                return true;
            }
        }
        return false;
    }

    public boolean isStateful() {
        return false;
    }
}
