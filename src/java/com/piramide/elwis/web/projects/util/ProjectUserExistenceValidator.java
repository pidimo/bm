package com.piramide.elwis.web.projects.util;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.projects.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProjectUserExistenceValidator implements WorkFlowValidator {
    private ActionErrors errors = new ActionErrors();

    public List<ActionError> validate(WorkFlowAction workFlowAction, DefaultForm form, HttpServletRequest request) {
        return null;
    }

    private ActionError getError(HttpServletRequest request) {
        return new ActionError("ProjectTime.notProjectUser.error");
    }

    private ActionForward getForward(HttpServletRequest request,
                                     ActionMapping mapping,
                                     String forwardName) {
        return new ActionForwardParameters().
                add("dto(projectId)", request.getParameter("projectId")).
                add("index", "0").
                add("dto(op)", "read").
                forward(mapping.findForward(forwardName));
    }

    public ActionForward isUserOfProject(HttpServletRequest request, ActionMapping mapping) {
        if (!Functions.isUserOfProject(request.getParameter("projectId"), request)) {
            errors.add("AdminPermissionError", getError(request));
            return getForward(request, mapping, "Detail");
        }

        return null;
    }

    public ActionErrors getErrors() {
        return errors;
    }
}
