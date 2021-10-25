/**
 * @author : ivan
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;

public interface InvoicePayment extends EJBLocalObject {
    Integer getPaymentId();

    void setPaymentId(Integer paymentid);

    java.math.BigDecimal getAmount();

    void setAmount(java.math.BigDecimal amount);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getFreetextId();

    void setFreetextId(Integer freetextId);

    Integer getInvoiceId();

    void setInvoiceId(Integer invoiceId);

    Integer getPayDate();

    void setPayDate(Integer payDate);

    Integer getVersion();

    void setVersion(Integer version);

    FinanceFreeText getFinanceFreeText();

    void setFinanceFreeText(FinanceFreeText financeFreeText);

    Invoice getInvoice();

    void setInvoice(Invoice invoice);

    Integer getCreditNoteId();

    void setCreditNoteId(Integer creditNoteId);

    Integer getBankAccountId();

    void setBankAccountId(Integer bankAccountId);

    Boolean getExported();

    void setExported(Boolean exported);
}
