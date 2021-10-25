package com.piramide.elwis.domain.webmailmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Alfacentauro Team
 *
 * @author Alvaro
 * @version $Id: MailContactHome.java 7936 2007-10-27 16:08:39Z fernando $MailContactHome.java ,v 1.1 27-07-2005 10:30:08 AM Alvaro Exp $
 */

public interface MailContactHome extends EJBLocalHome {
    MailContact findByPrimaryKey(Integer key) throws FinderException;

    public MailContact create(ComponentDTO dto) throws CreateException;

    public Collection findByMailId(Integer mailId, Integer companyId) throws FinderException;

    public MailContact findByContactId(Integer contactId, Integer companyId) throws FinderException;
}
