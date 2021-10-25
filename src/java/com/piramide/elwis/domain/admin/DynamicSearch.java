package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public interface DynamicSearch extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getDynamicSearchId();

    void setDynamicSearchId(Integer dynamicSearchId);

    String getModule();

    void setModule(String module);

    String getName();

    void setName(String name);

    Integer getUserId();

    void setUserId(Integer userId);

    Collection getDynamicSearchFields();

    void setDynamicSearchFields(Collection dynamicSearchFields);
}
