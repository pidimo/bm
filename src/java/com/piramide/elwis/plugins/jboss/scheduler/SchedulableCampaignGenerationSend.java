package com.piramide.elwis.plugins.jboss.scheduler;

import com.piramide.elwis.service.campaign.CampaignSendBackgroundService;
import com.piramide.elwis.service.campaign.CampaignSendBackgroundServiceHome;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.campaign.CampaignBackgroundProcessUtil;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.ejb3.common.proxy.plugins.async.AsyncUtils;
import org.jboss.varia.scheduler.Schedulable;

import javax.ejb.CreateException;
import java.util.Date;

/**
 * Scheduler to send camopaigns in background
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.5
 */
public class SchedulableCampaignGenerationSend implements Schedulable {
    private static final Log log = LogFactory.getLog(SchedulableCampaignGenerationSend.class);

    public void perform(Date date, long remainingRepetitions) {
        //log.debug("Executing SchedulableCampaignGenerationSend........." + date + " --- " + CampaignBackgroundProcessUtil.i);

        processCampGenerationInBackground();
    }

    private void processCampGenerationInBackground() {

        boolean existItems = true;
        while (existItems && CampaignBackgroundProcessUtil.i.isStartNextProcess()) {

            CampaignSendBackgroundService campaignSendBackgroundService = getCampaignSendBackgroundService();
            if (campaignSendBackgroundService != null) {

                Integer sentLogContactId = campaignSendBackgroundService.findSentLogContactIdToProcess();
                if (sentLogContactId != null) {
                    boolean isProcessStatus = campaignSendBackgroundService.updateSentLogContactStatusToProcess(sentLogContactId);
                    if (isProcessStatus) {
                        //create async thread to send in background
                        CampaignSendBackgroundService generateServiceAsync = AsyncUtils.mixinAsync(campaignSendBackgroundService);
                        generateServiceAsync.backgroundProcess(sentLogContactId);
                    }
                } else {
                    existItems = false;
                }
            }
        }
    }

    private CampaignSendBackgroundService getCampaignSendBackgroundService() {
        CampaignSendBackgroundServiceHome campaignSendBackgroundServiceHome = (CampaignSendBackgroundServiceHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNSENDBACKGROUND_SERVICE);
        try {
            return campaignSendBackgroundServiceHome.create();
        } catch (CreateException e) {
            log.error("Error in get backgroun generation service..", e);
        }
        return null;
    }

}