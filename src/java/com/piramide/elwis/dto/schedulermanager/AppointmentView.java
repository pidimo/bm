package com.piramide.elwis.dto.schedulermanager;

import com.piramide.elwis.utils.DateUtils;
import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Apr 12, 2005
 * Time: 12:13:35 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AppointmentView {
    protected AppointmentDate startDate;
    protected AppointmentDate endDate;

    protected AppointmentTime startTime;
    protected AppointmentTime endTime;

    private boolean isPublic;
    private boolean isRecurrent;
    private String color;
    protected Integer id;
    private String virtualId;
    private String title;
    private boolean isAllDay;
    private boolean isOwner;
    private String location;
    private int reminderType;
    private String timeBefore;
    private String typeName;
    private Integer userId;
    private String ownerUserName;

    protected AppointmentTime realEndTime;

    AppointmentView(DateTime startDateTime, DateTime endDateTime, boolean isOwner, boolean isRecurrent, boolean isPublic, boolean isAllDay) {
        setStartDateTime(startDateTime);
        setEndDateTime(endDateTime);
        this.isAllDay = isAllDay;
        this.isPublic = isPublic;
        this.isOwner = isOwner;
        this.isRecurrent = isRecurrent;
        virtualId = null;
    }

    AppointmentView(boolean isOwner, boolean isRecurrent, boolean isPublic, boolean isAllDay) {
        this.isAllDay = isAllDay;
        this.isPublic = isPublic;
        this.isOwner = isOwner;
        this.isRecurrent = isRecurrent;
        virtualId = null;
    }


    public void setVirtualId(long c) {
        virtualId = virtualId + "-" + c;
    }

    public void initialize(Integer id, String title, String color, String name) {
        this.id = id;
        this.title = title;
        this.color = color;
        this.typeName = name;
        virtualId = id.toString();
    }

    AppointmentView(AppointmentDate sd, AppointmentDate ed, boolean isPublic) {
        startDate = sd;
        endDate = ed;
        this.isPublic = isPublic;
    }

    public abstract String getTimeLabel(boolean is24h);

    public boolean inRangeDay(Integer date) {
        return getStartDate().toIntegerDate().intValue() < date.intValue() && date.intValue() < getEndDate().toIntegerDate().intValue();
    }

    public boolean isLimitRangeDay(Integer date) {
        return getStartDate().toIntegerDate().intValue() == date.intValue() || date.intValue() == getEndDate().toIntegerDate().intValue();
    }

    public boolean isSameDay() {
        return startDate.equals(endDate);
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    public int minutesDuration() {
        return endTime.toMinutes() - startTime.toMinutes();
    }

    public int minutesDuration(int workEndHour) {
        return workEndHour * 60 - startTime.toMinutes();
    }

    public AppointmentTime newAppointmentTime(int h, int m) {
        return new AppointmentTime(h, m);
    }

    protected String get24TimeFormat(int hour, int minute) {
        StringBuffer time = new StringBuffer();
        if (hour < 10) {
            time.append("0");
        }
        time.append(hour).append(":");
        if (minute < 10) {
            time.append("0");
        }
        time.append(minute);
        return time.toString();
    }

    protected String get12TimeFormat(int hours, int minute) {
        StringBuffer time = new StringBuffer();
        boolean isPM = hours > 11;
        int hour = hours > 12 ? hours - 12 : hours;
        if (hour == 0 || hour == -12) {
            time.append("12");
        } else {
            hour = Math.abs(hour);
            if (hour < 10) {
                time.append("0");
            }
            time.append(hour);
        }
        time.append(":");
        if (minute < 10) {
            time.append("0");
        }
        time.append(minute);
        time.append(isPM ? " pm" : " am");
        return time.toString();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String name) {
        this.typeName = name;
    }

    public String getId() {
        return virtualId;
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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean aOwner) {
        isOwner = aOwner;
    }

    public boolean isRecurrent() {
        return isRecurrent;
    }

    public void setRecurrent(boolean recurrent) {
        isRecurrent = recurrent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location != null ? location : "";
    }

    public int getReminderType() {
        return reminderType;
    }

    public void setReminderType(int reminderType) {
        this.reminderType = reminderType;
    }

    public String getTimeBefore() {
        return timeBefore;
    }

    public void setTimeBefore(String timeBefore) {
        this.timeBefore = timeBefore;
    }

    public String toString() {
        return "AppointmentView{" +
                "title=" + title +
                ", startDate=" + startDate +
                //", isPublic=" + isPublic +
                " - startTime=" + get24TimeFormat(startTime.getHour(), startTime.getMinute()) +
                ", endDate=" + endDate +
                "- endTime=" + get24TimeFormat(endTime.getHour(), endTime.getMinute()) +
                ", isAllDay=" + isAllDay() +
                ", isSameDay=" + isSameDay() +
                "}\n";
    }

    /*public static AppointmentDate getInstance(int year, int month, int day){
        return new AppointmentDate(year, month, day);
    }*/


    public class AppointmentDate {
        private int day;
        private int month;
        private int year;

        public AppointmentDate(int date) {
            year = date / 10000;
            month = (date / 100) - year * 100;
            day = (date % 100);
        }

        public AppointmentDate(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AppointmentDate)) {
                return false;
            }

            final AppointmentDate appointmentDate = (AppointmentDate) o;

            if (day != appointmentDate.day) {
                return false;
            }
            if (month != appointmentDate.month) {
                return false;
            }
            if (year != appointmentDate.year) {
                return false;
            }

            return true;
        }

        public int compareTo(int y, int m, int d) {
            int date = DateUtils.getInteger(year, month, day).intValue();
            int date1 = DateUtils.getInteger(y, m, d).intValue();
            int res = 0;
            if (date < date1) {
                res = -1;
            }
            //else if(date == date1) res=0;
            else if (date > date1) {
                res = 1;
            }
            return res;
        }

        /**
         * Compare the current integer date with parameter
         *
         * @param d
         * @return If current Object > parameter -> 1<br>
         *         If current Object = parameter -> 0<br>
         *         If current Object < parameter -> -1
         */
        public int compareTo(Integer d) {
            int date = DateUtils.getInteger(year, month, day).intValue();
            int date1 = d.intValue();
            int res = 0;
            if (date > date1) {
                res = 1;
            } else if (date < date1) {
                res = -1;
            }
            return res;
        }

        public Integer toIntegerDate() {
            return DateUtils.getInteger(year, month, day);
        }

        public Integer getDateAsInteger() {
            return toIntegerDate();
        }

        public Date toDate() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, day);
            return calendar.getTime();
        }


        public int hashCode() {
            int result;
            result = day;
            result = 29 * result + month;
            result = 29 * result + year;
            return result;
        }


        public String toString() {
            return "AppointmentDate{" +
                    "day=" + day +
                    ", month=" + month +
                    ", year=" + year +
                    "}";
        }
    }


    public class AppointmentTime {
        int minute;
        int hour;

        public AppointmentTime(String time) {
            String[] i = time.split(":");
            hour = Integer.parseInt(i[0]);
            minute = Integer.parseInt(i[1]);
        }

        public AppointmentTime(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public int toMinutes() {
            return hour * 60 + minute;
        }

        public int getMinute() {
            return minute;
        }

        public int getHour() {
            return hour;
        }

        public void changeTime(int h, int m) {
            hour = h;
            minute = m;
        }

        public String getTimeLabel() {
            StringBuffer s = new StringBuffer();
            if (hour < 10) {
                s.append("0");
            }
            s.append(hour).append(":");
            if (minute < 10) {
                s.append("0");
            }
            s.append(minute);
            return s.toString();
        }

        public String getTimeFormat(boolean as24Hour) {
            return as24Hour ? get24TimeFormat(hour, minute) : get12TimeFormat(hour, minute);
        }


        public AppointmentTime newAppointmentTime(int minuteInterval) {
            int h, m;
            if (minute + minuteInterval == 60) {
                h = hour + 1;
                m = 0;
            } else {
                h = hour;
                m = minute + minuteInterval;
            }
            return new AppointmentTime(h, m);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AppointmentTime)) {
                return false;
            }

            final AppointmentTime appointmentTime = (AppointmentTime) o;

            if (hour != appointmentTime.hour) {
                return false;
            }
            if (minute != appointmentTime.minute) {
                return false;
            }

            return true;
        }

        public int hashCode() {
            int result;
            result = minute;
            result = 29 * result + hour;
            return result;
        }

        public String toString() {
            return "Time{" +
                    "hour=" + hour +
                    ", minute=" + minute +
                    "}";
        }
    }

    public AppointmentDate getStartDate() {
        return startDate;
    }

    public AppointmentDate getEndDate() {
        return endDate;
    }

    public AppointmentTime getStartTime() {
        return startTime;
    }

    public AppointmentTime getEndTime() {
        return endTime;
    }

    public void setAllDay(boolean allDay) {
        isAllDay = allDay;
    }

    public void setStartDateTime(DateTime startDateTime) {
        startDate = new AppointmentDate(startDateTime.getYear(), startDateTime.getMonthOfYear(), startDateTime.getDayOfMonth());
        startTime = new AppointmentTime(startDateTime.getHourOfDay(), startDateTime.getMinuteOfHour());

    }

    public void setEndDateTime(DateTime endDateTime) {
        endDate = new AppointmentDate(endDateTime.getYear(), endDateTime.getMonthOfYear(), endDateTime.getDayOfMonth());
        endTime = new AppointmentTime(endDateTime.getHourOfDay(), endDateTime.getMinuteOfHour());
    }

    public AppointmentTime getRealEndTime() {
        return realEndTime;
    }

    public void setRealEndTime(int hour, int minute) {
        this.realEndTime = new AppointmentTime(hour, minute);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }
}
