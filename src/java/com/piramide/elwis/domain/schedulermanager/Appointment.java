package com.piramide.elwis.domain.schedulermanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 6, 2005
 * Time: 3:34:40 PM
 * To change this template use File | Settings | File Templates.
 */

public interface Appointment extends EJBLocalObject {

    Integer getAppointmentId();

    void setAppointmentId(Integer appoinmentId);

    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Integer getUserId();

    void setUserId(Integer userId);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    String getTitle();

    void setTitle(String title);

    Integer getStartDate();

    void setStartDate(Integer startDate);

    String getStartTime();

    void setStartTime(String startTime);

    Integer getEndDate();

    void setEndDate(Integer endDate);

    String getEndTime();

    void setEndTime(String endTime);

    Integer getAppointmentTypeId();

    void setAppointmentTypeId(Integer appoinmentTypeId);

    String getLocation();

    void setLocation(String location);

    Boolean getIsPrivate();

    void setIsPrivate(Boolean isPrivate);

    Integer getPriorityId();

    void setPriorityId(Integer priorityId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer version);

    Boolean getIsRecurrence();

    void setIsRecurrence(Boolean isRecurrence);

    SchedulerFreeText getSchedulerFreeText();

    void setSchedulerFreeText(SchedulerFreeText schedulerFreeText);

    /*Collection getScheduleEmployee();

    void setScheduleEmployee(Collection scheduleEmployee);*/

    Reminder getReminder();

    void setReminder(Reminder reminder);

    Long getStartDateTime();

    void setStartDateTime(Long startDateTime);

    Long getEndDateTime();

    void setEndDateTime(Long endDateTime);

    Collection getScheludedUsers();

    void setScheludedUsers(Collection scheludedUsers);

    Recurrence getRecurrence();

    void setRecurrence(Recurrence recurrence);

    Boolean getIsAllDay();

    void setIsAllDay(Boolean isAllDay);

    Integer getCreatedById();

    void setCreatedById(Integer createdById);
}
