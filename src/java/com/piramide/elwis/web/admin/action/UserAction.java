package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.cmd.admin.UserAdminCmd;
import com.piramide.elwis.web.admin.form.UserInfoForm;
import com.piramide.elwis.web.common.util.MessagesUtil;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 9, 2005
 * Time: 2:55:11 PM
 * To change this template use File | Settings | File Templates.
 */

public class UserAction extends com.piramide.elwis.web.common.action.DefaultAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping action, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("  .....  userAction   execute  .........");

        if (isCancelled(request)) {
            log.debug("Is Cancel");
            return (action.findForward("Cancel"));
        }

        UserInfoForm userInfoForm = (UserInfoForm) form;
        ActionErrors errors = new ActionErrors();
        UserAdminCmd userAdminCmd = new UserAdminCmd();
        userAdminCmd.putParam(userInfoForm.getDtoMap());
        BusinessDelegate.i.execute(userAdminCmd, request);
        errors = MessagesUtil.i.convertToActionErrors(action, request, userAdminCmd.getResultDTO());
        saveErrors(request, errors);

        if (userAdminCmd.getResultDTO().isFailure()) {
            request.setAttribute("jsLoad", "onLoad=\"userTypeChange()\"");
            return action.findForward("Fail");
        } else {
            return action.findForward("Success");
        }
    }
}
