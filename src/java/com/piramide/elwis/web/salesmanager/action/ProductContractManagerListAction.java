package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.salesmanager.el.Functions;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ivan Alban
 * @version 4.3
 */
public class ProductContractManagerListAction extends ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if (null != (forward = validateInvoiceExistence(request, mapping))) {
            return forward;
        }

        addFilter("contractId", request.getParameter("contractId"));
        setModuleId(request.getParameter("contractId"));
        return super.execute(mapping, form, request, response);
    }

    protected ActionForward validateInvoiceExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {
        if (!Functions.existsProductContract(request.getParameter("contractId"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("ProductContractNotFound", new ActionError("ProductContract.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("MainProductContractList");
        }

        return null;
    }
}
