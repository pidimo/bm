package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.RecipientAddCmd;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.campaignmanager.form.RecipientExcludeForm;
import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * User: yumi
 * Date: 23-ene-2006
 * Time: 16:59:06
 * To change this template use File | Settings | File Templates.
 */

public class CampaignContactImportAction extends ListAction {

    public ActionForward execute(ActionMapping action, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug(" --  CampaignContactImportAction   ------------- execute ----- ");

        RecipientExcludeForm recipientForm = (RecipientExcludeForm) form;
        ActionForward forward = action.findForward("Fail");
        ActionErrors errors = new ActionErrors();
        RecipientAddCmd cmd = new RecipientAddCmd();
        String campaignId = null;

        Collection excludes = Arrays.asList((Object[]) recipientForm.getExcludes());

        if ("true".equals(request.getParameter("cancel"))) {
            log.debug("cancel ....  CampaignContactImportAction");
            return action.findForward("Cancel");
        }

        if (request.getParameter("campaignId") != null) {
            campaignId = request.getParameter("campaignId");
        }

        if ("true".equals(request.getParameter("Import_value"))) {

            errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN, "campaignId",
                    campaignId, errors, new ActionError("customMsg.NotFound", request.getParameter("campaignName")));

            if (errors.isEmpty()) { // verified addressId not has deleted another user.
                for (Iterator iterator = excludes.iterator(); iterator.hasNext();) {
                    String o = (String) iterator.next();
                    StringTokenizer strt = new StringTokenizer(o, ",");
                    while (strt.hasMoreTokens() && errors.isEmpty()) {
                        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_ADDRESS, "addressid",
                                strt.nextToken(), errors, new ActionError("Campaign.campaignContact.ContactHasDeleted"));
                    }
                }
            }

            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return action.findForward("Fail");
            }

            if (request.getParameter("excludes") != null) {

                cmd.putParam(recipientForm.getDto());
                BusinessDelegate.i.execute(cmd, request);
                if (cmd.getResultDTO().isFailure()) {
                    saveErrors(request, errors);
                    forward = action.findForward("Fail");
                } else {
                    forward = action.findForward("Success");
                }
                addFilter("campaignId", cmd.getResultDTO().get("campaignId").toString());
            } else if (request.getParameter("excludes") == null) {
                errors.add("empty", new ActionError("Campaign.addEmpty"));
                saveErrors(request, errors);
                forward = action.findForward("Fail");
            }

            if (CampaignConstants.TRUEVALUE.equals(cmd.getResultDTO().get("already"))) {
                errors.add("already", new ActionError("Campaign.alreadyCampaignContact"));
                saveErrors(request.getSession(), errors);
            }
        }
        return forward;
    }
}
