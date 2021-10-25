/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;

public interface Invoice extends EJBLocalObject {
    Integer getInvoiceId();

    void setInvoiceId(Integer invoiceId);

    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Integer getCurrencyId();

    void setCurrencyId(Integer currencyId);

    Integer getDocumentId();

    void setDocumentId(Integer documentId);

    Integer getInvoiceDate();

    void setInvoiceDate(Integer invoiceDate);

    Integer getNotesId();

    void setNotesId(Integer notesId);

    String getNumber();

    void setNumber(String number);

    java.math.BigDecimal getOpenAmount();

    void setOpenAmount(java.math.BigDecimal openAmount);

    Integer getPayConditionId();

    void setPayConditionId(Integer payConditionId);

    Integer getPaymentDate();

    void setPaymentDate(Integer paymentDate);

    Integer getReminderLevel();

    void setReminderLevel(Integer reminderLevel);

    Integer getReminderDate();

    void setReminderDate(Integer reminderDate);

    Integer getType();

    void setType(Integer type);

    java.math.BigDecimal getTotalAmountNet();

    void setTotalAmountNet(java.math.BigDecimal totalAmountnet);

    java.math.BigDecimal getTotalAmountGross();

    void setTotalAmountGross(java.math.BigDecimal totalAmountGross);

    Integer getTemplateId();

    void setTemplateId(Integer templateId);

    Integer getVersion();

    void setVersion(Integer version);

    FinanceFreeText getFinanceFreeText();

    void setFinanceFreeText(FinanceFreeText financeFreeText);

    java.util.Collection getInvoicePayment();

    void setInvoicePayment(java.util.Collection invoicePayment);

    java.util.Collection getInvoicePositions();

    void setInvoicePositions(java.util.Collection invoicePositions);

    java.util.Collection getInvoiceVats();

    void setInvoiceVats(java.util.Collection invoiceVats);

    java.util.Collection getInvoiceReminders();

    void setInvoiceReminders(java.util.Collection invoiceReminders);

    FinanceFreeText getDocument();

    void setDocument(FinanceFreeText document);

    String getRuleFormat();

    void setRuleFormat(String ruleFormat);

    Integer getRuleNumber();

    void setRuleNumber(Integer ruleNumber);

    Integer getCreditNoteOfId();

    void setCreditNoteOfId(Integer creditNoteOfId);

    Integer getNetGross();

    void setNetGross(Integer netGross);

    Integer getSequenceRuleId();

    void setSequenceRuleId(Integer sequenceRuleId);

    SequenceRule getSequenceRule();

    void setSequenceRule(SequenceRule sequenceRule);

    Integer getSentAddressId();

    void setSentAddressId(Integer sentAddressId);

    Integer getSentContactPersonId();

    void setSentContactPersonId(Integer sentContactPersonId);

    Integer getAdditionalAddressId();

    void setAdditionalAddressId(Integer additionalAddressId);

    String getTitle();

    void setTitle(String title);

    Integer getServiceDate();

    void setServiceDate(Integer serviceDate);
}
