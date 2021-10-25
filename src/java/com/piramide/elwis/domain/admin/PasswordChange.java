package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public interface PasswordChange extends EJBLocalObject {
    Long getChangeTime();

    void setChangeTime(Long changeTime);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getDescription();

    void setDescription(String description);

    Integer getPasswordChangeId();

    void setPasswordChangeId(Integer passwordChangeId);

    Integer getTotalUser();

    void setTotalUser(Integer totalUser);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getRolePasswordChanges();

    void setRolePasswordChanges(Collection rolePasswordChanges);

    Collection getUserPasswordChanges();

    void setUserPasswordChanges(Collection userPasswordChanges);

    Long getUpdateDateTime();

    void setUpdateDateTime(Long updateDateTime);

    Integer getUserId();

    void setUserId(Integer userId);
}
