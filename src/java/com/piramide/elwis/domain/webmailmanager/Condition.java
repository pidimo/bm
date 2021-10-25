package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: Condition.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java ,v 1.1 02-02-2005 04:28:49 PM miky Exp $
 */

public interface Condition extends EJBLocalObject {
    Integer getConditionId();

    void setConditionId(Integer conditionId);

    Integer getConditionNameKey();

    void setConditionNameKey(Integer conditionNameKey);

    Integer getConditionKey();

    void setConditionKey(Integer conditionKey);

    String getConditionText();

    void setConditionText(String conditiontext);

    Integer getFilterId();

    void setFilterId(Integer filterId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

}
