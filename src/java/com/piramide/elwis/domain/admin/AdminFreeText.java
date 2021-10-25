package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;

/**
 * AdminFreeText local interface
 *
 * @author Fernando Montaño
 * @version $Id: AdminFreeText.java 9121 2009-04-17 00:28:59Z fernando $
 */
public interface AdminFreeText extends EJBLocalObject {
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
