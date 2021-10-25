package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.cmd.supportmanager.CommentReadListCmd;
import com.piramide.elwis.utils.SupportConstants;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 23, 2005
 * Time: 11:53:06 AM
 * To change this template use File | Settings | File Templates.
 */

public class CommentListAction extends ArticleManagerAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("         CommentListAction ... execute ...");
        ActionErrors errors = new ActionErrors();

        errors = ForeignkeyValidator.i.validate(SupportConstants.TABLE_ARTICLE, "articleid",
                request.getParameter("articleId"), errors, new ActionError("msg.NotFound",
                        request.getParameter("dto(articleTitle)")));
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        } else {
            DefaultForm defaultForm = (DefaultForm) form;
            ActionForward forward = mapping.findForward("Success");
            CommentReadListCmd cmd = new CommentReadListCmd();
            cmd.putParam("articleId", request.getParameter("articleId"));
            BusinessDelegate.i.execute(cmd, request);
            errors = MessagesUtil.i.convertToActionErrors(mapping, request, cmd.getResultDTO());
            saveErrors(request, errors);
            if (errors.isEmpty()) {
                defaultForm.getDtoMap().putAll(cmd.getResultDTO());
                defaultForm.setDto("commentList", cmd.getResultDTO().get("commentList"));
            } else {
                forward = mapping.findForward("Fail");
            }
            return forward;
        }
    }
}

