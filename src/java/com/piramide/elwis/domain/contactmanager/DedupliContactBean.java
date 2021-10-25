package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public abstract class DedupliContactBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setDedupliContactId(PKGenerator.i.nextKey(ContactConstants.TABLE_DEDUPLICONTACT));
        setVersion(new Integer(1));

        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public DedupliContactBean() {
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

    public abstract Integer getDedupliContactId();

    public abstract void setDedupliContactId(Integer dedupliContactId);

    public abstract Long getStartTime();

    public abstract void setStartTime(Long startTime);

    public abstract Integer getStatus();

    public abstract void setStatus(Integer status);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getDuplicateGroups();

    public abstract void setDuplicateGroups(Collection duplicateGroups);
}
