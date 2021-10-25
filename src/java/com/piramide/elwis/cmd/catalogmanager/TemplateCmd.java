package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.dto.catalogmanager.TemplateDTO;
import com.piramide.elwis.dto.catalogmanager.TemplateTextDTO;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, control concurrency,
 * referencial integriry and entry duplicated); all relatinated with the templates
 *
 * @author Yumi
 * @version $Id: TemplateCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class TemplateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Execute template  command");
        boolean isRead = true;

        TemplateDTO templateDTO = new TemplateDTO(paramDTO);
        Template template = null;
        if ("create".equals(getOp())) {
            isRead = false;
            template = (Template) CRUDDirector.i.doCRUD(getOp(), templateDTO, resultDTO);

            ArrayByteWrapper wrapper = (ArrayByteWrapper) paramDTO.get("packedFile");

            TemplateTextHome tTxhome = (TemplateTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TEMPLATETEXT);
            FreeTextHome frHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);

            try {
                FreeText fr = frHome.create(wrapper.getFileData(), template.getCompanyId(),
                        new Integer(FreeTextTypes.FREETEXT_TEMPLATE_TEXT));

                Integer languageId = new Integer((String) paramDTO.get("languageId"));

                TemplateTextDTO dto = new TemplateTextDTO();
                dto.put("companyId", template.getCompanyId());
                dto.put("freeTextId", fr.getFreeTextId());
                dto.put("languageId", languageId);
                dto.put("templateId", template.getTemplateId());
                dto.put("byDefault", Boolean.valueOf("true"));
                tTxhome.create(dto);

                //create images store relations related to template text. only to HTML
                if (CatalogConstants.MediaType.HTML.equal(template.getMediaType())) {
                    createTemplateTextImg(template, languageId, new String(wrapper.getFileData()), ctx);
                }

            } catch (CreateException e) {
                log.error("Cannot Create Template...");
            }
        }
        if ("update".equals(getOp())) {
            isRead = false;
            /*if (resultDTO.isFailure()) return;
            // The version control
            if ("update".equals(getOp())) {*/
            VersionControlChecker.i.check(new TemplateDTO(paramDTO), resultDTO, paramDTO);
            if (resultDTO.get("EntityBeanNotFound") != null) {
                new TemplateDTO(paramDTO).addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
                return;
            }
            if (resultDTO.isFailure()) {
                setOp(CRUDDirector.OP_READ);
            }
            /*}*/
            templateDTO = new TemplateDTO(paramDTO);
            template = (Template) CRUDDirector.i.doCRUD(getOp(), templateDTO, resultDTO);
        }
        if ("delete".equals(getOp())) {
            isRead = false;

            if (paramDTO.getAsBool("withReferences")) {
                IntegrityReferentialChecker.i.check(new TemplateDTO(paramDTO), resultDTO);
                if (resultDTO.isFailure()) {
                    return;
                }
            }
            template = (Template) CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, templateDTO, resultDTO);
            if (!resultDTO.isFailure()) {
                try {
                    Collection templateTexts = new ArrayList(template.getTemplateText());
                    for (Iterator iterator = templateTexts.iterator(); iterator.hasNext();) {
                        TemplateText file = (TemplateText) iterator.next();

                        //remove image store relations
                        deleteTemplateTextImg(file.getTemplateId(), file.getLanguageId(), ctx);
                        //remove template text
                        file.remove();
                    }
                    template.remove();
                } catch (RemoveException e) {
                    log.error("Cannot remove Template");
                }
            } else {
                resultDTO.setForward("Fail");
                return;
            }
        }
        if (isRead) {
            if (paramDTO.getAsBool("withReferences")) {
                IntegrityReferentialChecker.i.check(new TemplateDTO(paramDTO), resultDTO);
                if (resultDTO.isFailure()) {
                    return;
                }
            }
            CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, templateDTO, resultDTO);
        }
    }

    private void createTemplateTextImg(Template template, Integer languageId, String templateBody, SessionContext ctx) {

        TemplateTextImgCmd templateTextImgCmd = new TemplateTextImgCmd();
        templateTextImgCmd.putParam("op", "createFromTemplate");
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

    public boolean isStateful() {
        return false;
    }
}