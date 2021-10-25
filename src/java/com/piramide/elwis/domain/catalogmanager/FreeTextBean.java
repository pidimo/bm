package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;

import javax.ejb.*;

/**
 * This Class represents the FreeText Entity Bean
 *
 * @author Ivan
 * @version $Id: FreeTextBean.java 2204 2004-08-11 20:26:32Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public abstract class FreeTextBean implements EntityBean {

    public Integer ejbCreate(byte[] text, Integer companyId, Integer type) throws CreateException {
        setFreeTextId(PKGenerator.i.nextKey(CatalogConstants.TABLE_FREETEXT));
        setValue(text);
        setCompanyId(companyId);
        setVersion(new Integer(1));
        setType(type);
        return null;
    }

    public void ejbPostCreate(byte[] text, Integer companyId, Integer type) {
    }

    public FreeTextBean() {
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

    public abstract void setType(Integer type);

    public abstract byte[] getValue();

    public abstract void setValue(byte[] value);
}
