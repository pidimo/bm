package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.cmd.admin.LogonCmd;
import com.piramide.elwis.cmd.contactmanager.CompanyCmd;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.el.Functions;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.admin.session.UserSessionLogUtil;
import com.piramide.elwis.web.common.urlencrypt.UrlEncryptCipher;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.joda.time.DateTimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Log on into application, retrieving the user info.
 *
 * @author Fernando Montaño
 * @version $Id: LogonAction.java 12655 2017-03-28 00:45:30Z miguel $
 */

public class LogonAction extends DefaultAction {

    private Log log = LogFactory.getLog(this.getClass());

    /**
     * Execute the logon action
     */

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("  Elwis LogonAction executing  ...  ");
        DefaultForm loginForm = (DefaultForm) form;

        //add additional data in form dto before execute cmd
        addAdditionalData(loginForm);

        //create a new session if there's no one.
        HttpSession session = request.getSession(true);

        //define mode bootstrap attribute
        if("true".equals(request.getParameter("isBootstrapUI"))) {
            request.getSession().setAttribute("isBootstrapUI", true);
        }

        LogonCmd logonCmd = new LogonCmd();
        logonCmd.putParam(loginForm.getDtoMap());
        logonCmd.putParam("ip", request.getRemoteAddr());


        ResultDTO resultDTO = BusinessDelegate.i.execute(logonCmd, request);

        if (resultDTO.isFailure()) {
            ActionErrors errors;
            if (resultDTO.containsKey("msgError")) {
                Locale locale;
                try {
                    locale = new Locale((String) loginForm.getDto("language"));
                } catch (Exception e) {
                    locale = Locale.getDefault();
                }
                String dataPattern = JSPHelper.getMessage(locale, "datePattern");
                errors = new ActionErrors();
                String msg = (String) resultDTO.get("msgError");
                Date param = DateUtils.integerToDate((Integer) resultDTO.get("msgErrorParam"));
                String paramStr = DateUtils.parseDate(param, dataPattern);
                String paramStr1 = null;

                if (resultDTO.containsKey("msgErrorParam2")) {
                    Date param2 = DateUtils.integerToDate((Integer) resultDTO.get("msgErrorParam2"));
                    paramStr1 = DateUtils.parseDate(param2, dataPattern);
                }
                if (paramStr1 == null) {
                    errors.add(msg, new ActionError(msg, paramStr));
                } else {
                    errors.add(msg, new ActionError(msg, paramStr, paramStr1));
                }

            } else {
                errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
            }
            saveErrors(request, errors);
            request.setAttribute("hasErrors", "true");
            return mapping.findForward("Fail");

        }

        Integer companyType = (Integer) resultDTO.get("companyType");
        User user = new User();
        //define user attributes recovering from resultDTO
        setUserSessionAttributes(resultDTO, loginForm.getDto("language").toString(), user);
        //initialize locale
        initializeLocaleSettings(user, session);


        //validations to enable access from apps
        ActionForward mobileAccessForward = verifyUserMobileAccess(resultDTO, mapping, request);
        if (mobileAccessForward != null) {
            return mobileAccessForward;
        }

        //verify if user is planned to change your password
        Integer userId = new Integer(resultDTO.get("userId").toString());
        DateTimeZone dateTimeZone= (DateTimeZone) resultDTO.get("dateTimeZone");
        boolean isMobileInterface = loginForm.getDto("mobile") != null;
        if (!isMobileInterface) {
            ActionForward forward = getPasswordChangeForward(userId, dateTimeZone, mapping, request);
            if (forward != null) {
                return forward;
            }
        }

        ActionErrors aErrors;
        aErrors = MessagesUtil.i.convertToActionErrors(mapping, request, logonCmd.getResultDTO());
        saveErrors(request, aErrors);

        //define the session expire value
        defineSessionExpiredTimeout(session, user);

        //Setting the user in the session
        session.setAttribute(Constants.USER_KEY, user);


        //setting the layout to use
        //session.setAttribute("layout", "/layout/themes/" + theme);
        session.setAttribute("layout", "/layout/ui");
        //session.setAttribute("baselayout", request.getContextPath() + "/layout/themes/" + theme);
        session.setAttribute("baselayout", request.getContextPath() + "/layout/ui");

        /**
         * Adding a mobile attribute in the user session in order to know if
         * it is using mobile interface or not.
         * To do this it expect a dto(mobile) from the logon form.
         */
        if (loginForm.getDto("mobile") != null) {
            user.setValue("mobile", true);
        }

