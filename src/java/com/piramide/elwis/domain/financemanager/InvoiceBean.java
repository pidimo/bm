/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class InvoiceBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setInvoiceId(PKGenerator.i.nextKey(FinanceConstants.TABLE_INVOICE));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public InvoiceBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        this.entityContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract Integer getInvoiceId();

    public abstract void setInvoiceId(Integer invoiceId);

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Integer getCurrencyId();

    public abstract void setCurrencyId(Integer currencyId);

    public abstract Integer getDocumentId();

    public abstract void setDocumentId(Integer documentId);

    public abstract Integer getInvoiceDate();

    public abstract void setInvoiceDate(Integer invoiceDate);

    public abstract Integer getNotesId();

    public abstract void setNotesId(Integer notesId);

    public abstract String getNumber();

    public abstract void setNumber(String number);

    public abstract java.math.BigDecimal getOpenAmount();

    public abstract void setOpenAmount(java.math.BigDecimal openAmount);

    public abstract Integer getPayConditionId();

    public abstract void setPayConditionId(Integer payConditionId);

    public abstract Integer getPaymentDate();

    public abstract void setPaymentDate(Integer paymentDate);

    public abstract Integer getReminderLevel();

    public abstract void setReminderLevel(Integer reminderLevel);

    public abstract Integer getReminderDate();

    public abstract void setReminderDate(Integer reminderDate);

    public abstract Integer getType();

    public abstract void setType(Integer type);

    public abstract java.math.BigDecimal getTotalAmountNet();

    public abstract void setTotalAmountNet(java.math.BigDecimal totalAmountnet);

    public abstract java.math.BigDecimal getTotalAmountGross();

    public abstract void setTotalAmountGross(java.math.BigDecimal totalAmountGross);

    public abstract Integer getTemplateId();

    public abstract void setTemplateId(Integer templateId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract FinanceFreeText getFinanceFreeText();

    public abstract void setFinanceFreeText(FinanceFreeText financeFreeText);

    public abstract java.util.Collection getInvoicePayment();

    public abstract void setInvoicePayment(java.util.Collection invoicePayment);

    public abstract java.util.Collection getInvoicePositions();

    public abstract void setInvoicePositions(java.util.Collection invoicePositions);

    public abstract java.util.Collection getInvoiceVats();

    public abstract void setInvoiceVats(java.util.Collection invoiceVats);

    public abstract java.util.Collection getInvoiceReminders();

    public abstract void setInvoiceReminders(java.util.Collection invoiceReminders);

    public abstract FinanceFreeText getDocument();

    public abstract void setDocument(FinanceFreeText document);

    public abstract String getRuleFormat();

    public abstract void setRuleFormat(String ruleFormat);

    public abstract Integer getRuleNumber();

    public abstract void setRuleNumber(Integer ruleNumber);

    public abstract Integer ejbSelectGetLastRuleNumberByInvoiceDate(Integer startDate,
                                                                    Integer endDate,
                                                                    Integer sequenceRuleId,
                                                                    Integer companyId) throws FinderException;

    public Integer ejbHomeSelectGetLastRuleNumberByInvoiceDate(Integer startDate,
                                                               Integer endDate,
                                                               Integer sequenceRuleId,
                                                               Integer companyId) throws FinderException {
        return ejbSelectGetLastRuleNumberByInvoiceDate(startDate, endDate, sequenceRuleId, companyId);
    }

    public abstract Integer ejbSelectGetLastRuleNumberByRuleFormat(Integer sequenceRuleId,
                                                                   Integer companyId) throws FinderException;

    public Integer ejbHomeSelectGetLastRuleNumberByRuleFormat(Integer sequenceRuleId,
                                                              Integer companyId) throws FinderException {

        return ejbSelectGetLastRuleNumberByRuleFormat(sequenceRuleId, companyId);
    }

    public abstract Integer getCreditNoteOfId();

    public abstract void setCreditNoteOfId(Integer creditNoteOfId);

    public abstract Integer getNetGross();

    public abstract void setNetGross(Integer netGross);

    public abstract Integer getSequenceRuleId();

    public abstract void setSequenceRuleId(Integer sequenceRuleId);

    public abstract SequenceRule getSequenceRule();

    public abstract void setSequenceRule(SequenceRule sequenceRule);

    public abstract Integer getSentAddressId();

    public abstract void setSentAddressId(Integer sentAddressId);

    public abstract Integer getSentContactPersonId();

    public abstract void setSentContactPersonId(Integer sentContactPersonId);

    public abstract Integer getAdditionalAddressId();

    public abstract void setAdditionalAddressId(Integer additionalAddressId);

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract Integer getServiceDate();

    public abstract void setServiceDate(Integer serviceDate);
}
