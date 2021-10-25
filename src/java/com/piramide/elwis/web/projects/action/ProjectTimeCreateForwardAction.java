package com.piramide.elwis.web.projects.action;

import com.piramide.elwis.dto.projects.ProjectDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ProjectConstants;
import static com.piramide.elwis.utils.ProjectConstants.ToBeInvoicedType;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.projects.util.ProjectUserExistenceValidator;
import com.piramide.elwis.web.projects.util.ProjectUserPermissionValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ProjectTimeCreateForwardAction extends ProjectManagerForwardAction {
    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        ActionForward forward = super.validateElementExistence(request, mapping);
        if (null != forward) {
            return forward;
        }

        String projectId = request.getParameter("projectId");

        ProjectUserExistenceValidator existenceValidator = new ProjectUserExistenceValidator();
        if (null != (forward = existenceValidator.isUserOfProject(request, mapping))) {
            saveErrors(request, existenceValidator.getErrors());
            return forward;
        }

        ProjectUserPermissionValidator permissionValidator = new ProjectUserPermissionValidator(projectId, request);
        if (null != (forward = permissionValidator.validateNewButtonPermissions(request, mapping))) {
            saveErrors(request, permissionValidator.getErrors());
            return forward;
        }

        return null;
    }

    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        String addressName = com.piramide.elwis.web.contactmanager.el.Functions.getAddressName(
                user.getValue(Constants.USER_ADDRESSID));
        defaultForm.setDto("userName", addressName);
        defaultForm.setDto("userId", user.getValue(Constants.USERID));
        defaultForm.setDto("status", ProjectConstants.ProjectTimeStatus.ENTERED.getValue());
        defaultForm.setDto("assigneeId", user.getValue(Constants.USER_ADDRESSID));
        defaultForm.setDto("assigneeName", addressName);

        String projectId = request.getParameter("projectId");
        ProjectDTO projectDTO = com.piramide.elwis.web.projects.el.Functions.getProject(request, projectId);
        Integer toBeInvoiced = (Integer) projectDTO.get("toBeInvoiced");
        if (ToBeInvoicedType.ALL_TIMES.getValue() == toBeInvoiced) {
            defaultForm.setDto("toBeInvoiced", true);
            defaultForm.setDto("disableInvoiceable", true);
        }
        if (ToBeInvoicedType.NO_TIMES.getValue() == toBeInvoiced) {
            defaultForm.setDto("toBeInvoiced", false);
            defaultForm.setDto("disableInvoiceable", true);
        }
        if (ToBeInvoicedType.DEPENDS.getValue() == toBeInvoiced) {
            defaultForm.setDto("toBeInvoiced", true);
            defaultForm.setDto("disableInvoiceable", false);
        }
    }
}
