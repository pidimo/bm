package com.piramide.elwis.service.contact;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.contactmanager.ContactPersonCreateCmd;
import com.piramide.elwis.cmd.contactmanager.CustomerUtilCmd;
import com.piramide.elwis.cmd.contactmanager.ImportAddressCmd;
import com.piramide.elwis.cmd.contactmanager.ImportRecordCmd;
import com.piramide.elwis.cmd.contactmanager.dataimport.build.BuildStructure;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.*;
import com.piramide.elwis.cmd.contactmanager.dataimport.filemanager.DataRow;
import com.piramide.elwis.cmd.contactmanager.dataimport.filemanager.FileManager;
import com.piramide.elwis.cmd.contactmanager.dataimport.validator.ImportErrorsException;
import com.piramide.elwis.cmd.contactmanager.dataimport.validator.ValidationException;
import com.piramide.elwis.domain.admin.UserGroup;
import com.piramide.elwis.domain.admin.UserGroupHome;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.domain.contactmanager.TelecomType;
import com.piramide.elwis.dto.catalogmanager.*;
import com.piramide.elwis.dto.contactmanager.*;
import com.piramide.elwis.service.contact.utils.DeduplicationNativeSqlFactory;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.utils.deduplication.ImportMergeField;
import com.piramide.elwis.utils.deduplication.ImportMergeWrapper;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;


/**
 * @author Ivan Alban
 * @version 4.2.1
 */
public class DataImportServiceBean implements SessionBean {
    private Log log = LogFactory.getLog(this.getClass());

    private Map<String, Integer> organizationCache = new HashMap<String, Integer>();
    private Map<String, Integer> personCache = new HashMap<String, Integer>();

    private SessionContext ctx;

