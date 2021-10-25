package com.piramide.elwis.web.projects.action;

import com.piramide.elwis.web.projects.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProjectUserCreateAction extends ProjectManagerAction {

    @Override
    protected ActionForward priorValidations(ActionForm form,
                                             HttpServletRequest request,
                                             ActionMapping mapping) {
        if (isSaveButtonPressed(request) || isSaveAndNewButtonPressed(request)) {
            return super.priorValidations(form, request, mapping);
        }

        ActionForward forward = super.priorValidations(form, request, mapping);
        if (null != forward) {
            return forward;
        }

        DefaultForm defaultForm = (DefaultForm) form;
        setDefaultPermissions(defaultForm, request);
        return mapping.getInputForward();
    }

    private void setDefaultPermissions(DefaultForm defaultForm, HttpServletRequest request) {
        Functions.setDefaultValuesForProjectUsers(defaultForm, request);
    }

    private boolean isSaveButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("saveButton");
    }

    private boolean isSaveAndNewButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("SaveAndNew");
    }
}
