package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.salesmanager.el.Functions;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SaleManagerListAction extends ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;

        if (null != (forward = validateElementExistence(request, mapping))) {
            return forward;
        }

        addFilter("saleId", request.getParameter("saleId"));
        setModuleId(request.getParameter("saleId"));
        return super.execute(mapping, form, request, response);
    }

    protected ActionForward validateElementExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {
        ActionErrors errors = Functions.existsSale(request);
        if (null == errors) {
            return null;
        }

        saveErrors(request.getSession(), errors);
        return mapping.findForward("MainSaleList");
    }
}
