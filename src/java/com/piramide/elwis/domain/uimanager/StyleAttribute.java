package com.piramide.elwis.domain.uimanager;

import javax.ejb.EJBLocalObject;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: StyleAttribute.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java ,v 1.1 22-04-2005 10:17:57 AM miky Exp $
 */

public interface StyleAttribute extends EJBLocalObject {
    Integer getAttributeId();

    void setAttributeId(Integer attributeId);

    String getName();

    void setName(String name);

    Integer getStyleId();

    void setStyleId(Integer styleId);

    String getValue();

    void setValue(String value);

    Style getStyle();

    void setStyle(Style styleSheet);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);
}
