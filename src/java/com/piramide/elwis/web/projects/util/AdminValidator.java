package com.piramide.elwis.web.projects.util;

import com.piramide.elwis.utils.ProjectUserPermissionUtil;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.projects.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class AdminValidator implements WorkFlowValidator {
    private ActionErrors errors = new ActionErrors();

    public List<ActionError> validate(WorkFlowAction workFlowAction, DefaultForm form, HttpServletRequest request) {
        List<ActionError> errorList = new ArrayList<ActionError>();

        OwnerUserValidator ownerUserValidator = new OwnerUserValidator();
        ActionError error = ownerUserValidator.validateProjectStatus(request);
        if (null != error) {
            errorList.add(error);
            return errorList;
        }

        if (!Functions.hasProjectUserPermission(request.getParameter("projectId"),
                ProjectUserPermissionUtil.Permission.ADMIN, request)) {
            errorList.add(getError(request));
            return errorList;
        }
        return null;
    }

    private ActionError getError(HttpServletRequest request) {
        return new ActionError("ProjectTime.permissionRequired",
                JSPHelper.getMessage(request, "ProjectAssignee.permission.admin"));
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

    public ActionForward hasPermission(HttpServletRequest request, ActionMapping mapping) {
        if (!Functions.hasProjectUserPermission(request.getParameter("projectId"),
                ProjectUserPermissionUtil.Permission.ADMIN, request)) {
            errors.add("AdminPermissionError", getError(request));
            request.getParameterMap().remove("SaveAndNew");
            return getForward(request, mapping, "Detail");
        }

        return null;
    }

    public ActionErrors getErrors() {
        return errors;
    }
}
