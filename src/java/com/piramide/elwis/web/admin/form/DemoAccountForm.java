package com.piramide.elwis.web.admin.form;

import com.piramide.elwis.web.common.validator.EmailValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.5
 */
public class DemoAccountForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);

        //validate email
        String email = (String) getDto("email");
        if (!EmailValidator.i.isValid(email)) {
            errors.add("errorEmail", new ActionError("errors.email", email));
        }
        return errors;
    }
}
