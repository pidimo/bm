package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public abstract class RecordColumnBean implements EntityBean {
    EntityContext entityContext;

    public RecordColumnPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public RecordColumnBean() {
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

    public abstract String getColumnValue();

    public abstract void setColumnValue(String columnValue);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getImportColumnId();

    public abstract void setImportColumnId(Integer importColumnId);

    public abstract Integer getImportRecordId();

    public abstract void setImportRecordId(Integer importRecordId);

    public abstract Integer getColumnIndex();

    public abstract void setColumnIndex(Integer columnIndex);
}
