package com.piramide.elwis.web.contactmanager.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Titus
 * @version CustomerManagerAction.java, v 2.0 Jul 11, 2004 5:33:27 PM
 */
public class CustomerManagerAction extends ContactManagerAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping action,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("executing action manager customer");
        return super.execute(action, form, request, response);
    }
}
