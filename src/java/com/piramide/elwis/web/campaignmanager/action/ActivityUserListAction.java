package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.web.common.util.MessagesUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: ActivityUserListAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ActivityUserListAction extends CampaignActivityListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ActivityUserListAction........");

        //if exist messages from import popup
        MessagesUtil.i.setAsErrorsPopupMessages(request);

        return super.execute(mapping, form, request, response);
    }
}
