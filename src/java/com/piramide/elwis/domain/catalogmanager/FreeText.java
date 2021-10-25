package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the FreeText Entity Bean
 *
 * @author Ivan
 * @version $Id: FreeText.java 841 2004-05-25 23:08:13Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface FreeText extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    Integer getType();

    void setType(Integer type);

    byte[] getValue();

    void setValue(byte[] value);
}
