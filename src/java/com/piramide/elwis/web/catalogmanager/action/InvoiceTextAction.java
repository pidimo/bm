package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceTextAction extends InvoiceTemplateAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward actionForward = super.execute(mapping, form, request, response);

        String templateId = request.getParameter("dto(templateId)");
        String title = request.getParameter("dto(title)");
        ActionForwardParameters parameters = new ActionForwardParameters();
        parameters.add("templateId", templateId).
                add("dto(templateId)", templateId).
                add("dto(title)", title).
                add("dto(op)", "read");

        return parameters.forward(actionForward);
    }
}
