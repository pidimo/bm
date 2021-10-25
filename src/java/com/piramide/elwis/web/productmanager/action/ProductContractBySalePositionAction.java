package com.piramide.elwis.web.productmanager.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProductContractBySalePositionAction extends ProductManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward = super.execute(mapping, form, request, response);

        String encodedCustomerName = com.piramide.elwis.web.common.el.Functions.encode(request.getParameter("customerName"));
        return new ActionForwardParameters().
                add("salePositionId", request.getParameter("salePositionId")).
                add("dto(salePositionId)", request.getParameter("salePositionId")).
                add("dto(customerName)", encodedCustomerName).forward(forward);
    }

    @Override
    protected ActionForward validateElementExistence(DefaultForm defaultForm,
                                                     HttpServletRequest request,
                                                     ActionMapping mapping) {
        ActionForward forward = super.validateElementExistence(defaultForm, request, mapping);
        if (null != forward) {
            return forward;
        }

        ActionErrors errors = new ActionErrors();
        if (!Functions.existsSalePosition(request.getParameter("salePositionId"))) {
            String customerName = request.getParameter("customerName");
            errors.add("salePositionNotFound", new ActionError("msg.NotFound", customerName));
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
