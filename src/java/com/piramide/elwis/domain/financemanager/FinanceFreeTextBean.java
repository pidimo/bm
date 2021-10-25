/**
 * Jatun S.R.L
 *
 * @author ivan
 */
package com.piramide.elwis.domain.financemanager;

import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.PKGenerator;

import javax.ejb.*;

public abstract class FinanceFreeTextBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(byte[] value, Integer companyId, Integer type) throws CreateException {
        setFreeTextId(PKGenerator.i.nextKey(FinanceConstants.TABLE_FREETEXT));
        setValue(value);
        setCompanyId(companyId);
        setVersion(new Integer(1));
        setType(type);
        return null;
    }

    public void ejbPostCreate(byte[] value, Integer companyId, Integer type) {
    }

    public FinanceFreeTextBean() {
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

    public abstract Integer getFreeTextId();

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getType();

    public abstract void setType(Integer type);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract byte[] getValue();

    public abstract void setValue(byte[] value);
}
