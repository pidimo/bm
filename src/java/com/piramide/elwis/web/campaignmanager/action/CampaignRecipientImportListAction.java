package com.piramide.elwis.web.campaignmanager.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 * action to show import activity contacts, close popup if error occur in onLoad
 *
 * @author Miky
 * @version $Id: CampaignRecipientImportListAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignRecipientImportListAction extends CampaignActivityListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CampaignRecipientImportListAction........");

        ActionForward forward = super.execute(mapping, form, request, response);

        if (forward == null || !"Success".equals(forward.getName())) {

            //onload to close popup
            String js = "onLoad=\"opener.selectedSubmit(); window.close();\"";
            request.setAttribute("jsLoad", js);

            forward = mapping.findForward("Success");
        }

        return forward;
    }
}
