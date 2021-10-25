package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: MailGroupAddr.java 7936 2007-10-27 16:08:39Z fernando ${NAME}, 14-03-2005 02:10:54 PM alvaro Exp $
 */

public interface MailGroupAddr extends EJBLocalObject {
    Integer getMailGroupAddrId();

    void setMailGroupAddrId(Integer mailGroupAddrId);

    String getName();

    void setName(String name);

    Integer getUserMailId();

    void setUserMailId(Integer userMailId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Collection getAddressGroups();

    void setAddressGroups(Collection addressGroups);
}
