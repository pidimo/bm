package com.piramide.elwis.domain.supportmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 3:10:53 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class SupportCaseWorkLevelBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setWorkLevelId(PKGenerator.i.nextKey(SupportConstants.TABLE_SUPPORTCASE_WORKLEVEL));
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getWorkLevelId();

    public abstract void setWorkLevelId(Integer workLevelId);

    public abstract Integer getSequence();

    public abstract void setSequence(Integer sequence);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract String getWorkLevelName();

    public abstract void setWorkLevelName(String name);

    public abstract Integer getLangTextId();

    public abstract void setLangTextId(Integer langTextId);
}
