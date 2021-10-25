package com.piramide.elwis.web.productmanager.form;

import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

/**
 * Product Form handler.
 *
 * @author Fernando Montaño
 * @version $Id: PricingForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class PricingForm extends DefaultForm {
    // the logger
    private Log log = LogFactory.getLog(PricingForm.class);

    public PricingForm() {
        super();

    }

    /**
     * Validate the input fields and set defaults values to dtoMap.
     */
    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 javax.servlet.http.HttpServletRequest request) {
        log.debug("ProductForm validation execution...");

        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        ActionErrors errors = new ActionErrors();
        //validating if save it has been pressed

        errors = super.validate(mapping, request);// validating with super class

        String quantity = (String) getDto("quantity");
        try {
            if (new Long(quantity).longValue() == 1 && "create".equals((String) getDto("op"))) {
                errors.add("product_price", new ActionError("errors.different", JSPHelper.getMessage(locale, "Product.quantity"), "1"));
            }
        } catch (java.lang.NumberFormatException ne) {
        }

        return errors;
    }

}
