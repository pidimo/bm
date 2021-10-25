package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.PermissionUtil;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: ivan
 * Date: Apr 25, 2006
 * Time: 5:06:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoleCreateAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)");

        String operation = (String) ((DefaultForm) form).getDto("op");


        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        ActionForward actualForward = mapping.findForward("ToUpdate");

        Byte accessRightValue = (Byte) user.getSecurityAccessRights().get("ACCESSRIGHT");
        String indexTab = "0";
        if (null != accessRightValue && PermissionUtil.hasAccessRight(accessRightValue, PermissionUtil.UPDATE)) {
            actualForward = mapping.findForward("ToAccessRight");
            indexTab = "1";
        }

        ActionForward forward = super.execute(mapping, form, request, response);

        DefaultForm roleForm = (DefaultForm) form;

        if (null != roleForm.getDto("roleId") && "create".equals(operation)) {

            ActionForward newForward = new ActionForwardParameters().
                    add("roleId", roleForm.getDto("roleId").toString()).
                    add("roleName", roleForm.getDto("roleName").toString()).
                    add("dto(op)", "read").
                    add("index", indexTab).
                    add("dto(roleId)", roleForm.getDto("roleId").toString()).forward(actualForward);
            return newForward;
        }

        return forward;
    }
}
