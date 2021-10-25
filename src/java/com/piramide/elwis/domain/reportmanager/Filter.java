package com.piramide.elwis.domain.reportmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * @author : ivan
 * @version : $Id ${NAME} ${time}
 */

public interface Filter extends EJBLocalObject {
    String getAliasCondition();

    void setAliasCondition(String aliasCondition);

    String getColumnReference();

    void setColumnReference(String columnReference);

    Integer getColumnType();

    void setColumnType(Integer columnType);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getOperator();

    void setOperator(Integer operator);

    Integer getFilterId();

    void setFilterId(Integer filterId);

    Integer getReportId();

    void setReportId(Integer reportId);

    String getTableReference();

    void setTableReference(String tableReference);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getSequence();

    void setSequence(Integer sequence);

    byte[] getPath();

    void setPath(byte[] path);

    Integer getFilterType();

    void setFilterType(Integer filterType);

    Collection getFilterValues();

    void setFilterValues(Collection filterValues);

    Report getReport();

    void setReport(Report report);

    String getLabel();

    void setLabel(String label);

    Boolean getIsParameter();

    void setIsParameter(Boolean isParameter);

    Integer getCategoryId();

    void setCategoryId(Integer categoryId);


    ReportQueryParam getReportQueryParam();

    void setReportQueryParam(ReportQueryParam reportQueryParam);
}
