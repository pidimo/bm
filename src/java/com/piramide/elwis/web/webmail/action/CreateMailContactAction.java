package com.piramide.elwis.web.webmail.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Alvaro Sejas
 */
public class CreateMailContactAction extends WebmailDefaultAction {
    Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("CreateMailContactAction executing.....");

        ActionForward actionForward = super.execute(mapping, form, request, response);
        log.info("BLACK☆STAR.... AQUI TOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY " + actionForward);
        if (actionForward.getName().equals("Success")) {
            actionForward = mapping.findForward("SuccessToMailTray");
            if (WebMailNavigationUtil.isAvancedSearch(request)) {
                actionForward = mapping.findForward("SuccessToMailAdvancedSearch");
            } else if (WebMailNavigationUtil.isSearch(request)) {
                actionForward = mapping.findForward("SuccessToMailSearch");
            }
        }
        log.info("BLACK☆STAR.... SUCCESS name: " + actionForward.getName());
        return (actionForward);
    }
}
