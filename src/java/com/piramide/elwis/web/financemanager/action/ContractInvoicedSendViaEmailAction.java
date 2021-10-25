package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.financemanager.util.InvoiceSendViaEmailUtil;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Action to create invoice document as draft email and redirect to webmail module
 * @author Miguel A. Rojas Cardenas
 * @version 4.7
 */
public class ContractInvoicedSendViaEmailAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ContractInvoicedSendViaEmailAction........" + request.getParameterMap());
        ActionForward actionForward = null;

        ActionErrors errors = new ActionErrors();
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
        Integer userMailId = (Integer) user.getValue("userId");

        Integer invoiceId = null;
        Integer telecomId = null;
        if (!GenericValidator.isBlankOrNull(request.getParameter("invoiceId"))) {
            invoiceId = new Integer(request.getParameter("invoiceId"));
        }

        if (!GenericValidator.isBlankOrNull(request.getParameter("telecomId"))) {
            telecomId = new Integer(request.getParameter("telecomId"));
        }

        if (invoiceId != null) {
            InvoiceSendViaEmailUtil invoiceSendViaEmailUtil = new InvoiceSendViaEmailUtil();
            Integer mailId = invoiceSendViaEmailUtil.createDraftEmail(userMailId, companyId, invoiceId, telecomId, request);

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
                if (!invoiceSendViaEmailUtil.getErrors().isEmpty()) {
                    saveErrors(request.getSession(), invoiceSendViaEmailUtil.getErrors());
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
