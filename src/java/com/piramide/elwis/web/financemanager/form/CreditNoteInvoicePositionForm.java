package com.piramide.elwis.web.financemanager.form;

import com.piramide.elwis.dto.financemanager.InvoiceDTO;
import com.piramide.elwis.dto.financemanager.InvoicePositionDTO;
import com.piramide.elwis.utils.BigDecimalUtils;
import com.piramide.elwis.utils.FinanceConstants;
import static com.piramide.elwis.web.financemanager.el.Functions.getInvoicePosition;
import static com.piramide.elwis.web.financemanager.el.Functions.getInvoicePositionsByContract;
import static com.piramide.elwis.web.financemanager.util.InvoicePositionCommon.i;
import com.piramide.elwis.web.financemanager.util.InvoicePositionValidatorUtil;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.3
 */
public class CreditNoteInvoicePositionForm extends DefaultForm {
    private InvoiceDTO invoiceDTO;
    private List<InvoicePositionDTO> dataBaseInvoicePositions = new ArrayList<InvoicePositionDTO>();

    /**
     * Validate the <code>CreditNoteInvoicePositionForm</code> data, make the next validations:
     * 1.- At least one element must be selected in the form.
     * 2.- All selected elements must be continue stored in data base.
     * 3.- The sum of prices of the all elements to be copied should not be greater than to the related
     * invoice open amount.
     *
     * @param mapping <code>ActionMapping</code> object.
     * @param request <code>HttpServletRequest</code> object for general purposes.
     * @return <code>ActionErrors</code> with <code>ActionErrors</code> if some validation fails.
     */
    @Override
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        String invoiceId = (String) getDto("invoiceId");
        if (!Functions.existsInvoice(invoiceId)) {
            return new ActionErrors();
        }

        invoiceDTO = com.piramide.elwis.web.financemanager.el.Functions.getInvoice(invoiceId, request);

        List<InvoicePositionDTO> sourceInvoicePositions = buildSourceInvoicePositionsList();
        ActionErrors errors = super.validate(mapping, request);

        ActionError atleastOneSelectedError = atleastOneSelectedValidation(sourceInvoicePositions);
        if (null != atleastOneSelectedError) {
            errors.add("atleastOneSelectedError", atleastOneSelectedError);
            setDto("sourceInvoicePositions", sourceInvoicePositions);
            return errors;
        }

        ActionError integrityReferentialError = integrityReferentialValidation(sourceInvoicePositions, request);
        if (null != integrityReferentialError) {
            errors.add("integrityReferentialError", integrityReferentialError);
            setDto("sourceInvoicePositions", sourceInvoicePositions);
            return errors;
        }

        ActionError openAmountError = totalAmountAndRelatedInvoiceOpenAmountValidation(request);
        if (null != openAmountError) {
            errors.add("openAmountError", openAmountError);
            setDto("sourceInvoicePositions", sourceInvoicePositions);
            return errors;
        }

        List<ActionError> sourceInvoicePositionErrors = sourceInvoicePositionValidation(request);
        if (!sourceInvoicePositionErrors.isEmpty()) {
            for (int i = 0; i < sourceInvoicePositionErrors.size(); i++) {
                ActionError actionError = sourceInvoicePositionErrors.get(i);
                errors.add("sourceInvoicePositionValidation_" + i, actionError);
            }
            setDto("sourceInvoicePositions", sourceInvoicePositions);
            return errors;
        }

