package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

/**
 * Supplier entity local interface.
 *
 * @author Fernando Montaño
 * @version $Id: Supplier.java 9121 2009-04-17 00:28:59Z fernando $
 */
public interface Supplier extends EJBLocalObject {
    Integer getBranchId();

    void setBranchId(Integer branchId);

    Integer getCategoryId();

    void setCategoryId(Integer categoryId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getCustomerNumber();

    void setCustomerNumber(String customerNumber);

    Integer getPriorityId();

    void setPriorityId(Integer priorityId);

    Integer getSupplierId();

    void setSupplierId(Integer supplierId);

    Integer getSupplierTypeId();

    void setSupplierTypeId(Integer supplierTypeId);

    Integer getVersion();

    void setVersion(Integer version);

    Address getAddress();

    void setAddress(Address address);
}
