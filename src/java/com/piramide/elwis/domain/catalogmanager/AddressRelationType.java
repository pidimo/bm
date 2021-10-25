package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public interface AddressRelationType extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getRelationTypeId();

    void setRelationTypeId(Integer relationTypeId);

    Integer getRelationType();

    void setRelationType(Integer relationType);

    String getTitle();

    void setTitle(String title);

    Integer getVersion();

    void setVersion(Integer version);
}
