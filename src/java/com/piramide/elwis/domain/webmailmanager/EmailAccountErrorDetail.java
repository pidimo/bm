package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public interface EmailAccountErrorDetail extends EJBLocalObject {
    Integer getEmailAccountErrorDetailId();

    void setEmailAccountErrorDetailId(Integer emailAccountErrorDetailId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getEmails();

    void setEmails(String emails);

    Integer getEmailAccountErrorId();

    void setEmailAccountErrorId(Integer emailAccountErrorId);

    String getSubject();

    void setSubject(String subject);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getMailIdentifier();

    void setMailIdentifier(Integer mailIdentifier);
}
