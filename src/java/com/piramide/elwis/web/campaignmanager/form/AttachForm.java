package com.piramide.elwis.web.campaignmanager.form;

import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
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
 * @author: ivan
 * Date: 25-10-2006: 01:03:58 PM
 */
public class AttachForm extends DefaultForm {
    FormFile file;

    public AttachForm() {
    }

    public FormFile getFile() {
        return file;
    }

    public void setFile(FormFile file) {
        this.file = file;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        Boolean maxLengthExceeded = (Boolean)
                request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);

        if (null != maxLengthExceeded && maxLengthExceeded.booleanValue()) {
            errors.add("maxLengthExceeded", new ActionError("Common.Error_maxLengthExceeded"));
            return errors;
        }

        User sessionUser = (User) request.getSession().getAttribute("user");
        int maxSize = Integer.parseInt(sessionUser.getValue("maxAttachSize").toString());

        byte[] data = null;
        if ("create".equals(getDto("op").toString())) {
            //check if has selected file
            if (GenericValidator.isBlankOrNull(file.getFileName())) {
                errors.add("file_required", new ActionError("errors.required",
                        JSPHelper.getMessage(request, "Common.file")));
                return errors;
            }
            //check file properties
            //eg, exists file, size of file
            if ((data = checkFileProperties(maxSize, errors)) == null) {
                return errors;
            }

        }
        if ("update".equals(getDto("op").toString())) {
            if (!GenericValidator.isBlankOrNull(file.getFileName())) {
                if ((data = checkFileProperties(maxSize, errors)) == null) {
                    return errors;
                }
            }
        }

        if (errors.isEmpty()) {
            ArrayByteWrapper wrapper = null;
            if (null != data) {
                wrapper = new ArrayByteWrapper(data);
                setDto("filename", file.getFileName());
                setDto("size", file.getFileSize());
            } else {
                getDtoMap().remove("filename");
                getDtoMap().remove("size");
            }
            setDto("file", wrapper);
        }
        return errors;
    }

    private byte[] checkFileProperties(int maxSize, ActionErrors errors) {
        byte[] data = null;

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

        if (null != data) {
            double actualSize = file.getFileSize() / Math.pow(1024, 2);
            if (actualSize > maxSize) {
                errors.add("attach", new ActionError("Common.error.fileExceedSize", new Integer(maxSize)));
                return null;
            }
        }
        return data;
    }

    public void reset(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {

    }
}
