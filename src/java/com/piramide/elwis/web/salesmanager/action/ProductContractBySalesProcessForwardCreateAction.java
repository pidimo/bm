package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class ProductContractBySalesProcessForwardCreateAction extends SalePositionBySalesProcessForwardCreateAction {
    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        ActionForward forward = super.validateElementExistence(request, mapping);

        //Salesprocess or Sale was deleted
        if (null != forward) {
            return forward;
        }

        ActionErrors errors = Functions.existsSalePosition(request);
        if (null != errors) {
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        return null;

    }

    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        String saleId = request.getParameter("saleId");
        String processId = request.getParameter("processId");
        String salePositionId = request.getParameter("salePositionId");
        Functions.setProductContractDefaultValues(defaultForm, saleId, processId, salePositionId, request);
    }
}
