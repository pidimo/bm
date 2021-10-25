package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: IncomingPaymentDefaultAction.java 27-feb-2009 16:58:34
 */
public class IncomingInvoiceDefaultAction extends DefaultAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("IncomingInvoiceDefaultAction execute..........");
        IncomingInvoiceActionUtil.readBasicInformation(request, log);
        ActionForward actionForward = mapping.findForward("Fail");
        if (request.getAttribute("invoiceNumber") != null) { //IncomingInvoice was read
            actionForward = super.execute(mapping, form, request, response);
        } else {
            ActionErrors errors = new ActionErrors();
            errors.add("general", new ActionError("Finance.IncomingInvoice.NotFound"));
            saveErrors(request, errors);
        }
        return (actionForward);
    }
}
