package com.piramide.elwis.web.projects.action;

import com.piramide.elwis.web.projects.util.ProjectUserExistenceValidator;
import com.piramide.elwis.web.projects.util.ProjectUserPermissionValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProjectTimeAction extends ProjectManagerAction {

    @Override
    protected ActionForward priorValidations(ActionForm form,
                                             HttpServletRequest request,
                                             ActionMapping mapping) {
        String projectId = request.getParameter("projectId");

        ActionForward forward;
        ProjectUserExistenceValidator existenceValidator = new ProjectUserExistenceValidator();
        if (null != (forward = existenceValidator.isUserOfProject(request, mapping))) {
            saveErrors(request, existenceValidator.getErrors());
            return forward;
        }

        ProjectUserPermissionValidator permissionValidator = new ProjectUserPermissionValidator(projectId, request);
        if (null !=
                (forward = permissionValidator.validateProjectUserPermissions(request, (DefaultForm) form, mapping))) {
            saveErrors(request, permissionValidator.getErrors());
            return forward;
        }

        return null;
    }
}
