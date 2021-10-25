package com.piramide.elwis.web.contactmanager.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action to manage events to create or update employees
 *
 * @author Titus
 * @version EmployeeManagerAction.java, v 2.0 May 20, 2004 9:49:43 AM
 */
public class EmployeeManagerAction extends ContactManagerAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping action,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("executing action manager employee");

        return super.execute(action, form, request, response);
    }
}
