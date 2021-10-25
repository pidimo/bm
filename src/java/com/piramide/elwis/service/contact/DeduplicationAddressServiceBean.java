package com.piramide.elwis.service.contact;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.*;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.dto.contactmanager.DedupliContactDTO;
import com.piramide.elwis.dto.contactmanager.DuplicateAddressDTO;
import com.piramide.elwis.dto.contactmanager.DuplicateGroupDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.deduplication.DeduplicationItemWrapper;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.*;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class DeduplicationAddressServiceBean implements SessionBean {
    private Log log = LogFactory.getLog(this.getClass());

    private SessionContext ctx;

    public DeduplicationAddressServiceBean() {
    }

    public void ejbCreate() throws CreateException {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException {
        this.ctx = sessionContext;
    }

    public void ejbRemove() throws EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public boolean companyHasContactDuplicates(Integer companyId) {
        boolean hasDuplicates = false;
        DedupliContact dedupliContact = findDedupliContactByCompany(companyId);
        if (dedupliContact != null) {
            DuplicateGroupHome duplicateGroupHome = (DuplicateGroupHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DUPLICATEGROUP);
            try {
                Collection collection = duplicateGroupHome.findByDedupliContactId(dedupliContact.getDedupliContactId());
                if (!collection.isEmpty()) {
                    hasDuplicates = true;
                }
            } catch (FinderException ignore) {
            }
        }
        return hasDuplicates;
    }

    public void emptyContactDuplicates(Integer companyId) {
        DuplicateGroupHome duplicateGroupHome = (DuplicateGroupHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DUPLICATEGROUP);

        DedupliContact dedupliContact = findDedupliContactByCompany(companyId);

        //init transaction
        UserTransaction transaction = ctx.getUserTransaction();
        try {
            //define timeout 60 min in seconds
            transaction.setTransactionTimeout(3600);
            transaction.begin();

            try {
                if (dedupliContact != null) {
                    try {
                        Collection duplicateGroups = duplicateGroupHome.findByDedupliContactId(dedupliContact.getDedupliContactId());
                        for (Iterator iterator = duplicateGroups.iterator(); iterator.hasNext();) {
                            DuplicateGroup duplicateGroup = (DuplicateGroup) iterator.next();
                            duplicateGroup.remove();
                        }
                    } catch (FinderException ignore) {
                    }
                }

            } catch (Exception e) {
                transaction.rollback();
                transaction.setTransactionTimeout(0);
                log.error("Error in empty contact duplicates.. ", e);
            }

            transaction.commit();
            transaction.setTransactionTimeout(0);
        } catch (Exception e) {
            log.error("Unexpected error.. ", e);
            throw new RuntimeException(e);
        }
    }

    public Integer initializeContactDeduplication(Integer companyId, Integer userId) {
        Integer dedupliContactId = null;
        DedupliContact dedupliContact = findDedupliContactByCompany(companyId);

        //init transaction
        UserTransaction transaction = ctx.getUserTransaction();

        try {
            transaction.begin();
            try {
                if (dedupliContact == null) {
                    dedupliContact = createDedupliContact(companyId, userId);
                } else {
                    dedupliContact.setUserId(userId);
                    dedupliContact.setStartTime(System.currentTimeMillis());
                    dedupliContact.setStatus(ContactConstants.DedupliContactStatus.PROCESS.getConstant());
                }

                dedupliContactId = dedupliContact.getDedupliContactId();
            } catch (Exception e) {
                transaction.rollback();
                log.error("Error in initialize deduplication.. ", e);
            }
            transaction.commit();
        } catch (Exception e) {
            log.error("Unexpected error.. ", e);
            throw new RuntimeException(e);
        }

        return dedupliContactId;
    }

    public void finishContactDeduplication(Integer dedupliContactId) {
        UserTransaction transaction = ctx.getUserTransaction();
        try {
            transaction.begin();
            try {
                DedupliContact dedupliContact = findDedupliContact(dedupliContactId);
                dedupliContact.setStatus(ContactConstants.DedupliContactStatus.COMPLETED.getConstant());
            } catch (Exception e) {
                transaction.rollback();
                log.error("Error in update dedupliContact.. ", e);
            }
            transaction.commit();
        } catch (Exception e) {
            log.error("Unexpected error.. ", e);
            throw new RuntimeException(e);
        }
    }

    public void createProcessedDuplicateAddress(Integer dedupliContactId, Integer companyId, List<List<Integer>> addressIdDupliList) {
        UserTransaction transaction = ctx.getUserTransaction();
        try {
            if (addressIdDupliList.size() > 400) {
                int timeSeconds = addressIdDupliList.size();
                transaction.setTransactionTimeout(timeSeconds);
            }

            transaction.begin();
            try {
                for (List<Integer> addressIdList : addressIdDupliList) {
                    DuplicateGroup duplicateGroup = createDuplicateGroup(dedupliContactId, companyId);
                    for (int j = 0; j < addressIdList.size(); j++) {
                        Integer addressId = addressIdList.get(j);
                        boolean isMain = (j == 0);
                        Integer positionIndex = j + 1;
                        createDuplicateAddress(duplicateGroup.getDuplicateGroupId(), addressId, companyId, isMain, positionIndex);
                    }
                }
            } catch (Exception e) {
                transaction.rollback();
                transaction.setTransactionTimeout(0);
                log.error("Error when create duplicate address.. ", e);
            }

            transaction.commit();
            transaction.setTransactionTimeout(0);
        } catch (Exception e) {
            log.error("Unexpected error happen  ", e);
            throw new RuntimeException(e);
        }
    }

    private DedupliContact createDedupliContact(Integer companyId, Integer userId) throws CreateException {
        DedupliContactHome dedupliContactHome = (DedupliContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DEDUPLICONTACT);
        DedupliContactDTO dto = new DedupliContactDTO();
        dto.put("companyId", companyId);
        dto.put("userId", userId);
        dto.put("startTime", System.currentTimeMillis());
        dto.put("status", ContactConstants.DedupliContactStatus.PROCESS.getConstant());
        return dedupliContactHome.create(dto);
    }

    private DuplicateGroup createDuplicateGroup(Integer dedupliContactId, Integer companyId) throws CreateException {
        DuplicateGroupHome duplicateGroupHome = (DuplicateGroupHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DUPLICATEGROUP);
        DuplicateGroupDTO duplicateGroupDTO = new DuplicateGroupDTO();
        duplicateGroupDTO.put("companyId", companyId);
        duplicateGroupDTO.put("dedupliContactId", dedupliContactId);
        return duplicateGroupHome.create(duplicateGroupDTO);
    }

    private DuplicateAddress createDuplicateAddress(Integer duplicateGroupId, Integer addressId, Integer companyId, boolean isMain, Integer index) throws CreateException {
        DuplicateAddressHome duplicateAddressHome = (DuplicateAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DUPLICATEADDRESS);
        DuplicateAddressDTO duplicateAddressDTO = new DuplicateAddressDTO();
        duplicateAddressDTO.put("duplicateGroupId", duplicateGroupId);
        duplicateAddressDTO.put("addressId", addressId);
        duplicateAddressDTO.put("companyId", companyId);
        duplicateAddressDTO.put("isMain", isMain);
        duplicateAddressDTO.put("positionIndex", index);

        return duplicateAddressHome.create(duplicateAddressDTO);
    }


    public List<Integer> getImportRecordDuplicateAddressIds(Integer importRecordId) {
        List<Integer> addressIdList = new ArrayList<Integer>();

        RecordDuplicateHome recordDuplicateHome = (RecordDuplicateHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_RECORDDUPLICATE);
        Collection recordDuplicateCollection;
        try {
            recordDuplicateCollection = recordDuplicateHome.findByImportRecordId(importRecordId);
        } catch (FinderException e) {
            log.debug("Not found record duplicate with id: " + importRecordId, e);
            recordDuplicateCollection = new ArrayList();
        }

        for (Iterator iterator = recordDuplicateCollection.iterator(); iterator.hasNext();) {
            RecordDuplicate recordDuplicate = (RecordDuplicate) iterator.next();
            addressIdList.add(recordDuplicate.getAddressId());
        }
        return addressIdList;
    }

    public List<Column> getImportProfileColumns(Integer profileId) {
        List<Column> result = new ArrayList<Column>();

        DataImportService dataImportService = getDataImportService();
        ImportProfile importProfile = findImportProfile(profileId);

        if (importProfile != null && dataImportService!= null) {
            List<CompoundGroup> columnConfiguration = dataImportService.getColumnStructureConfigurationByProfileType(importProfile.getProfileType(), importProfile.getCompanyId());

            List<ImportColumn> importColumnList = getImportColumns(importProfile.getProfileId(), importProfile.getCompanyId());

            for (ImportColumn importColumn : importColumnList) {
                Column column = null;
                for (CompoundGroup compoundGroup : columnConfiguration) {
                    column = compoundGroup.getColumn(importColumn.getGroupId(), importColumn.getColumnId());
                    if (column != null) {
                        break;
                    }
                }

                if (column == null) {
                    //if import column not exist in configuration
                    return new ArrayList<Column>();
                }

                Column newColumn = column.getCopy();
                newColumn.setImportColumnId(importColumn.getImportColumnId());
                newColumn.setPosition(importColumn.getColumnValue());

                result.add(newColumn);
            }
        }
        return result;
    }

    public List<Column> getImportProfileColumnsAddressFixed(Integer profileId) {
        List<Column> addressColumns = new ArrayList<Column>();
        List<Column> allColumns = getImportProfileColumns(profileId);

        for (Column column : allColumns) {
            if (!Column.ColumnType.CONTACT_PERSON.equals(column.getType())) {
                addressColumns.add(column);
            }
        }
        return addressColumns;
    }

    public List<Column> getImportProfileColumnsContactPersonFixed(Integer profileId) {
        List<Column> columnList = new ArrayList<Column>();
        List<Column> allColumns = getImportProfileColumns(profileId);

        for (Column column : allColumns) {
            if (Column.ColumnType.CONTACT_PERSON.equals(column.getType())) {
                columnList.add(column);
            }
        }
        return columnList;
    }

    public List<DeduplicationItemWrapper> readImportRecordColumnValues(Integer importRecordId, List<Column> profileColumns) {
        List<DeduplicationItemWrapper> itemWrapperList = new ArrayList<DeduplicationItemWrapper>();

        for (Column column : profileColumns) {
            RecordColumn recordColumn = findRecordColumn(column.getImportColumnId(), importRecordId);

            String value = null;
            if (recordColumn != null) {
                value = recordColumn.getColumnValue();
            }

            DeduplicationItemWrapper itemWrapper = composeItemWrapper(column, value);
            itemWrapperList.add(itemWrapper);
        }
        return itemWrapperList;
    }

    public List<DeduplicationItemWrapper> readAddressColumnValues(Integer addressId, Integer organizationId, List<Column> profileColumns) {
        List<DeduplicationItemWrapper> itemWrapperList = new ArrayList<DeduplicationItemWrapper>();

        Address address = findAddress(addressId);
        if (address != null) {
            for (Column column : profileColumns) {
                String value = readAddressColumnValue(address, organizationId, column);
                DeduplicationItemWrapper itemWrapper = composeItemWrapper(column, value, true);

                itemWrapperList.add(itemWrapper);
            }
        }
        return itemWrapperList;
    }

    public List<Integer> getContactDuplicateAddressIds(Integer duplicateGroupId) {
        List<Integer> addressIdList = new ArrayList<Integer>();

        DuplicateAddressHome duplicateAddressHome = (DuplicateAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DUPLICATEADDRESS);
        Collection collection;
        try {
            collection = duplicateAddressHome.findByDuplicateGroupId(duplicateGroupId);
        } catch (FinderException e) {
            log.debug("Not found duplicate address...", e);
            collection = new ArrayList();
        }

        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            DuplicateAddress duplicateAddress = (DuplicateAddress) iterator.next();
            addressIdList.add(duplicateAddress.getAddressId());
        }
        return addressIdList;
    }

    private DeduplicationItemWrapper composeItemWrapper(Column column, String value) {
        return composeItemWrapper(column, value, false);
    }

    private DeduplicationItemWrapper composeItemWrapper(Column column, String value, boolean isUnformattedValue) {

        DeduplicationItemWrapper itemWrapper = new DeduplicationItemWrapper(column.getPosition(), validValue(value));

        if (column instanceof StaticColumn) {
            itemWrapper.setEjbFieldName(((StaticColumn) column).getEjbFieldName());
        }

        if (isUnformattedValue) {
            if (column instanceof CategoryDinamicColumn) {
                CategoryDinamicColumn categoryDinamicColumn = (CategoryDinamicColumn) column;
                Integer categoryId = categoryDinamicColumn.getColumnId();

                Category category = findCategory(categoryId);
                if (category != null) {
                    if (CatalogConstants.CategoryType.DATE.getConstantAsInt() == category.getCategoryType()) {
                        itemWrapper.setIsDateValue(true);
                    }
                }
            }
        }

        return itemWrapper;
    }

    private String validValue(String value) {
        return (value != null) ? value : "";
    }

    private String readAddressColumnValue(Address address, Integer organizationId, Column column) {
        String value = null;

        if (Column.ColumnType.ADDRESS.equals(column.getType())) {
            if (column instanceof StaticColumn) {
                StaticColumn staticColumn = (StaticColumn) column;
                String ejbFieldName = staticColumn.getEjbFieldName();
                value = readAddressFieldValue(address, ejbFieldName);

            } else if (column instanceof CategoryDinamicColumn) {
                CategoryDinamicColumn categoryDinamicColumn = (CategoryDinamicColumn) column;
                Integer categoryId = categoryDinamicColumn.getColumnId();
                value = readAddressCategoryValue(address.getAddressId(), categoryId, address.getCompanyId());

            } else if (column instanceof TelecomDinamicColumn) {
                TelecomDinamicColumn telecomDinamicColumn = (TelecomDinamicColumn) column;
                Integer telecomTypeId = telecomDinamicColumn.getColumnId();
                value = readTelecomsValue(address.getAddressId(), null, telecomTypeId);
            }

        } else if (Column.ColumnType.CUSTOMER.equals(column.getType())) {
            Customer customer = findCustomer(address.getAddressId());

            if (customer != null) {
                if (column instanceof StaticColumn) {
                    StaticColumn staticColumn = (StaticColumn) column;
                    String ejbFieldName = staticColumn.getEjbFieldName();
                    value = readCustomerFieldValue(customer, ejbFieldName);

                } else if (column instanceof CategoryDinamicColumn) {
                    CategoryDinamicColumn categoryDinamicColumn = (CategoryDinamicColumn) column;
                    Integer categoryId = categoryDinamicColumn.getColumnId();
                    value = readCustomerCategoryValue(customer.getCustomerId(), categoryId, customer.getCompanyId());
                }
            }
        }  else if (Column.ColumnType.CONTACT_PERSON.equals(column.getType()) && organizationId != null) {
            ContactPerson contactPerson = findContactPerson(organizationId, address.getAddressId());

            if (column instanceof StaticColumn) {
                StaticColumn staticColumn = (StaticColumn) column;
                String ejbFieldName = staticColumn.getEjbFieldName();
                value = readContactPersonFieldValue(contactPerson, address, ejbFieldName);
            }

            if (contactPerson != null) {
                if (column instanceof CategoryDinamicColumn) {
                    CategoryDinamicColumn categoryDinamicColumn = (CategoryDinamicColumn) column;
                    Integer categoryId = categoryDinamicColumn.getColumnId();
                    value = readContactPersonCategoryValue(contactPerson.getAddressId(), contactPerson.getContactPersonId(), categoryId, contactPerson.getCompanyId());

                } else if (column instanceof TelecomDinamicColumn) {
                    TelecomDinamicColumn telecomDinamicColumn = (TelecomDinamicColumn) column;
                    Integer telecomTypeId = telecomDinamicColumn.getColumnId();
                    value = readTelecomsValue(contactPerson.getAddressId(), contactPerson.getContactPersonId(), telecomTypeId);
                }
            }
        }

        return value;
    }

    private String readAddressFieldValue(Address address, String ejbFieldName) {
        String value = "";

        if (address != null) {
            if ("name1".equals(ejbFieldName)) {
                value = address.getName1();
            } else if ("name2".equals(ejbFieldName)) {
                value = address.getName2();
            } else if ("name3".equals(ejbFieldName)) {
                value = address.getName3();
            } else if ("countryId".equals(ejbFieldName)) {
                Country country = findCountry(address.getCountryId());
                if (country != null) {
                    value = country.getCountryName();
                }
            } else if ("cityId".equals(ejbFieldName)) {
                City city = findCity(address.getCityId());
                if (city != null) {
                    value = city.getCityName();
                }
            } else if ("cityZip".equals(ejbFieldName)) {
                City city = findCity(address.getCityId());
                if (city != null) {
                    value = city.getCityZip();
                }
            } else if ("street".equals(ejbFieldName)) {
                value = address.getStreet();
            } else if ("houseNumber".equals(ejbFieldName)) {
                value = address.getHouseNumber();
            } else if ("titleId".equals(ejbFieldName)) {
                Title title = address.getTitle();
                if (title != null) {
                    value = title.getTitleName();
                }
            } else if ("salutationId".equals(ejbFieldName)) {
                Salutation salutation = address.getSalutation();
                if (salutation != null) {
                    value = salutation.getSalutationLabel();
                }
            } else if ("education".equals(ejbFieldName)) {
                value = address.getEducation();
            } else if ("keywords".equals(ejbFieldName)) {
                value = address.getKeywords();
            } else if ("searchName".equals(ejbFieldName)) {
                value = address.getSearchName();
            } else if ("languageId".equals(ejbFieldName)) {
                Language language = address.getLanguage();
                if (language != null) {
                    value = language.getLanguageName();
                }
            }
        }

        return value;
    }

    private String readContactPersonFieldValue(ContactPerson contactPerson, Address address, String ejbFieldName) {
        String value = null;

        if (contactPerson != null) {
            if ("function".equals(ejbFieldName)) {
                value = contactPerson.getFunction();
            }
        }

        if (value == null) {
            value = readAddressFieldValue(address, ejbFieldName);
        }
        return value;
    }

    private String readCustomerFieldValue(Customer customer, String ejbFieldName) {
        String value = "";

        if (customer != null) {
            if ("number".equals(ejbFieldName)) {
                value = customer.getNumber();
            } else if ("sourceId".equals(ejbFieldName)) {
                AddressSource addressSource = findAddressSource(customer.getSourceId());
                if (addressSource != null) {
                    value = addressSource.getAddressSourceName();
                }
            } else if ("branchId".equals(ejbFieldName)) {
                Branch branch = findBranch(customer.getBranchId());
                if (branch != null) {
                    value = branch.getBranchName();
                }
            } else if ("customerTypeId".equals(ejbFieldName)) {
                CustomerType customerType = findCustomerType(customer.getCustomerTypeId());
                if (customerType != null) {
                    value = customerType.getCustomerTypeName();
                }
            }
        }

        return value;
    }



    private Address findAddress(Integer addressId) {
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        try {
            return addressHome.findByPrimaryKey(addressId);
        } catch (FinderException e) {
            log.debug("Not found Address with id: " + addressId, e);
            return null;
        }
    }

    private ContactPerson findContactPerson(Integer addressId, Integer contactPersonId) {
        ContactPersonHome contactPersonHome = (ContactPersonHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTPERSON);
        if (addressId != null && contactPersonId != null) {
            try {
                ContactPersonPK contactPersonPK = new ContactPersonPK(addressId, contactPersonId);
                return contactPersonHome.findByPrimaryKey(contactPersonPK);
            } catch (FinderException e) {
                log.debug("Contact person not found with id: " + addressId + " " + contactPersonId + " " + e);
            }
        }
        return null;
    }

    private Customer findCustomer(Integer customerId) {
        CustomerHome home = (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);
        try {
            return home.findByPrimaryKey(customerId);
        } catch (FinderException e) {
            log.debug("The Customer not found with id: " + customerId + " " + e);
            return null;
        }
    }

    private Country findCountry(Integer countryId) {
        Country country = null;
        if (countryId != null) {
            CountryHome countryHome = (CountryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_COUNTRY);
            try {
                country = countryHome.findByPrimaryKey(countryId);
            } catch (FinderException e) {
                log.debug("Error in find country " + countryId, e);
            }
        }
        return country;
    }

    private City findCity(Integer cityId) {
        City city = null;
        if (cityId != null) {
            CityHome cityHome = (CityHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CITY);
            try {
                city = cityHome.findByPrimaryKey(cityId);
            } catch (FinderException e) {
                log.debug("Error in find city " + cityId, e);
            }
        }
        return city;
    }

    private AddressSource findAddressSource(Integer sourceId) {
        AddressSource addressSource = null;
        if (sourceId != null) {
            AddressSourceHome addressSourceHome = (AddressSourceHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_ADDRESSSOURCE);
            try {
                addressSource = addressSourceHome.findByPrimaryKey(sourceId);
            } catch (FinderException e) {
                log.debug("Error in find addressSource " + sourceId, e);
            }
        }
        return addressSource;
    }

    private Branch findBranch(Integer branchId) {
        Branch branch = null;
        if (branchId != null) {
            BranchHome branchHome = (BranchHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_BRANCH);
            try {
                branch = branchHome.findByPrimaryKey(branchId);
            } catch (FinderException e) {
                log.debug("Error in find Branch " + branchId, e);
            }
        }
        return branch;
    }

    private CustomerType findCustomerType(Integer customerTypeId) {
        CustomerType customerType = null;
        if (customerTypeId != null) {
            CustomerTypeHome customerTypeHome = (CustomerTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CUSTOMERTYPE);
            try {
                customerType = customerTypeHome.findByPrimaryKey(customerTypeId);
            } catch (FinderException e) {
                log.debug("Error in find customerType " + customerTypeId, e);
            }
        }
        return customerType;
    }

    private TelecomType findTelecomType(Integer telecomTypeId) {
        TelecomTypeHome telecomTypeHome = (TelecomTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TELECOMTYPE);
        if (telecomTypeId != null) {
            try {
                return telecomTypeHome.findByPrimaryKey(telecomTypeId);
            } catch (FinderException e) {
                log.debug("Error in find TelecomType telecomTypeId=" + telecomTypeId, e);
            }
        }
        return null;
    }

    private String readTelecomsValue(Integer addressId, Integer contactPersonId, Integer telecomTypeId) {
        String telecomValues = "";
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

        TelecomType telecomType = findTelecomType(telecomTypeId);
        if (telecomType != null) {

            Collection telecoms;
            try {
                if (contactPersonId != null) {
                    telecoms = telecomHome.findAllContactPersonTelecomsByTypeId(addressId, contactPersonId, telecomType.getTelecomTypeId());
                } else {
                    telecoms = telecomHome.findAllAddressTelecomsByTypeId(addressId, telecomType.getTelecomTypeId());
                }
            } catch (FinderException e) {
                log.debug("Not found telecoms " + e);
                telecoms = new ArrayList();
            }

            for (Iterator iterator2 = telecoms.iterator(); iterator2.hasNext();) {
                Telecom telecom = (Telecom) iterator2.next();
                telecomValues += telecom.getData();

                if (iterator2.hasNext()) {
                    telecomValues += "\n";
                }
            }
        }
        return telecomValues;
    }

    private String readAddressCategoryValue(Integer addressId, Integer categoryId, Integer companyId) {
        CategoryFieldValueHome fieldValueHome = (CategoryFieldValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYFIELDVALUE);

        Collection catFieldValues = null;
        try {
            catFieldValues = fieldValueHome.findByAddressIdCategoryId(addressId, categoryId, companyId);
        } catch (FinderException e) {
            log.debug("Not found values.. " + e);
            catFieldValues = new ArrayList();
        }
        return readCategoryFieldValue(new ArrayList<CategoryFieldValue>(catFieldValues));
    }

    private String readContactPersonCategoryValue(Integer addressId, Integer contactPersonId, Integer categoryId, Integer companyId) {
        CategoryFieldValueHome fieldValueHome = (CategoryFieldValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYFIELDVALUE);

        Collection catFieldValues = null;
        try {
            catFieldValues = fieldValueHome.findByAddressIdContactPersonIdCategoryId(addressId, contactPersonId, categoryId, companyId);
        } catch (FinderException e) {
            log.debug("Not found values.. " + e);
            catFieldValues = new ArrayList();
        }
        return readCategoryFieldValue(new ArrayList<CategoryFieldValue>(catFieldValues));
    }

    private String readCustomerCategoryValue(Integer customerId, Integer categoryId, Integer companyId) {
        CategoryFieldValueHome fieldValueHome = (CategoryFieldValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYFIELDVALUE);

        Collection catFieldValues = null;
        try {
            catFieldValues = fieldValueHome.findByCustomerIdCategoryId(customerId, categoryId, companyId);
        } catch (FinderException e) {
            log.debug("Not found values.. " + e);
            catFieldValues = new ArrayList();
        }
        return readCategoryFieldValue(new ArrayList<CategoryFieldValue>(catFieldValues));
    }

    private String readCategoryFieldValue(List<CategoryFieldValue> categoryFieldValues) {
        String result = "";

        CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);

        for (CategoryFieldValue fieldValue : categoryFieldValues) {

            //read category associated
            Category category = null;
            try {
                category = categoryHome.findByPrimaryKey(fieldValue.getCategoryId());
            } catch (FinderException e) {
                log.debug("Not found category...");
            }
            if (category != null) {

                //setting up object value according type of category
                Object value = null;
                if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() == category.getCategoryType()
                        || CatalogConstants.CategoryType.SINGLE_SELECT.getConstantAsInt() == category.getCategoryType()) {
                    CategoryValue categoryValue = findCategoryValue(fieldValue.getCategoryValueId());
                    if (categoryValue != null) {
                        value = categoryValue.getCategoryValueName();
                    }
                } else if (CatalogConstants.CategoryType.DATE.getConstantAsInt() == category.getCategoryType()) {
                    value = fieldValue.getDateValue();
                } else if (CatalogConstants.CategoryType.DECIMAL.getConstantAsInt() == category.getCategoryType()) {
                    value = fieldValue.getDecimalValue();
                } else if (CatalogConstants.CategoryType.INTEGER.getConstantAsInt() == category.getCategoryType()) {
                    value = fieldValue.getIntegerValue();
                } else if (CatalogConstants.CategoryType.TEXT.getConstantAsInt() == category.getCategoryType()) {
                    value = fieldValue.getStringValue();
                } else if (CatalogConstants.CategoryType.LINK_VALUE.getConstantAsInt() == category.getCategoryType()) {
                    value = fieldValue.getLinkValue();
                } else if (CatalogConstants.CategoryType.FREE_TEXT.getConstantAsInt() == category.getCategoryType()) {
                    value = null;
                    if (null != fieldValue.getFreeTextId()) {
                        value = readFreeText(fieldValue.getFreeTextId());
                    }
                } else if (CatalogConstants.CategoryType.ATTACH.getConstantAsInt() == category.getCategoryType()) {
                    value = "";
                    if (null != fieldValue.getFilename()) {
                        value = fieldValue.getFilename();
                    }
                }

                //compose result value
                if (value != null) {
                    if (!result.isEmpty()) {
                        result += ", ";
                    }
                    result += value.toString();
                }
            }
        }

        return result;
    }

    private CategoryValue findCategoryValue(Integer categoryValueId) {
        CategoryValue categoryValue = null;
        if (categoryValueId != null) {
            CategoryValueHome categoryValueHome = (CategoryValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYVALUE);

            try {
                categoryValue = categoryValueHome.findByPrimaryKey(categoryValueId);
            } catch (FinderException e) {
                log.debug("Error in find categoryValue " + categoryValueId, e);
            }
        }
        return categoryValue;
    }

    private Category findCategory(Integer categoryId) {
        CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);
        if (categoryId != null) {
            try {
                return categoryHome.findByPrimaryKey(categoryId);
            } catch (FinderException e) {
                log.debug("Error in find category " + categoryId + " " + e);
            }
        }
        return null;
    }

    private String readFreeText(Integer freeTextId) {
        FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
        String text = "";
        try {
            FreeText freeText = freeTextHome.findByPrimaryKey(freeTextId);
            text = new String(freeText.getValue());
        } catch (FinderException e) {
            log.debug("->Read FreeText freeTextId=" + freeTextId + " FAIL");
        }
        return text;
    }

    private ImportProfile findImportProfile(Integer profileId) {
        ImportProfileHome importProfileHome = (ImportProfileHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTPROFILE);
        if (profileId != null) {
            try {
                return importProfileHome.findByPrimaryKey(profileId);
            } catch (FinderException e) {
                log.debug("Error in find import profile " + profileId, e);
            }
        }
        return null;
    }

    private DataImportService getDataImportService() {
        DataImportServiceHome home = (DataImportServiceHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DATAIMPORTSERVICE);
        try {
            return home.create();
        } catch (CreateException e) {
            log.debug("-> Create DataImportService Fail ", e);
        }
        return null;
    }

    private List<ImportColumn> getImportColumns(Integer profileId, Integer companyId) {
        ImportColumnHome importColumnHome = (ImportColumnHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTCOLUMN);
        try {
            return (List<ImportColumn>) importColumnHome.findByProfileIdOrderByPosition(profileId, companyId);
        } catch (FinderException e) {
            return new ArrayList<ImportColumn>();
        }
    }

    private RecordColumn findRecordColumn(Integer importColumnId, Integer importRecordId) {
        RecordColumnHome recordColumnHome = (RecordColumnHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_RECORDCOLUMN);
        RecordColumnPK recordColumnPK = new RecordColumnPK(importColumnId, importRecordId);
        try {
            return recordColumnHome.findByPrimaryKey(recordColumnPK);
        } catch (FinderException e) {
            log.debug("Error in find record column importColumnId:" + importColumnId + " importRecordId:" + importRecordId + " " + e);
        }
        return null;
    }

    private DedupliContact findDedupliContact(Integer dedupliContactId) {
        DedupliContactHome dedupliContactHome = (DedupliContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DEDUPLICONTACT);
        if (dedupliContactId != null) {
            try {
                return dedupliContactHome.findByPrimaryKey(dedupliContactId);
            } catch (FinderException e) {
                log.debug("Error in find dedupli contact: " + dedupliContactId, e);
            }
        }
        return null;
    }

    private DedupliContact findDedupliContactByCompany(Integer companyId) {
        DedupliContactHome dedupliContactHome = (DedupliContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DEDUPLICONTACT);
        DedupliContact dedupliContact = null;
        try {
            Collection collection = dedupliContactHome.findByCompanyId(companyId);
            if (!collection.isEmpty()) {
                dedupliContact = (DedupliContact) collection.iterator().next();
            }
        } catch (FinderException ignore) {
        }
        return dedupliContact;
    }
}
