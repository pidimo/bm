package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 22, 2005
 * Time: 3:42:33 PM
 * To change this template use File | Settings | File Templates.
 */

public interface UserGroup extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getGroupName();

    void setGroupName(String groupName);

    Integer getUserGroupId();

    void setUserGroupId(Integer userGroupId);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getUserOfGroup();

    void setUserOfGroup(Collection userOfGroup);

    Integer getGroupType();

    void setGroupType(Integer groupType);
}
