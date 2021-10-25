package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.CampaignResultPageList;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.domain.catalogmanager.LanguageHome;
import com.piramide.elwis.dto.campaignmanager.CampaignTextDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: ivan
 * Date: 28-10-2006: 06:24:40 PM
 */
public class CampaignTemplateFileCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        log.debug("PARAM DTO " + paramDTO);
        boolean isRead = true;
        if ("create".equals(getOp())) {
            isRead = false;
            create(sessionContext);
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update(sessionContext);
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(sessionContext);
        }
        if ("checkDuplicate".equals(getOp())) {
            isRead = false;
            checkDuplicate();
        }
        if ("getDefaultTemplate".equals(getOp())) {
            isRead = false;
            Integer templateId = (Integer) paramDTO.get("templateId");
            Integer companyId = (Integer) paramDTO.get("companyId");

            getDefaultTemplate(templateId, companyId);
        }

        if (isRead && null != paramDTO.get("templateId") && null != paramDTO.get("languageId")) {
            read();
        }
    }

    private void checkDuplicate() {
        int templateId = new Integer(paramDTO.get("templateId").toString());
        int languageId = new Integer(paramDTO.get("languageId").toString());

        CampaignTextPK pk = new CampaignTextPK(languageId, templateId);

        CampaignTextHome h = (CampaignTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEXT);
        try {
            h.findByPrimaryKey(pk);
            resultDTO.put("isDuplicaded", true);
        } catch (FinderException e) {
            resultDTO.put("isDuplicaded", false);
        }
    }

    private void delete(SessionContext ctx) {
        int templateId = new Integer(paramDTO.get("templateId").toString());
        int languageId = new Integer(paramDTO.get("languageId").toString());
        int companyId = new Integer(paramDTO.get("companyId").toString());
        int documentType = new Integer(paramDTO.get("documentType").toString());
        int campaignId = new Integer(paramDTO.get("campaignId").toString());

        CampaignTextPK pk = new CampaignTextPK(languageId, templateId);

        CampaignTextHome h = (CampaignTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEXT);

        try {
            CampaignText t = h.findByPrimaryKey(pk);
            if (!t.getByDefault()) {
                try {
                    //remove image store relations
                    deleteCampaignTextImg(t.getTemplateId(), t.getLanguageId(), ctx);

                    t.remove();
                    if (CampaignConstants.DocumentType.HTML.getConstant() == documentType) {
                        CampaignResultPageList.deleteCampaignTemplateDir(campaignId, templateId);
                    }
                } catch (RemoveException e) {
                }
            } else {
                resultDTO.addResultMessage("TemplateFile.error.default");
                resultDTO.setForward("Fail");
                return;
            }
        } catch (FinderException e) {
            resultDTO.addResultMessage("TemplateFile.error.find");
            resultDTO.setForward("Fail");
        }
    }


    private void update(SessionContext ctx) {

        int templateId = new Integer(paramDTO.get("templateId").toString());
        int companyId = new Integer(paramDTO.get("companyId").toString());
        int languageId = new Integer(paramDTO.get("languageId").toString());
        int documentType = new Integer(paramDTO.get("documentType").toString());
        int campaignId = new Integer(paramDTO.get("campaignId").toString());

        boolean isDefault = (Boolean) paramDTO.get("byDefault");

        CampaignTextHome h = (CampaignTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEXT);

        CampaignTextPK pk = new CampaignTextPK(languageId, templateId);

        CampaignText t = null;
        try {
            t = h.findByPrimaryKey(pk);
            int actualVersion = new Integer(paramDTO.get("version").toString());
            if (t.getVersion() != actualVersion) {
                resultDTO.addResultMessage("Common.error.concurrency");
                resultDTO.setResultAsFailure();
            } else {
                t.setVersion(actualVersion + 1);
            }

        } catch (FinderException e) {
            resultDTO.addResultMessage("TemplateFile.error.find");
            resultDTO.setForward("Fail");
        }

        if (null != t) {
            if (!resultDTO.isFailure()) {
                if (isDefault) {
                    CampaignText td = getDefaultCampaignText(templateId, companyId);
                    if (null != td) {
                        td.setByDefault(false);
                    }

                }

                t.setByDefault(isDefault);
                ArrayByteWrapper w = (ArrayByteWrapper) paramDTO.get("packedFile");
                t.getCampaignFreeText().setValue(w.getFileData());

                if (CampaignConstants.DocumentType.HTML.getConstant() == documentType) {
                    //create images store relations related to template text. only to HTML
                    createCampaignTextImg(t, new String(w.getFileData()), ctx, true);

                    //Deleting campaign cache HTML files
                    CampaignResultPageList.deleteCampaignTemplateDir(campaignId, templateId);
                }

                defineCampaignTextSize(templateId, languageId, ctx);

                //if preview option has selected
                if (null != paramDTO.get("isPreview") && "true".equals(paramDTO.get("isPreview"))) {
                    resultDTO.setForward("Preview");
                    resultDTO.put("documentType", documentType);
                }
            } else {
                paramDTO.setOp("read");
                read();
            }
        }

    }

    private void read() {

        String checkDefaultTemplateAsString = (String) paramDTO.get("checkDefaultTemplate");
        boolean checkDefaultTemplate = false;
        if (null != checkDefaultTemplateAsString && "true".equals(checkDefaultTemplateAsString.toString())) {
            checkDefaultTemplate = true;
        }


        int templateId = new Integer(paramDTO.get("templateId").toString());
        int languageId = new Integer(paramDTO.get("languageId").toString());
        int documentType = new Integer(paramDTO.get("documentType").toString());
        int companyId = Integer.valueOf(paramDTO.get("companyId").toString());


        CampaignTextHome h = (CampaignTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEXT);

        CampaignTextPK pk = new CampaignTextPK(languageId, templateId);

        CampaignText t = null;
        try {
            t = h.findByPrimaryKey(pk);

            if (checkDefaultTemplate) {
                List<CampaignText> files = getFilesByTemplate(templateId, companyId);
                if (files.size() == 1) {
                    resultDTO.addResultMessage("TemplateFile.error.CannotDeleteTemplate");
                    resultDTO.setForward("Fail");
                    return;
                }

                if (t.getByDefault()) {
                    resultDTO.addResultMessage("TemplateFile.error.default");
                    resultDTO.setForward("Fail");
                    return;
                }
            }

            CampaignTextDTO dto = new CampaignTextDTO();
            dto.renderDTO(t);
            resultDTO.putAll(dto);

            LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
            Language l = null;
            try {
                l = languageHome.findByPrimaryKey(languageId);
            } catch (FinderException e) {
                log.error("Cannot find language associated to template");
            }
            resultDTO.put("languageName", l.getLanguageName());

        } catch (FinderException e) {
            resultDTO.addResultMessage("TemplateFile.error.find");
            resultDTO.setForward("Fail");
        }
        if (null != t) {
            if (documentType == CampaignConstants.DocumentType.HTML.getConstant()) {
                String data = new String(t.getCampaignFreeText().getValue());
                resultDTO.put("editor", data);
            }
        }

    }

    private List<CampaignText> getFilesByTemplate(Integer templateId, Integer companyId) {
        List<CampaignText> result = new ArrayList<CampaignText>();
        CampaignTextHome campaignTextHome =
                (CampaignTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEXT);
        try {
            Collection files = campaignTextHome.findByTemplateId(templateId, companyId);
            if (null != files) {
                for (Object object : files) {
                    CampaignText file = (CampaignText) object;
                    result.add(file);
                }
            }
        } catch (FinderException e) {
            log.debug("->Execute findByTemplateId(" + templateId + ", " + companyId + ") FAIL");
        }
        return result;
    }

    private void create(SessionContext ctx) {
        int companyId = new Integer(paramDTO.get("companyId").toString());
        int templateId = new Integer(paramDTO.get("templateId").toString());
        int languageId = new Integer(paramDTO.get("languageId").toString());
        int documentType = new Integer(paramDTO.get("documentType").toString());

        ArrayByteWrapper wrapper = (ArrayByteWrapper) paramDTO.get("packedFile");
        CampaignFreeTextHome frtxHome =
                (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);
        CampaignFreeText e = null;
        try {
            e = frtxHome.create(wrapper.getFileData(), companyId, FreeTextTypes.FREETEXT_CAMPAIGN_TEXT);
        } catch (CreateException ce) {
        }
        if (null != e) {
            CampaignTextDTO dto = new CampaignTextDTO();
            dto.put("companyId", companyId);
            dto.put("campaignId", paramDTO.get("campaignId"));
            dto.put("freeTextId", e.getFreeTextId());
            dto.put("languageId", paramDTO.get("languageId"));
            dto.put("templateId", templateId);
            boolean isDefault = (Boolean) paramDTO.get("byDefault");
            if (isDefault) {
                CampaignText td = getDefaultCampaignText(templateId, companyId);
                if (null != td) {
                    td.setByDefault(false);
                }
            }
            dto.put("byDefault", isDefault);

            CampaignText tx = (CampaignText) ExtendedCRUDDirector.i.create(dto, resultDTO, true);

            if (tx != null) {
                //create relations to image store, only if template is HTML
                if (CampaignConstants.DocumentType.HTML.equal(documentType)) {
                    createCampaignTextImg(tx, new String(e.getValue()), ctx, false);
                }
            }

            //if preview option has selected
            if (null != paramDTO.get("isPreview") && "true".equals(paramDTO.get("isPreview"))) {
                resultDTO.setForward("Preview");
                resultDTO.put("documentType", paramDTO.get("documentType"));
            }

            defineCampaignTextSize(templateId, languageId, ctx);
        }
    }

    private CampaignTextDTO getDefaultTemplate(int templateId, int companyId) {
        CampaignText campaignText = getDefaultCampaignText(templateId, companyId);
        if (campaignText == null) {
            resultDTO.put("getDefaultTemplate", null);
            return null;
        }

        CampaignTextDTO dto = new CampaignTextDTO();
        DTOFactory.i.copyToDTO(campaignText, dto);
        resultDTO.put("getDefaultTemplate", dto);
        return dto;
    }

    private CampaignText getDefaultCampaignText(int templateId, int companyId) {
        CampaignTextHome h =
                (CampaignTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNTEXT);
        CampaignText t = null;
        try {
            t = h.findDefaultByTemplate(templateId, companyId);
        } catch (FinderException e) {
        }
        return t;
    }

    private void createCampaignTextImg(CampaignText campaignText, String templateBody, SessionContext ctx, boolean isUpdate) {

        CampaignTextImgCmd campaignTextImgCmd = new CampaignTextImgCmd();
        campaignTextImgCmd.putParam("op", isUpdate ? "updateFromTemplate" : "createFromTemplate");
        campaignTextImgCmd.putParam("companyId", campaignText.getCompanyId());
        campaignTextImgCmd.putParam("templateId", campaignText.getTemplateId());
        campaignTextImgCmd.putParam("languageId", campaignText.getLanguageId());
        campaignTextImgCmd.putParam("campaignTextBody", templateBody);

        campaignTextImgCmd.executeInStateless(ctx);
    }

    private void deleteCampaignTextImg(Integer templateId, Integer languageId, SessionContext ctx) {

        CampaignTextImgCmd campaignTextImgCmd = new CampaignTextImgCmd();
        campaignTextImgCmd.putParam("op", "deleteFromTemplate");
        campaignTextImgCmd.putParam("templateId", templateId);
        campaignTextImgCmd.putParam("languageId", languageId);

        campaignTextImgCmd.executeInStateless(ctx);
    }

    private void defineCampaignTextSize(Integer templateId, Integer languageId, SessionContext ctx) {

        CampaignTemplateUtilCmd campaignTemplateUtilCmd = new CampaignTemplateUtilCmd();
        campaignTemplateUtilCmd.putParam("op", "setCampaignTextSize");
        campaignTemplateUtilCmd.putParam("templateId", templateId);
        campaignTemplateUtilCmd.putParam("languageId", languageId);

        campaignTemplateUtilCmd.executeInStateless(ctx);
    }


    public boolean isStateful() {
        return false;
    }
}
