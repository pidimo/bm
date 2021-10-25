package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.dto.financemanager.InvoiceDTO;
import com.piramide.elwis.dto.financemanager.InvoicePositionDTO;
import com.piramide.elwis.web.financemanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.3
 */
public class CreditNoteInvoicePositionCreateForwardAction extends InvoicePositionCreateForwardAction {
    private InvoiceDTO creditNote;


    /**
     * Set in <code>DefaultForm</code> object the next fields:
     * <code>sourceInvoicePositions</code> is a <code>List</code> that contain the <code>InvoicePositionDTO</code>
     * objects that will be copied.
     * <code>availableInvoicePositions</code> is a <code>String</code> object that contain the
     * <code>InvoicePositionDTO</code> identifiers
     *
     * @param defaultForm <code>DefaultForm</code> object.
     * @param request     <code>HttpServletRequest</code> object
     */
    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        Integer creditNoteOfId = (Integer) creditNote.get("creditNoteOfId");

        List<InvoicePositionDTO> sourceInvoicePositions =
                com.piramide.elwis.web.financemanager.el.Functions.getSourceInvoicePositions(creditNoteOfId, request);
        String availableInvoicePositions = "";

        for (int i = 0; i < sourceInvoicePositions.size(); i++) {
            InvoicePositionDTO dto = sourceInvoicePositions.get(i);
            Integer positionId = (Integer) dto.get("positionId");

            defaultForm.setDto(positionId.toString(), positionId);

            availableInvoicePositions += positionId.toString();
            if (i < sourceInvoicePositions.size() - 1) {
                availableInvoicePositions += ",";
            }
        }
        defaultForm.setDto("sourceInvoicePositions", sourceInvoicePositions);
        defaultForm.setDto("availableInvoicePositions", availableInvoicePositions);
    }

    @Override
    protected ActionForward validateInvoiceExistence(HttpServletRequest request, ActionMapping mapping) {
        ActionForward forward = super.validateInvoiceExistence(request, mapping);
        if (null == forward) {
            String creditNoteId = request.getParameter("invoiceId");
            creditNote = Functions.getInvoice(creditNoteId, request);
        }

        return forward;
    }
}
