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
 * action to manage show email send recipient without email
 *
 * @author Miky
 * @version $Id: ViewRecipientWithoutEmailAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ViewRecipientWithoutEmailAction extends CampaignActivityManagerAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Executing ViewRecipientWithoutEmailAction...." + request.getParameterMap());
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
        defaultForm.setDto("telecomTypeId", telecomTypeId);
        defaultForm.setDto("activityId", request.getParameter("activityId"));

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
