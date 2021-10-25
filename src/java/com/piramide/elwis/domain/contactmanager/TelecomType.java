package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the TelecomType Entity Bean
 *
 * @author yumi
 * @version $Id: TelecomType.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface TelecomType extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getTelecomTypeId();

    void setTelecomTypeId(Integer telecomTypeId);

    String getTelecomTypeName();

    void setTelecomTypeName(String name);

    String getPosition();

    void setPosition(String position);

    Integer getVersion();

    void setVersion(Integer versionId);

    Integer getLangTextId();

    void setLangTextId(Integer langTextId);

    String getType();

    void setType(String type);
}
