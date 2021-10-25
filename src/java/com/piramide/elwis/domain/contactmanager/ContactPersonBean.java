package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.DateUtils;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Date;

/**
 * Represents ContactPerson EntityBean
 *
 * @author Fernando Montaño
 * @version $Id: ContactPersonBean.java 10359 2013-07-04 00:29:08Z miguel $
 */

public abstract class ContactPersonBean implements EntityBean {

    EntityContext entityContext;

    public ContactPersonPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setVersion(1);
        setRecordDate(DateUtils.dateToInteger(new Date()));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        this.entityContext = null;
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

    public abstract Boolean getActive();

    public abstract void setActive(Boolean active);

    public abstract String getFunction();

    public abstract void setFunction(String function);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Address getAddress();

    public abstract void setAddress(Address address);

    public abstract Address getContactPerson();

    public abstract void setContactPerson(Address contactPerson);

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer company);

    public abstract java.util.Collection getTelecoms();

    public abstract void setTelecoms(java.util.Collection telecoms);

    public abstract Integer getDepartmentId();

    public abstract void setDepartmentId(Integer departmentId);

    public abstract Integer getPersonTypeId();

    public abstract void setPersonTypeId(Integer personTypeId);

    public abstract Integer getRecordDate();

    public abstract void setRecordDate(Integer recordDate);

    public abstract Integer getRecordUserId();

    public abstract void setRecordUserId(Integer recordUserId);

    public abstract String getAddAddressLine();

    public abstract void setAddAddressLine(String additionalAddressLine);

    public abstract Integer getAdditionalAddressId();

    public abstract void setAdditionalAddressId(Integer additionalAddressId);
}
