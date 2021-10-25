package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SalePositionCreateForwardAction extends SaleManagerForwardAction {

    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        String saleId = request.getParameter("saleId");
        Functions.setSalePositionDefaultValues(defaultForm, saleId, request);
    }
}
