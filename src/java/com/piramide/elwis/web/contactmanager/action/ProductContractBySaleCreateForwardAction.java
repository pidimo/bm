package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.dto.salesmanager.SaleDTO;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ProductContractBySaleCreateForwardAction extends SaleManagerForwardActon {
    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        String saleId = request.getParameter("saleId");

        SaleDTO saleDTO = Functions.getSaleDTO(saleId, request);
        Integer processId = (Integer) saleDTO.get("processId");
        Integer contactId = (Integer) saleDTO.get("contactId");
        if (null != processId && null != contactId) {
            Functions.setProductContractDefaultValues(defaultForm,
                    saleId,
                    processId.toString(),
                    request.getParameter("salePositionId"),
                    request);
        } else {
            Functions.setProductContractDefaultValues(defaultForm, request.getParameter("salePositionId"), request);
        }
    }

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        ActionForward forward = super.validateElementExistence(request, mapping);
        if (null != forward) {
            return forward;
        }

        ActionErrors errors = Functions.existsSalePosition(request);
        if (null != errors) {
            saveErrors(request.getSession(), errors);
            return mapping.findForward("NotFound");
        }

        return forward;
    }
}