    public DataImportServiceBean() {
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

    public CompoundGroup getOrganizationGroup(Integer companyId) {
        BuildStructure buildBuildStructure = new BuildStructure();
        return buildBuildStructure.buildOrganizationStructure(companyId);
    }

    public CompoundGroup getContactGroup(Integer companyId) {
        BuildStructure buildStructure = new BuildStructure();
        return buildStructure.buildContactStructure(companyId);
    }

    public List getOrganizationAndContactPersonGroups(Integer companyId) {
        BuildStructure buildStructure = new BuildStructure();
        return buildStructure.builOrganizationAndContactPersonStructure(companyId);
    }

    /**
     * Invoque methods according to <code>profileType</code> and
     * <code>companyId</code> to obtain the associated configuration.
     *
     * @param profileType <code>Integer</code> value that is associated to <code>ContactConstants.ImportProfileType</code>
     * @param companyId   <code>Integer</code> value that is the company identifier
     * @return <code>List</code> of <code>CompoundGroup</code> that represents the configuration associated to
     *         <code>profileType</code> parameter.
     */
    public List<CompoundGroup> getColumnStructureConfigurationByProfileType(Integer profileType, Integer companyId) {
        List<CompoundGroup> result = new ArrayList<CompoundGroup>();

        if (ContactConstants.ImportProfileType.ORGANIZATION.equal(profileType)) {
            result.add(getOrganizationGroup(companyId));
        }
        if (ContactConstants.ImportProfileType.PERSON.equal(profileType)) {
            result.add(getContactGroup(companyId));
        }
        if (ContactConstants.ImportProfileType.ORGANIZATION_AND_CONTACT_PERSON.equal(profileType)) {
            result.addAll(getOrganizationAndContactPersonGroups(companyId));
        }
        return result;
    }

    public ImportProfileDTO getImportProfileDTO(Integer profileId) {
        ImportProfileDTO importProfileDTO = null;

        ImportProfile importProfile = findImportProfile(profileId);
        if (importProfile != null) {
            importProfileDTO = new ImportProfileDTO();
            DTOFactory.i.copyToDTO(importProfile, importProfileDTO);
        }
        return importProfileDTO;
    }

    public boolean importProfileContainDuplicateRecords(Integer profileId) {
        ImportRecordHome importRecordHome = (ImportRecordHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTRECORD);
        if (profileId != null) {
            try {
                Integer importRecordDuplicates = importRecordHome.selectCountByProfileIdDuplicates(profileId);
                if (importRecordDuplicates > 0) {
                    return true;
                }
            } catch (FinderException ignore) {
            }
        }
        return false;
    }

    public Integer readImportProfileTotalRecord(Long importStartTime) {
        Integer totalRecord = 0;

        if (importStartTime != null) {
            ImportProfileHome importProfileHome = (ImportProfileHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTPROFILE);
                try {
                    ImportProfile importProfile = importProfileHome.findByImportStartTime(importStartTime);
                    if (importProfile != null) {
                        if (importProfile.getTotalRecord() != null) {
                            totalRecord = importProfile.getTotalRecord();
                        }
                        if (ContactConstants.ImportProfileType.ORGANIZATION_AND_CONTACT_PERSON.equal(importProfile.getProfileType())) {
                            totalRecord = totalRecord * 2;
                        }
                    }

                } catch (FinderException e) {
                    log.debug("Error in find import profile by start time:" + importStartTime, e);
                }
        }
        return totalRecord;
    }

    public void emptyImportProfileDuplicates(Integer profileId) {
        log.debug("Execute empty all profile import records...");

        ImportRecordHome importRecordHome = (ImportRecordHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTRECORD);

        //init transaction
        UserTransaction transaction = ctx.getUserTransaction();

        try {
            //define timeout 60 min in seconds
            transaction.setTransactionTimeout(3600);
            transaction.begin();

            if (profileId != null) {

                InitialContext initialContext = new InitialContext();
                DataSource dataSource = (DataSource) initialContext.lookup(Constants.JNDI_ELWISDS);
                Connection connection = dataSource.getConnection();

                connection.prepareStatement(DeduplicationNativeSqlFactory.i.deleteRecordColumn(profileId)).execute();
                connection.prepareStatement(DeduplicationNativeSqlFactory.i.deleteRecordDuplicate(profileId)).execute();
                connection.prepareStatement(DeduplicationNativeSqlFactory.i.deleteChildImportRecord(profileId)).execute();
                connection.prepareStatement(DeduplicationNativeSqlFactory.i.deleteImportRecord(profileId)).execute();

                connection.close();
            }

            transaction.commit();
            transaction.setTransactionTimeout(0);
        } catch (Exception e) {
            log.error("Error in empty import duplicates.. ", e);
            try {
                transaction.rollback();
                transaction.setTransactionTimeout(0);
            } catch (Exception e1) {
                log.error("Unexpected error.. ", e1);
            }
            throw new RuntimeException(e);
        }
    }

    public boolean isImportRecordDuplicate(Integer importRecordId) {
        ImportRecord importRecord = findImportRecord(importRecordId);
        return importRecord != null && importRecord.getIsDuplicate();
    }

    public boolean importRecordFixedHasDuplicateContactPerson(Integer importRecordId) {
        ImportRecordHome importRecordHome = (ImportRecordHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTRECORD);
        ImportRecord importRecord = findImportRecord(importRecordId);

        if (importRecord != null && importRecord.getOrganizationId() != null) {

            Collection childDuplicateRecords = null;
            try {
                childDuplicateRecords = importRecordHome.findByParentImportRecordIdDuplicates(importRecord.getImportRecordId(), importRecord.getCompanyId());
            } catch (FinderException ignore) {
                childDuplicateRecords = new ArrayList();
            }

            if (!childDuplicateRecords.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void importData(String cachePath,
                           List<Column> selectedColumns,
                           ArrayByteWrapper file,
                           String fileName,
                           DataImportConfiguration configuration,
                           Integer userId,
                           Integer companyId) throws ImportErrorsException {

        List<ValidationException> validationErrorList = new ArrayList<ValidationException>();

        UserTransaction transaction = ctx.getUserTransaction();

        FileManager fileManager = null;
        try {
            fileManager = new FileManager(file, fileName, cachePath, companyId, userId);
        } catch (ValidationException e) {
            log.debug("Error in read file....... ", e);
            validationErrorList.add(e);
            throw new ImportErrorsException(validationErrorList);
        }

        try {
            if (fileManager.getNumberOfRows() > 1000) {
                int time = Math.round(fileManager.getNumberOfRows() / 2);
                log.debug("-> Time : " + time);

                transaction.setTransactionTimeout(time);
            }

            transaction.begin();

            while (fileManager.hasMoreRows()) {
                DataRow data = fileManager.getNextRow();

                if (null == data) {
                    continue;
                }

                if (configuration.isSkipHeader() && 0 == data.getRowNumber()) {
                    continue;
                }

                log.debug("-> Validating #ROW=" + data.getRowNumber() + " Data " + data.getColumns());
                for (Column column : selectedColumns) {
                    try {
                        column.validate(configuration, data);
                    } catch (ValidationException validationException) {
                        validationErrorList.add(validationException);
                    }
                }

                if (validationErrorList.isEmpty()) {
                    try {

                        if (ContactConstants.ImportProfileType.ORGANIZATION.equal(configuration.getType())) {
                            List<Column> organizationColumns = getOrganizationColumns(selectedColumns);
                            List<StaticColumn> addressColumns = getAddressStaticColumns(organizationColumns);
                            List<DinamicColumn> addressCategoryColumns = getAddresCategoryColumns(organizationColumns);
                            List<DinamicColumn> addressTelecomColumns = getAddressTelecomColumns(organizationColumns);
                            List<StaticColumn> customerColumns = getCustomerStaticColumns(organizationColumns);
                            List<DinamicColumn> customerCategoryColumns = getCustomerCategoryColumns(selectedColumns);

                            createOrganization(addressColumns,
                                    addressCategoryColumns,
                                    addressTelecomColumns,
                                    customerColumns,
                                    customerCategoryColumns,
                                    data,
                                    userId,
                                    companyId,
                                    configuration,
                                    transaction);
                        }

                        if (ContactConstants.ImportProfileType.PERSON.equal(configuration.getType())) {
                            List<Column> contactColumns = getContactColumns(selectedColumns);
                            List<StaticColumn> addressColumns = getAddressStaticColumns(contactColumns);
                            List<DinamicColumn> addressCategoryColumns = getAddresCategoryColumns(contactColumns);
                            List<DinamicColumn> addressTelecomColumns = getAddressTelecomColumns(contactColumns);
                            List<StaticColumn> customerColumns = getCustomerStaticColumns(contactColumns);
                            List<DinamicColumn> customerCategoryColumns = getCustomerCategoryColumns(selectedColumns);

                            createContact(addressColumns,
                                    addressCategoryColumns,
                                    addressTelecomColumns,
                                    customerColumns,
                                    customerCategoryColumns,
                                    data,
                                    userId,
                                    companyId,
                                    configuration,
                                    transaction);
                        }

                        if (ContactConstants.ImportProfileType.ORGANIZATION_AND_CONTACT_PERSON.equal(configuration.getType())) {

                            List<Column> organizationColumns = getOrganizationColumns(selectedColumns);
                            List<StaticColumn> addressColumns = getAddressStaticColumns(organizationColumns);
                            List<DinamicColumn> addressCategoryColumns = getAddresCategoryColumns(organizationColumns);
                            List<DinamicColumn> addressTelecomColumns = getAddressTelecomColumns(organizationColumns);
                            List<StaticColumn> customerColumns = getCustomerStaticColumns(organizationColumns);
                            List<DinamicColumn> customerCategoryColumns = getCustomerCategoryColumns(selectedColumns);

                            Integer organizationId = createOrganization(addressColumns,
                                    addressCategoryColumns,
                                    addressTelecomColumns,
                                    customerColumns,
                                    customerCategoryColumns,
                                    data,
                                    userId,
                                    companyId,
                                    configuration,
                                    transaction);

                            List<Column> contactPersonColumns = getContactPersonColumns(selectedColumns);
                            List<StaticColumn> contactPersonStaticColumns = getContactPersonStaticColumns(contactPersonColumns);
                            List<DinamicColumn> contactPersonCategoriesColumns = getContactPersonCategoryColumns(contactPersonColumns);
                            List<DinamicColumn> contactPersonTelecomColumns = getContactPersonTelecomColumns(contactPersonColumns);
                            createContactPerson(contactPersonStaticColumns,
                                    contactPersonCategoriesColumns,
                                    contactPersonTelecomColumns,
                                    data,
                                    organizationId,
                                    userId,
                                    companyId,
                                    configuration,
                                    transaction);
                        }

                    } catch (Exception e) {
                        transaction.rollback();
                        log.error("Unexpected error happen when create Data " + data.getColumns(), e);
                        throw new RuntimeException(e);
                    }
                }
            }

            if (validationErrorList.isEmpty()) {
                transaction.commit();
                log.debug("-> All Data saved OK...");
                organizationCache.clear();
                personCache.clear();
                transaction.setTransactionTimeout(0);

            } else {
                fileManager.deleteFileFromCache();

                transaction.rollback();
                transaction.setTransactionTimeout(0);
                organizationCache.clear();
                personCache.clear();

                throw new ImportErrorsException(validationErrorList);
            }

        } catch (ImportErrorsException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error happen ", e);
            fileManager.deleteFileFromCache();
            throw new RuntimeException(e);
        }
    }

    public void importForDeduplicate(String cachePath,
                           List<Column> selectedColumns,
                           ArrayByteWrapper file,
                           String fileName,
                           DataImportConfiguration configuration,
                           Integer userId,
                           Integer companyId,
                           Integer profileId) throws ImportErrorsException {

        List<ValidationException> validationErrorList = new ArrayList<ValidationException>();

        FileManager fileManager = null;
        try {
            fileManager = new FileManager(file, fileName, cachePath, companyId, userId);
        } catch (ValidationException e) {
            log.debug("Error in read file....... ", e);
            validationErrorList.add(e);
            throw new ImportErrorsException(validationErrorList);
        }

        //first update the total record to update progress bar
        updateImportProfileTotalRecord(profileId, fileManager.getNumberOfRows());

        UserTransaction transaction = ctx.getUserTransaction();
        try {
            if (fileManager.getNumberOfRows() > 800) {
                int time = Math.round(fileManager.getNumberOfRows() / 2);
                log.debug("-> Time : " + time);

                transaction.setTransactionTimeout(time);
            }

            transaction.begin();

            while (fileManager.hasMoreRows()) {
                DataRow data = fileManager.getNextRow();

                if (null == data) {
                    continue;
                }

                if (configuration.isSkipHeader() && 0 == data.getRowNumber()) {
                    continue;
                }

                log.debug("-> Validating #ROW=" + data.getRowNumber() + " Data " + data.getColumns());
                for (Column column : selectedColumns) {
                    try {
                        column.validate(configuration, data);
                    } catch (ValidationException validationException) {
                        validationErrorList.add(validationException);
                    }
                }

                if (validationErrorList.isEmpty()) {
                    try {

                        if (ContactConstants.ImportProfileType.ORGANIZATION.equal(configuration.getType())) {
                            createOrganizationImportRecord(data, selectedColumns, companyId, profileId);
                        }

                        if (ContactConstants.ImportProfileType.PERSON.equal(configuration.getType())) {
                            createPersonImportRecord(data, selectedColumns, companyId, profileId);
                        }

                        if (ContactConstants.ImportProfileType.ORGANIZATION_AND_CONTACT_PERSON.equal(configuration.getType())) {

                            Integer importRecordId = createOrganizationImportRecord(data, selectedColumns, companyId, profileId);
                            createContactPersonImportRecord(importRecordId, data, selectedColumns, companyId, profileId);
                        }

                    } catch (Exception e) {
                        transaction.rollback();
                        log.error("Unexpected error happen when create Data " + data.getColumns(), e);
                        throw new RuntimeException(e);
                    }
                }
            }

            if (validationErrorList.isEmpty()) {
                transaction.commit();
                log.debug("-> All Data saved OK...");
                organizationCache.clear();
                personCache.clear();
                transaction.setTransactionTimeout(0);

            } else {
                fileManager.deleteFileFromCache();

                transaction.rollback();
                transaction.setTransactionTimeout(0);
                organizationCache.clear();
                personCache.clear();

                throw new ImportErrorsException(validationErrorList);
            }

        } catch (ImportErrorsException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error happen ", e);
            fileManager.deleteFileFromCache();
            throw new RuntimeException(e);
        }
    }

    private void updateImportProfileTotalRecord(Integer profileId, Integer totalRecord) {

        UserTransaction transaction = ctx.getUserTransaction();

        try {
            transaction.begin();

            ImportProfile importProfile = findImportProfile(profileId);
            if (importProfile != null) {
                importProfile.setTotalRecord(totalRecord);
            }

            transaction.commit();
        } catch (Exception e) {
            log.error("Error in update import profile total record.. ", e);
            try {
                transaction.rollback();
            } catch (Exception e1) {
                log.error("Unexpected error.. ", e1);
            }
        }
    }

    public void saveImportRecordWithoutDuplicate(Map<Integer, List<Integer>> importRecordDuplicatesMap,
                                                 Integer profileId,
                                                 List<Column> selectedColumns,
                                                 DataImportConfiguration configuration,
                                                 Integer userId,
                                                 Integer companyId) throws ValidationException {

        ImportRecordHome importRecordHome = (ImportRecordHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTRECORD);

        UserTransaction transaction = ctx.getUserTransaction();



        try {

            Integer countRecords = importRecordHome.selectCountByProfileId(profileId);

            if (countRecords > 1000) {
                int time = Math.round(countRecords / 2);
                log.debug("-> Time : " + time);
                transaction.setTransactionTimeout(time);
            }

            transaction.begin();

            try {

                createImportRecordDuplicate(importRecordDuplicatesMap, companyId);

                List<ImportRecord> importRecordList = getImportRecordsWithoutDuplicate(profileId, companyId);

                for (ImportRecord importRecord : importRecordList) {
                    saveImportRecord(importRecord, selectedColumns, configuration, userId, companyId, transaction);
                }
                removeNotDuplicateImportRecords(profileId);

            } catch (ValidationException validationException) {
                transaction.rollback();
                transaction.setTransactionTimeout(0);
                organizationCache.clear();
                personCache.clear();
                throw validationException;
            } catch (Exception e) {
                transaction.rollback();
                log.error("Unexpected error happen when create Data.. ", e);
                throw new RuntimeException(e);
            }

            transaction.commit();
            log.debug("-> All Data saved OK...");
            organizationCache.clear();
            personCache.clear();
            transaction.setTransactionTimeout(0);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error happen ", e);
            throw new RuntimeException(e);
        }
    }

    private void createImportRecordDuplicate(Map<Integer, List<Integer>> importRecordDuplicatesMap, Integer companyId) throws CreateException {
        for (Integer importRecordId : importRecordDuplicatesMap.keySet()) {
            List<Integer> addressIdList = importRecordDuplicatesMap.get(importRecordId);

            if (!addressIdList.isEmpty()) {
                ImportRecord importRecord = findImportRecord(importRecordId);
                if (importRecord != null) {
                    createRecordDuplicate(importRecordId, addressIdList, companyId);
                    importRecord.setIsDuplicate(true);
                }
            }
        }
    }

    private void createRecordDuplicate(Integer importRecordId, List<Integer> addressIdList, Integer companyId) throws CreateException {
        RecordDuplicateHome recordDuplicateHome = (RecordDuplicateHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_RECORDDUPLICATE);
        for (Integer addressId : addressIdList) {
            RecordDuplicateDTO recordDuplicateDTO = new RecordDuplicateDTO();
            recordDuplicateDTO.put("importRecordId", importRecordId);
            recordDuplicateDTO.put("addressId", addressId);
            recordDuplicateDTO.put("companyId", companyId);

            recordDuplicateHome.create(recordDuplicateDTO);
        }
    }

    private void saveImportRecord(ImportRecord importRecord,
                                  List<Column> selectedColumns,
                                  DataImportConfiguration configuration,
                                  Integer userId,
                                  Integer companyId,
                                  UserTransaction transaction) throws ValidationException {

        if (importRecord != null) {

            DataRow data = composeDataRowFromImportRecord(importRecord.getImportRecordId(), importRecord.getRecordIndex());
            if (null == data) {
                return;
            }

            if (configuration.isSkipHeader() && 0 == data.getRowNumber()) {
                return;
            }

            log.debug("-> Validating #ROW=" + data.getRowNumber() + " Data " + data.getColumns());
            for (Column column : selectedColumns) {
                try {
                    column.validate(configuration, data);
                } catch (ValidationException validationException) {
                    //organizationCache.clear();
                    //personCache.clear();
                    throw validationException;
                }
            }

            try {

                if (ContactConstants.ImportProfileType.ORGANIZATION.equal(configuration.getType())) {
                    List<Column> organizationColumns = getOrganizationColumns(selectedColumns);
                    List<StaticColumn> addressColumns = getAddressStaticColumns(organizationColumns);
                    List<DinamicColumn> addressCategoryColumns = getAddresCategoryColumns(organizationColumns);
                    List<DinamicColumn> addressTelecomColumns = getAddressTelecomColumns(organizationColumns);
                    List<StaticColumn> customerColumns = getCustomerStaticColumns(organizationColumns);
                    List<DinamicColumn> customerCategoryColumns = getCustomerCategoryColumns(selectedColumns);

                    createOrganization(addressColumns,
                            addressCategoryColumns,
                            addressTelecomColumns,
                            customerColumns,
                            customerCategoryColumns,
                            data,
                            userId,
                            companyId,
                            configuration,
                            transaction);
                }

                if (ContactConstants.ImportProfileType.PERSON.equal(configuration.getType())) {
                    List<Column> contactColumns = getContactColumns(selectedColumns);
                    List<StaticColumn> addressColumns = getAddressStaticColumns(contactColumns);
                    List<DinamicColumn> addressCategoryColumns = getAddresCategoryColumns(contactColumns);
                    List<DinamicColumn> addressTelecomColumns = getAddressTelecomColumns(contactColumns);
                    List<StaticColumn> customerColumns = getCustomerStaticColumns(contactColumns);
                    List<DinamicColumn> customerCategoryColumns = getCustomerCategoryColumns(selectedColumns);

                    createContact(addressColumns,
                            addressCategoryColumns,
                            addressTelecomColumns,
                            customerColumns,
                            customerCategoryColumns,
                            data,
                            userId,
                            companyId,
                            configuration,
                            transaction);
                }

                if (ContactConstants.ImportProfileType.ORGANIZATION_AND_CONTACT_PERSON.equal(configuration.getType())) {

                    if (isContactPersonRecord(importRecord.getRecordType())) {

                        Integer organizationId = getParentRecordOrganizationId(importRecord);
                        saveImportRecordContactPerson(importRecord, organizationId, selectedColumns, configuration, userId, companyId, transaction);

                    } else {
                        //is organization and create
                        List<Column> organizationColumns = getOrganizationColumns(selectedColumns);
                        List<StaticColumn> addressColumns = getAddressStaticColumns(organizationColumns);
                        List<DinamicColumn> addressCategoryColumns = getAddresCategoryColumns(organizationColumns);
                        List<DinamicColumn> addressTelecomColumns = getAddressTelecomColumns(organizationColumns);
                        List<StaticColumn> customerColumns = getCustomerStaticColumns(organizationColumns);
                        List<DinamicColumn> customerCategoryColumns = getCustomerCategoryColumns(selectedColumns);

                        Integer organizationId = createOrganization(addressColumns,
                                addressCategoryColumns,
                                addressTelecomColumns,
                                customerColumns,
                                customerCategoryColumns,
                                data,
                                userId,
                                companyId,
                                configuration,
                                transaction);

                        defineImportRecordOrganizationId(importRecord, organizationId);
                        saveOrganizationImportRecordContactPerson(importRecord.getImportRecordId(), organizationId, selectedColumns, configuration, userId, companyId, transaction);
                    }
                }

            } catch (Exception e) {
                log.error("Unexpected error when create import record Data " + data.getColumns(), e);
                throw new RuntimeException(e);
            }
        }
    }

    private void defineImportRecordOrganizationId(ImportRecord importRecord, Integer organizationId) {
        importRecord.setOrganizationId(organizationId);
    }

    private Integer getParentRecordOrganizationId(ImportRecord contactPersonRecord) {
        ImportRecord parentRecord = findImportRecord(contactPersonRecord.getParentRecordId());
        if (parentRecord != null) {
            return parentRecord.getOrganizationId();
        }
        return null;
    }

    private void saveOrganizationImportRecordContactPerson(Integer parentRecordId,
                                               Integer organizationId,
                                               List<Column> selectedColumns,
                                               DataImportConfiguration configuration,
                                               Integer userId,
                                               Integer companyId,
                                               UserTransaction transaction) throws ValidationException {

        ImportRecordHome importRecordHome = (ImportRecordHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTRECORD);
        Collection childImportRecords = null;
        try {
            childImportRecords = importRecordHome.findByParentImportRecordIdWithoutDuplicates(parentRecordId, companyId);
        } catch (FinderException e) {
            childImportRecords = new ArrayList();
        }

        for (Iterator iterator = childImportRecords.iterator(); iterator.hasNext();) {
            ImportRecord childImportRecord = (ImportRecord) iterator.next();
            saveImportRecordContactPerson(childImportRecord, organizationId, selectedColumns, configuration, userId, companyId, transaction);
        }
    }

    private void saveImportRecordContactPerson(ImportRecord contactPersonRecord,
                                               Integer organizationId,
                                               List<Column> selectedColumns,
                                               DataImportConfiguration configuration,
                                               Integer userId,
                                               Integer companyId,
                                               UserTransaction transaction) throws ValidationException {

        List<Column> contactPersonColumns = getContactPersonColumns(selectedColumns);
        List<StaticColumn> contactPersonStaticColumns = getContactPersonStaticColumns(contactPersonColumns);
        List<DinamicColumn> contactPersonCategoriesColumns = getContactPersonCategoryColumns(contactPersonColumns);
        List<DinamicColumn> contactPersonTelecomColumns = getContactPersonTelecomColumns(contactPersonColumns);

        DataRow data = composeDataRowFromImportRecord(contactPersonRecord.getImportRecordId(), contactPersonRecord.getRecordIndex());
        if (data != null) {
            log.debug("-> Validating contact person #ROW=" + data.getRowNumber() + " Data " + data.getColumns());
            for (Column column : selectedColumns) {
                column.validate(configuration, data);
            }

            createContactPerson(contactPersonStaticColumns,
                    contactPersonCategoriesColumns,
                    contactPersonTelecomColumns,
                    data,
                    organizationId,
                    userId,
                    companyId,
                    configuration,
                    transaction);
        }
    }

    public boolean mergeImportRecord(ImportMergeWrapper importMergeWrapper) {
        boolean success = true;
        Integer addressId = importMergeWrapper.getAddressId();

        UserTransaction transaction = ctx.getUserTransaction();

        Address address = findAddress(addressId);
        if (address != null) {
            try {
                transaction.begin();

                mergeImportRecordInAddress(address, importMergeWrapper);
                createMergeOrganizationContactPersons(address.getAddressId(), importMergeWrapper, transaction);
                removeMainImportRecord(importMergeWrapper.getImportRecordId());

                transaction.commit();
                transaction.setTransactionTimeout(0);
            } catch (Exception e) {
                log.error("Fail import Merge.. ", e);
                success = false;
                try {
                    transaction.rollback();
                } catch (SystemException e1) {
                    log.debug("System error in rollback..", e1);
                    throw new RuntimeException(e1);
                }
            }
        }
        return success;
    }

    public void notImportDuplicateProcess(Integer importRecordId) {
        ImportRecordHome importRecordHome = (ImportRecordHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTRECORD);
        ImportRecord importRecord = findImportRecord(importRecordId);

        if (importRecord != null) {

            Integer parentRecordId = null;
            if (isContactPersonRecord(importRecord.getRecordType())) {
                parentRecordId = importRecord.getParentRecordId();
            }

            UserTransaction transaction = ctx.getUserTransaction();
            try {
                transaction.begin();
                //first delete childs
                try {
                    Collection childImportRecords = importRecordHome.findByParentImportRecordId(importRecord.getImportRecordId(), importRecord.getCompanyId());
                    for (Iterator iterator = childImportRecords.iterator(); iterator.hasNext();) {
                        ImportRecord childImportRecord = (ImportRecord) iterator.next();
                        childImportRecord.remove();
                    }
                } catch (FinderException ignore) {
                }

                importRecord.remove();
                if (parentRecordId != null) {
                    removeMainImportRecord(parentRecordId);
                }

                transaction.commit();
            } catch (Exception e) {
                log.error("Fail not import process.. ", e);
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    log.debug("System error in not import rollback..", e1);
                }
            }
        }
    }

    public boolean importNevertheless(Integer importRecordId, Integer userId, DataImportConfiguration configuration) throws ValidationException {
        boolean success = false;
        ImportRecord importRecord = findImportRecord(importRecordId);
        if (importRecord != null) {
            DeduplicationAddressService service = getDeduplicationAddressService();
            List<Column> profileColumns = service.getImportProfileColumns(importRecord.getProfileId());
            profileColumns = fixColumnPositionIndexMinus(profileColumns); //this to read by index of an data list

            if (!profileColumns.isEmpty()) {
                UserTransaction transaction = ctx.getUserTransaction();

                try {
                    transaction.begin();
                    try {
                        saveImportRecord(importRecord, profileColumns, configuration, userId, importRecord.getCompanyId(), transaction);
                        removeMainImportRecord(importRecord.getImportRecordId());

                    } catch (ValidationException validationException) {
                        transaction.rollback();
                        transaction.setTransactionTimeout(0);
                        organizationCache.clear();
                        personCache.clear();
                        throw validationException;
                    } catch (Exception e) {
                        transaction.rollback();
                        log.error("Unexpected error happen when create Data.. ", e);
                        throw new RuntimeException(e);
                    }

                    transaction.commit();
                    log.debug("-> All Data saved OK...");
                    organizationCache.clear();
                    personCache.clear();
                    transaction.setTransactionTimeout(0);

                    success = true;
                } catch (ValidationException e) {
                    throw e;
                } catch (Exception e) {
                    log.error("Unexpected error happen ", e);
                    throw new RuntimeException(e);
                }
            }
        }
        return success;
    }

    private void mergeImportRecordInAddress(Address address, ImportMergeWrapper importMergeWrapper) {
        Integer companyId = address.getCompanyId();

        String name1 = getAddressStaticColumnValue("name1", importMergeWrapper);
        String name2 = getAddressStaticColumnValue("name2", importMergeWrapper);
        String name3 = getAddressStaticColumnValue("name3", importMergeWrapper);
        String country = getAddressStaticColumnValue("countryId", importMergeWrapper);
        String city = getAddressStaticColumnValue("cityId", importMergeWrapper);
        String zip = getAddressStaticColumnValue("cityZip", importMergeWrapper);
        String street = getAddressStaticColumnValue("street", importMergeWrapper);
        String houseNumber = getAddressStaticColumnValue("houseNumber", importMergeWrapper);
        String keywords = getAddressStaticColumnValue("keywords", importMergeWrapper);
        String searchName = getAddressStaticColumnValue("searchName", importMergeWrapper);
        String language = getAddressStaticColumnValue("languageId", importMergeWrapper);

        String title = getAddressStaticColumnValue("titleId", importMergeWrapper);
        String salutation = getAddressStaticColumnValue("salutationId", importMergeWrapper);
        String education = getAddressStaticColumnValue("education", importMergeWrapper);
        //List<String> userGroupList = getMultipleStaticColumnValue("userGroupId", data, addressColumns);

        String number = getCustomerStaticColumnValue("number", importMergeWrapper);
        String source = getCustomerStaticColumnValue("sourceId", importMergeWrapper);
        String branch = getCustomerStaticColumnValue("branchId", importMergeWrapper);
        String customerType = getCustomerStaticColumnValue("customerTypeId", importMergeWrapper);

        if (name1 != null) {
            address.setName1(name1);
        }
        if (name2 != null) {
            address.setName2(name2);
        }
        if (name3 != null) {
            address.setName3(name3);
        }

        Integer countryId = null;
        if (country != null) {
            countryId = getCountryId(country, companyId);
            if (countryId != null) {
                address.setCountryId(countryId);
                address.setCityId(null);
            }
        } else {
            countryId = address.getCountryId();
        }

        if (countryId != null && city != null) {
            if (zip == null && address.getCityEntity() != null) {
                zip = address.getCityEntity().getCityZip();
            }

            Integer cityId = getCity(countryId, city, zip, companyId);
            if (cityId != null) {
                address.setCityId(cityId);
            }
        }

        if (street != null) {
            address.setStreet(street);
        }
        if (houseNumber != null) {
            address.setHouseNumber(houseNumber);
        }
        if (title != null) {
            Integer titleId = getTitleId(title, companyId);
            address.setTitleId(titleId);
        }
        if (salutation != null) {
            Integer salutationId = getSaludationId(salutation, companyId);
            address.setSalutationId(salutationId);
        }
        if (education != null) {
            address.setEducation(education);
        }
        if (keywords != null) {
            address.setKeywords(keywords);
        }
        if (searchName != null) {
            address.setSearchName(searchName);
        }
        if (language != null) {
            Integer languageId = getLanguageId(language, companyId);
            if (languageId != null) {
                address.setLanguageId(languageId);
            }
        }

        //customer
        Customer customer = findCustomer(address.getAddressId());
        if (customer == null && existMergeCustomerColumns(importMergeWrapper)) {
            customer = createCustomer(address.getAddressId(), address.getCompanyId(), number);
        }

        if (customer != null) {
            if (number != null) {
                customer.setNumber(number);
            }
            if (source != null) {
                Integer sourceId = getAddressSource(source, companyId);
                customer.setSourceId(sourceId);
            }
            if (branch != null) {

                Integer branchId = getBranch(branch, companyId);
                customer.setBranchId(branchId);
            }
            if (customerType != null) {
                Integer customerTypeId = getCustomerType(customerType, companyId);
                customer.setCustomerTypeId(customerTypeId);
            }
        }

        //merge categories
        mergeAddressCategoriesColumns(address.getAddressId(), importMergeWrapper);

        //merge telecoms
        mergeAddressTelecomColumns(address.getAddressId(), importMergeWrapper);
    }

    private void mergeAddressCategoriesColumns(Integer addressId, ImportMergeWrapper importMergeWrapper) {
        Integer importRecordId = importMergeWrapper.getImportRecordId();
        ImportRecord importRecord = findImportRecord(importRecordId);

        if (importRecord != null) {
            DataImportConfiguration configuration = importMergeWrapper.getConfiguration();

            List<Column> dinamicColumnList = filterDinamicColumnsToMerge(importMergeWrapper);
            dinamicColumnList = fixColumnPositionIndexMinus(dinamicColumnList);

            List<DinamicColumn> addressCategoryColumns = getAddresCategoryColumns(dinamicColumnList);
            List<DinamicColumn> customerCategoryColumns = getCustomerCategoryColumns(dinamicColumnList);

            DataRow data = composeDataRowFromImportRecord(importRecord.getImportRecordId(), importRecord.getRecordIndex());

            if (data != null) {
                Map addressCategoryMap = buildCategoryValuesMap(addressCategoryColumns, data, configuration);
                if (!addressCategoryMap.isEmpty()) {
                    updateAddressCategories(addressCategoryMap, addressId, importRecord.getCompanyId());
                }

                Map customerCategoryMap = buildCategoryValuesMap(customerCategoryColumns, data, configuration);
                if (!customerCategoryMap.isEmpty()) {
                    updateCustomerCategories(customerCategoryMap, addressId, importRecord.getCompanyId());
                }
            }
        }
    }

    private void mergeContactPersonCategoriesColumns(ContactPerson contactPerson, ImportMergeWrapper importMergeWrapper) {
        Integer importRecordId = importMergeWrapper.getImportRecordId();
        ImportRecord importRecord = findImportRecord(importRecordId);

        if (importRecord != null) {
            DataImportConfiguration configuration = importMergeWrapper.getConfiguration();

            List<Column> dinamicColumnList = filterDinamicColumnsToMerge(importMergeWrapper);
            dinamicColumnList = fixColumnPositionIndexMinus(dinamicColumnList);

            List<DinamicColumn> contactPersonCategoryColumns = getContactPersonCategoryColumns(dinamicColumnList);

            DataRow data = composeDataRowFromImportRecord(importRecord.getImportRecordId(), importRecord.getRecordIndex());

            if (data != null) {
                Map contactPersonCategoryMap = buildCategoryValuesMap(contactPersonCategoryColumns, data, configuration);
                if (!contactPersonCategoryMap.isEmpty()) {
                    updateContactPersonCategories(contactPersonCategoryMap, contactPerson.getAddressId(), contactPerson.getContactPersonId(), contactPerson.getCompanyId());
                }
            }
        }
    }

    private void mergeAddressTelecomColumns(Integer addressId, ImportMergeWrapper importMergeWrapper) {
        Integer importRecordId = importMergeWrapper.getImportRecordId();
        ImportRecord importRecord = findImportRecord(importRecordId);

        if (importRecord != null && addressId != null) {

            List<Column> dinamicColumnList = filterDinamicColumnsToMerge(importMergeWrapper);
            dinamicColumnList = fixColumnPositionIndexMinus(dinamicColumnList);

            List<DinamicColumn> addressTelecomColumns = getAddressTelecomColumns(dinamicColumnList);

            DataRow data = composeDataRowFromImportRecord(importRecord.getImportRecordId(), importRecord.getRecordIndex());

            if (data != null) {

                Map addressTelecomMap = buildTelecomMap(addressTelecomColumns, data);
                if (!addressTelecomMap.isEmpty()) {
                    mergeTelecomValues(addressTelecomMap, addressId, null, importRecord.getCompanyId());
                }
            }
        }
    }

    private void mergeContactPersonTelecomColumns(ContactPerson contactPerson, ImportMergeWrapper importMergeWrapper) {
        Integer importRecordId = importMergeWrapper.getImportRecordId();
        ImportRecord importRecord = findImportRecord(importRecordId);

        if (importRecord != null && contactPerson != null) {

            List<Column> dinamicColumnList = filterDinamicColumnsToMerge(importMergeWrapper);
            dinamicColumnList = fixColumnPositionIndexMinus(dinamicColumnList);

            List<DinamicColumn> contactPersonTelecomColumns = getContactPersonTelecomColumns(dinamicColumnList);

            DataRow data = composeDataRowFromImportRecord(importRecord.getImportRecordId(), importRecord.getRecordIndex());

            if (data != null) {

                Map contactPersonTelecomMap = buildTelecomMap(contactPersonTelecomColumns, data);
                if (!contactPersonTelecomMap.isEmpty()) {
                    mergeTelecomValues(contactPersonTelecomMap, contactPerson.getAddressId(), contactPerson.getContactPersonId(), importRecord.getCompanyId());
                }
            }
        }
    }

    private void createMergeOrganizationContactPersons(Integer addressId, ImportMergeWrapper importMergeWrapper, UserTransaction transaction) throws ValidationException, CreateException {
        ImportProfile importProfile = findImportProfile(importMergeWrapper.getProfileId());

        if (importProfile != null && ContactConstants.ImportProfileType.ORGANIZATION_AND_CONTACT_PERSON.equal(importProfile.getProfileType())) {

            ImportRecord importRecord = findImportRecord(importMergeWrapper.getImportRecordId());
            if (importRecord != null) {

                if (isContactPersonRecord(importRecord.getRecordType())) {
                    mergeImportRecordInContactPerson(importRecord, addressId, importMergeWrapper);

                } else {
                    //is organization record
                    defineImportRecordOrganizationId(importRecord, addressId);

                    DeduplicationAddressService service = getDeduplicationAddressService();
                    List<Column> profileColumns = service.getImportProfileColumns(importRecord.getProfileId());
                    profileColumns = fixColumnPositionIndexMinus(profileColumns); //this to read by index of an data list

                    saveOrganizationImportRecordContactPerson(importRecord.getImportRecordId(), addressId, profileColumns, importMergeWrapper.getConfiguration(), importMergeWrapper.getUserId(), importRecord.getCompanyId(), transaction);
                }
            }
        }
    }

    private boolean isContactPersonRecord(Integer recordType) {
        return ContactConstants.ImportRecordType.CONTACTPERSONRECORD.equal(recordType);
    }

    private void mergeImportRecordInContactPerson(ImportRecord importRecord, Integer contactPersonId, ImportMergeWrapper importMergeWrapper) throws CreateException {
        Integer organizationId = getParentRecordOrganizationId(importRecord);
        ContactPerson contactPerson = findContactPerson(organizationId, contactPersonId);

        if (contactPerson == null) {
            ContactPersonHome contactPersonHome = (ContactPersonHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTPERSON);

            ContactPersonDTO contactPersonDTO = new ContactPersonDTO();
            contactPersonDTO.put("addressId", organizationId);
            contactPersonDTO.put("contactPersonId", contactPersonId);
            contactPersonDTO.put("companyId", importRecord.getCompanyId());
            contactPersonDTO.put("active", Boolean.TRUE);

            contactPerson = contactPersonHome.create(contactPersonDTO);
        }

        String function = getContactPersonStaticColumnValue("function", importMergeWrapper);

        if (function != null) {
            contactPerson.setFunction(function);
        }

        //merge categories
        mergeContactPersonCategoriesColumns(contactPerson, importMergeWrapper);

        //merge telecoms
        mergeContactPersonTelecomColumns(contactPerson, importMergeWrapper);
    }

    private List<Column> filterDinamicColumnsToMerge(ImportMergeWrapper importMergeWrapper) {
        List<Column> dinamicColumnList = new ArrayList<Column>();

        List<ImportMergeField> importMergeFieldList = importMergeWrapper.getImportMergeFieldList();
        for (ImportMergeField importMergeField : importMergeFieldList) {

            if (importMergeField.isKeepImportField()) {
                Column column = importMergeField.getColumn();
                if (column instanceof DinamicColumn ) {
                    Column newColumn = column.getCopy();
                    dinamicColumnList.add(newColumn);
                }
            }
        }
        return dinamicColumnList;
    }

    private List<Column> fixColumnPositionIndexMinus(List<Column> columnList) {
        for (Column column : columnList) {
            column.setPosition(column.getPosition() - 1); //to read value from index
        }
        return columnList;
    }

    private String getAddressStaticColumnValue(String ejbFieldName, ImportMergeWrapper importMergeWrapper) {
        if (importMergeWrapper.isContactPerson()) {
            return getStaticColumnValue(ejbFieldName, importMergeWrapper, Column.ColumnType.CONTACT_PERSON);
        }
        return getStaticColumnValue(ejbFieldName, importMergeWrapper, Column.ColumnType.ADDRESS);
    }

    private String getCustomerStaticColumnValue(String ejbFieldName, ImportMergeWrapper importMergeWrapper) {
        return getStaticColumnValue(ejbFieldName, importMergeWrapper, Column.ColumnType.CUSTOMER);
    }

    private String getContactPersonStaticColumnValue(String ejbFieldName, ImportMergeWrapper importMergeWrapper) {
        return getStaticColumnValue(ejbFieldName, importMergeWrapper, Column.ColumnType.CONTACT_PERSON);
    }

    private String getStaticColumnValue(String ejbFieldName, ImportMergeWrapper importMergeWrapper, Column.ColumnType columnType) {
        String value = null;
        Integer importRecordId = importMergeWrapper.getImportRecordId();
        List<ImportMergeField> importMergeFieldList = importMergeWrapper.getImportMergeFieldList();

        for (ImportMergeField importMergeField : importMergeFieldList) {

            if (importMergeField.isKeepImportField()) {
                Column column = importMergeField.getColumn();
                if (column instanceof StaticColumn && columnType.equals(column.getType())) {
                    StaticColumn staticColumn = (StaticColumn) column;
                    if (ejbFieldName.equals(staticColumn.getEjbFieldName())) {
                        RecordColumn recordColumn = findRecordColumn(staticColumn.getImportColumnId(), importRecordId);
                        if (recordColumn != null) {
                            value = recordColumn.getColumnValue();
                        }
                    }
                }
            }
        }

        return (value != null && !"".equals(value.trim())) ? value.trim() : null;
    }

    private boolean existMergeCustomerColumns(ImportMergeWrapper importMergeWrapper) {
        List<ImportMergeField> importMergeFieldList = importMergeWrapper.getImportMergeFieldList();

        for (ImportMergeField importMergeField : importMergeFieldList) {
            if (importMergeField.isKeepImportField()) {
                Column column = importMergeField.getColumn();
                if (Column.ColumnType.CUSTOMER.equals(column.getType())) {
                    return true;
                }
            }
        }
        return false;
    }

    private Integer createOrganizationImportRecord(DataRow data, List<Column> selectedColumns, Integer companyId, Integer profileId) {

        List<Column> organizationColumns = getOrganizationColumns(selectedColumns);
        List<StaticColumn> addressColumns = getAddressStaticColumns(organizationColumns);

        //read data by columns
        StaticColumn name1Column = getStaticColumn("name1", addressColumns);
        String name1 = data.getValue(name1Column.getPosition());

        String name2 = getStaticColumnValue("name2", data, addressColumns);
        String name3 = getStaticColumnValue("name3", data, addressColumns);
        String country = getStaticColumnValue("countryId", data, addressColumns);
        String city = getStaticColumnValue("cityId", data, addressColumns);
        String zip = getStaticColumnValue("cityZip", data, addressColumns);
        String street = getStaticColumnValue("street", data, addressColumns);
        String houseNumber = getStaticColumnValue("houseNumber", data, addressColumns);


        String organizationKey = companyId + "_" +
                name1.toLowerCase() + "_" +
                (null != name2 ? name2.toLowerCase() : null) + "_" +
                (null != name3 ? name3.toLowerCase() : null) + "_" +
                (null != city ? city.toLowerCase() : null) + "_" +
                (null != houseNumber ? houseNumber.toLowerCase() : null) + "_" +
                (null != street ? street.toLowerCase() : null) + "_" +
                (null != zip ? zip.toLowerCase() : null);

        ImportRecord importRecord = findImportRecordByIdentityKey(organizationKey, profileId, companyId);
        if (importRecord != null) {
            //already exist
            return importRecord.getImportRecordId();
        }

        ImportRecordCmd importRecordCmd = new ImportRecordCmd();
        importRecordCmd.putParam("op", "create");
        importRecordCmd.putParam("name1", name1);
        importRecordCmd.putParam("name2", name2);
        importRecordCmd.putParam("name3", name3);
        importRecordCmd.putParam("countryName", country);
        importRecordCmd.putParam("cityName", city);
        importRecordCmd.putParam("companyId", companyId);
        importRecordCmd.putParam("zip", zip);
        importRecordCmd.putParam("street", street);
        importRecordCmd.putParam("houseNumber", houseNumber);
        importRecordCmd.putParam("recordIndex", data.getRowNumber());
        importRecordCmd.putParam("identityKey", organizationKey);
        importRecordCmd.putParam("recordType", ContactConstants.ImportRecordType.MAINRECORD.getConstant());
        importRecordCmd.putParam("isDuplicate", Boolean.FALSE);
        importRecordCmd.putParam("profileId", profileId);

        importRecordCmd.putParam("recordColumnMapList", composeRecordColumnMapList(data, selectedColumns));
        importRecordCmd.executeInStateless(ctx);

        ResultDTO resultDTO = importRecordCmd.getResultDTO();
        return new Integer(resultDTO.get("importRecordId").toString());
    }

    private void createPersonImportRecord(DataRow data, List<Column> selectedColumns, Integer companyId, Integer profileId) {
        List<Column> contactColumns = getContactColumns(selectedColumns);
        List<StaticColumn> addressColumns = getAddressStaticColumns(contactColumns);

        //read data by column
        String lastName = getStaticColumnValue("name1", data, addressColumns);
        String firstName = getStaticColumnValue("name2", data, addressColumns);
        String country = getStaticColumnValue("countryId", data, addressColumns);
        String city = getStaticColumnValue("cityId", data, addressColumns);
        String zip = getStaticColumnValue("cityZip", data, addressColumns);
        String street = getStaticColumnValue("street", data, addressColumns);
        String houseNumber = getStaticColumnValue("houseNumber", data, addressColumns);

        String title = getStaticColumnValue("titleId", data, addressColumns);
        String salutation = getStaticColumnValue("salutationId", data, addressColumns);

        String personKey = companyId + "_"
                + lastName.toLowerCase() + "_"
                + (null != firstName ? firstName.toLowerCase() : null) + "_"
                + (null != salutation ? salutation.toLowerCase() : null) + "_"
                + (null != title ? title.toLowerCase() : null);

        ImportRecordCmd importRecordCmd = new ImportRecordCmd();
        importRecordCmd.putParam("op", "create");
        importRecordCmd.putParam("name1", lastName);
        importRecordCmd.putParam("name2", firstName);
        importRecordCmd.putParam("countryName", country);
        importRecordCmd.putParam("cityName", city);
        importRecordCmd.putParam("companyId", companyId);
        importRecordCmd.putParam("zip", zip);
        importRecordCmd.putParam("street", street);
        importRecordCmd.putParam("houseNumber", houseNumber);
        importRecordCmd.putParam("recordIndex", data.getRowNumber());
        importRecordCmd.putParam("identityKey", personKey);
        importRecordCmd.putParam("recordType", ContactConstants.ImportRecordType.MAINRECORD.getConstant());
        importRecordCmd.putParam("isDuplicate", Boolean.FALSE);
        importRecordCmd.putParam("profileId", profileId);

        importRecordCmd.putParam("recordColumnMapList", composeRecordColumnMapList(data, selectedColumns));
        importRecordCmd.executeInStateless(ctx);
    }

    private void createContactPersonImportRecord(Integer importRecordId, DataRow data, List<Column> selectedColumns, Integer companyId, Integer profileId) {
        List<Column> contactPersonColumns = getContactPersonColumns(selectedColumns);
        List<StaticColumn> contactPersonStaticColumns = getContactPersonStaticColumns(contactPersonColumns);

        String lastName = getStaticColumnValue("name1", data, contactPersonStaticColumns);
        String firstName = getStaticColumnValue("name2", data, contactPersonStaticColumns);
        String title = getStaticColumnValue("titleId", data, contactPersonStaticColumns);
        String salutation = getStaticColumnValue("salutationId", data, contactPersonStaticColumns);
        String function = getStaticColumnValue("function", data, contactPersonStaticColumns);

        String contactPersonKey = companyId + "_"
                + lastName.toLowerCase() + "_"
                + (null != firstName ? firstName.toLowerCase() : null) + "_"
                + (null != salutation ? salutation.toLowerCase() : null) + "_"
                + (null != title ? title.toLowerCase() : null);

        ImportRecordCmd importRecordCmd = new ImportRecordCmd();
        importRecordCmd.putParam("op", "create");
        importRecordCmd.putParam("parentRecordId", importRecordId);
        importRecordCmd.putParam("name1", lastName);
        importRecordCmd.putParam("name2", firstName);
        importRecordCmd.putParam("function", function);
        importRecordCmd.putParam("companyId", companyId);
        importRecordCmd.putParam("recordIndex", data.getRowNumber());
        importRecordCmd.putParam("identityKey", contactPersonKey);
        importRecordCmd.putParam("recordType", ContactConstants.ImportRecordType.CONTACTPERSONRECORD.getConstant());
        importRecordCmd.putParam("isDuplicate", Boolean.FALSE);
        importRecordCmd.putParam("profileId", profileId);

        importRecordCmd.putParam("recordColumnMapList", composeRecordColumnMapList(data, selectedColumns));
        importRecordCmd.executeInStateless(ctx);
    }

    private List<Map> composeRecordColumnMapList(DataRow data, List<Column> selectedColumns) {
        List<Map> resultList = new ArrayList<Map>();

        if (data != null) {
            for (int i = 0; i < data.getColumns().size(); i++) {
                String value = data.getColumns().get(i);
                Column column = findColumnByPosition(selectedColumns, i);

                if (column != null) {
                    Map map = new HashMap();
                    map.put("columnValue", value);
                    map.put("columnIndex", i);
                    map.put("importColumnId", column.getImportColumnId());

                    resultList.add(map);
                }
            }
        }

        return resultList;
    }

    private Column findColumnByPosition(List<Column> selectedColumns, int position) {
        for (Column column : selectedColumns) {
            if (position == column.getPosition()) {
                return column;
            }
        }
        return null;
    }

    private List<ImportRecord> getImportRecordsWithoutDuplicate(Integer profileId, Integer companyId) {
        List<ImportRecord> importRecordList = new ArrayList<ImportRecord>();

        ImportRecordHome importRecordHome = (ImportRecordHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTRECORD);

        try {
            Collection importRecords = importRecordHome.findByImportProfileRecordTypeWithoutDuplicates(profileId, companyId, ContactConstants.ImportRecordType.MAINRECORD.getConstant());
            if (importRecords != null) {
                importRecordList.addAll(importRecords);
            }
        } catch (FinderException ignore) {
        }

        return importRecordList;
    }

    private void removeNotDuplicateImportRecords(Integer profileId) throws NamingException, SQLException {
        InitialContext initialContext = new InitialContext();
        DataSource dataSource = (DataSource) initialContext.lookup(Constants.JNDI_ELWISDS);
        Connection connection = dataSource.getConnection();

        ArrayList<String> sqlList = DeduplicationNativeSqlFactory.i.deleteImportRecordWithoutDuplicateSqlList(profileId);

        for (String sql : sqlList) {
            log.debug("SQL to execute: " + sql);
            connection.prepareStatement(sql).execute();
        }
        connection.close();
    }

/*
    private void removeNotDuplicateImportRecords(Integer profileId, Integer companyId) throws RemoveException {
        ImportRecordHome importRecordHome = (ImportRecordHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTRECORD);

        try {
            Collection importRecords = importRecordHome.findByImportProfileRecordTypeWithoutDuplicates(profileId, companyId, ContactConstants.ImportRecordType.MAINRECORD.getConstant());
            for (Iterator iterator = importRecords.iterator(); iterator.hasNext();) {
                ImportRecord importRecord = (ImportRecord) iterator.next();

                removeImportRecordRecordDuplicates(importRecord.getImportRecordId());
                if (removeChildRecords(importRecord.getImportRecordId(), companyId)) {
                    importRecord.remove();
                }
            }
        } catch (FinderException e) {
            log.debug("Not found import records to remove..");
        }
    }
*/


    private void removeMainImportRecord(Integer importRecordId) throws RemoveException {
        ImportRecord importRecord = findImportRecord(importRecordId);
        ImportRecord parentRecord = null;

        if (importRecord != null) {
            if (isContactPersonRecord(importRecord.getRecordType())) {
                parentRecord = findImportRecord(importRecord.getParentRecordId());
            }

            removeImportRecordRecordDuplicates(importRecord.getImportRecordId());
            if (removeChildRecords(importRecord.getImportRecordId(), importRecord.getCompanyId())) {
                importRecord.remove();
            }
        }

        if (parentRecord != null) {
            if (removeChildRecords(parentRecord.getImportRecordId(), parentRecord.getCompanyId())) {
                parentRecord.remove();
            }
        }
    }

    private boolean removeChildRecords(Integer parentImportRecordId, Integer companyId) throws RemoveException {
        boolean success = true;
        ImportRecordHome importRecordHome = (ImportRecordHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTRECORD);
        try {
            Collection childImportRecords = importRecordHome.findByParentImportRecordId(parentImportRecordId, companyId);
            for (Iterator iterator = childImportRecords.iterator(); iterator.hasNext();) {
                ImportRecord childImportRecord = (ImportRecord) iterator.next();
                if (childImportRecord.getIsDuplicate()) {
                    success = false;
                } else {
                    childImportRecord.remove();
                }
            }
        } catch (FinderException ignore) {
        }
        return success;
    }

    private void removeImportRecordRecordDuplicates(Integer importRecordId) throws RemoveException {
        RecordDuplicateHome recordDuplicateHome = (RecordDuplicateHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_RECORDDUPLICATE);
        try {
            Collection collection = recordDuplicateHome.findByImportRecordId(importRecordId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                RecordDuplicate recordDuplicate = (RecordDuplicate) iterator.next();
                recordDuplicate.remove();
            }
        } catch (FinderException ignore) {
        }
    }

    private boolean hasChildRecords(Integer parentImportRecordId, Integer companyId) {
        ImportRecordHome importRecordHome = (ImportRecordHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTRECORD);
        try {
            Collection childImportRecords = importRecordHome.findByParentImportRecordId(parentImportRecordId, companyId);
            if (!childImportRecords.isEmpty()) {
                return true;
            }
        } catch (FinderException ignore) {
        }
        return false;
    }

    private DataRow composeDataRowFromImportRecord(Integer importRecordId, Integer rowIndex) {
        DataRow dataRow = null;

        RecordColumnHome recordColumnHome = (RecordColumnHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_RECORDCOLUMN);

        try {
            Collection recordColumns = recordColumnHome.findByImportRecord(importRecordId);
            if (recordColumns != null) {
                LinkedList<String> values = new LinkedList<String>();
                int index = 0;

                for (Iterator iterator = recordColumns.iterator(); iterator.hasNext();) {
                    RecordColumn recordColumn = (RecordColumn) iterator.next();

                    while (index < recordColumn.getColumnIndex()) {
                        values.add(index, null);
                        index++;
                    }

                    if (index == recordColumn.getColumnIndex()) {
                        values.add(recordColumn.getColumnIndex(), recordColumn.getColumnValue());
                    } else {
                        values.set(recordColumn.getColumnIndex(), recordColumn.getColumnValue());
                    }

                    index++;
                }

                dataRow = new DataRow(values);
                dataRow.setRowNumber(rowIndex);
            }
        } catch (FinderException ignore) {
        }

        return dataRow;
    }

    private void createContactPerson(List<StaticColumn> contactPersonColumns,
                                     List<DinamicColumn> contactPersonCategories,
                                     List<DinamicColumn> contactPersonTelecoms,
                                     DataRow data,
                                     Integer organizationId,
                                     Integer userId,
                                     Integer companyId,
                                     DataImportConfiguration configuration,
                                     UserTransaction transaction) {

        createContactPerson(contactPersonColumns,
                contactPersonCategories,
                contactPersonTelecoms,
                data,
                organizationId,
                userId,
                companyId,
                configuration);
    }

    private void createContactPerson(List<StaticColumn> contactPersonColumns,
                                     List<DinamicColumn> contactPersonCategories,
                                     List<DinamicColumn> contactPersonTelecoms,
                                     DataRow data,
                                     Integer organizationId,
                                     Integer userId,
                                     Integer companyId,
                                     DataImportConfiguration configuration) {
        String addressType = ContactConstants.ADDRESSTYPE_PERSON;

        String lastName = getStaticColumnValue("name1", data, contactPersonColumns);

        String firstName = getStaticColumnValue("name2", data, contactPersonColumns);
        String title = getStaticColumnValue("titleId", data, contactPersonColumns);
        String salutation = getStaticColumnValue("salutationId", data, contactPersonColumns);
        String education = getStaticColumnValue("education", data, contactPersonColumns);
        String keywords = getStaticColumnValue("keywords", data, contactPersonColumns);
        String searchName = getStaticColumnValue("searchName", data, contactPersonColumns);
        String function = getStaticColumnValue("function", data, contactPersonColumns);
        String language = getStaticColumnValue("languageId", data, contactPersonColumns);
        List<String> userGroupList = getMultipleStaticColumnValue("userGroupId", data, contactPersonColumns);


        String contactPersonKey = companyId + "_"
                + lastName.toLowerCase() + "_"
                + (null != firstName ? firstName.toLowerCase() : null) + "_"
                + (null != salutation ? salutation.toLowerCase() : null) + "_"
                + (null != title ? title.toLowerCase() : null);

        Integer personAddressId = personCache.get(contactPersonKey);

        Map contactPersonTelecomMap = buildTelecomMap(contactPersonTelecoms, data);

        Map contactPersonCategoryMap = buildCategoryValuesMap(contactPersonCategories, data, configuration);

        Integer salutationId = null;
        if (null != salutation && !"".equals(salutation.trim())) {
            salutationId = getSaludationId(salutation, companyId);
        }

        Integer titleId = null;
        if (null != title && !"".equals(title.trim())) {
            titleId = getTitleId(title, companyId);
        }

        Integer languageId = getLanguageId(language, companyId);
        String userGroupAccessIds = getUserGroupAddressAccessIds(userGroupList, companyId);

        if (null == personAddressId) {
            ContactPersonCreateCmd contactPersonCreateCmd = new ContactPersonCreateCmd();
            contactPersonCreateCmd.setOp("create");
            contactPersonCreateCmd.putParam("addressId", organizationId.toString());
            contactPersonCreateCmd.putParam("newAddress", "true");
            contactPersonCreateCmd.putParam("addressType", addressType);
            contactPersonCreateCmd.putParam("companyId", companyId.toString());
            contactPersonCreateCmd.putParam("recordUserId", userId.toString());
            contactPersonCreateCmd.putParam("salutationId", salutationId);
            contactPersonCreateCmd.putParam("titleId", titleId);
            contactPersonCreateCmd.putParam("name1", lastName);
            contactPersonCreateCmd.putParam("name2", firstName);
            contactPersonCreateCmd.putParam("function", function);
            contactPersonCreateCmd.putParam("education", education);
            contactPersonCreateCmd.putParam("keywords", keywords);
            contactPersonCreateCmd.putParam("searchName", searchName);
            contactPersonCreateCmd.putParam("contactPersonTelecomMap", contactPersonTelecomMap);
            contactPersonCreateCmd.putParam("active", true);
            contactPersonCreateCmd.putParam("languageId", languageId);
            contactPersonCreateCmd.putParam("accessUserGroupIds", userGroupAccessIds);

            contactPersonCreateCmd.executeInStateless(ctx);

            Integer contactPersonId = (Integer) contactPersonCreateCmd.getResultDTO().get("contactPersonIdToImport");
            Integer addressId = (Integer) contactPersonCreateCmd.getResultDTO().get("addresIdToImport");

            if (!contactPersonCategoryMap.isEmpty()) {
                createContactPersonCategories(contactPersonCategoryMap, addressId, contactPersonId, companyId);
            }

            personCache.put(contactPersonKey, contactPersonId);
        } else {
            ContactPersonCreateCmd contactPersonCreateCmd = new ContactPersonCreateCmd();
            contactPersonCreateCmd.setOp("create");
            contactPersonCreateCmd.putParam("addressId", organizationId.toString());
            contactPersonCreateCmd.putParam("importAddress", "true");
            contactPersonCreateCmd.putParam("addressIdToImport", personAddressId);
            contactPersonCreateCmd.putParam("addressType", addressType);
            contactPersonCreateCmd.putParam("companyId", companyId.toString());
            contactPersonCreateCmd.putParam("recordUserId", userId.toString());
            contactPersonCreateCmd.putParam("salutationId", salutationId);
            contactPersonCreateCmd.putParam("titleId", titleId);
            contactPersonCreateCmd.putParam("name1", lastName);
            contactPersonCreateCmd.putParam("name2", firstName);
            contactPersonCreateCmd.putParam("function", function);
            contactPersonCreateCmd.putParam("education", education);
            contactPersonCreateCmd.putParam("keywords", keywords);
            contactPersonCreateCmd.putParam("searchName", searchName);
            contactPersonCreateCmd.putParam("contactPersonTelecomMap", contactPersonTelecomMap);
            contactPersonCreateCmd.putParam("active", true);
            contactPersonCreateCmd.putParam("languageId", languageId);
            contactPersonCreateCmd.executeInStateless(ctx);

            Integer contactPersonId = (Integer) contactPersonCreateCmd.getResultDTO().get("contactPersonIdToImport");
            Integer addressId = (Integer) contactPersonCreateCmd.getResultDTO().get("addresIdToImport");

            if (!contactPersonCategoryMap.isEmpty()) {
                createContactPersonCategories(contactPersonCategoryMap, addressId, contactPersonId, companyId);
            }
        }

    }

    private Integer createOrganization(
            List<StaticColumn> addressColumns,
            List<DinamicColumn> addressCategories,
            List<DinamicColumn> addressTelecoms,
            List<StaticColumn> customerColumns,
            List<DinamicColumn> customerCategories,
            DataRow data,
            Integer userId,
            Integer companyId,
            DataImportConfiguration configuration,
            UserTransaction transaction) {

        return createOrganization(addressColumns,
                addressCategories,
                addressTelecoms, customerColumns,
                customerCategories,
                data,
                userId,
                companyId,
                configuration);
    }

    private Integer createOrganization(List<StaticColumn> addressColumns,
                                       List<DinamicColumn> addressCategories,
                                       List<DinamicColumn> addressTelecoms,
                                       List<StaticColumn> customerColumns,
                                       List<DinamicColumn> customerCategories,
                                       DataRow data,
                                       Integer userId,
                                       Integer companyId,
                                       DataImportConfiguration configuration) {
        String addressType = ContactConstants.ADDRESSTYPE_ORGANIZATION;

        byte code = CodeUtil.default_;
        if (!customerColumns.isEmpty() || !customerCategories.isEmpty()) {
            code = CodeUtil.customer;
        }

        StaticColumn name1Column = getStaticColumn("name1", addressColumns);
        String name1 = data.getValue(name1Column.getPosition());

        String name2 = getStaticColumnValue("name2", data, addressColumns);
        String name3 = getStaticColumnValue("name3", data, addressColumns);
        String country = getStaticColumnValue("countryId", data, addressColumns);
        String city = getStaticColumnValue("cityId", data, addressColumns);
        String zip = getStaticColumnValue("cityZip", data, addressColumns);
        String street = getStaticColumnValue("street", data, addressColumns);
        String houseNumber = getStaticColumnValue("houseNumber", data, addressColumns);
        String keywords = getStaticColumnValue("keywords", data, addressColumns);
        String searchName = getStaticColumnValue("searchName", data, addressColumns);
        String language = getStaticColumnValue("languageId", data, addressColumns);
        List<String> userGroupList = getMultipleStaticColumnValue("userGroupId", data, addressColumns);

        String number = getStaticColumnValue("number", data, customerColumns);
        String source = getStaticColumnValue("sourceId", data, customerColumns);
        String branch = getStaticColumnValue("branchId", data, customerColumns);
        String customerType = getStaticColumnValue("customerTypeId", data, customerColumns);

        String organizationKey = companyId + "_" +
                name1.toLowerCase() + "_" +
                (null != name2 ? name2.toLowerCase() : null) + "_" +
                (null != name3 ? name3.toLowerCase() : null) + "_" +
                (null != city ? city.toLowerCase() : null) + "_" +
                (null != houseNumber ? houseNumber.toLowerCase() : null) + "_" +
                (null != street ? street.toLowerCase() : null) + "_" +
                (null != zip ? zip.toLowerCase() : null);

        log.debug("-> Search in organization cache Key=" + organizationKey);
        Integer organizationId = organizationCache.get(organizationKey);
        if (null != organizationId) {
            return organizationId;
        }


        Map addressTelecomMap = buildTelecomMap(addressTelecoms, data);

        Map addressCategoryMap = buildCategoryValuesMap(addressCategories, data, configuration);

        Map customerCategoryMap = buildCategoryValuesMap(customerCategories, data, configuration);

        Integer countryId = null;
        Integer cityId = null;
        if (null != country && !"".equals(country.trim())) {
            countryId = getCountryId(country, companyId);
        }

        if (null != countryId) {
            if (null != city && !"".equals(city)) {
                cityId = getCity(countryId, city, zip, companyId);
            }
        }

        Integer sourceId = null;
        if (null != source && !"".equals(source)) {
            sourceId = getAddressSource(source, companyId);
        }

        Integer branchId = null;
        if (null != branch && !"".equals(branch)) {
            branchId = getBranch(branch, companyId);
        }

        Integer customerTypeId = null;
        if (null != customerType && !"".equals(customerType)) {
            customerTypeId = getCustomerType(customerType, companyId);
        }

        Integer languageId = getLanguageId(language, companyId);

        String userGroupAccessIds = getUserGroupAddressAccessIds(userGroupList, companyId);

        ImportAddressCmd addressCreateCmd = new ImportAddressCmd();
        addressCreateCmd.setOp("create");

        addressCreateCmd.putParam("number", number);
        addressCreateCmd.putParam("branchId", branchId);
        addressCreateCmd.putParam("sourceId", sourceId);
        addressCreateCmd.putParam("customerTypeId", customerTypeId);

        addressCreateCmd.putParam("companyId", companyId.toString());
        addressCreateCmd.putParam("code", code);
        addressCreateCmd.putParam("addressType", addressType);
        addressCreateCmd.putParam("userTypeValue", "0");
        addressCreateCmd.putParam("recordUserId", userId);
        addressCreateCmd.putParam("name1", name1);
        addressCreateCmd.putParam("name2", name2);
        addressCreateCmd.putParam("name3", name3);
        addressCreateCmd.putParam("countryId", countryId);
        addressCreateCmd.putParam("cityId", cityId);
        addressCreateCmd.putParam("houseNumber", houseNumber);
        addressCreateCmd.putParam("street", street);
        addressCreateCmd.putParam("telecomMap", addressTelecomMap);
        addressCreateCmd.putParam("searchName", searchName);
        addressCreateCmd.putParam("keywords", keywords);
        addressCreateCmd.putParam("languageId", languageId);
        addressCreateCmd.putParam("accessUserGroupIds", userGroupAccessIds);

        addressCreateCmd.executeInStateless(ctx);
        organizationId = (Integer) addressCreateCmd.getResultDTO().get("addressId");

        if (!addressCategoryMap.isEmpty()) {
            createAddressCategories(addressCategoryMap, organizationId, companyId);
        }

        if (!customerCategoryMap.isEmpty()) {
            createCustomerCategories(customerCategoryMap, organizationId, companyId);
        }

        organizationCache.put(organizationKey, organizationId);


        return organizationId;
    }


    private Integer createContact(List<StaticColumn> addressColumns,
                                  List<DinamicColumn> addressCategories,
                                  List<DinamicColumn> addressTelecoms,
                                  List<StaticColumn> customerColumns,
                                  List<DinamicColumn> customerCategories,
                                  DataRow data,
                                  Integer userId,
                                  Integer companyId,
                                  DataImportConfiguration configuration,
                                  UserTransaction transaction) {

        return createContact(addressColumns,
                addressCategories,
                addressTelecoms,
                customerColumns,
                customerCategories,
                data,
                userId,
                companyId,
                configuration);
    }

    private Integer createContact(List<StaticColumn> addressColumns,
                                  List<DinamicColumn> addressCategories,
                                  List<DinamicColumn> addressTelecoms,
                                  List<StaticColumn> customerColumns,
                                  List<DinamicColumn> customerCategories,
                                  DataRow data,
                                  Integer userId,
                                  Integer companyId,
                                  DataImportConfiguration configuration) {
        String addressType = ContactConstants.ADDRESSTYPE_PERSON;

        byte code = CodeUtil.default_;
        if (!customerColumns.isEmpty() || !customerCategories.isEmpty()) {
            code = CodeUtil.customer;
        }

        String lastName = getStaticColumnValue("name1", data, addressColumns);

        String firstName = getStaticColumnValue("name2", data, addressColumns);

        String country = getStaticColumnValue("countryId", data, addressColumns);
        String city = getStaticColumnValue("cityId", data, addressColumns);
        String zip = getStaticColumnValue("cityZip", data, addressColumns);
        String street = getStaticColumnValue("street", data, addressColumns);
        String houseNumber = getStaticColumnValue("houseNumber", data, addressColumns);

        String title = getStaticColumnValue("titleId", data, addressColumns);
        String salutation = getStaticColumnValue("salutationId", data, addressColumns);
        String education = getStaticColumnValue("education", data, addressColumns);
        String keywords = getStaticColumnValue("keywords", data, addressColumns);
        String searchName = getStaticColumnValue("searchName", data, addressColumns);
        String language = getStaticColumnValue("languageId", data, addressColumns);
        List<String> userGroupList = getMultipleStaticColumnValue("userGroupId", data, addressColumns);

        String number = getStaticColumnValue("number", data, customerColumns);
        String source = getStaticColumnValue("sourceId", data, customerColumns);
        String branch = getStaticColumnValue("branchId", data, customerColumns);
        String customerType = getStaticColumnValue("customerTypeId", data, customerColumns);

        String personKey = companyId + "_"
                + lastName.toLowerCase() + "_"
                + (null != firstName ? firstName.toLowerCase() : null) + "_"
                + (null != salutation ? salutation.toLowerCase() : null) + "_"
                + (null != title ? title.toLowerCase() : null);

        Integer personId = personCache.get(personKey);
        if (null != personId) {
            return personId;
        }

        Map addressTelecomMap = buildTelecomMap(addressTelecoms, data);

        Map addressCategoryMap = buildCategoryValuesMap(addressCategories, data, configuration);

        Map customerCategoryMap = buildCategoryValuesMap(customerCategories, data, configuration);

        Integer countryId = null;
        Integer cityId = null;
        if (null != country && !"".equals(country.trim())) {
            countryId = getCountryId(country, companyId);
        }

        if (null != countryId) {
            if (null != city && !"".equals(city)) {
                cityId = getCity(countryId, city, zip, companyId);
            }
        }

        Integer salutationId = null;
        if (null != salutation && !"".equals(salutation.trim())) {
            salutationId = getSaludationId(salutation, companyId);
        }

        Integer titleId = null;
        if (null != title && !"".equals(title.trim())) {
            titleId = getTitleId(title, companyId);
        }

        Integer sourceId = null;
        if (null != source && !"".equals(source)) {
            sourceId = getAddressSource(source, companyId);
        }

        Integer branchId = null;
        if (null != branch && !"".equals(branch)) {
            branchId = getBranch(branch, companyId);
        }

        Integer customerTypeId = null;
        if (null != customerType && !"".equals(customerType)) {
            customerTypeId = getCustomerType(customerType, companyId);
        }

        Integer languageId = getLanguageId(language, companyId);
        String userGroupAccessIds = getUserGroupAddressAccessIds(userGroupList, companyId);

        ImportAddressCmd addressCreateCmd = new ImportAddressCmd();

        addressCreateCmd.putParam("number", number);
        addressCreateCmd.putParam("branchId", branchId);
        addressCreateCmd.putParam("sourceId", sourceId);
        addressCreateCmd.putParam("customerTypeId", customerTypeId);

        addressCreateCmd.setOp("create");
        addressCreateCmd.putParam("companyId", companyId.toString());
        addressCreateCmd.putParam("addressType", addressType);
        addressCreateCmd.putParam("code", code);
        addressCreateCmd.putParam("recordUserId", userId.toString());
        addressCreateCmd.putParam("userTypeValue", "0");
        addressCreateCmd.putParam("salutationId", salutationId);
        addressCreateCmd.putParam("titleId", titleId);
        addressCreateCmd.putParam("name1", lastName);
        addressCreateCmd.putParam("name2", firstName);
        addressCreateCmd.putParam("telecomMap", addressTelecomMap);
        addressCreateCmd.putParam("education", education);
        addressCreateCmd.putParam("keywords", keywords);
        addressCreateCmd.putParam("countryId", countryId);
        addressCreateCmd.putParam("cityId", cityId);
        addressCreateCmd.putParam("houseNumber", houseNumber);
        addressCreateCmd.putParam("street", street);
        addressCreateCmd.putParam("searchName", searchName);
        addressCreateCmd.putParam("languageId", languageId);
        addressCreateCmd.putParam("accessUserGroupIds", userGroupAccessIds);
        addressCreateCmd.executeInStateless(ctx);

        personId = (Integer) addressCreateCmd.getResultDTO().get("addressId");

        if (!addressCategoryMap.isEmpty()) {
            createAddressCategories(addressCategoryMap, personId, companyId);
        }

        if (!customerCategoryMap.isEmpty()) {
            createCustomerCategories(customerCategoryMap, personId, companyId);
        }

        personCache.put(personKey, personId);

        return personId;
    }

    private List<Column> getOrganizationColumns(List<Column> columns) {
        return filterColumnByGroupType(columns, Group.GroupType.ORGANIZATION);
    }

    private List<StaticColumn> getAddressStaticColumns(List<Column> columns) {
        return filterStaticColumnsByColumnType(columns, Column.ColumnType.ADDRESS);
    }

    private List<StaticColumn> getContactPersonStaticColumns(List<Column> columns) {
        return filterStaticColumnsByColumnType(columns, Column.ColumnType.CONTACT_PERSON);
    }

    private List<DinamicColumn> getAddresCategoryColumns(List<Column> columns) {
        List<DinamicColumn> result = new ArrayList<DinamicColumn>();
        for (Column column : columns) {
            if (column instanceof CategoryDinamicColumn && column.getType().equals(Column.ColumnType.ADDRESS)) {
                result.add((DinamicColumn) column);
            }
        }

        return result;
    }

    private List<DinamicColumn> getCustomerCategoryColumns(List<Column> columns) {
        List<DinamicColumn> result = new ArrayList<DinamicColumn>();
        for (Column column : columns) {
            if (column instanceof CategoryDinamicColumn && column.getType().equals(Column.ColumnType.CUSTOMER)) {
                result.add((DinamicColumn) column);
            }
        }

        return result;
    }

    private List<DinamicColumn> getContactPersonCategoryColumns(List<Column> columns) {
        List<DinamicColumn> result = new ArrayList<DinamicColumn>();
        for (Column column : columns) {
            if (column instanceof CategoryDinamicColumn && column.getType().equals(Column.ColumnType.CONTACT_PERSON)) {
                result.add((DinamicColumn) column);
            }
        }

        return result;
    }

    private List<DinamicColumn> getAddressTelecomColumns(List<Column> columns) {
        List<DinamicColumn> result = new ArrayList<DinamicColumn>();
        for (Column column : columns) {
            if (column instanceof TelecomDinamicColumn && column.getType().equals(Column.ColumnType.ADDRESS)) {
                result.add((DinamicColumn) column);
            }
        }

        return result;
    }

    private List<DinamicColumn> getContactPersonTelecomColumns(List<Column> columns) {
        List<DinamicColumn> result = new ArrayList<DinamicColumn>();
        for (Column column : columns) {
            if (column instanceof TelecomDinamicColumn && column.getType().equals(Column.ColumnType.CONTACT_PERSON)) {
                result.add((DinamicColumn) column);
            }
        }

        return result;
    }

    private List<Column> getContactColumns(List<Column> columns) {
        return filterColumnByGroupType(columns, Group.GroupType.PERSON);
    }

    private List<StaticColumn> getCustomerStaticColumns(List<Column> columns) {
        return filterStaticColumnsByColumnType(columns, Column.ColumnType.CUSTOMER);
    }

    private List<Column> getContactPersonColumns(List<Column> columns) {
        return filterColumnByGroupType(columns, Group.GroupType.CONTACT_PERSON);
    }


    private List<Column> filterColumnByGroupType(List<Column> columns, Group.GroupType type) {
        List<Column> result = new ArrayList<Column>();
        for (Column column : columns) {
            if (column.getGroup().getGroupType().equals(type)) {
                result.add(column);
            }
        }

        return result;
    }

    private List<StaticColumn> filterStaticColumnsByColumnType(List<Column> columns, Column.ColumnType type) {
        List<StaticColumn> result = new ArrayList<StaticColumn>();
        for (Column column : columns) {
            if (column instanceof StaticColumn && column.getType().equals(type)) {
                result.add((StaticColumn) column);
            }
        }

        return result;
    }


    private StaticColumn getStaticColumn(String ejbFieldName, List<StaticColumn> columns) {
        for (StaticColumn staticColumn : columns) {
            if (staticColumn.getEjbFieldName().equals(ejbFieldName)) {
                return staticColumn;
            }
        }

        return null;
    }


    private String getStaticColumnValue(String ejbName, DataRow data, List<StaticColumn> staticColumns) {
        String value = null;
        StaticColumn staticColumn = getStaticColumn(ejbName, staticColumns);
        if (null != staticColumn) {
            value = data.getValue(staticColumn.getPosition());
        }

        return value;
    }

    private List<StaticColumn> getMultipleStaticColumn(String ejbFieldName, List<StaticColumn> columns) {
        List<StaticColumn> staticColumnList = new ArrayList<StaticColumn>();
        for (StaticColumn staticColumn : columns) {
            if (staticColumn.getEjbFieldName().equals(ejbFieldName)) {
                staticColumnList.add(staticColumn);
            }
        }
        return staticColumnList;
    }

    private List<String> getMultipleStaticColumnValue(String ejbName, DataRow data, List<StaticColumn> staticColumns) {
        List<String> valueList = new ArrayList<String>();
        List<StaticColumn> staticColumnList = getMultipleStaticColumn(ejbName, staticColumns);

        for (StaticColumn staticColumn : staticColumnList) {
            if (null != staticColumn) {
                String value = data.getValue(staticColumn.getPosition());
                if (value != null) {
                    valueList.add(value);
                }
            }
        }
        return valueList;
    }

    private Integer getTitleId(String titleValue, Integer companyId) {
        Integer titleId = null;

        if (null == titleValue || "".equals(titleValue.trim())) {
            return titleId;
        }

        TitleHome titleHome =
                (TitleHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TITLE);
        try {
            titleId = titleHome.findByTitleName(titleValue, companyId).getTitleId();
        } catch (FinderException e) {
            log.debug("-> Read title titleName=" + titleValue + " FAIL");
            TitleDTO dto = new TitleDTO();
            dto.put("companyId", companyId);
            dto.put("titleName", titleValue);

            try {
                Title title = titleHome.create(dto);
                titleId = title.getTitleId();
            } catch (CreateException e1) {
                throw new RuntimeException("Create Title Object FAIL,", e1);
            }
        }

        log.debug("-> Title titleName=" + titleValue + " OK");
        return titleId;
    }

    private Integer getSaludationId(String salutationValue, Integer companyId) {
        Integer salutationId = null;

        if (null == salutationValue || "".equals(salutationValue.trim())) {
            return salutationId;
        }

        SalutationHome salutationHome =
                (SalutationHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_SALUTATION);
        try {
            Salutation salutation = salutationHome.findByLabel(salutationValue, companyId);
            salutationId = salutation.getSalutationId();
        } catch (FinderException e) {
            log.debug("-> Read salutation salutationLabel=" + salutationValue + " FAIL");
            SalutationDTO dto = new SalutationDTO();
            dto.put("companyId", companyId);
            dto.put("salutationLabel", salutationValue);
            try {
                salutationId = (salutationHome.create(dto)).getSalutationId();
            } catch (CreateException e1) {
                throw new RuntimeException("Create Salutation Object FAIL", e1);
            }
        }
        log.debug("-> Salutation salutationLabel=" + salutationValue + " OK");
        return salutationId;
    }

    private TelecomType getTelecomType(String telecomTypeIdStr) {
        TelecomTypeHome telecomTypeHome = (TelecomTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TELECOMTYPE);
        Integer telecomTypeId = new Integer(telecomTypeIdStr);

        try {
            return telecomTypeHome.findByPrimaryKey(telecomTypeId);
        } catch (FinderException e) {
            log.debug("-> Read TelecomType telecomTypeId=" + telecomTypeId + " FAIL");
        }

        return null;
    }

    private Integer getCity(Integer countryId,
                            String cityName,
                            String zipCode,
                            Integer companyId) {
        Integer cityId = null;
        if (null == cityName || "".equals(cityName.trim())) {
            return cityId;
        }

        CityHome cityHome =
                (CityHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CITY);

        //search city with zip code
        if (null != zipCode && !"".equals(zipCode.trim())) {
            try {
                City city = cityHome.findByZipAndCityNameAndCountry(zipCode, cityName, countryId, companyId);
                cityId = city.getCityId();
            } catch (FinderException e) {
                log.debug("-> Read city cityName=" + cityName + " zip=" + zipCode + " FAIL");
                City city = createCity(countryId, cityName, zipCode, companyId);
                cityId = city.getCityId();
            }
            log.debug("-> City countryId=" + countryId + " name=" + cityName + " zip=" + zipCode + " OK");
            return cityId;
        } else {
            //search city without zip code
            try {
                City city = cityHome.findByCityNameAndCountry(cityName, countryId, companyId);
                cityId = city.getCityId();
            } catch (FinderException e) {
                log.debug("-> Read city cityName=" + cityName + " FAIL");
                City city = createCity(countryId, cityName, null, companyId);
                cityId = city.getCityId();
            }
            log.debug("-> City countryId=" + countryId + " name=" + cityName + " OK");
            return cityId;
        }
    }

    private City createCity(Integer countryId,
                            String cityName,
                            String zipCode,
                            Integer companyId) {
        CityHome cityHome =
                (CityHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CITY);

        CityDTO dto = new CityDTO();
        dto.put("countryId", countryId);
        dto.put("cityName", cityName);
        dto.put("companyId", companyId);
        dto.put("cityZip", zipCode);

        try {
            City city = cityHome.create(dto);
            log.debug("-> Create City Object cityName=" + cityName + " zipCode=" + zipCode + " OK");
            return city;
        } catch (CreateException e) {
            throw new RuntimeException("Create City Object FAIL", e);
        }
    }

    private Integer getCountryId(String countryName, Integer companyId) {
        Integer countryId = null;

        if (null == countryName || "".equals(countryName.trim())) {
            return countryId;
        }

        CountryHome countryHome =
                (CountryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_COUNTRY);

        try {
            Country country = countryHome.findByCountryName(countryName, companyId);
            countryId = country.getCountryId();
        } catch (FinderException e) {
            log.debug("-> Read country countryName=" + countryName + " FAIL");
            CountryDTO dto = new CountryDTO();
            dto.put("countryName", countryName);
            dto.put("companyId", companyId);

            try {
                Country country = countryHome.create(dto);
                countryId = country.getCountryId();
            } catch (CreateException e1) {
                throw new RuntimeException("Create Country Object FAIL", e1);
            }
        }
        log.debug("-> Country countryName=" + countryName + " OK");
        return countryId;
    }

    private Map buildTelecomMap(List<DinamicColumn> columns, DataRow data) {

        Map telecoms = new HashMap();

        for (DinamicColumn column : columns) {
            String telecomId = column.getIdentifierField();
            TelecomType telecomType = getTelecomType(telecomId);

            if (null == telecomType) {
                continue;
            }

            boolean isPredeterminated = false;
            TelecomWrapperDTO wrappedDto = (TelecomWrapperDTO) telecoms.get(telecomId);
            if (null == wrappedDto) {
                wrappedDto = new TelecomWrapperDTO();
                wrappedDto.setTelecomTypeId(telecomId);
                wrappedDto.setTelecomTypeName(telecomType.getTelecomTypeName());
                wrappedDto.setTelecomTypeType(telecomType.getType());
                wrappedDto.setTelecomTypePosition(telecomType.getPosition());
                isPredeterminated = true;
            }

            String value = data.getValue(column.getPosition());

            if (null != value && !"".equals(value.trim())) {
                TelecomDTO telecomDTO = new TelecomDTO();
                telecomDTO.setData(value);
                if (isPredeterminated) {
                    telecomDTO.setPredetermined(true);
                }
                wrappedDto.addTelecomDTO(telecomDTO);
            }

            if (wrappedDto.getTelecoms().size() > 0) {
                telecoms.put(telecomId, wrappedDto);
            }
        }

        return telecoms;
    }


    private Map buildCategoryValuesMap(List<DinamicColumn> dinamicColumns,
                                       DataRow dataRow,
                                       DataImportConfiguration configuration) {
        Map result = new HashMap();

        List<String> pageCategoryIds = new ArrayList<String>();

        Map<String, String> multipleSelectValues = new HashMap<String, String>();

        for (DinamicColumn column : dinamicColumns) {
            String value = dataRow.getValue(column.getPosition());

            //skip the null or empty values
            if (null == value || "".equals(value.trim())) {
                continue;
            }

            Category category = getCategory(new Integer(column.getIdentifierField()));

            //skip the category when could not found it
            if (null == category) {
                continue;
            }

            pageCategoryIds.add(category.getCategoryId().toString());
            String categoryNameKey = "categoryName_" + category.getCategoryId();
            String categoryTypeKey = "categoryType_" + category.getCategoryId();
            String categorySelectedKey = "selected_" + category.getCategoryId();

            result.put(categoryNameKey, category.getCategoryName());
            result.put(categoryTypeKey, category.getCategoryType().toString());

            if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() != category.getCategoryType()) {
                if (CatalogConstants.CategoryType.SINGLE_SELECT.getConstantAsInt() != category.getCategoryType()) {
                    Object newValue = column.applyConverter(configuration, value);

                    result.put(category.getCategoryId().toString(), newValue);
                } else {
                    CategoryValue categoryValue = getCategoryValue(category.getCategoryId(),
                            value,
                            category.getTable(),
                            category.getCompanyId());

                    result.put(category.getCategoryId().toString(), categoryValue.getCategoryValueId().toString());
                }
            } else {
                CategoryValue categoryValue = getCategoryValue(category.getCategoryId(),
                        value,
                        category.getTable(),
                        category.getCompanyId());

                String valuesSelected = multipleSelectValues.get(categorySelectedKey);
                if (null != valuesSelected &&
                        !"".equals(valuesSelected)) {
                    if (!valuesSelected.contains(categoryValue.getCategoryValueId().toString())) {
                        valuesSelected += "," + categoryValue.getCategoryValueId();
                    }
                } else {
                    valuesSelected = categoryValue.getCategoryValueId().toString();
                }

                multipleSelectValues.put(categorySelectedKey, valuesSelected);
            }
        }

        if (!multipleSelectValues.isEmpty()) {
            result.putAll(multipleSelectValues);
        }

        if (!result.isEmpty()) {
            result.put("pageCategoryIds", pageCategoryIds);
        }

        return result;
    }

    private Category getCategory(Integer categoryId) {
        CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);
        try {
            return categoryHome.findByPrimaryKey(categoryId);
        } catch (FinderException e) {
            return null;
        }
    }

    private CategoryValue getCategoryValue(Integer categoryId, String value, String tableId, Integer companyId) {
        CategoryValueHome categoryValueHome =
                (CategoryValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYVALUE);
        List selectedValues = null;
        try {
            selectedValues = (List) categoryValueHome.findByCategoryValueName(categoryId, value, companyId);
        } catch (FinderException e) {
            //
        }
        if (null != selectedValues && !selectedValues.isEmpty()) {
            return (CategoryValue) selectedValues.get(0);
        }

        CategoryValueDTO categoryValueDTO = new CategoryValueDTO();
        categoryValueDTO.put("categoryId", categoryId);
        categoryValueDTO.put("categoryValueName", value);
        categoryValueDTO.put("companyId", companyId);
        categoryValueDTO.put("tableId", tableId);

        try {
            return categoryValueHome.create(categoryValueDTO);
        } catch (CreateException e) {
            log.error("Cannot create categoryValue " + categoryValueDTO, e);
        }

        return null;
    }

    private Integer getBranch(String value, Integer companyId) {
        BranchHome branchHome = (BranchHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_BRANCH);
        List branches = new ArrayList();
        try {
            List elements = (List) branchHome.findByBranchName(value, companyId);
            if (!elements.isEmpty()) {
                return ((Branch) elements.get(0)).getBranchId();
            }
        } catch (FinderException e) {
            //
        }

        BranchDTO dto = new BranchDTO();
        dto.put("companyId", companyId);
        dto.put("branchName", value);
        try {
            return branchHome.create(dto).getBranchId();
        } catch (CreateException e) {
            //
        }

        return null;
    }

    private Integer getCustomerType(String customerTypeValue, Integer companyId) {
        Integer customerTypeId = null;
        if (customerTypeValue == null || "".equals(customerTypeValue.trim())) {
            return customerTypeId;
        }

        CustomerTypeHome customerTypeHome = (CustomerTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CUSTOMERTYPE);
        try {
            List customerTypes = (List) customerTypeHome.findByCustomerTypeName(customerTypeValue, companyId);
            if (customerTypes != null && !customerTypes.isEmpty()) {
                customerTypeId = ((CustomerType) customerTypes.get(0)).getCustomerTypeId();
            }
        } catch (FinderException ignore) {
        }

        if (customerTypeId == null) {
            CustomerTypeDTO dto = new CustomerTypeDTO();
            dto.put("companyId", companyId);
            dto.put("customerTypeName", customerTypeValue);

            try {
                CustomerType customerType = customerTypeHome.create(dto);
                customerTypeId = customerType.getCustomerTypeId();
            } catch (CreateException e) {
                log.debug("Create customer type Object FAIL",e);
            }
        }

        return customerTypeId;
    }

    private Integer getLanguageId(String languageName, Integer companyId) {
        Integer languageId = null;

        LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);

        if (languageName != null && !"".equals(languageName.trim())) {
            try {
                Language language = languageHome.findByLanguageByName(companyId, languageName);
                languageId = language.getLanguageId();
            } catch (FinderException ignore) {
            }

            //create if not exist
            if (languageId == null) {
                LanguageDTO languageDTO = new LanguageDTO();
                languageDTO.put("companyId", companyId);
                languageDTO.put("languageName", languageName);
                languageDTO.put("isDefault", false);

                try {
                    Language language = (Language) languageHome.create(languageDTO);
                    languageId = language.getLanguageId();
                } catch (CreateException e) {
                    log.debug("Create language FAIL",e);
                }
            }
        }

        //if not found, get the default language
        if (languageId == null) {
            try {
                Language language = languageHome.findByDefault(companyId);
                languageId = language.getLanguageId();
            } catch (FinderException ignore) {
            }
        }

        return languageId;
    }

