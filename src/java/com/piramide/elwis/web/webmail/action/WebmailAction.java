package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.common.ModuleEntriesLimitUtilCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.mapping.CheckEntriesMapping;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: WebmailAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class WebmailAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Elwis Webmail DefaultAction executing...");

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        if (mapping instanceof com.piramide.elwis.web.common.mapping.CheckEntriesMapping &&
                !"".equals(request.getParameter("dto(save)")) && request.getParameter("dto(save)") != null) {
            log.debug(" ... check entries limit ... ");
            CheckEntriesMapping entriesMapping = (CheckEntriesMapping) mapping;
            ModuleEntriesLimitUtilCmd limitUtilCmd = new ModuleEntriesLimitUtilCmd();
            limitUtilCmd.putParam("companyId", new Integer(user.getValue(Constants.COMPANYID).toString()));
            limitUtilCmd.putParam("mainTable", entriesMapping.getMainTable());
            limitUtilCmd.putParam("functionality", "MAIL");
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
                    saveErrors(request.getSession(), errors);
                    return mapping.findForward("redirect");
                }
            }
        }

        return super.execute(mapping, form, request, response);
    }
}
