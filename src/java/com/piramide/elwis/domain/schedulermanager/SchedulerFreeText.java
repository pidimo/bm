package com.piramide.elwis.domain.schedulermanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 8, 2005
 * Time: 4:07:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SchedulerFreeText extends EJBLocalObject {

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    Integer getType();

    void setType(Integer freeTextType);

    byte[] getValue();

    void setValue(byte[] freeTextValue);

    Integer getVersion();

    void setVersion(Integer version);

}
