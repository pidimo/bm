package com.piramide.elwis.web.schedulermanager.taglib;

import com.piramide.elwis.utils.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pojo to manage UI appointments in Weekly view
 * util to calculate the height and positions in week calendar matrix
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.5
 */
public class WeeklyAppointmentUI {
    protected static Log log = LogFactory.getLog(WeeklyAppointmentUI.class);

    public static final int UI_MINUTE_INTERVAL = 15;
    private static final int INTERVAL_HEIGHT_PIXELS = 21;
    public static final int Z_INDEX = 100;

    private String appointmentViewId;
    private String title;
    private boolean isAllDay;
    private Integer startInterval;
    private Integer endInterval;
    private Integer height;
    private Integer overlapCount;
    private boolean isLastOverlap;
    private Integer zIndex;
    private Integer leftPosition;

    private DateTime uiStartDateTime;
    private DateTime uiEndDateTime;

    private Integer weeklyWorkStartHour;


    public WeeklyAppointmentUI(String appointmentViewId, String title, Integer today, DateTime startDateTime, DateTime endDateTime, boolean isAllDay, Integer weeklyWorkStartHour) {
        this.appointmentViewId = appointmentViewId;
        this.title = title;
        this.isAllDay = isAllDay;
        this.weeklyWorkStartHour = weeklyWorkStartHour;
        this.height = 2;
        this.overlapCount = 0;
        this.isLastOverlap = false;
        this.zIndex = Z_INDEX;
        this.leftPosition = 0;

        setDefaults();

        if (!isAllDay) {
            initialize(today, startDateTime, endDateTime);
        }
    }

    private void setDefaults() {
        startInterval = 0;
        endInterval = 0;
    }

    private void initialize(Integer today, DateTime startDateTime, DateTime endDateTime) {

        Map infoMap = calculateRealAppointmentUIStartEndTime(today, startDateTime, endDateTime, isAllDay);
        this.isAllDay = (Boolean) infoMap.get("isAllDayAppointment");
        this.uiStartDateTime = (DateTime) infoMap.get("startTimeReal");
        this.uiEndDateTime = (DateTime) infoMap.get("endTimeReal");

        //calculate intervals
        if (!isAllDay) {
            startInterval = calculateIntervalPosition(uiStartDateTime);
            endInterval = calculateIntervalPosition(uiEndDateTime);
            height = endInterval - startInterval;
        }
    }

    /**
     * Calculate the real times in UI for the appointment in this day
     * @param today today
     * @param startDateTime appointment start
     * @param endDateTime appointment end
     * @return Map
     */
    public static Map calculateRealAppointmentUIStartEndTime(Integer today, DateTime startDateTime, DateTime endDateTime, boolean isAllDayAppointment) {
        DateTime realStart = null;
        DateTime realEnd = null;

        if (!isAllDayAppointment) {

            if (startDateTime.isBefore(endDateTime)) {
                Integer startDateInteger = DateUtils.dateToInteger(startDateTime);
                Integer endDateInteger = DateUtils.dateToInteger(endDateTime);

                if (today > startDateInteger && today < endDateInteger) {
                    isAllDayAppointment = true;

                } else if (startDateInteger.intValue() == today && today < endDateInteger ) {
                    realStart = startDateTime;
                    realEnd = DateUtils.integerToDateTime(today).withTime(23, 59, 59, 0);

                } else if (endDateInteger.intValue() == today && today > startDateInteger) {
                    realStart = DateUtils.integerToDateTime(today).withTime(0, 0, 0, 0);
                    realEnd = endDateTime;

                } else {
                    realStart = startDateTime;
                    realEnd = endDateTime;
                }
            }
        }

        Map infoMap = new HashMap();
        infoMap.put("isAllDayAppointment", isAllDayAppointment);
        infoMap.put("startTimeReal", realStart);
        infoMap.put("endTimeReal", realEnd);

        return infoMap;
    }

    private Integer calculateIntervalPosition(DateTime dateTime) {
        Integer pos = 0;
        if (dateTime != null) {

            if (weeklyWorkStartHour == null) {
                weeklyWorkStartHour = 0;
            }

            DateTime positionDateTime = dateTime.withTime(weeklyWorkStartHour, 0, 0, 0);

            while (positionDateTime.isBefore(dateTime)) {
                pos++;
                positionDateTime = positionDateTime.plusMinutes(UI_MINUTE_INTERVAL);
            }
        }
        return pos;
    }

