package com.piramide.elwis.domain.common;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.PKGenerator;

import javax.ejb.*;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Mar 16, 2005
 * Time: 10:20:46 AM
 * To change this template use File | Settings | File Templates.
 */

public abstract class FileFreeTextBean implements EntityBean {
    public Integer ejbCreate(byte[] value, Integer companyId, Integer type) throws CreateException {
        setFileId(PKGenerator.i.nextKey(Constants.TABLE_FILEFREETEXT));
        setValue(value);
        setCompanyId(companyId);
        setVersion(new Integer(1));
        setType(type);
        return null;
    }

    public void ejbPostCreate(byte[] value, Integer companyId, Integer type) {
    }


    public FileFreeTextBean() {
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

    public abstract Integer getFileId();

    public abstract void setFileId(Integer fileId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract byte[] getValue();

    public abstract void setValue(byte[] value);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getType();

    public abstract void setType(Integer type);
}
