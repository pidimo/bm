package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

/**
 * Telecom Entity local interface
 *
 * @author Ernesto
 * @version $Id: Telecom.java 7936 2007-10-27 16:08:39Z fernando $
 */

public interface Telecom extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getDescription();

    void setDescription(String description);

    Integer getTelecomId();

    void setTelecomId(Integer telecomId);

    String getData();

    void setData(String data);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getTelecomTypeId();

    void setTelecomTypeId(Integer telecomTypeId);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Address getAddress();

    void setAddress(Address address);

    ContactPerson getContactPerson();

    void setContactPerson(ContactPerson contactPerson);

    Boolean getPredetermined();

    void setPredetermined(Boolean predetermined);

    void setTelecomType(TelecomType telecomType);

    TelecomType getTelecomType();
}
