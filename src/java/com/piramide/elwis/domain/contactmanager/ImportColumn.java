package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Ivan Alban
 * @version 4.2.1
 */
public interface ImportColumn extends EJBLocalObject {
    Integer getColumnId();

    void setColumnId(Integer columnid);

    Integer getColumnValue();

    void setColumnValue(Integer columnValue);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getGroupId();

    void setGroupId(Integer groupId);

    Integer getImportColumnId();

    void setImportColumnId(Integer importColumnId);

    Integer getProfileId();

    void setProfileId(Integer profileId);

    Integer getUiPosition();

    void setUiPosition(Integer uiPosition);

    String getColumnName();

    void setColumnName(String columnName);
}
