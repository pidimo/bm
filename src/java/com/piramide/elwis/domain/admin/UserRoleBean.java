/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.admin;

import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;

public abstract class UserRoleBean implements EntityBean {

    EntityContext entityContext;

    public UserRolePK ejbCreate(ComponentDTO dto) throws CreateException {
        DTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public UserRoleBean() {
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Integer getRoleId();

    public abstract void setRoleId(Integer roleId);

    public abstract Role getRole();

    public abstract void setRole(Role role);
}
