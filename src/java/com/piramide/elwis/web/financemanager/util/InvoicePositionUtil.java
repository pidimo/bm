package com.piramide.elwis.web.financemanager.util;

import com.piramide.elwis.dto.financemanager.InvoicePositionDTO;
import com.piramide.elwis.dto.salesmanager.ProductContractDTO;
import com.piramide.elwis.dto.salesmanager.SalePositionDTO;
import com.piramide.elwis.utils.BigDecimalUtils;
import static com.piramide.elwis.web.financemanager.el.Functions.getInvoicePositionsByContract;
import static com.piramide.elwis.web.financemanager.util.InvoicePositionCommon.i;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.3
 */
public class InvoicePositionUtil {
    private static Log log = LogFactory.getLog(InvoicePositionUtil.class);

    private HttpServletRequest request;

    private ProductContractDTO contract;

    private SalePositionDTO salePosition;

    private List<InvoicePositionDTO> invoicePositions;

    private BigDecimal invoicePositionUnitPrice;

    private BigDecimal invoicePositionQuantity;

    private BigDecimal oldTotalPriceOfInvoice;

    private BigDecimal oldDiscountValueOfInvoice;

    private BigDecimal invoicePositionDiscountedValue;

    private BigDecimal remnant = BigDecimal.ZERO;

    private InvoicePositionDiscountValueCalculator invoicePositionDiscountValueCalculator;

    public InvoicePositionUtil(SalePositionDTO salePositionDTO,
                               ProductContractDTO contract,
                               Integer invoicePositionId,
                               Integer payStepId,
                               BigDecimal paymentStepPrice,
                               BigDecimal invoicePositionUnitPrice,
                               BigDecimal invoicePositionQuantity,
                               BigDecimal oldTotalPriceOfInvoice,
                               BigDecimal oldDiscountValueOfInvoice,
                               HttpServletRequest request) {

        this.request = request;

        this.contract = contract;
        this.salePosition = salePositionDTO;

        this.invoicePositions = getInvoicePositionsByContract((Integer) contract.get("contractId"), request);

        this.invoicePositionUnitPrice = invoicePositionUnitPrice;
        this.invoicePositionQuantity = invoicePositionQuantity;

        this.oldTotalPriceOfInvoice = oldTotalPriceOfInvoice;
        this.oldDiscountValueOfInvoice = oldDiscountValueOfInvoice;

        invoicePositionDiscountValueCalculator =
                new InvoicePositionDiscountValueCalculator(
                        contract,
                        salePositionDTO,
                        invoicePositionId,
                        payStepId,
                        invoicePositionUnitPrice,
                        invoicePositionQuantity,
                        paymentStepPrice,
                        request);

        if (null != invoicePositionId) {
            if (null == oldTotalPriceOfInvoice) {
                oldTotalPriceOfInvoice = BigDecimal.ZERO;
            }
            if (null == oldDiscountValueOfInvoice) {
                oldDiscountValueOfInvoice = BigDecimal.ZERO;
            }

            invoicePositionDiscountValueCalculator.modifyOpenAmountForUpdate(
                    oldTotalPriceOfInvoice,
                    oldDiscountValueOfInvoice,
                    invoicePositionUnitPrice,
                    invoicePositionQuantity);
        }

        if (i.isSingleContract(contract)) {
            invoicePositionDiscountedValue = invoicePositionDiscountValueCalculator.getDiscountValue(getInvoicePositionTotalPrice());
        } else if (i.isPartialFixedContract(contract)) {
            invoicePositionDiscountedValue =
                    invoicePositionDiscountValueCalculator.getDiscountValue(invoicePositionUnitPrice);
        } else {
            invoicePositionDiscountedValue =
                    invoicePositionDiscountValueCalculator.getDiscountValue(getInvoicePositionTotalPrice());
        }

        invoicePositionDiscountValueCalculator.showInformation();
    }

    public boolean isTheLastPayment() {
        return invoicePositionDiscountValueCalculator.isTheLastPayment(invoicePositionUnitPrice, invoicePositionQuantity);
    }

