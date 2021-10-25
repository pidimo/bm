package com.piramide.elwis.web.uimanager.action;

import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Alfacentauro Team
 * help to control the changes in the style sheet of an user or company
 *
 * @author miky
 * @version $Id: ChangeStyleSheetAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ChangeStyleSheetAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ChangeStyleSheetAction..........................");

        ActionForward forward = null;

        User user = (User) request.getSession().getAttribute("user");
        String companyId = user.getValue("companyId").toString();

        if (request.getParameter("isCompany") != null) {
            //company change count to update style sheet
            request.getSession().getServletContext().setAttribute("companyStyleStatus_" + companyId, new Long(System.currentTimeMillis()));
        } else {
            //user change count to update style sheet
            request.getSession().setAttribute("userStyleStatus", new Long(System.currentTimeMillis()));
        }

        forward = new ActionForward(mapping.getParameter());
        return forward;
    }
}
