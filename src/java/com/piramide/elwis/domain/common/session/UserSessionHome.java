package com.piramide.elwis.domain.common.session;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * UserSessionHome local home interface
 *
 * @author Fernando Montaño
 * @version $Id: UserSessionHome.java,v 1.1 2004/10/06 21:40:36 fernando
 */


public interface UserSessionHome extends EJBLocalHome {
    UserSession findByPrimaryKey(UserSessionPK key) throws FinderException;

    UserSession create(ComponentDTO dto) throws CreateException;

    public Collection findByUserId(Integer userId) throws FinderException;
}
