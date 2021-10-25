package com.piramide.elwis.web.projects.action;

import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.projects.el.Functions;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProjectManagerListAction extends ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;

        if (null != (forward = validateElementExistence(request, mapping))) {
            return forward;
        }

        addFilers(request);
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

    protected void addFilers(HttpServletRequest request) {
        addFilter("projectId", request.getParameter("projectId"));
        setModuleId(request.getParameter("projectId"));
    }
}
