package com.piramide.elwis.web.salesmanager.action;


import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SaleManagerAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if ((forward = checkCancel(mapping, request)) != null) {
            return forward;
        }

        if (null != (forward = validateElementExistence((DefaultForm) form, request, mapping))) {
            return forward;
        }

        forward = super.execute(mapping, form, request, response);
        return processForward(forward, (DefaultForm) form, mapping, request);
    }

    protected ActionForward validateElementExistence(DefaultForm defaultForm,
                                                     HttpServletRequest request,
                                                     ActionMapping mapping) {
        if (!Functions.existsSale(request.getParameter("saleId"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("SaleNotFound", new ActionError("Sale.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("MainSaleList");
        }

        return null;
    }

    protected ActionForward processForward(ActionForward forward,
                                           DefaultForm defaultForm,
                                           ActionMapping mapping,
                                           HttpServletRequest request) {
        return forward;
    }
}
