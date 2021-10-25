package com.piramide.elwis.domain.catalogmanager;

/**
 * @author Ernesto
 * @version $Id: AddressCatalog.java 841 2004-05-25 23:08:13Z ivan $
 * @see com.piramide.elwis.domain.contactmanager.AddressBean
 */

import javax.ejb.EJBLocalObject;

public interface AddressCatalog extends EJBLocalObject {
    Integer getAddressCatalogId();

    void setAddressCatalogId(Integer addressCatalogId);

    City getCity();

    void setCity(City city);

    Country getCountry();

    void setCountry(Country country);

    Language getLanguage();

    void setLanguage(Language language);

    Salutation getSalutation();

    void setSalutation(Salutation salutation);

    Title getTitle();

    void setTitle(Title title);

    FreeText getFreeText();

    void setFreeText(FreeText freeText);

    FreeText getWayDescription();

    void setWayDescription(FreeText wayDescription);

}
