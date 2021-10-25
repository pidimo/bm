package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

/**
 * Represents AddressCatalog EntityBean.
 * This class implements the "Modulator@ Pattern", that allows existence of address bean of module
 * contactmanager into catalog module.
 *
 * @author Ernesto
 * @version $Id: AddressCatalogBean.java 822 2004-05-25 19:23:21Z fernando $
 * @see com.piramide.elwis.domain.contactmanager.AddressBean
 */

public abstract class AddressCatalogBean implements EntityBean {
    public AddressCatalogBean() {
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

    public abstract Integer getAddressCatalogId();

    public abstract void setAddressCatalogId(Integer addressCatalogId);

    public abstract City getCity();

    public abstract void setCity(City city);

    public abstract Country getCountry();

    public abstract void setCountry(Country country);

    public abstract Language getLanguage();

    public abstract void setLanguage(Language language);

    public abstract Salutation getSalutation();

    public abstract void setSalutation(Salutation salutation);

    public abstract Title getTitle();

    public abstract void setTitle(Title title);

    public abstract FreeText getFreeText();

    public abstract void setFreeText(FreeText freeText);

    public abstract FreeText getWayDescription();

    public abstract void setWayDescription(FreeText wayDescription);

}
