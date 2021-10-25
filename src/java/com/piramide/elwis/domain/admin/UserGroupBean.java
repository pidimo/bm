package com.piramide.elwis.domain.admin;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 22, 2005
 * Time: 3:42:28 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class UserGroupBean implements EntityBean {

    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        setUserGroupId(PKGenerator.i.nextKey(AdminConstants.TABLE_USERGROUP));
        setVersion(new Integer(1));
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getGroupName();

    public abstract void setGroupName(String groupName);

    public abstract Integer getUserGroupId();

    public abstract void setUserGroupId(Integer userGroupId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getUserOfGroup();

    public abstract void setUserOfGroup(Collection userOfGroup);

    public abstract Integer getGroupType();

    public abstract void setGroupType(Integer groupType);
}