    private Integer getUserGroupId(String userGroupName, Integer companyId) {
        Integer userGroupId = null;
        UserGroupHome userGroupHome = (UserGroupHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERGROUP);

        if (userGroupName == null || "".equals(userGroupName.trim())) {
            return userGroupId;
        }

        try {
            List userGroups = (List) userGroupHome.findByUserGroupName(userGroupName, companyId);
            if (userGroups != null && !userGroups.isEmpty()) {
                userGroupId = ((UserGroup) userGroups.get(0)).getUserGroupId();
            }
        } catch (FinderException ignore) {
        }
        return userGroupId;
    }

    private String getUserGroupAddressAccessIds(List<String> userGroupNameList, Integer companyId) {
        String accessUserGroupIds = "";

        for (String userGroupName : userGroupNameList) {
            Integer userGroupId = getUserGroupId(userGroupName, companyId);
            if (userGroupId != null) {
                if (!accessUserGroupIds.isEmpty()) {
                    accessUserGroupIds += ",";
                }
                accessUserGroupIds += userGroupId.toString();
            }
        }
        return accessUserGroupIds;
    }

    private Integer getAddressSource(String value, Integer companyId) {
        AddressSourceHome addressSourceHome =
                (AddressSourceHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_ADDRESSSOURCE);

        try {
            List elements = (List) addressSourceHome.findByAddressSourceName(value, companyId);
            if (!elements.isEmpty()) {
                return ((AddressSource) elements.get(0)).getAddressSourceId();
            }
        } catch (FinderException e) {
            //
        }

        AddressSourceDTO dto = new AddressSourceDTO();
        dto.put("companyId", companyId);
        dto.put("addressSourceName", value);
        try {
            return addressSourceHome.create(dto).getAddressSourceId();
        } catch (CreateException e) {
            //
        }

        return null;
    }

