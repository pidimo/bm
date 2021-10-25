package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: AddressGroup.java 8204 2008-04-25 20:56:58Z ivan ${NAME}, 14-03-2005 02:22:45 PM alvaro Exp $
 */

public interface AddressGroup extends EJBLocalObject {
    Integer getAddressGroupId();

    void setAddressGroupId(Integer addressGroupId);

    Integer getMailGroupAddrId();

    void setMailGroupAddrId(Integer mailGroupAddrId);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Integer getTelecomId();

    void setTelecomId(Integer telecomId);

    Integer getSendToAll();

    void setSendToAll(Integer sendToAll);

    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);
}
