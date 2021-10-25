package com.piramide.elwis.web.contactmanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Yumi
 * @version $Id: UploadForm.java 1066 2004-06-05 14:20:27Z mauren $
 */
public class UploadForm extends DefaultForm {

    FormFile file;

    public UploadForm() {
        super();
    }

    public FormFile getFile() {
        return file;
    }

    public void setFile(FormFile file) {
        this.file = file;
    }

    public void reset() {

    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        //has the maximum length been exceeded?
        Boolean maxLengthExceeded = (Boolean)
                request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
        errors = super.validate(mapping, request);
        if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())) {
            errors.add("maxLengthExceeded", new ActionError("Common.Error_maxLengthExceeded"));
        }

        /*if (file != null)
            if (!GenericValidator.isBlankOrNull(file.getFileName())) {
                if (!(file.getContentType().equals("application/msword")))
                    errors.add("file_typeNotAllowed", new ActionError("File.error_TypeNotAllowed", ".doc(Word file)"));
                if (file.getFileSize() > (1 * 1024000))
                    errors.add("file_sizeExceeded", new ActionError("File.error_sizeExceeded"));
            }*/

        return errors;
    }


}
