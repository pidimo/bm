package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.cmd.admin.ReadTemplateCompanyCmd;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SystemLanguage;
import com.piramide.elwis.web.admin.form.CompanyForm;
import com.piramide.elwis.web.admin.form.TrialCompanyForm;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * This class build <code>CompayForm</code> object and execute <code>CompanyCreateCmd</code> command
 * to create trial company.
 *
 * @author: ivan
 */
public class CompanyTrialCreateAction extends CompanyCreateAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {


        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }
        log.debug("create trial company...");

        TrialCompanyForm trialCompanyForm = (TrialCompanyForm) form;

        ReadTemplateCompanyCmd cmd = new ReadTemplateCompanyCmd();
        cmd.putParam("language", trialCompanyForm.getDto("favoriteLanguage"));
        cmd.setOp("readTrialTemplateCompany");

        ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
        if (!resultDTO.isFailure()) {
            ActionForward forward;

            List<Map> templateModules = (List<Map>) resultDTO.get("modules");
            Integer usersAllowed = (Integer) resultDTO.get("usersAllowed");
            Integer companyTemplateId = (Integer) resultDTO.get("companyTemplateId");
            Map usersAllowedByModule = new HashMap();
            List modules = new ArrayList();
            for (Map templateModule : templateModules) {
                String moduleId = templateModule.get("moduleId").toString();
                String nameKey = templateModule.get("nameKey").toString();
                String key = nameKey.replace(".", "_") + "_" + moduleId;
                Integer moduleUsersAllowed = null;
                if (null != templateModule.get("usersAllowed")) {
                    moduleUsersAllowed = (Integer) templateModule.get("usersAllowed");
                }

                modules.add(moduleId);
                usersAllowedByModule.put(key, moduleUsersAllowed);
            }


            String companyCreateLogin = (String) trialCompanyForm.getDto("companyCreateLogin");
            String userName = (String) trialCompanyForm.getDto("userName");
            String password = (String) trialCompanyForm.getDto("userPassword");
            CompanyForm companyForm = new CompanyForm();
            companyForm.setDto("active", "true");
            companyForm.setDto("name1", trialCompanyForm.getDto("name1"));
            companyForm.setDto("name2", trialCompanyForm.getDto("name2"));
            companyForm.setDto("name3", trialCompanyForm.getDto("name3"));
            companyForm.setDto("companyCreateLogin", trialCompanyForm.getDto("companyCreateLogin"));
            companyForm.setDto("startLicenseDate", DateUtils.dateToInteger(new Date()));
            Date finishLicense = DateUtils.getNextXMonth(new Date(), 3);
            companyForm.setDto("finishLicenseDate", DateUtils.dateToInteger(finishLicense));
            companyForm.setDto("companyType", AdminConstants.CompanyType.TRIAL.getConstant());
            companyForm.setDto("usersAllowed", usersAllowed.toString());
            companyForm.setDto("templateCampaignId", companyTemplateId);
            companyForm.setDto("favoriteLanguage", trialCompanyForm.getDto("favoriteLanguage"));
            companyForm.setDto("creation_Date", DateUtils.dateToInteger(new Date()));
            companyForm.setDto("rootName1", trialCompanyForm.getDto("rootName1"));
            companyForm.setDto("rootName2", trialCompanyForm.getDto("rootName2"));
            companyForm.setDto("userName", trialCompanyForm.getDto("userName"));
            companyForm.setDto("userPassword", trialCompanyForm.getDto("userPassword"));
            companyForm.setDto("email", trialCompanyForm.getDto("email"));
            companyForm.setDto("modules", modules);
            companyForm.setDto("sentNotification", "true");
            companyForm.getDtoMap().putAll(usersAllowedByModule);

            forward = super.execute(mapping, companyForm, request, response);

            String locale = request.getParameter("locale");
            Locale defaultLocale = request.getLocale();
            if (locale != null) {
                defaultLocale = new Locale(locale);
            }
            ActionForwardParameters logonParameters = new ActionForwardParameters();
            logonParameters.add("dto(login)", userName).
                    add("dto(password)", password).
                    add("dto(companyLogin)", companyCreateLogin).
                    add("dto(language)", defaultLocale.getLanguage());

            forward = logonParameters.forward(forward);
            return forward;

        } else {
            String languageResource = (String) SystemLanguage.systemLanguages.get(trialCompanyForm.getDto("favoriteLanguage"));
            ActionErrors errors = new ActionErrors();
            errors.add("unavailableService", new ActionError("Common.serviceUnavailableByLanguage",
                    JSPHelper.getMessage(request, languageResource)));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
    }
}