    private void createAddressCategories(Map categoryMap, Integer id, Integer companyId) {
        List<Map> sourceValues = new ArrayList<Map>();
        Map productMap = new HashMap();
        productMap.put("identifier", "addressId");
        productMap.put("value", id);
        sourceValues.add(productMap);
        CategoryUtilCmd myCmd = new CategoryUtilCmd();
        myCmd.putParam("sourceValues", sourceValues);
        myCmd.putParam(categoryMap);
        myCmd.putParam("companyId", companyId);
        myCmd.setOp("createValues");
        myCmd.executeInStateless(ctx);
    }

    private void updateAddressCategories(Map categoryMap, Integer id, Integer companyId) {
        //update categoryfieldvalues for contact or organization
        String finderName = "findByAddressId";
        Object[] params = new Object[]{id, companyId};
        List paramsAsList = Arrays.asList(params);

        List<Map> sourceValues = new ArrayList<Map>();
        Map productMap = new HashMap();
        productMap.put("identifier", "addressId");
        productMap.put("value", id);
        sourceValues.add(productMap);

        CategoryUtilCmd myCmd = new CategoryUtilCmd();
        myCmd.putParam("sourceValues", sourceValues);
        myCmd.putParam("companyId", companyId);
        myCmd.putParam(categoryMap);
        myCmd.putParam("finderName", finderName);
        myCmd.putParam("params", paramsAsList);
        myCmd.setOp("updateValues");
        myCmd.executeInStateless(ctx);
    }

