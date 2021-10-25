package com.piramide.elwis.web.projects.action;

import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public class ProjectListAction extends ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ProjectListAction.....");

        User user = RequestUtils.getUser(request);

        addFilter("userAddressId", user.getValue("userAddressId").toString());

        return super.execute(mapping, form, request, response);
    }
}
