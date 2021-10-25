package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.common.Attachment;
import com.piramide.elwis.domain.common.AttachmentHome;
import com.piramide.elwis.domain.common.FileFreeText;
import com.piramide.elwis.domain.common.FileFreeTextHome;
import com.piramide.elwis.dto.campaignmanager.AttachDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignGenAttachDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignGenTextDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignGenerationDTO;
import com.piramide.elwis.dto.common.AttachmentDTO;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public class CampaignGenerationCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        log.debug("Executing CampaignGenerationCmd..... " + paramDTO);
        Integer generationId = null;

        String op = this.getOp();
        boolean isRead = true;
        if ("create".equals(op)) {
            isRead = false;
            generationId = create(sessionContext);
        }

        if (isRead) {
            generationId = read();
        }

        resultDTO.put("generationId", generationId);
    }

    private Integer create(SessionContext ctx) {
        Integer generationId = null;

        Integer templateId = new Integer(paramDTO.get("templateId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        List<Integer> attachIdList = new ArrayList<Integer>();
        if (paramDTO.containsKey("campAttachIdList")) {
            attachIdList = (List<Integer>) paramDTO.get("campAttachIdList");
        }

        CampaignTemplateHome campaignTemplateHome = (CampaignTemplateHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEMPLATE);
        CampaignTemplate campaignTemplate = null;
        try {
            campaignTemplate = campaignTemplateHome.findByPrimaryKey(templateId);
        } catch (FinderException e) {
            log.debug("Not found campaign templateId :" + templateId, e);
            return null;
        }

        if (campaignTemplate != null) {

            CampaignGenerationDTO generationDTO = new CampaignGenerationDTO(paramDTO);
            generationDTO.put("campaignId", paramDTO.get("campaignId"));
            generationDTO.put("activityId", paramDTO.get("activityId"));
            generationDTO.put("companyId", companyId);
            generationDTO.put("generationTime", new Long(System.currentTimeMillis()));
            generationDTO.put("templateId", templateId);
            generationDTO.put("userId", paramDTO.get("userId"));


            CampaignGeneration generation = (CampaignGeneration) ExtendedCRUDDirector.i.create(generationDTO, resultDTO, false);

            if (generation != null) {
                generationId = generation.getGenerationId();

                for (Iterator iterator = campaignTemplate.getCampaignText().iterator(); iterator.hasNext();) {
                    CampaignText campaignText = (CampaignText) iterator.next();
                    CampaignGenTextDTO genTextDTO = new CampaignGenTextDTO();
                    genTextDTO.put("companyId", companyId);
                    genTextDTO.put("generationId", generation.getGenerationId());
                    genTextDTO.put("languageId", campaignText.getLanguageId());
                    genTextDTO.put("isDefault", campaignText.getByDefault() != null ? campaignText.getByDefault() : Boolean.FALSE);

                    CampaignGenText campaignGenText = (CampaignGenText) ExtendedCRUDDirector.i.create(genTextDTO, resultDTO, false);
                    if (campaignGenText != null) {

                        CampaignFreeTextHome freeTextHome = (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);
                        try {
                            CampaignFreeText freeText = freeTextHome.create(campaignText.getCampaignFreeText().getValue(), companyId, FreeTextTypes.FREETEXT_CAMPAIGN_GEN_TEXT);
                            campaignGenText.setFreeTextId(freeText.getFreeTextId());

                            //create relations to image store, only if template is HTML
                            if (CampaignConstants.DocumentType.HTML.equal(campaignTemplate.getDocumentType())) {
                                createCampaignGenTextImg(campaignGenText, new String(freeText.getValue()), ctx);
                            }
                        } catch (CreateException e) {
                            log.error("Can't Create freeText element in Campaign module ", e);
                        }
                    }
                }

                //create generation attachs
                if (!attachIdList.isEmpty()) {
                    FileFreeTextHome fileFreeTextHome = (FileFreeTextHome) EJBFactory.i.getEJBLocalHome(Constants.JNDI_FILEFREETEXT);

                    for (Integer campAttachId : attachIdList) {
                        Attach attach = (Attach) ExtendedCRUDDirector.i.read(new AttachDTO(new Integer(campAttachId)), new ResultDTO(), false);
                        if (attach != null) {

                            FileFreeText fileFreeText = null;
                            try {
                                fileFreeText = fileFreeTextHome.create(attach.getCampaignFreeText().getValue(), companyId, FreeTextTypes.FREETEXT_ATTACHMENT);
                            } catch (CreateException e) {
                                log.debug("Can't create free text..", e);
                                continue;
                            }

                            AttachmentDTO attachmentDTO = new AttachmentDTO();
                            attachmentDTO.put("attachmentType", Constants.AttachmentType.CAMPGENERATION.getConstant());
                            attachmentDTO.put("companyId", companyId);
                            attachmentDTO.put("fileName", attach.getFilename());
                            attachmentDTO.put("fileSize", attach.getSize());
                            attachmentDTO.put("freeTextId", fileFreeText.getFileId());

                            Attachment attachment = (Attachment) ExtendedCRUDDirector.i.create(attachmentDTO, resultDTO, false);
                            if (attachment != null) {

                                //create relation with generation
                                CampaignGenAttachDTO genAttachDTO = new CampaignGenAttachDTO();
                                genAttachDTO.put("attachmentId", attachment.getAttachmentId());
                                genAttachDTO.put("companyId", companyId);
                                genAttachDTO.put("generationId", generationId);
                                ExtendedCRUDDirector.i.create(genAttachDTO, resultDTO, false);
                            }
                        }
                    }
                }
            }
        }

        return generationId;
    }

    private void createCampaignGenTextImg(CampaignGenText campaignGenText, String templateBody, SessionContext ctx) {

        CampaignGenTextImgCmd campaignGenTextImgCmd = new CampaignGenTextImgCmd();
        campaignGenTextImgCmd.putParam("op", "createFromTemplate");
        campaignGenTextImgCmd.putParam("companyId", campaignGenText.getCompanyId());
        campaignGenTextImgCmd.putParam("campaignGenTextId", campaignGenText.getCampaignGenTextId());
        campaignGenTextImgCmd.putParam("campaignTextBody", templateBody);

        campaignGenTextImgCmd.executeInStateless(ctx);
    }


    private Integer read() {
        Integer generationId = new Integer(paramDTO.get("generationId").toString());

        boolean checkReferences = false;
        if (null != paramDTO.get("withReferences")) {
            checkReferences = Boolean.valueOf(paramDTO.get("withReferences").toString());
        }
        CampaignGenerationDTO generationDTO = new CampaignGenerationDTO(generationId);
        CampaignGeneration generation = (CampaignGeneration) ExtendedCRUDDirector.i.read(generationDTO, resultDTO, checkReferences);
        if (generation != null) {
            generationId = generation.getGenerationId();

            readGenerationAttach(generation);

            //read from campaignSentLog
            CampaignSentLog campaignSentLog = generation.getCampaignSentLog();
            if (campaignSentLog != null) {
                resultDTO.put("generationKey", campaignSentLog.getGenerationKey());
            }
        }

        return generationId;
    }

    private void readGenerationAttach(CampaignGeneration generation) {
        AttachmentHome attachmentHome = (AttachmentHome) EJBFactory.i.getEJBLocalHome(Constants.JNDI_ATTACHMENT);

        List<Map> attachmentList = new ArrayList<Map>();
        for (Iterator iterator = generation.getGenerationAttachs().iterator(); iterator.hasNext();) {
            CampaignGenAttach campaignGenAttach = (CampaignGenAttach) iterator.next();

            Attachment attachment = null;
            try {
                attachment = attachmentHome.findByPrimaryKey(campaignGenAttach.getAttachmentId());
            } catch (FinderException e) {
                log.debug("Can't found attachment.. " + campaignGenAttach.getAttachmentId());
                continue;
            }

            Map attachmentMap = new HashMap();
            attachmentMap.put("attachmentId", attachment.getAttachmentId());
            attachmentMap.put("fileName", attachment.getFileName());
            attachmentMap.put("fileSize", attachment.getFileSize());

            attachmentList.add(attachmentMap);
        }

        resultDTO.put("genAttachmentList", attachmentList);
    }

    public boolean isStateful() {
        return false;
    }
}
