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

public abstract class ReminderLevelBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setReminderLevelId(PKGenerator.i.nextKey(FinanceConstants.TABLE_REMINDERLEVEL));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public ReminderLevelBean() {
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

    public abstract Integer getReminderLevelId();

    public abstract void setReminderLevelId(Integer reminderLevelId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract java.math.BigDecimal getFee();

    public abstract void setFee(java.math.BigDecimal fee);

    public abstract Integer getLevel();

    public abstract void setLevel(Integer level);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Integer getNumberOfDays();

    public abstract void setNumberOfDays(Integer numberofdays);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract java.util.Collection getReminderTexts();

    public abstract void setReminderTexts(java.util.Collection reminderText);

    public abstract Integer ejbSelectMaxLevel(Integer companyId) throws FinderException;

    public Integer ejbHomeSelectMaxLevel(Integer companyId) throws FinderException {
        return ejbSelectMaxLevel(companyId);
    }

}
