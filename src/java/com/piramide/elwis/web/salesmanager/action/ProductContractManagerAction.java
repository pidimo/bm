package com.piramide.elwis.web.salesmanager.action;


import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.salesmanager.el.Functions;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProductContractManagerAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if ((forward = checkCancel(mapping, request)) != null) {
            return forward;
        }

        if (null != (forward = validateElementExistence(request, mapping))) {
            return forward;
        }

        return super.execute(mapping, form, request, response);
    }

    protected ActionForward validateElementExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {
        if (!Functions.existsProductContract(request.getParameter("contractId"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("ProductContractNotFound", new ActionError("ProductContract.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("MainProductContractList");
        }

        return null;
    }
}
