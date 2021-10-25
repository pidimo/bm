package com.piramide.elwis.domain.admin;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * Role Entity Bean
 *
 * @author Fernando Montaño
 * @version $Id: RoleBean.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 09:47:26 AM Fernando Montaño Exp $
 */


public abstract class RoleBean implements EntityBean {

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setRoleId(PKGenerator.i.nextKey(AdminConstants.TABLE_ROLE));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public RoleBean() {
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

    public abstract Integer getRoleId();

    public abstract void setRoleId(Integer roleId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Boolean getIsDefault();

    public abstract void setIsDefault(Boolean isDefault);

    public abstract Integer getDescriptionId();

    public abstract void setDescriptionId(Integer descriptionId);

    public abstract String getRoleName();

    public abstract void setRoleName(String roleName);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract AdminFreeText getDescriptionText();

    public abstract void setDescriptionText(AdminFreeText descriptionText);

    public abstract Collection getAccessRights();

    public abstract void setAccessRights(Collection accessRights);

    public void setDescriptionText(EJBLocalObject descriptionText) {
        setDescriptionText((AdminFreeText) descriptionText);
    }
}
