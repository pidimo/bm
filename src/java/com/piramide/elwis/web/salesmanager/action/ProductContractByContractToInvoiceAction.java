package com.piramide.elwis.web.salesmanager.action;

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
public class ProductContractByContractToInvoiceAction extends ProductContractManagerAction {

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        if (!Functions.existsProductContract(request.getParameter("contractId"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("ProductContractNotFound", new ActionError("ProductContract.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("Fail");
        }

        return null;
    }
}
