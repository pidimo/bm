package com.piramide.elwis.web.projects.action;

import com.piramide.elwis.web.projects.util.AdminValidator;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class AdminUserProjectListAction extends ProjectManagerListAction {
    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        ActionForward forward = super.validateElementExistence(request, mapping);
        if (null != forward) {
            return forward;
        }

        AdminValidator adminValidator = new AdminValidator();
        if (null != (forward = adminValidator.hasPermission(request, mapping))) {
            saveErrors(request, adminValidator.getErrors());
            return forward;
        }

        return null;
    }
}
