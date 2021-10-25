package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 * @version : $Id RoleListAction ${time}
 */
public class RoleListAction extends ListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();

        Integer roleId = null;
        try {
            roleId = Integer.valueOf(request.getParameter("roleId"));

            ForeignkeyValidator.i.validate(AdminConstants.TABLE_ROLE, "roleId",
                    roleId, errors, new ActionError("customMsg.NotFound", request.getParameter("roleName")));

        } catch (NumberFormatException e) {
            errors.add("rolesList", new ActionError("customMsg.NotFound", request.getParameter("roleName")));
        }

        if (!errors.isEmpty()) {
            saveErrors(request.getSession(), errors);
            return mapping.findForward("Fail");
        }

        addFilter("roleId", roleId.toString());
        ActionForward forward = super.execute(mapping, form, request, response);

        log.debug("en el RoleList...................................." + forward);
        return forward;

    }
}
