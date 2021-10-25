package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SaleManagerAction extends ContactManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if ((forward = checkCancel(mapping, request)) != null) {
            return forward;
        }

        if (null != (forward = validateElementExistence((DefaultForm) form, request, mapping))) {
            return forward;
        }

        if (updateForm((DefaultForm) form, request)) {
            return mapping.getInputForward();
        }

        forward = super.execute(mapping, form, request, response);
        return processForward(forward, (DefaultForm) form, mapping, request);
    }

    protected ActionForward validateElementExistence(DefaultForm defaultForm,
                                                     HttpServletRequest request,
                                                     ActionMapping mapping) {
        if (!Functions.existsSale(request.getParameter("saleId"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("SaleNotFound", new ActionError("Sale.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("SaleList");
        }

        if (null != request.getParameter("contactId") &&
                !"".equals(request.getParameter("contactId").trim())) {
            if (Functions.hasChangeCustomer(request.getParameter("saleId"),
                    request.getParameter("contactId"), request)) {
                ActionErrors errors = new ActionErrors();
                errors.add("ChangedCustomer", new ActionError("Sale.hasChangedCustomer"));
                saveErrors(request.getSession(), errors);
                return mapping.findForward("SaleList");
            }
        }

        return null;
    }

    protected boolean updateForm(DefaultForm defaultForm, HttpServletRequest request) {
        return false;
    }

    @Override
    protected ActionForward processForward(ActionForward forward,
                                           DefaultForm defaultForm,
                                           ActionMapping mapping,
                                           HttpServletRequest request) {
        return forward;
    }
}
