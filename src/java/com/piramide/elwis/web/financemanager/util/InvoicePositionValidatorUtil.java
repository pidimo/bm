package com.piramide.elwis.web.financemanager.util;

import com.piramide.elwis.cmd.salesmanager.util.PayMethodUtil;
import com.piramide.elwis.dto.financemanager.InvoicePositionDTO;
import com.piramide.elwis.dto.salesmanager.PaymentStepDTO;
import com.piramide.elwis.dto.salesmanager.ProductContractDTO;
import com.piramide.elwis.dto.salesmanager.SalePositionDTO;
import com.piramide.elwis.utils.BigDecimalUtils;
import static com.piramide.elwis.utils.SalesConstants.PayMethod.Periodic;
import com.piramide.elwis.web.common.util.FormatUtils;
import static com.piramide.elwis.web.financemanager.util.InvoicePositionCommon.i;
import com.piramide.elwis.web.salesmanager.el.Functions;
import static com.piramide.elwis.web.salesmanager.el.Functions.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.3
 */
public class InvoicePositionValidatorUtil {
    private final BigDecimal ONE_HUNDRED = new BigDecimal("100.00");
    private final BigDecimal NEGATIVE_ONE = new BigDecimal("-1.00");

    private static Log log = LogFactory.getLog(InvoicePositionValidatorUtil.class);

    private BigDecimal creditNoteDiscountValue = BigDecimal.ZERO;

    public ActionError validateSingleContract(ProductContractDTO productContractDTO,
                                              InvoicePositionUtil invoicePositionUtil,
                                              Integer invoicePositionId,
                                              HttpServletRequest request) {

        BigDecimal formTotalPrice = invoicePositionUtil.getInvoicePositionTotalPrice();

        BigDecimal invoicePriceLimit = invoicePositionUtil.getInvoicePositionDiscountValueCalculator().getInvoicePriceLimit();

        if (i.isGreaterThan(formTotalPrice, invoicePriceLimit)) {
            return new ActionError("InvoicePosition.productContract.totalPriceError",
                    formatDecimalNumber(invoicePriceLimit, request));
        }

        ActionError openAmountGeneratedError = validateUnitPriceForSingleContract(
                productContractDTO,
                invoicePositionUtil.getInvoicePositionUnitPrice(),
                invoicePositionUtil.getInvoicePositionQuantity(),
                invoicePositionUtil.getInvoicePositionDiscountedValue(),
                request);

        if (null != openAmountGeneratedError) {
            return openAmountGeneratedError;
        }

        if (invoicePositionUtil.isTheLastPayment()) {
            BigDecimal remanant = invoicePositionUtil.getRemnant(invoicePositionId);

            BigDecimal newUnitPrice = BigDecimalUtils.sum(invoicePositionUtil.getInvoicePositionUnitPrice(), remanant);

            BigDecimal discount = (BigDecimal) productContractDTO.get("discount");
            if (null == discount && BigDecimal.ZERO.compareTo(remanant) != 0) {
                return new ActionError("InvoicePosition.productContract.unitPriceUpdate",
                        formatDecimalNumber(newUnitPrice, request));
            }
        }

        return null;
    }


    private ActionError validateUnitPriceForSingleContract(ProductContractDTO productContractDTO,
                                                           BigDecimal invoicePositionUnitPrice,
                                                           BigDecimal invoicePositionQuantity,
                                                           BigDecimal invoicePositionDiscountValue,
                                                           HttpServletRequest request) {
        BigDecimal openAmount = (BigDecimal) productContractDTO.get("openAmount");
        BigDecimal payValue = BigDecimalUtils.sum(
                BigDecimalUtils.multiply(invoicePositionUnitPrice, invoicePositionQuantity),
                invoicePositionDiscountValue
        );

        BigDecimal newOpenAmount = BigDecimalUtils.subtract(openAmount, payValue);

        if (newOpenAmount.compareTo(BigDecimal.ZERO) == -1) {
            return null;
        }

        SalePositionDTO salePosition = Functions.getSalePositionDTO(
                productContractDTO.get("salePositionId").toString(),
                request);

        if (!PayMethodUtil.isValidSingleOpenAmount(newOpenAmount, (BigDecimal) salePosition.get("quantity"))) {
            return new ActionError("InvoicePosition.SingleContract.openAmountGeneratedError");
        }

        return null;
    }

