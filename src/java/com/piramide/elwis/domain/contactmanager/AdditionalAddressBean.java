package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public abstract class AdditionalAddressBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setAdditionalAddressId(PKGenerator.i.nextKey(ContactConstants.TABLE_ADDITIONALADDRESS));
        setVersion(new Integer(1));

        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public AdditionalAddressBean() {
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

    public abstract Integer getAdditionalAddressId();

    public abstract void setAdditionalAddressId(Integer additionalAddressId);

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract String getAdditionalAddressLine();

    public abstract void setAdditionalAddressLine(String additionalAddressLine);

    public abstract Integer getCityId();

    public abstract void setCityId(Integer cityId);

    public abstract Integer getCommentId();

    public abstract void setCommentId(Integer commentId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getCountryId();

    public abstract void setCountryId(Integer countryId);

    public abstract String getHouseNumber();

    public abstract void setHouseNumber(String houseNumber);

    public abstract Boolean getIsDefault();

    public abstract void setIsDefault(Boolean isDefault);

    public abstract String getStreet();

    public abstract void setStreet(String street);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract ContactFreeText getContactFreeText();

    public abstract void setContactFreeText(ContactFreeText contactFreeText);

    public void setContactFreeText(EJBLocalObject contactFreeText) {
        setContactFreeText((ContactFreeText) contactFreeText);
    }

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Integer getAdditionalAddressType();

    public abstract void setAdditionalAddressType(Integer additionalAddressType);
}
