package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.InitializeCampaignDocumentCmd;
import com.piramide.elwis.cmd.campaignmanager.LightlyInternalUserCmd;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Mar 30, 2005
 * Time: 11:36:02 AM
 * To change this template use File | Settings | File Templates.
 */

public class CampaignGenerateAction extends CampaignManagerAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("*** enter to campaignGenerate *** Action");
        /*GenerateForm generateForm = (GenerateForm) form;*/
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN, "campaignid",
                request.getParameter("campaignId"), errors, new ActionError("error.CampaignSession.NotFound"));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        //activity concurrency validation
        if (null != request.getParameter("activityId")) {
            errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGNACTIVITY, "activityid",
                    request.getParameter("activityId"), errors, new ActionError("Campaign.activity.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request.getSession(), errors);
                return mapping.findForward("Fail_");
            }
        } else {
            errors.add("activityid", new ActionError("Campaign.activity.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("Fail_");
        }

        //campaign template validation
        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN_TEMPLATE, "templateid",
                request.getParameter("templateId"), errors, new ActionError("Campaign.template.NotFound"));

        DefaultForm generateForm = (DefaultForm) form;
        //errors = generateForm.validate(mapping, request);

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (mapping.findForward("Fail"));
        }

        InitializeCampaignDocumentCmd cmd = new InitializeCampaignDocumentCmd();
        cmd.putParam(generateForm.getDtoMap());
        cmd.putParam("templateId", request.getParameter("templateId"));
        cmd.putParam("activityId", request.getParameter("activityId"));
        BusinessDelegate.i.execute(cmd, request);

        if (cmd.getResultDTO().isFailure()) {
            ActionErrors actionErrors = MessagesUtil.i.convertToActionErrors(mapping, request, cmd.getResultDTO());
            saveErrors(request, actionErrors);
            if (cmd.getResultDTO().get("RecipientError") != null) {
                return mapping.findForward("FailRecipient");
            }
            return (mapping.findForward("Fail"));
        }

        request.setAttribute("documentByLanguage", cmd.getResultDTO().get("documentByLanguage"));

        if (generateForm.getDto("company") == null) //set as default if it does not defined.
        {
            generateForm.setDto("company", Boolean.TRUE);
        }

        //current user is internal user
        User user = (User) request.getSession().getAttribute("user");

        LightlyInternalUserCmd internalUserCmd = new LightlyInternalUserCmd();
        internalUserCmd.putParam("userId", user.getValue("userId"));
        internalUserCmd.putParam("op", "read");
        BusinessDelegate.i.execute(internalUserCmd, request);

        boolean isInternal = internalUserCmd.getResultDTO().getAsBool("isInternalUser");
        generateForm.setDto("currentIsInternal", isInternal ? "true" : "false");

        //get activity responsible address id
        internalUserCmd = new LightlyInternalUserCmd();
        internalUserCmd.putParam("userId", generateForm.getDto("activityUserId"));
        internalUserCmd.putParam("op", "read");
        BusinessDelegate.i.execute(internalUserCmd, request);

        generateForm.setDto("activityAddressId", internalUserCmd.getResultDTO().get("addressId"));

        log.debug("FORM:::::" + generateForm.getDtoMap());

        return mapping.findForward("Success");
    }
}
