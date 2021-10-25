package com.piramide.elwis.web.financemanager.util;

import com.piramide.elwis.dto.financemanager.InvoicePositionDTO;
import com.piramide.elwis.dto.salesmanager.PaymentStepDTO;
import com.piramide.elwis.dto.salesmanager.ProductContractDTO;
import com.piramide.elwis.dto.salesmanager.SalePositionDTO;
import com.piramide.elwis.utils.BigDecimalUtils;
import static com.piramide.elwis.web.financemanager.el.Functions.getInvoicePositionsByContract;
import static com.piramide.elwis.web.financemanager.util.InvoicePositionCommon.i;
import static com.piramide.elwis.web.salesmanager.el.Functions.getPaymentSteps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.3
 */
public class InvoicePositionDiscountValueCalculator {
    private Log log = LogFactory.getLog(InvoicePositionDiscountValueCalculator.class);

    private final BigDecimal NEGATIVE_ONE = new BigDecimal("-1.00");

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100.00");

    private HttpServletRequest request;

    private BigDecimal suggestedPrice;

    private BigDecimal suggestedDiscountValue;

    private BigDecimal paymentStepPrice;

    private BigDecimal contractDiscount = null;

    private BigDecimal theoreticalDiscountValue;

    private BigDecimal theoreticalInvoicePrice;

    private BigDecimal invoicePriceLimit;

    private ProductContractDTO contract;

    private SalePositionDTO salePosition;

    private List<InvoicePositionDTO> invoicePositions = new ArrayList<InvoicePositionDTO>();

    private List<PaymentStepDTO> paymentSteps = new ArrayList<PaymentStepDTO>();

    private Integer invoicePositionId;

    private Integer payStepId;

    public InvoicePositionDiscountValueCalculator(ProductContractDTO contractDTO,
                                                  SalePositionDTO salePositionDTO,
                                                  Integer invoicePositionId,
                                                  Integer payStepId,
                                                  BigDecimal invoicePositionUnitPrice,
                                                  BigDecimal invoicePositionQuantity,
                                                  BigDecimal paymentStepPrice,
                                                  HttpServletRequest request) {
        this.request = request;

        this.contract = contractDTO;
        this.salePosition = salePositionDTO;
        this.invoicePositions = getInvoicePositionsByContract((Integer) contract.get("contractId"), request);

        if (i.isPartialFixedContract(contract)) {
            paymentSteps = getPaymentSteps(contract.get("contractId").toString(), request);
        }

        this.contractDiscount = (BigDecimal) contractDTO.get("discount");

        this.paymentStepPrice = paymentStepPrice;

        this.invoicePositionId = invoicePositionId;
        this.payStepId = payStepId;

        setTheoreticalDiscountValue();
        setInvoicePriceLimit();
        setTheoreticalInvoicePrice();

        BigDecimal unitPrice = invoicePositionUnitPrice;
        if (null == invoicePositionUnitPrice) {
            unitPrice = theoreticalInvoicePrice;
        }

        if (i.isGreaterThan(theoreticalInvoicePrice, BigDecimal.ZERO)) {
            buildSuggestedValues(unitPrice, invoicePositionQuantity);
        }
    }

    public BigDecimal getDiscountValue(BigDecimal invoicePrice) {
        BigDecimal percentageOfInvoicePrice = getPercentageOfInvoicePrice(invoicePrice);

        return BigDecimalUtils.divide(
                percentageOfInvoicePrice.multiply(getTheoreticalDiscountValue()),
                ONE_HUNDRED);
    }

    public static BigDecimal getDiscountValue(BigDecimal invoicePrice,
                                              BigDecimal discountValueSuggested,
                                              BigDecimal limit) {
        BigDecimal percentageOfInvoicePrice = getPercentageOfInvoicePrice(invoicePrice, limit);

        return BigDecimalUtils.divide(
                percentageOfInvoicePrice.multiply(discountValueSuggested),
                ONE_HUNDRED);
    }


