package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.salesmanager.el.Functions;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 * Action to create invoices based in contracts,
 * set in session result ids of invoices documents generated
 *
 * @author Miky
 * @version $Id: ContractInvoiceCreateAction.java 11-dic-2008 18:53:27 $
 */
public class ContractInvoiceCreateAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ContractInvoiceCreateAction........" + request.getParameterMap());

        ActionForward forward = super.execute(mapping, form, request, response);
        log.debug("After of cmd execute...................." + request.getAttribute("dto"));

        if ("Success".equals(forward.getName())) {
            Map dtoValues = (Map) request.getAttribute("dto");
            List invoiceDocumentIds = (List) dtoValues.get("documentGenIds");

            if (!invoiceDocumentIds.isEmpty()) {
                //add in session the list of generated document ids
                request.getSession().setAttribute(Functions.composeMergeInvoiceDocumentsSessionKey(request), invoiceDocumentIds);

                //onload to merge documents
                String js = "onLoad=\"mergeInvoiceDocuments();\"";
                request.setAttribute("jsLoad", js);
            }
        }

        return forward;
    }
}
