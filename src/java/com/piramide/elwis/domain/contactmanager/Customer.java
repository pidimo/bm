package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;
import java.util.Collection;

/**
 * Represents Customer entity local home.
 *
 * @author Fernando Montaño
 * @version $Id: Customer.java 12586 2016-09-07 20:54:40Z miguel $
 */
public interface Customer extends EJBLocalObject {
    Integer getCustomerId();

    void setCustomerId(Integer customerId);

    Integer getBranchId();

    void setBranchId(Integer branchId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getNumber();

    void setNumber(String number);

    Integer getCustomerTypeId();

    void setCustomerTypeId(Integer customerTypeId);

    java.math.BigDecimal getExpectedTurnOver();

    void setExpectedTurnOver(java.math.BigDecimal expectedTurnOver);

    Integer getPartnerId();

    void setPartnerId(Integer partnerId);

    Integer getPayConditionId();

    void setPayConditionId(Integer payConditionId);

    Integer getPayMoralityId();

    void setPayMoralityId(Integer payMoralityId);

    Integer getPriorityId();

    void setPriorityId(Integer priorityId);

    Integer getSourceId();

    void setSourceId(Integer sourceId);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getNumberOfEmployees();

    void setNumberOfEmployees(Integer numberOfEmployees);

    Collection getCustomerCategoryList();

    void setCustomerCategoryList(Collection customerCategoryList);

    Address getPartner();

    void setPartner(Address partner);

    Address getAddress();

    void setAddress(Address address);

    Integer getEmployeeId();

    void setEmployeeId(Integer employeeId);

    BigDecimal getDefaultDiscount();

    void setDefaultDiscount(BigDecimal defaultDiscount);

    Integer getInvoiceAddressId();

    void setInvoiceAddressId(Integer invoiceAddressId);

    Integer getInvoiceContactPersonId();

    void setInvoiceContactPersonId(Integer invoiceContactPersonId);

    Integer getAdditionalAddressId();

    void setAdditionalAddressId(Integer additionalAddressId);

    Integer getInvoiceShipping();

    void setInvoiceShipping(Integer invoiceShipping);
    
    String getReferenceId();

    void setReferenceId(String referenceId);
}
