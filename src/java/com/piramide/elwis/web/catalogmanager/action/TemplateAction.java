package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: TemplateAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class TemplateAction extends DefaultAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        //UploadForm uploadForm = (UploadForm) form;
        if (isCancelled(request)) {
            return (mapping.findForward("Cancel"));
        } else {

            //log.debug("File Content Type:" + uploadForm.getFile().getContentType());
            //uploadForm.getDtoMap().put("file", uploadForm.getFile());
            ActionForward forward = super.execute(mapping, form, request, response);
            if (request.getParameter("dto(SaveAndNew)") != null) {
                /*TemplateForm uploadForm = new TemplateForm();
                uploadForm.setDto("operation", "create");
                request.setAttribute("templateForm",uploadForm);*/
                forward = mapping.findForward("SaveAndNew");
            }
            return forward;
        }
    }
}
