package com.piramide.elwis.web.supportmanager.form;

import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.form.MainCommunicationForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 19, 2005
 * Time: 2:48:15 PM
 * To change this template use File | Settings | File Templates.
 */

public class SupportCaseActivityForm extends MainCommunicationForm {
    private Log log = LogFactory.getLog(SupportCaseActivityForm.class);

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if ("s".equals(getDto("nosave"))) {
            return errors;
        }

        errors = super.validate(mapping, request);

        User user = RequestUtils.getUser(request);
        if (AdminConstants.INTERNAL_USER.equals(user.getValue(Constants.USER_TYPE))) {
            if (GenericValidator.isBlankOrNull((String) getDto("stateId"))) {
                errors.add("stateId", new ActionError("errors.required",
                        JSPHelper.getMessage(request, "State.title")));
            }

            if (GenericValidator.isBlankOrNull((String) getDto("toUserId"))) {
                errors.add("toUserId", new ActionError("errors.required",
                        JSPHelper.getMessage(request, "User.title")));
            }
        }
        return errors;
    }
}

