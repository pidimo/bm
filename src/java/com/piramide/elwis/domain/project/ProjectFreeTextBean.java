/**
 *
 * @author Fernando Montao
 * @version $Id: ${NAME}.java 2009-02-20 11:58:10 $
 */
package com.piramide.elwis.domain.project;

import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.ProjectConstants;

import javax.ejb.*;

public abstract class ProjectFreeTextBean implements EntityBean {
    public ProjectFreeTextBean() {
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getFreeTextId();

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract Integer getType();

    public abstract void setType(Integer type);

    public abstract byte[] getValue();

    public abstract void setValue(byte[] value);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public Integer ejbCreate(byte[] value, Integer companyId, Integer type) throws CreateException {
        setFreeTextId(PKGenerator.i.nextKey(ProjectConstants.TABLE_FREETEXT));
        setValue(value);
        setCompanyId(companyId);
        setVersion(1);
        setType(type);
        return null;
    }

    public void ejbPostCreate(byte[] value, Integer companyId, Integer type) throws CreateException {

    }
}
