package com.piramide.elwis.domain.webmailmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: UserMailHome.java 10216 2012-05-11 22:55:19Z ivan ${NAME}.java ,v 1.1 02-02-2005 04:08:33 PM miky Exp $
 */

public interface UserMailHome extends EJBLocalHome {
    UserMail findByPrimaryKey(Integer key) throws FinderException;

    public UserMail create(ComponentDTO dto) throws CreateException;

    public Collection findAll() throws FinderException;

    Collection selectBackgroundUserMailIds() throws FinderException;
}