    public void modifyOpenAmountForUpdate(BigDecimal oldTotalPriceOfInvoice,
                                          BigDecimal oldDiscountValue,
                                          BigDecimal invoicePositionUnitPrice,
                                          BigDecimal invoicePositionQuantity) {
        BigDecimal actualOpenAmount = (BigDecimal) contract.get("openAmount");

        actualOpenAmount = BigDecimalUtils.sum(actualOpenAmount, oldTotalPriceOfInvoice);
        actualOpenAmount = BigDecimalUtils.sum(actualOpenAmount, oldDiscountValue);
        contract.put("openAmount", actualOpenAmount);

        log.debug("---------------------- UPDATE CONTRACT DTO FOR UPDATE ----------------------");
        log.debug(contract);

        setTheoreticalDiscountValue();
        setTheoreticalInvoicePrice();
        setInvoicePriceLimit();

        BigDecimal unitPrice = invoicePositionUnitPrice;
        if (null == invoicePositionUnitPrice) {
            unitPrice = theoreticalInvoicePrice;
        }

        if (i.isGreaterThan(unitPrice, BigDecimal.ZERO)) {
            buildSuggestedValues(unitPrice, invoicePositionQuantity);
        }
    }

    public boolean isTheLastPayment(BigDecimal invoicePositionUnitPrice,
                                    BigDecimal invoicePositionQuantity) {

        BigDecimal openAmount = (BigDecimal) contract.get("openAmount");
        BigDecimal price = (BigDecimal) contract.get("price");

        BigDecimal totalPaid = i.calculateTotalPaid(invoicePositionId, invoicePositions, request);

        BigDecimal realPayment;
        BigDecimal formDiscountValue;
        if (i.isPartialFixedContract(contract)) {
            formDiscountValue = this.getDiscountValue(invoicePositionUnitPrice);

            realPayment = BigDecimalUtils.sum(
                    BigDecimalUtils.multiply(invoicePositionUnitPrice, invoicePositionQuantity),
                    formDiscountValue);

            if (i.usePercentageInPayments(contract)) {
                BigDecimal realPaymentPercentage = BigDecimalUtils.divide(
                        BigDecimalUtils.multiply(realPayment, ONE_HUNDRED),
                        price);

                BigDecimal openAmountPercentage = BigDecimalUtils.divide(
                        BigDecimalUtils.multiply((BigDecimal) contract.get("openAmount"), ONE_HUNDRED),
                        price
                );

                return i.isEqual(openAmountPercentage, realPaymentPercentage);
            }

            if (i.isEqual(price, BigDecimalUtils.sum(totalPaid, realPayment))) {
                return true;
            } else {
                int invoicesPayments = countInvoicedPayments(paymentSteps, payStepId);

                Integer installment = (Integer) contract.get("installment");

                if ((installment - invoicesPayments) == 1) {
                    return true;
                }
            }
        }

        BigDecimal formTotalPrice = BigDecimalUtils.multiply(invoicePositionUnitPrice, invoicePositionQuantity);

        if (i.isSingleContract(contract)) {
            formDiscountValue = this.getDiscountValue(formTotalPrice);

            realPayment = BigDecimalUtils.sum(formTotalPrice, formDiscountValue);
            if (i.isEqual(formTotalPrice, getInvoicePriceLimit())) {
                return true;
            }
        } else {
            formDiscountValue = this.getDiscountValue(invoicePositionUnitPrice);
            realPayment = BigDecimalUtils.sum(
                    BigDecimalUtils.multiply(invoicePositionUnitPrice, invoicePositionQuantity),
                    formDiscountValue);
        }

        if (i.isEqual(openAmount, realPayment)) {
            return true;
        }

        if (i.isEqual(price, realPayment)) {
            return true;
        }

        if (i.isEqual(price, BigDecimalUtils.sum(totalPaid, realPayment))) {
            return true;
        }

        return false;
    }


    public BigDecimal getTheoreticalDiscountValue() {
        if (i.isSingleContract(contract)) {
            BigDecimal salePositionQuantity = (BigDecimal) salePosition.get("quantity");
            return BigDecimalUtils.multiply(theoreticalDiscountValue, salePositionQuantity);
        }

        return theoreticalDiscountValue;
    }

    public BigDecimal getTheoreticalInvoicePrice() {
        return theoreticalInvoicePrice;
    }


    public BigDecimal getInvoicePriceLimit() {
        return invoicePriceLimit;
    }

    public BigDecimal getContractDiscount() {
        return contractDiscount;
    }

