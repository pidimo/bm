package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.action.DefaultForwardAction;
import com.piramide.elwis.web.contactmanager.el.Functions;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public class ContactManagerForwardAction extends DefaultForwardAction {

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {
        if (!Functions.existsAddress(request.getParameter("contactId"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("general", new ActionError("Address.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        return null;
    }
}
