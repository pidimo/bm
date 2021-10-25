/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class CategoryRelationBean implements EntityBean {

    EntityContext entityContext;

    public CategoryRelationPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public CategoryRelationPK ejbCreate(Integer categoryId,
                                        Integer categoryValueId,
                                        Integer companyId) throws CreateException {
        setCategoryId(categoryId);
        setCategoryValueId(categoryValueId);
        setCompanyId(companyId);
        return null;
    }

    public void ejbPostCreate(Integer categoryId,
                              Integer categoryValueId,
                              Integer companyId) throws CreateException {

    }


    public CategoryRelationBean() {
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

    public abstract Integer getCategoryId();

    public abstract void setCategoryId(Integer categoryId);

    public abstract Integer getCategoryValueId();

    public abstract void setCategoryValueId(Integer categoryValueId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Category getCategory();

    public abstract void setCategory(Category category);

    public abstract CategoryValue getCategoryValue();

    public abstract void setCategoryValue(CategoryValue categoryValue);
}