    public void showInformation() {
        log.debug("-------------------- INVOICE POSITION CALCULATOR --------------------");
        log.debug("Contract open amount    : " + contract.get("openAmount"));
        log.debug("Contract discount       : " + contractDiscount);
        log.debug("Payment step price      : " + paymentStepPrice);

        log.debug("Theoretical invoice position price  : " + theoreticalInvoicePrice);
        log.debug("Theoretical discount value          : " + getTheoreticalDiscountValue());

        log.debug("Invoice position price suggested          : " + suggestedPrice);
        log.debug("Invoice position discount value suggested : " + suggestedDiscountValue);
        log.debug("Invoice position price limit              : " + getInvoicePriceLimit());
        log.debug("---------------------------------------------------------------------");
    }

    public BigDecimal getSuggestedPrice() {
        return suggestedPrice;
    }

    public BigDecimal getSuggestedDiscountValue() {
        return suggestedDiscountValue;
    }

    private int countInvoicedPayments(List<PaymentStepDTO> paymentSteps,
                                      Integer excludedPaymentStepId) {
        int counter = 0;
        for (PaymentStepDTO dto : paymentSteps) {
            Integer payStepId = (Integer) dto.get("payStepId");
            if (null != excludedPaymentStepId && payStepId.equals(excludedPaymentStepId)) {
                continue;
            }

            Boolean hasInvoicePosition = (Boolean) dto.get("hasInvoicePosition");
            if (hasInvoicePosition) {
                counter++;
            }
        }

        return counter;
    }

    private void setTheoreticalDiscountValue() {
        if (null == contractDiscount) {
            theoreticalDiscountValue = BigDecimal.ZERO;
        } else {
            if (i.isSingleContract(contract)) {
                BigDecimal salePositionQuantity = (BigDecimal) salePosition.get("quantity");
                BigDecimal openAmount = (BigDecimal) contract.get("openAmount");

                BigDecimal singlePayment = BigDecimalUtils.divide(openAmount, salePositionQuantity);

                theoreticalDiscountValue = BigDecimalUtils.getPercentage(singlePayment, contractDiscount);
            } else if (i.isPartialFixedContract(contract) && null != paymentStepPrice) {
                theoreticalDiscountValue = BigDecimalUtils.getPercentage(paymentStepPrice, contractDiscount);
            } else if (i.isPeriodicContract(contract)) {
                BigDecimal contractPrice = (BigDecimal) contract.get("price");

                theoreticalDiscountValue = BigDecimalUtils.getPercentage(contractPrice, contractDiscount);
            } else {
                BigDecimal openAmount = (BigDecimal) contract.get("openAmount");

                theoreticalDiscountValue = BigDecimalUtils.getPercentage(openAmount, contractDiscount);
            }
        }
    }

    private void setTheoreticalInvoicePrice() {
        if (i.isSingleContract(contract)) {
            BigDecimal salePositionQuantity = (BigDecimal) salePosition.get("quantity");
            BigDecimal openAmount = (BigDecimal) contract.get("openAmount");

            BigDecimal singlePayment = BigDecimalUtils.divide(openAmount, salePositionQuantity);

            theoreticalInvoicePrice = BigDecimalUtils.subtract(singlePayment, theoreticalDiscountValue);
        } else if (i.isPartialFixedContract(contract) && null != paymentStepPrice) {
            BigDecimal value = paymentStepPrice;

            theoreticalInvoicePrice = BigDecimalUtils.subtract(value, theoreticalDiscountValue);
        } else if (i.isPeriodicContract(contract)) {
            BigDecimal contractPrice = (BigDecimal) contract.get("price");
            theoreticalInvoicePrice = BigDecimalUtils.subtract(contractPrice, theoreticalDiscountValue);
        } else {
            BigDecimal openAmount = (BigDecimal) contract.get("openAmount");
            theoreticalInvoicePrice = BigDecimalUtils.subtract(openAmount, theoreticalDiscountValue);
        }
    }

