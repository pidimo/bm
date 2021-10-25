package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SalePositionBySaleCreateAction extends SaleManagerAction {

    @Override
    protected ActionForward processForward(ActionForward forward,
                                           DefaultForm defaultForm,
                                           ActionMapping mapping,
                                           HttpServletRequest request) {
        if ("Cancel".equals(forward.getName()) || "SaleList".equals(forward.getName())) {
            return forward;
        }

        Integer payMethod = (Integer) defaultForm.getDto("payMethod");

        if (null != payMethod && SalesConstants.PayMethod.SingleWithoutContract.equal(payMethod)) {
            return mapping.findForward("ToList");
        }

        ActionForwardParameters parameters = new ActionForwardParameters();
        String productName =
                Functions.encode(defaultForm.getDto("productName").toString());

        parameters.add("saleId", defaultForm.getDto("saleId").toString()).
                add("dto(op)", "read").
                add("salePositionId", defaultForm.getDto("salePositionId").toString()).
                add("dto(salePositionId)", defaultForm.getDto("salePositionId").toString()).
                add("productName", productName).
                add("dto(productName)", productName);

        return parameters.forward(forward);
    }
}
