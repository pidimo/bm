package com.piramide.elwis.dto.schedulermanager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Apr 13, 2005
 * Time: 6:57:50 PM
 * To change this template use File | Settings | File Templates.
 */

public class AppointmentCompountView extends AppointmentView {
    private boolean reminder;
    private String priority;
    private Log log = LogFactory.getLog(this.getClass());

    public AppointmentCompountView cloneMe(int today, int startHour, int endHour) {
        AppointmentCompountView clone = new AppointmentCompountView(
                isOwner(),
                isRecurrent(),
                isPublic(),
                isAllDay(),
                getPriority(),
                isReminder());

        clone.initialize(getRealId(), getTitle(), getColor(), getTypeName());
        clone.startDate = startDate;
        clone.endDate = endDate;
        clone.startTime = new AppointmentTime(startTime.hour, startTime.minute);
        clone.endTime = new AppointmentTime(endTime.hour, endTime.minute);
        clone.priority = priority;
        clone.reminder = reminder;
        clone.setReminderType(getReminderType());
        clone.setTimeBefore(getTimeBefore());
        clone.setUserId(getUserId());
        clone.setOwnerUserName(getOwnerUserName());

        int startDateInt = startDate.toIntegerDate().intValue();
        int endDateInt = endDate.toIntegerDate().intValue();

        log.debug("*****************************");
        log.debug("startDateInt:" + startDateInt + " - today:" + today + " -  endDateInt:" + endDateInt);
        log.debug("startTime:" + startTime + " - endTime:" + endTime);

        if (startDateInt == today && today < endDateInt) {
            log.debug("En endtime  ..." + endHour);
            //before change the end time, save this
            clone.realEndTime = endTime;

            clone.endTime.hour = endHour;
            clone.endTime.minute = 0;
        } else if (endDateInt == today && today > startDateInt) {
            log.debug("En startTime  ..." + startDateInt);
            if (endTime.hour > startHour) {
                clone.startTime.hour = startHour;
            } else {
                clone.startTime.hour = 0;
            }

            clone.startTime.minute = 0;
        } else if (startDate.toIntegerDate().intValue() < today && today < endDate.toIntegerDate().intValue()) {
            clone.setAllDay(true);
        }
        log.debug("*****************************");
        return clone;
    }

    public AppointmentCompountView(DateTime startDateTime, DateTime endDateTime, boolean isOwner, boolean isrecurrent, boolean isPublic, boolean isAllDay) {
        super(startDateTime, endDateTime, isOwner, isrecurrent, isPublic, isAllDay);
    }


    public AppointmentCompountView(DateTime startDateTime, DateTime endDateTime, boolean isOwner, boolean isrecurrent, boolean isPublic, boolean isAllDay, String priority, boolean reminder) {
        super(startDateTime, endDateTime, isOwner, isrecurrent, isPublic, isAllDay);
        this.priority = priority;
        this.reminder = reminder;
    }

    public AppointmentCompountView(boolean isOwner, boolean isrecurrent, boolean isPublic, boolean isAllDay, String priority, boolean reminder) {
        super(isOwner, isrecurrent, isPublic, isAllDay);
        this.priority = priority;
        this.reminder = reminder;
    }

    public String getTimeLabel(boolean is24h) {
        log.debug(" ... getTimeLabel function execute ..." + is24h);
        StringBuffer buffer = new StringBuffer()
                .append(is24h ? get24TimeFormat(startTime.hour, startTime.minute) : get12TimeFormat(startTime.hour, startTime.minute))
                .append(" - ")
                .append(is24h ? get24TimeFormat(endTime.hour, endTime.minute) : get12TimeFormat(endTime.hour, endTime.minute));

        return buffer.toString();

    }

    public boolean isReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Integer getRealId() {
        return id;
    }
}
