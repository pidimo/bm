package com.piramide.elwis.web.campaignmanager.action;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public class CampaignRetryEmailSendForwardAction extends CampaignRetryLightEmailSendForwardAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CampaignRetryEmailSendForwardAction........" + request.getParameterMap());

        DefaultForm defaultForm = (DefaultForm) form;
        return super.execute(mapping, defaultForm, request, response);
    }


}