    public BigDecimal getRemnant(Integer excludedInvoicePositionId) {
        BigDecimal totalPaid = i.calculateTotalPaid(excludedInvoicePositionId, invoicePositions, request);

        BigDecimal formPaid = BigDecimalUtils.sum(
                getTotalPrice(invoicePositionUnitPrice, invoicePositionQuantity),
                invoicePositionDiscountedValue);

        BigDecimal invoicePositionTotalPaid = BigDecimalUtils.sum(totalPaid,
                formPaid);

        BigDecimal totalPaidFromContract = getTotalPaidFromContract();
        BigDecimal totalDiscountFromContract = getTotalDiscountFromContract();

        BigDecimal contractTotalPaid;

        if (i.isSingleContract(contract)) {
            contractTotalPaid = (BigDecimal) contract.get("price");
        } else {
            contractTotalPaid = BigDecimalUtils.sum(totalPaidFromContract, totalDiscountFromContract);
        }

        this.remnant = BigDecimalUtils.subtract(contractTotalPaid, invoicePositionTotalPaid);

        log.debug("-------------------------- REMANANT CALCULATION --------------------------");
        log.debug("total paid from invoicePositions     : " + invoicePositionTotalPaid);
        log.debug("Total paid from contract             : " + contractTotalPaid);
        log.debug("The remnant : " + remnant);
        log.debug("--------------------------------------------------------------------------");
        return remnant;
    }


    private BigDecimal getTotalPaidFromContract() {
        BigDecimal contractPrice = (BigDecimal) contract.get("price");
        BigDecimal contractDiscount = (BigDecimal) contract.get("discount");

        if (i.isSingleContract(contract)) {
            BigDecimal salePositionQuantity = (BigDecimal) salePosition.get("quantity");

            BigDecimal singlePayment = BigDecimalUtils.divide(contractPrice, salePositionQuantity);
            BigDecimal realPayment = singlePayment;
            if (null != contractDiscount) {
                BigDecimal realDiscount = BigDecimalUtils.getPercentage(singlePayment, contractDiscount);
                realPayment = BigDecimalUtils.subtract(realPayment, realDiscount);
            }

            return BigDecimalUtils.multiply(realPayment, salePositionQuantity);

        } else {
            BigDecimal realPayment = contractPrice;
            if (null != contractDiscount) {
                BigDecimal realDiscount = BigDecimalUtils.getPercentage(contractPrice, contractDiscount);
                realPayment = BigDecimalUtils.subtract(contractPrice, realDiscount);
            }

            return realPayment;
        }
    }

    private BigDecimal getTotalDiscountFromContract() {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal discount = (BigDecimal) contract.get("discount");

        if (null != discount) {
            BigDecimal contractPrice = (BigDecimal) contract.get("price");

            if (i.isSingleContract(contract)) {
                BigDecimal salePositionQuantity = (BigDecimal) salePosition.get("quantity");

                BigDecimal singlePayment = BigDecimalUtils.divide(contractPrice, salePositionQuantity);
                BigDecimal realDiscount = BigDecimalUtils.getPercentage(singlePayment, discount);

                totalDiscount = BigDecimalUtils.multiply(realDiscount, salePositionQuantity);
            } else {
                BigDecimal price = (BigDecimal) contract.get("price");
                totalDiscount = BigDecimalUtils.getPercentage(price, discount);
            }
        }

        return totalDiscount;
    }

    private BigDecimal getTotalPrice(BigDecimal price, BigDecimal quantity) {
        return BigDecimalUtils.multiply(price, quantity);
    }

    public InvoicePositionDiscountValueCalculator getInvoicePositionDiscountValueCalculator() {
        return invoicePositionDiscountValueCalculator;
    }

    public BigDecimal getInvoicePositionTotalPrice() {
        return getTotalPrice(invoicePositionUnitPrice, invoicePositionQuantity);
    }

    public BigDecimal getInvoicePositionDiscountedValue() {
        return invoicePositionDiscountedValue;
    }

    public BigDecimal getRemnant() {
        return remnant;
    }

    public void showInformation() {
        log.debug("----------------------INVOICE POSITION UTIL INFORMATION----------------------");
        log.debug("Invoice position unit price : " + invoicePositionUnitPrice);
        log.debug("Invoice position quantity   : " + invoicePositionQuantity);
        log.debug("Invoice position total price: " + getInvoicePositionTotalPrice());
        log.debug("Discount value for " + getInvoicePositionTotalPrice() + " : " + invoicePositionDiscountedValue);
        log.debug("-----------------------------------------------------------------------------");

    }

    public BigDecimal getInvoicePositionUnitPrice() {
        return invoicePositionUnitPrice;
    }

    public BigDecimal getInvoicePositionQuantity() {
        return invoicePositionQuantity;
    }

    public BigDecimal getOldTotalPriceOfInvoice() {
        return oldTotalPriceOfInvoice;
    }

    public BigDecimal getOldDiscountValueOfInvoice() {
        return oldDiscountValueOfInvoice;
    }
}
