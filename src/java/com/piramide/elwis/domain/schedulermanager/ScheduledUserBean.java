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
 * @version $Id: ScheduledUserBean.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 22-04-2005 02:33:18 PM Fernando Montaño Exp $
 */


public abstract class ScheduledUserBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        setScheduledUserId(PKGenerator.i.nextKey(SchedulerConstants.TABLE_SCHEDULEDUSER));
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


    public abstract Integer getScheduledUserId();

    public abstract void setScheduledUserId(Integer scheduledUserId);

    public abstract Integer getAppointmentId();

    public abstract void setAppointmentId(Integer appointmentId);

    public abstract Integer getTaskId();

    public abstract void setTaskId(Integer taskId);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Appointment getAppointment();

    public abstract void setAppointment(Appointment appointment);

    public abstract Task getTask();

    public abstract void setTask(Task task);

    public abstract Integer getUserGroupId();

    public abstract void setUserGroupId(Integer userGroupId);

    public abstract UserTask getUserTask();

    public abstract void setUserTask(UserTask userTask);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);
}
