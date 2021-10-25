package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ReminderLevelAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if (null != (forward = validateReminderLevelExistence(request, mapping))) {
            return forward;
        }

        return super.execute(mapping, form, request, response);
    }

    protected ActionForward validateReminderLevelExistence(HttpServletRequest request, ActionMapping mapping) {
        if (!existsReminderLevel(request.getParameter("dto(reminderLevelId)"))) {
            String name = request.getParameter("dto(name)");
            ActionErrors errors = new ActionErrors();
            errors.add("reminderLevelDelete", new ActionError("customMsg.NotFound", name));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }
        return null;
    }

    private boolean existsReminderLevel(Object keyValue) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(
                FinanceConstants.TABLE_REMINDERLEVEL,
                "reminderLevelId",
                keyValue,
                errors, new ActionError("template.NotFound"));
        return errors.isEmpty();
    }
}
