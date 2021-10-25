package com.piramide.elwis.web.common.action;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Jatun S.R.L.
 * Write  saveErrors method to setting up error messages in session
 *
 * @author Ivan
 */
public class DefaultForwardAction extends ForwardAction {
    private Log log = LogFactory.getLog(DefaultForwardAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if (null != (forward = validateElementExistence(request, mapping))) {
            return forward;
        }

        setDTOValues((DefaultForm) form, request);

        return super.execute(mapping, form, request, response);
    }

    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {

    }

    protected ActionForward validateElementExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {
        log.debug("->Method 'validateElementExistence' always return null");
        return null;
    }

    protected void saveErrors(HttpSession session, ActionErrors errors) {
        if (errors == null || errors.isEmpty()) {
            session.removeAttribute(Globals.ERROR_KEY);
            return;
        } else {
            session.setAttribute(Globals.ERROR_KEY, errors);
            return;
        }
    }
}
