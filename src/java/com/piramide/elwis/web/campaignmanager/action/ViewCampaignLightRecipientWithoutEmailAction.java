package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Jatun S.R.L.
 * Action to revise emails for campaign light generation contacts
 *
 * @author Miky
 * @version $Id: ViewCampaignLightRecipientWithoutEmailAction.java 04-may-2009 18:22:57 $
 */
public class ViewCampaignLightRecipientWithoutEmailAction extends CampaignManagerAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Executing ViewCampaignLightRecipientWithoutEmailAction...." + request.getParameterMap());
        ActionErrors errors = new ActionErrors();

        //validate telecom type
        String telecomTypeId = request.getParameter("telecomTypeId");
        if (GenericValidator.isBlankOrNull(telecomTypeId)) {
            errors.add("telTypeId", new ActionError("errors.required", JSPHelper.getMessage(request, "TelecomType.type.mail")));
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        DefaultForm defaultForm = (DefaultForm) form;
        defaultForm.setDto("op", "campaignRecipientsWithoutEmail");
        defaultForm.setDto("campaignId", request.getParameter("campaignId"));
        defaultForm.setDto("telecomTypeId", telecomTypeId);

        ActionForward forward = super.execute(mapping, defaultForm, request, response);

        log.debug("After of cmd execute...................." + request.getAttribute("dto"));
        Map dtoValues = (Map) request.getAttribute("dto");
        if ("Success".equals(forward.getName())) {
            request.setAttribute("withoutEmailList", dtoValues.get("withoutEmailList"));
            return forward;
        }

        return mapping.findForward("Fail");
    }
}
