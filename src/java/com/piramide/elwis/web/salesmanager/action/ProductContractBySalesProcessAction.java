package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class ProductContractBySalesProcessAction extends SaleBySalesProcessAction {
    @Override
    protected ActionForward validateElementExistence(DefaultForm defaultForm,
                                                     HttpServletRequest request,
                                                     ActionMapping mapping) {

        ActionForward forward = super.validateElementExistence(defaultForm, request, mapping);

        //Salesprocess or Sale was deleted
        if (null != forward) {
            return forward;
        }

        ActionErrors errors;
        if (null != (errors = Functions.existsSalePosition(request))) {
            saveErrors(request, errors);
            return mapping.findForward("Fail");
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
