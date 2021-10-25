package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: Signature.java 8011 2008-02-20 14:40:49Z ivan ${NAME}.java ,v 1.1 02-02-2005 04:15:25 PM miky Exp $
 */

public interface Signature extends EJBLocalObject {
    Integer getSignatureId();

    void setSignatureId(Integer signatureId);

    String getSignatureName();

    void setSignatureName(String signatureName);

    Integer getUserMailId();

    void setUserMailId(Integer userMailId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Boolean getIsDefault();

    void setIsDefault(Boolean isDefault);

    Integer getTextSignatureId();

    void setTextSignatureId(Integer textsignatureid);

    Integer getHtmlSignatureId();

    void setHtmlSignatureId(Integer htmlSignatureId);

    Integer getMailAccountId();

    void setMailAccountId(Integer mailAccountId);

    java.util.Collection getMails();

    void setMails(java.util.Collection mails);
}
