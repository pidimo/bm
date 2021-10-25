package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 3:28:59 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportFreeText extends EJBLocalObject {
    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getFreeTextType();

    void setFreeTextType(Integer type);

    Integer getVersion();

    void setVersion(Integer version);

    byte[] getFreeTextValue();

    void setFreeTextValue(byte[] freeTextValue);

}
