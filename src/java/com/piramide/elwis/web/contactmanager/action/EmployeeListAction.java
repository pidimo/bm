package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.contactmanager.form.EmployeeForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author Titus
 * @version EmployeeListAction.java, v 2.0 Jul 6, 2004 8:09:58 PM
 */
public class EmployeeListAction extends ContactListAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping action,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if (((EmployeeForm) request.getSession().getAttribute("employeeFormTemp")) != null) {
            log.debug("enter remove employee sesion");
            request.getSession().removeAttribute("employeeFormTemp");
        }
        return super.execute(action, form, request, response);
    }
}
