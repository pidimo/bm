package com.piramide.elwis.web.productmanager.action;

import com.piramide.elwis.utils.ProductConstants;
import com.piramide.elwis.web.common.action.DefaultForwardAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 * Mainly this class check existence of working productId, if product has been deleted by other user
 * redirect with error message.
 *
 * @author Miky
 * @version $Id: ProductManagerForwardAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ProductManagerForwardAction extends DefaultForwardAction {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {
        ActionErrors errors = new ActionErrors();

        log.debug("productId for user request  = " + request.getParameter("productId"));
        if (null != request.getParameter("productId")) {
            errors = ForeignkeyValidator.i.validate(ProductConstants.TABLE_PRODUCT, "productid",
                    request.getParameter("productId"), errors, new ActionError("Product.NotFound"));
            if (!errors.isEmpty()) {
                log.debug("The active product was deleted by other user...");
                saveErrors(request, errors);
                return mapping.findForward("MainSearch");
            }
        } else {
            errors.add("productNotFound", new ActionError("Product.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        return null;
    }
}

