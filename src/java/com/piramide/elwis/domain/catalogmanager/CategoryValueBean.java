package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * This Class represents the CategoryValue Entity Bean
 *
 * @author Ivan
 * @version $Id: CategoryValueBean.java 8261 2008-06-02 23:44:41Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public abstract class CategoryValueBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setCategoryValueId(PKGenerator.i.nextKey(CatalogConstants.TABLE_CATEGORYVALUE));
        setVersion(new Integer(1));
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

    public abstract Integer getCategoryValueId();

    public abstract void setCategoryValueId(Integer categoryValueId);

    public abstract String getCategoryValueName();

    public abstract void setCategoryValueName(String name);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Category getCategory();

    public abstract void setCategory(Category category);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getCategoryId();

    public abstract void setCategoryId(Integer categoryId);

    public abstract String getTableId();

    public abstract void setTableId(String tableId);

    public abstract java.util.Collection getCategoryRelations();

    public abstract void setCategoryRelations(java.util.Collection categoryRelations);
}
