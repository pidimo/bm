package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the Priority Entity Bean
 *
 * @author Ivan
 * @version $Id: Priority.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface Priority extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getPriorityId();

    void setPriorityId(Integer priorityId);

    Integer getSequence();

    void setSequence(Integer sequence);

    Integer getVersion();

    void setVersion(Integer versionId);

    String getPriorityName();

    void setPriorityName(String nameId);

    String getType();

    void setType(String type);

    Integer getLangTextId();

    void setLangTextId(Integer langTextId);
}
