package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignRetryLightEmailSendCmd;
import com.piramide.elwis.service.campaign.CampaignMailerService;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.campaignmanager.delegate.CampaignMailerServiceDelegate;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.dto.ParamDTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public class CampaignRetryLightEmailSendAction extends CampaignSentLogAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CampaignRetryLightEmailSendAction...." + request.getParameterMap());

        CampaignMailerService mailerService = CampaignMailerServiceDelegate.i.getCampaignMailerService();


        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        ActionErrors errors = new ActionErrors();
        //validate if current user is internal
        if (!isInternalUser(request)) {
            errors.add("userError", new ActionError("Campaign.generate.error.internalUser"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        DefaultForm defaultForm = (DefaultForm) form;
        defaultForm.setDto("requestLocale", request.getLocale().toString());

        ActionForward forward;

        forward = validateCampaignSentLogExistence(request, mapping);
        if (forward != null) {
            return forward; //campaign sent log has been removed
        }

        forward = validateCampaignExistence(request, mapping);
        if (forward != null) {
            return forward; //campaign has been removed
        }
        setDtoAttributes(request, defaultForm);


        ParamDTO params = new ParamDTO();
        params.putAll(defaultForm.getDtoMap());
        ResultDTO resultDTO = mailerService.sendMassiveMails(params, CampaignRetryLightEmailSendCmd.class);

        if (resultDTO == null) {
            errors = new ActionErrors();
            errors.add("unexpectedError", new ActionError("Campaign.mailing.unexpectedError"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("Cancel");
        }

        errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
        saveErrors(request, errors);

        if (resultDTO.isFailure()) {
            return mapping.getInputForward();
        }

        forward = mapping.findForward(resultDTO.getForward());

        Map dtoValues = resultDTO;
        if ("Success".equals(forward.getName())) {

            if (isSendInBackground(defaultForm)) {
                campaignIsSendInBackgroundSummary(defaultForm, request);
            } else {
                summaryOfSendCampaign(dtoValues, request);
            }
        }
        return forward;


    }

    private boolean isInternalUser(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        return com.piramide.elwis.web.admin.el.Functions.isInternalUser(user.getValue("userId"));
    }

    private boolean isSendInBackground(DefaultForm defaultForm) {
        return "true".equals(defaultForm.getDto("sendInBackground"));
    }

    private void summaryOfSendCampaign(Map dtoValues, HttpServletRequest request) {
        List summaryList = new ArrayList();

        if (dtoValues.containsKey("generationResume")) {
            Map generationResumeMap = (Map) dtoValues.get("generationResume");
            for (Iterator iterator = generationResumeMap.keySet().iterator(); iterator.hasNext();) {
                String language = (String) iterator.next();
                summaryList.add(new LabelValueBean(language, generationResumeMap.get(language).toString()));
            }
        }
        request.setAttribute("summaryList", summaryList);

        request.setAttribute("totalFail", dtoValues.get("totalFail"));
        request.setAttribute("totalSuccess", dtoValues.get("totalSuccess"));
        request.setAttribute("totalRecipients", dtoValues.get("totalRecipients"));
        request.setAttribute("withoutMailList", dtoValues.get("withoutMailList"));
        request.setAttribute("failSentList", dtoValues.get("failSentList"));
        request.setAttribute("templateId", dtoValues.get("templateId"));
    }

    private void campaignIsSendInBackgroundSummary(DefaultForm defaultForm, HttpServletRequest request) {
        request.setAttribute("sendInBackgroundSummary", "true");
        request.setAttribute("notificationMail", defaultForm.getDto("notificationMail"));
    }

}
