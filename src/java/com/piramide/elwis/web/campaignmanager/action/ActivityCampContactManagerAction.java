package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: ActivityCampContactManagerAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ActivityCampContactManagerAction extends CampaignActivityManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ActivityCampContactManagerAction............." + request.getParameterMap());

        if (isCancelled(request)) {
            log.debug("action was canceled");
            return (mapping.findForward("Cancel"));
        }

        ActionErrors errors = new ActionErrors();

        if (null != request.getParameter("dto(campaignContactId)")) {
            errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGNCONTACT, "campcontactid",
                    request.getParameter("dto(campaignContactId)"), errors, new ActionError("Campaign.campaignContact.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("FailC_");
            }

        } else {
            errors.add("campcontactid", new ActionError("Campaign.campaignContact.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("FailC_");
        }

        return super.execute(mapping, form, request, response);
    }
}
