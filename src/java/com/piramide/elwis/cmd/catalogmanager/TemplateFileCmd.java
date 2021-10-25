package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.dto.catalogmanager.TemplateDTO;
import com.piramide.elwis.dto.catalogmanager.TemplateTextDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.*;
import java.util.Collection;
import java.util.Iterator;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: TemplateFileCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class TemplateFileCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("executeInStateless(javax.ejb.SessionContext)");
        log.debug("paramDTO  : " + paramDTO);

        TemplateTextHome tTxhome = (TemplateTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TEMPLATETEXT);

        FreeTextHome frHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);

        TemplateHome templateHome = (TemplateHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TEMPLATE);

        Template template = null;

        try {
            template = templateHome.findByPrimaryKey(new Integer((String) paramDTO.get("templateId")));
        } catch (FinderException e) {
            log.error("Cannot find template");
            new TemplateDTO(paramDTO).addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Fail");
            return;
        }

        if ("uploadFile".equals(this.getOp())) {

            boolean bydefault = false;
            if (paramDTO.get("byDefault") != null) {
                Collection collection = template.getTemplateText();
                for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                    TemplateText o = (TemplateText) iterator.next();
                    if (o.getByDefault().booleanValue()) {
                        o.setByDefault(Boolean.valueOf(false));
                    }
                }
                bydefault = true;
            }

            ArrayByteWrapper wrapper = (ArrayByteWrapper) paramDTO.get("packedFile");
            try {
                FreeText fr = frHome.create(wrapper.getFileData(), new Integer(paramDTO.get("companyId").toString()),
                        new Integer(FreeTextTypes.FREETEXT_TEMPLATE_TEXT));
                Integer languageId = new Integer((String) paramDTO.get("languageId"));

                TemplateTextDTO dto = new TemplateTextDTO();
                dto.put("companyId", new Integer(paramDTO.get("companyId").toString()));
                dto.put("freeTextId", fr.getFreeTextId());
                dto.put("languageId", languageId);
                dto.put("templateId", new Integer(paramDTO.get("templateId").toString()));
                dto.put("byDefault", Boolean.valueOf(bydefault));
                tTxhome.create(dto);

                //create images store relations related to template text. only to HTML
                if (CatalogConstants.MediaType.HTML.equal(template.getMediaType())) {
                    createTemplateTextImg(template, languageId, new String(wrapper.getFileData()), ctx, false);
                }

            } catch (CreateException e) {
                if (e instanceof DuplicateKeyException) {
                    resultDTO.addResultMessage("TemplateFile.error.duplicate");
                    resultDTO.setForward("Input");
                } else {
                    log.error("Cannot Create Template... ", e);
                }
            }

            resultDTO.put("companyId", paramDTO.get("companyId"));
            resultDTO.put("templateId", paramDTO.get("templateId"));
            resultDTO.put("maxAttachSize", paramDTO.get("maxAttachSize"));
        }

        if ("deleteFileConfirmation".equals(getOp())) {
            log.debug("Comfirm elimination of template file... ");
            Integer languageId = new Integer((String) paramDTO.get("languageId"));
            Integer templateId = new Integer((String) paramDTO.get("templateId"));
            TemplateTextPK templateTextPK = new TemplateTextPK(languageId, templateId);
            TemplateText templateText = null;
            resultDTO.put("companyId", paramDTO.get("companyId"));
            resultDTO.put("templateId", paramDTO.get("templateId"));
            resultDTO.put("maxAttachSize", paramDTO.get("maxAttachSize"));
            try {
                templateText = tTxhome.findByPrimaryKey(templateTextPK);
            } catch (FinderException e) {
                log.error("Cannot find templateText...");
                resultDTO.addResultMessage("TemplateFile.error.find");
                resultDTO.setForward("Fail");
                return;
            }
            Language language = null;
            LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
            try {
                language = languageHome.findByPrimaryKey(languageId);
            } catch (FinderException e) {
                log.error("Cannot find language associated to template");
            }

            if (templateText.getByDefault().booleanValue()) {
                resultDTO.addResultMessage("TemplateFile.error.default");
                resultDTO.setForward("Fail");
                return;
            }
            resultDTO.put("languageName", language.getLanguageName());
            resultDTO.put("templateDescription", template.getDescription());
            resultDTO.put("languageId", languageId);
        }
        if ("delete".equals(getOp()) && paramDTO.get("cancel") == null) {
            Integer languageId = new Integer((String) paramDTO.get("languageId"));
            Integer templateId = new Integer((String) paramDTO.get("templateId"));
            TemplateTextPK templateTextPK = new TemplateTextPK(languageId, templateId);
            try {
                TemplateText templateText = tTxhome.findByPrimaryKey(templateTextPK);
                if (templateText.getByDefault().booleanValue()) {
                    resultDTO.addResultMessage("TemplateFile.error.default");
                    resultDTO.setForward("Fail");
                    return;
                } else {
                    //remove image store relations
                    deleteTemplateTextImg(templateText.getTemplateId(), templateText.getLanguageId(), ctx);
                    templateText.remove();
                }
            } catch (FinderException e) {
                log.error("Cannot find templateText...");
                resultDTO.addResultMessage("TemplateFile.error.find");
                resultDTO.setForward("Fail");
                return;
            } catch (RemoveException e) {
                log.error("Cannot remove templateText...", e);
                ctx.setRollbackOnly();
            }
            resultDTO.put("companyId", paramDTO.get("companyId"));
            resultDTO.put("templateId", paramDTO.get("templateId"));
            resultDTO.put("maxAttachSize", paramDTO.get("maxAttachSize"));
        }

        if ("changeDefault".equals(getOp())) {
            log.debug("change the default file");
            resultDTO.put("companyId", paramDTO.get("companyId"));
            resultDTO.put("templateId", paramDTO.get("templateId"));
            resultDTO.put("maxAttachSize", paramDTO.get("maxAttachSize"));
            Integer languageId = new Integer((String) paramDTO.get("languageId"));
            Integer templateId = new Integer((String) paramDTO.get("templateId"));
            TemplateTextPK pk = new TemplateTextPK(languageId, templateId);
            TemplateText templateText = null;
            try {
                templateText = tTxhome.findByPrimaryKey(pk);
                Collection templateTexts = template.getTemplateText();

                for (Iterator iterator = templateTexts.iterator(); iterator.hasNext();) {
                    TemplateText o = (TemplateText) iterator.next();
                    if (o.getLanguageId().equals(languageId)) {
                        o.setByDefault(Boolean.valueOf(true));
                    }
                    if (o.getByDefault().booleanValue() && !o.getLanguageId().equals(languageId)) {
                        o.setByDefault(Boolean.valueOf(false));
                    }
                }
            } catch (FinderException e) {
                resultDTO.addResultMessage("TemplateFile.error.find");
                resultDTO.setForward("Fail");
                return;
            }

        }

        if ("update".equals(getOp())) {
            update(template, ctx);
        }

        if ("read".equals(getOp())) {
            read(template);
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void update(Template template, SessionContext ctx) {

        Integer languageId = new Integer((String) paramDTO.get("languageId"));
        Integer templateId = new Integer((String) paramDTO.get("templateId"));
        TemplateTextPK templateTextPK = new TemplateTextPK(languageId, templateId);

        TemplateTextDTO dto = new TemplateTextDTO();
        dto.setPrimKey(templateTextPK);
        dto.putAll(paramDTO);
        dto.remove("languageId");
        dto.remove("templateId");

        TemplateText templateText = (TemplateText) ExtendedCRUDDirector.i.update(dto, resultDTO, false, true, false, "Fail");
        if (templateText != null) {
            if (!resultDTO.isFailure()) {
                ArrayByteWrapper wrapper = (ArrayByteWrapper) paramDTO.get("packedFile");
                if (wrapper != null) {
                    templateText.getFreeText().setValue(wrapper.getFileData());

                    //create images store relations related to template text. only to HTML
                    if (CatalogConstants.MediaType.HTML.equal(template.getMediaType())) {
                        createTemplateTextImg(template, languageId, new String(wrapper.getFileData()), ctx, true);
                    }
                }
            } else {
                read(template);
            }
        }
    }

    private void read(Template template) {

        Integer languageId = new Integer((String) paramDTO.get("languageId"));
        Integer templateId = new Integer((String) paramDTO.get("templateId"));
        TemplateTextPK templateTextPK = new TemplateTextPK(languageId, templateId);

        TemplateTextDTO dto = new TemplateTextDTO();
        dto.setPrimKey(templateTextPK);

        TemplateText templateText = (TemplateText) ExtendedCRUDDirector.i.read(dto, resultDTO, false);

        if (templateText != null) {

            if (CatalogConstants.MediaType.HTML.equal(template.getMediaType())) {
                String data = new String(templateText.getFreeText().getValue());
                resultDTO.put("editor", data);
            }

            LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
            Language l = null;
            try {
                l = languageHome.findByPrimaryKey(languageId);
                resultDTO.put("languageName", l.getLanguageName());
            } catch (FinderException e) {
                log.error("Cannot find language associated to template");
            }

        }
        resultDTO.put("mediaType", template.getMediaType());
        resultDTO.put("templateDescription", template.getDescription());
    }

    private void createTemplateTextImg(Template template, Integer languageId, String templateBody, SessionContext ctx, boolean isUpdate) {

        TemplateTextImgCmd templateTextImgCmd = new TemplateTextImgCmd();
        templateTextImgCmd.putParam("op", isUpdate ? "updateFromTemplate" : "createFromTemplate");
        templateTextImgCmd.putParam("companyId", template.getCompanyId());
        templateTextImgCmd.putParam("templateId", template.getTemplateId());
        templateTextImgCmd.putParam("languageId", languageId);
        templateTextImgCmd.putParam("templateBody", templateBody);

        templateTextImgCmd.executeInStateless(ctx);
    }

    private void deleteTemplateTextImg(Integer templateId, Integer languageId, SessionContext ctx) {

        TemplateTextImgCmd templateTextImgCmd = new TemplateTextImgCmd();
        templateTextImgCmd.putParam("op", "deleteFromTemplate");
        templateTextImgCmd.putParam("templateId", templateId);
        templateTextImgCmd.putParam("languageId", languageId);

        templateTextImgCmd.executeInStateless(ctx);
    }

}
