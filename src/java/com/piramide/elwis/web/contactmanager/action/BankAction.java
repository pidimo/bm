package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.catalogmanager.BankCmd;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.MessagesUtil;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Mar 28, 2005
 * Time: 11:04:34 AM
 * To change this template use File | Settings | File Templates.
 */

public class BankAction extends ContactManagerAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("enter to Bank *** Action");
        DefaultForm bankForm = (DefaultForm) form;
        ActionErrors errors = bankForm.validate(mapping, request);

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (mapping.findForward("Fail"));
        }

        BankCmd cmd = new BankCmd();
        cmd.putParam(bankForm.getDtoMap());
        cmd.putParam("bankCode", request.getParameter("dto(bankCode)"));

        BusinessDelegate.i.execute(cmd, request);

        if (cmd.getResultDTO().isFailure()) {
            ActionErrors actionErrors = MessagesUtil.i.convertToActionErrors(mapping, request, cmd.getResultDTO());
            saveErrors(request, actionErrors);
            return (mapping.findForward("Fail"));
        }
        response.setContentType("text/html");
        Object bankId = cmd.getResultDTO().get("bankId");

        if (Functions.isBootstrapUIMode(request)) {
            closeSubmitModal(response, bankId);
        } else {
            closeSubmitPopup(response, bankId);
        }
        return null;
    }

    private void closeSubmitPopup(HttpServletResponse response, Object bankId) throws IOException {
        response.getOutputStream().print("<html><head><script>" +
                "function sendId(){" +
                "opener.setId(" + bankId + ");" +
                "window.close();" +
                "}" +
                "</script>" +
                "<body onLoad=\"sendId()\"/>" +
                "</head></html>");
    }

    private void closeSubmitModal(HttpServletResponse response, Object bankId) throws IOException {
        response.getOutputStream().print("<html><head><script>" +
                "function sendId(){" +
                "parent.setId(" + bankId + ");" +
                "parent.hideBootstrapPopup();" +
                "}" +
                "</script>" +
                "<body onLoad=\"sendId()\"/>" +
                "</head></html>");
    }
}
