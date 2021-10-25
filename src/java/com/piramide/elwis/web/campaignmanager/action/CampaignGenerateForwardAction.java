package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.ReadEnabledActivityContactsCmd;
import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.webmail.el.Functions;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Jatun S.R.L.
 * set default values in form for campaign generation
 *
 * @author Miky
 * @version $Id: CampaignGenerateForwardAction.java 10531 2015-03-27 21:02:35Z miguel $
 */
public class CampaignGenerateForwardAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Executing CampaignGenerateForwardAction...." + request.getParameterMap());
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

        DefaultForm generateForm = (DefaultForm) form;

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (mapping.findForward("Fail"));
        }

        ReadEnabledActivityContactsCmd cmd = new ReadEnabledActivityContactsCmd();
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

        //set telecom type as selected
        List telecomTypeList = Functions.getTelecomTypesOfEmails(request);
        if (telecomTypeList.size() > 0) {
            LabelValueBean labelValueBean = (LabelValueBean) telecomTypeList.get(0);
            generateForm.setDto("telecomTypeId", labelValueBean.getValue());
        }

        //set sender email selected
        generateForm.setDto("employeeMail", CampaignConstants.DEFAULT_ADDRESS_OF_CONTACTRESPONSIBLE);

        //set default notification email
        User user = RequestUtils.getUser(request);
        Integer userAddressId = new Integer(user.getValue("userAddressId").toString());
        TelecomDTO telecomDTO = com.piramide.elwis.web.contactmanager.el.Functions.findDefaultEmailTelecom(userAddressId, null);
        if (telecomDTO != null) {
            generateForm.setDto("notificationMail", telecomDTO.getData());
        }

        log.debug("FORM:::::" + generateForm.getDtoMap());

        return mapping.findForward("Success");
    }
}
