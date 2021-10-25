package com.piramide.elwis.domain.reportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Alfacentauro Team
 *
 * @author Alvaro
 * @version $Id: ReportFreeText.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java ,v 1.1 16-11-2005 11:31:38 AM Alvaro Exp $
 */

public interface ReportFreeText extends EJBLocalObject {
    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getType();

    void setType(Integer type);

    byte[] getValue();

    void setValue(byte[] value);

}
