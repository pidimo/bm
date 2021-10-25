package com.piramide.elwis.web.financemanager.util;

import com.piramide.elwis.dto.financemanager.InvoiceDTO;
import com.piramide.elwis.dto.financemanager.InvoicePositionDTO;
import com.piramide.elwis.dto.salesmanager.PaymentStepDTO;
import com.piramide.elwis.dto.salesmanager.ProductContractDTO;
import com.piramide.elwis.utils.BigDecimalUtils;
import com.piramide.elwis.utils.FinanceConstants;
import static com.piramide.elwis.utils.SalesConstants.AmounType.AMOUNT;
import static com.piramide.elwis.utils.SalesConstants.AmounType.PERCENTAGE;
import static com.piramide.elwis.utils.SalesConstants.PayMethod.*;
import com.piramide.elwis.web.financemanager.el.Functions;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ivan Alban
 */
public class InvoicePositionCommon {

    private final BigDecimal NEGATIVE_ONE = new BigDecimal("-1.00");

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100.00");

    public static InvoicePositionCommon i = new InvoicePositionCommon();

    private InvoicePositionCommon() {

    }

    public BigDecimal calculateTotalPaid(Integer excludedInvoicePositionId,
                                         List<InvoicePositionDTO> invoicePositions,
                                         HttpServletRequest request) {
        BigDecimal result = BigDecimal.ZERO;

        for (InvoicePositionDTO invoicePositionDTO : invoicePositions) {

            if (null != excludedInvoicePositionId &&
                    excludedInvoicePositionId.equals(invoicePositionDTO.get("positionId"))) {
                continue;
            }

            BigDecimal invoicePositionPayment = getTotalPrice(invoicePositionDTO, request);
            BigDecimal discountValue = (BigDecimal) invoicePositionDTO.get("discountValue");
            if (null == discountValue) {
                discountValue = BigDecimal.ZERO;
            }

            invoicePositionPayment = BigDecimalUtils.sum(invoicePositionPayment, discountValue);

            Boolean isCreditNote = (Boolean) invoicePositionDTO.get("isCreditNote");

            if (isCreditNote) {
                invoicePositionPayment = BigDecimalUtils.multiply(invoicePositionPayment, NEGATIVE_ONE);
            }

            result = BigDecimalUtils.sum(result, invoicePositionPayment);
        }

        return result;
    }

    public BigDecimal getPaymentStepAmount(ProductContractDTO contractDTO,
                                           PaymentStepDTO paymentStepDTO) {
        BigDecimal paymentStepPrice = null;

        if (useAmountInPayments(contractDTO)) {
            paymentStepPrice = (BigDecimal) paymentStepDTO.get("payAmount");
        }

        if (usePercentageInPayments(contractDTO)) {
            paymentStepPrice = BigDecimalUtils.getPercentage((BigDecimal) contractDTO.get("price"),
                    (BigDecimal) paymentStepDTO.get("payAmount"));
        }

        return paymentStepPrice;
    }

    /*public BigDecimal getTotalDiscount(Integer excludedInvoicePositionId, List<InvoicePositionDTO> invoicePositions) {
        BigDecimal result = BigDecimal.ZERO;
        for (InvoicePositionDTO invoicePositionDTO : invoicePositions) {

            if (null != excludedInvoicePositionId &&
                    excludedInvoicePositionId.equals(invoicePositionDTO.get("positionId"))) {
                continue;
            }

            BigDecimal invoiceDiscountValue = (BigDecimal) invoicePositionDTO.get("discountValue");
            if (null == invoiceDiscountValue) {
                invoiceDiscountValue = BigDecimal.ZERO;
            }

            Boolean isCreditNote = (Boolean) invoicePositionDTO.get("isCreditNote");

            if (isCreditNote) {
                invoiceDiscountValue = BigDecimalUtils.multiply(invoiceDiscountValue, NEGATIVE_ONE);
            }

            result = BigDecimalUtils.sum(result, invoiceDiscountValue);
        }

        return result;
    }*/

    public BigDecimal sumPaymentStepsAmount(List<PaymentStepDTO> paymentSteps,
                                            Integer actualPaymentStepId,
                                            ProductContractDTO productContractDTO) {

        BigDecimal result = new BigDecimal(0.0);

        BigDecimal contractPrice = (BigDecimal) productContractDTO.get("price");
        for (int i = 0; i < paymentSteps.size(); i++) {
            PaymentStepDTO paymentStepDTO = paymentSteps.get(i);

            Integer payStepId = (Integer) paymentStepDTO.get("payStepId");

            if (null != actualPaymentStepId && actualPaymentStepId.equals(payStepId)) {
                continue;
            }

            BigDecimal value = new BigDecimal(0.0);
            if (useAmountInPayments(productContractDTO)) {
                value = (BigDecimal) paymentStepDTO.get("payAmount");
            }

            if (usePercentageInPayments(productContractDTO)) {
                value = applyPercentage(contractPrice, (BigDecimal) paymentStepDTO.get("payAmount"));
            }
            result = BigDecimalUtils.sum(result, value);
        }

        return result;
    }