    private void createCustomerCategories(Map categoryMap, Integer id, Integer companyId) {
        String finderName = "findByCustomerId";
        Object[] params = new Object[]{id, companyId};
        List paramsAsList = Arrays.asList(params);

        List<Map> sourceValues = new ArrayList<Map>();
        Map productMap = new HashMap();
        productMap.put("identifier", "customerId");
        productMap.put("value", id);
        sourceValues.add(productMap);

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.putParam("sourceValues", sourceValues);
        cmd.putParam("companyId", companyId);
        cmd.putParam(categoryMap);
        cmd.putParam("finderName", finderName);
        cmd.putParam("params", paramsAsList);
        cmd.setOp("updateValues");
        cmd.executeInStateless(ctx);
    }

    private void updateCustomerCategories(Map categoryMap, Integer id, Integer companyId) {
        createCustomerCategories(categoryMap, id, companyId);
    }

    private void createContactPersonCategories(Map categoryMap, Integer addressId, Integer contactPersonId, Integer companyId) {
        List<Map> sourceValues = new ArrayList<Map>();
        Map m1 = new HashMap();
        m1.put("identifier", "addressId");
        m1.put("value", addressId);
        sourceValues.add(m1);
        Map m2 = new HashMap();
        m2.put("identifier", "contactPersonId");
        m2.put("value", contactPersonId);
        sourceValues.add(m2);

        CategoryUtilCmd myCmd = new CategoryUtilCmd();
        myCmd.putParam("sourceValues", sourceValues);
        myCmd.putParam(categoryMap);
        myCmd.putParam("companyId", companyId);
        myCmd.setOp("createValues");
        myCmd.executeInStateless(ctx);
    }

