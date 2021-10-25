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
 * @version $Id: AttachHome.java 12561 2016-07-05 21:34:16Z miguel ${NAME}, 02-02-2005 04:27:32 PM alvaro Exp $
 */

public interface AttachHome extends EJBLocalHome {
    Attach findByPrimaryKey(Integer key) throws FinderException;

    public Attach create(ComponentDTO dto) throws CreateException;

    public Attach create(Integer companyId, Integer mailId, String attachName, byte[] attachFile) throws CreateException;

    public Attach create(Integer companyId, Integer mailId, String attachName, String emlAttachUUID, Integer size) throws CreateException;

    public Collection findByMailId(Integer mailId, Integer companyId) throws FinderException;

    public Integer selectSumSizeByMailId(Integer mailId) throws FinderException;

    /**
     * Use in UpdateAttachmentSizeCmd
     *
     * @return
     * @throws FinderException
     */
    public Collection findAttachmentsWithOutSize() throws FinderException;
}
