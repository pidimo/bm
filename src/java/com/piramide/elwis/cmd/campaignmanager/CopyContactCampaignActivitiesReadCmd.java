package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.domain.campaignmanager.CampaignActivity;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityHome;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L.
 * Read all activities with campaign contacts of this campaign except el activity that arrive in paramDTO
 *
 * @author Miky
 * @version $Id: CopyContactCampaignActivitiesReadCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CopyContactCampaignActivitiesReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing CopyContactCampaignActivitiesReadCmd..... " + paramDTO);

        Integer activityId = new Integer(paramDTO.get("activityId").toString());
        Integer campaignId = new Integer(paramDTO.get("campaignId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        CampaignActivityHome activityHome = (CampaignActivityHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_ACTIVITY);

        List activityList = new ArrayList();
        try {
            Collection activities = activityHome.findByCampaignIdWithoutThisActivity(companyId, campaignId, activityId);
            for (Iterator iterator = activities.iterator(); iterator.hasNext();) {
                CampaignActivity activity = (CampaignActivity) iterator.next();
                if (!activity.getCampaignContacts().isEmpty()) {
                    Map activityMap = new HashMap();
                    activityMap.put("activityId", activity.getActivityId());
                    activityMap.put("title", activity.getTitle());
                    activityList.add(activityMap);
                }
            }

        } catch (FinderException e) {
            log.debug("FinderException: ", e);
            resultDTO.setResultAsFailure();
        }
        resultDTO.put("activityList", activityList);
    }

    public boolean isStateful() {
        return false;
    }
}
