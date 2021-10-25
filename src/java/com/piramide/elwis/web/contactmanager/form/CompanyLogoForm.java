package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Form validate company
 *
 * @author Tayes
 * @version $Id: CompanyLogoForm.java 12515 2016-02-26 19:03:50Z miguel $
 */
public class CompanyLogoForm extends DefaultForm {
    private Log log = LogFactory.getLog(this.getClass());
    private FormFile imageFile;

    public FormFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(FormFile imageFile) {
        this.imageFile = imageFile;
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
        log.debug("Executing validate CompanyLogoForm........." + getDtoMap());

        ActionErrors errors = new ActionErrors();
        errors = super.validate(actionMapping, request);
        if (getDto("logoDelete") == null) {
            /** validate the image if it is setted */
            errors = validateImageFile(errors, getDtoMap(), imageFile, request);
        }
        return errors;
    }

    /**
     * Validate The Image file uploaded
     */
    public ActionErrors validateImageFile(ActionErrors errors, Map dtoMap, FormFile imageFile, HttpServletRequest request) {
        boolean hasErrors = false;
        if (imageFile != null && !GenericValidator.isBlankOrNull(imageFile.getFileName())) {
            if (!(imageFile.getContentType().equals("image/jpeg") || imageFile.getContentType().equals("image/gif")
                    || imageFile.getContentType().equals("image/pjpeg") || imageFile.getContentType().equals("image/png"))) {
                errors.add("file_typeNotAllowed", new ActionError("UIManager.error.logo_invalidType", "gif, jpeg, png"));
                hasErrors = true;
            } else if (imageFile.getFileSize() > 102400) { //100KB
                errors.add("maxLogoLengthExceeded", new ActionError("UIManager.error.logo_maxLengthExceeded", "100 KB"));
                hasErrors = true;
            }

            if (!hasErrors) {
                ArrayByteWrapper wrapper = new ArrayByteWrapper();
                try {
                    wrapper.setFileData(imageFile.getFileData());
                    dtoMap.put("image", wrapper);
                } catch (IOException e) {
                    log.error("unexpected error, cannot apply the arraybytewrapper to file", e);
                }
            }
        } else {
            errors.add("fileRequired", new ActionError("errors.required", JSPHelper.getMessage(request, "Common.file")));
        }
        return errors;
    }
}

