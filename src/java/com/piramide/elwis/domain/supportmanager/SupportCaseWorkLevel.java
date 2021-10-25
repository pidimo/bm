package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 3:10:53 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportCaseWorkLevel extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getWorkLevelId();

    void setWorkLevelId(Integer workLevelId);

    Integer getSequence();

    void setSequence(Integer sequence);

    Integer getVersion();

    void setVersion(Integer version);

    String getWorkLevelName();

    void setWorkLevelName(String name);

    Integer getLangTextId();

    void setLangTextId(Integer langTextId);
}
