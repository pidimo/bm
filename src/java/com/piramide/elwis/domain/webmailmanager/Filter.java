package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: Filter.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java ,v 1.1 02-02-2005 04:21:54 PM miky Exp $
 */

public interface Filter extends EJBLocalObject {
    Integer getFilterId();

    void setFilterId(Integer filterId);

    String getFilterName();

    void setFilterName(String filterName);

    Integer getFolderId();

    void setFolderId(Integer folderId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Collection getConditions();

    void setConditions(Collection conditions);

    public boolean evaluateConditions(String from, String to, String cc, String subject, String body);

    public boolean evaluate(String param, Condition condition);
}
