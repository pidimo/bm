package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.contactmanager.form.EmployeeForm;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Redirect to new Employee or go to search List for Create Employee
 *
 * @author Titus
 * @version EmployeeAddAction.java, v 2.0 May 18, 2004 9:03:00 PM
 */
public class EmployeeAddAction extends ContactListAction {

    private Log log = LogFactory.getLog(EmployeeAddAction.class);

    /**
     * Action for create employee or import from serarch employee layuot
     */
    public ActionForward execute(ActionMapping action,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing action employee ");

        SearchForm searchForm = (SearchForm) form;

        if (((EmployeeForm) request.getSession().getAttribute("employeeFormTemp")) != null) {
            log.debug("enter remove employee sesion");
            request.getSession().removeAttribute("employeeFormTemp");
        }
        if (isCancelled(request)) {
            return action.findForward("Cancel");
        } else {
            return super.execute(action, searchForm, request, response);
        }
    }
}
