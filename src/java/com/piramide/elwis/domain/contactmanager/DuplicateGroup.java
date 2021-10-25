package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public interface DuplicateGroup extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getDedupliContactId();

    void setDedupliContactId(Integer dedupliContactId);

    Integer getDuplicateGroupId();

    void setDuplicateGroupId(Integer duplicateGroupId);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getDuplicateAddress();

    void setDuplicateAddress(Collection duplicateAddress);
}
