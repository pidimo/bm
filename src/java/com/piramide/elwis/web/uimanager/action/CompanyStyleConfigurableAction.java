package com.piramide.elwis.web.uimanager.action;

import com.piramide.elwis.utils.UIManagerConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Alfacentauro Team
 * used when be modify an style sheet of a company
 *
 * @author miky
 * @version $Id: CompanyStyleConfigurableAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CompanyStyleConfigurableAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CompanyStyleConfigurableAction..............................");

        ActionForward forward;
        forward = null;

        request.setAttribute("companyStyleKey", UIManagerConstants.COMPANY_STYLE_KEY);

        forward = new ActionForward(mapping.getParameter());

        return forward;
    }

}
