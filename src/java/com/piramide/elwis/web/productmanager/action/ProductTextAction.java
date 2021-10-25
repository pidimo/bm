package com.piramide.elwis.web.productmanager.action;

import com.piramide.elwis.utils.ProductConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ProductTextAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String productId = request.getParameter("productId");

        ActionErrors errors = new ActionErrors();
        if (null != productId) {
            errors = ForeignkeyValidator.i.validate(ProductConstants.TABLE_PRODUCT, "productid",
                    productId, errors, new ActionError("Product.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("MainSearch");
            }
        } else {
            errors.add("productNotFound", new ActionError("Product.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("MainSearch");
        }
        ((DefaultForm) form).setDto("isProduct", "true");
        return super.execute(mapping, form, request, response);
    }
}
