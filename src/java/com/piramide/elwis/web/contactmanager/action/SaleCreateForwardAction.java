package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class SaleCreateForwardAction extends com.piramide.elwis.web.salesmanager.action.SaleCreateForwardAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm defaultForm = (DefaultForm) form;

        if (request.getParameter("contactId") != null) {
            //set default customer values for sale
            Functions.setSaleCustomerDefaultValues(defaultForm, Integer.valueOf(request.getParameter("contactId")), request);
        }

        return super.execute(mapping, defaultForm, request, response);
    }
}
