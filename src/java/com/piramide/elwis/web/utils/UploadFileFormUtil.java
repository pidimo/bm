package com.piramide.elwis.web.utils;

import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.FileValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class UploadFileFormUtil {
    private Log log = LogFactory.getLog(UploadFileFormUtil.class);

    private String fileDTOKey;
    private String fileResourceKey;
    private String languageDTOKey;
    private String languageResourceKey;

    private ArrayByteWrapper wrappedFile = new ArrayByteWrapper();

    public UploadFileFormUtil(String fileDTOKey,
                              String fileResourceKey,
                              String languageDTOKey,
                              String languageResourceKey) {
        this.fileDTOKey = fileDTOKey;
        this.fileResourceKey = fileResourceKey;
        this.languageDTOKey = languageDTOKey;
        this.languageResourceKey = languageResourceKey;
    }

    public void validateUploadWordTemplate(HttpServletRequest request,
                                           DefaultForm form,
                                           ActionErrors errors) {
        FormFile file = (FormFile) form.getDto(fileDTOKey);
        ActionError fileValidation =
                FileValidator.i.validateWordTemplate(file, request, fileResourceKey);
        if (null != fileValidation) {
            errors.add("fileError", fileValidation);
        }

        ActionError languageValidation = validateLanguage(request, form);
        if (null != languageValidation) {
            errors.add("languageError", languageValidation);
        }

        if (errors.isEmpty()) {
            buildFileWrapper(file);
        }
    }

    private ActionError validateLanguage(HttpServletRequest request, DefaultForm form) {
        String languageId = (String) form.getDto(languageDTOKey);
        if (GenericValidator.isBlankOrNull(languageId)) {
            return new ActionError("errors.required",
                    JSPHelper.getMessage(request, languageResourceKey));
        }
        return null;
    }

    private void buildFileWrapper(FormFile file) {
        try {
            wrappedFile.setFileData(file.getFileData());
            wrappedFile.setFileName(file.getFileName());
            log.debug("->Build wrappedFile for " + file.getFileName() + " OK");
        } catch (IOException e) {
            log.error("->Read " + file.getFileName() + "Fail", e);
        }
    }

    public ArrayByteWrapper getWrappedFile() {
        return wrappedFile;
    }
}
