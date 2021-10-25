package com.piramide.elwis.web.financemanager.form;

import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.common.validator.FileValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.4
 */
public class IncomingInvoiceForm extends DefaultForm {
    FormFile file;

    public IncomingInvoiceForm() {
    }

    public FormFile getInvoiceFile() {
        return file;
    }

    public void setInvoiceFile(FormFile file) {
        this.file = file;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate IncomingInvoiceForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        validateInvoiceDocument(errors, request);

        return errors;
    }

    private void validateInvoiceDocument(ActionErrors errors, HttpServletRequest request) {

        if (file != null && !GenericValidator.isBlankOrNull(file.getFileName())) {

            Boolean maxLengthExceeded = (Boolean) request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);

            if (null != maxLengthExceeded && maxLengthExceeded.booleanValue()) {
                errors.add("maxLengthExceeded", new ActionError("Common.Error_maxLengthExceeded"));
                return;
            }

            byte[] data = checkFileProperties(null, errors);

            if (data != null) {
                ArrayByteWrapper wrapper = new ArrayByteWrapper(data);
                wrapper.setFileName(file.getFileName());

                setDto("fileWrapper", wrapper);
            }
        }
    }

    private byte[] checkFileProperties(Integer maxSize, ActionErrors errors) {
        byte[] data = null;

        String fileExtension = FileValidator.i.getFileExtension(file.getFileName());

        if ("pdf".equals(fileExtension)) {
            //OBS: when upload from firefox content type of pdf is 'application/download'
            ActionError pdfTypError = FileValidator.i.validateType(file, ".pdf", "application/pdf", "application/download");
            if (pdfTypError != null) {
                errors.add("pdfError", pdfTypError);
                return null;
            }
        } else {
            errors.add("fileExtension", new ActionError("File.error.allowedTypes", file.getFileName(), ".pdf"));
            return null;
        }

        try {
            data = file.getFileData();
            if (null != data && 0 == data.length) {
                errors.add("file_notFound", new ActionError("Common.error.fileInvalid", file.getFileName()));
                return null;
            }
        } catch (IOException e) {
            errors.add("file_notFound", new ActionError("Common.error.fileNotFound"));
            return null;
        }

        if (data != null && maxSize != null) {
            double actualSize = file.getFileSize() / Math.pow(1024, 2);
            if (actualSize > maxSize) {
                errors.add("attach", new ActionError("Common.error.fileExceedSize", new Integer(maxSize)));
                return null;
            }
        }
        return data;
    }
}
