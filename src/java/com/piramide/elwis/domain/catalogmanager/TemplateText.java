/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: TemplateText.java 1314 2004-06-25 20:28:05Z ivan ${NAME}.java, v 2.0 09-jun-2004 11:19:11 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

public interface TemplateText extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    Integer getLanguageId();

    void setLanguageId(Integer languageId);

    Integer getTemplateId();

    void setTemplateId(Integer templateId);

    Integer getVersion();

    void setVersion(Integer version);

    FreeText getFreeText();

    void setFreeText(FreeText freeText);

    Boolean getByDefault();

    void setByDefault(Boolean byDefault);
}
