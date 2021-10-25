package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.campaignmanager.util.CampaignRecipientWrapper;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionBusinessDelegate;
import com.piramide.elwis.cmd.utils.ElwisCacheManager;
import com.piramide.elwis.domain.campaignmanager.Campaign;
import com.piramide.elwis.domain.campaignmanager.CampaignGeneration;
import com.piramide.elwis.domain.campaignmanager.CampaignTemplate;
import com.piramide.elwis.domain.common.Attachment;
import com.piramide.elwis.dto.campaignmanager.CampaignGenerationDTO;
import com.piramide.elwis.dto.common.AttachmentDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public class CampaignRetryLightEmailSendCmd extends CampaignLightEmailSendCmd {

    private Log log = LogFactory.getLog(this.getClass());
    private CampaignGeneration campaignGeneration;

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CampaignRetryLightEmailSendCmd..... " + paramDTO);

        Integer generationId = new Integer(paramDTO.get("generationId").toString());
        campaignGeneration = (CampaignGeneration) ExtendedCRUDDirector.i.read(new CampaignGenerationDTO(generationId), resultDTO, false);

        if (campaignGeneration != null) {
            super.executeInStateless(ctx);
        }
    }

    @Override
    protected boolean isRetrySendOperation() {
        return true;
    }

    @Override
    protected Map<String, List<CampaignRecipientWrapper>> findSendRecipients(Campaign campaign, CampaignTemplate campaignTemplate) {
        return CampaignGenerateUtil.getSentLogRecipientsFailedByLanguage(campaignGeneration.getCampaignSentLog(), campaignTemplate);
    }

    @Override
    protected Integer processAndGetGenerationId(Campaign campaign, CampaignTemplate campaignTemplate, Integer userId, List attachIdList) {
        return campaignGeneration.getGenerationId();
    }

    @Override
    protected Integer processAndGetCampaignSentLogId(Integer generationId, Long generationKey, Integer campaignId, Integer companyId) {
        Integer campaignSentLogId = campaignGeneration.getCampaignSentLog().getCampaignSentLogId();

        CampaignSentLogCmd campaignSentLogCmd = new CampaignSentLogCmd();
        campaignSentLogCmd.putParam("op", "update");
        campaignSentLogCmd.putParam("campaignSentLogId", campaignSentLogId);
        campaignSentLogCmd.putParam("generationKey", generationKey);
        campaignSentLogCmd.putParam("totalSent", CampaignGenerateUtil.countFailedCampaignSentLogRecipients(campaignSentLogId));
        campaignSentLogCmd.putParam("totalSuccess", Integer.valueOf(0));

        try {
            BeanTransactionBusinessDelegate.i.execute(campaignSentLogCmd);
        } catch (AppLevelException e) {
            log.error("Error in execute CampaignSentLogCmd cmd..", e);
        }

        return campaignSentLogId;
    }

    @Override
    protected void processSentLogContact(Integer campaignSentLogId, Long generationKey, Integer companyId, Map contactInfoMap) {

        SentLogContactCmd sentLogContactCmd = new SentLogContactCmd();
        sentLogContactCmd.putParam("op", "update");
        sentLogContactCmd.putParam("sentLogContactId", contactInfoMap.get("sentLogContactId"));
        sentLogContactCmd.putParam("generationKey", generationKey);
        sentLogContactCmd.putParam("errorMessage",contactInfoMap.get("errorMessage") );
        sentLogContactCmd.putParam("status", contactInfoMap.get("status"));

        try {
            BeanTransactionBusinessDelegate.i.execute(sentLogContactCmd);
        } catch (AppLevelException e) {
            log.error("Error in execute SentLogContactCmd cmd..", e);
        }
    }

    @Override
    protected Map<Integer, String> processAttach(String listIdAttach, List<ArrayByteWrapper> newAttachWrapperList, Campaign campaign, Integer userId, Long generationKey) {
        Map<Integer, String> attachInfoMap = new HashMap<Integer, String>();

        String pathFolderAttach = ElwisCacheManager.pathCampaignMailAttachFolder_CreateIfNotExist(campaign.getCompanyId(), campaign.getCampaignId(), userId, generationKey, true);

        //process campaign attach
        if (listIdAttach != null && listIdAttach.trim().length() > 0) {
            String[] attachIDs = listIdAttach.split(",");

            for (int i = 0; i < attachIDs.length; i++) {
                String attachmentID = attachIDs[i].trim();

                Attachment attachment = (Attachment) ExtendedCRUDDirector.i.read(new AttachmentDTO(new Integer(attachmentID)), new ResultDTO(), false);

                if (attachment != null) {
                    String pathToFile = pathFolderAttach + attachment.getFileName();

                    File file = new File(pathToFile);
                    try {
                        //verify iif file already exist in cache
                        if (!file.isFile()) {
                            FileOutputStream stream = new FileOutputStream(pathToFile);
                            stream.write(attachment.getFileFreeText().getValue());
                            stream.close();
                        }
                        attachInfoMap.put(attachment.getAttachmentId(), pathToFile);
                    } catch (IOException e) {
                        log.debug("Canot write attach in cache...", e);
                    }
                }
            }
        }

        return attachInfoMap;
    }
}
