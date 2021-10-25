package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public interface RecordColumn extends EJBLocalObject {
    String getColumnValue();

    void setColumnValue(String columnValue);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getImportColumnId();

    void setImportColumnId(Integer importColumnId);

    Integer getImportRecordId();

    void setImportRecordId(Integer importRecordId);

    Integer getColumnIndex();

    void setColumnIndex(Integer columnIndex);
}
