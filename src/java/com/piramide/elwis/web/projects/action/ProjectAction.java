package com.piramide.elwis.web.projects.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Project main action controller.
 *
 * @author Fernando Montao
 * @version $Id: ProjectAction.java 2009-02-21 15:02:45 $
 */
public class ProjectAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm projectForm = (DefaultForm) form;
        String op = (String) projectForm.getDto("op");
        if (isCancelled(request)) {
            return (mapping.findForward("Cancel"));
        }

        if (request.getParameter("saveButton") != null) {
            ActionForward forward = super.execute(mapping, projectForm, request, response);
            if (projectForm.getDto("projectId") != null && "create".equals(op)) {
                return new ActionForwardParameters().add("projectId",
                        projectForm.getDto("projectId").toString()).forward(forward);
            }
            return forward;
        } else {
            return mapping.getInputForward();
        }
    }

}
