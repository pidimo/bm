package com.piramide.elwis.web.productmanager.action;

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
public class SalePositionCreateAction extends ProductManagerAction {
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

        String encodedCustomerName =
                com.piramide.elwis.web.common.el.Functions.encode((String) defaultForm.getDto("customerName"));

        ActionForwardParameters parameters = new ActionForwardParameters().
                add("salePositionId", salePositionId.toString()).
                add("dto(salePositionId)", salePositionId.toString()).
                add("customerName", encodedCustomerName);

        return parameters.forward(forward);
    }
}
