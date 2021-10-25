package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.salesmanager.util.ActionSendViaEmailUtil;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Action to send sales process action document via email
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.5.3
 */
public class ActionDocumentSendViaEmailAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ActionDocumentSendViaEmailAction........" + request.getParameterMap());
        ActionForward actionForward = null;

        ActionErrors errors = new ActionErrors();
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
        Integer userMailId = (Integer) user.getValue("userId");

        Integer freeTextId = null;
        Integer telecomId = null;
        Integer processId = null;
        Integer contactId = null;

        if (!GenericValidator.isBlankOrNull(request.getParameter("freeTextId"))) {
            freeTextId = new Integer(request.getParameter("freeTextId"));
        }

        if (!GenericValidator.isBlankOrNull(request.getParameter("telecomId"))) {
            telecomId = new Integer(request.getParameter("telecomId"));
        }

        if (!GenericValidator.isBlankOrNull(request.getParameter("paramProcessId"))) {
            processId = new Integer(request.getParameter("paramProcessId"));
        }

        if (!GenericValidator.isBlankOrNull(request.getParameter("paramContactId"))) {
            contactId = new Integer(request.getParameter("paramContactId"));
        }

        if (freeTextId != null) {
            ActionSendViaEmailUtil actionSendViaEmailUtil = new ActionSendViaEmailUtil();
            Integer mailId = actionSendViaEmailUtil.createDraftEmail(userMailId, companyId, freeTextId, telecomId, processId, contactId, request);

            if (mailId != null) {
                String folderDraftId = findSystemFolderDraftId(userMailId, request);

                ActionForwardParameters forwardParameters = new ActionForwardParameters();
                forwardParameters.add("dto(mailId)", mailId.toString()).
                        add("dto(draftId)", folderDraftId).
                        add("dto(isDraft)", Boolean.TRUE.toString());

                actionForward = forwardParameters.forward(mapping.findForward("Success"));

                //set in session the folder selected to show in webmail module
                request.getSession().setAttribute("folderView", folderDraftId);
            } else {
                if (!actionSendViaEmailUtil.getErrors().isEmpty()) {
                    saveErrors(request.getSession(), actionSendViaEmailUtil.getErrors());
                }
            }
        }

        if (actionForward == null) {
            actionForward = mapping.findForward("Fail");
        }

        return actionForward;
    }

    private String findSystemFolderDraftId(Integer userMailId,HttpServletRequest request) {
        String folderDraftId = "";
        Map systemFolders = com.piramide.elwis.web.webmail.el.Functions.getSystemFolders(userMailId.toString(), request);
        if (null != systemFolders && null != systemFolders.get("draftId")) {
            folderDraftId = systemFolders.get("draftId").toString();
        }
        return folderDraftId;
    }
}
