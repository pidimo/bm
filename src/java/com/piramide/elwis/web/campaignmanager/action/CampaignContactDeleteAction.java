package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.RecipientExcludeCmd;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.campaignmanager.form.RecipientExcludeForm;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Nov 12, 2004
 * Time: 5:27:25 PM
 * To change this template use File | Settings | File Templates.
 */

public class CampaignContactDeleteAction extends com.piramide.elwis.web.common.action.ListAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping action, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        RecipientExcludeCmd cmd = new RecipientExcludeCmd();
        RecipientExcludeForm excludeForm = (RecipientExcludeForm) form;
        ActionErrors errors = new ActionErrors();
        String campaignId = null;

        ActionForward forward = action.findForward("Fail");

        log.debug("... campaignContactDeleteAction execute ...");
        log.debug("CampaignId for user session  = " + request.getParameter("campaignId"));

        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN, "campaignid",
                request.getParameter("campaignId"), errors, new ActionError("error.CampaignSession.NotFound"));

        if (!errors.isEmpty()) {
            log.debug("..... campaign not found was deleted by other user............");
            saveErrors(request.getSession(), errors);
            return action.findForward("Fail_");
        }

        if ("true".equals(request.getParameter("Delete")) || request.getParameter("dto(campaignContactId)") != null) {
            if (request.getParameter("excludes") != null || request.getParameter("dto(campaignContactId)") != null) {
                cmd.putParam(excludeForm.getDto());
                BusinessDelegate.i.execute(cmd, request);
                forward = super.execute(action, excludeForm, request, response);
            } else if (request.getParameter("excludes") == null) {
                errors = new ActionErrors();
                errors.add("empty", new ActionError("Campaign.DeleteContact"));
                saveErrors(request, errors);
                forward = action.findForward("Fail");
            }
        }
        return forward;
    }
}
