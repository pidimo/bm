package com.piramide.elwis.domain.dashboard;

import javax.ejb.EJBLocalObject;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:48:14 PM
 */

public interface Filter extends EJBLocalObject {
    Integer getFilterId();

    void setFilterId(Integer filterId);

    String getVal();

    void setVal(String val);

    Boolean getIsRange();

    void setIsRange(Boolean isRange);

    String getName();

    void setName(String name);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);
}
