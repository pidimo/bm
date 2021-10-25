package com.piramide.elwis.domain.admin;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Accesss righ entity bean
 *
 * @author Fernando Montaño
 * @version $Id: AccessRightsBean.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 11:40:40 AM Fernando Montaño Exp $
 */


public abstract class AccessRightsBean implements EntityBean {
    EntityContext entityContext;

    public AccessRightsPK ejbCreate(Integer functionId, Integer roleId, Integer moduleId, Integer companyId) throws CreateException {
        this.setFunctionId(functionId);
        this.setRoleId(roleId);
        setModuleId(moduleId);
        setCompanyId(companyId);
        setActive(new Boolean(true));
        return null;
    }

    public void ejbPostCreate(Integer functionId, Integer roleId, Integer moduleId, Integer companyId) throws CreateException {
    }

    public AccessRightsPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public AccessRightsBean() {
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

    public abstract Integer getFunctionId();

    public abstract void setFunctionId(Integer functionId);

    public abstract Integer getRoleId();

    public abstract void setRoleId(Integer roleId);

    public abstract Byte getPermission();

    public abstract void setPermission(Byte permission);

    public abstract SystemFunction getSystemFunction();

    public abstract void setSystemFunction(SystemFunction systemFunction);

    public abstract Boolean getActive();

    public abstract void setActive(Boolean active);

    public abstract Integer getModuleId();

    public abstract void setModuleId(Integer moduleId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);
}
