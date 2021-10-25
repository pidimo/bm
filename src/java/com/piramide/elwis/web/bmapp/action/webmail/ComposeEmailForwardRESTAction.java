package com.piramide.elwis.web.bmapp.action.webmail;

import com.piramide.elwis.web.webmail.action.ComposeForwardAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1.6
 */
public class ComposeEmailForwardRESTAction extends ComposeForwardAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  ComposeEmailForwardRESTAction..." + request.getParameterMap());

        ActionForward forward = super.execute(mapping, form, request, response);

        if ("NoUserMail".equals(forward.getName())) {
            userMailNotFoundError(request);
        }

        return forward;
    }

    private void userMailNotFoundError(HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        errors.add("userMailNotFound", new ActionError("webmail.error.account"));
        saveErrors(request, errors);
    }
}
