package com.piramide.elwis.web.productmanager.form;

import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: ProductPictureForm.java 12515 2016-02-26 19:03:50Z miguel $
 */
public class ProductPictureForm extends DefaultForm {
    FormFile file;

    public ProductPictureForm() {
        super();
    }

    public FormFile getFile() {
        file = (FormFile) getDto("picture");
        return file;
    }

    public void setFile(FormFile file) {
        this.file = file;
        setDto("picture", this.file);
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        ActionErrors errors = new ActionErrors();
        errors = super.validate(mapping, request);


        log.debug("----------------------------------------------------------- ................" + getFile().getContentType());

        if (errors.isEmpty()) {
            if ("create".equals(getDto("op"))) {
                if (GenericValidator.isBlankOrNull(getFile().getFileName())) {
                    errors.add("file_required", new ActionError("errors.required", JSPHelper.getMessage(locale, "ProductPicture.file")));
                } else if (!(getFile().getContentType().equals("image/jpeg") || getFile().getContentType().equals("image/gif") || getFile().getContentType().equals("image/pjpeg") || getFile().getContentType().equals("image/png"))) {
                    errors.add("file_typeNotAllowed", new ActionError("File.error_TypeNotAllowed", file.getFileName()));
                }
            }
            if ("update".equals(getDto("op"))) {
                if (!GenericValidator.isBlankOrNull(getFile().getFileName())) {
                    if (!(getFile().getContentType().equals("image/jpeg") || getFile().getContentType().equals("image/gif") || getFile().getContentType().equals("image/pjpeg") || getFile().getContentType().equals("image/png"))) {
                        errors.add("file_typeNotAllowed", new ActionError("File.error_TypeNotAllowed", file.getFileName()));
                    } else {
                        setDto("change", new Boolean(true));
                    }
                } else {
                    setDto("change", new Boolean(false));
                }
            }
            String pattern = JSPHelper.getMessage(locale, "datePattern").trim();
            Date date = DateUtils.formatDate((String) getDto("uploadDate"), pattern);
            setDto("uploadDate", DateUtils.dateToInteger(date));
            Boolean maxLengthExceeded = (Boolean) request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
            setDto("fileSize", new Integer(file.getFileSize() / 1024));
            ArrayByteWrapper wrapper = new ArrayByteWrapper();
            try {
                wrapper.setFileData(file.getFileData());
                setDto("wrappedPicture", wrapper);
            } catch (IOException e) {
                log.error("cannot apply the arraybytewrapper on the file");
            }
            if (getFile().getFileSize() > 1047552) {
                errors.add("maxLengthExceeded", new ActionError("Common.Error_maxLengthExceeded"));
            }
        }
        return errors;
    }
}
