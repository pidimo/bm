/**
 * @author Yumi
 * @version $Id: ContactAttachHome.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v 2.0 13-may-2004 18:25:50 Yumi Exp $
 */
package com.piramide.elwis.domain.contactmanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ContactAttachHome extends EJBLocalHome {

    public ContactAttach create(Integer contactId, Integer freeTextId, Integer companyId, String fileName, String contentType, Integer size) throws CreateException;

    ContactAttach findByPrimaryKey(ContactAttachPK contactAttachPK) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByContact(Integer contactId) throws FinderException;

}
