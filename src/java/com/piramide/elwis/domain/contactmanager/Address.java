package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.domain.catalogmanager.*;

import javax.ejb.EJBLocalObject;
import java.util.Collection;
import java.util.Date;

/**
 * Represent Address EntityBean local interface
 *
 * @author Ernesto
 * @version $Id: Address.java 10377 2013-09-27 15:59:03Z miguel $
 */
public interface Address extends EJBLocalObject {
    Boolean getActive();

    Integer getAddressId();

    void setAddressId(Integer addressId);

    String getAddressType();

    void setAddressType(String addressType);

    void setActive(Boolean active);

    Integer getBirthday();

    void setBirthday(Integer myField);

    String getHouseNumber();

    void setHouseNumber(String houseNumber);

    Integer getLastModificationDate();

    void setLastModificationDate(Integer lastModificationDate);

    String getName1();

    void setName1(String name1);

    String getName2();

    void setName2(String name2);

    String getName3();

    void setName3(String name3);

    Boolean getPersonal();

    void setPersonal(Boolean personal);

    Integer getRecordDate();

    void setRecordDate(Integer recordDate);

    String getSearchName();

    void setSearchName(String searchName);

    String getStreet();

    void setStreet(String street);

    String getTaxNumber();

    void setTaxNumber(String taxNumber);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getContactPersons();

    void setContactPersons(Collection contactPersons);

    Collection getOrganizations();

    void setOrganizations(Collection organizations);

    Byte getCode();

    void setCode(Byte code);

    String getCity();

    void setCity(String city);

    Country getCountry();

    void setCountry(Country country);

    Language getLanguage();

    void setLanguage(Language language);

    Salutation getSalutation();

    void setSalutation(Salutation salutation);

    Title getTitle();

    void setTitle(Title title);

    Collection getBanks();

    void setBanks(Collection banks);

    Integer getCompanyId();

    void setCompanyId(Integer company);

    Collection getBankAccounts();

    void setBankAccounts(Collection bankAccounts);

    Collection getDepartments();

    void setDepartments(Collection departments);

    FreeText getFreeText();

    void setFreeText(FreeText freeText);

    FreeText getWayDescription();

    void setWayDescription(FreeText wayDescription);

    Collection getTelecoms();

    void setTelecoms(Collection telecoms);

    Integer getLanguageId();

    void setLanguageId(Integer languageId);

    Integer getTitleId();

    void setTitleId(Integer titleId);

    Integer getSalutationId();

    void setSalutationId(Integer salutationId);

    void setBirthdayDate(Date value);

    public Date getBirthdayDate();

    Integer getRecordUserId();

    void setRecordUserId(Integer recordUserId);

    Integer getBankAccountId();

    void setBankAccountId(Integer bankAccountId);

    Integer getLastModificationUserId();

    void setLastModificationUserId(Integer lastModificationUserId);

    Integer getCountryId();

    void setCountryId(Integer countryId);

    Integer getCityId();

    void setCityId(Integer cityId);

    public void setLastUpdateDate(Date value);

    public Date getLastUpdateDate();

    Collection getCommunications();

    void setCommunications(Collection communications);

    Collection getFavorites();

    void setFavorites(Collection favorites);

    Collection getRecents();

    void setRecents(Collection recents);

    String getEducation();

    void setEducation(String education);

    City getCityEntity();

    void setCityEntity(City city);

    String getKeywords();

    void setKeywords(String keywords);

    ContactFreeText getImageFreeText();

    void setImageFreeText(ContactFreeText imageFreeText);

    Integer getImageId();

    void setImageId(Integer imageId);

    /**
     * Formatt the address name in base the addressType
     *
     * @return The formatted name of address
     */
    public String getName();

    String getAdditionalAddressLine();

    void setAdditionalAddressLine(String additionalAddressLine);

    Boolean getIsPublic();

    void setIsPublic(Boolean isPublic);
}
