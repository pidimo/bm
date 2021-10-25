package com.piramide.elwis.web.financemanager.action;

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
public class InvoiceManagerAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if ((forward = checkCancel(mapping, request)) != null) {
            return forward;
        }

        if (null != (forward = validateInvoiceExistence(request, mapping))) {
            return forward;
        }

        forward = super.execute(mapping, form, request, response);
        addAlertMessages((DefaultForm) form, request);

        return forward;
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

    protected void addAlertMessages(DefaultForm defaultForm, HttpServletRequest request) {

    }
}
