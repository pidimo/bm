package com.piramide.elwis.web.productmanager.action;

import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProductContractBySalePositionCreateForwardAction extends ProductManagerForwardAction {

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {
        if (!Functions.existsSalePosition(request.getParameter("salePositionId"))) {
            String customerName = request.getParameter("customerName");
            ActionErrors errors = new ActionErrors();
            errors.add("salePositionNotFound", new ActionError("msg.NotFound", customerName));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("SalePositionList");
        }

        return super.validateElementExistence(request, mapping);
    }

    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        Functions.setProductContractDefaultValues(defaultForm, request.getParameter("salePositionId"), request);
    }
}
