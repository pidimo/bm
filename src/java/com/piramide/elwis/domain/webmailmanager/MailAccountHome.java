/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
package com.piramide.elwis.domain.webmailmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface MailAccountHome extends EJBLocalHome {
    MailAccount create(ComponentDTO dto) throws CreateException;

    MailAccount findByPrimaryKey(Integer key) throws FinderException;

    MailAccount findAccountByUserMail(String login,
                                      String serverName,
                                      Integer userMailId,
                                      Integer companyId) throws FinderException;

    Collection findAccountsByLoginAndServerName(String login,
                                                String serverName,
                                                Integer userMailId,
                                                Integer companyId) throws FinderException;

    Collection findSMTPAccountsByUserAndServerName(String smtpUser,
                                                   String smtpServer,
                                                   Integer userMailId,
                                                   Integer companyId) throws FinderException;

    Collection findAccountsByUserMailAndCompany(Integer userMailId,
                                                Integer companyId) throws FinderException;

    Collection findAccountsByEmailAndUser(String email, Integer userMailId) throws FinderException;

    MailAccount findDefaultAccount(Integer userMailId, Integer companyId) throws FinderException;

    MailAccount findSMTPAccountsByUserMail(String smtpUser,
                                           String smtpServer,
                                           Integer userMailId,
                                           Integer companyId) throws FinderException;

    Collection findBatchWithReplyMessages() throws FinderException;

}
