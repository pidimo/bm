package com.piramide.elwis.domain.schedulermanager;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: SchedulerAccess.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 22-04-2005 02:38:46 PM Fernando Montaño Exp $
 */


public interface SchedulerAccess extends EJBLocalObject {
    Integer getOwnerUserId();

    void setOwnerUserId(Integer ownerUserId);

    Integer getUserId();

    void setUserId(Integer userId);

    Byte getPermission();

    void setPermission(Byte permission);

    Byte getPrivatePermission();

    void setPrivatePermission(Byte privatePermission);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);
}
