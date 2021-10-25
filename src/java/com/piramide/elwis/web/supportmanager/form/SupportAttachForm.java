package com.piramide.elwis.web.supportmanager.form;

import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author : ivan
 * @version : $Id SupportAttachForm ${time}
 */

public class SupportAttachForm extends DefaultForm {
    FormFile file;

    public FormFile getFile() {
        return file;
    }

    public void setFile(FormFile file) {
        this.file = file;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Executing validate method with \n" +
                "mapping = ????\n" +
                "request = ????\n" +
                "operation = " + getDto("op"));
        ActionErrors errors = new ActionErrors();
        ArrayByteWrapper wrapper = new ArrayByteWrapper();
        User sessionUser = (User) request.getSession().getAttribute(Constants.USER_KEY);
        int maxAttachSize = Integer.parseInt(sessionUser.getValue("maxAttachSize").toString());
        String dateTimeZone = sessionUser.getValue("dateTimeZone").toString();
        Integer elwisUserId = Integer.valueOf(sessionUser.getValue("userId").toString());
        setDto("userId", elwisUserId);
        DateTimeZone zone = DateTimeZone.forID(dateTimeZone);
        DateTime dateTime = new DateTime(zone);

        if ("create".equals(getDto("op"))) {
            if (GenericValidator.isBlankOrNull(((FormFile) getDto("file")).getFileName())) {
                errors.add("fileRequired", new ActionError("errors.required", JSPHelper.getMessage(request, "SupportAttach.fileName")));
            }
        }
        if ("update".equals(getDto("op"))) {
            String fileName = getDto("fileName").toString();
            request.setAttribute("fileName", fileName);
        }

        if (GenericValidator.isBlankOrNull((String) getDto("comment"))) {
            errors.add("commentRequired", new ActionError("errors.required", JSPHelper.getMessage(request, "SupportAttach.comment")));
        }

        if (!errors.isEmpty()) {
            return errors;
        }
        if (null != getDto("file") && !"".equals(getDto("file").toString().trim())) {
            if (((FormFile) getDto("file")).getFileSize() > maxAttachSize * 1000000) {
                errors.add("maxLengthExceeded", new ActionError("Common.Error_FilemaxLengthExceeded",
                        ((FormFile) getDto("file")).getFileName()));
                return errors;
            }

            try {
                wrapper.setFileData(((FormFile) getDto("file")).getFileData());
                if (errors.isEmpty()) {
                    setDto("uploadFile", wrapper);
                    setDto("supportAttachName", ((FormFile) getDto("file")).getFileName());
                    setDto("size", new Integer(((FormFile) getDto("file")).getFileSize()));
                    setDto("uploadDateTime", new Long(dateTime.getMillis()));
                }
            } catch (IOException e) {
                log.error("Error on  pack to file in ArrayByteWrapper...");
            }
        }
        return super.validate(mapping, request);
    }
}
