/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class InvoiceVatBean implements EntityBean {
    EntityContext entityContext;

    public InvoiceVatPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public InvoiceVatBean() {
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

    public abstract Integer getVatId();

    public abstract void setVatId(Integer vatId);

    public abstract java.math.BigDecimal getAmount();

    public abstract void setAmount(java.math.BigDecimal amount);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract java.math.BigDecimal getVatRate();

    public abstract void setVatRate(java.math.BigDecimal vatRate);
}
