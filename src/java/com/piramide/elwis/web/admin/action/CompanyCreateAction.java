package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.cmd.common.SendEmailCmd;
import com.piramide.elwis.dto.catalogmanager.LanguageDTO;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SystemLanguage;
import com.piramide.elwis.utils.TelecomType;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import com.piramide.elwis.web.admin.form.CompanyForm;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.template.TemplateFactory;
import com.piramide.elwis.web.common.template.TemplateResources;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * Company create action
 */

public class CompanyCreateAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        CompanyForm companyForm = (CompanyForm) form;

        /*** Setting values needed to command **/
        /*default telecomtypes names*/
        Locale locale = new Locale(companyForm.getDto("favoriteLanguage").toString());
        List languages = new ArrayList(SystemLanguage.systemLanguages.size());
        for (Iterator iterator = SystemLanguage.systemLanguages.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            LanguageDTO languageDTO = new LanguageDTO();
            languageDTO.put("languageName", JSPHelper.getMessage(locale, (String) entry.getValue()));
            languageDTO.put("languageIso", entry.getKey());
            languages.add(languageDTO);
        }

        companyForm.setDto("languages", languages);
        /*companyForm.setDto("languageByDefault",companyForm.getDto("favoriteLanguage"));*/

        List telecomTypes = new ArrayList(5);
        TelecomTypeDTO phone = new TelecomTypeDTO();
        TelecomTypeDTO mobile = new TelecomTypeDTO();
        TelecomTypeDTO fax = new TelecomTypeDTO();
        TelecomTypeDTO email = new TelecomTypeDTO();
        TelecomTypeDTO internet = new TelecomTypeDTO();
        phone.put("telecomTypeName", JSPHelper.getMessage(locale, "Contact.phone"));
        phone.put("type", TelecomType.PHONE_TYPE);
        phone.put("position", "1");
        mobile.put("telecomTypeName", JSPHelper.getMessage(locale, "Contact.mobile"));
        mobile.put("type", TelecomType.PHONE_TYPE);
        mobile.put("position", "2");
        fax.put("telecomTypeName", JSPHelper.getMessage(locale, "Contact.fax"));
        fax.put("type", TelecomType.FAX_TYPE);
        fax.put("position", "3");
        email.put("telecomTypeName", JSPHelper.getMessage(locale, "Contact.email"));
        email.put("type", TelecomType.EMAIL_TYPE);
        email.put("position", "4");
        internet.put("telecomTypeName", JSPHelper.getMessage(locale, "Contact.internet"));
        internet.put("type", TelecomType.LINK_TYPE);
        internet.put("position", "5");
        telecomTypes.add(phone);
        telecomTypes.add(mobile);
        telecomTypes.add(fax);
        telecomTypes.add(email);
        telecomTypes.add(internet);
        companyForm.setDto("defaultTelecomTypes", telecomTypes);

        ActionForward forward = super.execute(mapping, form, request, response);
        if ("Success".equals(forward.getName()) &&
                null != companyForm.getDto("sentNotification") &&
                "true".equals(companyForm.getDto("sentNotification"))) {
            sendNotification(request, companyForm, locale);
        }

        return forward;

    }

    private void sendNotification(HttpServletRequest request, CompanyForm companyForm, Locale locale) {
        Map parameters = new HashMap();
        Map companyInformation = new HashMap();
        companyInformation.put("name", buildCompanyName(companyForm));
        companyInformation.put("login", companyForm.getDto("companyCreateLogin"));
        if (null != companyForm.getDto("startLicenseDate") && !"".equals(companyForm.getDto("startLicenseDate").toString().trim())) {
            companyInformation.put("startLicence", companyForm.getDto("startLicenseDate"));
        }
        if (null != companyForm.getDto("finishLicenseDate") && !"".equals(companyForm.getDto("finishLicenseDate").toString().trim())) {
            companyInformation.put("endLicence", companyForm.getDto("finishLicenseDate"));
        }

        parameters.put("showNumberUsers", false);
        if (null != companyForm.getDto("usersAllowed") && !"".equals(companyForm.getDto("usersAllowed").toString().trim())) {
            parameters.put("showNumberUsers", true);
            companyInformation.put("users", companyForm.getDto("usersAllowed"));
        }

        Map administratorInformation = new HashMap();
        administratorInformation.put("lastName", companyForm.getDto("rootName1"));
        administratorInformation.put("firstName", companyForm.getDto("rootName2"));
        administratorInformation.put("userName", companyForm.getDto("userName"));
        administratorInformation.put("password", companyForm.getDto("userPassword"));


        User user = RequestUtils.getUser(request);
        DateTimeZone userTimeZone = DateTimeZone.getDefault();
        if (null != user) {
            userTimeZone = (DateTimeZone) user.getValue("dateTimeZone");
        }


        TemplateResources resources = new TemplateResources(request);
        resources.setLocale(locale);


        String appUrl = ConfigurationFactory.getConfigurationManager().getValue("elwis.URL");
        parameters.put("timeZone", userTimeZone);
        parameters.put("company", companyInformation);
        parameters.put("administrator", administratorInformation);
        parameters.put("messages", resources);
        parameters.put("serverName", new String[]{appUrl});


        String message = TemplateFactory.getTemplateManager().mergeTemplate("templates/email/CompanyCreateNotification.vm",
                parameters,
                resources);
        String toEmail = (String) companyForm.getDto("email");
        String subject = JSPHelper.getMessage(locale, "Notification.Company.create");

        SendEmailCmd cmd = new SendEmailCmd();
        cmd.putParam("message", message);
        cmd.putParam("timeZone", userTimeZone);
        cmd.putParam("subject", subject);
        cmd.putParam("emailTo", toEmail);
        cmd.putParam("userId", user.getValue(Constants.USERID));

        try {
            BusinessDelegate.i.execute(cmd, request);
        } catch (AppLevelException e) {
            log.error("Cannot execute SendEmailCmd ...", e);
        }
    }


    private String buildCompanyName(CompanyForm companyForm) {
        String name1 = (String) companyForm.getDto("name1");
        if (null != companyForm.getDto("name2")) {
            name1 += " " + companyForm.getDto("name2");
        }
        if (null != companyForm.getDto("name3")) {
            name1 += " " + companyForm.getDto("name3");
        }

        return name1;
    }
}

