package com.piramide.elwis.web.bmapp.action.admin;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.action.LogonAction;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Action to logon as REST service.
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1
 */
public class LogonRESTAction extends LogonAction {

    @Override
    protected void defineSessionExpiredTimeout(HttpSession session, User user) {
        //A negative time indicates the session should never timeout.
        session.setMaxInactiveInterval(-1);

        //define user logged as mobile app
        user.setValue("isMobileUser", "true");
    }

    @Override
    protected void successLogin(HttpSession session, HttpServletRequest request) {
        request.setAttribute("loginJSessionId", session.getId());
    }


    @Override
    protected boolean isLogonFromMobileApp() {
        return true;
    }

    @Override
    protected ActionForward verifyUserMobileAccess(ResultDTO resultDTO, ActionMapping mapping, HttpServletRequest request) {
        if (resultDTO != null) {

            ActionErrors logInErrors = checkAccessForUserInMobileEnvironment(resultDTO, isLogonFromWVApp(), request);
            if (logInErrors != null && !logInErrors.isEmpty()) {
                saveErrors(request, logInErrors);
                return mapping.findForward("Fail");
            }
        }
        return null;
    }

    public static ActionErrors checkAccessForUserInMobileEnvironment(ResultDTO resultDTO, boolean isFromWVApp, HttpServletRequest request) {
        ActionErrors errors = null;

        if (resultDTO != null) {

            errors = new ActionErrors();
            Boolean companyEnabled = (resultDTO.get("companyMobileActive") != null) ? (Boolean) resultDTO.get("companyMobileActive") : Boolean.FALSE;
            Boolean userEnabled = (resultDTO.get("userMobileActive") != null) ? (Boolean) resultDTO.get("userMobileActive") : Boolean.FALSE;
            Boolean userWVAppEnabled = (resultDTO.get("enableMobileWVApp") != null) ? (Boolean) resultDTO.get("enableMobileWVApp") : Boolean.FALSE;

            if (!companyEnabled) {
                if (isFromWVApp) {
                    errors.add("accessCompany", new ActionError("Company.mobile.wvapp.ErrorInactiveCompany"));
                } else {
                    errors.add("accessCompany", new ActionError("Company.mobile.ErrorInactiveCompany"));
                }
                return errors;
            }

            if (isFromWVApp) {

                if (!userWVAppEnabled) {
                    errors.add("accessUser", new ActionError("Company.mobile.wvapp.ErrorInactiveUser"));
                    return errors;
                }
            } else {

                if (!userEnabled) {
                    errors.add("accessUser", new ActionError("Company.mobile.ErrorInactiveUser"));
                    return errors;
                }
            }

            if (companyEnabled) {
                Integer startLicense = (Integer) resultDTO.get("mobileStartLicense");
                Integer endLicense = (Integer) resultDTO.get("mobileEndLicense");
                Integer currentDate = DateUtils.dateToInteger(new Date());

                String datePattern = JSPHelper.getMessage(request, "datePattern").trim();

                if (startLicense != null && endLicense != null && (currentDate < startLicense || currentDate > endLicense)) {
                    errors.add("accessLicense", new ActionError("Company.mobile.ErrorIntervalLicense", DateUtils.parseDate(startLicense, datePattern), DateUtils.parseDate(endLicense, datePattern)));

                } else if (startLicense != null && endLicense == null && currentDate < startLicense) {
                    errors.add("accessLicense", new ActionError("Company.mobile.ErrorStartLicense", DateUtils.parseDate(startLicense, datePattern)));

                } else if (startLicense == null && endLicense != null && currentDate > endLicense) {
                    errors.add("accessLicense", new ActionError("Company.mobile.ErrorEndLicense", DateUtils.parseDate(endLicense, datePattern)));
                }
            }

            if (errors.isEmpty()) {
                errors = null;
            }
        }

        return errors;
    }

    protected boolean isLogonFromWVApp() {
        return false;
    }
}