    private void updateContactPersonCategories(Map categoryMap, Integer addressId, Integer contactPersonId, Integer companyId) {
        String finderName = "findByAddressIdAndContactPersonId";
        Object[] params = new Object[]{addressId, contactPersonId, companyId};
        List paramsAsList = Arrays.asList(params);

        List<Map> sourceValues = new ArrayList<Map>();
        Map m1 = new HashMap();
        m1.put("identifier", "addressId");
        m1.put("value", addressId);
        sourceValues.add(m1);
        Map m2 = new HashMap();
        m2.put("identifier", "contactPersonId");
        m2.put("value", contactPersonId);
        sourceValues.add(m2);

        CategoryUtilCmd myCmd = new CategoryUtilCmd();
        myCmd.putParam("sourceValues", sourceValues);
        myCmd.putParam("companyId", companyId);
        myCmd.putParam(categoryMap);
        myCmd.putParam("finderName", finderName);
        myCmd.putParam("params", paramsAsList);
        myCmd.setOp("updateValues");
        myCmd.executeInStateless(ctx);
    }

    private Customer createCustomer(Integer addressId, Integer companyId, String number) {
        CustomerUtilCmd customerUtilCmd = new CustomerUtilCmd();
        customerUtilCmd.setOp("createCustomerWithNumber");
        customerUtilCmd.putParam("addressId", addressId);
        customerUtilCmd.putParam("companyId", companyId);
        customerUtilCmd.putParam("number", number);
        customerUtilCmd.executeInStateless(ctx);

        return findCustomer(addressId);
    }

