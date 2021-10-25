package com.piramide.elwis.domain.supportmanager;

import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SupportConstants;

import javax.ejb.*;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 3:28:54 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class SupportFreeTextBean implements EntityBean {

    public Integer ejbCreate(byte[] value, Integer companyId, Integer type) throws CreateException {
        setFreeTextId(PKGenerator.i.nextKey(SupportConstants.TABLE_SUPPORTFREETEXT));
        setFreeTextValue(value);
        setCompanyId(companyId);
        setVersion(new Integer(1));
        setFreeTextType(type);
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


    public abstract Integer getFreeTextId();

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getFreeTextType();

    public abstract void setFreeTextType(Integer type);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract byte[] getFreeTextValue();

    public abstract void setFreeTextValue(byte[] freeTextValue);

}
