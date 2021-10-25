package com.piramide.elwis.domain.schedulermanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * Recurrence Entity Bean
 *
 * @author Fernando Montaño
 * @version $Id: RecurrenceBean.java 9121 2009-04-17 00:28:59Z fernando $
 */


public abstract class RecurrenceBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {

        setAppointmentId(PKGenerator.i.nextKey(SchedulerConstants.TABLE_RECURRENCE));
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
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

    public abstract Integer getAppointmentId();

    public abstract void setAppointmentId(Integer appointmentId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getRecurEvery();

    public abstract void setRecurEvery(Integer recurEvery);

    public abstract Integer getRangeType();

    public abstract void setRangeType(Integer rangeType);

    public abstract Integer getRangeValue();

    public abstract void setRangeValue(Integer rangeValue);

    public abstract Integer getRuleType();

    public abstract void setRuleType(Integer ruleType);

    public abstract String getRuleValue();

    public abstract void setRuleValue(String ruleValue);

    public abstract Integer getRuleValueType();

    public abstract void setRuleValueType(Integer ruleValueType);

    public abstract Appointment getAppointment();

    public abstract void setAppointment(Appointment appointment);

    public abstract Collection getExceptions();

    public abstract void setExceptions(Collection exceptions);
}
