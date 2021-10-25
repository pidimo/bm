package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.financemanager.util.InvoiceGenerateDocumentUtil;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Jatun S.R.L.
 * action to create and generate invoice reminder if is necessary
 *
 * @author Miky
 * @version $Id: InvoiceReminderCreateAction.java 07-oct-2008 10:14:51 $
 */
public class InvoiceReminderCreateAction extends InvoiceManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing InvoiceReminderCreateAction........" + request.getParameterMap());

        ActionForward forward = super.execute(mapping, form, request, response);

        log.debug("After of cmd execute...................." + request.getAttribute("dto"));
        if ("Success".equals(forward.getName()) && request.getParameter("generate") != null) {
            Map resultDto = (Map) request.getAttribute("dto");

            Integer reminderId = new Integer(resultDto.get("reminderId").toString());

            InvoiceGenerateDocumentUtil generateUtil = new InvoiceGenerateDocumentUtil();

            ActionErrors errors = generateUtil.generateReminderDocument(reminderId, mapping, request);

            ActionForwardParameters forwardParameters = new ActionForwardParameters();
            forwardParameters.add("reminderId", reminderId.toString()).
                    add("dto(reminderId)", reminderId.toString()).
                    add("dto(op)", "read");

            if (errors.isEmpty()) {
                //onload to download document
                String js = "onLoad=\"downloadReminder(\'" + generateUtil.getReminderDocumentId() + "\');\"";
                request.setAttribute("jsLoad", js);
                forward = forwardParameters.forward(mapping.findForward("Generation"));
            } else {
                saveErrors(request.getSession(), errors);
                forward = forwardParameters.forward(mapping.findForward("GenerationError"));
            }

        }

        return forward;
    }
}
