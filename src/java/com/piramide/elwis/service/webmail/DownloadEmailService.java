/**
 * @author : Ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.service.webmail;

import com.piramide.elwis.utils.webmail.jms.WebmailAccountMessage;

import javax.ejb.EJBLocalObject;
import javax.transaction.SystemException;

public interface DownloadEmailService extends EJBLocalObject {
    public void downloadEmails(Integer userMailId);

    public void downloadEmails(WebmailAccountMessage accountMessage) throws SystemException;
}
