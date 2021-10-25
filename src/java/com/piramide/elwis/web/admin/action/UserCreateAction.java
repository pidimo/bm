package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.el.Functions;
import com.piramide.elwis.web.admin.form.UserInfoForm;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.CheckEntriesForwardAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Add default values in <code>UserInfoForm</code> object when create a new user.
 * <p/>
 * The default vales are:
 * <p/>
 * <code>User.active</code> = <code>true</code>
 * <p/>
 * <code>User.timeZone</code> = <code>Company.timeZone</code>
 *
 * @author yumi
 */
public class UserCreateAction extends CheckEntriesForwardAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        UserInfoForm userInfoForm = (UserInfoForm) form;
        userInfoForm.setDto("active", true);
        setTimeZoneFromUserCompany(userInfoForm, request);

        return super.execute(mapping, userInfoForm, request, response);
    }

    private void setTimeZoneFromUserCompany(UserInfoForm form, HttpServletRequest request) {
        User sessionUser = RequestUtils.getUser(request);
        String companyTimeZone =
                Functions.getCompanyTimeZone((Integer) sessionUser.getValue(Constants.COMPANYID), request);

        if (!GenericValidator.isBlankOrNull(companyTimeZone)) {
            form.setDto("timeZone", companyTimeZone);
        } else {
            form.setDto("timeZone", "");
        }
    }
}