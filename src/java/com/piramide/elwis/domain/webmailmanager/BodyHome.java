package com.piramide.elwis.domain.webmailmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: BodyHome.java 7936 2007-10-27 16:08:39Z fernando ${NAME}, 02-02-2005 04:30:54 PM alvaro Exp $
 */

public interface BodyHome extends EJBLocalHome {
    Body findByPrimaryKey(Integer key) throws FinderException;

    public Body create(ComponentDTO dto) throws CreateException;

    public Body create(byte[] bodyContent, Integer type, Integer companyId) throws CreateException;
}
