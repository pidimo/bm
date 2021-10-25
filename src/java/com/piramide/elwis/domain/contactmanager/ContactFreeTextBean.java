/**
 * @author Tayes
 * @version $Id: ContactFreeTextBean.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 16-05-2004 12:15:46 PM Tayes Exp $
 */
package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.PKGenerator;

import javax.ejb.*;
import java.io.IOException;
import java.io.InputStream;

public abstract class ContactFreeTextBean implements EntityBean {

    public Integer ejbCreate(byte[] text, Integer companyId, Integer type) throws CreateException {
        setFreeTextId(PKGenerator.i.nextKey(ContactConstants.TABLE_FREETEXT));
        setValue(text);
        setCompanyId(companyId);
        setVersion(new Integer(1));
        setType(type);
        return null;
    }

    public Integer ejbCreate(InputStream fileStream, Integer fileSize, Integer companyId, Integer type) throws CreateException {
        setFreeTextId(PKGenerator.i.nextKey(ContactConstants.TABLE_FREETEXT));

        try {
            byte[] bytes = new byte[fileSize.intValue()];
            //noinspection ResultOfMethodCallIgnored
            fileStream.read(bytes);
            setValue(bytes);
        } catch (IOException e) {
            System.out.println("Unexpected error");
            e.printStackTrace();
        } finally {
            try {
                fileStream.close();
            } catch (IOException e) {
                System.out.println("Unexpected error");
                e.printStackTrace();
            }
        }

        setCompanyId(companyId);
        setVersion(new Integer(1));
        setType(type);
        return null;
    }

    public void ejbPostCreate(byte[] text, Integer companyId, Integer type) {
    }

    public void ejbPostCreate(InputStream fileStream, Integer fileSize, Integer companyId, Integer type) {
    }

    public ContactFreeTextBean() {
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

    public abstract ContactFreeText getContactFreeText();

    public abstract void setContactFreeText(ContactFreeText contactFreeText);

}