    public BigDecimal sumPaymentStepsAmountAsPercentage(List<PaymentStepDTO> paymentSteps,
                                                        Integer actualPaymentStepId,
                                                        ProductContractDTO productContractDTO) {
        BigDecimal result = BigDecimal.ZERO;

        BigDecimal contractPrice = (BigDecimal) productContractDTO.get("price");

        for (int i = 0; i < paymentSteps.size(); i++) {
            PaymentStepDTO paymentStepDTO = paymentSteps.get(i);

            Integer payStepId = (Integer) paymentStepDTO.get("payStepId");

            if (null != actualPaymentStepId && actualPaymentStepId.equals(payStepId)) {
                continue;
            }

            BigDecimal percentage = BigDecimal.ZERO;
            if (useAmountInPayments(productContractDTO)) {
                percentage = getPercentage((BigDecimal) paymentStepDTO.get("payAmount"), contractPrice);
            }

            if (usePercentageInPayments(productContractDTO)) {
                percentage = (BigDecimal) paymentStepDTO.get("payAmount");
            }
            result = BigDecimalUtils.sum(result, percentage);
        }

        return result;
    }

    public BigDecimal getPercentage(BigDecimal value, BigDecimal total) {
        return BigDecimalUtils.divide(
                BigDecimalUtils.multiply(value, ONE_HUNDRED),
                total);
    }

    public BigDecimal applyPercentage(BigDecimal value, BigDecimal percentage) {
        return BigDecimalUtils.getPercentage(value, percentage);
    }

    public boolean isEqual(BigDecimal firstValue, BigDecimal secondValue) {
        return firstValue.compareTo(secondValue) == 0;
    }

    public boolean isGreaterThan(BigDecimal firstValue, BigDecimal secondValue) {
        return firstValue.compareTo(secondValue) == 1;
    }

    public boolean isGreaterOrEqualThan(BigDecimal firstValue, BigDecimal secondValue) {
        return firstValue.compareTo(secondValue) >= 0;
    }

    public boolean useNetCalculations(InvoiceDTO invoiceDTO) {
        return FinanceConstants.NetGrossFLag.NET.equal((Integer) invoiceDTO.get("netGross"));
    }

    public boolean useGrossCalculations(InvoiceDTO invoiceDTO) {
        return FinanceConstants.NetGrossFLag.GROSS.equal((Integer) invoiceDTO.get("netGross"));
    }

    public boolean isPeriodicContract(ProductContractDTO contractDTO) {
        return Periodic.equal((Integer) contractDTO.get("payMethod"));
    }

    public boolean isPartialFixedContract(ProductContractDTO contractDTO) {
        return PartialFixed.equal((Integer) contractDTO.get("payMethod"));
    }

    public boolean isSingleContract(ProductContractDTO contractDTO) {
        return Single.equal((Integer) contractDTO.get("payMethod"));
    }

    public boolean isPartialPeriodicContract(ProductContractDTO contractDTO) {
        return PartialPeriodic.equal((Integer) contractDTO.get("payMethod"));
    }

    public boolean usePercentageInPayments(ProductContractDTO contractDTO) {
        return PERCENTAGE.equal((Integer) contractDTO.get("amounType"));
    }

    public boolean useAmountInPayments(ProductContractDTO contractDTO) {
        return AMOUNT.equal((Integer) contractDTO.get("amounType"));
    }

    private BigDecimal getTotalPrice(InvoicePositionDTO invoicePositionDTO,
                                     HttpServletRequest request) {

        String invoiceId = invoicePositionDTO.get("invoiceId").toString();

        InvoiceDTO invoiceDTO = Functions.getInvoice(invoiceId, request);

        BigDecimal totalPrice = BigDecimal.ZERO;
        if (useNetCalculations(invoiceDTO)) {
            totalPrice = (BigDecimal) invoicePositionDTO.get("totalPrice");
        }
        if (useGrossCalculations(invoiceDTO)) {
            totalPrice = (BigDecimal) invoicePositionDTO.get("totalPriceGross");
        }

        return totalPrice;
    }
}
