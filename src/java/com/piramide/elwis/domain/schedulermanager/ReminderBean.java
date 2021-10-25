package com.piramide.elwis.domain.schedulermanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 6, 2005
 * Time: 3:33:53 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class ReminderBean implements EntityBean {

    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        setAppointmentId(PKGenerator.i.nextKey(SchedulerConstants.TABLE_REMINDER));
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

    public abstract void setAppointmentId(Integer reminderId);

    public abstract Integer getTimeBefore();

    public abstract void setTimeBefore(Integer occurrence);

    public abstract Integer getReminderType();

    public abstract void setReminderType(Integer value);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Appointment getAppointment();

    public abstract void setAppointment(Appointment appointment);

    public abstract String getData();

    public abstract void setData(String data);

    public abstract Long getNextTime();

    public abstract void setNextTime(Long nextTime);
}
