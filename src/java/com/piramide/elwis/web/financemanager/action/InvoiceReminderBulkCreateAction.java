package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Jatun S.R.L.
 * Action to create bulk reminders and set in onload merge documents
 *
 * @author Miky
 * @version $Id: InvoiceReminderBulkCreateAction.java 07-nov-2008 14:49:07 $
 */
public class InvoiceReminderBulkCreateAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing InvoiceReminderBulkCreateAction........" + request.getParameterMap());

        ActionForward forward = super.execute(mapping, form, request, response);

        log.debug("After of cmd execute...................." + request.getAttribute("dto"));
        if ("Success".equals(forward.getName())) {
            Map dtoMap = (Map) request.getAttribute("dto");
            String reminderDocumentIds = (String) dtoMap.get("reminderDocumentIds");

            //onload to download document
            String js = "onLoad=\"mergeReminderDocuments(\'" + reminderDocumentIds + "\');\"";
            request.setAttribute("jsLoad", js);
        }

        return forward;
    }
}
