package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.dto.campaignmanager.CampaignTemplateDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignTextDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author Yumi
 * @version $Id: CampaignTemplateCmd.java 10515 2015-01-23 22:17:00Z miguel ${NAME}.java, v 2.0 20-jun-2004 17:52:26 Yumi Exp $
 */

public class CampaignTemplateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CampaignTemplate command with \n" +
                "paramDTO = " + paramDTO);
        /*Integer campaignId = Integer.valueOf(paramDTO.get("campaignId").toString().trim());
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString().trim());
        Integer languageId = Integer.valueOf(paramDTO.get("languageId").toString().trim());*/
        boolean isRead = true;
        if ("create".equals(getOp())) {
            isRead = false;
            create(ctx);
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update();
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(ctx);
        }
        if ("deleteTemplates".equals(getOp())) {
            isRead = false;
            deleteTemplates(ctx);
        }

        if (isRead && null != paramDTO.get("templateId")) {
            read();
        }
    }

    //todo hay que cambiar la llave primaria del bean campaigntext a templateID y languageID
    private void create(SessionContext ctx) {
        int companyId = new Integer(paramDTO.get("companyId").toString());

        List<String> l = Arrays.asList(
                new String[]{"companyId",
                        "description",
                        "documentType",
                        "campaignId"}
        );
        Map<String, Object> m = selectFromParamDTO(l);

        CampaignTemplateDTO dDto = new CampaignTemplateDTO();
        dDto.putAll(m);

        CampaignTemplate t = (CampaignTemplate) ExtendedCRUDDirector.i.create(dDto, resultDTO, false);

        ArrayByteWrapper pakedFile = (ArrayByteWrapper) paramDTO.get("packedFile");

        CampaignFreeTextHome frH =
                (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);

        CampaignFreeText myFreetext = null;
        try {
            myFreetext = frH.create(pakedFile.getFileData(),
                    companyId, FreeTextTypes.FREETEXT_CAMPAIGN_TEXT);
        } catch (CreateException e) {
            log.error("Cannot Create freeText element in Campaign module ", e);
        }

        if (null != myFreetext && null != t) {
            List<String> l2 = Arrays.asList(new String[]{"languageId",
                    "campaignId",
                    "companyId"});
            Map<String, Object> m2 = selectFromParamDTO(l2);

            CampaignTextDTO dto = new CampaignTextDTO();
            dto.putAll(m2);
            dto.put("freeTextId", myFreetext.getFreeTextId());
            dto.put("templateId", t.getTemplateId());
            dto.put("byDefault", true);

            ExtendedCRUDDirector.i.create(dto, resultDTO, false);

            //create relations to image store, only if template is HTML
            Integer languageId = new Integer((String) paramDTO.get("languageId"));
            if (CampaignConstants.DocumentType.HTML.equal(t.getDocumentType())) {
                createTemplateTextImg(t, languageId, new String(myFreetext.getValue()), ctx);
            }

            defineCampaignTextSize(t.getTemplateId(), languageId, ctx);
        }

        //if preview option has selected
        if (null != paramDTO.get("isPreview") && "true".equals(paramDTO.get("isPreview"))) {
            resultDTO.setForward("Preview");
        }
    }

    private void update() {
        CampaignTemplateDTO dto = new CampaignTemplateDTO();
        paramDTO.remove("documentType");
        dto.putAll(paramDTO);
        ExtendedCRUDDirector.i.update(dto, resultDTO, false, true, false, "Fail");
    }

    private void delete(SessionContext ctx) {
        CampaignTemplateDTO dto = new CampaignTemplateDTO(paramDTO);
        CampaignTemplate t = (CampaignTemplate) ExtendedCRUDDirector.i.read(dto, resultDTO, false);
        if (null != t && !resultDTO.isFailure()) {
            int templateId = new Integer(paramDTO.get("templateId").toString());
            int companyId = new Integer(paramDTO.get("companyId").toString());

            Object[] args = new Object[]{templateId, companyId};
            Collection files = (Collection) EJBFactory.i.callFinder(new CampaignTextDTO(), "findByTemplateId", args);
            List elementsToDelete = new ArrayList(files);
            for (int i = 0; i < elementsToDelete.size(); i++) {
                CampaignText e = (CampaignText) elementsToDelete.get(i);
                try {
                    //remove image store relations
                    deleteTemplateTextImg(e.getTemplateId(), e.getLanguageId(), ctx);

                    e.remove();
                } catch (RemoveException re) {
                    ctx.setRollbackOnly();
                    resultDTO.setForward("Fail");
                    return;
                }
            }

            try {
                t.remove();
            } catch (RemoveException e) {
                ctx.setRollbackOnly();
                resultDTO.setForward("Fail");
            }
        } else {
            resultDTO.setForward("Fail");
        }
    }

    private void read() {
        CampaignTemplateDTO dto = new CampaignTemplateDTO(paramDTO);
        ExtendedCRUDDirector.i.read(dto, resultDTO, true);
    }

    public boolean isStateful() {
        return false;
    }

    private Map<String, Object> selectFromParamDTO(List<String> l) {
        Map<String, Object> r = new HashMap<String, Object>();
        for (String k : l) {
            Object o = paramDTO.get(k);
            r.put(k, o);
        }
        return r;
    }

    private void deleteTemplates(SessionContext ctx) {
        int campaignId = new Integer(paramDTO.get("campaignId").toString());
        CampaignTemplateHome templateHome =
                (CampaignTemplateHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEMPLATE);

        List templates = new ArrayList();
        try {
            templates = new ArrayList(templateHome.findByCampaignId(campaignId));
        } catch (FinderException e) {
            log.debug("-> Cannot find templates for campaignId=" + campaignId);
        }
        for (Object object : templates) {
            CampaignTemplate template = (CampaignTemplate) object;
            Integer templateId = template.getTemplateId();
            Collection templatesByLanguage = template.getCampaignText();
            Iterator it = templatesByLanguage.iterator();
            while (it.hasNext()) {
                CampaignText templateByLanguage = (CampaignText) it.next();
                Integer languageId = templateByLanguage.getLanguageId();
                try {
                    //remove image store relations
                    deleteTemplateTextImg(templateByLanguage.getTemplateId(), templateByLanguage.getLanguageId(), ctx);

                    templateByLanguage.remove();
                    log.debug("-> Delete CampaignText languageId=" + languageId +
                            ", templateId =" + templateId + " OK");
                } catch (RemoveException e) {
                    log.debug("-> Delete CampaignText languageId=" + languageId +
                            ", templateId =" + templateId + " FAIL");
                    ctx.setRollbackOnly();
                    resultDTO.setResultAsFailure();
                }
                it = templatesByLanguage.iterator();
            }

            try {
                template.remove();
                log.debug("-> Delete CampaignTemplate templateId=" + templateId +
                        ", campaignId=" + campaignId + " OK");
            } catch (RemoveException e) {
                log.debug("-> Delete CampaignTemplate templateId=" + templateId +
                        ", campaignId=" + campaignId + " FAIL");
                ctx.setRollbackOnly();
                resultDTO.setResultAsFailure();
            }
        }
    }

    private void createTemplateTextImg(CampaignTemplate campaignTemplate, Integer languageId, String templateBody, SessionContext ctx) {

        CampaignTextImgCmd campaignTextImgCmd = new CampaignTextImgCmd();
        campaignTextImgCmd.putParam("op", "createFromTemplate");
        campaignTextImgCmd.putParam("companyId", campaignTemplate.getCompanyId());
        campaignTextImgCmd.putParam("templateId", campaignTemplate.getTemplateId());
        campaignTextImgCmd.putParam("languageId", languageId);
        campaignTextImgCmd.putParam("campaignTextBody", templateBody);

        campaignTextImgCmd.executeInStateless(ctx);
    }

    private void deleteTemplateTextImg(Integer templateId, Integer languageId, SessionContext ctx) {

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

}