    private ActionError validateUnitPriceForSingleContractCreditNote(ProductContractDTO productContractDTO,
                                                                     BigDecimal invoicePositionUnitPrice,
                                                                     BigDecimal invoicePositionQuantity,
                                                                     BigDecimal invoicePositionDiscountValue,
                                                                     BigDecimal oldTotalPrice,
                                                                     BigDecimal oldDiscountValue,
                                                                     String formOperation,
                                                                     HttpServletRequest request) {
        BigDecimal openAmount = (BigDecimal) productContractDTO.get("openAmount");

        BigDecimal payValue = BigDecimalUtils.sum(
                BigDecimalUtils.multiply(invoicePositionUnitPrice, invoicePositionQuantity),
                invoicePositionDiscountValue
        );

        if ("update".equals(formOperation)) {
            BigDecimal oldPayValue = BigDecimalUtils.sum(oldTotalPrice, oldDiscountValue);
            openAmount = BigDecimalUtils.subtract(openAmount, oldPayValue);
        }

        BigDecimal newOpenAmount = BigDecimalUtils.sum(openAmount, payValue);
        SalePositionDTO salePosition = Functions.getSalePositionDTO(
                productContractDTO.get("salePositionId").toString(),
                request);

        if (!PayMethodUtil.isValidSingleOpenAmount(newOpenAmount, (BigDecimal) salePosition.get("quantity"))) {
            return new ActionError("InvoicePosition.SingleContract.openAmountGeneratedError");
        }

        return null;
    }

    public ActionError validatePeriodicContract(InvoicePositionUtil invoicePositionUtil,
                                                HttpServletRequest request) {

        BigDecimal formPrice = invoicePositionUtil.getInvoicePositionUnitPrice();
        BigDecimal priceToInvoiced = invoicePositionUtil.getInvoicePositionDiscountValueCalculator().getInvoicePriceLimit();

        if (i.isGreaterThan(formPrice, priceToInvoiced)) {
            return new ActionError("InvoicePosition.periodicProductContract.totalPriceError",
                    formatDecimalNumber(priceToInvoiced, request));
        }

        return null;
    }

    public ActionError validatePartialPeriodicContract(ProductContractDTO productContractDTO,
                                                       InvoicePositionUtil invoicePositionUtil,
                                                       Integer invoicePositionId,
                                                       HttpServletRequest request) {

        BigDecimal formTotalPrice = invoicePositionUtil.getInvoicePositionTotalPrice();
        BigDecimal invoicePriceLimit = invoicePositionUtil.getInvoicePositionDiscountValueCalculator().getInvoicePriceLimit();

        if (i.isGreaterThan(formTotalPrice, invoicePriceLimit)) {
            return new ActionError("InvoicePosition.productContract.totalPriceError",
                    formatDecimalNumber(invoicePriceLimit, request));
        }

        if (invoicePositionUtil.isTheLastPayment()) {
            BigDecimal remanant = invoicePositionUtil.getRemnant(invoicePositionId);

            BigDecimal newUnitPrice = BigDecimalUtils.sum(invoicePositionUtil.getInvoicePositionUnitPrice(), remanant);

            BigDecimal discount = (BigDecimal) productContractDTO.get("discount");
            if (null == discount && BigDecimal.ZERO.compareTo(remanant) != 0) {
                return new ActionError("InvoicePosition.productContract.unitPriceUpdate",
                        formatDecimalNumber(newUnitPrice, request));
            }
        }

        return null;
    }

