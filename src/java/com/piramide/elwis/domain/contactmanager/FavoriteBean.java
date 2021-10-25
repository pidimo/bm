package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;

/**
 * Favorite Entity Bean
 *
 * @author Fernando Montaño
 * @version $Id: FavoriteBean.java 9121 2009-04-17 00:28:59Z fernando $
 */

public abstract class FavoriteBean implements EntityBean {
    EntityContext entityContext;

    public FavoritePK ejbCreate(ComponentDTO dto) throws CreateException {
        DTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public FavoriteBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
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

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Address getAddress();

    public abstract void setAddress(Address address);
}
