package com.piramide.elwis.web.projects.action;

import com.piramide.elwis.utils.Constants;
import static com.piramide.elwis.utils.ProjectUserPermissionUtil.Permission;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.projects.el.Functions;
import com.piramide.elwis.web.projects.util.ProjectUserExistenceValidator;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProjectTimeListAction extends ProjectManagerListAction {

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        ActionForward forward = super.validateElementExistence(request, mapping);
        if (null != forward) {
            return forward;
        }

        ProjectUserExistenceValidator existenceValidator = new ProjectUserExistenceValidator();
        if (null != (forward = existenceValidator.isUserOfProject(request, mapping))) {
            saveErrors(request, existenceValidator.getErrors());
            return forward;
        }

        return null;
    }

    @Override
    protected void addFilers(HttpServletRequest request) {
        super.addFilers(request);

        setProjectTimeFilters(request);
    }

    protected void setProjectTimeFilters(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        addFilter("userId", user.getValue(Constants.USERID).toString());
        addFilter("assigneeId", user.getValue(Constants.USER_ADDRESSID).toString());

        //clear filters
        addFilter("new", "");

        //set filters according to project permissions
        String projectId = request.getParameter("projectId");
        if (Functions.isUserOfProject(projectId, request)) {
            addFilter("new", "true");
        }

        if (Functions.hasProjectUserPermission(projectId, Permission.NEW, request)) {
            addFilter("new", "true");
        }

        if (Functions.hasProjectUserPermission(projectId, Permission.VIEW, request) ||
                Functions.hasProjectUserPermission(projectId, Permission.CONFIRM, request) ||
                Functions.hasProjectUserPermission(projectId, Permission.ADMIN, request)) {
            addFilter("new", "");
        }
    }
}
