package com.piramide.elwis.domain.admin;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public abstract class PasswordChangeBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setPasswordChangeId(PKGenerator.i.nextKey(AdminConstants.TABLE_PASSWORDCHANGE));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public PasswordChangeBean() {
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

    public abstract Long getChangeTime();

    public abstract void setChangeTime(Long changeTime);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract Integer getPasswordChangeId();

    public abstract void setPasswordChangeId(Integer passwordChangeId);

    public abstract Integer getTotalUser();

    public abstract void setTotalUser(Integer totalUser);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getRolePasswordChanges();

    public abstract void setRolePasswordChanges(Collection rolePasswordChanges);

    public abstract Collection getUserPasswordChanges();

    public abstract void setUserPasswordChanges(Collection userPasswordChanges);

    public abstract Long getUpdateDateTime();

    public abstract void setUpdateDateTime(Long updateDateTime);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);
}
