package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.dto.campaignmanager.*;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CampaignCreateCmd.java 10196 2011-12-14 15:03:20Z fernando $
 */

public class CampaignCreateCmd extends BeanTransactionEJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing create campaign command");
        log.debug("Operation = " + getOp());

        int companyId = new Integer(paramDTO.get("companyId").toString());
        if (null != paramDTO.get("numberContacts") && "".equals(paramDTO.get("numberContacts").toString().trim())) {
            paramDTO.put("numberContacts", 0);
        }

        log.debug("ParamDTO " + paramDTO);
        CampaignDTO campaignDTO = new CampaignDTO(paramDTO);

        if (null != paramDTO.get("copyCampaignId") && !"".equals(paramDTO.get("copyCampaignId").toString().trim())) {
            log.debug("Setting up main information to copy Campaign...");
            campaignDTO.remove("remark");
            boolean hasCopyCampaing = importAction(campaignDTO, ctx);
            if (hasCopyCampaing) {
                resultDTO.setForward("Success");
            } else {
                resultDTO.addResultMessage("error.campaign.copyCampaignNotFound");
                resultDTO.setForward("CopyFail");
            }
            return;
        }
        Campaign campaign = null;
        CampaignFreeText freeText = null;

        try {
            CampaignFreeTextHome frHome = (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);
            freeText = frHome.create(paramDTO.get("remarkValue").toString().getBytes(),
                    companyId,
                    new Integer(FreeTextTypes.FREETEXT_CAMPAIGN));
        } catch (CreateException e) {
            e.printStackTrace();
        }

        campaignDTO.put("remark", freeText.getFreeTextId());
        campaignDTO.put("includePartner", new Boolean(false));
        campaignDTO.put("addressType", new Integer(2));
        campaignDTO.put("contactType", new Integer(2));
        campaignDTO.put("isDouble", new Boolean(true));
        campaignDTO.put("closeDate", null);
        campaignDTO.put("changeDate", null);
        campaignDTO.put("numberContacts", 0);

        campaign = (Campaign) ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_CREATE,
                campaignDTO, resultDTO, false, false, false, false);
        paramDTO.put("campaignId", campaign.getCampaignId());
        return;
    }

    public boolean isStateful() {
        return false;
    }

    private boolean importAction(CampaignDTO campaignDto, SessionContext ctx) {

        campaignDto.remove("closeDate");

        CampaignHome campaignHome = (CampaignHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN);
        CampaignFreeTextHome campaignFreeTextHome = (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);
        Campaign originalCampaign = null;

        try {
            originalCampaign = campaignHome.findByPrimaryKey(new Integer(paramDTO.get("copyCampaignId").toString()));
        } catch (FinderException e) {
            log.debug("Cannot find main campaign...");
        }

        boolean result = false;
        if (originalCampaign != null) {
            result = true;
            Campaign campaign = null;
            try {
                campaign = campaignHome.create(campaignDto);
            } catch (CreateException e) {
                e.printStackTrace();
            }
            if (campaign != null) {
                resultDTO.put("campaignId", campaign.getCampaignId());
                if (null != paramDTO.get("remarkValue") &&
                        !"".equals(paramDTO.get("remarkValue").toString().trim())) {
                    CampaignFreeText freeText = null;
                    try {
                        CampaignFreeTextHome frHome =
                                (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);

                        freeText = frHome.create(paramDTO.get("remarkValue").toString().getBytes(),
                                campaign.getCompanyId(), FreeTextTypes.FREETEXT_CAMPAIGN);

                        campaign.setRemark(freeText.getFreeTextId());
                    } catch (CreateException e) {
                        e.printStackTrace();
                    }
                }

                copyActivity(originalCampaign, campaign);
                copyAttach(originalCampaign, campaign);
                copyTemplate(originalCampaign, campaign, ctx);
                copyCampaignCriterion(originalCampaign, campaign);
                copyRecipients(originalCampaign, campaign);
            }
        }
        return result;
    }

    private void copyActivity(Campaign sourceCampaign, Campaign targetCampaign) {
        Collection activities = sourceCampaign.getCampaignActivity();

        for (Iterator it = activities.iterator(); it.hasNext(); ) {
            CampaignActivity e = (CampaignActivity) it.next();
            CampaignActivityDTO dto = new CampaignActivityDTO();

            DTOFactory.i.copyToDTO(e, dto);
            dto.put("campaignId", targetCampaign.getCampaignId());
            dto.remove("descriptionId");
            dto.remove("activityId");
            dto.remove("version");
            dto.remove("startDate");
            dto.remove("closeDate");
            dto.remove("state");

            CampaignFreeText f = copyFreeText(e.getCampaignFreeText());
            if (null != f) {
                dto.put("descriptionId", f.getFreeTextId());
            }

            ExtendedCRUDDirector.i.create(dto, resultDTO, false);
        }
    }

    private void copyAttach(Campaign sourceCampaign, Campaign targetCampaign) {
        Collection atraches = sourceCampaign.getAttach();
        for (Iterator it = atraches.iterator(); it.hasNext(); ) {
            Attach a = (Attach) it.next();
            AttachDTO dto = new AttachDTO();
            DTOFactory.i.copyToDTO(a, dto);
            dto.put("campaignId", targetCampaign.getCampaignId());
            dto.remove("freeTextId");
            dto.remove("version");
            CampaignFreeText f = copyFreeText(a.getCampaignFreeText());
            if (null != f) {
                dto.put("freeTextId", f.getFreeTextId());
            }

            ExtendedCRUDDirector.i.create(dto, resultDTO, false);
        }
    }

    private void copyTemplate(Campaign sourceCampaign, Campaign targetCampaign, SessionContext ctx) {
        Collection templates = sourceCampaign.getCampaignTemplate();
        for (Iterator it = templates.iterator(); it.hasNext(); ) {
            CampaignTemplate t = (CampaignTemplate) it.next();
            CampaignTemplateDTO dto = new CampaignTemplateDTO();
            DTOFactory.i.copyToDTO(t, dto);
            dto.put("campaignId", targetCampaign.getCampaignId());
            dto.remove("version");

            CampaignTemplate tt = (CampaignTemplate) ExtendedCRUDDirector.i.create(dto, resultDTO, false);
            copyCampaignText(t, targetCampaign.getCampaignId(), tt.getTemplateId(), ctx);
        }
    }

    private void copyCampaignText(CampaignTemplate sourceTemplate, int campaignId, int templateId, SessionContext ctx) {
        Collection c = sourceTemplate.getCampaignText();
        for (Iterator it = c.iterator(); it.hasNext(); ) {
            CampaignText t = (CampaignText) it.next();
            CampaignTextDTO dto = new CampaignTextDTO();
            DTOFactory.i.copyToDTO(t, dto);
            dto.put("templateId", templateId);
            dto.put("campaignId", campaignId);
            dto.remove("version");

            CampaignFreeText f = copyFreeText(t.getCampaignFreeText());
            if (null != f) {
                dto.put("freeTextId", f.getFreeTextId());
            }

            CampaignText copyCampaignText = (CampaignText) ExtendedCRUDDirector.i.create(dto, resultDTO, false);

            if (copyCampaignText != null) {
                //create relations to image store, only if template is HTML
                if (CampaignConstants.DocumentType.HTML.equal(sourceTemplate.getDocumentType())) {
                    createCampaignTextImg(copyCampaignText, new String(f.getValue()), ctx);
                }
            }

        }
    }

    private void createCampaignTextImg(CampaignText campaignText, String templateBody, SessionContext ctx) {

        CampaignTextImgCmd campaignTextImgCmd = new CampaignTextImgCmd();
        campaignTextImgCmd.putParam("op", "createFromTemplate");
        campaignTextImgCmd.putParam("companyId", campaignText.getCompanyId());
        campaignTextImgCmd.putParam("templateId", campaignText.getTemplateId());
        campaignTextImgCmd.putParam("languageId", campaignText.getLanguageId());
        campaignTextImgCmd.putParam("campaignTextBody", templateBody);

        campaignTextImgCmd.executeInStateless(ctx);
    }

    private void copyRecipients(Campaign sourceCampaign, Campaign targetCampaign) {
        CampaignContactHome campaignContactHome = (CampaignContactHome)
                EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);

        List elements;
        try {
            Collection recipients =
                    campaignContactHome.findByCampaignIdAndActivityIdNULL(sourceCampaign.getCampaignId());
            elements = new ArrayList(recipients);
        } catch (FinderException e) {
            log.debug("-> Read Recipients for source campaign" + sourceCampaign.getCampaignId() + " FAIL");
            elements = new ArrayList();
        }

        if (elements.isEmpty()) {
            return;
        }

        CampaignContactDTO dto;
        int i = 0;
        while (i < elements.size()) {
            CampaignContact campaignContact = (CampaignContact) elements.get(i);

            dto = new CampaignContactDTO();
            DTOFactory.i.copyToDTO(campaignContact, dto);
            dto.put("campaignId", targetCampaign.getCampaignId());
            dto.remove("version");

            try {
                campaignContactHome.create(dto);
            } catch (CreateException e) {
                log.error("-> Execute campaignContactHome.create(" + dto + ") FAIL", e);
            }

            i++;
        }
    }

    private void copyCampaignCriterion(Campaign sourceCampaign, Campaign targetCampaign) {
        Collection c = sourceCampaign.getCampaignCriterion();
        CampaignCriterionHome h =
                (CampaignCriterionHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCRITERION);
        List criterions = new ArrayList();
        for (Iterator it = c.iterator(); it.hasNext(); ) {
            CampaignCriterion cc = (CampaignCriterion) it.next();
            CampaignCriterionDTO dto = new CampaignCriterionDTO();
            DTOFactory.i.copyToDTO(cc, dto);
            dto.put("campaignId", targetCampaign.getCampaignId());
            dto.remove("valueId");
            dto.remove("campaignCriterionId");
            CampaignFreeText f = copyFreeText(cc.getCampaignFreeText());
            if (null != f) {
                f.setType(FreeTextTypes.FREETEXT_CAMPAIGN_CRITERION);
                dto.put("valueId", f.getFreeTextId());
            }
            dto.remove("version");

            try {
                h.create(dto);
            } catch (CreateException e) {
                log.error("EXCEPTION: ", e);
            }
        }
    }

    private CampaignFreeText copyFreeText(CampaignFreeText sourceFreeText) {
        CampaignFreeTextHome h = (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);
        CampaignFreeText frtx = null;
        if (null != sourceFreeText) {
            try {
                frtx = h.create(sourceFreeText.getValue(), sourceFreeText.getCompanyId(), sourceFreeText.getType());
            } catch (CreateException e) {
                log.debug("Cannot create FreeText...");
            }
        }
        return frtx;
    }
}
