package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.cmd.supportmanager.CommentCmd;
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
 * Date: Aug 25, 2005
 * Time: 2:09:21 PM
 * To change this template use File | Settings | File Templates.
 */

public class CommentCreateAction extends ArticleManagerAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("         CommentCREATEAction ... execute ...");
        ActionErrors errors = new ActionErrors();
        ActionForward forward = mapping.findForward("Success");
        DefaultForm defaultForm = (DefaultForm) form;

        errors = ForeignkeyValidator.i.validate(SupportConstants.TABLE_ARTICLE, "articleid",
                request.getParameter("articleId"), errors, new ActionError("msg.NotFound",
                        request.getParameter("dto(articleTitle)")));
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        errors = new ActionErrors();
        errors = defaultForm.validate(mapping, request);
        if (!errors.isEmpty()) {
            CommentReadListCmd cmd = new CommentReadListCmd();
            cmd.putParam("articleId", request.getParameter("articleId"));
            BusinessDelegate.i.execute(cmd, request);
            saveErrors(request, errors);
            defaultForm.setDto("commentList", cmd.getResultDTO().get("commentList"));
            return mapping.findForward("Fail");
        } else {
            CommentCmd commentCmd = new CommentCmd();
            commentCmd.putParam(defaultForm.getDtoMap());
            BusinessDelegate.i.execute(commentCmd, request);
            errors = MessagesUtil.i.convertToActionErrors(mapping, request, commentCmd.getResultDTO());
            saveErrors(request, errors);
            if (errors.isEmpty()) {
                defaultForm.getDtoMap().putAll(commentCmd.getResultDTO());
            } else {
                forward = mapping.findForward("Fail");
            }
        }
        return forward;
    }
}


