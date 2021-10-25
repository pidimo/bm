package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.contactmanager.el.Functions;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class ContactDeduplicationForwardAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if (null != (forward = validateDuplicateGroupExistence(request, mapping))) {
            return forward;
        }
        return mapping.findForward("Success");
    }

    private ActionForward validateDuplicateGroupExistence(HttpServletRequest request, ActionMapping mapping) {

        String duplicateGroupId = request.getParameter("duplicateGroupId");
        if (duplicateGroupId == null || !Functions.existsDuplicateGroup(duplicateGroupId)) {
            ActionErrors errors = new ActionErrors();
            errors.add("DuplicateGroup", new ActionError("DedupliContact.duplicate.notFound"));
            saveErrors(request.getSession(), errors);
            //return main list
            return mapping.findForward("Fail");
        }
        return null;
    }
}
