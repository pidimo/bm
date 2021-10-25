package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.domain.campaignmanager.CampaignActivity;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityHome;
import com.piramide.elwis.domain.campaignmanager.CampaignContactHome;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Jatun S.R.L.
 * read activity recipients and verify if exist valid recipients, set error messages if no exist valid recipients
 *
 * @author Miky
 * @version $Id: ReadEnabledActivityContactsCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ReadEnabledActivityContactsCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing ReadEnabledActivityContactsCmd..... " + paramDTO);

        Integer activityId = new Integer(paramDTO.get("activityId").toString());

        CampaignActivityHome activityHome = (CampaignActivityHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_ACTIVITY);
        CampaignContactHome campaignContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);

        try {
            CampaignActivity campaignActivity = activityHome.findByPrimaryKey(activityId);
            List recipients = new ArrayList(campaignContactHome.findEnabledCampaignContactsByActivity(campaignActivity.getActivityId()));
            if (recipients.size() == 0) {
                if (campaignActivity.getCampaignContacts().size() > 0) {
                    resultDTO.addResultMessage("Campaign.msg.emptyValidRecipients");
                } else {
                    resultDTO.addResultMessage("Campaign.msg.emptyRecipients");
                }
                resultDTO.setResultAsFailure();
                resultDTO.put("RecipientError", "true");
                return;
            }
        } catch (FinderException e) {
            log.debug("Finder exception...", e);
        }
    }

    public boolean isStateful() {
        return false;
    }
}
