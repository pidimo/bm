/**
 * @author ivan
 *
 * Jatun S.R.L. 
 */
package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.*;

public abstract class EmailSourceBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(Integer mailId, Integer companyId, byte[] source) throws CreateException {
        this.setMailId(mailId);
        this.setCompanyId(companyId);
        this.setSource(source);
        this.setFileSize(source.length);
        return null;
    }

    public void ejbPostCreate(Integer mailId, Integer companyId, byte[] source) throws CreateException {

    }

    public EmailSourceBean() {
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

    public abstract Integer getFileSize();

    public abstract void setFileSize(Integer fileSize);

    public abstract Integer getMailId();

    public abstract void setMailId(Integer mailId);

    public abstract byte[] getSource();

    public abstract void setSource(byte[] source);
}
