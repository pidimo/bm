package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.CampaignGenAttach;
import com.piramide.elwis.domain.campaignmanager.CampaignGenText;
import com.piramide.elwis.domain.campaignmanager.CampaignGeneration;
import com.piramide.elwis.domain.common.Attachment;
import com.piramide.elwis.dto.campaignmanager.CampaignGenerationDTO;
import com.piramide.elwis.dto.common.AttachmentDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * Jatun S.R.L.
 * Cmd to delete all data related to campaign generation
 *
 * @author Miky
 * @version $Id: CampaignGenerationDeleteCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignGenerationDeleteCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CampaignGenerationDeleteCmd........" + paramDTO);

        Integer generationId = new Integer(paramDTO.get("generationId").toString());
        CampaignGeneration generation = (CampaignGeneration) ExtendedCRUDDirector.i.read(new CampaignGenerationDTO(generationId), resultDTO, false);
        if (generation != null) {
            try {
                //delete camp gen text
                Collection campGenTexts = generation.getCampaignGenTexts();
                Object[] campGenTextObj = campGenTexts.toArray();
                for (int i = 0; i < campGenTextObj.length; i++) {
                    CampaignGenText campaignGenText = (CampaignGenText) campGenTextObj[i];
                    if (campaignGenText.getGenerationText() != null) {
                        campaignGenText.getGenerationText().remove();
                    }
                    //remove image store relations
                    deleteCampaignGenTextImg(campaignGenText.getCampaignGenTextId(), ctx);

                    campaignGenText.remove();
                }

                //delete generation attachments
                Collection campGenAttachs = generation.getGenerationAttachs();
                Object[] campGenAttachObj = campGenAttachs.toArray();
                for (int i = 0; i < campGenAttachObj.length; i++) {
                    CampaignGenAttach campaignGenAttach = (CampaignGenAttach) campGenAttachObj[i];
                    Integer attachmentId = campaignGenAttach.getAttachmentId();

                    campaignGenAttach.remove();
                    Attachment attachment = (Attachment) ExtendedCRUDDirector.i.read(new AttachmentDTO(attachmentId), new ResultDTO(), false);
                    if (attachment != null) {
                        if (attachment.getFileFreeText() != null) {
                            attachment.getFileFreeText().remove();
                        }
                        attachment.remove();
                    }
                }

                //remove generation
                generation.remove();
            } catch (RemoveException e) {
                log.debug("Can't remove entity...", e);
                ctx.setRollbackOnly();
                resultDTO.setResultAsFailure();
            }
        }
    }

    private void deleteCampaignGenTextImg(Integer campaignGenTextId, SessionContext ctx) {

        CampaignGenTextImgCmd campaignGenTextImgCmd = new CampaignGenTextImgCmd();
        campaignGenTextImgCmd.putParam("op", "deleteFromTemplate");
        campaignGenTextImgCmd.putParam("campaignGenTextId", campaignGenTextId);

        campaignGenTextImgCmd.executeInStateless(ctx);
    }


    public boolean isStateful() {
        return false;
    }
}
