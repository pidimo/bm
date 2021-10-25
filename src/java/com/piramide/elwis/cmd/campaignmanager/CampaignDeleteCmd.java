package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.dto.campaignmanager.CampaignDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Iterator;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CampaignDeleteCmd.java 12583 2016-08-31 23:29:21Z miguel $
 */
public class CampaignDeleteCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing CampaignDeleteCmd ...");


        if ("delete".equals(getOp())) {
            delete(ctx);
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void delete(SessionContext ctx) {
        log.debug("delete data campaign ............. ");

        CampaignDTO dto = new CampaignDTO(paramDTO);
        dto.put("withReferences", true);

        try {

            Campaign campaignDelete = (Campaign) ExtendedCRUDDirector.i.read(dto, resultDTO, true);

            if (null != campaignDelete) {
                log.debug("Delete campaign Activity relations");
                CampaignActivityCmd cmd2 = new CampaignActivityCmd();
                cmd2.setOp("deleteRelations");
                cmd2.putParam("campaignId", campaignDelete.getCampaignId());
                cmd2.putParam("isCampaignCascadeDelete", paramDTO.get("isCampaignCascadeDelete"));
                cmd2.executeInStateless(ctx);
                Boolean hasRelations = (Boolean) cmd2.getResultDTO().get("hasRelations");
                if (hasRelations) {
                    resultDTO.addResultMessage("Campaign.error.delete");
                    resultDTO.setForward("fail");
                    return;
                }

                deleteCampaignCriterion(campaignDelete);
                deleteCampaignContact(campaignDelete);
                deleteCampaignAttach(campaignDelete);
                deleteCampaignActivity(campaignDelete);

                CampaignTemplateCmd cmd = new CampaignTemplateCmd();
                cmd.setOp("deleteTemplates");
                cmd.putParam("campaignId", campaignDelete.getCampaignId());
                cmd.executeInStateless(ctx);

                log.debug("Delete CampaignFreeText");
                if (campaignDelete.getCampaignFreeText() != null) {
                    campaignDelete.getCampaignFreeText().remove();
                }
                campaignDelete.remove();
                log.debug("Finish delete");
            }
        } catch (Exception e) {
            log.error("Error trying to delete campaign", e);
            ctx.setRollbackOnly();
        }
    }

    private void deleteCampaignActivity(Campaign campaign) throws RemoveException {
        Iterator it = campaign.getCampaignActivity().iterator();
        while (it.hasNext()) {
            CampaignActivity campaignActivity = (CampaignActivity) it.next();
            Integer id = campaignActivity.getActivityId();

            CampaignFreeText freeText = campaignActivity.getCampaignFreeText();

            if (freeText != null) {
                Integer freeTextId = freeText.getFreeTextId();
                campaignActivity.setCampaignFreeText(null);

                try {
                    freeText.remove();
                    log.debug("-> Delete CampaignFreeText " + freeTextId + " OK");
                } catch (RemoveException e) {
                    log.debug("-> Delete CampaignFreeText " + freeTextId + " FAIL");
                    throw e;
                }
            }

            try {
                campaignActivity.remove();
                log.debug("-> Delete CampaignActivity activityId=" + id + " OK");
            } catch (RemoveException e) {
                log.debug("-> Delete CampaignActivity activityId=" + id + " FAIL");
                throw e;
            }
            it = campaign.getCampaignActivity().iterator();
        }
    }

    private void deleteCampaignCriterion(Campaign campaign) throws RemoveException {
        Iterator it = campaign.getCampaignCriterion().iterator();
        while (it.hasNext()) {
            CampaignCriterion campaignCriterion = (CampaignCriterion) it.next();

            Integer id = campaignCriterion.getCampaignCriterionId();


            CampaignFreeText freeText = campaignCriterion.getCampaignFreeText();
            Integer freeTextId = freeText.getFreeTextId();
            campaignCriterion.setCampaignFreeText(null);

            try {
                freeText.remove();
                log.debug("-> Delete CampaignFreeText " + freeTextId + " OK");
            } catch (RemoveException e) {
                log.debug("-> Delete CampaignFreeText " + freeTextId + " FAIL");
                throw e;
            }
            try {
                campaignCriterion.remove();
                log.debug("-> Delete CampaignCriterion campaignCriterionId=" + id + " OK");
            } catch (RemoveException e) {
                log.debug("-> Delete CampaignCriterion campaignCriterionId=" + id + " FAIL");
                throw e;
            }
            it = campaign.getCampaignCriterion().iterator();
        }
    }

    private void deleteCampaignContact(Campaign campaign) throws RemoveException {
        Iterator it = campaign.getCampaignContact().iterator();
        while (it.hasNext()) {
            CampaignContact campaignContact = (CampaignContact) it.next();
            Integer id = campaignContact.getCampaignContactId();

            try {
                campaignContact.remove();
                log.debug("-> Delete CampaignContact campaignContactId=" + id + " OK");
            } catch (RemoveException e) {
                log.debug("-> Delete CampaignContact campaignContactId=" + id + " FAIL");
                throw e;
            }
            it = campaign.getCampaignContact().iterator();
        }
    }

    private void deleteCampaignAttach(Campaign campaign) throws RemoveException {
        Iterator it = campaign.getAttach().iterator();
        while (it.hasNext()) {
            Attach attach = (Attach) it.next();
            Integer id = attach.getAttachId();

            try {
                attach.remove();
                log.debug("-> Delete Attach attachId=" + id + " OK");
            } catch (RemoveException e) {
                log.debug("-> Delete Attach attachId=" + id + " FAIL");
                throw e;
            }
            it = campaign.getAttach().iterator();
        }
    }
}
