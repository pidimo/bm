package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.dto.financemanager.InvoiceDTO;
import com.piramide.elwis.dto.salesmanager.PaymentStepDTO;
import com.piramide.elwis.dto.salesmanager.ProductContractDTO;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.financemanager.util.InvoicePositionCommon;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

import static com.piramide.elwis.web.financemanager.el.Functions.getInvoice;
import static com.piramide.elwis.web.salesmanager.el.Functions.getPaymentSteps;
import static com.piramide.elwis.web.salesmanager.el.Functions.getProductContract;

/**
 * @author Ivan Alban
 * @version 4.3
 */
public class InvoicePositionAction extends InvoiceManagerAction {

    @Override
    protected void addAlertMessages(DefaultForm defaultForm, HttpServletRequest request) {
        Integer invoiceId = (Integer) defaultForm.getDto("invoiceId");

        if (null == invoiceId) {
            return;
        }

        Integer contractId = (Integer) defaultForm.getDto("contractId");
        if (null == contractId) {
            return;
        }

        ProductContractDTO contractDTO = getProductContract(contractId.toString(), request);

        InvoiceDTO invoiceDTO = getInvoice(invoiceId.toString(), request);

        if (isInvoice(invoiceDTO)) {
            addMessageForPeriodicContract(contractDTO, request);
        }

        addMessageForPartialFixedContract(contractDTO, invoiceDTO, request);

    }

    private void addMessageForPeriodicContract(ProductContractDTO productContractDTO,
                                               HttpServletRequest request) {
        if (isPeriodicContract(productContractDTO) || isPartialPeriodicContract(productContractDTO)) {
            ActionErrors errors = new ActionErrors();
            errors.add("PeriodicContractAlert",
                    new ActionError("InvoicePosition.PeriodicContract.invoiceUntilMessage",
                            productContractDTO.get("contractNumber"),
                            JSPHelper.getMessage(request, "ProductContract.invoiceUntil")));
            saveErrors(request.getSession(), errors);
        }
    }

    private void addMessageForPartialFixedContract(ProductContractDTO productContractDTO,
                                                   InvoiceDTO invoiceDTO,
                                                   HttpServletRequest request) {
        if (isPartialFixedContract(productContractDTO)) {
            Integer contractId = (Integer) productContractDTO.get("contractId");

            List<PaymentStepDTO> paymentStepDTOs = getPaymentSteps(contractId.toString(), request);

            Integer contractInstallment = (Integer) productContractDTO.get("installment");

            if (contractInstallment.equals(paymentStepDTOs.size())) {
                if (isCreditNote(invoiceDTO)) {
                    ActionErrors errors = new ActionErrors();
                    errors.add("partialFixedOpenAmount", getPartialFixedError(productContractDTO));
                    saveErrors(request.getSession(), errors);
                }
                if (isInvoice(invoiceDTO)) {

                    BigDecimal productContractPrice = (BigDecimal) productContractDTO.get("price");

                    BigDecimal sumOfPaymentAmounts = sumPaymentSteps(paymentStepDTOs, productContractDTO);

                    if (productContractPrice.compareTo(sumOfPaymentAmounts) == 1) {
                        ActionErrors errors = new ActionErrors();
                        errors.add("partialFixedOpenAmount", getPartialFixedError(productContractDTO));
                        saveErrors(request.getSession(), errors);
                    }
                }
            }
        }
    }

    private ActionError getPartialFixedError(ProductContractDTO productContractDTO) {
        ActionError error =
                new ActionError("InvoicePosition.PartialFixedContract.generalOpenAmountError");

        if (null != productContractDTO.get("contractNumber")
                && !"".equals(productContractDTO.get("contractNumber").toString().trim())) {
            error = new ActionError("InvoicePosition.PartialFixedContract.OpenAmountError",
                    productContractDTO.get("contractNumber"));
        }

        return error;
    }

    private BigDecimal sumPaymentSteps(List<PaymentStepDTO> paymentStepDTOs, ProductContractDTO productContractDTO) {
        return InvoicePositionCommon.i.sumPaymentStepsAmount(paymentStepDTOs,
                null,
                productContractDTO);
    }

    private boolean isCreditNote(InvoiceDTO invoiceDTO) {
        return FinanceConstants.InvoiceType.CreditNote.equal((Integer) invoiceDTO.get("type"));
    }

    private boolean isInvoice(InvoiceDTO invoiceDTO) {
        return FinanceConstants.InvoiceType.Invoice.equal((Integer) invoiceDTO.get("type"));
    }

    private boolean isPeriodicContract(ProductContractDTO productContractDTO) {
        return SalesConstants.PayMethod.Periodic.equal((Integer) productContractDTO.get("payMethod"));
    }

    private boolean isPartialPeriodicContract(ProductContractDTO productContractDTO) {
        return SalesConstants.PayMethod.PartialPeriodic.equal((Integer) productContractDTO.get("payMethod"));
    }

    private boolean isPartialFixedContract(ProductContractDTO productContractDTO) {
        return SalesConstants.PayMethod.PartialFixed.equal((Integer) productContractDTO.get("payMethod"));
    }
}
