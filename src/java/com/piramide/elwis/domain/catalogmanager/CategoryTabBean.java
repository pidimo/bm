/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class CategoryTabBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setCategoryTabId(PKGenerator.i.nextKey(CatalogConstants.TABLE_CATEGORYTAB));
        setVersion(0);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public CategoryTabBean() {
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

    public abstract Integer getCategoryTabId();

    public abstract void setCategoryTabId(Integer categoryTabId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getLabel();

    public abstract void setLabel(String label);

    public abstract Integer getSequence();

    public abstract void setSequence(Integer sequence);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract String getTable();

    public abstract void setTable(String table);

    public abstract java.util.Collection getCategoryGroups();

    public abstract void setCategoryGroups(java.util.Collection categoryGroups);
}
