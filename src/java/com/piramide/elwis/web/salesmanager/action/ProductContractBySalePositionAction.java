package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ProductContractBySalePositionAction extends SaleManagerAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward = super.execute(mapping, form, request, response);

        String encodedProductName = com.piramide.elwis.web.common.el.Functions.encode(request.getParameter("productName"));
        return new ActionForwardParameters().
                add("salePositionId", request.getParameter("salePositionId")).
                add("dto(salePositionId)", request.getParameter("salePositionId")).
                add("dto(productName)", encodedProductName).forward(forward);
    }

    @Override
    protected ActionForward validateElementExistence(DefaultForm defaultForm,
                                                     HttpServletRequest request,
                                                     ActionMapping mapping) {
        ActionErrors errors;

        ActionForward forward = super.validateElementExistence(defaultForm, request, mapping);
        if (null != forward) {
            return forward;
        }

        if (null != (errors = Functions.existsSalePosition(request))) {
            saveErrors(request.getSession(), errors);
            return mapping.findForward("SalePositionList");
        }

        String op = (String) defaultForm.getDto("op");
        if ("create".equals(op)) {
            if (null != (errors = Functions.validateSalePositionPayMethodValue(request.getParameter("salePositionId"), request))) {
                saveErrors(request.getSession(), errors);
                return mapping.findForward("Cancel");
            }
        }

        return null;
    }
}
