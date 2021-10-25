package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public interface DynamicSearchField extends EJBLocalObject {
    String getAlias();

    void setAlias(String alias);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getDynamicSearchId();

    void setDynamicSearchId(Integer dynamicSearchId);

    Integer getDynamicSearchFieldId();

    void setDynamicSearchFieldId(Integer dynamicSearchFieldId);

    Integer getPosition();

    void setPosition(Integer position);
}
