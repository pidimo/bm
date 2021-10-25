/**
 *
 * @author Fernando Montao
 * @version $Id: ${NAME}.java 2009-02-20 11:58:10 $
 */
package com.piramide.elwis.domain.project;

import javax.ejb.EJBLocalObject;

public interface ProjectFreeText extends EJBLocalObject {
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
