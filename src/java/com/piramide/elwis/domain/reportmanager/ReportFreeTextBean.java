package com.piramide.elwis.domain.reportmanager;

import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.ReportConstants;

import javax.ejb.*;

/**
 * Alfacentauro Team
 *
 * @author Alvaro
 * @version $Id: ReportFreeTextBean.java 7936 2007-10-27 16:08:39Z fernando $
 */

public abstract class ReportFreeTextBean implements EntityBean {

    public ReportFreeTextBean() {
    }

    public Integer ejbCreate(byte[] value, Integer companyId, Integer type) throws CreateException {
        setFreeTextId(PKGenerator.i.nextKey(ReportConstants.TABLE_REPORTFREETEXT));
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

    public abstract Integer getFreeTextId();

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getType();

    public abstract void setType(Integer type);

    public abstract byte[] getValue();

    public abstract void setValue(byte[] value);

}
