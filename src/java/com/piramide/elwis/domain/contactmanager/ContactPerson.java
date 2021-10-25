package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

/**
 * Represent ContactPerson Entity local interface
 *
 * @author Fernando Montaño
 * @version $Id: ContactPerson.java 10359 2013-07-04 00:29:08Z miguel $
 */

public interface ContactPerson extends EJBLocalObject {
    Boolean getActive();

    void setActive(Boolean active);

    String getFunction();

    void setFunction(String function);

    Integer getVersion();

    void setVersion(Integer version);

    Address getAddress();

    void setAddress(Address address);

    Address getContactPerson();

    void setContactPerson(Address contactPerson);

    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Integer getCompanyId();

    void setCompanyId(Integer company);

    java.util.Collection getTelecoms();

    void setTelecoms(java.util.Collection telecoms);

    Integer getDepartmentId();

    void setDepartmentId(Integer departmentId);

    Integer getPersonTypeId();

    void setPersonTypeId(Integer personTypeId);

    Integer getRecordDate();

    void setRecordDate(Integer recordDate);

    Integer getRecordUserId();

    void setRecordUserId(Integer recordUserId);

    String getAddAddressLine();

    void setAddAddressLine(String additionalAddressLine);

    Integer getAdditionalAddressId();

    void setAdditionalAddressId(Integer additionalAddressId);
}
