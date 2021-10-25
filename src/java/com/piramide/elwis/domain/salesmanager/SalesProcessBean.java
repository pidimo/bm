package com.piramide.elwis.domain.salesmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * Sales process entity bean class
 *
 * @author Fernando Montaño
 * @version $Id: SalesProcessBean.java 9121 2009-04-17 00:28:59Z fernando $
 */


public abstract class SalesProcessBean implements EntityBean {
    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setProcessId(PKGenerator.i.nextKey(SalesConstants.TABLE_SALESPROCESS));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public SalesProcessBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
    }

    public void unsetEntityContext() throws EJBException {
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

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getDescriptionId();

    public abstract void setDescriptionId(Integer descriptionId);

    public abstract Integer getEmployeeId();

    public abstract void setEmployeeId(Integer employeeId);

    public abstract Integer getEndDate();

    public abstract void setEndDate(Integer endDate);

    public abstract Integer getPriorityId();

    public abstract void setPriorityId(Integer priorityId);

    public abstract Integer getProcessId();

    public abstract void setProcessId(Integer processId);

    public abstract String getProcessName();

    public abstract void setProcessName(String processName);

    public abstract Integer getProbability();

    public abstract void setProbability(Integer probability);

    public abstract Integer getStatusId();

    public abstract void setStatusId(Integer statusId);

    public abstract Integer getStartDate();

    public abstract void setStartDate(Integer startDate);

    public abstract java.math.BigDecimal getValue();

    public abstract void setValue(java.math.BigDecimal value);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract SalesFreeText getDescriptionText();

    public abstract void setDescriptionText(SalesFreeText descriptionText);

    public void setDescriptionText(EJBLocalObject descriptionText) {
        setDescriptionText((SalesFreeText) descriptionText);
    }

    public abstract Collection getActions();

    public abstract void setActions(Collection actions);

    public abstract Integer getActivityId();

    public abstract void setActivityId(Integer activityId);

    public abstract Collection getSale();

    public abstract void setSale(Collection sale);
}
