package com.piramide.elwis.domain.webmailmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: ConditionHome.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java ,v 1.1 02-02-2005 04:28:49 PM miky Exp $
 */

public interface ConditionHome extends EJBLocalHome {
    Condition findByPrimaryKey(Integer key) throws FinderException;

    public Condition create(ComponentDTO dto) throws CreateException;

}
