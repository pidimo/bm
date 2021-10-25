package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.web.financemanager.util.InvoiceGenerateDocumentUtil;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 * Action to update invoice, and generate invoice documentr if this is necessary
 *
 * @author Miky
 * @version $Id: InvoiceSalesUpdateAction.java 17-sep-2008 19:52:32 $
 */
public class InvoiceSalesUpdateAction extends InvoiceManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing InvoiceSalesUpdateAction........" + request.getParameterMap());
        DefaultForm defaultForm = (DefaultForm) form;
        Integer invoiceId = new Integer(defaultForm.getDto("invoiceId").toString());

        ActionForward forward = super.execute(mapping, form, request, response);
        log.debug("After of cmd execute...................." + request.getAttribute("dto"));
        if ("Success".equals(forward.getName()) && request.getParameter("generate") != null) {

            InvoiceGenerateDocumentUtil generateUtil = new InvoiceGenerateDocumentUtil();
            ActionErrors errors = generateUtil.generateInvoiceDocument(invoiceId, mapping, request);
            if (errors.isEmpty()) {
                //onload to download document
                String js = "onLoad=\"downloadInvoice(\'" + generateUtil.getInvoiceDocumentId() + "\');\"";
                request.setAttribute("jsLoad", js);
            } else {
                saveErrors(request.getSession(), errors);
                forward = mapping.findForward("FailGeneration");
            }
        }

        return forward;
    }
}
