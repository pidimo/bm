package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ReadSubCategoriesAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String formName = request.getParameter("formName");
        if (null != formName && !"".equals(formName.trim())) {
            request.setAttribute("formName", formName);
        }
        ActionForward forward = mapping.findForward("Success");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "max-age=0");
        return forward;
    }
}
