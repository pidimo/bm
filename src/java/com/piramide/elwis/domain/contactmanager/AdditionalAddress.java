package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public interface AdditionalAddress extends EJBLocalObject {
    Integer getAdditionalAddressId();

    void setAdditionalAddressId(Integer additionalAddressId);

    Integer getAddressId();

    void setAddressId(Integer addressId);

    String getAdditionalAddressLine();

    void setAdditionalAddressLine(String additionalAddressLine);

    Integer getCityId();

    void setCityId(Integer cityId);

    Integer getCommentId();

    void setCommentId(Integer commentId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getCountryId();

    void setCountryId(Integer countryId);

    String getHouseNumber();

    void setHouseNumber(String houseNumber);

    Boolean getIsDefault();

    void setIsDefault(Boolean isDefault);

    String getStreet();

    void setStreet(String street);

    Integer getVersion();

    void setVersion(Integer version);

    ContactFreeText getContactFreeText();

    void setContactFreeText(ContactFreeText contactFreeText);

    public void setContactFreeText(EJBLocalObject contactFreeText);

    String getName();

    void setName(String name);

    Integer getAdditionalAddressType();

    void setAdditionalAddressType(Integer additionalAddressType);
}
