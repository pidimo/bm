package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SupportConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 2:09:12 PM
 * To change this template use File | Settings | File Templates.
 */

public class ArticleManagerAction extends DefaultAction {
    /**
     * Check if articleId exists, if exists continue, otherwise cancel operation and return article page
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("ArticleManagerAction execution  ..." + request.getParameter("articleId"));

        if (isCancelled(request) || "true".equals(request.getParameter("cancel"))) {
            log.debug("action was canceled");
            return (mapping.findForward("Cancel"));
        }
        //user in the session
        //cheking if working article was not deleted by other user.
        log.debug("articleId for user session  = " + request.getParameter("articleId"));
        ActionErrors errors = new ActionErrors();
        DateTimeZone timeZone;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        errors = ForeignkeyValidator.i.validate(SupportConstants.TABLE_ARTICLE, "articleid",
                request.getParameter("articleId"), errors, new ActionError("msg.NotFound",
                        request.getParameter("dto(articleTitle)")));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        if (user != null) {
            timeZone = (DateTimeZone) user.getValue("dateTimeZone");
        } else {
            timeZone = DateTimeZone.getDefault();
        }

        request.setAttribute("timeZone", timeZone);
        ((DefaultForm) form).setDto("articleId", request.getParameter("articleId"));
        ((DefaultForm) form).setDto("userId", user.getValue("userId"));

        return super.execute(mapping, form, request, response);
    }
}