package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.web.common.action.ListAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: IncomingPaymentListAction.java 24-feb-2009 22:48:17
 */
public class IncomingPaymentListAction extends ListAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("IncomingPaymentListAction executing...............");
        IncomingInvoiceActionUtil.readBasicInformation(request, log);
        ActionForward actionForward = mapping.findForward("Fail");
        if (request.getAttribute("invoiceNumber") != null) {
            this.addFilter("incomingInvoiceId", request.getAttribute("incomingInvoiceId").toString());
            actionForward = super.execute(mapping, form, request, response);
        } else {

            ActionErrors errors = new ActionErrors();
            errors.add("general", new ActionError("Finance.IncomingInvoice.NotFound"));
            saveErrors(request, errors);
        }

        return (actionForward);
    }

}
