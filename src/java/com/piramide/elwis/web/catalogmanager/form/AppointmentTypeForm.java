package com.piramide.elwis.web.catalogmanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;

/**
 * Alfacentauro Team
 *
 * @author Alvaro
 * @version $Id: AppointmentTypeForm.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class AppointmentTypeForm extends DefaultForm {
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        errors = super.validate(mapping, request);
        String color = getDto("color").toString();
        if (color.startsWith("#")) {
            try {
                String hexValue = color.substring(1, color.length());
                BigInteger bi = new BigInteger(hexValue, 16);
            } catch (NumberFormatException ne) {
                errors.add("color", new ActionError("AppointmentType.error.hexNumberFormat"));
            }
        } else {
            errors.add("color", new ActionError("AppointmentType.error.hexNumberFormat"));
        }
        return (errors);
    }
}
