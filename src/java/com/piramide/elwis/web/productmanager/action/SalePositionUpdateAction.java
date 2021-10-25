package com.piramide.elwis.web.productmanager.action;

import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class SalePositionUpdateAction extends ProductManagerAction {
    @Override
    protected ActionForward processForward(ActionForward forward,
                                           DefaultForm defaultForm,
                                           ActionMapping mapping,
                                           HttpServletRequest request) {
        if ("Success".equals(forward.getName())) {
            Integer payMethod = (Integer) defaultForm.getDto("payMethod");

            if (null != payMethod && SalesConstants.PayMethod.SingleWithoutContract.equal(payMethod)) {
                return mapping.findForward("ToList");
            }
        }

        return super.processForward(forward, defaultForm, mapping, request);
    }
}
