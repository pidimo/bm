package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.contactmanager.form.UploadForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Yumi
 * @version $Id: TemplateAction.java 1181 2004-06-16 22:42:26Z ivan $
 */

public class TemplateAction extends DefaultAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        if (isCancelled(request)) {
            return (mapping.findForward("Cancel"));
        } else {
            UploadForm uploadForm = (UploadForm) form;
            //log.debug("File Content Type:" + uploadForm.getFile().getContentType());
            uploadForm.getDtoMap().put("file", uploadForm.getFile());
            ActionForward forward = super.execute(mapping, form, request, response);
            if (request.getParameter("SaveAndNew") != null) {
                forward = mapping.findForward("SaveAndNew");
            }
            return forward;
        }
    }
}
