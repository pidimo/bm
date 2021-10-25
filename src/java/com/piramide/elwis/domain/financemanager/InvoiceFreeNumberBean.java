/**
 * @author : ivan
 *
 * Jatun S.R.L.
 */
package com.piramide.elwis.domain.financemanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class InvoiceFreeNumberBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setFreeNumberId(PKGenerator.i.nextKey(FinanceConstants.TABLE_INVOICEFREENUMBER));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public InvoiceFreeNumberBean() {
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyid);

    public abstract Integer getFreeNumberId();

    public abstract void setFreeNumberId(Integer freeNumberId);

    public abstract Integer getNumber();

    public abstract void setNumber(Integer number);

    public abstract Integer ejbSelectGetNumberByRange(Integer sequenceRuleId,
                                                      Integer startDate,
                                                      Integer endDate,
                                                      Integer companyId) throws FinderException;

    public Integer ejbHomeSelectGetNumberByRange(Integer sequenceRuleId,
                                                 Integer startDate,
                                                 Integer endDate,
                                                 Integer companyId) throws FinderException {
        return ejbSelectGetNumberByRange(sequenceRuleId, startDate, endDate, companyId);
    }

    public abstract Integer ejbSelectGetNumber(Integer sequenceRuleId, Integer companyId) throws FinderException;

    public Integer ejbHomeSelectGetNumber(Integer sequenceRuleId, Integer companyId) throws FinderException {
        return ejbSelectGetNumber(sequenceRuleId, companyId);
    }

    public abstract Integer getInvoiceDate();

    public abstract void setInvoiceDate(Integer invoiceDate);

    public abstract String getRuleFormat();

    public abstract void setRuleFormat(String ruleFormat);

    public abstract Integer getSequenceRuleId();

    public abstract void setSequenceRuleId(Integer sequenceRuleId);

    public abstract SequenceRule getSequenceRule();

    public abstract void setSequenceRule(SequenceRule sequenceRule);
}
