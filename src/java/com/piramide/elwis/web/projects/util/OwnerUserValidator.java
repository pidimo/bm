package com.piramide.elwis.web.projects.util;

import com.piramide.elwis.dto.projects.ProjectDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ProjectConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
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
public class OwnerUserValidator implements WorkFlowValidator {
    public List<ActionError> validate(WorkFlowAction workFlowAction, DefaultForm form,
                                      HttpServletRequest request) {
        User sessionUser = RequestUtils.getUser(request);
        Integer sessionUserId = (Integer) sessionUser.getValue(Constants.USERID);
        Integer sessionUserAddressId = (Integer) sessionUser.getValue(Constants.USER_ADDRESSID);
        String projectTimeOwnerUserId = (String) form.getDto("userId");

        List<ActionError> errors = new ArrayList<ActionError>();

        if (!"viewButton".equals(workFlowAction.getAction())) {
            ActionError error = validateProjectStatus(request);
            if (null != error) {
                errors.add(error);
                return errors;
            }
        }

        if (sessionUserId.toString().equals(projectTimeOwnerUserId)) {
            return null;
        }

        String projectTimeAssigneeId = (String) form.getDto("assigneeId");
        if (sessionUserAddressId.toString().equals(projectTimeAssigneeId)) {
            return null;
        }

        errors.add(new ActionError("ProjectTime.ownerUserRequired"));
        return errors;
    }


    public ActionError validateProjectStatus(HttpServletRequest request) {
        ProjectDTO projectDTO = Functions.getProject(request, request.getParameter("projectId"));
        Integer projectStatus = (Integer) projectDTO.get("status");
        if (ProjectConstants.ProjectStatus.FINISHED.getValue() == projectStatus ||
                ProjectConstants.ProjectStatus.INVOICED.getValue() == projectStatus) {
            return new ActionError("ProjectTime.projectStatus.closedOrInvoiced",
                    JSPHelper.getMessage(request, "Project.status.finished"),
                    JSPHelper.getMessage(request, "Project.status.invoiced"));
        }

        return null;
    }
}
