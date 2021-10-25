package com.piramide.elwis.domain.schedulermanager;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: ScheduledUser.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 22-04-2005 02:33:30 PM Fernando Montaño Exp $
 */


public interface ScheduledUser extends EJBLocalObject {
    Integer getScheduledUserId();

    void setScheduledUserId(Integer scheduledUserId);

    Integer getAppointmentId();

    void setAppointmentId(Integer appointmentId);

    Integer getTaskId();

    void setTaskId(Integer taskId);

    Integer getUserId();

    void setUserId(Integer userId);

    Appointment getAppointment();

    void setAppointment(Appointment appointment);

    Task getTask();

    void setTask(Task task);

    Integer getUserGroupId();

    void setUserGroupId(Integer userGroupId);

    UserTask getUserTask();

    void setUserTask(UserTask userTask);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);
}
