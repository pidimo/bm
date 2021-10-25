package com.piramide.elwis.cmd.salesmanager.util;

import com.piramide.elwis.domain.salesmanager.PaymentStep;

import java.math.BigDecimal;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: ContractInvoiceData.java 16-ene-2009 16:42:07 $
 */
public class ContractInvoiceData {
    private Integer invoicedFrom;
    private Integer invoicedUntil;
    private Integer periodicMonths;

    private BigDecimal invoicePrice;
    private BigDecimal unitPriceDiscounted;
    private BigDecimal discountValue;

    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal contractPrice;
    private PaymentStep paymentStepInvoiced;
    private BigDecimal pricePerMonth;

    public ContractInvoiceData() {
        this.unitPriceDiscounted = BigDecimal.ZERO;
        this.periodicMonths = new Integer(0);
    }

    public Integer getInvoicedFrom() {
        return invoicedFrom;
    }

    public void setInvoicedFrom(Integer invoicedFrom) {
        this.invoicedFrom = invoicedFrom;
    }

    public Integer getInvoicedUntil() {
        return invoicedUntil;
    }

    public void setInvoicedUntil(Integer invoicedUntil) {
        this.invoicedUntil = invoicedUntil;
    }

    public Integer getPeriodicMonths() {
        return periodicMonths;
    }

    public void setPeriodicMonths(Integer periodicMonths) {
        this.periodicMonths = periodicMonths;
    }

    public BigDecimal getInvoicePrice() {
        return invoicePrice;
    }

    public void setInvoicePrice(BigDecimal invoicePrice) {
        this.invoicePrice = invoicePrice;
    }

    public BigDecimal getUnitPriceDiscounted() {
        return unitPriceDiscounted;
    }

    public void setUnitPriceDiscounted(BigDecimal unitPriceDiscounted) {
        this.unitPriceDiscounted = unitPriceDiscounted;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(BigDecimal contractPrice) {
        this.contractPrice = contractPrice;
    }

    public PaymentStep getPaymentStepInvoiced() {
        return paymentStepInvoiced;
    }

    public void setPaymentStepInvoiced(PaymentStep paymentStepInvoiced) {
        this.paymentStepInvoiced = paymentStepInvoiced;
    }

    public BigDecimal getPricePerMonth() {
        return pricePerMonth;
    }

    public void setPricePerMonth(BigDecimal pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }
}
