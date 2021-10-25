package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.cmd.common.ModuleEntriesLimitUtilCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.mapping.CheckEntriesMapping;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 30, 2005
 * Time: 4:21:11 PM
 * To change this template use File | Settings | File Templates.
 */

public class ArticleCreateAction extends DefaultAction {

    public ActionForward execute(ActionMapping map, ActionForm actionForm, HttpServletRequest req, HttpServletResponse res) throws Exception {

        if (isCancelled(req)) {
            log.debug("Is Cancel");
            return (map.findForward("Cancel"));
        }

        DefaultForm articleForm = (DefaultForm) actionForm;

        ActionForward forward = map.findForward("Success");
        User user = (User) req.getSession().getAttribute(Constants.USER_KEY);
        DateTimeZone timeZone;


        if (map instanceof com.piramide.elwis.web.common.mapping.CheckEntriesMapping) {
            log.debug(" ... check entries limit ... ");
            CheckEntriesMapping entriesMapping = (CheckEntriesMapping) map;
            ModuleEntriesLimitUtilCmd limitUtilCmd = new ModuleEntriesLimitUtilCmd();
            limitUtilCmd.putParam("companyId", new Integer(user.getValue(Constants.COMPANYID).toString()));
            limitUtilCmd.putParam("mainTable", entriesMapping.getMainTable());
            limitUtilCmd.putParam("functionality", "ARTICLE");
            try {
                BusinessDelegate.i.execute(limitUtilCmd, req);
            } catch (AppLevelException e) {
                log.debug(" ... error execute ModuleEntriesUtilCmd ...");
            }
            if (!limitUtilCmd.getResultDTO().isFailure()) {
                if (!((Boolean) limitUtilCmd.getResultDTO().get("canCreate")).booleanValue()) {
                    log.debug(" ... can't create ...");
                    ActionErrors errors = new ActionErrors();
                    ActionForward redirect = new ActionForward();
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Common.message.checkEntries"));
                    saveErrors(req.getSession(), errors);
                    if (entriesMapping.getRedirect() != null) {
                        redirect = map.findForward("redirect");
                    } else {
                        redirect = map.findForward("Error");
                    }
                    return redirect;
                }
            }
        }

        if (user != null) {
            timeZone = (DateTimeZone) user.getValue("dateTimeZone");
        } else {
            timeZone = DateTimeZone.getDefault();
        }

        req.setAttribute("timeZone", timeZone);
        String ope = (String) articleForm.getDto("op");
        articleForm.setDto("userId", user.getValue("userId"));
        ActionErrors errors = actionForm.validate(map, req);

        if (!errors.isEmpty()) {
            saveErrors(req, errors);
            return map.findForward("Fail");
        }

        if (articleForm.getDto("articleId") == null && "".equals(articleForm.getDto("articleId"))) {
            articleForm.setDto("articleId", articleForm.getDto("articleId"));
        } else {
            articleForm.setDto("articleId", req.getParameter("articleId"));
        }

        forward = super.execute(map, articleForm, req, res);

        if (articleForm.getDto("articleId") != null && "create".equals(ope)) {
            return new ActionForwardParameters().add("articleId", ((DefaultForm) actionForm).getDto("articleId").toString()).forward(forward);
        }
        return forward;
    }
}
