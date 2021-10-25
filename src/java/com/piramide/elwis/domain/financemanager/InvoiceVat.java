/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;

public interface InvoiceVat extends EJBLocalObject {
    Integer getInvoiceId();

    void setInvoiceId(Integer invoiceId);

    Integer getVatId();

    void setVatId(Integer vatId);

    java.math.BigDecimal getAmount();

    void setAmount(java.math.BigDecimal amount);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    java.math.BigDecimal getVatRate();

    void setVatRate(java.math.BigDecimal vatRate);
}
