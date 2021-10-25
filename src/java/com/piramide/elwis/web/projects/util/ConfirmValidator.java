package com.piramide.elwis.web.projects.util;

import com.piramide.elwis.utils.ProjectUserPermissionUtil;
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
public class ConfirmValidator implements WorkFlowValidator {
    public List<ActionError> validate(WorkFlowAction workFlowAction, DefaultForm form,
                                      HttpServletRequest request) {
        List<ActionError> errors = new ArrayList<ActionError>();
        String projectId = (String) form.getDto("projectId");

        OwnerUserValidator ownerUserValidator = new OwnerUserValidator();
        ActionError error = ownerUserValidator.validateProjectStatus(request);
        if (null != error) {
            errors.add(error);
            return errors;
        }

        if (Functions.hasProjectUserPermission(projectId, ProjectUserPermissionUtil.Permission.CONFIRM, request)) {
            return null;
        }


        errors.add(new ActionError("ProjectTime.permissionRequired",
                JSPHelper.getMessage(request, "ProjectAssignee.permission.confirm")));
        return errors;
    }
}
