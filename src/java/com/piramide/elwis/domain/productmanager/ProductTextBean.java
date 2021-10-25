/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.productmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class ProductTextBean implements EntityBean {
    EntityContext entityContext;

    public ProductTextPK ejbCreate(ComponentDTO componentDTO) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(componentDTO, this, false);
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO componentDTO) throws CreateException {
    }

    public ProductTextBean() {
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

    public abstract Integer getProductId();

    public abstract void setProductId(Integer productId);

    public abstract Integer getLanguageId();

    public abstract void setLanguageId(Integer languageId);

    public abstract Integer getFreetextId();

    public abstract void setFreetextId(Integer freetextId);

    public abstract Boolean getIsDefault();

    public abstract void setIsDefault(Boolean isDefault);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract ProductFreeText getProductFreeText();

    public abstract void setProductFreeText(ProductFreeText productFreeText);
}
