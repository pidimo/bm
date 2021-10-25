package com.piramide.elwis.web.projects.form;

import com.piramide.elwis.web.projects.el.Functions;
import com.piramide.elwis.web.projects.util.AdminValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SubProjectForm extends DefaultForm {
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
        if (!Functions.existsProject(getDto("projectId"))) {
            return new ActionErrors();
        }

        AdminValidator adminValidator = new AdminValidator();
        if (null != adminValidator.hasPermission(request, actionMapping)) {
            return new ActionErrors();
        }

        return super.validate(actionMapping, request);
    }
}
