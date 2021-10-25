package com.piramide.elwis.web.bmapp.action.admin;

import com.piramide.elwis.web.admin.el.Functions;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Action to logon from mobile WV app
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.3
 */
public class LogonWVAppRESTAction extends LogonRESTAction {

    @Override
    protected void addAdditionalData(DefaultForm loginForm) {
        loginForm.setDto("isLoginFromWVApp", "true");
    }

    @Override
    protected ActionForward verifyUserMobileAccess(ResultDTO resultDTO, ActionMapping mapping, HttpServletRequest request) {
        ActionForward failForward = super.verifyUserMobileAccess(resultDTO, mapping, request);

        if (failForward == null) {

            //check if user has assigned an organization
            Object userId = resultDTO.get("userId");
            if (userId != null) {
                Map userMap = Functions.getUserMap(userId);

                if (userMap.get("mobileOrganizationId") == null) {
                    ActionErrors errors = new ActionErrors();
                    errors.add("error", new ActionError("User.mobile.error.emptyOrganization"));
                    saveErrors(request, errors);

                    failForward = mapping.findForward("Fail");
                }
            }
        }

        return failForward;
    }

    @Override
    protected boolean isLogonFromWVApp() {
        return true;
    }
}
