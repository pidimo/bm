package com.piramide.elwis.service.contact;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.contactmanager.CustomerUtilCmd;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.*;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.domain.contactmanager.TelecomType;
import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.domain.productmanager.*;
import com.piramide.elwis.domain.project.*;
import com.piramide.elwis.domain.reportmanager.Report;
import com.piramide.elwis.domain.reportmanager.ReportHome;
import com.piramide.elwis.domain.salesmanager.*;
import com.piramide.elwis.domain.schedulermanager.Appointment;
import com.piramide.elwis.domain.schedulermanager.AppointmentHome;
import com.piramide.elwis.domain.schedulermanager.Task;
import com.piramide.elwis.domain.schedulermanager.TaskHome;
import com.piramide.elwis.domain.supportmanager.SupportCase;
import com.piramide.elwis.domain.supportmanager.SupportCaseHome;
import com.piramide.elwis.domain.webmailmanager.AddressGroup;
import com.piramide.elwis.domain.webmailmanager.AddressGroupHome;
import com.piramide.elwis.dto.contactmanager.*;
import com.piramide.elwis.dto.productmanager.ProductSupplierDTO;
import com.piramide.elwis.dto.projects.ProjectAssigneeDTO;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.utils.deduplication.ContactMergeField;
import com.piramide.elwis.utils.deduplication.ContactMergeWrapper;
import com.piramide.elwis.utils.deduplication.exception.DeleteAddressException;
import com.piramide.elwis.utils.deduplication.exception.MergeAddressException;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.*;
import javax.transaction.UserTransaction;
import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class ContactMergeServiceBean implements SessionBean {
    private Log log = LogFactory.getLog(this.getClass());

    private SessionContext ctx;

    public ContactMergeServiceBean() {
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

    public void deleteDuplicateAddressProcess(Integer duplicateGroupId, Integer addressId) throws DeleteAddressException {
        log.debug("Delete address and all relations " + addressId);

        UserTransaction transaction = ctx.getUserTransaction();

        Address address = findAddress(addressId);
        if (address != null) {
            try {
                transaction.begin();

                deleteAllAddress(address);
                redefineMainDuplicateAddress(duplicateGroupId);
                removeDuplicateGroupIfIsRequired(duplicateGroupId);

                transaction.commit();
            } catch (Exception e) {
                log.error("Fail delete Address.. ", e);
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    log.debug("System error in delete rollback..", e1);
                }
                throw new DeleteAddressException(e);
            }
        }
    }

    public void removeDuplicateGroup(Integer duplicateGroupId) throws MergeAddressException {
        UserTransaction transaction = ctx.getUserTransaction();

        DuplicateGroup duplicateGroup = findDuplicateGroup(duplicateGroupId);
        if (duplicateGroup != null) {
            try {
                transaction.begin();
                duplicateGroup.remove();
                transaction.commit();
            } catch (Exception e) {
                log.error("Fail remove duplicate group.. ", e);
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    log.debug("System error in rollback..", e1);
                }
                throw new MergeAddressException(e);
            }
        }
    }

    public void keepAllDuplicateAddressProcess(Integer duplicateGroupId) throws MergeAddressException {
        log.debug("Keep all contact merge process... " + duplicateGroupId);
        UserTransaction transaction = ctx.getUserTransaction();

        DuplicateGroup duplicateGroup = findDuplicateGroup(duplicateGroupId);

        if (duplicateGroup != null) {
            try {
                transaction.begin();
                duplicateGroup.remove();
                transaction.commit();
            } catch (Exception e) {
                log.error("Fail remove duplicate group.. ", e);
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    log.debug("System error in rollback..", e1);
                }
                throw new MergeAddressException(e);
            }
        }
    }

    public void keepDuplicateAddressProcess(Integer duplicateGroupId, Integer addressId) throws MergeAddressException {
        log.debug("Keep contact merge process... " + duplicateGroupId + " " + addressId);
        UserTransaction transaction = ctx.getUserTransaction();

        DuplicateAddress duplicateAddress = findDuplicateAddress(duplicateGroupId, addressId);
        if (duplicateAddress != null) {
            try {
                transaction.begin();

                duplicateAddress.remove();
                redefineMainDuplicateAddress(duplicateGroupId);
                removeDuplicateGroupIfIsRequired(duplicateGroupId);

                transaction.commit();
            } catch (Exception e) {
                log.error("Fail remove duplicate address.. ", e);
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    log.debug("System error in rollback..", e1);
                }
                throw new MergeAddressException(e);
            }
        }
    }

    public boolean mergeDuplicateContact(ContactMergeWrapper contactMergeWrapper) throws MergeAddressException {
        log.debug("Merge contacts with contactMergeWrapper.. " + contactMergeWrapper);
        boolean success = false;
        Integer duplicateGroupId = contactMergeWrapper.getDuplicateGroupId();
        Integer addressId1 = contactMergeWrapper.getAddressId1();
        Integer addressId2 = contactMergeWrapper.getAddressId2();

        UserTransaction transaction = ctx.getUserTransaction();

        Address address1 = findAddress(addressId1);
        Address address2 = findAddress(addressId2);
        if (address1 != null && address2 != null) {
            try {
                transaction.begin();

                mergeAddress1InAddress2(address1, address2, contactMergeWrapper);
                copyAllRelationsOfAddress1InAddress2(address1, address2);
                removeMergedAddress(address1);
                redefineMainDuplicateAddress(duplicateGroupId);
                removeDuplicateGroupIfIsRequired(duplicateGroupId);

                transaction.commit();
                transaction.setTransactionTimeout(0);

                success = true;
            } catch (Exception e) {
                log.error("Fail contact Merge.. ", e);
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    log.error("System error in rollback..", e1);
                }

                MergeAddressException mergeAddressException;
                if (e instanceof MergeAddressException) {
                    mergeAddressException = (MergeAddressException) e;
                } else {
                    mergeAddressException = new MergeAddressException(e);
                }
                throw mergeAddressException;
            }
        }
        return success;
    }

    private void mergeAddress1InAddress2(Address address1, Address address2, ContactMergeWrapper contactMergeWrapper) {
        List<ContactMergeField> contactMergeFieldList = contactMergeWrapper.getContactMergeFieldList();

        if (isAddressMergeField("name1", contactMergeFieldList)) {
            address2.setName1(address1.getName1());
        }
        if (isAddressMergeField("name2", contactMergeFieldList)) {
            address2.setName2(address1.getName2());
        }
        if (isAddressMergeField("name3", contactMergeFieldList)) {
            address2.setName3(address1.getName3());
        }

        if (isAddressMergeField("countryId", contactMergeFieldList)) {
            boolean isSameCountry = (address2.getCountryId() != null && address2.getCountryId().equals(address1.getCountryId()));
            address2.setCountryId(address1.getCountryId());
            if (!isSameCountry) {
                address2.setCityId(null);
            }
        }

        if (isAddressMergeField("cityId", contactMergeFieldList)) {
            boolean isSameCountry = (address2.getCountryId() != null && address2.getCountryId().equals(address1.getCountryId()));
            if (isSameCountry || address1.getCityId() == null) {
                address2.setCityId(address1.getCityId());
            }
        }

        if (isAddressMergeField("street", contactMergeFieldList)) {
            address2.setStreet(address1.getStreet());
        }
        if (isAddressMergeField("houseNumber", contactMergeFieldList)) {
            address2.setHouseNumber(address1.getHouseNumber());
        }
        if (isAddressMergeField("titleId", contactMergeFieldList)) {
            address2.setTitleId(address1.getTitleId());
        }
        if (isAddressMergeField("salutationId", contactMergeFieldList)) {
            address2.setSalutationId(address1.getSalutationId());
        }
        if (isAddressMergeField("education", contactMergeFieldList)) {
            address2.setEducation(address1.getEducation());
        }
        if (isAddressMergeField("keywords", contactMergeFieldList)) {
            address2.setKeywords(address1.getKeywords());
        }
        if (isAddressMergeField("searchName", contactMergeFieldList)) {
            address2.setSearchName(address1.getSearchName());
        }
        if (isAddressMergeField("languageId", contactMergeFieldList)) {
            address2.setLanguageId(address1.getLanguageId());
        }

        //customer
        Customer customer1 = findCustomer(address1.getAddressId());
        Customer customer2 = findCustomer(address2.getAddressId());

        if (customer2 == null && existMergeCustomerFields(contactMergeFieldList)) {
            customer2 = createCustomer(address2.getAddressId(), address2.getCompanyId(), null);
        }

        if (customer1 != null && customer2 != null) {

            if (isCustomerMergeField("number", contactMergeFieldList)) {
                customer2.setNumber(customer1.getNumber());
            }
            if (isCustomerMergeField("sourceId", contactMergeFieldList)) {
                customer2.setSourceId(customer1.getSourceId());
            }
            if (isCustomerMergeField("branchId", contactMergeFieldList)) {
                customer2.setBranchId(customer1.getBranchId());
            }
            if (isCustomerMergeField("customerTypeId", contactMergeFieldList)) {
                customer2.setCustomerTypeId(customer1.getCustomerTypeId());
            }
        }

        //merge categories
        mergeCategoriesFields(address1, address2, contactMergeFieldList);

        //merge telecoms
        mergeAddressTelecomFields(address1, address2, contactMergeFieldList);
    }

    private void copyAllRelationsOfAddress1InAddress2(Address address1, Address address2) throws CreateException, RemoveException, MergeAddressException {
        //todo contact copy relations
        Integer companyId = address1.getCompanyId();
        Integer addressId = address1.getAddressId();
        Integer newAddressId = address2.getAddressId();

        //firts copy all strong relations
        copyInAddressNullFields(address1, address2);
        copyAllCustomerRelations(addressId, address2);
        copyAllSupplierRelations(addressId, address2);
        copyAllEmployeeRelations(addressId, address2);
        copyTelecomsRelations(addressId, newAddressId);
        copyAllContactPersonRelations(address1, address2);

        //sales
        copySaleRelations(addressId, newAddressId);
        copySalesProcessRelations(addressId, newAddressId);
        copyProductContractRelations(addressId, newAddressId);

        //product
        copyCompetitorProductRelations(addressId, newAddressId);

        //finance
        copyInvoiceRelations(addressId, newAddressId);
        copySequenceRuleRelations(addressId, newAddressId);

        //campaign
        copyCampaignContactRelations(addressId, newAddressId);
        copySentLogContactRelations(addressId, newAddressId);

        //project
        copyProjectTimeRelations(addressId, newAddressId);
        copyProjectAssigneeRelationsComposedKey(addressId, newAddressId);

        //scheduler
        copyAppointmentRelations(addressId, newAddressId);
        copySupportCaseRelations(addressId, newAddressId);
        copyTaskRelations(addressId, newAddressId);

        //admin
        copyUserRelations(addressId, newAddressId, companyId);

        //customer
        copyCustomerAddressesRelations(addressId, newAddressId);

        //employee
        copyEmployeeAddressesRelations(addressId, newAddressId);

        //webmail
        copyAddressGroupRelations(addressId, newAddressId);

        //contact
        copyBankAccountRelations(addressId, newAddressId);
        copyCommunicationRelations(addressId, newAddressId);
        copyDepartmentRelations(addressId, newAddressId);
        copyOfficeRelations(addressId, newAddressId);
        copyAddressRelationRelations(addressId, newAddressId);
        copyAdditionalAddressRelations(addressId, newAddressId);
        copyImportRecordRelations(addressId, newAddressId);

        copyRecentRelationsComposedKey(addressId, newAddressId);
        copyFavoriteRelationsComposedKey(addressId, newAddressId);
        copyRecordDuplicateRelationsComposedKey(addressId, newAddressId);
        copyUserAddressAccessRelationsComposedKey(addressId, newAddressId);

    }

    private void copyAllContactPersonRelations(Address address1, Address address2) throws CreateException, RemoveException {
        //contact persons
        Collection contactPersonList = address1.getContactPersons();
        for (Iterator iterator = contactPersonList.iterator(); iterator.hasNext();) {
            ContactPerson contactPerson = (ContactPerson) iterator.next();
            Integer newAddressId = address2.getAddressId();
            Integer newContactPersonId = contactPerson.getContactPersonId();

            copyAllContactPersonRelations(contactPerson, newAddressId, newContactPersonId);
        }

        //address as contact person of
        List<ContactPerson> addressAsContactPersonList = findAddressAsContactPerson(address1.getAddressId(), address1.getCompanyId());
        for (ContactPerson contactPerson : addressAsContactPersonList) {
            Integer newAddressId = contactPerson.getAddressId();
            Integer newContactPersonId = address2.getAddressId();

            copyAllContactPersonRelations(contactPerson, newAddressId, newContactPersonId);
        }
    }

    private void copyAllContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) throws CreateException, RemoveException {

        //create new contact person merged
        createNewContactPersonAndMerge(contactPerson, newAddressId, newContactPersonId);

        //sale
        copySalePositionContactPersonRelations(contactPerson, newAddressId, newContactPersonId);
        copySaleContactPersonRelations(contactPerson, newAddressId, newContactPersonId);
        copyProductContractContactPersonRelations(contactPerson, newAddressId, newContactPersonId);

        //finance
        copyInvoiceContactPersonRelations(contactPerson, newAddressId, newContactPersonId);

        //campaign
        copyCampaignContactContactPersonRelations(contactPerson, newAddressId, newContactPersonId);
        copySentLogContactContactPersonRelations(contactPerson, newAddressId, newContactPersonId);

        //project
        copyProjectContactPersonRelations(contactPerson, newAddressId, newContactPersonId);

        //scheduler
        copyAppointmentContactPersonRelations(contactPerson, newAddressId, newContactPersonId);
        copySupportCaseContactPersonRelations(contactPerson, newAddressId, newContactPersonId);
        copyTaskContactPersonRelations(contactPerson, newAddressId, newContactPersonId);

        //customer
        copyCustomerContactPersonsContactPersonRelations(contactPerson, newAddressId, newContactPersonId);

        //supplier
        copyProductSupplierContactPersonRelationsComposedKeySupplier(contactPerson, newAddressId, newContactPersonId);

        //contact
        copyCommunicationContactPersonRelations(contactPerson, newAddressId, newContactPersonId);
        copyDepartmentContactPersonRelations(contactPerson, newAddressId, newContactPersonId);
        copyAddressGroupContactPersonRelations(contactPerson, newAddressId, newContactPersonId);

        copyTelecomsContactPersonRelations(contactPerson, newAddressId, newContactPersonId);
        copyCategoryFieldValueContactPersonRelations(contactPerson, newAddressId, newContactPersonId);
    }

    private void copyAllCustomerRelations(Integer addressId1, Address address2) throws CreateException {
        Customer customer = findCustomer(addressId1);
        if (customer != null) {
            Customer newCustomer = createNewCustomerAndMerge(customer, address2);
            Integer customerId = customer.getCustomerId();
            Integer newCustomerId = newCustomer.getCustomerId();

            copySaleRelationsCustomer(customerId, newCustomerId);
            copySalePositionRelationsCustomer(customerId, newCustomerId);
            copyProjectRelationsCustomer(customerId, newCustomerId);
        }
    }

    private void copyAllSupplierRelations(Integer addressId1, Address address2) throws CreateException, RemoveException {
        Supplier supplier = findSupplier(addressId1);

        if (supplier != null) {
            Supplier newSupplier = createNewSupplierAndMerge(supplier, address2);
            Integer supplierId = supplier.getSupplierId();
            Integer newSupplierId = newSupplier.getSupplierId();

            copyIncomingInvoiceRelationsSupplier(supplierId, newSupplierId);
            copyProductSupplierRelationsComposedKeySupplier(supplierId, newSupplierId);
        }
    }

    private void copyAllEmployeeRelations(Integer addressId1, Address address2) throws CreateException, RemoveException {
        Employee employee = findEmployee(addressId1);

        if (employee != null) {
            Employee newEmployee = createNewEmployeeAndMerge(employee, address2);
            Integer employeeId = employee.getEmployeeId();
            Integer newEmployeeId = newEmployee.getEmployeeId();

            copySalesProcessRelationsEmployee(employeeId, newEmployeeId);
            copySaleRelationsEmployee(employeeId, newEmployeeId);
            copyReportRelationsEmployee(employeeId, newEmployeeId);
            copyProductContractRelationsEmployee(employeeId, newEmployeeId);
            copyOfficeRelationsEmployee(employeeId, newEmployeeId);
            copyCustomerRelationsEmployee(employeeId, newEmployeeId);
            copyCommunicationRelationsEmployee(employeeId, newEmployeeId);
            copyCampaignRelationsEmployee(employeeId, newEmployeeId);
        }
    }

    private void removeMergedAddress(Address address) throws RemoveException {
        //todo: remove address
        Integer addressId = address.getAddressId();
        Integer companyId = address.getCompanyId();

        deleteCategoryFieldValueAddress(addressId, companyId);
        deleteTelecoms(addressId);
        deleteMergedSupplier(addressId);
        deleteMergedCustomer(addressId);
        deleteMergedEmployee(addressId);

        address.setBankAccountId(null);
        // removing freetexts
        if (address.getImageFreeText() != null) {
            ContactFreeText image = address.getImageFreeText();
            address.setImageFreeText(null);
            image.remove();
        }
        if (address.getFreeText() != null) {
            address.getFreeText().remove();
        }
        if (address.getWayDescription() != null) {
            address.getWayDescription().remove();
        }

        deleteMergedContactPersons(address);
        deleteDuplicateAddress(addressId);
        deleteAdditionalAddress(addressId);

        address.remove();
    }

    private void deleteMergedContactPersons(Address address) throws RemoveException {
        //contact persons
        Collection contactPersonList = address.getContactPersons();
        Object[] obj = contactPersonList.toArray();
        for (int i = 0; i < obj.length; i++) {
            ContactPerson contactPerson = (ContactPerson) obj[i];
            deleteMergedContactPerson(contactPerson);
        }

        //address as contact person of
        List<ContactPerson> addressAsContactPersonList = findAddressAsContactPerson(address.getAddressId(), address.getCompanyId());
        for (ContactPerson contactPerson : addressAsContactPersonList) {
            deleteMergedContactPerson(contactPerson);
        }
    }

    private void deleteMergedContactPerson(ContactPerson contactPerson) throws RemoveException {
        Integer addressId = contactPerson.getAddressId();
        Integer contactPersonId = contactPerson.getContactPersonId();
        Integer companyId = contactPerson.getCompanyId();

        deleteTelecomsContactPerson(addressId, contactPersonId);
        deleteCategoryFieldValueContactPerson(addressId, contactPersonId, companyId);

        contactPerson.remove();
    }

    private void deleteAllAddress(Address address) throws RemoveException {
        Integer addressId = address.getAddressId();
        Integer companyId = address.getCompanyId();

        deleteCategoryFieldValueAddress(addressId, companyId);
        deleteTelecoms(addressId);

        address.setBankAccountId(null);
        // removing freetexts
        if (address.getImageFreeText() != null) {
            ContactFreeText image = address.getImageFreeText();
            address.setImageFreeText(null);
            image.remove();
        }
        if (address.getFreeText() != null) {
            address.getFreeText().remove();
        }
        if (address.getWayDescription() != null) {
            address.getWayDescription().remove();
        }

        // removing communications
        Iterator communications = address.getCommunications().iterator();
        ContactFreeText communicationFreetext = null;
        while (communications.hasNext()) {
            Contact communication = (Contact) communications.next();
            communicationFreetext = communication.getContactFreeText();
            communication.remove();
            if (communicationFreetext != null) {
                communicationFreetext.remove();
            }
            communications = address.getCommunications().iterator();
        }

        //contact persons
        Collection contactPersonList = address.getContactPersons();
        Object[] obj = contactPersonList.toArray();
        for (int i = 0; i < obj.length; i++) {
            ContactPerson contactPerson = (ContactPerson) obj[i];
            deleteMergedContactPerson(contactPerson);
        }

        deleteAdditionalAddress(addressId);
        deleteDuplicateAddress(addressId);

        address.remove();
    }

    private void deleteMergedSupplier(Integer addressId) throws RemoveException {
        Supplier supplier = findSupplier(addressId);
        if (supplier != null) {
            supplier.remove();
        }
    }

    private void deleteMergedCustomer(Integer addressId) throws RemoveException {
        Customer customer = findCustomer(addressId);
        if (customer != null) {
            deleteCategoryFieldValueCustomer(customer.getCustomerId(), customer.getCompanyId());
            customer.remove();
        }
    }

    private void deleteMergedEmployee(Integer addressId) throws RemoveException {
        Employee employee = findEmployee(addressId);
        if (employee != null) {
            employee.remove();
        }
    }

    private void removeDuplicateGroupIfIsRequired(Integer duplicateGroupId) throws RemoveException {
        DuplicateGroup duplicateGroup = findDuplicateGroup(duplicateGroupId);
        if (duplicateGroup != null) {
            Collection duplicateAddressList = duplicateGroup.getDuplicateAddress();
            if (duplicateAddressList.size() <= 1) {
                duplicateGroup.remove();
            }
        }
    }

    private void copyInAddressNullFields(Address address1, Address address2) throws RemoveException {

        if (address2.getActive() == null) {
            address2.setActive(address1.getActive());
        }
        if (address2.getAdditionalAddressLine() == null) {
            address2.setAdditionalAddressLine(address1.getAdditionalAddressLine());
        }
        if (address2.getBankAccountId() == null) {
            address2.setBankAccountId(address1.getBankAccountId());
        }
        if (address2.getBirthday() == null) {
            address2.setBirthday(address1.getBirthday());
        }
        if (address2.getEducation() == null) {
            address2.setEducation(address1.getEducation());
        }
        if (address2.getImageId() == null) {
            address2.setImageId(address1.getImageId());
            address1.setImageId(null);
        }
        if (address2.getIsPublic() == null) {
            address2.setIsPublic(address1.getIsPublic());
        }
        if (address2.getPersonal() == null) {
            address2.setPersonal(address1.getPersonal());
        }
        if (address2.getSearchName() == null) {
            address2.setSearchName(address1.getSearchName());
        }
        if (address2.getTaxNumber() == null) {
            address2.setTaxNumber(address1.getTaxNumber());
        }

        FreeText freeText = address2.getFreeText();
        String freeTextValue = getAddressFreeTextValue(freeText);
        if (freeTextValue == null || freeTextValue.trim().isEmpty()) {
            address2.setFreeText(address1.getFreeText());
            address1.setFreeText(null);
            if (freeText != null) {
                freeText.remove();
            }
        }

        FreeText wayDescription = address2.getWayDescription();
        String wayDescriptionValue = getAddressFreeTextValue(wayDescription);
        if (wayDescriptionValue == null || wayDescriptionValue.trim().isEmpty()) {
            address2.setWayDescription(address1.getWayDescription());
            address1.setWayDescription(null);
            if (wayDescription != null) {
                wayDescription.remove();
            }
        }
    }

    private String getAddressFreeTextValue(FreeText freeText) {
        String value = null;
        if (freeText != null && freeText.getValue() != null) {
            value = new String(freeText.getValue()).trim();
        }
        return value;
    }

    private void copyCampaignRelationsEmployee(Integer employeeId, Integer newEmployeeId) {
        CampaignHome campaignHome = (CampaignHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN);
        try {
            Collection collection = campaignHome.findByEmployeeId(employeeId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Campaign campaign = (Campaign) iterator.next();
                campaign.setEmployeeId(newEmployeeId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyCommunicationRelationsEmployee(Integer employeeId, Integer newEmployeeId) {
        ContactHome contactHome = (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
        try {
            Collection collection = contactHome.findByEmployeeId(employeeId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Contact contact = (Contact) iterator.next();
                contact.setEmployeeId(newEmployeeId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyCustomerRelationsEmployee(Integer employeeId, Integer newEmployeeId) {
        CustomerHome customerHome = (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);
        try {
            Collection collection = customerHome.findByEmployeeId(employeeId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Customer customer = (Customer) iterator.next();
                customer.setEmployeeId(newEmployeeId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyOfficeRelationsEmployee(Integer employeeId, Integer newEmployeeId) {
        OfficeHome officeHome = (OfficeHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_OFFICE);
        try {
            Collection collection = officeHome.findBySupervisorId(employeeId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Office office = (Office) iterator.next();
                office.setSupervisorId(newEmployeeId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyProductContractRelationsEmployee(Integer employeeId, Integer newEmployeeId) {
        ProductContractHome productContractHome = (ProductContractHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PRODUCTCONTRACT);
        try {
            Collection collection = productContractHome.findBySellerId(employeeId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                ProductContract productContract = (ProductContract) iterator.next();
                productContract.setSellerId(newEmployeeId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyReportRelationsEmployee(Integer employeeId, Integer newEmployeeId) {
        ReportHome reportHome = (ReportHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_REPORT);
        try {
            Collection collection = reportHome.findByEmployeeId(employeeId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Report report = (Report) iterator.next();
                report.setEmployeeId(newEmployeeId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copySaleRelationsEmployee(Integer employeeId, Integer newEmployeeId) {
        SaleHome saleHome = (SaleHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALE);
        try {
            Collection collection = saleHome.findBySellerId(employeeId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Sale sale = (Sale) iterator.next();
                sale.setSellerId(newEmployeeId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copySalesProcessRelationsEmployee(Integer employeeId, Integer newEmployeeId) {
        SalesProcessHome salesProcessHome = (SalesProcessHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALESPROCESS);
        try {
            Collection collection = salesProcessHome.findByEmployeeId(employeeId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                SalesProcess salesProcess = (SalesProcess) iterator.next();
                salesProcess.setEmployeeId(newEmployeeId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyEmployeeAddressesRelations(Integer addressId, Integer newAddressId) {
        EmployeeHome employeeHome = (EmployeeHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_EMPLOYEE);
        //health fund address
        try {
            Collection collection = employeeHome.findByHealthFundId(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Employee employee = (Employee) iterator.next();
                employee.setHealthFundId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyCustomerAddressesRelations(Integer addressId, Integer newAddressId) {
        CustomerHome customerHome = (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);
        //partner address
        try {
            Collection collection = customerHome.findByPartnerId(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Customer customer = (Customer) iterator.next();
                customer.setPartnerId(newAddressId);
            }
        } catch (FinderException ignore) {
        }

        //invoice address
        try {
            Collection collection = customerHome.findByInvoiceAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Customer customer = (Customer) iterator.next();
                customer.setInvoiceAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyCustomerContactPersonsContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        CustomerHome customerHome = (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);
        //invoice contact person
        try {
            Collection collection = customerHome.findByInvoiceContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Customer customer = (Customer) iterator.next();
                customer.setInvoiceAddressId(newAddressId);
                customer.setInvoiceContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyUserRelations(Integer addressId, Integer newAddressId, Integer companyId) throws MergeAddressException {
        User user = findUserByAddressId(addressId, companyId);
        if (user != null) {
            User newUser = findUserByAddressId(newAddressId, companyId);
            if (newUser == null) {
                user.setAddressId(newAddressId);
            } else {
                MergeAddressException mergeAddressException = new MergeAddressException("Cannot merge an address as user with other address as user...");
                mergeAddressException.setUserMergeError(true);
                throw mergeAddressException;
            }
        }
    }

    private void copyAddressRelationRelations(Integer addressId, Integer newAddressId) {
        AddressRelationHome addressRelationHome = (AddressRelationHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESSRELATION);
        try {
            Collection collection = addressRelationHome.findByAddressId(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                AddressRelation addressRelation = (AddressRelation) iterator.next();
                addressRelation.setAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
        //related address
        try {
            Collection collection = addressRelationHome.findByRelatedAddressId(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                AddressRelation addressRelation = (AddressRelation) iterator.next();
                addressRelation.setRelatedAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyAppointmentRelations(Integer addressId, Integer newAddressId) {
        AppointmentHome appointmentHome = (AppointmentHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_APPOINTMENT);
        try {
            Collection collection = appointmentHome.findByAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Appointment appointment = (Appointment) iterator.next();
                appointment.setAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyAppointmentContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        AppointmentHome appointmentHome = (AppointmentHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_APPOINTMENT);
        try {
            Collection collection = appointmentHome.findByContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Appointment appointment = (Appointment) iterator.next();
                appointment.setAddressId(newAddressId);
                appointment.setContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyBankAccountRelations(Integer addressId, Integer newAddressId) {
        BankAccountHome bankAccountHome = (BankAccountHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_BANKACCOUNT);
        try {
            Collection collection = bankAccountHome.findByAddressId(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                BankAccount bankAccount = (BankAccount) iterator.next();
                bankAccount.setAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyCampaignContactRelations(Integer addressId, Integer newAddressId) {
        CampaignContactHome campContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);
        try {
            Collection collection = campContactHome.findByAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                CampaignContact campaignContact = (CampaignContact) iterator.next();
                campaignContact.setAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyCampaignContactContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        CampaignContactHome campContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);
        try {
            Collection collection = campContactHome.findByContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                CampaignContact campaignContact = (CampaignContact) iterator.next();
                campaignContact.setAddressId(newAddressId);
                campaignContact.setContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyCompetitorProductRelations(Integer addressId, Integer newAddressId) {
        CompetitorProductHome competitorProductHome = (CompetitorProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_COMPETITORPRODUCT);
        try {
            Collection collection = competitorProductHome.findByCompetitorId(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                CompetitorProduct competitorProduct = (CompetitorProduct) iterator.next();
                competitorProduct.setCompetitorId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyCommunicationRelations(Integer addressId, Integer newAddressId) {
        ContactHome contactHome = (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
        try {
            Collection collection = contactHome.findByAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Contact contact = (Contact) iterator.next();
                contact.setAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyCommunicationContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        ContactHome contactHome = (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
        try {
            Collection collection = contactHome.findByContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Contact contact = (Contact) iterator.next();
                contact.setAddressId(newAddressId);
                contact.setContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyDepartmentRelations(Integer addressId, Integer newAddressId) {
        DepartmentHome departmentHome = (DepartmentHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DEPARTMENT);
        try {
            Collection collection = departmentHome.findByAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Department department = (Department) iterator.next();
                department.setOrganizationId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyDepartmentContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        DepartmentHome departmentHome = (DepartmentHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DEPARTMENT);
        try {
            Collection collection = departmentHome.findByContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Department department = (Department) iterator.next();
                department.setOrganizationId(newAddressId);
                department.setManagerId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyIncomingInvoiceRelationsSupplier(Integer supplierId, Integer newSupplierId) {
        IncomingInvoiceHome incomingInvoiceHome = (IncomingInvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INCOMINGINVOICE);
        try {
            Collection collection = incomingInvoiceHome.findBySupplierId(supplierId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                IncomingInvoice incomingInvoice = (IncomingInvoice) iterator.next();
                incomingInvoice.setSupplierId(newSupplierId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyInvoiceRelations(Integer addressId, Integer newAddressId) {
        InvoiceHome invoiceHome = (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
        //address
        try {
            Collection collection = invoiceHome.findByAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Invoice invoice = (Invoice) iterator.next();
                invoice.setAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }

        //sent address
        try {
            Collection collection = invoiceHome.findBySentAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Invoice invoice = (Invoice) iterator.next();
                invoice.setSentAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyInvoiceContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        InvoiceHome invoiceHome = (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
        //contact person
        try {
            Collection collection = invoiceHome.findByContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Invoice invoice = (Invoice) iterator.next();
                invoice.setAddressId(newAddressId);
                invoice.setContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
        //sent contact person
        try {
            Collection collection = invoiceHome.findBySentContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Invoice invoice = (Invoice) iterator.next();
                invoice.setSentAddressId(newAddressId);
                invoice.setSentContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyOfficeRelations(Integer addressId, Integer newAddressId) {
        OfficeHome officeHome = (OfficeHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_OFFICE);
        try {
            Collection collection = officeHome.findByOrganizationId(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Office office = (Office) iterator.next();
                office.setOrganizationId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyProductContractRelations(Integer addressId, Integer newAddressId) {
        ProductContractHome productContractHome = (ProductContractHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PRODUCTCONTRACT);
        //address
        try {
            Collection collection = productContractHome.findByAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                ProductContract productContract = (ProductContract) iterator.next();
                productContract.setAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }

        //sent address
        try {
            Collection collection = productContractHome.findBySentAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                ProductContract productContract = (ProductContract) iterator.next();
                productContract.setSentAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyProductContractContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        ProductContractHome productContractHome = (ProductContractHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PRODUCTCONTRACT);
        //contact person
        try {
            Collection collection = productContractHome.findByContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                ProductContract productContract = (ProductContract) iterator.next();
                productContract.setAddressId(newAddressId);
                productContract.setContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
        //sent contact person
        try {
            Collection collection = productContractHome.findBySentContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                ProductContract productContract = (ProductContract) iterator.next();
                productContract.setSentAddressId(newAddressId);
                productContract.setSentContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyProjectRelationsCustomer(Integer customerId, Integer newCustomerId) {
        ProjectHome projectHome = (ProjectHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT);
        try {
            Collection collection = projectHome.findByCustomer(customerId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Project project = (Project) iterator.next();
                project.setCustomerId(newCustomerId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyProjectContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        ProjectHome projectHome = (ProjectHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT);
        try {
            Collection collection = projectHome.findByContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Project project = (Project) iterator.next();
                project.setCustomerId(newAddressId);
                project.setContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyProjectTimeRelations(Integer addressId, Integer newAddressId) {
        ProjectTimeHome projectTimeHome = (ProjectTimeHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_TIME);
        try {
            Collection collection = projectTimeHome.findByAssigneeId(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                ProjectTime projectTime = (ProjectTime) iterator.next();
                projectTime.setAssigneeId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copySaleRelations(Integer addressId, Integer newAddressId) {
        SaleHome saleHome = (SaleHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALE);
        try {
            Collection collection = saleHome.findBySentAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Sale sale = (Sale) iterator.next();
                sale.setSentAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copySaleRelationsCustomer(Integer customerId, Integer newCustomerId) {
        SaleHome saleHome = (SaleHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALE);
        try {
            Collection collection = saleHome.findByCustomer(customerId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Sale sale = (Sale) iterator.next();
                sale.setCustomerId(newCustomerId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copySaleContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        SaleHome saleHome = (SaleHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALE);

        //contact person
        try {
            Collection collection = saleHome.findByContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Sale sale = (Sale) iterator.next();
                sale.setCustomerId(newAddressId);
                sale.setContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }

        //sent contact person
        try {
            Collection collection = saleHome.findBySentContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Sale sale = (Sale) iterator.next();
                sale.setSentAddressId(newAddressId);
                sale.setSentContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copySalePositionRelationsCustomer(Integer customerId, Integer newCustomerId) {
        SalePositionHome salePositionHome = (SalePositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);
        try {
            Collection collection = salePositionHome.findByCustomer(customerId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                SalePosition salePosition = (SalePosition) iterator.next();
                salePosition.setCustomerId(newCustomerId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copySalePositionContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        SalePositionHome salePositionHome = (SalePositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);
        try {
            Collection collection = salePositionHome.findByContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                SalePosition salePosition = (SalePosition) iterator.next();
                salePosition.setCustomerId(newAddressId);
                salePosition.setContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copySalesProcessRelations(Integer addressId, Integer newAddressId) {
        SalesProcessHome salesProcessHome = (SalesProcessHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALESPROCESS);
        try {
            Collection collection = salesProcessHome.findByAddressId(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                SalesProcess salesProcess = (SalesProcess) iterator.next();
                salesProcess.setAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copySentLogContactRelations(Integer addressId, Integer newAddressId) {
        SentLogContactHome sentLogContactHome = (SentLogContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_SENTLOGCONTACT);
        try {
            Collection collection = sentLogContactHome.findByAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                SentLogContact sentLogContact = (SentLogContact) iterator.next();
                sentLogContact.setAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copySentLogContactContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        SentLogContactHome sentLogContactHome = (SentLogContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_SENTLOGCONTACT);
        try {
            Collection collection = sentLogContactHome.findByContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                SentLogContact sentLogContact = (SentLogContact) iterator.next();
                sentLogContact.setAddressId(newAddressId);
                sentLogContact.setContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copySequenceRuleRelations(Integer addressId, Integer newAddressId) {
        SequenceRuleHome sequenceRuleHome = (SequenceRuleHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_SEQUENCERULE);
        try {
            Collection collection = sequenceRuleHome.findByDebitorId(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                SequenceRule sequenceRule = (SequenceRule) iterator.next();
                sequenceRule.setDebitorId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copySupportCaseRelations(Integer addressId, Integer newAddressId) {
        SupportCaseHome supportCaseHome = (SupportCaseHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CASE);
        try {
            Collection collection = supportCaseHome.findByAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                SupportCase supportCase = (SupportCase) iterator.next();
                supportCase.setAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copySupportCaseContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        SupportCaseHome supportCaseHome = (SupportCaseHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CASE);
        try {
            Collection collection = supportCaseHome.findByContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                SupportCase supportCase = (SupportCase) iterator.next();
                supportCase.setAddressId(newAddressId);
                supportCase.setContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyTaskRelations(Integer addressId, Integer newAddressId) {
        TaskHome taskHome = (TaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_TASK);
        try {
            Collection collection = taskHome.findByAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Task task = (Task) iterator.next();
                task.setAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyTaskContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        TaskHome taskHome = (TaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_TASK);
        try {
            Collection collection = taskHome.findByContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Task task = (Task) iterator.next();
                task.setAddressId(newAddressId);
                task.setContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void redefineMainDuplicateAddress(Integer duplicateGroupId) {
        DuplicateAddressHome duplicateAddressHome = (DuplicateAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DUPLICATEADDRESS);
        try {
            Collection collection = duplicateAddressHome.findByDuplicateGroupIdOrderPositionIndex(duplicateGroupId);
            boolean existMain = false;
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                DuplicateAddress duplicateAddress = (DuplicateAddress) iterator.next();
                if (existMain) {
                    duplicateAddress.setIsMain(false);
                } else {
                    duplicateAddress.setIsMain(true);
                    existMain = true;
                }
            }
        } catch (FinderException ignore) {
        }
    }

    private void deleteDuplicateAddress(Integer addressId) throws RemoveException {
        DuplicateAddressHome duplicateAddressHome = (DuplicateAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DUPLICATEADDRESS);
        try {
            Collection collection = duplicateAddressHome.findByAddressId(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                DuplicateAddress duplicateAddress = (DuplicateAddress) iterator.next();
                duplicateAddress.remove();
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyImportRecordRelations(Integer addressId, Integer newAddressId) {
        ImportRecordHome importRecordHome = (ImportRecordHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTRECORD);
        try {
            Collection collection = importRecordHome.findByOrganizationId(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                ImportRecord importRecord = (ImportRecord) iterator.next();
                importRecord.setOrganizationId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyAdditionalAddressRelations(Integer addressId, Integer newAddressId) throws RemoveException {
        AdditionalAddressHome additionalAddressHome = (AdditionalAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDITIONALADDRESS);

        //verify if new address has main relation
        AdditionalAddress mainAdditionalAddress = findMainAdditionalAddress(addressId);
        AdditionalAddress newMainAdditionalAddress = findMainAdditionalAddress(newAddressId);

        if (newMainAdditionalAddress != null && mainAdditionalAddress != null) {
            //copy relations of the main and remove this
            copyAllAdditionalAddressRelationsAndRemove(newMainAdditionalAddress, mainAdditionalAddress.getAdditionalAddressId());
        }

        //copy all address relations
        try {
            Collection collection = additionalAddressHome.findByAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                AdditionalAddress additionalAddress = (AdditionalAddress) iterator.next();
                additionalAddress.setAddressId(newAddressId);
                additionalAddress.setIsDefault(false);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyAllAdditionalAddressRelationsAndRemove(AdditionalAddress additionalAddress, Integer newAdditionalAddressId) throws RemoveException {
        Integer additionalAddressId = additionalAddress.getAdditionalAddressId();

        copySaleRelationsAdditionalAddress(additionalAddressId, newAdditionalAddressId);
        copyProductContractRelationsAdditionalAddress(additionalAddressId, newAdditionalAddressId);
        copyInvoiceRelationsAdditionalAddress(additionalAddressId, newAdditionalAddressId);
        copyCustomerRelationsAdditionalAddress(additionalAddressId, newAdditionalAddressId);
        copyContactPersonRelationsAdditionalAddress(additionalAddressId, newAdditionalAddressId);
        copyCommunicationRelationsAdditionalAddress(additionalAddressId, newAdditionalAddressId);

        additionalAddress.remove();
    }

    private void copySaleRelationsAdditionalAddress(Integer additionalAddressId, Integer newAdditionalAddressId) {
        SaleHome saleHome = (SaleHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALE);
        try {
            Collection collection = saleHome.findByAdditionalAddressId(additionalAddressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Sale sale = (Sale) iterator.next();
                sale.setAdditionalAddressId(newAdditionalAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyProductContractRelationsAdditionalAddress(Integer additionalAddressId, Integer newAdditionalAddressId) {
        ProductContractHome productContractHome = (ProductContractHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PRODUCTCONTRACT);
        try {
            Collection collection = productContractHome.findByAdditionalAddressId(additionalAddressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                ProductContract productContract = (ProductContract) iterator.next();
                productContract.setAdditionalAddressId(newAdditionalAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyInvoiceRelationsAdditionalAddress(Integer additionalAddressId, Integer newAdditionalAddressId) {
        InvoiceHome invoiceHome = (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
        try {
            Collection collection = invoiceHome.findByAdditionalAddressId(additionalAddressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Invoice invoice = (Invoice) iterator.next();
                invoice.setAdditionalAddressId(newAdditionalAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyCustomerRelationsAdditionalAddress(Integer additionalAddressId, Integer newAdditionalAddressId) {
        CustomerHome customerHome = (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);
        try {
            Collection collection = customerHome.findByAdditionalAddressId(additionalAddressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Customer customer = (Customer) iterator.next();
                customer.setAdditionalAddressId(newAdditionalAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyContactPersonRelationsAdditionalAddress(Integer additionalAddressId, Integer newAdditionalAddressId) {
        ContactPersonHome contactPersonHome = (ContactPersonHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTPERSON);
        try {
            Collection collection = contactPersonHome.findByAdditionalAddressId(additionalAddressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                ContactPerson contactPerson = (ContactPerson) iterator.next();
                contactPerson.setAdditionalAddressId(newAdditionalAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyCommunicationRelationsAdditionalAddress(Integer additionalAddressId, Integer newAdditionalAddressId) {
        ContactHome contactHome = (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
        try {
            Collection collection = contactHome.findByAdditionalAddressId(additionalAddressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Contact contact = (Contact) iterator.next();
                contact.setAdditionalAddressId(newAdditionalAddressId);
            }
        } catch (FinderException ignore) {
        }
    }


    private void deleteAdditionalAddress(Integer addressId) throws RemoveException {
        AdditionalAddressHome additionalAddressHome = (AdditionalAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDITIONALADDRESS);
        try {
            Collection collection = additionalAddressHome.findByAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                AdditionalAddress additionalAddress = (AdditionalAddress) iterator.next();
                additionalAddress.remove();
            }
        } catch (FinderException ignore) {
        }
    }

    private void deleteCategoryFieldValueAddress(Integer addressId, Integer companyId) throws RemoveException {
        CategoryFieldValueHome fieldValueHome = (CategoryFieldValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYFIELDVALUE);
        try {
            Collection collection = fieldValueHome.findByAddressId(addressId, companyId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                CategoryFieldValue categoryFieldValue = (CategoryFieldValue) iterator.next();
                categoryFieldValue.remove();
            }
        } catch (FinderException ignore) {
        }
    }

    private void deleteCategoryFieldValueCustomer(Integer customerId, Integer companyId) throws RemoveException {
        CategoryFieldValueHome fieldValueHome = (CategoryFieldValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYFIELDVALUE);
        try {
            Collection collection = fieldValueHome.findByCustomerId(customerId, companyId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                CategoryFieldValue categoryFieldValue = (CategoryFieldValue) iterator.next();
                categoryFieldValue.remove();
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyCategoryFieldValueContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        CategoryFieldValueHome fieldValueHome = (CategoryFieldValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYFIELDVALUE);
        try {
            Collection collection = fieldValueHome.findByAddressIdAndContactPersonId(contactPerson.getAddressId(), contactPerson.getContactPersonId(), contactPerson.getCompanyId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                CategoryFieldValue categoryFieldValue = (CategoryFieldValue) iterator.next();
                categoryFieldValue.setAddressId(newAddressId);
                categoryFieldValue.setContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void deleteCategoryFieldValueContactPerson(Integer addressId, Integer contactPersonId, Integer companyId) throws RemoveException {
        CategoryFieldValueHome fieldValueHome = (CategoryFieldValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYFIELDVALUE);
        try {
            Collection collection = fieldValueHome.findByAddressIdAndContactPersonId(addressId, contactPersonId, companyId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                CategoryFieldValue categoryFieldValue = (CategoryFieldValue) iterator.next();
                categoryFieldValue.remove();
            }
        } catch (FinderException ignore) {
        }
    }

    private List<ContactPerson> findAddressAsContactPerson(Integer addressId, Integer companyId) {
        List<ContactPerson> contactPersonList = new ArrayList<ContactPerson>();
        ContactPersonHome contactPersonHome = (ContactPersonHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTPERSON);
        try {
            Collection collection = contactPersonHome.findByAddressAsContactPerson(addressId, companyId);
            contactPersonList.addAll(collection);
        } catch (FinderException ignore) {
        }
        return contactPersonList;
    }

    private void createNewContactPersonAndMerge(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) throws CreateException {
        ContactPersonHome contactPersonHome = (ContactPersonHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTPERSON);

        ContactPerson newContactPerson = findContactPerson(newAddressId, newContactPersonId);
        if (newContactPerson == null) {
            ContactPersonDTO contactPersonDTO = new ContactPersonDTO();
            contactPersonDTO.put("addressId", newAddressId);
            contactPersonDTO.put("contactPersonId", newContactPersonId);
            contactPersonDTO.put("companyId", contactPerson.getCompanyId());

            newContactPerson = contactPersonHome.create(contactPersonDTO);
        }

        if (newContactPerson.getActive() == null) {
            newContactPerson.setActive(contactPerson.getActive());
        }
        if (newContactPerson.getAddAddressLine() == null) {
            newContactPerson.setAddAddressLine(contactPerson.getAddAddressLine());
        }
        if (newContactPerson.getAdditionalAddressId() == null) {
            newContactPerson.setAdditionalAddressId(contactPerson.getAdditionalAddressId());
        }
        if (newContactPerson.getDepartmentId() == null) {
            newContactPerson.setDepartmentId(contactPerson.getDepartmentId());
        }
        if (newContactPerson.getFunction() == null) {
            newContactPerson.setFunction(contactPerson.getFunction());
        }
        if (newContactPerson.getPersonTypeId() == null) {
            newContactPerson.setPersonTypeId(contactPerson.getPersonTypeId());
        }
        if (newContactPerson.getRecordDate() == null) {
            newContactPerson.setRecordDate(contactPerson.getRecordDate());
        }
        if (newContactPerson.getRecordUserId() == null) {
            newContactPerson.setRecordUserId(contactPerson.getRecordUserId());
        }
        if (newContactPerson.getVersion() == null) {
            newContactPerson.setVersion(contactPerson.getVersion());
        }
    }

    private Customer createNewCustomerAndMerge(Customer customer, Address address2) throws CreateException {
        CustomerHome customerHome = (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);

        Customer newCustomer = findCustomer(address2.getAddressId());
        if (newCustomer == null) {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.put("customerId", address2.getAddressId());
            customerDTO.put("companyId", address2.getCompanyId());

            newCustomer = customerHome.create(customerDTO);

            //assign code if no exist
            if (!CodeUtil.isCustomer(address2.getCode())) {
                byte newCode = (byte) (address2.getCode() + CodeUtil.customer);
                address2.setCode(newCode);
            }
        }

        if (newCustomer.getBranchId() == null) {
            newCustomer.setBranchId(customer.getBranchId());
        }
        if (newCustomer.getNumber() == null) {
            newCustomer.setNumber(customer.getNumber());
        }
        if (newCustomer.getCustomerTypeId() == null) {
            newCustomer.setCustomerTypeId(customer.getCustomerTypeId());
        }
        if (newCustomer.getDefaultDiscount() == null) {
            newCustomer.setDefaultDiscount(customer.getDefaultDiscount());
        }
        if (newCustomer.getEmployeeId() == null) {
            newCustomer.setEmployeeId(customer.getEmployeeId());
        }
        if (newCustomer.getExpectedTurnOver() == null) {
            newCustomer.setExpectedTurnOver(customer.getExpectedTurnOver());
        }
        if (newCustomer.getNumberOfEmployees() == null) {
            newCustomer.setNumberOfEmployees(customer.getNumberOfEmployees());
        }
        if (newCustomer.getPartnerId() == null) {
            newCustomer.setPartnerId(customer.getPartnerId());
        }
        if (newCustomer.getPayConditionId() == null) {
            newCustomer.setPayConditionId(customer.getPayConditionId());
        }
        if (newCustomer.getPayMoralityId() == null) {
            newCustomer.setPayMoralityId(customer.getPayMoralityId());
        }
        if (newCustomer.getPriorityId() == null) {
            newCustomer.setPriorityId(customer.getPriorityId());
        }
        if (newCustomer.getSourceId() == null) {
            newCustomer.setSourceId(customer.getSourceId());
        }

        //validate invoice address
        if (newCustomer.getAdditionalAddressId() == null && newCustomer.getInvoiceAddressId() == null) {
            newCustomer.setAdditionalAddressId(customer.getAdditionalAddressId());
            newCustomer.setInvoiceAddressId(customer.getInvoiceAddressId());
            newCustomer.setInvoiceContactPersonId(customer.getInvoiceContactPersonId());
        }

        return newCustomer;
    }

    private Supplier createNewSupplierAndMerge(Supplier supplier, Address address2) throws CreateException {
        SupplierHome supplierHome = (SupplierHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_SUPPLIER);
        Supplier newSupplier = findSupplier(address2.getAddressId());
        if (newSupplier == null) {
            SupplierDTO supplierDTO = new SupplierDTO();
            supplierDTO.put("supplierId", address2.getAddressId());
            supplierDTO.put("companyId", address2.getCompanyId());

            newSupplier = supplierHome.create(supplierDTO);

            //assign code if no exist
            if (!CodeUtil.isSupplier(address2.getCode())) {
                byte newCode = (byte) (address2.getCode() + CodeUtil.supplier);
                address2.setCode(newCode);
            }
        }

        if (newSupplier.getBranchId() == null) {
            newSupplier.setBranchId(supplier.getBranchId());
        }
        if (newSupplier.getCategoryId() == null) {
            newSupplier.setCategoryId(supplier.getCategoryId());
        }
        if (newSupplier.getCustomerNumber() == null) {
            newSupplier.setCustomerNumber(supplier.getCustomerNumber());
        }
        if (newSupplier.getPriorityId() == null) {
            newSupplier.setPriorityId(supplier.getPriorityId());
        }
        if (newSupplier.getSupplierTypeId() == null) {
            newSupplier.setSupplierTypeId(supplier.getSupplierTypeId());
        }

        return newSupplier;
    }

    private Employee createNewEmployeeAndMerge(Employee employee, Address address2) throws CreateException {
        EmployeeHome employeeHome = (EmployeeHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_EMPLOYEE);
        Employee newEmployee = findEmployee(address2.getAddressId());
        if (newEmployee == null) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.put("employeeId", address2.getAddressId());
            employeeDTO.put("company", address2.getCompanyId());

            newEmployee = employeeHome.create(employeeDTO);

            //assign code if no exist
            if (!CodeUtil.isEmployee(address2.getCode())) {
                Byte newCode = new Byte(CodeUtil.addCode(address2.getCode(), CodeUtil.employee));
                address2.setCode(newCode);
            }
        }

        if (newEmployee.getCostCenterId() == null) {
            newEmployee.setCostCenterId(employee.getCostCenterId());
        }
        if (newEmployee.getCostHour() == null) {
            newEmployee.setCostHour(employee.getCostHour());
        }
        if (newEmployee.getCostPosition() == null) {
            newEmployee.setCostPosition(employee.getCostPosition());
        }
        if (newEmployee.getDateEnd() == null) {
            newEmployee.setDateEnd(employee.getDateEnd());
        }
        if (newEmployee.getDepartmentId() == null) {
            newEmployee.setDepartmentId(employee.getDepartmentId());
        }
        if (newEmployee.getFunction() == null) {
            newEmployee.setFunction(employee.getFunction());
        }
        if (newEmployee.getHealthFundId() == null) {
            newEmployee.setHealthFundId(employee.getHealthFundId());
        }
        if (newEmployee.getHireDate() == null) {
            newEmployee.setHireDate(employee.getHireDate());
        }
        if (newEmployee.getHourlyRate() == null) {
            newEmployee.setHourlyRate(employee.getHourlyRate());
        }
        if (newEmployee.getInitials() == null) {
            newEmployee.setInitials(employee.getInitials());
        }
        if (newEmployee.getOfficeId() == null) {
            newEmployee.setOfficeId(employee.getOfficeId());
        }
        if (newEmployee.getSocialSecurityNumber() == null) {
            newEmployee.setSocialSecurityNumber(employee.getSocialSecurityNumber());
        }
        return newEmployee;
    }


    private void copyRecentRelationsComposedKey(Integer addressId, Integer newAddressId) throws CreateException, RemoveException {
        RecentHome recentHome = (RecentHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_RECENT);
        Collection collection = null;
        try {
            collection = recentHome.findByAddressId(addressId);
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            Recent recent = (Recent) iterator.next();

            Recent newRecent = findRecent(newAddressId, recent.getUserId());
            if (newRecent == null) {
                //create if no exist
                RecentDTO recentDTO = new RecentDTO();
                recentDTO.put("addressId", newAddressId);
                recentDTO.put("userId", recent.getUserId());
                recentDTO.put("companyId", recent.getCompanyId());
                recentDTO.put("recentId", recent.getRecentId());

                newRecent = recentHome.create(recentDTO);
            }

            //finally remove
            recent.remove();
        }
    }

    private void copyFavoriteRelationsComposedKey(Integer addressId, Integer newAddressId) throws CreateException, RemoveException {
        FavoriteHome favoriteHome = (FavoriteHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_FAVORITE);
        Collection collection = null;
        try {
            collection = favoriteHome.findByAddressId(addressId);
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            Favorite favorite = (Favorite) iterator.next();

            Favorite newFavorite = findFavorite(favorite.getUserId(), newAddressId);
            if (newFavorite == null) {
                FavoriteDTO favoriteDTO = new FavoriteDTO();
                favoriteDTO.put("userId", favorite.getUserId());
                favoriteDTO.put("addressId", newAddressId);
                favoriteDTO.put("companyId", favorite.getCompanyId());

                newFavorite = favoriteHome.create(favoriteDTO);
            }

            //finally remove
            favorite.remove();
        }
    }

    private void copyRecordDuplicateRelationsComposedKey(Integer addressId, Integer newAddressId) throws CreateException, RemoveException {
        RecordDuplicateHome recordDuplicateHome = (RecordDuplicateHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_RECORDDUPLICATE);
        Collection collection = null;
        try {
            collection = recordDuplicateHome.findByAddressId(addressId);
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            RecordDuplicate recordDuplicate = (RecordDuplicate) iterator.next();

            RecordDuplicate newRecordDuplicate = findRecordDuplicate(recordDuplicate.getImportRecordId(), newAddressId);
            if (newRecordDuplicate == null) {
                RecordDuplicateDTO recordDuplicateDTO = new RecordDuplicateDTO();
                recordDuplicateDTO.put("importRecordId", recordDuplicate.getImportRecordId());
                recordDuplicateDTO.put("addressId", newAddressId);
                recordDuplicateDTO.put("companyId", recordDuplicate.getCompanyId());

                newRecordDuplicate = recordDuplicateHome.create(recordDuplicateDTO);
            }
            //finally remove
            recordDuplicate.remove();
        }
    }

    private void copyProjectAssigneeRelationsComposedKey(Integer addressId, Integer newAddressId) throws CreateException, RemoveException {
        ProjectAssigneeHome projectAssigneeHome = (ProjectAssigneeHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_ASSIGNEE);
        Collection collection = null;
        try {
            collection = projectAssigneeHome.findByAddressId(addressId);
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            ProjectAssignee projectAssignee = (ProjectAssignee) iterator.next();

            ProjectAssignee newProjectAssignee = findProjectAssignee(projectAssignee.getProjectId(), newAddressId);
            if (newProjectAssignee == null) {
                ProjectAssigneeDTO projectAssigneeDTO = new ProjectAssigneeDTO();
                projectAssigneeDTO.put("projectId", projectAssignee.getProjectId());
                projectAssigneeDTO.put("addressId", newAddressId);
                projectAssigneeDTO.put("companyId", projectAssignee.getCompanyId());
                projectAssigneeDTO.put("permission", projectAssignee.getPermission());
                projectAssigneeDTO.put("version", projectAssignee.getVersion());

                newProjectAssignee = projectAssigneeHome.create(projectAssigneeDTO);
            }
            //finally remove
            projectAssignee.remove();
        }
    }

    private void copyProductSupplierRelationsComposedKeySupplier(Integer supplierId, Integer newSupplierId) throws CreateException, RemoveException {
        ProductSupplierHome productSupplierHome = (ProductSupplierHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCTSUPPLIER);
        Collection collection = null;
        try {
            collection = productSupplierHome.findBySupplier(supplierId);
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            ProductSupplier productSupplier = (ProductSupplier) iterator.next();

            ProductSupplier newProductSupplier = findProductSupplier(newSupplierId, productSupplier.getProductId());
            if (newProductSupplier == null) {
                newProductSupplier = createNewProductSupplierComposedKey(productSupplier, newSupplierId, productSupplier.getContactPersonId());
            }
            //finally remove
            productSupplier.remove();
        }
    }

    private void copyProductSupplierContactPersonRelationsComposedKeySupplier(ContactPerson contactPerson, Integer newSupplierId, Integer newContactPersonId) throws CreateException, RemoveException {
        ProductSupplierHome productSupplierHome = (ProductSupplierHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCTSUPPLIER);
        Collection collection = null;
        try {
            collection = productSupplierHome.findByContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            ProductSupplier productSupplier = (ProductSupplier) iterator.next();

            ProductSupplier newProductSupplier = findProductSupplier(newSupplierId, productSupplier.getProductId());
            if (newProductSupplier == null) {
                newProductSupplier = createNewProductSupplierComposedKey(productSupplier, newSupplierId, newContactPersonId);
            }
            //finally remove
            productSupplier.remove();
        }
    }

    private ProductSupplier createNewProductSupplierComposedKey(ProductSupplier productSupplier, Integer newSupplierId, Integer newContactPersonId) throws CreateException{
        ProductSupplierHome productSupplierHome = (ProductSupplierHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCTSUPPLIER);
        ProductSupplierDTO productSupplierDTO = new ProductSupplierDTO();
        productSupplierDTO.put("productId", productSupplier.getProductId());
        productSupplierDTO.put("supplierId", newSupplierId);
        productSupplierDTO.put("companyId", productSupplier.getCompanyId());
        productSupplierDTO.put("contactPersonId", newContactPersonId);
        productSupplierDTO.put("active", productSupplier.getActive());
        productSupplierDTO.put("discount", productSupplier.getDiscount());
        productSupplierDTO.put("partNumber", productSupplier.getPartNumber());
        productSupplierDTO.put("price", productSupplier.getPrice());
        productSupplierDTO.put("unitId", productSupplier.getUnitId());

        return productSupplierHome.create(productSupplierDTO);
    }

    private void copyUserAddressAccessRelationsComposedKey(Integer addressId, Integer newAddressId) throws CreateException, RemoveException {
        UserAddressAccessHome userAddressAccessHome = (UserAddressAccessHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_USERADDRESSACCESS);
        Collection collection = null;
        try {
            collection = userAddressAccessHome.findUserAddressAccessByAddress(addressId);
        } catch (FinderException ignore) {
            collection = new ArrayList();
        }

        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            UserAddressAccess userAddressAccess = (UserAddressAccess) iterator.next();

            UserAddressAccess newUserAddressAccess = findUserAddressAccess(newAddressId, userAddressAccess.getUserGroupId());
            if (newUserAddressAccess == null) {
                UserAddressAccessDTO dto = new UserAddressAccessDTO();
                dto.put("addressId", newAddressId);
                dto.put("userGroupId", userAddressAccess.getUserGroupId());
                dto.put("companyId", userAddressAccess.getCompanyId());

                newUserAddressAccess = userAddressAccessHome.create(dto);
            }
            //finally remove
            userAddressAccess.remove();
        }
    }

    private void deleteUserAddressAccess(Integer addressId) throws RemoveException {
        UserAddressAccessHome userAddressAccessHome = (UserAddressAccessHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_USERADDRESSACCESS);
        try {
            Collection collection = userAddressAccessHome.findUserAddressAccessByAddress(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                UserAddressAccess userAddressAccess = (UserAddressAccess) iterator.next();
                userAddressAccess.remove();
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyAddressGroupRelations(Integer addressId, Integer newAddressId) {
        AddressGroupHome addressGroupHome = (AddressGroupHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_ADDRESSGROUP);
        try {
            Collection collection = addressGroupHome.findByAddressId(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                AddressGroup addressGroup = (AddressGroup) iterator.next();
                addressGroup.setAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyAddressGroupContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        AddressGroupHome addressGroupHome = (AddressGroupHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_ADDRESSGROUP);
        try {
            Collection collection = addressGroupHome.findByContactPerson(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                AddressGroup addressGroup = (AddressGroup) iterator.next();
                addressGroup.setAddressId(newAddressId);
                addressGroup.setContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void deleteAddressGroups(Integer addressId) throws RemoveException {
        AddressGroupHome addressGroupHome = (AddressGroupHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_ADDRESSGROUP);
        try {
            Collection collection = addressGroupHome.findByAddressId(addressId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                AddressGroup addressGroup = (AddressGroup) iterator.next();
                addressGroup.remove();
            }
        } catch (FinderException ignore) {
        }
    }

    private void deleteAddressGroupContactPerson(Integer addressId, Integer contactPersonId) throws RemoveException {
        AddressGroupHome addressGroupHome = (AddressGroupHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_ADDRESSGROUP);
        try {
            Collection collection = addressGroupHome.findByContactPerson(addressId, contactPersonId);
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                AddressGroup addressGroup = (AddressGroup) iterator.next();
                addressGroup.remove();
            }
        } catch (FinderException ignore) {
        }
    }


    private void copyTelecomsRelations(Integer addressId, Integer newAddressId) {
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        try {
            Collection telecoms = telecomHome.findAddressTelecoms(addressId);
            for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
                Telecom telecom = (Telecom) iterator.next();

                //firts verify for predetermined
                if (telecom.getPredetermined() != null && telecom.getPredetermined() && existPredeterminedTelecom(newAddressId, telecom.getTelecomTypeId())) {
                    telecom.setPredetermined(false);
                }

                telecom.setAddressId(newAddressId);
            }
        } catch (FinderException ignore) {
        }
    }

    private void copyTelecomsContactPersonRelations(ContactPerson contactPerson, Integer newAddressId, Integer newContactPersonId) {
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        try {
            Collection telecoms = telecomHome.findContactPersonTelecoms(contactPerson.getAddressId(), contactPerson.getContactPersonId());
            for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
                Telecom telecom = (Telecom) iterator.next();

                //firts verify for predetermined
                if (telecom.getPredetermined() != null && telecom.getPredetermined() && existPredeterminedTelecom(newAddressId, newContactPersonId, telecom.getTelecomTypeId())) {
                    telecom.setPredetermined(false);
                }

                telecom.setAddressId(newAddressId);
                telecom.setContactPersonId(newContactPersonId);
            }
        } catch (FinderException ignore) {
        }

    }

    private void deleteTelecoms(Integer addressId) throws RemoveException {
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        try {
            Collection telecoms = telecomHome.findAddressTelecoms(addressId);
            for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
                Telecom telecom = (Telecom) iterator.next();
                telecom.remove();
            }
        } catch (FinderException ignore) {
        }
    }

    private void deleteTelecomsContactPerson(Integer addressId, Integer contactPersonId) throws RemoveException {
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        try {
            Collection telecoms = telecomHome.findContactPersonTelecoms(addressId, contactPersonId);
            for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
                Telecom telecom = (Telecom) iterator.next();
                telecom.remove();
            }
        } catch (FinderException ignore) {
        }
    }

    private void mergeAddressTelecomFields(Address address1, Address address2, List<ContactMergeField> contactMergeFieldList) {
        List<DinamicColumn> addressTelecomColumns = getAddressTelecomColumns(contactMergeFieldList);

        Map addressTelecomMap = buildTelecomValuesMap(addressTelecomColumns, address1.getAddressId());
        if (!addressTelecomMap.isEmpty()) {
            mergeTelecomValues(addressTelecomMap, address2.getAddressId(), address2.getCompanyId());
        }
    }

    private Map buildTelecomValuesMap(List<DinamicColumn> dinamicColumns, Integer sourceAddressId) {

        Map telecoms = new HashMap();

        for (DinamicColumn column : dinamicColumns) {
            String telecomTypeId = column.getIdentifierField();
            TelecomType telecomType = findTelecomType(new Integer(telecomTypeId));

            if (null == telecomType) {
                continue;
            }

            List<String> telecomValuesList = readTelecomsValue(sourceAddressId, null, telecomType);
            for (int i = 0; i < telecomValuesList.size(); i++) {
                String value = telecomValuesList.get(i);

                boolean isPredeterminated = false;
                TelecomWrapperDTO wrappedDto = (TelecomWrapperDTO) telecoms.get(telecomTypeId);
                if (null == wrappedDto) {
                    wrappedDto = new TelecomWrapperDTO();
                    wrappedDto.setTelecomTypeId(telecomTypeId);
                    wrappedDto.setTelecomTypeName(telecomType.getTelecomTypeName());
                    wrappedDto.setTelecomTypeType(telecomType.getType());
                    wrappedDto.setTelecomTypePosition(telecomType.getPosition());
                    isPredeterminated = true;
                }

                if (null != value && !"".equals(value.trim())) {
                    TelecomDTO telecomDTO = new TelecomDTO();
                    telecomDTO.setData(value);
                    if (isPredeterminated) {
                        telecomDTO.setPredetermined(true);
                    }
                    wrappedDto.addTelecomDTO(telecomDTO);
                }

                if (wrappedDto.getTelecoms().size() > 0) {
                    telecoms.put(telecomTypeId, wrappedDto);
                }
            }
        }
        return telecoms;
    }

    private List<String> readTelecomsValue(Integer addressId, Integer contactPersonId, TelecomType telecomType) {
        List<String> telecomValuesList = new ArrayList<String>();
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

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
                if (telecom.getData() != null) {
                    telecomValuesList.add(telecom.getData());
                }
            }
        }
        return telecomValuesList;
    }

    private void mergeTelecomValues(Map telecomMap, Integer addressId, Integer companyId) {
        // creating telecoms
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        if (telecomMap != null && companyId != null && telecomMap.size() > 0) {
            for (Iterator iterator = telecomMap.values().iterator(); iterator.hasNext();) {
                TelecomWrapperDTO telecomWrapperDTO = (TelecomWrapperDTO) iterator.next();

                Integer telecomTypeId = new Integer(telecomWrapperDTO.getTelecomTypeId());
                boolean isPredetermined = false;
                if (!existPredeterminedTelecom(addressId, telecomTypeId)) {
                    isPredetermined = true;
                }

                for (Iterator iterator1 = telecomWrapperDTO.getTelecoms().iterator(); iterator1.hasNext();) {
                    TelecomDTO telecomDTO = (TelecomDTO) iterator1.next();
                    try {
                        Telecom telecom = telecomHome.create(addressId, null, telecomDTO.getData(),
                                telecomDTO.getDescription(), isPredetermined, telecomTypeId, companyId);

                        isPredetermined = false;
                    } catch (CreateException e) {
                        log.error("Unexpected error creating telecoms in merge", e);
                    }
                }
            }
        }
    }

    private boolean existPredeterminedTelecom(Integer addressId, Integer telecomTypeId) {
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        Telecom telecom = null;
        try {
            telecom = telecomHome.findAddressDefaultTelecomsByTypeId(addressId, telecomTypeId);
        } catch (FinderException e) {
            log.debug("Not found predetermined telecom.. " + e);
        }
        return telecom != null;
    }

    private boolean existPredeterminedTelecom(Integer addressId, Integer contactPersonId, Integer telecomTypeId) {
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        Telecom telecom = null;
        try {
            telecom = telecomHome.findContactPersonDefaultTelecomsByTypeId(addressId, contactPersonId, telecomTypeId);
        } catch (FinderException e) {
            log.debug("Not found predetermined telecom.. " + e);
        }
        return telecom != null;
    }

    private void mergeCategoriesFields(Address address1, Address address2, List<ContactMergeField> contactMergeFieldList) {
        List<DinamicColumn> addressCategoryColumns = getAddresCategoryColumns(contactMergeFieldList);
        List<DinamicColumn> customerCategoryColumns = getCustomerCategoryColumns(contactMergeFieldList);

        Map addressCategoryMap = buildCategoryValuesMap(addressCategoryColumns, address1.getAddressId());
        if (!addressCategoryMap.isEmpty()) {
            updateAddressCategories(addressCategoryMap, address2.getAddressId(), address2.getCompanyId());
        }

        Map customerCategoryMap = buildCategoryValuesMap(customerCategoryColumns, address1.getAddressId());
        if (!customerCategoryMap.isEmpty()) {
            updateCustomerCategories(customerCategoryMap, address2.getAddressId(), address2.getCompanyId());
        }
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

    private void updateCustomerCategories(Map categoryMap, Integer id, Integer companyId) {
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

    private Map buildCategoryValuesMap(List<DinamicColumn> dinamicColumns, Integer sourceAddressId) {

        Map result = new HashMap();

        List<String> pageCategoryIds = new ArrayList<String>();

        for (DinamicColumn column : dinamicColumns) {

            Category category = getCategory(new Integer(column.getIdentifierField()));
            //skip the category when could not found it
            if (null == category) {
                continue;
            }

            List<Object> valueList = readColumnCategoryValues(column, sourceAddressId, category.getCategoryId(), category.getCompanyId());
            if (!valueList.isEmpty()) {
                Object value = valueList.get(0);
                if (value != null) {
                    pageCategoryIds.add(category.getCategoryId().toString());
                    String categoryNameKey = "categoryName_" + category.getCategoryId();
                    String categoryTypeKey = "categoryType_" + category.getCategoryId();
                    String categorySelectedKey = "selected_" + category.getCategoryId();

                    result.put(categoryNameKey, category.getCategoryName());
                    result.put(categoryTypeKey, category.getCategoryType().toString());

                    if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() == category.getCategoryType()) {

                        String valuesSelected = "";
                        for (int i = 0; i < valueList.size(); i++) {
                            Object compoundValue = valueList.get(i);
                            if (!"".equals(valuesSelected)) {
                                valuesSelected += ",";
                            }
                            valuesSelected += compoundValue.toString();
                        }
                        result.put(categorySelectedKey, valuesSelected);
                    } else {

                        result.put(category.getCategoryId().toString(), value);
                    }
                }
            }
        }

        if (!result.isEmpty()) {
            result.put("pageCategoryIds", pageCategoryIds);
        }

        return result;
    }

    private List<Object> readColumnCategoryValues(DinamicColumn dinamicColumn, Integer addressId, Integer categoryId, Integer companyId) {
        List<Object> valueList = new ArrayList<Object>();
        if (Column.ColumnType.ADDRESS.equals(dinamicColumn.getType())) {
            valueList = readAddressCategoryValue(addressId, categoryId, companyId);
        } else if (Column.ColumnType.CUSTOMER.equals(dinamicColumn.getType())) {
            valueList = readCustomerCategoryValue(addressId, categoryId, companyId);
        }
        return valueList;
    }

    private List<Object> readAddressCategoryValue(Integer addressId, Integer categoryId, Integer companyId) {
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

    private List<Object> readCustomerCategoryValue(Integer customerId, Integer categoryId, Integer companyId) {
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

    private List<Object> readCategoryFieldValue(List<CategoryFieldValue> categoryFieldValues) {
        List<Object> result = new ArrayList<Object>();

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
                        value = categoryValue.getCategoryValueId();
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
                    FreeText freeText = findFreeText(fieldValue.getAttachId());
                    if (freeText!= null) {
                        ArrayByteWrapper arrayByteWrapper = new ArrayByteWrapper();
                        arrayByteWrapper.setFileData(freeText.getValue());
                        arrayByteWrapper.setFileName(fieldValue.getFilename());

                        value = arrayByteWrapper;
                    }
                }

                if (value != null) {
                    result.add(value);
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

    private FreeText findFreeText(Integer freeTextId) {
        FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
        if (freeTextId != null) {
            try {
                return freeTextHome.findByPrimaryKey(freeTextId);
            } catch (FinderException e) {
                log.debug("The Freetext not found with id: " + freeTextId + " " + e);
            }
        }
        return null;
    }

    private boolean isAddressMergeField(String ejbFieldName, List<ContactMergeField> contactMergeFieldList) {
        return isStaticColumnMergeField(ejbFieldName, contactMergeFieldList, Column.ColumnType.ADDRESS);
    }

    private boolean isCustomerMergeField(String ejbFieldName, List<ContactMergeField> contactMergeFieldList) {
        return isStaticColumnMergeField(ejbFieldName, contactMergeFieldList, Column.ColumnType.CUSTOMER);
    }

    private boolean isStaticColumnMergeField(String ejbFieldName, List<ContactMergeField> contactMergeFieldList, Column.ColumnType columnType) {
        for (ContactMergeField contactMergeField : contactMergeFieldList) {
            if (contactMergeField.isMergeField()) {
                Column column = contactMergeField.getColumn();
                if (column instanceof StaticColumn && columnType.equals(column.getType())) {
                    StaticColumn staticColumn = (StaticColumn) column;
                    if (ejbFieldName.equals(staticColumn.getEjbFieldName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean existMergeCustomerFields(List<ContactMergeField> contactMergeFieldList) {
        for (ContactMergeField contactMergeField : contactMergeFieldList) {
            if (contactMergeField.isMergeField()) {
                Column column = contactMergeField.getColumn();
                if (Column.ColumnType.CUSTOMER.equals(column.getType())) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<DinamicColumn> getAddresCategoryColumns(List<ContactMergeField> contactMergeFieldList) {
        List<DinamicColumn> result = new ArrayList<DinamicColumn>();
        for (ContactMergeField contactMergeField : contactMergeFieldList) {
            if (contactMergeField.isMergeField()) {
                Column column = contactMergeField.getColumn();
                if (column instanceof CategoryDinamicColumn && column.getType().equals(Column.ColumnType.ADDRESS)) {
                    result.add((DinamicColumn) column);
                }
            }
        }
        return result;
    }

    private List<DinamicColumn> getCustomerCategoryColumns(List<ContactMergeField> contactMergeFieldList) {
        List<DinamicColumn> result = new ArrayList<DinamicColumn>();
        for (ContactMergeField contactMergeField : contactMergeFieldList) {
            if (contactMergeField.isMergeField()) {
                Column column = contactMergeField.getColumn();
                if (column instanceof CategoryDinamicColumn && column.getType().equals(Column.ColumnType.CUSTOMER)) {
                    result.add((DinamicColumn) column);
                }
            }
        }
        return result;
    }

    private List<DinamicColumn> getAddressTelecomColumns(List<ContactMergeField> contactMergeFieldList) {
        List<DinamicColumn> result = new ArrayList<DinamicColumn>();
        for (ContactMergeField contactMergeField : contactMergeFieldList) {
            if (contactMergeField.isMergeField()) {
                Column column = contactMergeField.getColumn();
                if (column instanceof TelecomDinamicColumn && column.getType().equals(Column.ColumnType.ADDRESS)) {
                    result.add((DinamicColumn) column);
                }
            }
        }
        return result;
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
        if (customerId != null) {
            try {
                return home.findByPrimaryKey(customerId);
            } catch (FinderException e) {
                log.debug("The Customer not found with id: " + customerId + " " + e);
            }
        }
        return null;
    }

    private Supplier findSupplier(Integer supplierId) {
        SupplierHome supplierHome = (SupplierHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_SUPPLIER);
        if (supplierId != null) {
            try {
                return supplierHome.findByPrimaryKey(supplierId);
            } catch (FinderException e) {
                log.debug("The supplier not found with id: " + supplierId + " " + e);
            }
        }
        return null;
    }

    private Employee findEmployee(Integer employeeId) {
        EmployeeHome employeeHome = (EmployeeHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_EMPLOYEE);
        if (employeeId != null) {
            try {
                return employeeHome.findByPrimaryKey(employeeId);
            } catch (FinderException e) {
                log.debug("The employee not found with id: " + employeeId + " " + e);
            }
        }
        return null;
    }

    private Category getCategory(Integer categoryId) {
        CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);
        try {
            return categoryHome.findByPrimaryKey(categoryId);
        } catch (FinderException e) {
            return null;
        }
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

    private Recent findRecent(Integer addressId, Integer userId) {
        RecentHome recentHome = (RecentHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_RECENT);
        if (addressId != null && userId != null ) {
            try {
                RecentPK recentPK = new RecentPK(addressId, userId);
                return recentHome.findByPrimaryKey(recentPK);
            } catch (FinderException e) {
                log.debug("Recent not found with id: " + addressId + " " + userId + " " + e);
            }
        }
        return null;
    }

    private Favorite findFavorite(Integer userId, Integer addressId) {
        FavoriteHome favoriteHome = (FavoriteHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_FAVORITE);
        if (userId != null && addressId != null ) {
            try {
                FavoritePK favoritePK = new FavoritePK(userId, addressId);
                return favoriteHome.findByPrimaryKey(favoritePK);
            } catch (FinderException e) {
                log.debug("Favorite not found with id: " + addressId + " " + userId + " " + e);
            }
        }
        return null;
    }

    private RecordDuplicate findRecordDuplicate(Integer importRecordId, Integer addressId) {
        RecordDuplicateHome recordDuplicateHome = (RecordDuplicateHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_RECORDDUPLICATE);
        if (importRecordId != null && addressId != null ) {
            try {
                RecordDuplicatePK recordDuplicatePK = new RecordDuplicatePK(importRecordId, addressId);
                return recordDuplicateHome.findByPrimaryKey(recordDuplicatePK);
            } catch (FinderException e) {
                log.debug("record duplicate not found with id: " + importRecordId + " " + addressId + " " + e);
            }
        }
        return null;
    }

    private UserAddressAccess findUserAddressAccess(Integer addressId, Integer userGroupId) {
        UserAddressAccessHome userAddressAccessHome = (UserAddressAccessHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_USERADDRESSACCESS);
        if (addressId != null && userGroupId != null) {
            try {
                UserAddressAccessPK userAddressAccessPK = new UserAddressAccessPK(addressId, userGroupId);
                return userAddressAccessHome.findByPrimaryKey(userAddressAccessPK);
            } catch (FinderException e) {
                log.debug("user address access not found with id: " + addressId + " " + userGroupId + " " + e);
            }
        }
        return null;
    }

    private ProjectAssignee findProjectAssignee(Integer projectId, Integer addressId) {
        ProjectAssigneeHome projectAssigneeHome = (ProjectAssigneeHome) EJBFactory.i.getEJBLocalHome(ProjectConstants.JNDI_PROJECT_ASSIGNEE);
        if (addressId != null && projectId != null) {
            try {
                ProjectAssigneePK projectAssigneePK = new ProjectAssigneePK(projectId, addressId);
                return projectAssigneeHome.findByPrimaryKey(projectAssigneePK);
            } catch (FinderException e) {
                log.debug("project assignee not found with id: " + projectId + " " + addressId + " " + e);
            }
        }
        return null;
    }

    private ProductSupplier findProductSupplier(Integer supplierId, Integer productId) {
        ProductSupplierHome productSupplierHome = (ProductSupplierHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCTSUPPLIER);
        if (supplierId != null && productId != null) {
            try {
                ProductSupplierPK productSupplierPK = new ProductSupplierPK(supplierId, productId);
                return productSupplierHome.findByPrimaryKey(productSupplierPK);
            } catch (FinderException e) {
                log.debug("product supplier not found with id: " + supplierId + " " + productId + " " + e);
            }
        }
        return null;
    }

    private DuplicateGroup findDuplicateGroup(Integer duplicateGroupId) {
        DuplicateGroupHome duplicateGroupHome = (DuplicateGroupHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DUPLICATEGROUP);
        if (duplicateGroupId != null) {
            try {
                return duplicateGroupHome.findByPrimaryKey(duplicateGroupId);
            } catch (FinderException e) {
                log.debug("The duplicate group not found with id: " + duplicateGroupId + " " + e);
            }
        }
        return null;
    }

    private DuplicateAddress findDuplicateAddress(Integer duplicateGroupId, Integer addressId) {
        DuplicateAddressHome duplicateAddressHome = (DuplicateAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DUPLICATEADDRESS);
        if (duplicateGroupId != null && addressId != null) {
            try {
                DuplicateAddressPK duplicateAddressPK = new DuplicateAddressPK(duplicateGroupId, addressId);
                return duplicateAddressHome.findByPrimaryKey(duplicateAddressPK);
            } catch (FinderException e) {
                log.debug("duplicate address not found with id: " + duplicateGroupId + " " + addressId + " " + e);
            }
        }
        return null;
    }

    private User findUserByAddressId(Integer addressId, Integer companyId) {
        User user = null;
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        try {
            user = userHome.findByAddressId(companyId, addressId);
        } catch (FinderException e) {
            log.debug("Error in find user with address..." + addressId + "-" + companyId + " " + e);
        }
        return user;
    }

    private AdditionalAddress findMainAdditionalAddress(Integer addressId) {
        AdditionalAddress mainAdditionalAddress = null;

        AdditionalAddressHome additionalAddressHome = (AdditionalAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDITIONALADDRESS);
        if (addressId != null) {
            try {
                Collection additionalAddresses = additionalAddressHome.findByAdditionalAddressType(addressId, ContactConstants.AdditionalAddressType.MAIN.getConstant());
                if (!additionalAddresses.isEmpty()) {
                    mainAdditionalAddress = (AdditionalAddress) additionalAddresses.iterator().next();
                }
            } catch (FinderException e) {
                log.debug("error in found main additional address.. " + e);
            }
        }
        return mainAdditionalAddress;
    }

}
