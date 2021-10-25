/**
 * @author Tayes
 * @version $Id: ContactFreeTextHome.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 16-05-2004 12:15:45 PM Tayes Exp $
 */
package com.piramide.elwis.domain.contactmanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.io.InputStream;

public interface ContactFreeTextHome extends EJBLocalHome {
    public ContactFreeText create(byte[] text, Integer companyId, Integer type) throws CreateException;

    public ContactFreeText create(InputStream fileStream, Integer fileSize, Integer companyId, Integer type) throws CreateException;

    com.piramide.elwis.domain.contactmanager.ContactFreeText findByPrimaryKey(Integer key) throws FinderException;

}
