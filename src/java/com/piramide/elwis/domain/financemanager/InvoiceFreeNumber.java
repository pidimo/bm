/**
 * @author : ivan
 *
 * Jatun S.R.L.
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;

public interface InvoiceFreeNumber extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyid);

    Integer getFreeNumberId();

    void setFreeNumberId(Integer freeNumberId);

    Integer getNumber();

    void setNumber(Integer number);

    Integer getInvoiceDate();

    void setInvoiceDate(Integer invoiceDate);

    String getRuleFormat();

    void setRuleFormat(String ruleFormat);

    Integer getSequenceRuleId();

    void setSequenceRuleId(Integer sequenceRuleId);

    SequenceRule getSequenceRule();

    void setSequenceRule(SequenceRule sequenceRule);
}
