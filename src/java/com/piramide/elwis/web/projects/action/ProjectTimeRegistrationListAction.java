package com.piramide.elwis.web.projects.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class ProjectTimeRegistrationListAction extends ProjectTimeListAction {
    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        return null;
    }

    @Override
    protected void addFilers(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        addFilter("userId", user.getValue(Constants.USERID).toString());
        addFilter("assigneeId", user.getValue(Constants.USER_ADDRESSID).toString());
        addFilter("new", "true");
    }
}
