package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 3:22:05 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportUser extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getProductId();

    void setProductId(Integer productId);

    Integer getUserId();

    void setUserId(Integer userId);
}
