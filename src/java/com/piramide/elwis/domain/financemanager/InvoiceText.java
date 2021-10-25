/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;

public interface InvoiceText extends EJBLocalObject {
    Integer getLanguageId();

    void setLanguageId(Integer languageId);

    Integer getTemplateId();

    void setTemplateId(Integer templateId);

    Integer getFreetextId();

    void setFreetextId(Integer freetextId);

    Boolean getIsDefault();

    void setIsDefault(Boolean isDefault);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer version);

    FinanceFreeText getFinanceFreeText();

    void setFinanceFreeText(FinanceFreeText financeFreeText);
}
