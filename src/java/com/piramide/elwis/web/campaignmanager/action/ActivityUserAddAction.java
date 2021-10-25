package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.MessagesUtil;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Jatun S.R.L.
 * action to management add users to an activity
 *
 * @author Miky
 * @version $Id: ActivityUserAddAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ActivityUserAddAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ActivityUserAddAction........" + request.getParameterMap());
        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();
        forward = super.execute(mapping, form, request, response);

        log.debug("After of cmd execute...................." + request.getAttribute("dto"));
        Map dtoValues = (Map) request.getAttribute("dto");
        if (dtoValues.containsKey("successful")) {

            if (dtoValues.containsKey("alreadyExist")) {
                errors.add("someExist", new ActionError("Campaign.activity.user.alreadyExist"));
            }
            if (dtoValues.containsKey("userDeleted")) {
                errors.add("userDeleted", new ActionError("Campaign.activity.user.deleted"));
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
