package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: IncomingInvoiceCreateAction.java 28-feb-2009 1:35:09
 */
public class IncomingInvoiceCreateAction extends DefaultAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("IncomingInvoiceCreateAction execute..........");
        DefaultForm defaultForm = (DefaultForm) form;
        if (isCancelled(request)) {
            return (mapping.findForward("Cancel"));
        }

        ActionForward actionForward = super.execute(mapping, defaultForm, request, response);
        if (defaultForm.getDto("incomingInvoiceId") != null) {
            return new ActionForwardParameters().add("incomingInvoiceId",
                    defaultForm.getDto("incomingInvoiceId").toString()).forward(actionForward);
        } else {
            return (actionForward);
        }
    }

}



