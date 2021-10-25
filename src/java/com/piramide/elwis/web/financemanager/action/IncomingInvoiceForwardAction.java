package com.piramide.elwis.web.financemanager.action;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: IncomingInvoiceForwardAction.java 27-feb-2009 17:04:24
 */
public class IncomingInvoiceForwardAction extends ForwardAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("IncomingInvoiceForwardAction execute..........");

        DefaultForm defaultForm = (DefaultForm) form;

        IncomingInvoiceActionUtil.readBasicInformation(request, log);
        ActionForward actionForward = mapping.findForward("Fail");
        if (request.getAttribute("invoiceNumber") != null) { //IncomingInvoice exists...
            //set default amount
            defaultForm.setDto("amount", request.getAttribute("incomingInvoiceOpenAmount"));
            actionForward = super.execute(mapping, defaultForm, request, response);

        } else {
            ActionErrors errors = new ActionErrors();
            errors.add("general", new ActionError("Finance.IncomingInvoice.NotFound"));
            saveErrors(request, errors);
        }
        return (actionForward);
    }
}