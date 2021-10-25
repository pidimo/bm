/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

public interface SequenceRule extends EJBLocalObject {
    Integer getNumberId();

    void setNumberId(Integer numberId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getFormat();

    void setFormat(String format);

    Integer getLastNumber();

    void setLastNumber(Integer lastNumber);

    Integer getLastDate();

    void setLastDate(Integer lastDate);

    Integer getResetType();

    void setResetType(Integer resetType);

    Integer getStartNumber();

    void setStartNumber(Integer startNumber);

    Integer getType();

    void setType(Integer type);

    Integer getVersion();

    void setVersion(Integer version);

    String getLabel();

    void setLabel(String label);

    Collection getInvoiceFreeNumbers();

    void setInvoiceFreeNumbers(Collection invoiceFreeNumbers);

    Collection getInvoices();

    void setInvoices(Collection invoices);

    Integer getDebitorId();

    void setDebitorId(Integer debitorId);

    String getDebitorNumber();

    void setDebitorNumber(String debitorNumber);
}
