package com.piramide.elwis.domain.reportmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * @author : ivan
 * @version : $Id ${NAME} ${time}
 */

public interface Report extends EJBLocalObject {
    String getFilterRule();

    void setFilterRule(String filterRule);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getEmployeeId();

    void setEmployeeId(Integer employeeId);

    String getInitialTableReference();

    void setInitialTableReference(String initialTableReference);

    String getModule();

    void setModule(String module);

    String getName();

    void setName(String name);

    Integer getReportId();

    void setReportId(Integer reportId);

    Boolean getState();

    void setState(Boolean state);

    Integer getReportType();

    void setReportType(Integer type);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getFilters();

    void setFilters(Collection filters);

    //Collection getReports();

    //void setReports(Collection reports);

    Collection getColumns();

    void setColumns(Collection columns);

    Collection getTotalizers();

    void setTotalizers(Collection totalizers);

    Chart getCharts();

    void setCharts(Chart charts);

    ReportFreeText getDescriptionText();

    void setDescriptionText(ReportFreeText descriptionText);

    void setDescriptionText(EJBLocalObject descriptionText);

    Integer getPageSize();

    void setPageSize(Integer pageSize);

    Integer getPageOrientation();

    void setPageOrientation(Integer pageOrientation);

    String getReportFormat();

    void setReportFormat(String reportFormat);

    Integer getJrxmlFileId();

    void setJrxmlFileId(Integer jrxmlFileId);

    String getJrxmlFileName();

    void setJrxmlFileName(String jrxmlFileName);

    Integer getSourceType();

    void setSourceType(Integer sourceType);

    ReportFreeText getJrxmlFile();

    void setJrxmlFile(ReportFreeText jrxmlFile);

    Collection getReportQueryParams();

    void setReportQueryParams(Collection reportQueryParams);

    Collection getReportArtifacts();

    void setReportArtifacts(Collection reportArtifacts);
}
