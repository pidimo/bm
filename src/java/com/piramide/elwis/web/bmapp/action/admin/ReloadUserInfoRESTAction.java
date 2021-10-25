package com.piramide.elwis.web.bmapp.action.admin;

import com.piramide.elwis.cmd.admin.ReloadUserInfoCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1.6
 */
public class ReloadUserInfoRESTAction extends DefaultAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing  ReloadUserInfoRESTAction..." + request.getParameterMap());

        User user = RequestUtils.getUser(request);
        Integer sessionUserId = Integer.valueOf(user.getValue(Constants.USERID).toString());

        ReloadUserInfoCmd reloadUserInfoCmd = new ReloadUserInfoCmd();
        reloadUserInfoCmd.putParam("userId", sessionUserId);

        ResultDTO resultDTO = BusinessDelegate.i.execute(reloadUserInfoCmd, request);

        if (resultDTO.isFailure()) {
            return mapping.findForward("Fail");
        }

        ActionForward mobileAccessForward = verifyUserMobileAccess(resultDTO, mapping, request);
        if (mobileAccessForward != null) {
            //invalidate session
            request.getSession().invalidate();
            return mobileAccessForward;
        }

        updateUserSessionAttributes(resultDTO, user);
        updateLocaleSettings(user, request);

        return mapping.findForward("Success");
    }

    private void updateUserSessionAttributes(ResultDTO resultDTO, User user) {
        //set user locale
        if (resultDTO.get("userLanguage") != null) {
            user.setValue("locale", resultDTO.get("userLanguage"));
        }

        user.setValue("dateTimeZone", resultDTO.get("dateTimeZone"));

        // update lastAction-userSessionLog
        user.setValue("lastActionTime", new Long(System.currentTimeMillis()));

        //setting user access rights
        user.setSecurityAccessRights((Map) resultDTO.get("accessRights"));
    }

    private void updateLocaleSettings(User user, HttpServletRequest request) {
        /** Setting the locale **/
        Config.set(request.getSession(), Config.FMT_LOCALE, new Locale(user.getValue("locale").toString()));
        request.getSession().setAttribute(org.apache.struts.Globals.LOCALE_KEY, new Locale(user.getValue("locale").toString()));
    }

    protected ActionForward verifyUserMobileAccess(ResultDTO resultDTO, ActionMapping mapping, HttpServletRequest request) {
        if (resultDTO != null) {

            ActionErrors logInErrors = LogonRESTAction.checkAccessForUserInMobileEnvironment(resultDTO, isReloadInfoFromWVApp(), request);
            if (logInErrors != null && !logInErrors.isEmpty()) {
                saveErrors(request, logInErrors);
                return mapping.findForward("Fail");
            }
        }

        return null;
    }

    protected boolean isReloadInfoFromWVApp() {
        return false;
    }

}
