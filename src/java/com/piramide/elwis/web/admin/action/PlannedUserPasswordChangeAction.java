package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.DefaultAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.5.3
 */
public class PlannedUserPasswordChangeAction extends DefaultAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing PlannedUserPasswordChangeAction................");
        DefaultForm defaultForm = (DefaultForm) form;
        Integer userId = new Integer(defaultForm.getDto("userId").toString());
        Integer passwordChangeId = new Integer(defaultForm.getDto("passwordChangeId").toString());

        ActionErrors errors = foreignValidationUserPasswordChange(userId, passwordChangeId);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            request.setAttribute("hasErrors", "true");
            return mapping.findForward("Fail");
        }

        ActionForward forward = super.execute(mapping, form, request, response);

        if ("Success".equals(forward.getName())) {
            request.setAttribute("hasErrors", "true"); //to show Ok message in main page
        }
        return forward;
    }

    private ActionErrors foreignValidationUserPasswordChange(Object userId, Object passwordChangeId) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(
                AdminConstants.TABLE_USERPASSWORDCHANGE,
                "passwordchangeid",
                "userid",
                passwordChangeId,
                userId,
                errors, new ActionError("PasswordChange.error.userNotFound"));
        return errors;
    }
}
