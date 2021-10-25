/**
 * Jatun S.R.L
 *
 * @author ivan
 */
package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

public interface EmailAccountError extends EJBLocalObject {
    Integer getEmailAccountErrorId();

    void setEmailAccountErrorId(Integer emailAccountErrorId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getErrorType();

    void setErrorType(Integer errortype);

    Integer getMailAccountId();

    void setMailAccountId(Integer mailAccountId);

    Integer getUserMailId();

    void setUserMailId(Integer userMailId);

    Long getTimeError();

    void setTimeError(Long timeError);

    Collection getEmailAccountErrorDetails();

    void setEmailAccountErrorDetails(Collection emailAccountErrorDetails);

    String getCausedBy();

    void setCausedBy(String causedBy);
}
