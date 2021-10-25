package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;
import java.math.BigDecimal;
import java.util.Collection;

/**
 * Represents Customer EntityBean
 *
 * @author Fernando Montaño
 * @version $Id: CustomerBean.java 12586 2016-09-07 20:54:40Z miguel $
 */
public abstract class CustomerBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        DTOFactory.i.copyFromDTO(dto, this, false);
        setVersion(new Integer(1));
        setNumber(dto.get("customerId").toString());//TODO: this is done up to the generation of numbers will be automatically
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

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

    public abstract Integer getCustomerId();

    public abstract void setCustomerId(Integer customerId);

    public abstract Integer getBranchId();

    public abstract void setBranchId(Integer branchId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getNumber();

    public abstract void setNumber(String number);

    public abstract Integer getCustomerTypeId();

    public abstract void setCustomerTypeId(Integer customerTypeId);

    public abstract java.math.BigDecimal getExpectedTurnOver();

    public abstract void setExpectedTurnOver(java.math.BigDecimal expectedTurnOver);

    public abstract Integer getPartnerId();

    public abstract void setPartnerId(Integer partnerId);

    public abstract Integer getPayConditionId();

    public abstract void setPayConditionId(Integer payConditionId);

    public abstract Integer getPayMoralityId();

    public abstract void setPayMoralityId(Integer payMoralityId);

    public abstract Integer getPriorityId();

    public abstract void setPriorityId(Integer priorityId);

    public abstract Integer getSourceId();

    public abstract void setSourceId(Integer sourceId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getNumberOfEmployees();

    public abstract void setNumberOfEmployees(Integer numberOfEmployees);

    public abstract Collection getCustomerCategoryList();

    public abstract void setCustomerCategoryList(Collection customerCategoryList);

    public abstract Address getPartner();

    public abstract void setPartner(Address partner);

    public abstract Address getAddress();

    public abstract void setAddress(Address address);

    public abstract Integer getEmployeeId();

    public abstract void setEmployeeId(Integer employeeId);

    public abstract String ejbSelectMaxCustomerNumber(Integer companyId) throws FinderException;

    public String ejbHomeSelectMaxCustomerNumber(Integer companyId) throws FinderException {
        return ejbSelectMaxCustomerNumber(companyId);
    }

    public abstract BigDecimal getDefaultDiscount();

    public abstract void setDefaultDiscount(BigDecimal defaultDiscount);

    public abstract Integer getInvoiceAddressId();

    public abstract void setInvoiceAddressId(Integer invoiceAddressId);

    public abstract Integer getInvoiceContactPersonId();

    public abstract void setInvoiceContactPersonId(Integer invoiceContactPersonId);

    public abstract Integer getAdditionalAddressId();

    public abstract void setAdditionalAddressId(Integer additionalAddressId);

    public abstract Integer getInvoiceShipping();

    public abstract void setInvoiceShipping(Integer invoiceShipping);
}
