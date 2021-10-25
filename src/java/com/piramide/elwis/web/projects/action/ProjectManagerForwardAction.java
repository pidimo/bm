package com.piramide.elwis.web.projects.action;

import com.piramide.elwis.web.common.action.DefaultForwardAction;
import com.piramide.elwis.web.projects.el.Functions;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ProjectManagerForwardAction extends DefaultForwardAction {
    @Override
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
}
