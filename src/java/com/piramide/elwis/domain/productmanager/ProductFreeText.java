package com.piramide.elwis.domain.productmanager;

import javax.ejb.EJBLocalObject;

/**
 * Product FreeText local interface
 *
 * @author Fernando Montaño
 * @version $Id: ProductFreeText.java 9121 2009-04-17 00:28:59Z fernando $
 */


public interface ProductFreeText extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    Integer getType();

    void setType(Integer type);

    byte[] getValue();

    void setValue(byte[] value);

    Integer getVersion();

    void setVersion(Integer version);
}
