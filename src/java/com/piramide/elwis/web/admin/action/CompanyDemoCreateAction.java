package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.cmd.common.SendEmailCmd;
import com.piramide.elwis.dto.admin.DemoAccountDTO;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import com.piramide.elwis.web.common.template.TemplateFactory;
import com.piramide.elwis.web.common.template.TemplateResources;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.EmailValidator;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Action to create demo companies and redirect to home url
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.5
 */
public class CompanyDemoCreateAction extends DefaultAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CompanyDemoCreateAction................" + request.getParameterMap());
        DefaultForm defaultForm = (DefaultForm) form;

        String localeStr = (String) defaultForm.getDto("demoLocale");
        Locale locale = Locale.getDefault();
        if (!GenericValidator.isBlankOrNull(localeStr)) {
            locale = new Locale(localeStr);
        }

        ActionForward forward = super.execute(mapping, form, request, response);

        if ("Success".equals(forward.getName())) {

            //result dto
            Map resultDto = (Map) request.getAttribute("dto");
            sendNotificationToAdmin(resultDto, locale, request);

            String homeUrl = ConfigurationFactory.getConfigurationManager().getValue("elwis.URL");
            request.setAttribute("jsLoad", "onload=\"location.href = '" + homeUrl + "';\"");
        }
        return forward;
    }

    private void sendNotificationToAdmin(Map resultDto, Locale locale, HttpServletRequest request) {

        String recipientEmails = getAdminRecipientEmails();

        if (resultDto.get("infoDemoAccountDTO") != null && recipientEmails != null) {
            DemoAccountDTO demoAccountDTO = (DemoAccountDTO) resultDto.get("infoDemoAccountDTO");

            TemplateResources resources = new TemplateResources(request);
            resources.setLocale(locale);

            Map parameters = new HashMap();
            parameters.put("messages", resources);
            //parameters.put("timeZone", userTimeZone);
            parameters.put("demoCompanyName", demoAccountDTO.get("companyName"));
            parameters.put("demoLastName", demoAccountDTO.get("lastName"));
            parameters.put("demoFirstName", demoAccountDTO.get("firstName"));
            parameters.put("demoEmail", demoAccountDTO.get("email"));
            parameters.put("demoPhoneNumber", demoAccountDTO.get("phoneNumber"));
            parameters.put("demoUserLogin", demoAccountDTO.get("userLogin"));
            parameters.put("demoUserPassword", demoAccountDTO.get("password"));
            parameters.put("demoCompanyLogin", demoAccountDTO.get("companyLogin"));

            String message = TemplateFactory.getTemplateManager().mergeTemplate("templates/email/DemoAccountAdminNotification.vm",
                    parameters,
                    resources);

            String subject = JSPHelper.getMessage(locale, "DemoAccount.adminNotification.subject");

            SendEmailCmd cmd = new SendEmailCmd();
            cmd.putParam("message", message);
            cmd.putParam("timeZone", DateTimeZone.getDefault());
            cmd.putParam("subject", subject);
            cmd.putParam("emailTo", recipientEmails);

            try {
                BusinessDelegate.i.execute(cmd, request);
            } catch (AppLevelException e) {
                log.error("Cannot execute SendEmailCmd ...", e);
            }


        }
    }

    private String getAdminRecipientEmails() {
        String validEmails = null;

        String recipients = ConfigurationFactory.getConfigurationManager().getValue("elwis.demoAccount.admin.recipient.emails");
        String[] recipientArray = recipients.split(",");

        for (int i = 0; i < recipientArray.length; i++) {
            String email = recipientArray[i].trim();
            if (!"".equals(email)) {
                if (EmailValidator.i.isValid(email)) {
                    validEmails = (validEmails != null) ? validEmails + "," + email : email;
                } else {
                    log.info("Configuration property 'elwis.demoAccount.admin.recipient.emails' has invalid emails, notification to admin is not send. Value: " + recipients);
                    validEmails = null;
                    break;
                }
            }
        }
        return validEmails;
    }
}
