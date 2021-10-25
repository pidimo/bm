package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 22, 2005
 * Time: 3:46:24 PM
 * To change this template use File | Settings | File Templates.
 */

public interface UserOfGroup extends EJBLocalObject {
    Integer getUserId();

    void setUserId(Integer userId);

    Integer getUserGroupId();

    void setUserGroupId(Integer userGroupId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);
}