        /**
         * Remember user, company cookies management
         */
        // Remember Info
        if (loginForm.getDto("rememberInfo") != null) {
            log.debug("Remember info enables");
            Cookie c = new Cookie("UserLastLogin", URLEncoder.encode((String) loginForm.getDto("login"), Constants.CHARSET_ENCODING));
            c.setMaxAge(2147483647);
            response.addCookie(c);
            c = new Cookie("UserLastCompany", "" + URLEncoder.encode((String) loginForm.getDto("companyLogin"), Constants.CHARSET_ENCODING));
            c.setMaxAge(2147483647);
            response.addCookie(c);
        } else {
            log.debug("remember info disabled");
            Cookie c = new Cookie("UserLastLogin", null);
            c.setMaxAge(0);
            response.addCookie(c);
            c = new Cookie("UserLastCompany", "" + null);
            c.setMaxAge(0);
            response.addCookie(c);
        }

        //set logoId in context application
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
        putCompanyLogoIdInContext(companyId, request);

        //put counter to update style sheet
        request.getSession().setAttribute("logonStyleStatus", new Long(System.currentTimeMillis()));


        //success login
        successLogin(session, request);

        log.info("[" + loginForm.getDto("login") + "/" + loginForm.getDto("companyLogin") + "] authenticated successfully");

        //User session tracking
        ActionForward userSessionForward = userSessionTracking(user, companyType, session, mapping, request);
        if (userSessionForward != null) {
            return userSessionForward;
        }

