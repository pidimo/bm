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

public interface EmailAccountErrorHome extends EJBLocalHome {
    EmailAccountError create(ComponentDTO dto) throws CreateException;

    EmailAccountError findByPrimaryKey(Integer key) throws FinderException;

    Collection findByUserMailId(Integer userMailId, Integer companyId) throws FinderException;

    Collection findByMailAccountId(Integer userMailId,
                                          Integer mailAccountId,
                                          Integer companyId) throws FinderException;

    Collection findByErrorType(Integer userMailId,
                                      Integer mailAccountId,
                                      Integer errorType,
                                      Integer companyId) throws FinderException;

    Collection findByCause(Integer userMailId,
                                      Integer mailAccountId,
                                      Integer errorType,
                                      Integer companyId,
                                      String causedBy) throws FinderException;
}
