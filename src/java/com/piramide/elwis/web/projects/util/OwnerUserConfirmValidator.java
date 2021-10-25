package com.piramide.elwis.web.projects.util;

import com.piramide.elwis.web.common.util.JSPHelper;
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
public class OwnerUserConfirmValidator implements WorkFlowValidator {
    public List<ActionError> validate(WorkFlowAction workFlowAction,
                                      DefaultForm form,
                                      HttpServletRequest request) {
        OwnerUserValidator ownerUserValidator = new OwnerUserValidator();
        ConfirmValidator confirmValidator = new ConfirmValidator();

        List<ActionError> errors = new ArrayList<ActionError>();
        ActionError error = ownerUserValidator.validateProjectStatus(request);
        if (null != error) {
            errors.add(error);
            return errors;
        }

        if (null == ownerUserValidator.validate(workFlowAction, form, request) ||
                null == confirmValidator.validate(workFlowAction, form, request)) {
            return null;
        }


        errors.add(new ActionError("ProjectTime.ownerUserRequired"));
        errors.add(new ActionError("ProjectTime.permissionRequired",
                JSPHelper.getMessage(request, "ProjectAssignee.permission.confirm")));
        return errors;
    }
}
