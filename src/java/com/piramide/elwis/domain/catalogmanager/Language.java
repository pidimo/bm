package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the Language Entity Bean
 *
 * @author Ivan
 * @version $Id: Language.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface Language extends EJBLocalObject {

    Integer getLanguageId();

    Integer getCompanyId();

    String getLanguageName();

    void setLanguageId(Integer languageId);

    Integer getVersion();

    void setVersion(Integer versionId);

    void setCompanyId(Integer companyId);

    void setLanguageName(String nameId);

    String getLanguageIso();

    void setLanguageIso(String code);

    Boolean getIsDefault();

    void setIsDefault(Boolean is_default);

}
