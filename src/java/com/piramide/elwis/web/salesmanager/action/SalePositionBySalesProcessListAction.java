package com.piramide.elwis.web.salesmanager.action;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SalePositionBySalesProcessListAction extends SalesProcessListAction {

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        //because this list always executed into iframe
        return null;
    }

    @Override
    protected void addFilter(HttpServletRequest request) {
        super.addFilter(request);
        addFilter("saleId", request.getParameter("saleId"));
        setModuleId(request.getParameter("saleId"));
    }
}
