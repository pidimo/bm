/**
 * Jatun S.R.L.
 * @author alvaro
 * @version $Id: ${NAME}.java 18-feb-2009 9:10:21
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;

public interface IncomingInvoice extends EJBLocalObject {
    Integer getIncomingInvoiceId();

    void setIncomingInvoiceId(Integer incomingInvoiceId);

    BigDecimal getAmountGross();

    void setAmountGross(BigDecimal amountGross);

    BigDecimal getAmountNet();

    void setAmountNet(BigDecimal amountNet);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getCurrencyId();

    void setCurrencyId(Integer currencyId);

    Integer getInvoiceDate();

    void setInvoiceDate(Integer invoiceDate);

    Integer getNotesId();

    void setNotesId(Integer notesId);

    BigDecimal getOpenAmount();

    void setOpenAmount(BigDecimal openamount);

    Integer getPaidUntil();

    void setPaidUntil(Integer paidUntil);

    Integer getReceiptDate();

    void setReceiptDate(Integer receiptDate);

    Integer getSupplierId();

    void setSupplierId(Integer suplierId);

    Integer getToBePaidUntil();

    void setToBePaidUntil(Integer toBePaidUntil);

    Integer getType();

    void setType(Integer type);

    Integer getVersion();

    void setVersion(Integer version);

    java.util.Collection getIncomingPayments();

    void setIncomingPayments(java.util.Collection incomingPayments);

    FinanceFreeText getNote();

    void setNote(FinanceFreeText note);

    String getInvoiceNumber();

    void setInvoiceNumber(String invoiceNumber);

    Integer getDocumentId();

    void setDocumentId(Integer documentId);

    FinanceFreeText getInvoiceDocument();

    void setInvoiceDocument(FinanceFreeText invoiceDocument);
}
