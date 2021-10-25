package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Collection;
import java.util.Date;

/**
 * Represents Address EntityBean
 *
 * @author Ernesto
 * @version $Id: AddressBean.java 10382 2013-10-08 00:13:32Z miguel $
 * @see com.piramide.elwis.domain.catalogmanager.AddressCatalogBean
 */

public abstract class AddressBean implements EntityBean {

    private static Log log = LogFactory.getLog(AddressBean.class);
    EntityContext entityContext;
    AddressCatalog catalog;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setAddressId(PKGenerator.i.nextKey(ContactConstants.TABLE_ADDRESS));
        setVersion(new Integer(1));
        setActive(new Boolean(true));
        setRecordDate(DateUtils.dateToInteger(new Date()));
        return null;
    }

    /**
     * After create the Address Bean find and set AddressCatalog Bean to AddressCatalog catalog attribute
     *
     * @param dto
     * @throws CreateException if fails
     * @see com.piramide.elwis.domain.catalogmanager.AddressCatalogBean
     */

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
        try {
            AddressCatalogHome catalogHome = (AddressCatalogHome)
                    new InitialContext().lookup(CatalogConstants.JNDI_ADDRESSCATALOG);
            try {
                catalog = catalogHome.findByPrimaryKey(getAddressId());
            } catch (FinderException e) {
                log.error("Error finding AddressCatalog Bean", e);
            }
        } catch (NamingException e) {
            log.error("Naming error, cannot find AddressCatalogHome interface", e);
        }
    }

    public AddressBean() {
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

    public void ejbStore() throws EJBException {
    }

    public abstract Boolean getActive();

    public abstract void setActive(Boolean active);

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract String getAddressType();

    public abstract void setAddressType(String addressType);

    public abstract void setBirthday(Integer value);

    public abstract Integer getBirthday();

    public abstract Byte getCode();

    public abstract void setCode(Byte code);

    public abstract String getHouseNumber();

    public abstract void setHouseNumber(String houseNumber);

    public abstract Integer getLastModificationDate();

    public abstract void setLastModificationDate(Integer lastModificationDate);

    public abstract String getName1();

    public abstract void setName1(String name1);

    public abstract String getName2();

    public abstract void setName2(String name2);

    public abstract String getName3();

    public abstract void setName3(String name3);

    public abstract Boolean getPersonal();

    public abstract void setPersonal(Boolean personal);

    public abstract Integer getRecordDate();

    public abstract void setRecordDate(Integer recordDate);

    public abstract String getSearchName();

    public abstract void setSearchName(String searchName);

    public abstract String getStreet();

    public abstract void setStreet(String street);

    public abstract String getTaxNumber();

    public abstract void setTaxNumber(String taxNumber);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getContactPersons();

    public abstract void setContactPersons(Collection contactPersons);

    public abstract Collection getOrganizations();

    public abstract void setOrganizations(Collection organizations);

    /**
     * Implementation of "Modulator@ Pattern"
     * ejbLoad finds thre refrence to the bean that links
     * address with the catalog module, and the other modules
     *
     * @see com.piramide.elwis.domain.catalogmanager.AddressCatalogBean
     */
    public void ejbLoad() throws EJBException {
        try {
            AddressCatalogHome catalogHome = (AddressCatalogHome)
                    new InitialContext().lookup(CatalogConstants.JNDI_ADDRESSCATALOG);
            try {
                catalog = catalogHome.findByPrimaryKey(getAddressId());
            } catch (FinderException e) {
                log.error("Error finding AddressCatalog bean", e);
            }
        } catch (NamingException e) {
            log.error("Error finding AddressCatalogHome interface", e);
        }
    }

    public String getCity() {
        if (catalog.getCity() != null) {
            return catalog.getCity().getCityName();
        } else {
            return null;
        }
    }

    public void setCity(String city) {
        //noting to do, this is setted throught cityId cmp
    }

    public City getCityEntity() {
        return catalog.getCity();

    }

    public void setCityEntity(City cityEntity) {
        catalog.setCity(cityEntity);
    }

    public Country getCountry() {
        return catalog.getCountry();
    }

    public void setCountry(Country country) {
        catalog.setCountry(country);
    }

    public FreeText getFreeText() {
        return catalog.getFreeText();
    }

    public void setFreeText(FreeText freeText) {
        catalog.setFreeText(freeText);
    }

    public Language getLanguage() {
        return catalog.getLanguage();
    }

    public void setLanguage(Language language) {
        catalog.setLanguage(language);
    }

    public Salutation getSalutation() {
        return catalog.getSalutation();
    }

    public void setSalutation(Salutation salutation) {
        catalog.setSalutation(salutation);
    }

    public Title getTitle() {
        return catalog.getTitle();
    }

    public void setTitle(Title title) {
        catalog.setTitle(title);
    }

    public FreeText getWayDescription() {
        return catalog.getWayDescription();
    }

    public void setWayDescription(FreeText freeText) {
        catalog.setWayDescription(freeText);
    }

    public abstract Collection getBanks();

    public abstract void setBanks(Collection banks);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer company);

    public abstract Collection getBankAccounts();

    public abstract void setBankAccounts(Collection bankAccounts);

    public abstract Collection getDepartments();

    public abstract void setDepartments(Collection departments);

    public abstract Collection getTelecoms();

    public abstract void setTelecoms(Collection telecoms);

    public abstract Integer getLanguageId();

    public abstract void setLanguageId(Integer languageId);

    public abstract Integer getTitleId();

    public abstract void setTitleId(Integer titleId);

    public abstract Integer getSalutationId();

    public abstract void setSalutationId(Integer salutationId);

    public void setBirthdayDate(Date value) {
        setBirthday(DateUtils.dateToInteger(value));
    }

    public Date getBirthdayDate() {
        return DateUtils.integerToDate(getBirthday());
    }

    public abstract Integer getRecordUserId();

    public abstract void setRecordUserId(Integer recordUserId);

    public abstract Integer getBankAccountId();

    public abstract void setBankAccountId(Integer bankAccountId);

    public abstract Integer getLastModificationUserId();

    public abstract void setLastModificationUserId(Integer lastModificationUserId);

    public abstract Integer getCountryId();

    public abstract void setCountryId(Integer countryId);

    public abstract Integer getCityId();

    public abstract void setCityId(Integer cityId);


    public void setLastUpdateDate(Date value) {
        setLastModificationDate(DateUtils.dateToInteger(value));
    }

    public Date getLastUpdateDate() {
        return DateUtils.integerToDate(getLastModificationDate());
    }

    public abstract Collection getCommunications();

    public abstract void setCommunications(Collection communications);

    public abstract Collection getFavorites();

    public abstract void setFavorites(Collection favorites);

    public abstract Collection getRecents();

    public abstract void setRecents(Collection recents);

    public abstract String getEducation();

    public abstract void setEducation(String education);

    public abstract Collection ejbSelectAddressesByNames(String sql, Object[] params) throws FinderException;

    public Collection ejbHomeSelectAddressesByNames(String name1, String name2, String name3, String addressType,
                                                    Integer companyId, Integer userId) throws FinderException {
        StringBuffer query = new StringBuffer();
        query.append("SELECT Object(a) FROM Address as a ")
                .append(" WHERE a.name1=")
                .append("'" + processQuotationMarks(name1) + "'")
                .append(" AND a.name2")
                .append((name2 != null) ? "='" + processQuotationMarks(name2) + "' " : " IS NULL")
                .append(" AND a.name3")
                .append((name3 != null) ? "='" + processQuotationMarks(name3) + "' " : " IS NULL")
                .append(" AND a.addressType=")
                .append("'" + addressType + "'")
                .append(" AND a.companyId=")
                .append(companyId)
                .append(" ORDER BY a.name1, a.name2, a.name3");
        log.debug("The ejbHome sql = " + new String(query));
        Object args[] = {};
        return ejbSelectAddressesByNames(new String(query), args);

    }

    private String processQuotationMarks(String string) {
        return string.replaceAll("'", "''");
    }

    public abstract String getKeywords();

    public abstract void setKeywords(String keywords);

    public abstract ContactFreeText getImageFreeText();

    public abstract void setImageFreeText(ContactFreeText imageFreeText);

    public abstract Integer getImageId();

    public abstract void setImageId(Integer imageId);

    public String getName() {
        StringBuffer name = new StringBuffer(getName1());
        if ("1".equals(getAddressType())) {//Person
            if (getName2() != null) {
                name.append(", ").append(getName2());
            }
        } else {
            if (getName2() != null) {
                name.append(" ").append(getName2());
            }
            if (getName3() != null) {
                name.append(" ").append(getName3());
            }
        }
        return name.toString();
    }

    public abstract String getAdditionalAddressLine();

    public abstract void setAdditionalAddressLine(String additionalAddressLine);

    public abstract Boolean getIsPublic();

    public abstract void setIsPublic(Boolean isPublic);
}
