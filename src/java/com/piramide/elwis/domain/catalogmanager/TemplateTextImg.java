/**
 *
 * @author Miky
 * @version $Id: ${NAME}.java 2009-06-19 06:20:40 PM $
 */
package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

public interface TemplateTextImg extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getImageStoreId();

    void setImageStoreId(Integer imageStoreId);

    Integer getLanguageId();

    void setLanguageId(Integer languageId);

    Integer getTemplateId();

    void setTemplateId(Integer templateId);
}
