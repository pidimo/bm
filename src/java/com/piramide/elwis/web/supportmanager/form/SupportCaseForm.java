package com.piramide.elwis.web.supportmanager.form;

import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 19, 2005
 * Time: 2:48:15 PM
 * To change this template use File | Settings | File Templates.
 */

public class SupportCaseForm extends DefaultForm {
    // the logger
    private Log log = LogFactory.getLog(SupportCaseForm.class);

    /**
     * Validate the input fields and set defaults values to dtoMap.
     */

    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        errors = super.validate(mapping, request);// validating with super class
        log.debug("errors... " + !errors.isEmpty());
        User user = RequestUtils.getUser(request);
        String caseDescription = (String) getDto("caseDescription");

        if (GenericValidator.isBlankOrNull(caseDescription) || "<br />".equals(caseDescription.trim())) {
            errors.add("caseDescription", new ActionError("errors.required", JSPHelper.getMessage(request, "Common.detail")));
        }

        if (AdminConstants.INTERNAL_USER.equals(user.getValue(Constants.USER_TYPE))) {
            log.debug("Validation internal user ... !!!");
            if (GenericValidator.isBlankOrNull((String) getDto("stateId"))) {
                errors.add("stateId", new ActionError("errors.required", JSPHelper.getMessage(request, "State.title")));
            }

            if (GenericValidator.isBlankOrNull((String) getDto("toUserId"))) {
                errors.add("toUserId", new ActionError("errors.required", JSPHelper.getMessage(request, "User.title")));
            }
        }

        return errors;
    }
}

