package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.MessagesUtil;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Jatun S.R.L.
 * action to management add campaign contacts to an activity
 *
 * @author Miky
 * @version $Id: ActivityCampContactAddAction.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class ActivityCampContactAddAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ActivityCampContactAddAction........" + request.getParameterMap());
        if (isCancelled(request)) {
            log.debug("action was canceled");
            return (mapping.findForward("Cancel"));
        }

        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();
        forward = super.execute(mapping, form, request, response);

        log.debug("After of cmd execute...................." + request.getAttribute("dto"));
        Map dtoValues = (Map) request.getAttribute("dto");
        if (dtoValues.containsKey("successful")) {

            if (dtoValues.containsKey("someExist")) {
                errors.add("someExist", new ActionError("Campaign.activity.campContact.someAlreadyExist"));
            }
            if (dtoValues.containsKey("someDeleted")) {
                errors.add("someExist", new ActionError("Campaign.activity.campContact.someDeleted"));
            }
            if (dtoValues.containsKey("copyActivityDeleted")) {
                errors.add("someExist", new ActionError("Campaign.activity.campContact.copyActivityDeleted"));
            }
            if (dtoValues.containsKey("emptyRecipient")) {
                errors.add("empty", new ActionError("Campaign.activity.campContact.campaignEmptyRecipient"));
            }
            if (!errors.isEmpty()) {
                MessagesUtil.i.savePopupMessages(request.getSession(), errors);
            }
        }

        //onload to close popup
        String js = "onLoad=\"goToParent();\"";
        request.setAttribute("jsLoad", js);

        return forward;
    }

}
