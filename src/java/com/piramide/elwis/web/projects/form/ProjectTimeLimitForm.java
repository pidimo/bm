package com.piramide.elwis.web.projects.form;

import com.piramide.elwis.cmd.projects.ProjectTimeLimitCmd;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public class ProjectTimeLimitForm extends DefaultForm {
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
        log.debug("Executing validate ProjectTimeLimitForm......." + getDtoMap());

        ActionErrors errors = super.validate(actionMapping, request);

        validateAssigneeSubProjectRegistered(errors, request);

        return errors;
    }

    private void validateAssigneeSubProjectRegistered(ActionErrors errors, HttpServletRequest request) {

        if (!errors.isEmpty()) {
            return;
        }

        ProjectTimeLimitCmd projectTimeLimitCmd = new ProjectTimeLimitCmd();
        projectTimeLimitCmd.setOp("existAssigneeSubProject");
        projectTimeLimitCmd.putParam("projectId", Integer.valueOf(getDto("projectId").toString()));
        projectTimeLimitCmd.putParam("assigneeId", Integer.valueOf(getDto("assigneeId").toString()));
        projectTimeLimitCmd.putParam("subProjectId", Integer.valueOf(getDto("subProjectId").toString()));

        String timeLimitId = (String) getDto("timeLimitId");
        if (!GenericValidator.isBlankOrNull(timeLimitId)) {
            projectTimeLimitCmd.putParam("timeLimitId", timeLimitId);
        }

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(projectTimeLimitCmd, request);
            Boolean alreadyRegistered = (Boolean) resultDTO.get("existAssigned");

            if (alreadyRegistered == null || alreadyRegistered) {
                errors.add("registered", new ActionError("ProjectTimeLimit.error.alreadyRegistered", resultDTO.get("assigneeName"), resultDTO.get("subProjectName")));
            }

        } catch (AppLevelException e) {
            log.error("-> Execute " + ProjectTimeLimitCmd.class.getName() + "FAIL ", e);
        }
    }
}
