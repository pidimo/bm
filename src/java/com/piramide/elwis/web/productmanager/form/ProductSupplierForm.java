package com.piramide.elwis.web.productmanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;

/**
 * Product Form handler.
 *
 * @author Fernando Montaño
 * @version $Id: ProductSupplierForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ProductSupplierForm extends DefaultForm {
    // the logger
    private Log log = LogFactory.getLog(ProductSupplierForm.class);

    public ProductSupplierForm() {
        super();

    }

    /**
     * Validate the input fields and set defaults values to dtoMap.
     */
    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 javax.servlet.http.HttpServletRequest request) {
        log.debug("ProductSupplierForm validation execution...");

        ActionErrors errors = new ActionErrors();
        //validating if save it has been pressed
        errors = super.validate(mapping, request);
        if (super.getDto("isActive") != null) {
            super.setDto("active", new Boolean(true));
        } else {
            super.setDto("active", new Boolean(false));
        }
        setDto("supplierName", getDto("supplierName"));

        return errors;
    }
}
