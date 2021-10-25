package com.piramide.elwis.dto.schedulermanager;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: May 2, 2005
 * Time: 12:22:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppointmentData {

    private Integer id;
    private String title;
    private int priority;
    private boolean hasReminder;
    private String startTime;
    private String endTime;
    private boolean isPrivate;

    public AppointmentData(Integer id, String title, String startTime, String endTime, int priority, boolean hasReminder, boolean isPrivate) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;

        this.priority = priority;
        this.hasReminder = hasReminder;
        this.isPrivate = isPrivate;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isHasReminder() {
        return hasReminder;
    }

    public void setHasReminder(boolean hasReminder) {
        this.hasReminder = hasReminder;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }


}
