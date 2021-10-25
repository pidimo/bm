package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.financemanager.InvoicePosition;
import com.piramide.elwis.dto.financemanager.InvoicePositionDTO;

import javax.ejb.SessionContext;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.3
 */
public class CreditNoteInvoicePositionCmd extends InvoicePositionCmd {
    @Override
    public void executeInStateless(SessionContext ctx) {
        List<Integer> sourceInvoicePositionsIdentifiers =
                (List<Integer>) paramDTO.get("sourceInvoicePositionsIdentifiers");
        Integer invoiceId = EJBCommandUtil.i.getValueAsInteger(this, "invoiceId");
        Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
        copyInvoicePositions(
                sourceInvoicePositionsIdentifiers,
                invoiceId, companyId, ctx);
    }

    /**
     * Makes a copy of  the selected invoice positions to the credit note.
     *
     * @param sourceInvoicePositionsIdentifiers
     *                  <code>List</code> that contain the Invoice position identifiers to make the copy.
     * @param invoiceId <code>Integer</code> Credit note identifier
     * @param companyId <code>Integer</code> Company identifier.
     * @param ctx       <code>SessionContext</code> for general purposes.
     */
    private void copyInvoicePositions(List<Integer> sourceInvoicePositionsIdentifiers,
                                      Integer invoiceId,
                                      Integer companyId,
                                      SessionContext ctx) {
        List registredPositions = getInvoicePositions(invoiceId, companyId);
        int storedPositionsCounter = registredPositions.size();

        for (int i = 0; i < sourceInvoicePositionsIdentifiers.size(); i++) {
            Integer sourcePositionId = sourceInvoicePositionsIdentifiers.get(i);
            InvoicePosition sourceInvoicePosition = findInvoicePosition(sourcePositionId);

            InvoicePositionDTO targetInvoicePosition = new InvoicePositionDTO();
            targetInvoicePosition.put("invoiceId", invoiceId);
            targetInvoicePosition.put("number", storedPositionsCounter + i + 1);
            targetInvoicePosition.put("productId", sourceInvoicePosition.getProductId());
            targetInvoicePosition.put("companyId", sourceInvoicePosition.getCompanyId());
            targetInvoicePosition.put("accountId", sourceInvoicePosition.getAccountId());
            targetInvoicePosition.put("quantity", sourceInvoicePosition.getQuantity());
            targetInvoicePosition.put("unit", sourceInvoicePosition.getUnit());
            targetInvoicePosition.put("vatId", sourceInvoicePosition.getVatId());
            targetInvoicePosition.put("unitPriceGross", sourceInvoicePosition.getUnitPriceGross());
            targetInvoicePosition.put("unitPrice", sourceInvoicePosition.getUnitPrice());
            targetInvoicePosition.put("totalPrice", sourceInvoicePosition.getTotalPrice());
            targetInvoicePosition.put("totalPriceGross", sourceInvoicePosition.getTotalPriceGross());
            targetInvoicePosition.put("vatRate", sourceInvoicePosition.getVatRate());
            targetInvoicePosition.put("payStepId", sourceInvoicePosition.getPayStepId());
            targetInvoicePosition.put("contractId", sourceInvoicePosition.getContractId());
            targetInvoicePosition.put("discount", sourceInvoicePosition.getDiscount());
            targetInvoicePosition.put("discountValue", sourceInvoicePosition.getDiscountValue());
            String text = null;
            if (null != sourceInvoicePosition.getFreetextId()) {
                text = new String(sourceInvoicePosition.getFinanceFreeText().getValue());
            }

            create(targetInvoicePosition, text, ctx);
        }
    }

}
