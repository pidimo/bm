package com.piramide.elwis.domain.schedulermanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 6, 2005
 * Time: 3:33:53 PM
 * To change this template use File | Settings | File Templates.
 */

public interface Reminder extends EJBLocalObject {

    Integer getAppointmentId();

    void setAppointmentId(Integer reminderId);

    Integer getTimeBefore();

    void setTimeBefore(Integer occurrence);

    Integer getReminderType();

    void setReminderType(Integer value);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Appointment getAppointment();

    void setAppointment(Appointment appointment);

    String getData();

    void setData(String data);

    Long getNextTime();

    void setNextTime(Long nextTime);
}
