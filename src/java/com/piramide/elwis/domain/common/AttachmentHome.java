package com.piramide.elwis.domain.common;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: AttachmentHome.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v1.0 05-jun-2008 17:10:04 Miky Exp $
 */

public interface AttachmentHome extends EJBLocalHome {
    public Attachment create(ComponentDTO dto) throws CreateException;

    Attachment findByPrimaryKey(Integer key) throws FinderException;
}
