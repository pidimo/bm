/**
 * Jatun S.R.L.
 * @author alvaro
 * @version $Id: ${NAME}.java 18-feb-2009 9:10:19
 */
package com.piramide.elwis.domain.financemanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.math.BigDecimal;

public abstract class IncomingInvoiceBean implements EntityBean {
    EntityContext entityContext;

    public IncomingInvoiceBean() {
    }

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {

        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        this.setIncomingInvoiceId(PKGenerator.i.nextKey(FinanceConstants.TABLE_INCOMINGINVOICE));
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

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }


    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract Integer getIncomingInvoiceId();

    public abstract void setIncomingInvoiceId(Integer incomingInvoiceId);

    public abstract BigDecimal getAmountGross();

    public abstract void setAmountGross(BigDecimal amountGross);

    public abstract BigDecimal getAmountNet();

    public abstract void setAmountNet(BigDecimal amountNet);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getCurrencyId();

    public abstract void setCurrencyId(Integer currencyId);

    public abstract Integer getInvoiceDate();

    public abstract void setInvoiceDate(Integer invoiceDate);

    public abstract Integer getNotesId();

    public abstract void setNotesId(Integer notesId);

    public abstract BigDecimal getOpenAmount();

    public abstract void setOpenAmount(BigDecimal openamount);

    public abstract Integer getPaidUntil();

    public abstract void setPaidUntil(Integer paidUntil);

    public abstract Integer getReceiptDate();

    public abstract void setReceiptDate(Integer receiptDate);

    public abstract Integer getSupplierId();

    public abstract void setSupplierId(Integer suplierId);

    public abstract Integer getToBePaidUntil();

    public abstract void setToBePaidUntil(Integer toBePaidUntil);

    public abstract Integer getType();

    public abstract void setType(Integer type);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract java.util.Collection getIncomingPayments();

    public abstract void setIncomingPayments(java.util.Collection incomingPayments);

    public abstract FinanceFreeText getNote();

    public abstract void setNote(FinanceFreeText note);

    public abstract String getInvoiceNumber();

    public abstract void setInvoiceNumber(String invoiceNumber);

    public abstract Integer getDocumentId();

    public abstract void setDocumentId(Integer documentId);

    public abstract FinanceFreeText getInvoiceDocument();

    public abstract void setInvoiceDocument(FinanceFreeText invoiceDocument);
}
