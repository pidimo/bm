package com.piramide.elwis.web.reports.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ivan
 * @version : $Id ReportForm ${time}
 */
public class ReportForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

        ActionErrors errors = super.validate(mapping, request);

        /*Object saveButton = this.getDto("save");
        if (null != saveButton)
            return super.validate(mapping, request);
        errors.add("city", new ActionError("Address.error.cityNotFound")); // put any error to return to input page
        request.setAttribute("skipErrors", "true"); //skip to show the errors in the page*/

        return errors;
    }
}
