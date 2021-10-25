package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class SalePositionBySalesProcessCreateAction extends SaleBySalesProcessAction {
    @Override
    protected ActionForward processForward(ActionForward forward,
                                           DefaultForm defaultForm,
                                           ActionMapping mapping,
                                           HttpServletRequest request) {
        if (!"Success".equals(forward.getName())) {
            return super.processForward(forward, defaultForm, mapping, request);
        }

        Integer salePositionId = (Integer) defaultForm.getDto("salePositionId");

        Integer payMethod = (Integer) defaultForm.getDto("payMethod");
        if (null != payMethod && SalesConstants.PayMethod.SingleWithoutContract.equal(payMethod)) {
            return mapping.findForward("ToList");
        }

        String encodedProductName =
                com.piramide.elwis.web.common.el.Functions.encode((String) defaultForm.getDto("productName"));
        String encodedCustomerName =
                com.piramide.elwis.web.common.el.Functions.encode((String) defaultForm.getDto("customerName"));

        ActionForwardParameters parameters = new ActionForwardParameters().
                add("salePositionId", salePositionId.toString()).
                add("dto(salePositionId)", salePositionId.toString()).
                add("customerName", encodedCustomerName).
                add("productName", encodedProductName);

        return parameters.forward(forward);
    }
}
