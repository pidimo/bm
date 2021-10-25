package com.piramide.elwis.domain.schedulermanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 6, 2005
 * Time: 3:34:40 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class AppointmentBean implements EntityBean {

    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setAppointmentId(PKGenerator.i.nextKey(SchedulerConstants.TABLE_APPOINTMENT));
        setVersion(new Integer(1));
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

    public abstract void setAppointmentId(Integer appoinmentId);

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Integer getFreeTextId();

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract Integer getStartDate();

    public abstract void setStartDate(Integer startDate);

    public abstract String getStartTime();

    public abstract void setStartTime(String startTime);

    public abstract Integer getEndDate();

    public abstract void setEndDate(Integer endDate);

    public abstract String getEndTime();

    public abstract void setEndTime(String endTime);

    public abstract Integer getAppointmentTypeId();

    public abstract void setAppointmentTypeId(Integer appoinmentTypeId);

    public abstract String getLocation();

    public abstract void setLocation(String location);

    public abstract Boolean getIsPrivate();

    public abstract void setIsPrivate(Boolean isPrivate);

    public abstract Integer getPriorityId();

    public abstract void setPriorityId(Integer priorityId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Boolean getIsRecurrence();

    public abstract void setIsRecurrence(Boolean isRecurrence);

    public abstract SchedulerFreeText getSchedulerFreeText();

    public abstract void setSchedulerFreeText(SchedulerFreeText schedulerFreeText);

    /*public abstract Collection getScheduleEmployee();

    public abstract void setScheduleEmployee(Collection scheduleEmployee);*/

    public abstract Reminder getReminder();

    public abstract void setReminder(Reminder reminder);

    public abstract Long getStartDateTime();

    public abstract void setStartDateTime(Long startDateTime);

    public abstract Long getEndDateTime();

    public abstract void setEndDateTime(Long endDateTime);

    public abstract Collection getScheludedUsers();

    public abstract void setScheludedUsers(Collection scheludedUsers);

    public abstract Recurrence getRecurrence();

    public abstract void setRecurrence(Recurrence recurrence);

    public abstract Boolean getIsAllDay();

    public abstract void setIsAllDay(Boolean isAllDay);

    public abstract Integer getCreatedById();

    public abstract void setCreatedById(Integer createdById);
}
