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
import java.util.Collection;

public abstract class SequenceRuleBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setNumberId(PKGenerator.i.nextKey(FinanceConstants.TABLE_SEQUENCERULE));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public SequenceRuleBean() {
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

    public abstract Integer getNumberId();

    public abstract void setNumberId(Integer numberId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getFormat();

    public abstract void setFormat(String format);

    public abstract Integer getLastNumber();

    public abstract void setLastNumber(Integer lastNumber);

    public abstract Integer getLastDate();

    public abstract void setLastDate(Integer lastDate);

    public abstract Integer getResetType();

    public abstract void setResetType(Integer resetType);

    public abstract Integer getStartNumber();

    public abstract void setStartNumber(Integer startNumber);

    public abstract Integer getType();

    public abstract void setType(Integer type);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract String getLabel();

    public abstract void setLabel(String label);

    public abstract Collection getInvoiceFreeNumbers();

    public abstract void setInvoiceFreeNumbers(Collection invoiceFreeNumbers);

    public abstract Collection getInvoices();

    public abstract void setInvoices(Collection invoices);

    public abstract Integer getDebitorId();

    public abstract void setDebitorId(Integer debitorId);

    public abstract String getDebitorNumber();

    public abstract void setDebitorNumber(String debitorNumber);
}
