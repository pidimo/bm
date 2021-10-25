/**
 * Jatun S.R.L.
 * @author alvaro
 * @version $Id: ${NAME}.java 18-feb-2009 9:46:33
 */
package com.piramide.elwis.domain.financemanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class IncomingPaymentBean implements EntityBean {
    EntityContext entityContext;

    public IncomingPaymentBean() {
    }

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {

        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        this.setPaymentId(PKGenerator.i.nextKey(FinanceConstants.TABLE_INCOMINGPAYMENT));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

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

    public abstract void setPaymentId(Integer incomingPaymentId);

    public abstract java.math.BigDecimal getAmount();

    public abstract void setAmount(java.math.BigDecimal amount);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getNotesId();

    public abstract void setNotesId(Integer notesId);

    public abstract Integer getPayDate();

    public abstract void setPayDate(Integer payDate);

    public abstract Integer getIncomingInvoiceId();

    public abstract void setIncomingInvoiceId(Integer incomingInvoiceId);

    public abstract FinanceFreeText getNote();

    public abstract void setNote(FinanceFreeText note);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer ejbSelectMaxPayDate(Integer incomingInvoiceId, Integer companyId) throws FinderException;

    public Integer ejbHomeSelectMaxPayDate(Integer invoiceId, Integer companyId) throws FinderException {
        return ejbSelectMaxPayDate(invoiceId, companyId);
    }
}
