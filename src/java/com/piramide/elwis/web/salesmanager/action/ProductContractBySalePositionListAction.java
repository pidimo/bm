package com.piramide.elwis.web.salesmanager.action;

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
public class ProductContractBySalePositionListAction extends SaleManagerListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        addFilter("salePositionId", request.getParameter("salePositionId"));
        setModuleId(request.getParameter("salePositionId"));
        return super.execute(mapping, form, request, response);
    }

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        return null;
    }
}
