package com.piramide.elwis.web.bmapp.action.contacts;

import com.piramide.elwis.web.contactmanager.action.ContactManagerAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.5.7
 */
public class ContactManagerRESTAction extends ContactManagerAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ContactManagerRESTAction......." + request.getParameterMap());
        return (super.execute(mapping, form, request, response));
    }

}