    private void mergeTelecomValues(Map telecomMap, Integer addressId,Integer contactPersonId, Integer companyId) {

        // creating telecoms
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        if (telecomMap != null && companyId != null && telecomMap.size() > 0) {
            for (Iterator iterator = telecomMap.values().iterator(); iterator.hasNext();) {
                TelecomWrapperDTO telecomWrapperDTO = (TelecomWrapperDTO) iterator.next();

                Integer telecomTypeId = new Integer(telecomWrapperDTO.getTelecomTypeId());
                boolean isPredetermined = false;
                if (!existPredeterminedTelecom(addressId, contactPersonId, telecomTypeId)) {
                    isPredetermined = true;
                }

                for (Iterator iterator1 = telecomWrapperDTO.getTelecoms().iterator(); iterator1.hasNext();) {
                    TelecomDTO telecomDTO = (TelecomDTO) iterator1.next();
                    try {
                        Telecom telecom = telecomHome.create(addressId, contactPersonId, telecomDTO.getData(),
                                telecomDTO.getDescription(), isPredetermined, telecomTypeId, companyId);

                        isPredetermined = false;
                    } catch (CreateException e) {
                        log.error("Unexpected error creating telecoms in merge", e);
                    }
                }

            }
        }
    }

    private boolean existPredeterminedTelecom(Integer addressId, Integer contactPersonId, Integer telecomTypeId) {
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        Telecom telecom = null;
        try {
            if (contactPersonId != null) {
                telecom = telecomHome.findContactPersonDefaultTelecomsByTypeId(addressId, contactPersonId, telecomTypeId);
            } else {
                telecom = telecomHome.findAddressDefaultTelecomsByTypeId(addressId, telecomTypeId);
            }
        } catch (FinderException e) {
            log.debug("Not found predetermined telecom.. " + e);
        }
        return telecom != null;
    }

