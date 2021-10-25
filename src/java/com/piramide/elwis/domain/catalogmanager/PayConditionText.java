/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

public interface PayConditionText extends EJBLocalObject {
    Integer getPayConditionId();

    void setPayConditionId(Integer payConditionId);

    Integer getLanguageId();

    void setLanguageId(Integer languageId);

    Integer getFreetextId();

    void setFreetextId(Integer freetextId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer version);

    FreeText getFreeText();

    void setFreeText(FreeText freeText);
}
