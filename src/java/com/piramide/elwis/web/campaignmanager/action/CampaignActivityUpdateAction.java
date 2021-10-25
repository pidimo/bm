package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.web.common.util.MessagesUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Jatun S.R.L.
 * action to register error messages from contact register popups
 *
 * @author Miky
 * @version $Id: CampaignActivityUpdateAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignActivityUpdateAction extends CampaignManagerAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("CampaignActivityUpdateAction execution  ...");

        //register errors from popups if exist
        MessagesUtil.i.setAsErrorsPopupMessages(request);

        return super.execute(mapping, form, request, response);
    }
}