package com.piramide.elwis.web.campaignmanager.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * @version $Id: ActivityContactAllImportForwardAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ActivityContactAllImportForwardAction extends ActivityManagerForwardAction {
    public Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ActivityContactAllImportForwardAction........");
        ActionForward forward = super.execute(mapping, form, request, response);
        log.debug("forward....." + forward);
        if (forward == null || forward.getName() != null) {
            log.debug(" in .......");
            //onload to close popup
            String js = "onLoad=\"opener.selectedSubmit(); window.close();\"";
            request.setAttribute("jsLoad", js);

            forward = mapping.findForward("Fail");
        }

        return forward;
    }
}
