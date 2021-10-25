package com.piramide.elwis.web.projects.form;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class ProjectTimeRegistrationForm extends ProjectTimeForm {
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        String projectId = (String) getDto("projectId");
        if (GenericValidator.isBlankOrNull(projectId)) {
            return new ActionErrors();
        }

        request.getParameterMap().put("projectId", projectId);

        String changeProject = (String) getDto("changeProject");
        if ("true".equals(changeProject)) {
            return new ActionErrors();
        }

        return super.validate(mapping, request);
    }
}
