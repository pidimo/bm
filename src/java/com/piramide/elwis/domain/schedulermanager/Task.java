package com.piramide.elwis.domain.schedulermanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 6, 2005
 * Time: 3:30:51 PM
 * To change this template use File | Settings | File Templates.
 */

public interface Task extends EJBLocalObject {

    Integer getTaskId();

    void setTaskId(Integer taskId);

    String getTitle();

    void setTitle(String title);

    Integer getStartDate();

    void setStartDate(Integer startDate);

    Integer getExpireDate();

    void setExpireDate(Integer expireDate);

    String getExpireTime();

    void setExpireTime(String expireTime);

    Integer getStatus();

    void setStatus(Integer status);

    Integer getPriorityId();

    void setPriorityId(Integer priorityId);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    Integer getUserId();

    void setUserId(Integer userId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer version);

    SchedulerFreeText getSchedulerFreeText();

    void setSchedulerFreeText(SchedulerFreeText schedulerFreeText);

    /*Collection getScheludeEmployees();

    void setScheludeEmployees(Collection scheludeEmployees);*/

    Long getExpireDateTime();

    void setExpireDateTime(Long expireDateTime);

    Collection getScheduledUsers();

    void setScheduledUsers(Collection scheduledUsers);

    Boolean getNotification();

    void setNotification(Boolean notification);

    ScheduledUser getTask();

    void setTask(ScheduledUser task);

    String getStartTime();

    void setStartTime(String startTime);

    Long getStartDateTime();

    void setStartDateTime(Long startDateTime);

    Integer getPercent();

    void setPercent(Integer percent);

    Integer getTaskTypeId();

    void setTaskTypeId(Integer taskTypeId);

    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Integer getProcessId();

    void setProcessId(Integer processId);

    Integer getActivityId();

    void setActivityId(Integer activityId);

    Long getCreateDateTime();

    void setCreateDateTime(Long createDateTime);

    Long getUpdateDateTime();

    void setUpdateDateTime(Long updateDateTime);
}
