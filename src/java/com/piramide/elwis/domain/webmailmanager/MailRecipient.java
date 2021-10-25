package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: MailRecipient.java 9139 2009-04-22 22:31:38Z ivan ${NAME}, 14-03-2005 02:33:54 PM alvaro Exp $
 */

public interface MailRecipient extends EJBLocalObject {
    Integer getMailRecipientId();

    void setMailRecipientId(Integer mailRecipientId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getEmail();

    void setEmail(String email);

    Integer getMailId();

    void setMailId(Integer mailId);

    Integer getType();

    void setType(Integer type);

    String getPersonalName();

    void setPersonalName(String personalName);

    java.util.Collection getRecipientAddresses();

    void setRecipientAddresses(java.util.Collection recipientAddresses);
}
