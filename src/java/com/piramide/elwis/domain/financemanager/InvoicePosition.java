/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;

public interface InvoicePosition extends EJBLocalObject {
    Integer getPositionId();

    void setPositionId(Integer positionId);

    Integer getAccountId();

    void setAccountId(Integer accountId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContractId();

    void setContractId(Integer contractId);

    Integer getFreetextId();

    void setFreetextId(Integer freetextId);

    Integer getInvoiceId();

    void setInvoiceId(Integer invoiceId);

    Integer getPayStepId();

    void setPayStepId(Integer payStepId);

    Integer getProductId();

    void setProductId(Integer productId);

    Integer getNumber();

    void setNumber(Integer number);

    BigDecimal getQuantity();

    void setQuantity(BigDecimal quantity);

    java.math.BigDecimal getTotalPrice();

    void setTotalPrice(java.math.BigDecimal totalPrice);

    java.math.BigDecimal getUnitPrice();

    void setUnitPrice(java.math.BigDecimal unitPrice);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getVatId();

    void setVatId(Integer vatId);

    java.math.BigDecimal getVatRate();

    void setVatRate(java.math.BigDecimal vatRate);

    FinanceFreeText getFinanceFreeText();

    void setFinanceFreeText(FinanceFreeText financeFreeText);

    java.math.BigDecimal getDiscount();

    void setDiscount(java.math.BigDecimal discount);

    String getUnit();

    void setUnit(String unit);

    BigDecimal getUnitPriceGross();

    void setUnitPriceGross(BigDecimal unitPriceGross);

    BigDecimal getTotalPriceGross();

    void setTotalPriceGross(BigDecimal totalPriceGross);

    Invoice getInvoice();

    void setInvoice(Invoice invoice);

    Integer getSalePositionId();

    void setSalePositionId(Integer salePositionId);

    BigDecimal getDiscountValue();

    void setDiscountValue(BigDecimal discountValue);

    Boolean getExported();

    void setExported(Boolean exported);
}
