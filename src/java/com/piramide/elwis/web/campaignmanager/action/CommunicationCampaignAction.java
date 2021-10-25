package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignCommunicationCmd;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.contactmanager.action.MainCommunicationAction;
import com.piramide.elwis.web.contactmanager.form.MainCommunicationForm;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CommunicationCampaignAction extends MainCommunicationAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        User user = RequestUtils.getUser(request);
        ((DefaultForm) form).setDto("currentUserId", user.getValue(Constants.USERID).toString());
        ((DefaultForm) form).setDto("companyId", user.getValue(Constants.COMPANYID).toString());
        ((DefaultForm) form).setDto("campaignId", request.getParameter("campaignId"));

        //add miky: campaign activity recurrency validation
        MainCommunicationForm communicationForm = (MainCommunicationForm) form;

        if ("create".equals(communicationForm.getDto("op")) ||
                "update".equals(communicationForm.getDto("op"))) {
            if (communicationForm.getDto("activityId") != null
                    && !GenericValidator.isBlankOrNull(communicationForm.getDto("activityId").toString())) {
                ActionErrors errors = new ActionErrors();
                errors = ForeignkeyValidator.i.validate(
                        CampaignConstants.TABLE_CAMPAIGNACTIVITY, "activityid", communicationForm.getDto("activityId"),
                        errors, new ActionError("Campaign.communication.activity.notFound")
                );
                if (!errors.isEmpty()) {
                    saveErrors(request, errors);
                    return new ActionForward(mapping.getInput());
                }
            }
        }

        return super.execute(mapping, form, request, response);
    }

    protected EJBCommand getGenerateDocumentCmd() {
        return new CampaignCommunicationCmd();
    }

    protected ActionForward checkMainReferences(HttpServletRequest request, ActionMapping mapping) {
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("campaignId") != null) {
            errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN, "campaignid",
                    request.getParameter("campaignId"), errors, new ActionError("error.CampaignSession.NotFound"));

            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("MainSearch");
            }
        } else {
            errors.add("general", new ActionError("error.CampaignSession.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        return null;
    }
}
