package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceTemplateAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if (null != (forward = validateInvoiceTemplateExistence(request, mapping))) {
            return forward;
        }

        return super.execute(mapping, form, request, response);
    }

    protected ActionForward validateInvoiceTemplateExistence(HttpServletRequest request, ActionMapping mapping) {
        if (!existsInvoiceTemplate(request.getParameter("dto(templateId)"))) {
            String title = request.getParameter("dto(title)");
            ActionErrors errors = new ActionErrors();
            errors.add("templateDelete", new ActionError("customMsg.NotFound", title));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }
        return null;
    }

    private boolean existsInvoiceTemplate(Object keyValue) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(
                FinanceConstants.TABLE_INVOICETEMPLATE,
                "templateid",
                keyValue,
                errors, new ActionError("template.NotFound"));
        return errors.isEmpty();
    }
}
