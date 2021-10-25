package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 3:50:51 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportCaseSeverity extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getSequence();

    void setSequence(Integer sequence);

    Integer getSeverityId();

    void setSeverityId(Integer severityId);

    String getSeverityName();

    void setSeverityName(String name);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getLangTextId();

    void setLangTextId(Integer langTextId);
}
