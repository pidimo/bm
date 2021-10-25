package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class WebDocumentAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing WebDocumentAction........" + request.getParameterMap());

        DefaultForm defaultForm = (DefaultForm) form;
        Integer webDocumentId = new Integer(defaultForm.getDto("webDocumentId").toString());

        ActionForward forward = super.execute(mapping, form, request, response);

        return forward;
    }
}
