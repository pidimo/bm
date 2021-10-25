package com.piramide.elwis.web.admin.form;

import com.piramide.elwis.cmd.admin.CompanyReadCmd;
import com.piramide.elwis.cmd.admin.DemoAccountCmd;
import com.piramide.elwis.dto.catalogmanager.LanguageDTO;
import com.piramide.elwis.utils.SystemLanguage;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.5
 */
public class CompanyDemoCreateForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Executing validate CompanyDemoCreateForm........." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        checkDemoAccount(errors, request);

        if (errors.isEmpty()) {
            checkSourceCompanyTemplate(errors, request);
        }
        return errors;
    }

    private void checkDemoAccount(ActionErrors errors, HttpServletRequest request) {
        String formDemoAccountId = (String) getDto("demoAccountId");
        String formRegistrationKey = (String) getDto("registrationKey");

        Integer demoAccountId = null;
        Boolean isAlreadyCreated = false;
        String registrationKey = null;

        if (!GenericValidator.isBlankOrNull(formDemoAccountId)) {
            DemoAccountCmd demoAccountCmd = new DemoAccountCmd();
            demoAccountCmd.setOp("read");
            demoAccountCmd.putParam("demoAccountId", formDemoAccountId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(demoAccountCmd, request);
                if (!resultDTO.isFailure() && resultDTO.get("demoAccountId") != null) {
                    demoAccountId = (Integer) resultDTO.get("demoAccountId");
                    isAlreadyCreated = (Boolean) resultDTO.get("isAlreadyCreated");
                    registrationKey = (String) resultDTO.get("registrationKey");
                }
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd DemoAccountCmd...", e);
            }
        }


        if (demoAccountId == null || formRegistrationKey == null || !formRegistrationKey.equals(registrationKey)) {
            log.info("Error when check demo account demoAccountId:" + formDemoAccountId + " registrationKey:" + formRegistrationKey);
            errors.add("demoAccount", new ActionError("DemoAccount.error.unexpected"));
        }

        if (isAlreadyCreated) {
            errors.add("emptyError", new ActionError("Admin.Company.new"));
            request.setAttribute("skipErrors", "true");

            //redirect to home page
            String homeUrl = ConfigurationFactory.getConfigurationManager().getValue("elwis.URL");
            request.setAttribute("jsLoad", "onload=\"location.href = '" + homeUrl + "';\"");
        }
    }

    private void checkSourceCompanyTemplate(ActionErrors errors, HttpServletRequest request) {
        String companyLogin = ConfigurationFactory.getConfigurationManager().getValue("elwis.demoAccount.template.sourceCompany.loginName");
        Integer sourceTemplateCompanyId = null;
        String sourceLanguage = null;

        if (companyLogin != null) {
            CompanyReadCmd companyReadCmd = new CompanyReadCmd();
            companyReadCmd.setOp("getCompanyByLogin");
            companyReadCmd.putParam("login", companyLogin);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(companyReadCmd, request);
                if (!resultDTO.isFailure() && resultDTO.get("companyId") != null) {
                    sourceTemplateCompanyId = new Integer(resultDTO.get("companyId").toString());
                    sourceLanguage = (String) resultDTO.get("language");
                }
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd CompanyReadCmd..", e);
            }
        }

        if (sourceTemplateCompanyId != null) {
            setDto("sourceTemplateCompanyId", sourceTemplateCompanyId);

            //define default languages
            defineDefaultLanguages(sourceLanguage);
        } else {
            log.info("Error in create demo company, not found source company template with login: " + companyLogin);
            errors.add("sourceCompany", new ActionError("DemoAccount.error.unexpected"));
        }
    }

    private void defineDefaultLanguages(String sourceLanguage) {
        String favoriteLanguage = (String) getDto("demoLocale");

        if (GenericValidator.isBlankOrNull(favoriteLanguage)) {
            favoriteLanguage = sourceLanguage;
        }

        if (favoriteLanguage == null) {
            //the default language if null
            favoriteLanguage = "en";
        }

        Locale locale = new Locale(favoriteLanguage);
        List languagesList = new ArrayList(SystemLanguage.systemLanguages.size());
        for (Iterator iterator = SystemLanguage.systemLanguages.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iterator.next();
            LanguageDTO languageDTO = new LanguageDTO();
            languageDTO.put("languageName", JSPHelper.getMessage(locale, (String) entry.getValue()));
            languageDTO.put("languageIso", entry.getKey());
            languagesList.add(languageDTO);
        }

        setDto("favoriteLanguage", favoriteLanguage);
        setDto("languages", languagesList);
    }
}
