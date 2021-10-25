/**
 * @author: ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.domain.financemanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class InvoiceReminderBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setReminderId(PKGenerator.i.nextKey(FinanceConstants.TABLE_INVOICEREMINDER));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public InvoiceReminderBean() {
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

    public abstract Integer getReminderId();

    public abstract void setReminderId(Integer reminderId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getDate();

    public abstract void setDate(Integer date);

    public abstract Integer getDescriptionId();

    public abstract void setDescriptionId(Integer descriptionId);

    public abstract Integer getDocumentId();

    public abstract void setDocumentId(Integer documentId);

    public abstract Integer getInvoiceId();

    public abstract void setInvoiceId(Integer invoiceId);

    public abstract Integer getReminderLevelId();

    public abstract void setReminderLevelId(Integer reminderLevelId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract FinanceFreeText getDescription();

    public abstract void setDescription(FinanceFreeText description);

    public abstract ReminderLevel getReminderLevel();

    public abstract void setReminderLevel(ReminderLevel reminderLevel);

    public abstract Invoice getInvoice();

    public abstract void setInvoice(Invoice invoice);
}
