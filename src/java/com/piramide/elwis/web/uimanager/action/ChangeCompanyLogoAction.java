package com.piramide.elwis.web.uimanager.action;

import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Alfacentauro Team
 * add data to download company logo image
 *
 * @author miky
 * @version $Id: ChangeCompanyLogoAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ChangeCompanyLogoAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ChangeCompanyLogoAction..........................");

        ActionForward forward = null;

        //company change count to update company logo
        User user = (User) request.getSession().getAttribute("user");
        String companyId = user.getValue("companyId").toString();
        Object companyStatus = request.getSession().getServletContext().getAttribute("companyLogoStatus_" + companyId);
        int count = 0;
        if (companyStatus != null) {
            count = ((Integer) companyStatus).intValue() + 1;
            request.getSession().getServletContext().setAttribute("companyLogoStatus_" + companyId, new Integer(count));
        } else {
            request.getSession().getServletContext().setAttribute("companyLogoStatus_" + companyId, new Integer(count));
        }

        //set logoId in contex application
        Map resultMap = (Map) request.getAttribute("dto");  // resultDTO of CompanyCmd
        Object objLogoId = resultMap.get("logoId");
        if (objLogoId != null) {
            request.getSession().getServletContext().setAttribute("companyLogoId_" + companyId, objLogoId);
        } else {
            request.getSession().getServletContext().removeAttribute("companyLogoId_" + companyId);
        }

        forward = new ActionForward(mapping.getParameter());
        return forward;
    }

}
