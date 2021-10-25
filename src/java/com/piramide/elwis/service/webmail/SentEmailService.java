/**
 * @author : Ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.service.webmail;

import javax.ejb.EJBLocalObject;

public interface SentEmailService extends EJBLocalObject {
    public void sentEmailsSync(Integer userMailId,
                               Integer companyId);

    public void sentEmail(Integer mailId,
                          Integer userMailId,
                          Integer folderId,
                          Boolean isConnected);
}
