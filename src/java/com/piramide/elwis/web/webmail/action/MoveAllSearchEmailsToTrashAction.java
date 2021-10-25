package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class MoveAllSearchEmailsToTrashAction extends WebmailDefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm defaultForm = (DefaultForm) form;

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userMailId = new Integer(user.getValue("userId").toString());

        Object[] mailIds = request.getParameterValues("dto(mailIds)");
        defaultForm.setDto("mailIds", mailIds);
        defaultForm.setDto("userMailId", userMailId);
        defaultForm.setDto("companyId", user.getValue("companyId"));

        ActionForward forward = super.execute(mapping, defaultForm, request, response);

        return new ActionForwardParameters()
                .add("searchText", request.getParameter("searchText"))
                .add("searchFilter", request.getParameter("searchFilter"))
                .add("searchFolder", request.getParameter("searchFolder"))
                .add("returning", "true").forward(forward);
    }
}
