package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.web.common.util.MessagesUtil;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Jatun S.R.L.
 * Action to manage activity user automatic assign
 *
 * @author Miky
 * @version $Id: ActivityUserAutomaticAssignAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ActivityUserAutomaticAssignAction extends CampaignActivityManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ActivityUserAutomaticAssignAction............." + request.getParameterMap());

        //if exist messages from user import popup
        MessagesUtil.i.setAsErrorsPopupMessages(request);

        ActionErrors errors = new ActionErrors();
        if (isCancelled(request)) {
            log.debug("action was canceled");
            return (mapping.findForward("Cancel"));
        }

        if (request.getParameter("assignButton") != null) {
            return super.execute(mapping, form, request, response);
        }
        return mapping.getInputForward();
    }

}
