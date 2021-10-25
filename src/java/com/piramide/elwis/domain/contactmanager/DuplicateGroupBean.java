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
public abstract class DuplicateGroupBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setDuplicateGroupId(PKGenerator.i.nextKey(ContactConstants.TABLE_DUPLICATEGROUP));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public DuplicateGroupBean() {
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

    public abstract Integer getDuplicateGroupId();

    public abstract void setDuplicateGroupId(Integer duplicateGroupId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getDuplicateAddress();

    public abstract void setDuplicateAddress(Collection duplicateAddress);
}
