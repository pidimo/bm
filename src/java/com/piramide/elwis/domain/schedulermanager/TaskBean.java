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
 * Time: 3:30:50 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class TaskBean implements EntityBean {

    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setTaskId(PKGenerator.i.nextKey(SchedulerConstants.TABLE_TASK));
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

    public abstract Integer getTaskId();

    public abstract void setTaskId(Integer taskId);

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract Integer getStartDate();

    public abstract void setStartDate(Integer startDate);

    public abstract Integer getExpireDate();

    public abstract void setExpireDate(Integer expireDate);

    public abstract String getExpireTime();

    public abstract void setExpireTime(String expireTime);

    public abstract Integer getStatus();

    public abstract void setStatus(Integer status);

    public abstract Integer getPriorityId();

    public abstract void setPriorityId(Integer priorityId);

    public abstract Integer getFreeTextId();

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract SchedulerFreeText getSchedulerFreeText();

    public abstract void setSchedulerFreeText(SchedulerFreeText schedulerFreeText);

    /*public abstract Collection getScheludeEmployees();

    public abstract void setScheludeEmployees(Collection scheludeEmployees);
*/

    public abstract Long getExpireDateTime();

    public abstract void setExpireDateTime(Long expireDateTime);

    public abstract Collection getScheduledUsers();

    public abstract void setScheduledUsers(Collection scheduledUsers);

    public abstract Boolean getNotification();

    public abstract void setNotification(Boolean notification);

    public abstract ScheduledUser getTask();

    public abstract void setTask(ScheduledUser task);

    public abstract String getStartTime();

    public abstract void setStartTime(String startTime);

    public abstract Long getStartDateTime();

    public abstract void setStartDateTime(Long startDateTime);

    public abstract Integer getPercent();

    public abstract void setPercent(Integer percent);

    public abstract Integer getTaskTypeId();

    public abstract void setTaskTypeId(Integer taskTypeId);

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Integer getProcessId();

    public abstract void setProcessId(Integer processId);

    public abstract Integer getActivityId();

    public abstract void setActivityId(Integer activityId);

    public abstract Long getCreateDateTime();

    public abstract void setCreateDateTime(Long createDateTime);

    public abstract Long getUpdateDateTime();

    public abstract void setUpdateDateTime(Long updateDateTime);
}
