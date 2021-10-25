package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.salesmanager.el.Functions;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceManagerListAction extends ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if (null != (forward = validateInvoiceExistence(request, mapping))) {
            return forward;
        }

        addFilter("invoiceId", request.getParameter("invoiceId"));
        setModuleId(request.getParameter("invoiceId"));
        return super.execute(mapping, form, request, response);
    }

    protected ActionForward validateInvoiceExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {
        if (!Functions.existsInvoice(request.getParameter("invoiceId"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("InvoiceNotFound", new ActionError("Invoice.NotFound"));
            saveErrors(request.getSession(), errors);

            //return main list, invoiceSingleList is general forward
            return mapping.findForward("MainInvoiceList");
        }

        return null;
    }
}
