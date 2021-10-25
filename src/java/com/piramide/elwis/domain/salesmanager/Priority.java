package com.piramide.elwis.domain.salesmanager;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: Priority.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 14-01-2005 10:13:59 AM Fernando Montaño Exp $
 */


public interface Priority extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getPriorityId();

    void setPriorityId(Integer priorityId);

    String getPriorityName();

    void setPriorityName(String processPriorityName);

    Integer getSequence();

    void setSequence(Integer sequence);

    Integer getVersion();

    void setVersion(Integer version);
}
