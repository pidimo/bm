package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the PayMorality Entity Bean
 *
 * @author Ivan
 * @version $Id: PayMorality.java 1933 2004-07-21 16:35:51Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */


public interface PayMorality extends EJBLocalObject {
    Integer getPayMoralityId();

    void setPayMoralityId(Integer payMoralityId);

    String getPayMoralityName();

    void setPayMoralityName(String nameId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer versionId);
}
