package com.piramide.elwis.domain.schedulermanager;

import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SchedulerConstants;

import javax.ejb.*;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 8, 2005
 * Time: 4:07:19 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class SchedulerFreeTextBean implements EntityBean {

    public Integer ejbCreate(byte[] value, Integer companyId, Integer type) throws CreateException {
        setFreeTextId(PKGenerator.i.nextKey(SchedulerConstants.TABLE_SCHEDULERFREETEXT));
        setValue(value);
        setCompanyId(companyId);
        setVersion(new Integer(1));
        setType(type);
        return null;
    }

    public void ejbPostCreate(byte[] value, Integer companyId, Integer type) {
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

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getFreeTextId();

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract Integer getType();

    public abstract void setType(Integer freeTextType);

    public abstract byte[] getValue();

    public abstract void setValue(byte[] freeTextValue);

}
