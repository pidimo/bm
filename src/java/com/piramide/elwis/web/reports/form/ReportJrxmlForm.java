package com.piramide.elwis.web.reports.form;

import com.jatun.titus.reportgenerator.LoadJrxmlException;
import com.jatun.titus.reportgenerator.util.ReportGeneratorConstants;
import com.jatun.titus.reportgenerator.util.ReportJrxmlParameterUtil;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.FileValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10
 */
public class ReportJrxmlForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate ReportExecuteForm......." + getDtoMap());
        setReportDefaultValues();

        ActionErrors errors = super.validate(mapping, request);

        if (errors.isEmpty()) {
            validateAndSetFileWrapper(errors, request);
        }

        return errors;
    }

    private void setReportDefaultValues() {
        setDto("reportType", ReportConstants.SINGLE_TYPE.toString());
        setDto("pageSize", ReportGeneratorConstants.PAGE_A4);
        setDto("pageOrientation", ReportGeneratorConstants.PAGE_ORIENTATION_PORTRAIT);
    }

    private void validateAndSetFileWrapper(ActionErrors errors, HttpServletRequest request) {
        FormFile formFile = (FormFile) getDto("file");

        if ("update".equals(getDto("op")) && GenericValidator.isBlankOrNull(formFile.getFileName())) {
            return;
        }

        if (GenericValidator.isBlankOrNull(formFile.getFileName())) {
            errors.add("fileError", new ActionError("errors.required", JSPHelper.getMessage(request, "Report.jrxml.file")));
            return;
        }

        ActionError error = FileValidator.i.validateContent(formFile);
        if (error != null) {
            errors.add("fileError", error);
            return;
        }

        String fileExt = FileValidator.i.getFileExtension(formFile.getFileName());
        if (fileExt == null || !"jrxml".equals(fileExt.toLowerCase())) {
            errors.add("fileError", new ActionError("File.error.allowedTypes", formFile.getFileName(), "jrxml"));
            return;
        }

        ArrayByteWrapper wrapper = new ArrayByteWrapper();
        try {
            wrapper.setFileName(formFile.getFileName());
            wrapper.setFileData(formFile.getFileData());
        } catch (IOException e) {
            log.debug("Error in read file... " + formFile.getFileName());
            errors.add("fileError", new ActionError("Common.error.fileInvalid", formFile.getFileName()));
        }

        validateAndSetQueryParameters(wrapper, errors, request);

        if (errors.isEmpty()) {
            setDto("fileWrapper", wrapper);
        }
    }

    private void validateAndSetQueryParameters(ArrayByteWrapper wrapper, ActionErrors errors, HttpServletRequest request) {
        if (errors.isEmpty() && wrapper.getFileData() != null) {
            InputStream inputStream = new ByteArrayInputStream(wrapper.getFileData());

            Set<String> allParameters = new HashSet<String>();
            try {
                allParameters = ReportJrxmlParameterUtil.getQueryStringParameters(inputStream);

                if (allParameters.contains("companyId")) {
                    //remove companyId parameter because this is passed as default
                    allParameters.remove("companyId");
                } else {
                    errors.add("queryParamErr", new ActionError("Report.jrxml.error.paramCompanyId"));
                }
            } catch (LoadJrxmlException e) {
                log.debug("Error in load jrxml...", e);
                errors.add("loadErr", new ActionError("Report.jrxml.error.upload", wrapper.getFileName(), e.getMessage() != null ? e.getMessage() : ""));
            }

            setDto("queryParameterList", new ArrayList<String>(allParameters));
        }
    }
}
