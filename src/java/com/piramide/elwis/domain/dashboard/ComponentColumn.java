package com.piramide.elwis.domain.dashboard;

import javax.ejb.EJBLocalObject;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:50:56 PM
 */

public interface ComponentColumn extends EJBLocalObject {
    Integer getComponentColumnId();

    void setComponentColumnId(Integer componentColumnId);

    Integer getXmlColumnId();

    void setXmlColumnId(Integer xmlColumnId);

    String getOrd();

    void setOrd(String ord);

    Integer getPosition();

    void setPosition(Integer position);

    Component getComponent();

    void setComponent(Component component);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);
}
