package com.piramide.elwis.web.projects.form;

import com.piramide.elwis.dto.projects.ProjectDTO;
import com.piramide.elwis.utils.Constants;
import static com.piramide.elwis.utils.ProjectUserPermissionUtil.Permission;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.DataBaseValidator;
import com.piramide.elwis.web.projects.el.Functions;
import com.piramide.elwis.web.projects.util.AdminValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProjectUserForm extends DefaultForm {
    private ProjectDTO projectDTO;

    @Override
    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest request) {
        if (!Functions.existsProject(getDto("projectId"))) {
            return new ActionErrors();
        }

        AdminValidator adminValidator = new AdminValidator();
        if (null != adminValidator.hasPermission(request, actionMapping)) {
            return new ActionErrors();
        }

        if (!isSaveButtonPressed(request) && !isSaveAndNewButtonPressed(request)) {
            return new ActionErrors();
        }

        projectDTO = Functions.getProject(request, (String) getDto("projectId"));

        ActionErrors errors = super.validate(actionMapping, request);
        ActionError responsiblePermissionError = validateProjectResponsiblePermission(request);
        if (null != responsiblePermissionError) {
            errors.add("responsiblePermissionError", responsiblePermissionError);
        }

        ActionError duplicatedError = validateDuplicatedUSer(request);
        if (null != duplicatedError) {
            errors.add("duplicatedError", duplicatedError);
        }

        return errors;
    }

    private ActionError validateProjectResponsiblePermission(HttpServletRequest request) {
        Integer actualResponsibleAddressId = (Integer) projectDTO.get("responsibleAddressId");

        String responsibleAddressId = (String) getDto("responsibleAddressId");
        if (GenericValidator.isBlankOrNull(responsibleAddressId)) {
            return null;
        }

        String addressId = (String) getDto("addressId");
        if (GenericValidator.isBlankOrNull(addressId)) {
            return null;
        }

        String administrationPermission =
                (String) getDto(Permission.ADMIN.getName().toLowerCase());

        if (!responsibleAddressId.equals(actualResponsibleAddressId.toString()) &&
                addressId.equals(actualResponsibleAddressId.toString()) &&
                GenericValidator.isBlankOrNull(administrationPermission)) {
            return new ActionError("errors.required",
                    JSPHelper.getMessage(request, "ProjectAssignee.permission.admin"));
        }

        if (responsibleAddressId.equals(actualResponsibleAddressId.toString()) &&
                addressId.equals(actualResponsibleAddressId.toString()) &&
                GenericValidator.isBlankOrNull(administrationPermission)) {
            return new ActionError("errors.required",
                    JSPHelper.getMessage(request, "ProjectAssignee.permission.admin"));
        }
        return null;
    }

    private ActionError validateDuplicatedUSer(HttpServletRequest request) {
        if (!"create".equals(getDto("op").toString())) {
            return null;
        }

        String addressId = (String) getDto("addressId");
        if (GenericValidator.isBlankOrNull(addressId)) {
            return null;
        }

        User user = RequestUtils.getUser(request);
        Map conditions = new HashMap();
        conditions.put("companyId", user.getValue(Constants.COMPANYID));
        conditions.put("projectId", getDto("projectId").toString());
        boolean isDuplicated = DataBaseValidator.i.isDuplicate("projectassignee",
                "addressid", addressId,
                "projectId", getDto("projectId").toString(), conditions, false);

        if (isDuplicated) {
            return new ActionError("ProjectAssignee.error.addressDuplicated", getDto("userName"));
        }

        return null;
    }

    private boolean isSaveButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("saveButton");
    }

    private boolean isSaveAndNewButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("SaveAndNew");
    }
}
