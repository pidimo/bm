package com.piramide.elwis.domain.salesmanager;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: SalesFreeText.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 19-01-2005 05:07:36 PM Fernando Montaño Exp $
 */


public interface SalesFreeText extends EJBLocalObject {
    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    Integer getType();

    void setType(Integer type);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer version);

    byte[] getValue();

    void setValue(byte[] value);
}
