package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.campaignmanager.util.CampaignRecipientWrapper;
import com.piramide.elwis.domain.campaignmanager.Campaign;
import com.piramide.elwis.domain.campaignmanager.CampaignTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.List;
import java.util.Map;

/**
 * Cmd to send campaign in background
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.5
 */
public class CampaignBackgroundLightEmailSendCmd extends CampaignRetryLightEmailSendCmd {

    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CampaignBackgroundLightEmailSendCmd..... " + paramDTO);
        super.executeInStateless(ctx);
    }

    @Override
    protected Map<String, List<CampaignRecipientWrapper>> findSendRecipients(Campaign campaign, CampaignTemplate campaignTemplate) {
        Integer sentLogContactId = new Integer(paramDTO.get("sentLogContactId").toString());
        return CampaignGenerateUtil.getSentLogContactRecipientByLanguage(sentLogContactId, campaignTemplate);
    }

    @Override
    protected void deleteCampaignSendMailAttachCache(Campaign campaign, Integer userId, Long generationKey) {
        //this should be deleted when background process is completed
    }
}
