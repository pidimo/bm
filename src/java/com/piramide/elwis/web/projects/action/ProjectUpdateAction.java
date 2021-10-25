package com.piramide.elwis.web.projects.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProjectUpdateAction extends ProjectManagerAction {
    @Override
    protected ActionForward priorValidations(ActionForm form,
                                             HttpServletRequest request,
                                             ActionMapping mapping) {
        if (isSaveButtonPressed(request)) {
            return super.priorValidations(form, request, mapping);
        }

        return mapping.getInputForward();
    }


    private boolean isSaveButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("saveButton");
    }
}
