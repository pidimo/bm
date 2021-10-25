package com.piramide.elwis.web.productmanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * @author Yumi
 * @version $Id: ProductCustomerForm.java,v 1.8 2004/07/10 00:22:04
 */

public class ProductCustomerForm extends DefaultForm {
    private Log log = LogFactory.getLog(com.piramide.elwis.web.productmanager.form.ProductCustomerForm.class);

    /**
     * Validate customer product form (date)
     *
     * @param mapping
     * @param request
     * @return Errors
     */
    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 javax.servlet.http.HttpServletRequest request) {
        log.debug("Validate has been executed...");

        ActionErrors errors = new ActionErrors();
        if ((request.getParameter("dto(save)") != null || request.getParameter("dto(contract)") != null) &&
                !"delete".equals(getDto("op"))) {
            errors = super.validate(mapping, request);

            if (!GenericValidator.isBlankOrNull((String) getDto("inUse"))) {
                this.setDto("inUse", new Boolean(true));
            } else {
                this.setDto("inUse", new Boolean(false));
            }
        } else {
            errors.add("trucho", new ActionError("NoHasError")); // put any error to return to input page
            request.setAttribute("skipErrors", "true"); //skip to show the errors in the page
        }
        return errors;
    }
}
