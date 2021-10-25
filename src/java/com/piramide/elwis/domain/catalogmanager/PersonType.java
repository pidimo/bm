package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the PersonType Entity Bean
 *
 * @author Ivan
 * @version $Id: PersonType.java 1922 2004-07-19 21:20:07Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface PersonType extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getPersonTypeId();

    void setPersonTypeId(Integer personTypeId);

    Integer getVersion();

    void setVersion(Integer versionId);

    String getPersonTypeName();

    void setPersonTypeName(String nameId);
}
