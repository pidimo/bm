package com.piramide.elwis.domain.dashboard;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:46:48 PM
 */

public interface Component extends EJBLocalObject {
    Integer getComponentId();

    void setComponentId(Integer componentId);

    Integer getXmlComponentId();

    void setXmlComponentId(Integer xmlComponentId);

    Collection getColumns();

    void setColumns(Collection columns);

    Collection getFilters();

    void setFilters(Collection filters);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);
}
