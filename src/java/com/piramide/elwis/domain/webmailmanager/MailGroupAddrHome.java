package com.piramide.elwis.domain.webmailmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: MailGroupAddrHome.java 9695 2009-09-10 21:34:43Z fernando ${NAME}, 14-03-2005 02:10:54 PM alvaro Exp $
 */

public interface MailGroupAddrHome extends EJBLocalHome {
    MailGroupAddr findByPrimaryKey(Integer key) throws FinderException;

    public MailGroupAddr create(ComponentDTO dto) throws CreateException;
}
