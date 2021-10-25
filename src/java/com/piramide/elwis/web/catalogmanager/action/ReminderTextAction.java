package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ReminderTextAction extends ReminderLevelAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward actionForward = super.execute(mapping, form, request, response);

        String reminderLevelId = request.getParameter("dto(reminderLevelId)");
        String name = request.getParameter("dto(name)");
        ActionForwardParameters parameters = new ActionForwardParameters();
        parameters.add("reminderLevelId", reminderLevelId).
                add("dto(reminderLevelId)", reminderLevelId).
                add("dto(name)", name).
                add("dto(op)", "read");

        return parameters.forward(actionForward);
    }
}
