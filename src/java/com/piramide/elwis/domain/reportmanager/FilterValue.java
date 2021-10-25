package com.piramide.elwis.domain.reportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: FilterValue.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v1.0 03-abr-2006 17:34:00 Miky Exp $
 */

public interface FilterValue extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getPkSequence();

    void setPkSequence(Integer pkSequence);

    Integer getFilterId();

    void setFilterId(Integer reportFilterId);

    Integer getSequence();

    void setSequence(Integer sequence);

    Integer getFilterValueId();

    void setFilterValueId(Integer filterValueId);

    String getFilterValue();

    void setFilterValue(String filterValue);

    Filter getFilter();

    void setFilter(Filter filter);
}
