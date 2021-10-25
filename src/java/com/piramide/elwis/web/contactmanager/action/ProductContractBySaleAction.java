package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ProductContractBySaleAction extends SaleManagerAction {
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionForward forward;

        if ((forward = checkCancel(mapping, request)) != null) {
            return setSalePositionParamenters(request).forward(forward);
        }

        if (null == (forward = validateElementExistence((DefaultForm) form, request, mapping))) {
            User user = RequestUtils.getUser(request);
            if (form instanceof DefaultForm) {
                if (!(((DefaultForm) form).getDto("companyId") != null)) {
                    ((DefaultForm) form).setDto("companyId", user.getValue("companyId"));
                }
            }
            forward = super.execute(mapping, form, request, response);
        }

        return setSalePositionParamenters(request).forward(forward);
    }

    private ActionForwardParameters setSalePositionParamenters(HttpServletRequest request) {
        String productName = request.getParameter("productName");
        String encodedProductName = "";
        if (null != productName) {
            encodedProductName =
                    com.piramide.elwis.web.common.el.Functions.encode(request.getParameter("productName"));
        }

        return new ActionForwardParameters().
                add("salePositionId", request.getParameter("salePositionId")).
                add("dto(salePositionId)", request.getParameter("salePositionId")).
                add("dto(productName)", encodedProductName);
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
            String productName = request.getParameter("productName");
            errors.add("salePositionNotFound", new ActionError("msg.NotFound", productName));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("NotFound");
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
