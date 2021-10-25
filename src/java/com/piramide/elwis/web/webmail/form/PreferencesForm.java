package com.piramide.elwis.web.webmail.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ivan Alban
 * @version 4.2.1
 */
public class PreferencesForm extends DefaultForm {
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);

        return errors;
    }

}
