package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;

/**
 * Alfacentauro Team
 *
 * @author Alvaro
 * @version $Id: MailContact.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java ,v 1.1 27-07-2005 10:30:08 AM Alvaro Exp $
 */

public interface MailContact extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContactId();

    void setContactId(Integer contactId);

    Integer getMailContactId();

    void setMailContactId(Integer mailContactId);

    Integer getMailId();

    void setMailId(Integer mailId);

    Mail getMail();

    void setMail(Mail mail);

    String getEmail();

    void setEmail(String email);
}
