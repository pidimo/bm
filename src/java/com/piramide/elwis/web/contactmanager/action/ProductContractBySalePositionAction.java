package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProductContractBySalePositionAction extends ContactManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if (null == (forward = validateElementExistence((DefaultForm) form, request, mapping))) {
            User user = RequestUtils.getUser(request);
            if (form instanceof DefaultForm) {
                if (!(((DefaultForm) form).getDto("companyId") != null)) {
                    ((DefaultForm) form).setDto("companyId", user.getValue("companyId"));
                }
            }
            forward = super.execute(mapping, form, request, response);
        }


        String encodedProductName = com.piramide.elwis.web.common.el.Functions.encode(request.getParameter("productName"));
        return new ActionForwardParameters().
                add("salePositionId", request.getParameter("salePositionId")).
                add("dto(salePositionId)", request.getParameter("salePositionId")).
                add("dto(productName)", encodedProductName).forward(forward);
    }


    protected ActionForward validateElementExistence(DefaultForm defaultForm,
                                                     HttpServletRequest request,
                                                     ActionMapping mapping) {
        ActionErrors errors;
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
