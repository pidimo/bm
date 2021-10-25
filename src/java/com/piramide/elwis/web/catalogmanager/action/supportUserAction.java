package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.cmd.admin.UserCmd;
import com.piramide.elwis.cmd.supportmanager.SupportUserCmd;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.MessagesUtil;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 19-ene-2007
 * Time: 13:38:58
 * To change this template use File | Settings | File Templates.
 */

public class supportUserAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("--------  SupportUserAction in Catalogs executeFunction  --------");

        DefaultForm supportForm = (DefaultForm) form;

        ActionErrors errors = new ActionErrors();
        ActionForward forward = mapping.findForward("Fail");
        ActionErrors aErrors = new ActionErrors();
        UserCmd userCmd = new UserCmd();
        SupportUserCmd supportUserCmd = new SupportUserCmd();
        String email = "";

        if (isCancelled(request)) {
            log.debug("action was canceled");
            return (mapping.findForward("Cancel"));
        }

        if (supportForm.getDto("save") == null) {
            supportForm.setDto("save_", "false");

            if ("true".equals(supportForm.getDto("reload"))) {
                supportForm.setDto("userId", null);
                supportForm.setDto("emailNotification", null);
                supportForm.setDto("userName", null);
                supportForm.setDto("userId_selected", null);
                supportForm.setDto("reload", null);
                return mapping.findForward("Reload");
            } else {
                if (!"update".equals(supportForm.getDto("op"))) {
                    userCmd.putParam("userId", supportForm.getDto("userId"));
                } else {
                    userCmd.putParam("userId", supportForm.getDto("userId_selected"));
                    supportForm.setDto("userId", supportForm.getDto("userId_selected"));
                }
                userCmd.putParam("companyId", supportForm.getDto("companyId"));
                userCmd.putParam("op", "");
                BusinessDelegate.i.execute(userCmd, request);
                if (!userCmd.getResultDTO().isFailure()) {
                    email = (String) userCmd.getResultDTO().get("notificationSupportCaseEmail");
                }
                supportForm.setDto("emailNotification", email);
                return mapping.findForward("Reload");
            }
        } else {
            errors = supportForm.validate(mapping, request);
            saveErrors(request, errors);
            if (errors.isEmpty()) {
                if ((!"".equals(supportForm.getDto("emailNotification")) && supportForm.getDto("emailNotification") != null) ||
                        "true".equals(supportForm.getDto("save_"))) {
                    /*if(!"".equals(supportForm.getDto("userId_selected"))){
                    */
                    supportUserCmd.putParam(supportForm.getDtoMap());
                    BusinessDelegate.i.execute(supportUserCmd, request);
                    errors = MessagesUtil.i.convertToActionErrors(mapping, request, supportUserCmd.getResultDTO());
                    saveErrors(request, errors);
                    /*}else{
                        return mapping.findForward("Success");}*/
                    if (supportUserCmd.getResultDTO().isFailure()) {
                        supportForm.setDto("save_", null);
                        if ("true".equals(supportUserCmd.getResultDTO().get("duplicate"))) {
                            return mapping.findForward("Reload");
                        } else {
                            return mapping.findForward("Fail");
                        }
                    } else {
                        return mapping.findForward("Success");
                    }
                } else {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Support.user.haventEmailNotification"));
                    saveErrors(request, errors);
                    supportForm.setDto("save_", "true");
                    return mapping.findForward("Reload");
                }
            } else {
                return mapping.findForward("Reload");
            }
        }
    }
}
