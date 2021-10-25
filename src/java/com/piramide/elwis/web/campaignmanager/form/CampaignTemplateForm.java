package com.piramide.elwis.web.campaignmanager.form;

import com.piramide.elwis.cmd.campaignmanager.util.HtmlTemplateToVelocity;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.util.HTMLEntityDecoder;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.FileValidator;
import com.piramide.elwis.web.contactmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CampaignTemplateForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CampaignTemplateForm extends DefaultForm {
    private Log log = LogFactory.getLog(this.getClass());
    private final String LANGUAGESLIST = "languages"; // the representation of arrayList on the hash map
    private final String SIZELIST = "sizeLanguages"; // the representation of size the arrayList on the hash map

    FormFile file;
    ArrayList files; // List of Templates was be uploaded
    ArrayList languages; // list of languages assign for template uploaded
    String radioButtons = ""; // the radio button is selected
    Integer campaignId;

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public CampaignTemplateForm() {
        super();
        files = new ArrayList();
        languages = new ArrayList();
    }

    public FormFile getFile() {
        return file;
    }

    public void setFile(FormFile file) {
        this.file = file;
    }

    public ArrayList getFiles() {
        return files;
    }

    public void setFiles(ArrayList files) {
        this.files = files;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = super.validate(mapping, request);

        //this validations only executes when operation is create
        if ("create".equals((String) getDto("op"))) {
            if (GenericValidator.isBlankOrNull((String) getDto("documentType"))) {
                errors.add("documentType", new ActionError("errors.required", JSPHelper.getMessage(request, "Template.documenType")));
            }
            if (errors.isEmpty()) {
                ActionError myErrors = null;
                if (null != (myErrors = validateByDocumentType(request))) {
                    errors.add("editorErrors", myErrors);
                } else {
                    int documentType = new Integer(getDto("documentType").toString());
                    if (CampaignConstants.DocumentType.HTML.getConstant() == documentType) {
                        List<ActionError> errorsAsList = validateHtmlTelecomTypes(request);
                        if (null != errorsAsList && !errorsAsList.isEmpty()) {
                            for (int i = 0; i < errorsAsList.size(); i++) {
                                errors.add("VariableErrror_" + i, errorsAsList.get(i));
                            }
                        }
                    }
                }
            }
            if (GenericValidator.isBlankOrNull((String) getDto("languageId"))) {
                errors.add("languageId", new ActionError("errors.required", JSPHelper.getMessage(request, "Template.language")));
            }


            if (errors.isEmpty()) {
                int documentType = new Integer(getDto("documentType").toString());
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
                setDto("packedFile", wrapper);
            } else {
                //recover status on file upload or html document upload when ocurs error
                int documentType = -1;
                String htmlDisplay = "display:none";
                String wordDisplay = "display:none";
                String buttonDisplay = "display:none";
                try {
                    documentType = new Integer(getDto("documentType").toString());
                } catch (NumberFormatException nfe) {
                }
                if (CampaignConstants.DocumentType.HTML.getConstant() == documentType) {
                    htmlDisplay = "";
                    buttonDisplay = "";
                    wordDisplay = "display:none";
                }
                if (CampaignConstants.DocumentType.WORD.getConstant() == documentType) {
                    htmlDisplay = "display:none";
                    buttonDisplay = "display:none";
                    wordDisplay = "";
                }
                request.setAttribute("htmlDisplay", htmlDisplay);
                request.setAttribute("wordDisplay", wordDisplay);
                request.setAttribute("previewButtonDisplay0", buttonDisplay);
                request.setAttribute("previewButtonDisplay1", buttonDisplay);
            }
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