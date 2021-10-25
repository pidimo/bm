package com.piramide.elwis.domain.salesmanager;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: Status.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 14-01-2005 10:21:23 AM Fernando Montaño Exp $
 */


public interface Status extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Boolean getIsFinal();

    void setIsFinal(Boolean isFinal);

    Integer getStatusId();

    void setStatusId(Integer statusId);

    String getStatusName();

    void setStatusName(String statusName);

    Integer getVersion();

    void setVersion(Integer version);
}
