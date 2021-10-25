/**
 * @author Tayes
 * @version $Id: ContactFreeText.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 16-05-2004 12:15:45 PM Tayes Exp $
 */
package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

public interface ContactFreeText extends EJBLocalObject {
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
