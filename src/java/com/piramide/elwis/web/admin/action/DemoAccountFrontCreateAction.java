package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.cmd.common.SendEmailCmd;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.template.TemplateFactory;
import com.piramide.elwis.web.common.template.TemplateResources;
import com.piramide.elwis.web.common.urlencrypt.UrlEncryptCipher;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Action to save demo account info
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.5
 */
public class DemoAccountFrontCreateAction extends DefaultAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing DemoAccountFrontCreateAction................");
        DefaultForm defaultForm = (DefaultForm) form;


        String localeStr = request.getParameter("demoLocale");
        Locale locale = Locale.getDefault();
        if (!GenericValidator.isBlankOrNull(localeStr)) {
            locale = new Locale(localeStr);
        }
        setLocaleSettings(locale, request.getSession());

        ActionForward forward = super.execute(mapping, form, request, response);

        if ("Success".equals(forward.getName())) {
            //result dto
            Map resultDto = (Map) request.getAttribute("dto");
            sendNotification(resultDto, locale, request);
        }
        return forward;
    }

    private void sendNotification(Map resultDto, Locale locale, HttpServletRequest request) {

        String demoAccountId = resultDto.get("demoAccountId").toString();
        String registrationKey = resultDto.get("registrationKey").toString();
        String userLogin = resultDto.get("userLogin").toString();
        String password = resultDto.get("password").toString();
        String companyLogin = resultDto.get("companyLogin").toString();
        String email = resultDto.get("email").toString();

        String demoValidateUrl = composeDemoValidateUrl(demoAccountId, registrationKey, locale.getLanguage(), request);

        TemplateResources resources = new TemplateResources(request);
        resources.setLocale(locale);

        Map parameters = new HashMap();
        parameters.put("messages", resources);
        //parameters.put("timeZone", userTimeZone);
        parameters.put("demoUserLogin", userLogin);
        parameters.put("demoUserPassword", password);
        parameters.put("demoCompanyLogin", companyLogin);
        parameters.put("demoValidateUrl", new String[]{demoValidateUrl});

        String message = TemplateFactory.getTemplateManager().mergeTemplate("templates/email/DemoAccountNotification.vm",
                parameters,
                resources);

        String subject = JSPHelper.getMessage(locale, "DemoAccount.notification.subject");

        SendEmailCmd cmd = new SendEmailCmd();
        cmd.putParam("message", message);
        cmd.putParam("timeZone", DateTimeZone.getDefault());
        cmd.putParam("subject", subject);
        cmd.putParam("emailTo", email);

        try {
            BusinessDelegate.i.execute(cmd, request);
        } catch (AppLevelException e) {
            log.error("Cannot execute SendEmailCmd ...", e);
        }
    }

    private String composeDemoValidateUrl(String demoAccountId, String registrationKey, String localeStr, HttpServletRequest request) {
        String validateUrl = "";

        String actionUrl = "DemoAccount/Company/Create.do?dto(demoAccountId)=" + demoAccountId + "&dto(registrationKey)=" + registrationKey + "&dto(demoLocale)=" + localeStr;
        //encrypt url
        actionUrl = UrlEncryptCipher.i.encrypt(actionUrl);

        validateUrl = Functions.getServerDomainContext(request) + "/" + actionUrl;

        log.debug("Url to validate demo account:" + validateUrl);
        return validateUrl;
    }

    /**
     * Define the locale for user messages
     * @param locale
     * @param session
     */
    private void setLocaleSettings(Locale locale, HttpSession session) {
        /** Setting the locale **/
        Config.set(session, Config.FMT_LOCALE, locale);
        /** To Maintain compatibility with struts**/
        session.setAttribute(org.apache.struts.Globals.LOCALE_KEY, locale);
    }

}
