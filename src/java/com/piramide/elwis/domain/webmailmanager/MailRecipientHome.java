package com.piramide.elwis.domain.webmailmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: MailRecipientHome.java 9139 2009-04-22 22:31:38Z ivan ${NAME}, 14-03-2005 02:33:54 PM alvaro Exp $
 */

public interface MailRecipientHome extends EJBLocalHome {
    MailRecipient findByPrimaryKey(Integer key) throws FinderException;

    public MailRecipient create(ComponentDTO dto) throws CreateException;

    public Collection findByMailIdAndToType(Integer mailId, Integer mailToType, Integer companyId) throws FinderException;

    Collection findByMailId(Integer mailId, Integer companyId) throws FinderException;
}