        return mapping.findForward("Success");
    }

    /**
     * Return the forward to the user can change your password, only if for this user has been planned,
     * otherwise return null. So this manage ofter external log on and inner log on
     * @param userId
     * @param dateTimeZone
     * @param mapping
     * @param request
     * @return ActionForward
     */
    private ActionForward getPasswordChangeForward(Integer userId, DateTimeZone dateTimeZone, ActionMapping mapping, HttpServletRequest request) {
        ActionForward forward = null;

        List<Map> plannedUserPasswordChanges = Functions.getPlannedUserPasswordChanges(userId, dateTimeZone, request);
        if (!plannedUserPasswordChanges.isEmpty()) {
            Map userPasswordChangeMap = plannedUserPasswordChanges.get(0);
            String passwordChangeId = userPasswordChangeMap.get("passwordChangeId").toString();
            String description = (String) userPasswordChangeMap.get("description");

            ActionErrors errors = new ActionErrors();
            errors.add("changeMessage", new ActionError("PasswordChange.plannedMessage", description));
            saveErrors(request, errors);

            //to external log on
            String changeMessage = UrlEncryptCipher.i.encrypt(JSPHelper.getMessage(request, "PasswordChange.plannedMessage", description));
            String changeRelativeUrl = "PlannedUserPasswordChange.jsp?userId=" + userId + "&passwordChangeId=" + passwordChangeId + "&messageText=" + changeMessage;

            ActionForwardParameters forwardParameters = new ActionForwardParameters();
            forwardParameters.add("userId", userId.toString()).
                    add("passwordChangeId", passwordChangeId).
                    add("urlPasswordChange", UrlEncryptCipher.i.encrypt(changeRelativeUrl));

            forward = forwardParameters.forward(mapping.findForward("ChangePassword"));
        }

        return forward;
    }

    private void initializeLocaleSettings(User user, HttpSession session) {
        /** Setting the locale **/
        Config.set(session, Config.FMT_LOCALE, new Locale(user.getValue("locale").toString()));
        Config.set(session, "bundle", Constants.APPLICATION_RESOURCES);
        /** To Maintain compatibility with struts**/
        session.setAttribute(org.apache.struts.Globals.LOCALE_KEY, new Locale(user.getValue("locale").toString()));
    }

    private void setUserSessionAttributes(ResultDTO resultDTO, String locale, User user) {

        /** Set the user values **/
        user.setValue("userId", resultDTO.get("userId"));
        user.setValue("login", resultDTO.get("login"));
        user.setValue(Constants.USER_ADDRESSID, resultDTO.get(Constants.USER_ADDRESSID));
        user.setValue(Constants.USER_TYPE, resultDTO.get(Constants.USER_TYPE));

        //set user locale
        if (resultDTO.get("userLanguage") != null) {
            user.setValue("locale", resultDTO.get("userLanguage"));
        } else // default value
        {
            user.setValue("locale", locale);
        }

        // max recent list
        if (resultDTO.get("userMaxRecentList") != null) {
            user.setValue("maxRecentList", resultDTO.get("userMaxRecentList"));
        } else // by default value
        {
            user.setValue("maxRecentList", new Integer(10));
        }

        //rows per page
        if (resultDTO.get("userRowsPerPage") != null) {
            user.setValue("rowsPerPage", resultDTO.get("userRowsPerPage")); // from user
        } else if (resultDTO.get("companyRowsPerPage") != null) {
            user.setValue("rowsPerPage", resultDTO.get("companyRowsPerPage")); //from company
        } else {
            user.setValue("rowsPerPage", new Integer(10)); //by default
        }

        //company id
        user.setValue("companyId", resultDTO.get("companyId"));

        if (resultDTO.get("isDefaultCompany") != null) {
            user.setValue("isDefaultCompany", resultDTO.get("isDefaultCompany"));
        }

        //birthday list range
        if (resultDTO.get("rangeBirthdayStart") != null) {
            user.setValue("rangeBirthdayStart", resultDTO.get("rangeBirthdayStart"));
        } else {
            user.setValue("rangeBirthdayStart", new Integer(0));
        }

        if (resultDTO.get("rangeBirthdayFinish") != null) {
            user.setValue("rangeBirthdayFinish", resultDTO.get("rangeBirthdayFinish"));
        } else {
            user.setValue("rangeBirthdayFinish", new Integer(0));
        }

        //timeout
        if (resultDTO.get("userTimeout") != null) {
            user.setValue("timeout", resultDTO.get("userTimeout")); //from user
        } else if (resultDTO.get("companyTimeout") != null) {
            user.setValue("timeout", resultDTO.get("companyTimeout")); //from company
        } else {
            user.setValue("timeout", new Integer(30)); //by default
        }

        //max attach size
        if (resultDTO.get("maxAttachSize") != null) {
            user.setValue("maxAttachSize", resultDTO.get("maxAttachSize")); //from company
        } else {
            user.setValue("maxAttachSize", new Integer(2)); //by default 2MB limit
        }

        //company mail domain
        if (resultDTO.get("companyMailDomain") != null) {
            user.setValue("companyMailDomain", resultDTO.get("companyMailDomain"));
        }

        //user has mail account
        if (resultDTO.get("hasMailAccount") != null && ((Boolean) resultDTO.get("hasMailAccount")).booleanValue()) {
            user.setValue("sid", "{40F7EF26D0656-40F7EF26D065B-1089990438}");
        }
        user.setValue("dateTimeZone", resultDTO.get("dateTimeZone"));

        // update lastAction-userSessionLog
        user.setValue("lastActionTime", new Long(System.currentTimeMillis()));

        //setting up the backgroundDownload property in Session object 
        user.setValue("backgroundDownload", resultDTO.get("backgroundDownload"));

        //setting user access rights
        user.setSecurityAccessRights((Map) resultDTO.get("accessRights"));
    }

    /**
     * put in context application the logoId if this have
     *
     * @param companyId
     * @param request
     * @throws Exception
     */
    private void putCompanyLogoIdInContext(Integer companyId, HttpServletRequest request) throws Exception {

        Object contextCompanyLogo = request.getSession().getServletContext().getAttribute("companyLogoId_" + companyId);

        if (contextCompanyLogo == null) {
            CompanyCmd companyCmd = new CompanyCmd();
            companyCmd.putParam("addressId", companyId);
            ResultDTO resultDTO = BusinessDelegate.i.execute(companyCmd, request);

            //set logoId in contex application
            Object objLogoId = resultDTO.get("logoId");
            if (objLogoId != null) {
                request.getSession().getServletContext().setAttribute("companyLogoId_" + companyId, objLogoId.toString());
            }
        }
    }

    /**
     * User session tracking, check if other user is connected with the same account
     *
     * @param user
     * @param session
     * @param mapping
     * @param request
     * @return forward, null if no exist other user
     */
    private ActionForward userSessionTracking(User user, Integer companyType, HttpSession session, ActionMapping mapping, HttpServletRequest request) {
        ActionForward forward = null;

        Integer userId = (Integer) user.getValue("userId");
        boolean isLogonFromApp = isLogonFromMobileApp();

        if (!isLogonFromMobileApp() && !AdminConstants.CompanyType.DEMO.equal(companyType)) {
            if (UserSessionLogUtil.existOtherUserConnected(userId, session.getId())) {

                //to external log on
                String relativeUrl = "BannedConectedUser.jsp";

                ActionForwardParameters forwardParameters = new ActionForwardParameters();
                forwardParameters.add("urlOtherUserConnected", UrlEncryptCipher.i.encrypt(relativeUrl));

                forward = forwardParameters.forward(mapping.findForward("OtherUserConnected"));
                return forward;
            }
        }

        UserSessionLogUtil.startSession(user, session.getId(), isLogonFromApp, request);

        return forward;
    }

    /**
     * Define the session timeout for this user
     * @param session
     * @param user
     */
    protected void defineSessionExpiredTimeout(HttpSession session, User user) {
        session.setMaxInactiveInterval((Integer) user.getValue("timeout") * 60);
    }

    protected void successLogin(HttpSession session, HttpServletRequest request) {
    }

    protected ActionForward verifyUserMobileAccess(ResultDTO resultDTO, ActionMapping mapping, HttpServletRequest request) {
        return null;
    }

    protected boolean isLogonFromMobileApp() {
        return false;
    }

    protected void addAdditionalData(DefaultForm loginForm) {

    }

}