    private RecordColumn findRecordColumn(Integer importColumnId, Integer importRecordId) {
        RecordColumnHome recordColumnHome = (RecordColumnHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_RECORDCOLUMN);
        RecordColumnPK recordColumnPK = new RecordColumnPK(importColumnId, importRecordId);
        try {
            return recordColumnHome.findByPrimaryKey(recordColumnPK);
        } catch (FinderException e) {
            log.debug("Error in find record column importColumnId:" + importColumnId + " importRecordId:" + importRecordId, e);
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

    private ImportRecord findImportRecord(Integer importRecordId) {
        ImportRecordHome importRecordHome = (ImportRecordHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTRECORD);
        if (importRecordId != null) {
            try {
                return importRecordHome.findByPrimaryKey(importRecordId);
            } catch (FinderException e) {
                log.debug("Not found import record:" + importRecordId);
            }
        }
        return null;
    }

    private ImportRecord findImportRecordByIdentityKey(String identityKey, Integer profileId, Integer companyId) {
        ImportRecordHome importRecordHome = (ImportRecordHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTRECORD);
        if (identityKey != null && profileId != null && companyId != null) {
            try {
                return importRecordHome.findByIdentityKeyProfileId(identityKey, profileId, companyId);
            } catch (FinderException e) {
                log.debug("Not found import record by identity key:" + identityKey + " " + profileId);
            }
        }
        return null;
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

    private DeduplicationAddressService getDeduplicationAddressService() {
        DeduplicationAddressServiceHome home = (DeduplicationAddressServiceHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DEDUPLICATIONADDRESSSERVICE);
        try {
            return home.create();
        } catch (CreateException e) {
            log.debug("Create DeduplicationAddressService Fail.. ", e);
        }
        return null;
    }

}
                 