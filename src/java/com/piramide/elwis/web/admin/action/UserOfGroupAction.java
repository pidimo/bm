package com.piramide.elwis.web.admin.action;


import com.piramide.elwis.cmd.admin.UserOfGroupDeleteCmd;
import com.piramide.elwis.web.common.action.DefaultAction;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 21, 2005
 * Time: 5:08:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserOfGroupAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("*****************  RelationUserGroupAction execution  ...");
        DefaultForm defaultForm = (DefaultForm) form;
        UserOfGroupDeleteCmd groupDeleteCmd = new UserOfGroupDeleteCmd();

        groupDeleteCmd.putParam("userId", defaultForm.getDto("userId"));
        groupDeleteCmd.putParam("userGroupId", defaultForm.getDto("userGroupId"));

        BusinessDelegate.i.execute(groupDeleteCmd, request);

        if (groupDeleteCmd.getResultDTO().isFailure()) {
            return mapping.findForward("Fail");
        }

        request.setAttribute("userGroupId", groupDeleteCmd.getResultDTO().get("userGroupId").toString());
        return mapping.findForward("Success");
    }

}
