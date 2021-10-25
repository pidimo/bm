package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.financemanager.util.InvoiceGenerateDocumentUtil;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 * action to update reminder and generate document if is necessary
 *
 * @author Miky
 * @version $Id: InvoiceReminderUpdateAction.java 19-sep-2008 14:59:02 $
 */
public class InvoiceReminderUpdateAction extends InvoiceManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing InvoiceReminderUpdateAction........" + request.getParameterMap());
        DefaultForm defaultForm = (DefaultForm) form;
        Integer reminderId = new Integer(defaultForm.getDto("reminderId").toString());

        ActionForward forward = super.execute(mapping, form, request, response);

        log.debug("After of cmd execute...................." + request.getAttribute("dto"));
        if ("Success".equals(forward.getName()) && request.getParameter("generate") != null) {
            InvoiceGenerateDocumentUtil generateUtil = new InvoiceGenerateDocumentUtil();
            ActionErrors errors = generateUtil.generateReminderDocument(reminderId, mapping, request);
            if (errors.isEmpty()) {
                //onload to download document
                String js = "onLoad=\"downloadReminder(\'" + generateUtil.getReminderDocumentId() + "\');\"";
                request.setAttribute("jsLoad", js);
            } else {
                saveErrors(request.getSession(), errors);
            }

            ActionForwardParameters forwardParameters = new ActionForwardParameters();
            forwardParameters.add("reminderId", reminderId.toString()).
                    add("dto(reminderId)", reminderId.toString()).
                    add("dto(op)", "read");

            forward = forwardParameters.forward(mapping.findForward("Generation"));
        }

        return forward;
    }
}
