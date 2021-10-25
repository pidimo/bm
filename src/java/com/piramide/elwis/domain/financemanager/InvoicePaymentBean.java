/**
 * @author : ivan
 */
package com.piramide.elwis.domain.financemanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class InvoicePaymentBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setPaymentId(PKGenerator.i.nextKey(FinanceConstants.TABLE_INVOICEPAYMENT));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) {

    }

    public InvoicePaymentBean() {
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

    public abstract Integer getPaymentId();

    public abstract void setPaymentId(Integer paymentid);

    public abstract java.math.BigDecimal getAmount();

    public abstract void setAmount(java.math.BigDecimal amount);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getFreetextId();

    public abstract void setFreetextId(Integer freetextId);

    public abstract Integer getInvoiceId();

    public abstract void setInvoiceId(Integer invoiceId);

    public abstract Integer getPayDate();

    public abstract void setPayDate(Integer payDate);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract FinanceFreeText getFinanceFreeText();

    public abstract void setFinanceFreeText(FinanceFreeText financeFreeText);

    public abstract Invoice getInvoice();

    public abstract void setInvoice(Invoice invoice);

    public abstract Integer getCreditNoteId();

    public abstract void setCreditNoteId(Integer creditNoteId);

    public abstract Integer getBankAccountId();

    public abstract void setBankAccountId(Integer bankAccountId);

    public abstract Boolean getExported();

    public abstract void setExported(Boolean exported);
}
