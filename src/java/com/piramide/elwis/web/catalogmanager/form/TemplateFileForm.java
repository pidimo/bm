package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.cmd.campaignmanager.util.HtmlTemplateToVelocity;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.web.common.util.HTMLEntityDecoder;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.FileValidator;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.contactmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel
 * @version $Id: TemplateFileForm.java 2009-05-13 11:39:20 AM $
 */
public class TemplateFileForm extends DefaultForm {
    FormFile file;

    public TemplateFileForm() {
        super();
    }

    public FormFile getFile() {
        return file;
    }

    public void setFile(FormFile file) {
        this.file = file;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Executing validate TemplateFileForm......" + getDtoMap());
        String op = (String) getDto("op");

        ActionErrors errors = super.validate(mapping, request);

        if (errors.isEmpty()) {
            String mediaType = getDto("mediaType").toString();
            validateAndSetDocumentWrapper(mediaType, request, errors);
        }

        //validate language
        if (GenericValidator.isBlankOrNull((String) getDto("languageId"))) {
            errors.add("languageError", new ActionError("errors.required", JSPHelper.getMessage(request, "Template.language")));
        } else {
            errors = ForeignkeyValidator.i.validate("language", "languageid", getDto("languageId"), errors, "Template.language");
        }

        return errors;
    }


    private void validateAndSetDocumentWrapper(String mediaType, HttpServletRequest request, ActionErrors errors) {
        ArrayByteWrapper wrapper = new ArrayByteWrapper();

        if (CatalogConstants.MediaType.HTML.equal(mediaType)) {
            String htmlData = (String) getDto("editor");
            log.debug("->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            log.debug(htmlData);
            log.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<-");


            if (GenericValidator.isBlankOrNull(htmlData)) {
                errors.add("htmlEditorError", new ActionError("errors.required", JSPHelper.getMessage(request, "Template.htmlText")));
            } else {
                validateHtmlTelecomTypes(htmlData, request, errors);
                if (errors.isEmpty()) {
                    wrapper.setFileData(htmlData.getBytes());
                }
            }
        }

        if (CatalogConstants.MediaType.WORD.equal(mediaType)) {
            FormFile f = (FormFile) getDto("file");
            ActionError error = FileValidator.i.validateWordTemplate(f, request, "Template.file");
            if (error != null) {
                errors.add("wordFileError", error);
            } else {
                setFile(f);
                try {
                    wrapper.setFileData(f.getFileData());
                } catch (IOException e) {
                    log.error("-> Cannot create ArrayByteWrapper object ", e);
                }
            }
        }

        if (errors.isEmpty()) {
            setDto("packedFile", wrapper);
        }
    }

    private void validateHtmlTelecomTypes(String editorContent, HttpServletRequest request, ActionErrors errors) {
        List<LabelValueBean> actualTelecomTypes = Functions.getTelecomTypes(request);

        Map<Integer, List<String>> templateTelecomTypeIdsMap = HtmlTemplateToVelocity.findTelecomTypeIdsVariableInTemplate(new StringBuilder(editorContent));

        for (Integer telecomTypeId : templateTelecomTypeIdsMap.keySet()) {
            if (!existTelecomTypeIdInCompany(telecomTypeId, actualTelecomTypes)) {

                for (String fieldLabel : templateTelecomTypeIdsMap.get(telecomTypeId)) {
                    fieldLabel = HTMLEntityDecoder.decode(fieldLabel);
                    ActionError error = new ActionError("Campaign.htmlTemplate.variableError", fieldLabel);
                    errors.add("telecomError", error);
                }
            }
        }
    }

    private boolean existTelecomTypeIdInCompany(Integer telecomTypeId, List<LabelValueBean> telecomTypeList) {
        for (LabelValueBean labelValueBean : telecomTypeList) {
            if (telecomTypeId.toString().equals(labelValueBean.getValue())) {
                return true;
            }
        }
        return false;
    }
}
