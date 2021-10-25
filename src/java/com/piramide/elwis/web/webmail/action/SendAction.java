package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.webmailmanager.CommunicationManagerCmd;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : ivan
 * @version : $Id SendAction ${time}
 */
public class SendAction extends WebmailDefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        Boolean saveSendItem = Boolean.valueOf(request.getParameter("dto(saveSendItem)"));

        List mailListDispatched = new ArrayList();

        if (null != saveSendItem && !saveSendItem) {
            mailListDispatched = (List) request.getSession().getAttribute("mailListDispatched");
            //request.getSession().removeAttribute("mailListDispatched");

            if (null == mailListDispatched) {
                ActionErrors errors = new ActionErrors();
                errors.add("webmailError", new ActionError("WebMail.error.MessageNotFound"));
                saveErrors(request, errors);
            }

            addValuesInRequest(mailListDispatched, saveSendItem, null, request);
            return mapping.findForward("Success");
        }

        if (GenericValidator.isBlankOrNull(request.getParameter("dto(mailId)"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("webmailError", new ActionError("WebMail.error.MessageNotFound"));
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        Integer mailId = new Integer(request.getParameter("dto(mailId)"));
        User user = RequestUtils.getUser(request);
        CommunicationManagerCmd cmd = new CommunicationManagerCmd();
        cmd.setOp("checkRecipientSent");
        cmd.putParam("mailId", mailId);
        cmd.putParam("companyId", user.getValue("companyId"));
        cmd.putParam("saveSendItem", saveSendItem);

        ResultDTO myResultDTO = BusinessDelegate.i.execute(cmd, request);
        if (!((List) myResultDTO.get("mailListDispatched")).isEmpty()) {
            mailListDispatched = (List) myResultDTO.get("mailListDispatched");
        }

        addValuesInRequest(mailListDispatched, saveSendItem, mailId, request);
        return super.execute(mapping, form, request, response);
    }

    private void addValuesInRequest(List mailListDispatched,
                                    Boolean saveSendItem,
                                    Integer mailId,
                                    HttpServletRequest request) {
        Map dto = new HashMap();
        dto.put("mailListDispatched", mailListDispatched);
        dto.put("saveSendItem", saveSendItem);
        if (null != mailId) {
            dto.put("mailId", mailId);
        }

        request.setAttribute("dto", dto);
    }
}
