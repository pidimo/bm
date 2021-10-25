package com.piramide.elwis.web.webmail.form;

import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.common.validator.FileValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Miky
 * @version $Id: TemporalImageForm.java 2009-05-25 03:53:16 PM $
 */
public class TemporalImageForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate TemporalImageForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        validateImageFile(request, errors);

        //add the session id
        setDto("sessionId", request.getSession().getId());
        return errors;
    }

    public void validateImageFile(HttpServletRequest request, ActionErrors errors) {
        FormFile formFile = (FormFile) getDto("file");

        ActionError imageValidation = FileValidator.i.validateImageFile(formFile, request, "Mail.browseImage.file");
        if (null != imageValidation) {
            errors.add("image", imageValidation);
        }

        if (errors.isEmpty()) {
            try {
                ArrayByteWrapper wrapper = new ArrayByteWrapper();
                wrapper.setFileData(formFile.getFileData());
                wrapper.setFileName(formFile.getFileName());
                setDto("imageData", wrapper);
            } catch (IOException e) {
                log.debug("Error in read image", e);
            }
        }
    }
}
