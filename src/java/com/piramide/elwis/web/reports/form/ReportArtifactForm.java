package com.piramide.elwis.web.reports.form;

import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.FileValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10.1
 */
public class ReportArtifactForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate ReportArtifactForm......." + getDtoMap());
        ActionErrors errors = super.validate(mapping, request);

        if (errors.isEmpty()) {
            validateAndSetFileWrapper(errors, request);
        }

        return errors;
    }

    private void validateAndSetFileWrapper(ActionErrors errors, HttpServletRequest request) {
        FormFile formFile = (FormFile) getDto("file");

        if (GenericValidator.isBlankOrNull(formFile.getFileName())) {
            errors.add("fileError", new ActionError("errors.required", JSPHelper.getMessage(request, "Report.artifact.file")));
            return;
        }

        ActionError error = FileValidator.i.validateContent(formFile);
        if (error != null) {
            errors.add("fileError", error);
            return;
        }

        if (!isJrxmlFile(formFile)) {
            ActionError imgError = FileValidator.i.validateImageFile(formFile, request, "Report.artifact.file");
            if (imgError != null) {
                errors.add("fileError", new ActionError("Report.artifact.error.fileType", formFile.getFileName()));
                return;
            }
        }

        ArrayByteWrapper wrapper = new ArrayByteWrapper();
        try {
            wrapper.setFileName(formFile.getFileName());
            wrapper.setFileData(formFile.getFileData());
        } catch (IOException e) {
            log.debug("Error in read file... " + formFile.getFileName());
            errors.add("fileError", new ActionError("Common.error.fileInvalid", formFile.getFileName()));
        }

        if (errors.isEmpty()) {
            setDto("fileWrapper", wrapper);
            setDto("fileName", wrapper.getFileName());
        }
    }

    private boolean isJrxmlFile(FormFile formFile) {
        String fileExt = FileValidator.i.getFileExtension(formFile.getFileName());
        return (fileExt != null && "jrxml".equals(fileExt.toLowerCase()));
    }
}
