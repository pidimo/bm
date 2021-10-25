package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.cmd.admin.CompanyReadLightCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CompanyPreferencesForm.java  11-nov-2009 16:44:44$
 */
public class CompanyPreferencesForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate CompanyPreferencesForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        if (errors.isEmpty()) {
            //validate contract end email reminder, this should be of an company employee user
            String contractReminderEmail = (String) getDto("emailContract");
            if (!GenericValidator.isBlankOrNull(contractReminderEmail)) {
                if (!Functions.isValidContractEndEmailReminderInCompany(contractReminderEmail, request)) {
                    errors.add("emailError", new ActionError("Company.error.contractEndReminderEmail", JSPHelper.getMessage(request, "Company.contractEndReminderEmail")));
                }
            }

            //validate max attach size
            validateMaxAttachSize(errors, request);
        }
        return errors;
    }

    /**
     * validate max attach size, this must be less or equal to company max max attach size defined.
     * @param errors
     * @param request
     */
    private void validateMaxAttachSize(ActionErrors errors, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer companyId = Integer.valueOf(user.getValue(Constants.COMPANYID).toString());

        CompanyReadLightCmd companyReadLightCmd = new CompanyReadLightCmd();
        companyReadLightCmd.putParam("companyId", companyId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(companyReadLightCmd, null);

            if (resultDTO.containsKey("maxMaxAttachSize") && resultDTO.get("maxMaxAttachSize") != null) {
                Integer maxMaxAttachSize = (Integer) resultDTO.get("maxMaxAttachSize");
                Integer maxAttachSize = Integer.valueOf(getDto("maxAttachSize").toString());
                if (maxAttachSize > maxMaxAttachSize) {
                    errors.add("maxSize", new ActionError("Company.error.maxAttachSize", JSPHelper.getMessage(request, "Company.attach"), maxMaxAttachSize));
                }
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd...");
        }

    }
}
