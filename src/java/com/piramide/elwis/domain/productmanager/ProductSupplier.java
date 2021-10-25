/**
 * AlfaCentauro Team
 * @author Titus
 * @version ${NAME}.java, v 2.0 Aug 26, 2004 11:04:33 AM  
 */
package com.piramide.elwis.domain.productmanager;

import javax.ejb.EJBLocalObject;

public interface ProductSupplier extends EJBLocalObject {
    Boolean getActive();

    void setActive(Boolean active);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    java.math.BigDecimal getDiscount();

    void setDiscount(java.math.BigDecimal discount);

    String getPartNumber();

    void setPartNumber(String partNumber);

    java.math.BigDecimal getPrice();

    void setPrice(java.math.BigDecimal price);

    Integer getProductId();

    void setProductId(Integer productId);

    Integer getSupplierId();

    void setSupplierId(Integer supplierId);

    Integer getUnitId();

    void setUnitId(Integer unitId);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Product getProduct();

    void setProduct(Product product);

}
