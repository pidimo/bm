/**
 * @author Yumi
 * @version $Id: ContactAttachBean.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v 2.0 13-may-2004 18:25:50 Yumi Exp $
 */
package com.piramide.elwis.domain.contactmanager;

import javax.ejb.*;

public abstract class ContactAttachBean implements EntityBean {
    EntityContext entityContext;

    public ContactAttachPK ejbCreate(Integer contactId, Integer freeTextId, Integer companyId, String fileName, String contentType, Integer size) throws CreateException {
        setContactId(contactId);
        setFreeTextId(freeTextId);
        setFileName(fileName);
        setContentType(contentType);
        setFileSize(size);
        setCompanyId(companyId);
        return null;
    }

    public void ejbPostCreate(Integer contactId, Integer freeTextId, Integer companyId, String fileName, String contentType, Integer size) throws CreateException {
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

    public abstract Integer getContactId();

    public abstract void setContactId(Integer contactId);

    public abstract Integer getFreeTextId();

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract String getFileName();

    public abstract void setFileName(String fileName);

    public abstract Integer getFileSize();

    public abstract void setFileSize(Integer fileSize);

    public abstract String getContentType();

    public abstract void setContentType(String contentType);
}
