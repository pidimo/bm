package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action to manage sale update in contact module, if arrive from "Invoice" button, redirect to invoice if update is success.
 *
 * @author Miky
 * @version $Id: SaleUpdateAction.java 2009-08-25 03:33:39 PM $
 */
public class SaleUpdateAction extends SaleManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing SaleUpdateAction........" + request.getParameterMap());
        DefaultForm defaultForm = (DefaultForm) form;
        String saleId = defaultForm.getDto("saleId").toString();

        ActionForward forward = super.execute(mapping, form, request, response);

        log.debug("After of cmd execute...................." + request.getAttribute("dto"));

        if ("Success".equals(forward.getName()) && request.getParameter("invoiceFromSale") != null) {
            ActionForwardParameters forwardParameters = new ActionForwardParameters();
            forwardParameters.add("saleId", saleId).add("dto(saleId)", saleId);

            forward = forwardParameters.forward(mapping.findForward("SaleInvoice"));
        }

        return forward;
    }
}
