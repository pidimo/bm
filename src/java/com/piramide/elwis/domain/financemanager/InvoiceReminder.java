/**
 * @author: ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;

public interface InvoiceReminder extends EJBLocalObject {
    Integer getReminderId();

    void setReminderId(Integer reminderId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getDate();

    void setDate(Integer date);

    Integer getDescriptionId();

    void setDescriptionId(Integer descriptionId);

    Integer getDocumentId();

    void setDocumentId(Integer documentId);

    Integer getInvoiceId();

    void setInvoiceId(Integer invoiceId);

    Integer getReminderLevelId();

    void setReminderLevelId(Integer reminderLevelId);

    Integer getVersion();

    void setVersion(Integer version);

    FinanceFreeText getDescription();

    void setDescription(FinanceFreeText description);

    ReminderLevel getReminderLevel();

    void setReminderLevel(ReminderLevel reminderLevel);

    Invoice getInvoice();

    void setInvoice(Invoice invoice);
}
