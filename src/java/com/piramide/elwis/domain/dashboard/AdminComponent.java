package com.piramide.elwis.domain.dashboard;

import javax.ejb.EJBLocalObject;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:40:37 PM
 */

public interface AdminComponent extends EJBLocalObject {
    Integer getContainerId();

    void setContainerId(Integer containerId);

    Integer getComponentId();

    void setComponentId(Integer componentId);

    Integer getRowX();

    void setRowX(Integer rowX);

    Integer getColumnY();

    void setColumnY(Integer columnY);

    Integer getXmlComponentId();

    void setXmlComponentId(Integer xmlComponentId);

    Component getComponent();

    void setComponent(Component component);

    Integer getCompanyid();

    void setCompanyid(Integer companyid);
}
