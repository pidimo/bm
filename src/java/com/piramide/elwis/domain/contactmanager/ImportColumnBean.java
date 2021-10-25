package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Class models a import profile column
 * <p/>
 * This class modeling a selected column in the data import funcionality.
 *
 * @author Ivan Alban
 * @version 4.2.1
 */
public abstract class ImportColumnBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setImportColumnId(PKGenerator.i.nextKey(ContactConstants.TABLE_IMPORTCOLUMN));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public ImportColumnBean() {
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

    public abstract Integer getColumnId();

    public abstract void setColumnId(Integer columnid);

    public abstract Integer getColumnValue();

    public abstract void setColumnValue(Integer columnValue);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getGroupId();

    public abstract void setGroupId(Integer groupId);

    public abstract Integer getImportColumnId();

    public abstract void setImportColumnId(Integer importColumnId);

    public abstract Integer getProfileId();

    public abstract void setProfileId(Integer profileId);

    public abstract Integer getUiPosition();

    public abstract void setUiPosition(Integer uiPosition);

    public abstract String getColumnName();

    public abstract void setColumnName(String columnName);
}
