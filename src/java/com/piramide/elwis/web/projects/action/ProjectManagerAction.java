package com.piramide.elwis.web.projects.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.projects.el.Functions;
import com.piramide.elwis.web.projects.util.AdminValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProjectManagerAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if ((forward = checkCancel(mapping, request)) != null) {
            return forward;
        }

        if (null != (forward = validateElementExistence(request, mapping))) {
            return forward;
        }

        if (null != (forward = priorValidations(form, request, mapping))) {
            return forward;
        }

        return super.execute(mapping, form, request, response);
    }

    protected ActionForward validateElementExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {
        if (!Functions.existsProject(request.getParameter("projectId"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("projectNotFound", new ActionError("Project.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("MainSearch");
        }
        return null;
    }

    protected ActionForward priorValidations(ActionForm form,
                                             HttpServletRequest request,
                                             ActionMapping mapping) {
        ActionForward forward;

        AdminValidator adminValidator = new AdminValidator();
        if (null != (forward = adminValidator.hasPermission(request, mapping))) {
            saveErrors(request, adminValidator.getErrors());
            return forward;
        }

        return null;
    }
}
