package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;

/**
 * Supplier entity bean class
 *
 * @author Fernando Montaño
 * @version $Id: SupplierBean.java 9121 2009-04-17 00:28:59Z fernando $
 */
public abstract class SupplierBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        DTOFactory.i.copyFromDTO(dto, this, false);
        setVersion(new Integer(1));
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

    public abstract Integer getBranchId();

    public abstract void setBranchId(Integer branchId);

    public abstract Integer getCategoryId();

    public abstract void setCategoryId(Integer categoryId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getCustomerNumber();

    public abstract void setCustomerNumber(String customerNumber);

    public abstract Integer getPriorityId();

    public abstract void setPriorityId(Integer priorityId);

    public abstract Integer getSupplierId();

    public abstract void setSupplierId(Integer supplierId);

    public abstract Integer getSupplierTypeId();

    public abstract void setSupplierTypeId(Integer supplierTypeId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Address getAddress();

    public abstract void setAddress(Address address);
}
