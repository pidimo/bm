package com.piramide.elwis.domain.reportmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * @author : ivan
 * @version : $Id ${NAME} ${time}
 */

public interface Column extends EJBLocalObject {
    String getColumnReference();

    void setColumnReference(String columnReference);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getLabel();

    void setLabel(String label);

    byte[] getPath();

    void setPath(byte[] path);

    Integer getColumnId();

    void setColumnId(Integer columnId);

    Integer getReportId();

    void setReportId(Integer reportId);

    Integer getSequence();

    void setSequence(Integer squence);

    String getTableReference();

    void setTableReference(String tableReference);

    Integer getColumnType();

    void setColumnType(Integer type);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getReports();

    void setReports(Collection reports);

    ColumnGroup getColumnGroup();

    void setColumnGroup(ColumnGroup column);

    Collection getColumnTotalizers();

    void setColumnTotalizers(Collection columnTotalizers);

    Boolean getIsTotalizer();

    void setIsTotalizer(Boolean isTotalizer);

    Boolean getColumnOrder();

    void setColumnOrder(Boolean columnOrder);

    Integer getCategoryId();

    void setCategoryId(Integer categoryId);
}
