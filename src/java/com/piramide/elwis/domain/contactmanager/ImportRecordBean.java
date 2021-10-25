package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public abstract class ImportRecordBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setImportRecordId(PKGenerator.i.nextKey(ContactConstants.TABLE_IMPORTRECORD));
        setVersion(new Integer(1));

        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public ImportRecordBean() {
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

    public abstract String getCityName();

    public abstract void setCityName(String cityName);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getCountryName();

    public abstract void setCountryName(String countryName);

    public abstract String getHouseNumber();

    public abstract void setHouseNumber(String houseNumber);

    public abstract Integer getImportRecordId();

    public abstract void setImportRecordId(Integer importRecordId);

    public abstract Boolean getIsDuplicate();

    public abstract void setIsDuplicate(Boolean isDuplicate);

    public abstract String getName1();

    public abstract void setName1(String name1);

    public abstract String getName2();

    public abstract void setName2(String name2);

    public abstract String getName3();

    public abstract void setName3(String name3);

    public abstract Integer getProfileId();

    public abstract void setProfileId(Integer profileId);

    public abstract Integer getRecordIndex();

    public abstract void setRecordIndex(Integer recordIndex);

    public abstract String getStreet();

    public abstract void setStreet(String street);

    public abstract String getZip();

    public abstract void setZip(String zip);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract String getIdentityKey();

    public abstract void setIdentityKey(String identityKey);

    public abstract Integer getRecordType();

    public abstract void setRecordType(Integer recordType);

    public abstract Collection getRecordColumns();

    public abstract void setRecordColumns(Collection recordColumns);

    public abstract Collection getRecordDuplicates();

    public abstract void setRecordDuplicates(Collection recordDuplicates);

    public abstract Integer getParentRecordId();

    public abstract void setParentRecordId(Integer parentRecordId);

    public abstract Collection getChildImportRecords();

    public abstract void setChildImportRecords(Collection childImportRecords);

    public abstract ImportRecord getParentImportRecord();

    public abstract void setParentImportRecord(ImportRecord parentImportRecord);

    public abstract String getFunction();

    public abstract void setFunction(String function);

    public abstract Integer getOrganizationId();

    public abstract void setOrganizationId(Integer organizationId);

    public abstract Integer ejbSelectCountByProfileId(Integer profileId) throws FinderException;

    public Integer ejbHomeSelectCountByProfileId(Integer profileId) throws FinderException {
        return ejbSelectCountByProfileId(profileId);
    }

    public abstract Integer ejbSelectCountByProfileIdDuplicates(Integer profileId) throws FinderException;

    public Integer ejbHomeSelectCountByProfileIdDuplicates(Integer profileId) throws FinderException {
        return ejbSelectCountByProfileIdDuplicates(profileId);
    }

}