    public ActionError validatePartialFixedContract(ProductContractDTO contractDTO,
                                                    PaymentStepDTO paymentStepDTO,
                                                    InvoicePositionUtil invoicePositionUtil,
                                                    Integer invoicePositionId,
                                                    HttpServletRequest request) {

        BigDecimal formTotalPrice = invoicePositionUtil.getInvoicePositionTotalPrice();

        BigDecimal discounValue = invoicePositionUtil.getInvoicePositionDiscountedValue();

        BigDecimal discount = (BigDecimal) contractDTO.get("discount");

        if (invoicePositionUtil.isTheLastPayment()) {

            if (existsAvailablePayments(
                    (Integer) contractDTO.get("contractId"),
                    (Integer) paymentStepDTO.get("payStepId"),
                    request)) {
                return new ActionError("InvoicePosition.partialFixedContract.priceError");
            }

            BigDecimal remanant = invoicePositionUtil.getRemnant(invoicePositionId);

            if (null == discount) {
                if (!i.isEqual(BigDecimal.ZERO, remanant)) {
                    BigDecimal newUnitPrice = BigDecimalUtils.sum(
                            invoicePositionUtil.getInvoicePositionUnitPrice(),
                            remanant);

                    return new ActionError("InvoicePosition.productContract.unitPriceUpdate",
                            formatDecimalNumber(newUnitPrice, request));
                }
            }
        } else {
            /*BigDecimal paymentStepPrice = i.getPaymentStepAmount(contractDTO, paymentStepDTO);*/

            /*if (null != discount) {
                BigDecimal paymentDiscount = i.applyPercentage(paymentStepPrice, discount);
                realPaymentPrice = BigDecimalUtils.subtract(paymentStepPrice, paymentDiscount);
            }*/

            if (!canIncrementTheUnitPrice(contractDTO, request)) {
                if (i.isGreaterThan(formTotalPrice,
                        invoicePositionUtil.getInvoicePositionDiscountValueCalculator().getSuggestedPrice())) {
                    return new ActionError("InvoicePosition.partialFixedContract.priceError");
                }
            }
        }

        if (i.usePercentageInPayments(contractDTO)) {
            ActionError percentageError = isValidPercentage(
                    contractDTO,
                    formTotalPrice,
                    discounValue,
                    request);
            if (null != percentageError) {
                return percentageError;
            }
        }

        return null;
    }


    private ActionError isValidPercentage(ProductContractDTO productContractDTO,
                                          BigDecimal formUnitPrice,
                                          BigDecimal discountValue,
                                          HttpServletRequest request) {

        BigDecimal formTotalPrice = BigDecimalUtils.sum(formUnitPrice, discountValue);

        BigDecimal contractPrice = (BigDecimal) productContractDTO.get("price");

        BigDecimal formPercentage = i.getPercentage(formTotalPrice, contractPrice);

        BigDecimal contractUnitPrice = i.applyPercentage(contractPrice, formPercentage);

        BigDecimal discount = (BigDecimal) productContractDTO.get("discount");
        if (null != discount) {
            BigDecimal contractDiscountValue = i.applyPercentage(contractUnitPrice, discount);
            contractUnitPrice = BigDecimalUtils.subtract(contractUnitPrice, contractDiscountValue);
        }

        if (contractUnitPrice.compareTo(formUnitPrice) != 0) {
            return new ActionError("InvoicePosition.partialFixedContract.percentageError",
                    formatDecimalNumber(contractUnitPrice, request));
        }

        return null;
    }

    public ActionError validateCreditNoteProductContract(List<InvoicePositionDTO> invoicePositions,
                                                         Integer invoicePositionId,
                                                         String contractId,
                                                         String paymentStepId,
                                                         BigDecimal formPrice,
                                                         BigDecimal formQuantity,
                                                         BigDecimal oldTotalPrice,
                                                         BigDecimal oldDiscount,
                                                         String formOperation,
                                                         HttpServletRequest request) {
        final BigDecimal zero = new BigDecimal(0.0);

        ProductContractDTO productContractDTO = getProductContract(contractId, request);

        BigDecimal total = sumInvoicePositions(contractId, paymentStepId, request);

        BigDecimal limit = getTotalDiscount(invoicePositionId, invoicePositions);


        if (isCreate(formOperation)) {
            if (i.isEqual(BigDecimal.ZERO, total)) {
                return new ActionError("InvoicePosition.productContract.totalPriceZeroError");
            }

            BigDecimal formTotalPrice = getTotalPrice(formPrice, formQuantity);
            BigDecimal result = BigDecimalUtils.sum(total, BigDecimalUtils.multiply(formTotalPrice, new BigDecimal(-1)));

            if (i.isGreaterThan(zero, result)) {
                if (i.isPeriodicContract(productContractDTO)) {
                    BigDecimal priceToInvoiced = getPriceToInvoiced(productContractDTO);

                    return new ActionError("InvoicePosition.periodicProductContract.totalPriceError",
                            formatDecimalNumber(priceToInvoiced, request));
                } else {
                    return new ActionError("InvoicePosition.productContract.totalPriceError",
                            formatDecimalNumber(total, request));
                }
            }

            creditNoteDiscountValue = InvoicePositionDiscountValueCalculator.getDiscountValue(formTotalPrice, limit, total);
        }

        if (isUpdate(formOperation)) {
            BigDecimal newTotal = BigDecimalUtils.sum(total, oldTotalPrice);

            if (i.isEqual(BigDecimal.ZERO, newTotal)) {
                return new ActionError("InvoicePosition.productContract.totalPriceZeroError");
            }

            BigDecimal formTotalPrice = getTotalPrice(formPrice, formQuantity);
            BigDecimal result = BigDecimalUtils.sum(newTotal, BigDecimalUtils.multiply(formTotalPrice, new BigDecimal(-1)));

            if (i.isGreaterThan(zero, result)) {
                if (i.isPeriodicContract(productContractDTO)) {
                    BigDecimal priceToInvoiced = getPriceToInvoiced(productContractDTO);

                    return new ActionError("InvoicePosition.periodicProductContract.totalPriceError",
                            formatDecimalNumber(priceToInvoiced, request));
                } else {
                    return new ActionError("InvoicePosition.productContract.totalPriceError",
                            formatDecimalNumber(newTotal, request));
                }
            }

            creditNoteDiscountValue = InvoicePositionDiscountValueCalculator.getDiscountValue(formTotalPrice, limit, newTotal);
        }

        if (i.isSingleContract(productContractDTO)) {
            ActionError unitPriceError = validateUnitPriceForSingleContractCreditNote(
                    productContractDTO,
                    formPrice,
                    formQuantity,
                    creditNoteDiscountValue,
                    oldTotalPrice,
                    oldDiscount,
                    formOperation,
                    request);

            if (null != unitPriceError) {
                return unitPriceError;
            }
        }

        return null;
    }