        if (errors.isEmpty()) {
            List<Integer> sourceInvoicePositionsIdentifiers = new ArrayList<Integer>();
            for (InvoicePositionDTO dto : dataBaseInvoicePositions) {
                Integer sourcePositionId = (Integer) dto.get("positionId");
                sourceInvoicePositionsIdentifiers.add(sourcePositionId);
            }

            setDto("sourceInvoicePositionsIdentifiers", sourceInvoicePositionsIdentifiers);
        }
        return errors;
    }

    protected ActionError atleastOneSelectedValidation(List<InvoicePositionDTO> sourceInvoicePositions) {
        boolean atleastOneHasSelected = false;
        for (InvoicePositionDTO dto : sourceInvoicePositions) {
            String positionId = (String) dto.get("positionId");
            if (null != getDto(positionId)) {
                atleastOneHasSelected = true;
            }
        }

        if (!atleastOneHasSelected) {
            return new ActionError("CreditNote.CopyInvoicePosition.atleastOnePositionSelected.error");
        }

        return null;
    }

    protected List<ActionError> sourceInvoicePositionValidation(HttpServletRequest request) {
        InvoicePositionValidatorUtil invoicePositionValidatorUtil = new InvoicePositionValidatorUtil();

        List<ActionError> errors = new ArrayList<ActionError>();

        for (int i = 0; i < dataBaseInvoicePositions.size(); i++) {
            InvoicePositionDTO dto = dataBaseInvoicePositions.get(i);
            Integer contractId = (Integer) dto.get("contractId");
            if (null == contractId) {
                continue;
            }

            String payStepId = null;
            if (null != dto.get("payStepId")) {
                payStepId = dto.get("payStepId").toString();
            }
            BigDecimal price = getPrice(dto);
            if (null == price) {
                price = BigDecimal.ZERO;
            }
            BigDecimal quantity = (BigDecimal) dto.get("quantity");

            List<InvoicePositionDTO> invoicePositions = getInvoicePositionsByContract(contractId, request);

            Integer invoicePositionId = (Integer) dto.get("positionId");
            ActionError error =
                    invoicePositionValidatorUtil.validateCreditNoteProductContract(invoicePositions,
                            invoicePositionId,
                            contractId.toString(),
                            payStepId,
                            price,
                            quantity,
                            null,
                            null,
                            "create",
                            request
                    );

            if (null != error) {
                errors.add(new ActionError("CreditNote.copyInvoicePosition.totalPriceError", dto.get("number")));
            }
        }

        return errors;
    }

    /**
     * Verifies that all selected <code>InvoicePositionDTO</code> objects continues stored in data base.
     *
     * @param sourceInvoicePositions <code>List</code>  of <code>InvoicePositionDTO</code> that can be copy.
     * @param request                <code>HttpServletRequest</code> object for general purposes.
     * @return <code>ActionError</code> is some select <code>InvoicePositionDTO</code> was delete.
     */
    protected ActionError integrityReferentialValidation(List<InvoicePositionDTO> sourceInvoicePositions,
                                                         HttpServletRequest request) {

        for (InvoicePositionDTO dto : sourceInvoicePositions) {
            String positionId = (String) dto.get("positionId");
            String producName = (String) dto.get("productName");
            if (null != getDto(positionId)) {
                InvoicePositionDTO dbInvoicePositionDTO = getInvoicePosition(positionId, request);
                if (null == dbInvoicePositionDTO) {
                    sourceInvoicePositions.remove(dto);
                    return new ActionError("CreditNote.CopyInvoicePosition.positionDelete.error", producName);
                }
                dataBaseInvoicePositions.add(dbInvoicePositionDTO);
            }
        }

        return null;
    }

    private BigDecimal getPrice(InvoicePositionDTO invoicePositionDTO) {
        if (FinanceConstants.NetGrossFLag.NET.equal((Integer) invoiceDTO.get("netGross"))) {
            return (BigDecimal) invoicePositionDTO.get("unitPrice");
        }

        if (FinanceConstants.NetGrossFLag.GROSS.equal((Integer) invoiceDTO.get("netGross"))) {
            return (BigDecimal) invoicePositionDTO.get("unitPriceGross");
        }

        return null;
    }

    /**
     * Validates the sum of prices of the all elements to be copied should not be greater than to the
     * open amount of the related invoice.
     *
     * @param request <code>HttpServletRequest</code>object for general purposes
     * @return <code>ActionError</code> if the sum exceed the open amount of the related invoice.
     */
    protected ActionError totalAmountAndRelatedInvoiceOpenAmountValidation(HttpServletRequest request) {
        Integer creditNoteOfId = (Integer) invoiceDTO.get("creditNoteOfId");
        InvoiceDTO relatedInvoiceDTO = com.piramide.elwis.web.financemanager.el.Functions.getInvoice(
                creditNoteOfId.toString(),
                request);

        BigDecimal associatedInvoiceOpenAmount = (BigDecimal) relatedInvoiceDTO.get("openAmount");
        BigDecimal sumPrices = null;

        if (i.useNetCalculations(invoiceDTO)) {
            sumPrices = sumNetUnitPrices();
        }

        if (i.useGrossCalculations(invoiceDTO)) {
            sumPrices = sumGrossUnitPrices();
        }

        if (sumPrices.compareTo(associatedInvoiceOpenAmount) == 1) {
            String associatedInvoiceNumber = (String) relatedInvoiceDTO.get("number");
            return new ActionError("CreditNote.createInvoicePosition.error", associatedInvoiceNumber);
        }

        return null;
    }

    private BigDecimal sumNetUnitPrices() {
        BigDecimal total = new BigDecimal(0);
        for (InvoicePositionDTO dto : dataBaseInvoicePositions) {
            BigDecimal netPrice = (BigDecimal) dto.get("totalPrice");
            if (null != netPrice) {
                total = BigDecimalUtils.sum(total, netPrice);
            }
        }

        return total;
    }

    private BigDecimal sumGrossUnitPrices() {
        BigDecimal total = new BigDecimal(0);
        for (InvoicePositionDTO dto : dataBaseInvoicePositions) {
            BigDecimal grossPrice = (BigDecimal) dto.get("totalPriceGross");
            if (null != grossPrice) {
                total = BigDecimalUtils.sum(total, grossPrice);
            }
        }

        return total;
    }

    private List<InvoicePositionDTO> buildSourceInvoicePositionsList() {
        List<String> availableInvoicePositionsIds = getAvailableInvoicePositions();
        List<InvoicePositionDTO> result = new ArrayList<InvoicePositionDTO>();

        for (int i = 0; i < availableInvoicePositionsIds.size(); i++) {
            String id = availableInvoicePositionsIds.get(i);
            InvoicePositionDTO dto = new InvoicePositionDTO();
            dto.put("positionId", id);
            dto.put("productName", getProductName(id));
            dto.put("number", getNumber(id));
            dto.put("quantity", getQuantity(id));
            dto.put("unitPrice", getUnitPrice(id));
            dto.put("totalPrice", getTotalPrice(id));
            dto.put("unitPriceGross", getUnitPriceGross(id));
            dto.put("totalPriceGross", getTotalPriceGross(id));
            result.add(dto);
        }

        return result;
    }

    private String getProductName(String positionId) {
        return getInvoicePositionAttribute(positionId, "_productName");
    }

    private String getNumber(String positionId) {
        return getInvoicePositionAttribute(positionId, "_number");
    }

    private String getQuantity(String positionId) {
        return getInvoicePositionAttribute(positionId, "_quantity");
    }

    private String getUnitPrice(String positionId) {
        return getInvoicePositionAttribute(positionId, "_unitPrice");
    }

    private String getTotalPrice(String positionId) {
        return getInvoicePositionAttribute(positionId, "_totalPrice");
    }

    private String getUnitPriceGross(String positionId) {
        return getInvoicePositionAttribute(positionId, "_unitPriceGross");
    }

    private String getTotalPriceGross(String positionId) {
        return getInvoicePositionAttribute(positionId, "_totalPriceGross");
    }

    private String getInvoicePositionAttribute(String positionId, String key) {
        return (String) getDto(positionId + key);
    }

    private List<String> getAvailableInvoicePositions() {
        String availableInvoicePositions = (String) getDto("availableInvoicePositions");
        if (GenericValidator.isBlankOrNull(availableInvoicePositions)) {
            return new ArrayList<String>();
        }

        String[] identifiers = availableInvoicePositions.split("(,)");
        return Arrays.asList(identifiers);
    }
}
