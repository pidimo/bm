package com.piramide.elwis.domain.admin;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public abstract class DynamicSearchFieldBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setDynamicSearchFieldId(PKGenerator.i.nextKey(AdminConstants.TABLE_DYNAMICSEARCHFIELD));

        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public DynamicSearchFieldBean() {
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

    public abstract String getAlias();

    public abstract void setAlias(String alias);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getDynamicSearchId();

    public abstract void setDynamicSearchId(Integer dynamicSearchId);

    public abstract Integer getDynamicSearchFieldId();

    public abstract void setDynamicSearchFieldId(Integer dynamicSearchFieldId);

    public abstract Integer getPosition();

    public abstract void setPosition(Integer position);
}
