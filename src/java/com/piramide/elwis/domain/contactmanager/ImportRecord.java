package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public interface ImportRecord extends EJBLocalObject {
    String getCityName();

    void setCityName(String cityName);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getCountryName();

    void setCountryName(String countryName);

    String getHouseNumber();

    void setHouseNumber(String houseNumber);

    Integer getImportRecordId();

    void setImportRecordId(Integer importRecordId);

    Boolean getIsDuplicate();

    void setIsDuplicate(Boolean isDuplicate);

    String getName1();

    void setName1(String name1);

    String getName2();

    void setName2(String name2);

    String getName3();

    void setName3(String name3);

    Integer getProfileId();

    void setProfileId(Integer profileId);

    Integer getRecordIndex();

    void setRecordIndex(Integer recordIndex);

    String getStreet();

    void setStreet(String street);

    String getZip();

    void setZip(String zip);

    Integer getVersion();

    void setVersion(Integer version);

    String getIdentityKey();

    void setIdentityKey(String identityKey);

    Integer getRecordType();

    void setRecordType(Integer recordType);

    Collection getRecordColumns();

    void setRecordColumns(Collection recordColumns);

    Collection getRecordDuplicates();

    void setRecordDuplicates(Collection recordDuplicates);

    Integer getParentRecordId();

    void setParentRecordId(Integer parentRecordId);

    Collection getChildImportRecords();

    void setChildImportRecords(Collection childImportRecords);

    ImportRecord getParentImportRecord();

    void setParentImportRecord(ImportRecord parentImportRecord);

    String getFunction();

    void setFunction(String function);

    Integer getOrganizationId();

    void setOrganizationId(Integer organizationId);
}
