package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.common.ModuleEntriesLimitUtilCmd;
import com.piramide.elwis.cmd.webmailmanager.WebmailReadCmd;
import com.piramide.elwis.dto.webmailmanager.UserMailDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.mapping.CheckEntriesMapping;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: WebmailDefaultAction.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class WebmailDefaultAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Elwis WebmailDefaultAction executing...");

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        if (mapping instanceof com.piramide.elwis.web.common.mapping.CheckEntriesMapping) {
            log.debug(" ... check entries limit ... ");
            String roles[] = mapping.getRoleNames();
            String functionality = null;
            ActionForward forward = new ActionForward();
            if ((roles != null) && (roles.length >= 1)) {
                String token = roles[0];
                functionality = token.substring(0, token.indexOf("."));
            }
            CheckEntriesMapping entriesMapping = (CheckEntriesMapping) mapping;
            ModuleEntriesLimitUtilCmd limitUtilCmd = new ModuleEntriesLimitUtilCmd();
            limitUtilCmd.putParam("companyId", new Integer(user.getValue(Constants.COMPANYID).toString()));
            limitUtilCmd.putParam("mainTable", entriesMapping.getMainTable());
            limitUtilCmd.putParam("functionality", functionality);
            try {
                BusinessDelegate.i.execute(limitUtilCmd, request);
            } catch (AppLevelException e) {
                log.debug(" ... error execute ModuleEntriesUtilCmd ...");
            }

            if (!limitUtilCmd.getResultDTO().isFailure()) {
                if (!((Boolean) limitUtilCmd.getResultDTO().get("canCreate")).booleanValue()) {
                    log.debug(" ... can't create ...");
                    ActionErrors errors = new ActionErrors();
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Common.message.checkEntries"));
                    saveErrors(request, errors);
                    if (entriesMapping.getRedirect() != null) {
                        forward = mapping.findForward(entriesMapping.getRedirect());
                    } else {
                        forward = mapping.findForward("MainSearch");
                    }
                    return forward;
                }
            }
        }


        WebmailReadCmd webmailReadCmd = new WebmailReadCmd();
        webmailReadCmd.putParam("userId", user.getValue("userId"));
        webmailReadCmd.putParam("companyId", user.getValue("companyId"));
        ResultDTO resultDTO = BusinessDelegate.i.execute(webmailReadCmd, request);

        ArrayList uiFolderNames = (ArrayList) JSPHelper.getSystemFolderNames(request);

        DefaultForm webmailForm = (DefaultForm) form;
        webmailForm.setDto("userId", user.getValue("userId"));
        webmailForm.setDto("companyId", user.getValue("companyId"));
        webmailForm.setDto("uiFolderNames", uiFolderNames);

        if (resultDTO.get(UserMailDTO.KEY_USERMAILID) != null) {
            webmailForm.setDto(UserMailDTO.KEY_USERMAILID, resultDTO.get(UserMailDTO.KEY_USERMAILID));

            return super.execute(mapping, webmailForm, request, response);
        } else {
            request.setAttribute("hideMailConfigurationTabs", Boolean.valueOf(true));

            boolean hasExecutePermission = Functions.hasAccessRight(request, "MAIL", "EXECUTE");

            if (!hasExecutePermission) {
                String msgs[] = {JSPHelper.getMessage(request, "EXECUTE"),
                        JSPHelper.getMessage(request, "Webmail.common.mail"),
                        JSPHelper.getMessage(request, "Common.webmail")};

                ActionErrors errors = new ActionErrors();
                errors.add("especial error", new ActionError("error.invalid_access2", msgs));
                super.saveErrors(request, errors);
            }
            return mapping.findForward("NoUserMail");
        }
    }
}