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
 * @version $Id: FilterHome.java 8089 2008-03-08 00:22:03Z ivan ${NAME}.java ,v 1.1 02-02-2005 04:21:55 PM miky Exp $
 */

public interface FilterHome extends EJBLocalHome {
    Filter findByPrimaryKey(Integer key) throws FinderException;

    public Filter create(ComponentDTO dto) throws CreateException;

    Collection findByFolderId(Integer folderId) throws FinderException;

    Collection findByUserId(Integer userId, Integer companyId) throws FinderException;
}