    private void setInvoicePriceLimit() {
        if (i.isSingleContract(contract)) {
            BigDecimal salePositionQuantity = (BigDecimal) salePosition.get("quantity");
            BigDecimal openAmount = (BigDecimal) contract.get("openAmount");

            BigDecimal singlePayment = BigDecimalUtils.divide(openAmount, salePositionQuantity);

            BigDecimal realPayment = singlePayment;
            if (null != contractDiscount) {
                BigDecimal realDiscount = BigDecimalUtils.getPercentage(singlePayment, contractDiscount);
                realPayment = BigDecimalUtils.subtract(realPayment, realDiscount);
            }

            invoicePriceLimit = BigDecimalUtils.multiply(realPayment, salePositionQuantity);
        } else if (i.isPeriodicContract(contract)) {
            BigDecimal contractPrice = (BigDecimal) contract.get("price");
            if (null == contractDiscount) {
                invoicePriceLimit = contractPrice;
            } else {
                BigDecimal realDiscount = BigDecimalUtils.getPercentage(contractPrice, contractDiscount);
                invoicePriceLimit = BigDecimalUtils.subtract(contractPrice, realDiscount);
            }
        } else if (i.isPartialFixedContract(contract) && null != paymentStepPrice) {
            BigDecimal price = (BigDecimal) contract.get("price");
            BigDecimal openAmount = (BigDecimal) contract.get("openAmount");

            BigDecimal realPayment;
            if (i.usePercentageInPayments(contract)) {
                BigDecimal openAmountPercentage = BigDecimalUtils.divide(
                        BigDecimalUtils.multiply(openAmount, ONE_HUNDRED),
                        price
                );

                BigDecimal paymentStepPercentage = BigDecimalUtils.divide(
                        BigDecimalUtils.multiply(paymentStepPrice, ONE_HUNDRED),
                        price
                );

                if (i.isEqual(openAmountPercentage, paymentStepPercentage)) {
                    realPayment = paymentStepPrice;
                } else {
                    realPayment = openAmount;
                }
            } else {
                realPayment = openAmount;
            }

            if (null != contractDiscount) {
                BigDecimal realDiscount = BigDecimalUtils.getPercentage(realPayment, contractDiscount);
                realPayment = BigDecimalUtils.subtract(realPayment, realDiscount);
            }

            invoicePriceLimit = realPayment;
        } else {
            BigDecimal openAmount = (BigDecimal) contract.get("openAmount");
            BigDecimal realPayment = openAmount;

            if (null != contractDiscount) {
                BigDecimal realDiscount = BigDecimalUtils.getPercentage(openAmount, contractDiscount);
                realPayment = BigDecimalUtils.subtract(realPayment, realDiscount);
            }

            invoicePriceLimit = realPayment;
        }
    }

    private void buildSuggestedValues(BigDecimal invoicePositionUnitPrice,
                                      BigDecimal invoicePositionQuantity) {

        BigDecimal price = (BigDecimal) contract.get("price");
        BigDecimal discount = (BigDecimal) contract.get("discount");

        suggestedPrice = this.getTheoreticalInvoicePrice();
        suggestedDiscountValue = this.getTheoreticalDiscountValue();

        if (isTheLastPayment(invoicePositionUnitPrice, invoicePositionQuantity)) {
            log.debug("Process the last payment...");

            BigDecimal realSuggestedPaid;
            if (i.isSingleContract(contract)) {
                realSuggestedPaid = BigDecimalUtils.sum(BigDecimalUtils.multiply(suggestedPrice, invoicePositionQuantity), suggestedDiscountValue);

            } else {
                realSuggestedPaid = BigDecimalUtils.sum(suggestedPrice, suggestedDiscountValue);
            }

            BigDecimal totalPaid = BigDecimalUtils.sum(
                    i.calculateTotalPaid(invoicePositionId, invoicePositions, request),
                    realSuggestedPaid);

            BigDecimal res = BigDecimalUtils.subtract(price, totalPaid);
            if (null != discount) {
                suggestedDiscountValue = BigDecimalUtils.sum(suggestedDiscountValue, res);
            } else {
                suggestedPrice = BigDecimalUtils.sum(suggestedPrice, res);
            }
        } else {
            suggestedPrice = this.getTheoreticalInvoicePrice();
            suggestedDiscountValue = this.getTheoreticalDiscountValue();
        }
    }

    private static BigDecimal getPercentageOfInvoicePrice(BigDecimal invoicePrice, BigDecimal limit) {
        return BigDecimalUtils.divide(BigDecimalUtils.multiply(invoicePrice, ONE_HUNDRED), limit);
    }

    private BigDecimal getPercentageOfInvoicePrice(BigDecimal invoicePrice) {
        if (i.isPartialFixedContract(contract)) {
            return BigDecimalUtils.divide(BigDecimalUtils.multiply(invoicePrice, ONE_HUNDRED), theoreticalInvoicePrice);
        }
        return BigDecimalUtils.divide(BigDecimalUtils.multiply(invoicePrice, ONE_HUNDRED), invoicePriceLimit);
    }
}
