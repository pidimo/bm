package com.piramide.elwis.domain.webmailmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: SignatureHome.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java ,v 1.1 02-02-2005 04:15:25 PM miky Exp $
 */

public interface SignatureHome extends EJBLocalHome {
    Signature findByPrimaryKey(Integer key) throws FinderException;

    public Signature create(ComponentDTO dto) throws CreateException;

    public Signature findDefaultSignature(Integer userMailId, Integer mailAccountId) throws FinderException;
}
