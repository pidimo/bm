package com.piramide.elwis.web.common.action;

import com.piramide.elwis.cmd.common.ModuleEntriesLimitUtilCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.mapping.CheckEntriesMapping;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 13-sep-2006
 * Time: 15:40:04
 * To change this template use File | Settings | File Templates.
 */

public class CheckEntriesForwardAction extends ForwardAction {

    public Log log = LogFactory.getLog(CheckEntriesForwardAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("...CheckEntriesForwardAction ... execute .......");
        ActionForward forward;

        if (mapping instanceof com.piramide.elwis.web.common.mapping.CheckEntriesMapping) {
            log.debug(" ... check entries limit ... ");
            String roles[] = mapping.getRoleNames();
            String functionality = null;

            if ((roles != null) && (roles.length >= 1)) {
                String token = roles[0];
                functionality = token.substring(0, token.indexOf("."));
            }

            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            CheckEntriesMapping entriesMapping = new CheckEntriesMapping();
            entriesMapping = (CheckEntriesMapping) mapping;

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
                    if (entriesMapping.getRedirect() != null) {
                        forward = mapping.findForward(entriesMapping.getRedirect());
                    } else {
                        forward = mapping.findForward("MainSearch");
                    }
                    log.debug("... return forward ...  " + forward);
                    saveErrors(request, errors);
                    if (forward.getPath().equals("/SearchAddress.do")) {
                        return new ActionForwardParameters().add("allowCreation", request.getParameter("allowCreation"))
                                .forward(forward);
                    }
                    return forward;
                }
            }
        }

        return super.execute(mapping, form, request, response); //execute the command
    }
}
