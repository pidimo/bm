package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.campaignmanager.el.Functions;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.8
 */
public class CriterionOperatorReadValueAction extends ForwardAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        PrintWriter printerWriter = response.getWriter();
        response.setContentType("text/html");
        response.setHeader("Cache-Control", "max-age=0");
        response.setCharacterEncoding(Constants.CHARSET_ENCODING);
        response.setHeader("Pragma", "no-cache");
        printerWriter.write(getHtmlContent(request, user));
        return mapping.findForward("Success");
    }

    public String getHtmlContent(HttpServletRequest request, User user) {
        String campCategoryValueId = request.getParameter("campCriterionValueId").trim();
        String operator = request.getParameter("operator");
        String fieldType = request.getParameter("fieldType");
        StringBuffer html = new StringBuffer();


        if (CampaignConstants.CriteriaComparator.RELATION_EXISTS.equal(operator)) {
            html.append(Functions.composeRelationExistsOperatorValue(campCategoryValueId, request));
        }

        return html.toString();
    }
}
