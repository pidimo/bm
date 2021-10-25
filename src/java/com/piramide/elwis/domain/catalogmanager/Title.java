package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the Title Entity Bean
 *
 * @author Ivan
 * @version $Id: Title.java 1922 2004-07-19 21:20:07Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface Title extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getTitleId();

    void setTitleId(Integer titleId);

    String getTitleName();

    void setTitleName(String nameId);

    Integer getVersion();

    void setVersion(Integer versionId);
}
