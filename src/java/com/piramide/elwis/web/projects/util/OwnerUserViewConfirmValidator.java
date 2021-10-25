package com.piramide.elwis.web.projects.util;

import static com.piramide.elwis.utils.ProjectUserPermissionUtil.Permission;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.projects.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class OwnerUserViewConfirmValidator implements WorkFlowValidator {
    public List<ActionError> validate(WorkFlowAction workFlowAction, DefaultForm form,
                                      HttpServletRequest request) {

        String projectId = (String) form.getDto("projectId");

        OwnerUserValidator ownerUserValidator = new OwnerUserValidator();

        if (Functions.hasProjectUserPermission(projectId, Permission.VIEW, request) ||
                Functions.hasProjectUserPermission(projectId, Permission.CONFIRM, request) ||
                Functions.hasProjectUserPermission(projectId, Permission.ADMIN, request) ||
                null == ownerUserValidator.validate(workFlowAction, form, request)) {
            return null;
        }

        List<ActionError> errors = new ArrayList<ActionError>();
        errors.add(new ActionError("ProjectTime.ownerUserRequired"));
        errors.add(new ActionError("ProjectTime.permissionRequired",
                JSPHelper.getMessage(request, "ProjectAssignee.permission.confirm")));
        errors.add(new ActionError("ProjectTime.permissionRequired",
                JSPHelper.getMessage(request, "ProjectAssignee.permission.view")));
        errors.add(new ActionError("ProjectTime.permissionRequired",
                JSPHelper.getMessage(request, "ProjectAssignee.permission.admin")));

        return errors;
    }
}
