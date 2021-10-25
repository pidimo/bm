package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 06-jun-2006
 * Time: 12:05:58
 * To change this template use File | Settings | File Templates.
 */

public class CampaignTemplateForwardAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("languageTemplateId for user session  = " + request.getParameter("fid"));
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGNFREETEXT, "freetextid",
                request.getParameter("fid"), errors, new ActionError("Document.error.notfound"));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        StringBuffer sb = new StringBuffer();
        StringBuffer param = new StringBuffer(request.getContextPath());
        param.append("/campaign/Download.do");
        param.append("?dto(cid)=").append(request.getParameter("cid"));
        param.append("&dto(fid)=").append(request.getParameter("fid"));
        param.append("&dto(li)=").append(request.getParameter("li"));
        param.append("&dto(type)=").append("camp");

        sb.append("onLoad=\"window.open('").append(response.encodeRedirectURL(param.toString())).append("')\"");
        request.setAttribute("jsLoad", new String(sb));
        return mapping.findForward("Success_");
    }
}