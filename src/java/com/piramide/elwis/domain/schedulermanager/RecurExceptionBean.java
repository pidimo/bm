package com.piramide.elwis.domain.schedulermanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: RecurExceptionBean.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 12-04-2005 03:24:13 PM Fernando Montaño Exp $
 */


public abstract class RecurExceptionBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        setRecurExceptionId(PKGenerator.i.nextKey(SchedulerConstants.TABLE_RECUREXCEPTION));
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

    public abstract Integer getRecurExceptionId();

    public abstract void setRecurExceptionId(Integer recurExceptionId);

    public abstract Integer getAppointmentId();

    public abstract void setAppointmentId(Integer appointmentId);

    public abstract Integer getDateValue();

    public abstract void setDateValue(Integer dateValue);

    public abstract Appointment getAppointment();

    public abstract void setAppointment(Appointment appointment);

    public abstract Recurrence getRecurrence();

    public abstract void setRecurrence(Recurrence recurrence);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);
}
