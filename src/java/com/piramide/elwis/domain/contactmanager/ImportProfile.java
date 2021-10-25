package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * @author Ivan Alban
 * @version 4.2.1
 */
public interface ImportProfile extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getLabel();

    void setLabel(String label);

    Integer getProfileId();

    void setProfileId(Integer profileId);

    Integer getProfileType();

    void setProfileType(Integer profileType);

    Boolean getSkipFirstRow();

    void setSkipFirstRow(Boolean skipFirstRow);

    Integer getUserId();

    void setUserId(Integer userId);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getImportColumns();

    void setImportColumns(Collection importColumns);

    Boolean getCheckDuplicate();

    void setCheckDuplicate(Boolean checkDuplicate);

    Integer getTotalRecord();

    void setTotalRecord(Integer totalRecord);

    Long getImportStartTime();

    void setImportStartTime(Long importStartTime);
}
