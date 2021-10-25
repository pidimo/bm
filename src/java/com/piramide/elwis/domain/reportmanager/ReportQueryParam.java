package com.piramide.elwis.domain.reportmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10
 */
public interface ReportQueryParam extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getParameterName();

    void setParameterName(String parameterName);

    Integer getReportQueryParamId();

    void setReportQueryParamId(Integer reportQueryParamId);

    Integer getFilterId();

    void setFilterId(Integer filterId);

    Integer getReportId();

    void setReportId(Integer reportId);

    Integer getVersion();

    void setVersion(Integer version);

    Filter getFilter();

    void setFilter(Filter filter);
}
