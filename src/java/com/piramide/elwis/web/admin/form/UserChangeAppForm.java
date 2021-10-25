package com.piramide.elwis.web.admin.form;

import com.piramide.elwis.web.contactmanager.form.AddressContactPersonHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Form to manage changes to update from mobile app, like user image
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.3
 */
public class UserChangeAppForm extends DefaultForm {

    private Log log = LogFactory.getLog(this.getClass());

    private FormFile imageFile;

    public FormFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(FormFile imageFile) {
        this.imageFile = imageFile;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("UserChangeAppForm validation execution..." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        /** validate the image if it is setted from mobile app */
        errors = AddressContactPersonHelper.validateImageFile(errors, getDtoMap(), imageFile);

        return errors;
    }
}
