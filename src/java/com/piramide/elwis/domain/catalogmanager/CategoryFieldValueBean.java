package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class CategoryFieldValueBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setFieldValueId(PKGenerator.i.nextKey(CatalogConstants.TABLE_CATEGORYFIELDVALUE));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public CategoryFieldValueBean() {
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

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract Integer getCategoryId();

    public abstract void setCategoryId(Integer categoryId);

    public abstract Integer getCategoryValueId();

    public abstract void setCategoryValueId(Integer categoryValueId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Integer getCustomerId();

    public abstract void setCustomerId(Integer customerId);

    public abstract Integer getDateValue();

    public abstract void setDateValue(Integer dateValue);

    public abstract java.math.BigDecimal getDecimalValue();

    public abstract void setDecimalValue(java.math.BigDecimal decimalValue);

    public abstract Integer getIntegerValue();

    public abstract void setIntegerValue(Integer integerValue);

    public abstract Integer getProductId();

    public abstract void setProductId(Integer productId);

    public abstract String getStringValue();

    public abstract void setStringValue(String stringValue);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getFieldValueId();

    public abstract void setFieldValueId(Integer fieldValueId);

    public abstract String getLinkValue();

    public abstract void setLinkValue(String linkValue);

    public abstract Integer getFreeTextId();

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract Integer getAttachId();

    public abstract void setAttachId(Integer attachId);

    public abstract String getFilename();

    public abstract void setFilename(String filename);

    public abstract Integer getProcessId();

    public abstract void setProcessId(Integer processId);

    public abstract Integer getSalePositionId();

    public abstract void setSalePositionId(Integer salePositionId);
}
