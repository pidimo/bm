package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceCreateForwardAction extends ForwardAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm defaultForm = (DefaultForm) form;
        Functions.setDefaultOptionsForInvoice(defaultForm, request);
        return super.execute(mapping, defaultForm, request, response);
    }
}
