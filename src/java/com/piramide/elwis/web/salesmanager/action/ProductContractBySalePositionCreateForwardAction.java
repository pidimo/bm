package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ProductContractBySalePositionCreateForwardAction extends SaleManagerForwardAction {
    private Log log = LogFactory.getLog(ProductContractBySalePositionCreateForwardAction.class);

    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        Functions.setProductContractDefaultValues(defaultForm, request.getParameter("salePositionId"), request);
    }

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {
        if (!Functions.existsSalePosition(request.getParameter("salePositionId"))) {
            String productName = request.getParameter("productName");
            ActionErrors errors = new ActionErrors();
            errors.add("salePositionNotFound", new ActionError("msg.NotFound", productName));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("SalePositionList");
        }

        return super.validateElementExistence(request, mapping);
    }
}
