package com.piramide.elwis.domain.common.session;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * UserSessionBean Entity Bean
 *
 * @author Fernando Montaño
 * @version 4.3
 */


public abstract class UserSessionBean implements EntityBean {
    EntityContext entityContext;

    public UserSessionPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public UserSessionBean() {
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

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract String getStatusName();

    public abstract void setStatusName(String statusName);

    public abstract String getModule();

    public abstract void setModule(String module);

    public abstract Collection getParams();

    public abstract void setParams(Collection params);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);
}
