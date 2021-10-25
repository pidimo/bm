package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public interface DedupliContact extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getDedupliContactId();

    void setDedupliContactId(Integer dedupliContactId);

    Long getStartTime();

    void setStartTime(Long startTime);

    Integer getStatus();

    void setStatus(Integer status);

    Integer getUserId();

    void setUserId(Integer userId);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getDuplicateGroups();

    void setDuplicateGroups(Collection duplicateGroups);
}
