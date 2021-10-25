package com.piramide.elwis.web.bmapp.action.admin;

import com.piramide.elwis.web.admin.form.UserChangeAppForm;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.3
 */
public class UserChangeRESTAction extends DefaultAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  UserChangeRESTAction..." + request.getParameterMap());

        UserChangeAppForm userChangeAppForm = (UserChangeAppForm) form;
        log.debug("FORM DTO FROM REST:::::::" + userChangeAppForm.getDtoMap());

        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue("userId");

        ActionErrors errors = validate(userChangeAppForm, mapping,request);

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        //add user id in dto
        userChangeAppForm.setDto("userId", userId);

        return super.execute(mapping, userChangeAppForm, request, response);
    }

    private ActionErrors validate(UserChangeAppForm userChangeAppForm, ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = userChangeAppForm.validate(mapping, request);

        //define boolean values
        if (userChangeAppForm.getDto("visibleMobileApp") != null) {
            userChangeAppForm.setDto("visibleMobileApp", "true".equals(userChangeAppForm.getDto("visibleMobileApp").toString()));
        }

        ActionErrors pwdErrors = validateChangePassword(userChangeAppForm, request);

        errors.add(pwdErrors);
        return errors;
    }

    private ActionErrors validateChangePassword(DefaultForm defaultForm, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        String userPassword = (String) defaultForm.getDto("userPassword");
        String password1 = (String) defaultForm.getDto("password1");
        String password2 = (String) defaultForm.getDto("password2");

        if (GenericValidator.isBlankOrNull(userPassword) && GenericValidator.isBlankOrNull(password1) && GenericValidator.isBlankOrNull(password2)) {
            return new ActionErrors();
        }

        if (GenericValidator.isBlankOrNull(userPassword) || GenericValidator.isBlankOrNull(password1) || GenericValidator.isBlankOrNull(password2)) {
            errors.add("emptyPassword", new ActionError("User.mobile.wvapp.error.emptyPassword"));
        } else if (password1.length() < 6) {
            errors.add("lengthError", new ActionError("errors.minlength", JSPHelper.getMessage(request, "User.passNew"), 6));
        } else if (!password1.equals(password2)) {
            errors.add("eqError", new ActionError("errors.twofields"));
        }

        return errors;
    }
}
