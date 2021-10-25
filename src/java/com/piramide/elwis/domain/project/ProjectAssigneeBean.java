/**
 * Jatun S.R.L
 *
 * @author ivan
 */
package com.piramide.elwis.domain.project;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class ProjectAssigneeBean implements EntityBean {
    private EntityContext entityContext;

    public ProjectAssigneePK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public ProjectAssigneeBean() {
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

    public abstract Integer getProjectId();

    public abstract void setProjectId(Integer projectId);

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Byte getPermission();

    public abstract void setPermission(Byte permission);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Project getProject();

    public abstract void setProject(Project project);
}
