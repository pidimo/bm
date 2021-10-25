package com.piramide.elwis.domain.schedulermanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Jun 29, 2005
 * Time: 2:14:04 PM
 * To change this template use File | Settings | File Templates.
 */

public interface TaskType extends EJBLocalObject {
    Integer getTaskTypeId();

    void setTaskTypeId(Integer taskTypeId);

    String getName();

    void setName(String name);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer version);
}
