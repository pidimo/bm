/**
 * Jatun S.R.L.
 * @author alvaro
 * @version $Id: ${NAME}.java 18-feb-2009 9:46:34
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;

public interface IncomingPayment extends EJBLocalObject {
    Integer getPaymentId();

    void setPaymentId(Integer incomingPaymentId);

    java.math.BigDecimal getAmount();

    void setAmount(java.math.BigDecimal amount);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getNotesId();

    void setNotesId(Integer notesId);

    Integer getPayDate();

    void setPayDate(Integer payDate);

    Integer getIncomingInvoiceId();

    void setIncomingInvoiceId(Integer incomingInvoiceId);

    FinanceFreeText getNote();

    void setNote(FinanceFreeText note);

    Integer getVersion();

    void setVersion(Integer version);
}
