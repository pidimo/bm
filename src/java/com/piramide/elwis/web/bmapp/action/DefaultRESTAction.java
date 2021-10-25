package com.piramide.elwis.web.bmapp.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public class DefaultRESTAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Rest action execution... method:" + request.getMethod() + " mappingParameter:" + mapping.getParameter());

        if (request.getMethod().equals("GET")) {
            if (request.getParameterMap().containsKey(mapping.getParameter())) {
                return mapping.findForward("Get");
            } else {
                return mapping.findForward("List");
            }
        } else if (request.getMethod().equals("POST")) {
            return mapping.findForward("Create");
        } else if (request.getMethod().equals("PUT")) {
            return mapping.findForward("Update");
        } else if (request.getMethod().equals("DELETE")) {
            return mapping.findForward("Delete");
        }
        return null;

    }
}
