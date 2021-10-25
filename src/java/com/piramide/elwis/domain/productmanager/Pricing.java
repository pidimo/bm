package com.piramide.elwis.domain.productmanager;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Ernesto
 * @version $Id: Pricing.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 23-ago-2004 9:26:07 Ernesto Exp $
 */

public interface Pricing extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getProductId();

    void setProductId(Integer productId);

    java.math.BigDecimal getPrice();

    void setPrice(java.math.BigDecimal price);

    Integer getQuantity();

    void setQuantity(Integer quantity);

    Integer getVersion();

    void setVersion(Integer version);
}
