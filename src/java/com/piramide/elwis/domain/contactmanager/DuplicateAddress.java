package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public interface DuplicateAddress extends EJBLocalObject {
    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getDuplicateGroupId();

    void setDuplicateGroupId(Integer duplicateGroupId);

    Boolean getIsMain();

    void setIsMain(Boolean isMain);

    Integer getPositionIndex();

    void setPositionIndex(Integer positionIndex);
}
