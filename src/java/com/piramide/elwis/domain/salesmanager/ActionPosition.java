package com.piramide.elwis.domain.salesmanager;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: ActionPosition.java 9856 2009-11-12 22:37:48Z ivan ${NAME}.java, v 2.0 24-01-2005 03:54:56 PM Fernando Montaño Exp $
 */


public interface ActionPosition extends EJBLocalObject {
    BigDecimal getAmount();

    void setAmount(BigDecimal amount);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContactId();

    void setContactId(Integer contactId);

    Integer getDescriptionId();

    void setDescriptionId(Integer descriptionId);

    Integer getPositionId();

    void setPositionId(Integer positionId);

    java.math.BigDecimal getPrice();

    void setPrice(java.math.BigDecimal price);

    Integer getProductId();

    void setProductId(Integer productId);

    Integer getProcessId();

    void setProcessId(Integer processId);

    java.math.BigDecimal getTotalPrice();

    void setTotalPrice(java.math.BigDecimal totalPrice);

    Integer getUnit();

    void setUnit(Integer unit);

    Integer getVersion();

    void setVersion(Integer version);

    Action getAction();

    void setAction(Action action);

    SalesFreeText getDescriptionText();

    void setDescriptionText(SalesFreeText descriptionText);

    void setDescriptionText(EJBLocalObject descriptionText);

    java.math.BigDecimal getDiscount();

    void setDiscount(java.math.BigDecimal discount);

    Integer getNumber();

    void setNumber(Integer number);

    java.math.BigDecimal getUnitPriceGross();

    void setUnitPriceGross(java.math.BigDecimal unitPriceGross);

    java.math.BigDecimal getTotalPriceGross();

    void setTotalPriceGross(java.math.BigDecimal totalPriceGross);
}
