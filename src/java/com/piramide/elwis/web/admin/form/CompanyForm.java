package com.piramide.elwis.web.admin.form;

import com.piramide.elwis.cmd.admin.CompanyReadCmd;
import com.piramide.elwis.utils.AdminConstants;
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
import org.apache.struts.validator.Resources;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Jan 19, 2005
 * Time: 10:32:27 AM
 * To change this template use File | Settings | File Templates.
 */

public class CompanyForm extends DefaultForm {
    /*public CompanyForm() {
        super();
       // super.setDto("moduleMaxRecords", new HashMap());
    }*/
    private String trialCompanyLogin;
    private String trialCompanyName;
    private String languageResource;

    public Object[] getModules() {
        List modules = (List) getDto("modules");
        return (modules != null ? modules.toArray() : new ArrayList().toArray());
    }

    public void setModules(Object[] modules) {
        log.debug("Modules!!!!!!!...:" + modules);
        if (modules != null) {
            setDto("modules", Arrays.asList(modules));
        } else {
            setDto("modules", new ArrayList());
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        if (!GenericValidator.isBlankOrNull((String) getDto("updateLanguage"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("emptyError", new ActionError("Admin.Company.new"));
            request.setAttribute("skipErrors", "true");
            setDto("updateLanguage", "");
            setTimeZoneFromTemplate(request);
            return errors;
        }

        ActionErrors errors = super.validate(mapping, request);

        if ("create".equals(getDto("op"))) {
            if (GenericValidator.isBlankOrNull((String) getDto("favoriteLanguage"))) {
                errors.add("required", new ActionError("errors.required", JSPHelper.getMessage(request, "Company.defaultUILanguage")));
            }
        }

        if (!GenericValidator.isBlankOrNull((String) getDto("copyTemplate")) &&
                GenericValidator.isBlankOrNull((String) getDto("favoriteLanguage"))) {
            errors.add("required", new ActionError("errors.required", JSPHelper.getMessage(request, "Company.defaultUILanguage")));
        }

        if (null != getDto("copyTemplate") &&
                AdminConstants.CompanyTemplateTypes.trialTemplate.getConstantAsString().equals(getDto("copyTemplate"))) {
            if (!GenericValidator.isBlankOrNull((String) getDto("favoriteLanguage")) && !createTrialCompany(request)) {
                errors.add("TrialCompany", new ActionError("Company.error.TrialCompany",
                        trialCompanyName,
                        trialCompanyLogin,
                        JSPHelper.getMessage(request, languageResource)));
            }
        }


        if (errors.isEmpty()) {
            try {
                Integer ssd = (Integer) getDto("startLicenseDate");
                Integer sed = (Integer) getDto("finishLicenseDate");
                if (ssd != null && sed != null) {
                    if (ssd.intValue() > sed.intValue()) {
                        errors.add("finishLicenseDate", new ActionError("Common.greaterThan", JSPHelper.getMessage(request, "Company.finishLicenseDate"), JSPHelper.getMessage(request, "Company.startLicenseDate")));
                    }
                }
            } catch (ClassCastException e) {
            }

            //validate max max attach size
            validateCompanyMaxMaxAttachSize(errors, request);
        }

        List modules = (List) getDto("modules");
        if (modules != null) {
            for (Iterator iterator = modules.iterator(); iterator.hasNext();) {
                String id = (String) iterator.next();
                for (Iterator iterator1 = getDtoMap().keySet().iterator(); iterator1.hasNext();) {
                    String key = (String) iterator1.next();
                    if (key.endsWith("_" + id)) {
                        String value = (String) getDto(key);
                        if (!GenericValidator.isBlankOrNull(value)) {
                            String module = key.substring(0, key.lastIndexOf("_")).replace('_', '.');
                            //System.out.println("I18N:" + module);
                            if (!GenericValidator.isInt(value)) {
                                errors.add(module, new ActionError("Company.errors.entriesLimit", Resources.getMessage(request, module)));
                            } else {
                                setDto(key, new Integer(value));
                            }
                        } else {
                            setDto(key, null);
                        }
                    }
                }
            }
        }

        //validate molice access
        validateMobileLicenseDates(request, errors);

        //define boolean values
        if (super.getDto("mobileActive") != null) {
            super.setDto("mobileActive", new Boolean(true));
        } else {
            super.setDto("mobileActive", new Boolean(false));
        }

        return errors;
    }

    private boolean createTrialCompany(HttpServletRequest request) {
        CompanyReadCmd cmd = new CompanyReadCmd();
        cmd.setOp("checkTrialCompany");
        cmd.putParam("language", getDto("favoriteLanguage"));
        Boolean createTrialCompany = true;
        try {
            ResultDTO resulDTO = BusinessDelegate.i.execute(cmd, request);
            Integer companyTrialId = (Integer) resulDTO.get("companyTrialId");

            if (null != companyTrialId && !companyTrialId.toString().equals(getDto("companyId"))) {
                createTrialCompany = false;
                String name1 = (String) resulDTO.get("name1");
                String name2 = (String) resulDTO.get("name2");
                String name3 = (String) resulDTO.get("name3");
                trialCompanyLogin = (String) resulDTO.get("trialCompanyLogin");
                trialCompanyName = (name1);
                languageResource = (String) resulDTO.get("languageResource");
                if (null != name2 && !"".equals(name2)) {
                    trialCompanyName += " " + name2;
                }
                if (null != name3 && !"".equals(name3)) {
                    trialCompanyName += " " + name3;
                }
            }
        } catch (AppLevelException e) {
            log.warn("Cannot Execute CompanyReadCmd. ", e);
        }
        return createTrialCompany;
    }

    private void setTimeZoneFromTemplate(HttpServletRequest request) {
        String templateSelected = (String) getDto("templateSelected");

        if (!GenericValidator.isBlankOrNull(templateSelected)) {
            CompanyReadCmd cmd = new CompanyReadCmd();
            cmd.putParam("companyId", templateSelected);
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
                String timeZone = (String) resultDTO.get("timeZone");
                if (!GenericValidator.isBlankOrNull(timeZone)) {
                    super.setDto("timeZone", timeZone);
                } else {
                    super.setDto("timeZone", "");
                }
            } catch (AppLevelException e) {
                log.warn("Cannot execute CompanyReadCmd ", e);
            }
        }
    }

    /**
     * validate company max max attach size, this must be less or equal to "elwis.db.blob.size" elwis property configuration 
     * @param errors
     * @param request
     */
    private void validateCompanyMaxMaxAttachSize(ActionErrors errors, HttpServletRequest request) {
        log.debug("Validate maxMaxAttachSize...." + getDto("maxMaxAttachSize"));
        Long blobMaxSizeBits = new Long(ConfigurationFactory.getValue("elwis.db.blob.size"));
        //convert to megabytes
        int blobMaxSizeMB = Math.round(((blobMaxSizeBits / 1024) / 1024));

        Integer maxMaxAttachSize = Integer.valueOf(getDto("maxMaxAttachSize").toString());

        if (maxMaxAttachSize > blobMaxSizeMB) {
            errors.add("maxSize", new ActionError("Company.error.maxMaxAttachSize", JSPHelper.getMessage(request, "Company.maxMaxAttachSize"), blobMaxSizeMB));
        }
    }

    private void validateMobileLicenseDates(HttpServletRequest request, ActionErrors errors) {
        Object endDate = getDto("mobileEndLicense");
        if (!(endDate instanceof Integer)) {
            return;
        }

        Object startDate = getDto("mobileStartLicense");
        if (!(startDate instanceof Integer)) {
            return;
        }

        if (((Integer) endDate) < ((Integer) startDate)) {
            errors.add("dateErrors", new ActionError("Common.greaterThan",
                    JSPHelper.getMessage(request, "Company.mobile.finishLicense"),
                    JSPHelper.getMessage(request, "Company.mobile.startLicense")));
        }
    }

}