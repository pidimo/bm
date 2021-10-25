package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 * if campaign activity was deleted, go to Fail_ forward
 *
 * @author Miky
 * @version $Id: CampaignActivityListAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignActivityListAction extends CampaignListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CampaignActivityListAction........");

        //cheking if was not deleted by other user.
        log.debug("activityId = " + request.getParameter("activityId"));
        ActionErrors errors = new ActionErrors();
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

        addFilter("activityId", request.getParameter("activityId"));

        return super.execute(mapping, form, request, response);
    }
}
