package com.piramide.elwis.cmd.campaignmanager;

import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Cmd util for campaign generation
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.5
 */
public class CampaignGenerateUtilCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CampaignGenerateUtilCmd................" + paramDTO);
        String op = this.getOp();

        if ("recipients".equals(op)) {
            calculateCampaignRecipients();
        }
        if ("actRecipients".equals(op)) {
            calculateActivityRecipients();
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void calculateCampaignRecipients() {
        Integer campaignId = new Integer(paramDTO.get("campaignId").toString());
        resultDTO.put("countRecipients", CampaignGenerateUtil.countCampaignRecipients(campaignId));
    }

    private void calculateActivityRecipients() {
        Integer activityId = new Integer(paramDTO.get("activityId").toString());
        resultDTO.put("countRecipients", CampaignGenerateUtil.countEnabledActivityRecipients(activityId));
    }

}