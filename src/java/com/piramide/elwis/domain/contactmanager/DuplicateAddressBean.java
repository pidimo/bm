package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public abstract class DuplicateAddressBean implements EntityBean {
    EntityContext entityContext;

    public DuplicateAddressPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public DuplicateAddressBean() {
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getDuplicateGroupId();

    public abstract void setDuplicateGroupId(Integer duplicateGroupId);

    public abstract Boolean getIsMain();

    public abstract void setIsMain(Boolean isMain);

    public abstract Integer getPositionIndex();

    public abstract void setPositionIndex(Integer positionIndex);
}
