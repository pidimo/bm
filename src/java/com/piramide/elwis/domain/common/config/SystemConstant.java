package com.piramide.elwis.domain.common.config;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: SystemConstant.java 9121 2009-04-17 00:28:59Z fernando $
 */


public interface SystemConstant extends javax.ejb.EJBLocalObject {
    String getName();

    void setName(String name);

    String getResourceKey();

    void setResourceKey(String resourceKey);

    String getDescription();

    void setDescription(String description);

    String getType();

    void setType(String type);

    String getValue();

    void setValue(String value);
}