    private boolean existsAvailablePayments(Integer contractId, Integer selectedPaymentId, HttpServletRequest request) {

        List<PaymentStepDTO> paymentSteps = getPaymentSteps(contractId.toString(), request);

        for (PaymentStepDTO paymentStepDTO : paymentSteps) {
            Integer payStepId = (Integer) paymentStepDTO.get("payStepId");
            Boolean hasInvoicePosition =
                    (Boolean) paymentStepDTO.get("hasInvoicePosition");

            if (payStepId.equals(selectedPaymentId)) {
                continue;
            }

            if (!hasInvoicePosition) {
                return true;
            }
        }

        return false;
    }

    private boolean canIncrementTheUnitPrice(ProductContractDTO contractDTO, HttpServletRequest request) {
        Integer contractId = (Integer) contractDTO.get("contractId");

        List<PaymentStepDTO> paymentSteps = getPaymentSteps(contractId.toString(), request);
        BigDecimal result = i.sumPaymentStepsAmountAsPercentage(paymentSteps, null, contractDTO);

        log.debug("------------ " + result);
        if (i.isEqual(result, ONE_HUNDRED)) {
            log.debug("aaaaaaaaaaaaaaaaaaaa");
            return false;
        }
        log.debug("bbbbbbbbbbbbbbbbbbbbbb");
        return true;
    }

    private BigDecimal getPriceToInvoiced(ProductContractDTO productContractDTO) {
        BigDecimal discount = (BigDecimal) productContractDTO.get("discount");

        BigDecimal contractPrice;
        if (Periodic.equal((Integer) productContractDTO.get("payMethod"))) {
            contractPrice = (BigDecimal) productContractDTO.get("price");
        } else {
            contractPrice = (BigDecimal) productContractDTO.get("openAmount");
        }

        if (null == discount) {
            return contractPrice;
        }

        BigDecimal netDiscount = BigDecimalUtils.getPercentage(contractPrice, discount);
        return BigDecimalUtils.subtract(contractPrice, netDiscount);
    }

    private BigDecimal getTotalPrice(BigDecimal price, BigDecimal quantity) {
        return BigDecimalUtils.multiply(price, quantity);
    }

    private boolean isCreate(String operation) {
        return "create".equals(operation);
    }

    private boolean isUpdate(String operation) {
        return "update".equals(operation);
    }

    private Object formatDecimalNumber(BigDecimal number, HttpServletRequest request) {
        return FormatUtils.formatingDecimalNumber(number, request, 10, 2);
    }

    private BigDecimal getTotalDiscount(Integer excludedInvoicePositionId, List<InvoicePositionDTO> invoicePositions) {
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
    }

    public BigDecimal getCreditNoteDiscountValue() {
        return creditNoteDiscountValue;
    }
}
