package com.piramide.elwis.domain.reportmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10.1
 */
public interface ReportArtifact extends EJBLocalObject {
    Integer getArtifactId();

    void setArtifactId(Integer artifactId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getFileId();

    void setFileId(Integer fileId);

    String getFileName();

    void setFileName(String fileName);

    Integer getReportId();

    void setReportId(Integer reportId);

    Integer getVersion();

    void setVersion(Integer version);

    ReportFreeText getArtifactFile();

    void setArtifactFile(ReportFreeText artifactFile);
}
