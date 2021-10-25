/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;

public interface InvoiceTemplate extends EJBLocalObject {
    Integer getTemplateId();

    void setTemplateId(Integer templateId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getTitle();

    void setTitle(String title);

    Integer getVersion();

    void setVersion(Integer version);

    java.util.Collection getInvoiceTexts();

    void setInvoiceTexts(java.util.Collection invoiceTexts);
}
