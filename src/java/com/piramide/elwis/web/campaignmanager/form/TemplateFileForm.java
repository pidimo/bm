package com.piramide.elwis.web.campaignmanager.form;

import com.piramide.elwis.cmd.campaignmanager.CampaignTemplateFileCmd;
import com.piramide.elwis.cmd.campaignmanager.util.HtmlTemplateToVelocity;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.util.HTMLEntityDecoder;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.FileValidator;
import com.piramide.elwis.web.contactmanager.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: ivan
 * Date: 28-10-2006: 04:37:26 PM
 */
public class TemplateFileForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        errors = super.validate(mapping, request);
        if (GenericValidator.isBlankOrNull((String) getDto("languageId"))) {
            errors.add("languageId", new ActionError("errors.required", JSPHelper.getMessage(request, "Template.language")));
        }

        ActionError myError = null;

        int documentType = new Integer(getDto("documentType").toString());
        if (null != (myError = validateByDocumentType(request))) {
            errors.add("editorErrors", myError);
        } else {
            if (CampaignConstants.DocumentType.HTML.getConstant() == documentType) {
                List<ActionError> errorsAsList = validateHtmlTelecomTypes(request);
                if (null != errorsAsList && !errorsAsList.isEmpty()) {
                    for (int i = 0; i < errorsAsList.size(); i++) {
                        errors.add("VariableErrror_" + i, errorsAsList.get(i));
                    }
                }
            }
        }

        if (null != getDto("languageId") && "create".equals(getDto("op")) && errors.isEmpty()) {
            CampaignTemplateFileCmd cmd = new CampaignTemplateFileCmd();
            cmd.setOp("checkDuplicate");
            cmd.putParam("languageId", getDto("languageId"));
            cmd.putParam("templateId", getDto("templateId"));
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
                boolean isDuplicaded = (Boolean) resultDTO.get("isDuplicaded");
                if (isDuplicaded) {
                    errors.add("duplicated", new ActionError("TemplateFile.error.duplicate"));
                }
            } catch (AppLevelException e) {

            }
        }

        if (errors.isEmpty()) {

            ArrayByteWrapper wrapper = null;
            if (CampaignConstants.DocumentType.HTML.getConstant() == documentType) {
                String data = (String) getDto("editor");
                wrapper = new ArrayByteWrapper(data.getBytes());
            }
            if (CampaignConstants.DocumentType.WORD.getConstant() == documentType) {
                FormFile f = (FormFile) getDto("file");
                try {
                    wrapper = new ArrayByteWrapper(f.getFileData());
                } catch (IOException ioe) {
                    log.error("Cannot read File data ", ioe);
                }
            }
            if (null != getDto("byDefault") && "true".equals(getDto("byDefault").toString())) {
                setDto("byDefault", true);
            } else {
                setDto("byDefault", false);
            }
            setDto("packedFile", wrapper);
        }
        return errors;
    }

    private ActionError validateByDocumentType(HttpServletRequest request) {
        ActionError error = null;
        int documentType = new Integer(getDto("documentType").toString());
        if (CampaignConstants.DocumentType.HTML.getConstant() == documentType) {
            if (GenericValidator.isBlankOrNull((String) getDto("editor"))) {
                error = new ActionError("errors.required", JSPHelper.getMessage(request, "Template.htmlText"));
                return error;
            }
        }
        if (CampaignConstants.DocumentType.WORD.getConstant() == documentType) {
            FormFile f = (FormFile) getDto("file");
            error = FileValidator.i.validateWordTemplate(f, request, "Template.file");
        }
        return error;
    }

    private List<ActionError> validateHtmlTelecomTypes(HttpServletRequest request) {
        List<ActionError> errors = new ArrayList<ActionError>();
        List<LabelValueBean> actualTelecomTypes = Functions.getTelecomTypes(request);
        String editorContent = (String) getDto("editor");

        Map<Integer, List<String>> templateTelecomTypeIdsMap =
                HtmlTemplateToVelocity.findTelecomTypeIdsVariableInTemplate(new StringBuilder(editorContent));
        log.debug("Editor content :");
        log.debug(editorContent);
        for (Integer telecomTypeId : templateTelecomTypeIdsMap.keySet()) {
            if (!existTelecomTypeIdInCompany(telecomTypeId, actualTelecomTypes)) {

                for (String fieldLabel : templateTelecomTypeIdsMap.get(telecomTypeId)) {
                    fieldLabel = HTMLEntityDecoder.decode(fieldLabel);
                    ActionError error = new ActionError("Campaign.htmlTemplate.variableError", fieldLabel);
                    errors.add(error);
                }
            }
        }

        return errors;
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
