package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: Body.java 7936 2007-10-27 16:08:39Z fernando ${NAME}, 02-02-2005 04:30:54 PM alvaro Exp $
 */

public interface Body extends EJBLocalObject {
    Integer getBodyId();

    void setBodyId(Integer bodyId);

    Integer getBodyType();

    void setBodyType(Integer bodyType);

    byte[] getBodyContent();

    void setBodyContent(byte[] bodyBody);

    Integer getCompanyId();

    void setCompanyId(Integer bodyCompanyId);
}
