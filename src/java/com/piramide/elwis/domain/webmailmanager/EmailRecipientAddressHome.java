/**
 * Jatun S.R.L
 *
 * @author ivan
 */
package com.piramide.elwis.domain.webmailmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface EmailRecipientAddressHome extends EJBLocalHome {
    EmailRecipientAddress create(ComponentDTO dto) throws CreateException;

    EmailRecipientAddress findByPrimaryKey(Integer key) throws FinderException;

    Collection findByMailRecipientId(Integer mailRecipientId, Integer companyId) throws FinderException;
}
