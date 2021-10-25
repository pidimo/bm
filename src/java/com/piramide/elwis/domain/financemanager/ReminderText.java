/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;

public interface ReminderText extends EJBLocalObject {
    Integer getLanguageId();

    void setLanguageId(Integer languageId);

    Integer getReminderLevelId();

    void setReminderLevelId(Integer reminderLevelId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Boolean getIsDefault();

    void setIsDefault(Boolean isDefault);

    Integer getFreetextId();

    void setFreetextId(Integer freetextId);

    Integer getVersion();

    void setVersion(Integer version);

    FinanceFreeText getFinanceFreeText();

    void setFinanceFreeText(FinanceFreeText financeFreeText);
}
