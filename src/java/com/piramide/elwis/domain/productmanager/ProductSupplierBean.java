/**
 * AlfaCentauro Team
 * @author Titus
 * @version ${NAME}.java, v 2.0 Aug 26, 2004 11:04:33 AM  
 */
package com.piramide.elwis.domain.productmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class ProductSupplierBean implements EntityBean {

    public ProductSupplierPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setVersion(new Integer(1));
        setActive(new Boolean(true));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public ProductSupplierBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
    }

    public void unsetEntityContext() throws EJBException {
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

    public abstract Boolean getActive();

    public abstract void setActive(Boolean active);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract java.math.BigDecimal getDiscount();

    public abstract void setDiscount(java.math.BigDecimal discount);

    public abstract String getPartNumber();

    public abstract void setPartNumber(String partNumber);

    public abstract java.math.BigDecimal getPrice();

    public abstract void setPrice(java.math.BigDecimal price);

    public abstract Integer getProductId();

    public abstract void setProductId(Integer productId);

    public abstract Integer getSupplierId();

    public abstract void setSupplierId(Integer supplierId);

    public abstract Integer getUnitId();

    public abstract void setUnitId(Integer unitId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Product getProduct();

    public abstract void setProduct(Product product);

}
