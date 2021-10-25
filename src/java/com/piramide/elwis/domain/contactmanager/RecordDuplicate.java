package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public interface RecordDuplicate extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getImportRecordId();

    void setImportRecordId(Integer importRecordId);

    Integer getAddressId();

    void setAddressId(Integer addressId);
}