    public String getAppointmentViewId() {
        return appointmentViewId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    public void setIsAllDay(boolean isAllDay) {
        this.isAllDay = isAllDay;
    }

    public DateTime getUiStartDateTime() {
        return uiStartDateTime;
    }

    public DateTime getUiEndDateTime() {
        return uiEndDateTime;
    }

    public Integer getStartInterval() {
        return startInterval;
    }

    public void setStartInterval(Integer startInterval) {
        this.startInterval = startInterval;
    }

    public Integer getEndInterval() {
        return endInterval;
    }

    public void setEndInterval(Integer endInterval) {
        this.endInterval = endInterval;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getOverlapCount() {
        return overlapCount;
    }

    public void setOverlapCount(Integer overlapCount) {
        this.overlapCount = overlapCount;
    }

    public boolean isLastOverlap() {
        return isLastOverlap;
    }

    public void setIsLastOverlap(boolean isLastOverlap) {
        this.isLastOverlap = isLastOverlap;
    }

    public Integer getzIndex() {
        return zIndex;
    }

    public void setzIndex(Integer zIndex) {
        this.zIndex = zIndex;
    }

    public Integer getLeftPosition() {
        return leftPosition;
    }

    public void setLeftPosition(Integer leftPosition) {
        this.leftPosition = leftPosition;
    }

    public Integer getHeightInPixels() {
        return height * INTERVAL_HEIGHT_PIXELS;
    }

    public Integer getWidthInPercent() {
        Integer cardWidth = 100;
        if (overlapCount > 1) {
            Integer partialWidth = 100 / overlapCount;

            if (isLastOverlap) {
                cardWidth = partialWidth;
            } else {
                //add the 60% of the your width
                Integer additional = (partialWidth * 60) / 100;
                cardWidth = partialWidth + additional;
            }
        }
        return cardWidth;
    }

    public Integer getLeftPositionInPercent() {
        Integer left = 0;
        if (overlapCount > 1) {
            left = (100 / overlapCount) * leftPosition;
        }
        return left;
    }


    /**
     * check if some appointments overlaps with other and process this
     * @param dayWeeklyAppointmentUIList
     * @return list
     */
    public static List<WeeklyAppointmentUI> checkDayOverlapAppointments(List<WeeklyAppointmentUI> dayWeeklyAppointmentUIList) {
        List<List<WeeklyAppointmentUI>> overlapList = new ArrayList<List<WeeklyAppointmentUI>>();
        List<WeeklyAppointmentUI> allDayList = new ArrayList<WeeklyAppointmentUI>();
        List<WeeklyAppointmentUI> resultList = new ArrayList<WeeklyAppointmentUI>();

        for (WeeklyAppointmentUI weeklyAppointmentUI : dayWeeklyAppointmentUIList) {
            if (weeklyAppointmentUI.isAllDay()) {
                allDayList.add(weeklyAppointmentUI);
            } else {
                addInOverlapList(weeklyAppointmentUI, overlapList);
            }
        }

        //compose the result list
        resultList.addAll(allDayList);

        //process overlap appointments
        for (List<WeeklyAppointmentUI> appointmentUIList : overlapList) {

            if (appointmentUIList.size() > 1) {
                for (int j = 0; j < appointmentUIList.size(); j++) {
                    WeeklyAppointmentUI weeklyAppointmentUI = appointmentUIList.get(j);
                    weeklyAppointmentUI.setOverlapCount(appointmentUIList.size());
                    weeklyAppointmentUI.setLeftPosition(j);
                    weeklyAppointmentUI.setzIndex(WeeklyAppointmentUI.Z_INDEX + j);

                    if (j == appointmentUIList.size() - 1) {
                        weeklyAppointmentUI.setIsLastOverlap(true);
                    }
                }

                resultList.addAll(appointmentUIList);
            } else {
                resultList.addAll(appointmentUIList);
            }
        }

        return resultList;
    }

    private static void addInOverlapList(WeeklyAppointmentUI weeklyAppointmentUI, List<List<WeeklyAppointmentUI>> overlapList) {
        boolean isOverlap = false;

        for (int i = 0; i < overlapList.size(); i++) {
            List<WeeklyAppointmentUI> weeklyAppointmentUIList = overlapList.get(i);
            for (int j = 0; j < weeklyAppointmentUIList.size(); j++) {
                WeeklyAppointmentUI appointmentUI = weeklyAppointmentUIList.get(j);

                isOverlap = isAppointmentOverlayed(appointmentUI, weeklyAppointmentUI);
                if (isOverlap) {
                    break;
                }
            }

            if (isOverlap) {
                weeklyAppointmentUIList.add(weeklyAppointmentUI);
                break;
            }
        }

        if (!isOverlap) {
            List<WeeklyAppointmentUI> list = new ArrayList<WeeklyAppointmentUI>();
            list.add(weeklyAppointmentUI);

            overlapList.add(list);
        }
    }

    private static boolean isAppointmentOverlayed(WeeklyAppointmentUI weeklyAppointmentUI1 , WeeklyAppointmentUI weeklyAppointmentUI2) {
        boolean isOverlap = false;
        if (weeklyAppointmentUI2.getStartInterval() >= weeklyAppointmentUI1.getStartInterval() && weeklyAppointmentUI2.getStartInterval() < weeklyAppointmentUI1.getEndInterval()) {
            isOverlap = true;
        }
        return isOverlap;
    }

    @Override
    public String toString() {
        return "WeeklyAppointmentUI " +
                ", title=" + title +
                ", isAllDay=" + isAllDay +
                ", uiStartDateTime=" + uiStartDateTime +
                ", uiEndDateTime=" + uiEndDateTime +
                ", weeklyWorkStartHour=" + weeklyWorkStartHour +
                ", startInterval=" + startInterval +
                ", endInterval=" + endInterval +
                ", overlapCount=" + overlapCount +
                ", isLastOverlap=" + isLastOverlap +
                ", zIndex=" + zIndex +
                ", leftPosition=" + leftPosition +
                "";
    }
}
