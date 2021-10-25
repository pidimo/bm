package com.piramide.elwis.web.uimanager.action;

import com.piramide.elwis.cmd.common.ModuleEntriesLimitUtilCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.mapping.CheckEntriesMapping;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 05-oct-2006
 * Time: 9:15:40
 * To change this template use File | Settings | File Templates.
 */
public class StyleSheetAction extends com.piramide.elwis.web.common.action.DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("... StyleSheetAction executing ...");
        if ("create".equals(request.getParameter("dto(op)")) && !"".equals(request.getParameter("dto(save)")) && request.getParameter("dto(save)") != null) {
            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

            CheckEntriesMapping entriesMapping = (CheckEntriesMapping) mapping;
            ModuleEntriesLimitUtilCmd limitUtilCmd = new ModuleEntriesLimitUtilCmd();
            limitUtilCmd.putParam("companyId", new Integer(user.getValue(Constants.COMPANYID).toString()));
            limitUtilCmd.putParam("mainTable", entriesMapping.getMainTable());
            limitUtilCmd.putParam("functionality", "USERINTERFACE");
            try {
                BusinessDelegate.i.execute(limitUtilCmd, request);
            } catch (AppLevelException e) {
                log.debug(" ... error execute ModuleEntriesUtilCmd ...");
            }
            if (!limitUtilCmd.getResultDTO().isFailure()) {
                if (!((Boolean) limitUtilCmd.getResultDTO().get("canCreate")).booleanValue()) {
                    log.debug(" ... can't create ...");
                    ActionErrors errors = new ActionErrors();
                    Map map = new HashMap();
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Common.message.checkEntries"));
                    saveErrors(request.getSession(), errors);
                    return mapping.findForward("Success");
                }
            }
        }
        return super.execute(mapping, form, request, response);
    }
}
