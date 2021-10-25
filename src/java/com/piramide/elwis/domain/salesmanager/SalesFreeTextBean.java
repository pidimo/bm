package com.piramide.elwis.domain.salesmanager;

import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SalesConstants;

import javax.ejb.*;

/**
 * SalesProcess  freetext
 *
 * @author Fernando Montaño
 * @version $Id: SalesFreeTextBean.java 9121 2009-04-17 00:28:59Z fernando $
 */


public abstract class SalesFreeTextBean implements EntityBean {
    public Integer ejbCreate(byte[] value, Integer companyId, Integer type) throws CreateException {
        setFreeTextId(PKGenerator.i.nextKey(SalesConstants.TABLE_FREETEXT));
        setValue(value);
        setCompanyId(companyId);
        setVersion(new Integer(1));
        setType(type);
        return null;
    }

    public void ejbPostCreate(byte[] value, Integer companyId, Integer type) {
    }

    public SalesFreeTextBean() {
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

    public abstract Integer getType();

    public abstract void setType(Integer type);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract byte[] getValue();

    public abstract void setValue(byte[] value);
}
