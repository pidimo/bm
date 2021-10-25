package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.PKGenerator;

import javax.ejb.*;

/**
 * Telecom EntityBean
 *
 * @author Ernesto
 * @version $Id: TelecomBean.java 7936 2007-10-27 16:08:39Z fernando $
 */

public abstract class TelecomBean implements EntityBean {

    public Integer ejbCreate(Integer addressId, Integer contactPersonId,
                             String data, String description, Boolean predetermined, Integer telecomTypeId, Integer companyId)
            throws CreateException {
        setTelecomId(PKGenerator.i.nextKey(ContactConstants.TABLE_TELECOM));
        setAddressId(addressId);
        setContactPersonId(contactPersonId);
        setData(data);
        setDescription(description);
        setPredetermined(predetermined);
        setTelecomTypeId(telecomTypeId);
        setCompanyId(companyId);
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(Integer addressId, Integer contactPersonId, String data, String description,
                              Boolean predetermined, Integer telecomTypeId, Integer companyId) {
    }

    public TelecomBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
    }

    public void unsetEntityContext() throws EJBException {
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract Integer getTelecomId();

    public abstract void setTelecomId(Integer telecomId);

    public abstract String getData();

    public abstract void setData(String data);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract Integer getTelecomTypeId();

    public abstract void setTelecomTypeId(Integer telecomTypeId);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Address getAddress();

    public abstract void setAddress(Address address);

    public abstract ContactPerson getContactPerson();

    public abstract void setContactPerson(ContactPerson contactPerson);

    public abstract Boolean getPredetermined();

    public abstract void setPredetermined(Boolean predetermined);

    public abstract void setTelecomType(TelecomType telecomType);

    public abstract TelecomType getTelecomType();
}
