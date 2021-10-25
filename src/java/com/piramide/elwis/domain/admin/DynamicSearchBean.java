package com.piramide.elwis.domain.admin;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public abstract class DynamicSearchBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setDynamicSearchId(PKGenerator.i.nextKey(AdminConstants.TABLE_DYNAMICSEARCH));

        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public DynamicSearchBean() {
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

    public abstract Integer getDynamicSearchId();

    public abstract void setDynamicSearchId(Integer dynamicSearchId);

    public abstract String getModule();

    public abstract void setModule(String module);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Collection getDynamicSearchFields();

    public abstract void setDynamicSearchFields(Collection dynamicSearchFields);
}
