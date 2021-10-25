/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.salesmanager;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;

public interface SalePosition extends EJBLocalObject {
    Boolean getActive();

    void setActive(Boolean active);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getCustomerId();

    void setCustomerId(Integer customerId);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Integer getDeliveryDate();

    void setDeliveryDate(Integer deliveryDate);

    Integer getFreetextId();

    void setFreetextId(Integer freetextId);

    Integer getProductId();

    void setProductId(Integer productId);

    Integer getSalePositionId();

    void setSalePositionId(Integer salePositionId);

    BigDecimal getQuantity();

    void setQuantity(BigDecimal quantity);

    Integer getSaleId();

    void setSaleId(Integer saleId);

    String getSerial();

    void setSerial(String serial);

    java.math.BigDecimal getTotalPrice();

    void setTotalPrice(java.math.BigDecimal totalPrice);

    Integer getUnitId();

    void setUnitId(Integer unitId);

    java.math.BigDecimal getUnitPrice();

    void setUnitPrice(java.math.BigDecimal unitPrice);

    Integer getVersion();

    void setVersion(Integer version);

    String getVersionNumber();

    void setVersionNumber(String versionNumber);

    SalesFreeText getSalesFreeText();

    void setSalesFreeText(SalesFreeText salesFreeText);

    java.util.Collection getProductContracts();

    void setProductContracts(java.util.Collection productContract);

    Integer getVatId();

    void setVatId(Integer vatId);

    Integer getPayMethod();

    void setPayMethod(Integer payMethod);

    java.math.BigDecimal getDiscount();

    void setDiscount(java.math.BigDecimal discount);

    java.math.BigDecimal getUnitPriceGross();

    void setUnitPriceGross(java.math.BigDecimal unitPriceGross);

    java.math.BigDecimal getTotalPriceGross();

    void setTotalPriceGross(java.math.BigDecimal totalPriceGross);
}
