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
 * @version $Id: MailHome.java 10320 2013-02-26 20:02:58Z miguel ${NAME}.java ,v 1.1 02-02-2005 04:32:28 PM miky Exp $
 */

public interface MailHome extends EJBLocalHome {
    Mail findByPrimaryKey(Integer key) throws FinderException;

    public Mail create(ComponentDTO dto) throws CreateException;

    Collection findByFolderId(Integer folderId, Integer companyId) throws FinderException;

    Mail findByMessageID(String messageId, String mailAccount, Integer companyId) throws FinderException;

    Collection findByFolderIdAndIsMailContact(Integer folderId, Integer companyId) throws FinderException;

    Integer selectCountMessages(Integer folderId, Integer companyId) throws FinderException;

    /**
     * @deprecated because mail state now is bit code and bitwise operators should be applied
     */
    Integer selectCountUnReadMessages(Integer folderId, Integer companyId) throws FinderException;

    Long selectCalculateFolderSize(Integer folderId, Integer companyId) throws FinderException;

    Collection selectEmailIdsToSend(Integer folderId, Integer companyId) throws FinderException;

    /**
     * Temporal finder *
     */
    Collection findAllBySentDateAsNull() throws FinderException;

    Collection findMailWithHaveAttachmentIsNull() throws FinderException;

    Collection findNewEmails(Integer folderId, Integer companyId) throws FinderException;

    Collection selectGetIncompleteEmails(Integer folderId, Integer companyId) throws FinderException;
}
