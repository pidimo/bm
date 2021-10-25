package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.web.common.action.DefaultForwardAction;
import com.piramide.elwis.web.salesmanager.el.Functions;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SaleManagerForwardAction extends DefaultForwardAction {

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {
        if (!Functions.existsSale(request.getParameter("saleId"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("SaleNotFound", new ActionError("Sale.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("MainSaleList");
        }

        return null;
    }
}
