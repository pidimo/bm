/**
 * Jatun S.R.L
 *
 * @author ivan
 */
package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;

public interface EmailRecipientAddress extends EJBLocalObject {
    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getEmailRecipientAddressId();

    void setEmailRecipientAddressId(Integer emailRecipientAddressId);

    Integer getMailRecipientId();

    void setMailRecipientId(Integer mailRecipientId);
}
