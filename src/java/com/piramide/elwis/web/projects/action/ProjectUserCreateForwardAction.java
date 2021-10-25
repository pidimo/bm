package com.piramide.elwis.web.projects.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.projects.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ProjectUserCreateForwardAction extends AdminUserProjectForwardAction {
    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        if (null == request.getParameter("createPerson")) {
            User sessionUser = RequestUtils.getUser(request);
            defaultForm.setDto("addressId", sessionUser.getValue(Constants.USER_ADDRESSID).toString());
            Functions.setDefaultValuesForProjectUsers(defaultForm, request);
        }
    }
}
