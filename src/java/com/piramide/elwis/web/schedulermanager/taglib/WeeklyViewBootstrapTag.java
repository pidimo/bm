package com.piramide.elwis.web.schedulermanager.taglib;

import com.piramide.elwis.dto.schedulermanager.AppointmentCompountView;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.apache.struts.util.ResponseUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: lito
 * Date: Apr 07, 2015
 * Time: 11:12:20 AM
 * To change this template use File | Settings | File Templates.
 */

public class WeeklyViewBootstrapTag extends TableBaseBootstrapTag {
    private static final String WEEK_WIDTH = "14%";
    private static final String DAY_HEADER_WIDTH = "60px";
    private static final String APPOINTMENT_HEADER_WIDTH = "100%";

    private static final String WEEK_CELL_CSS = CALENDAR_CELL_CSS + " month_week_cell";
    private static final String MONTH_DAY_HEADER_CSS = "month_day_header";
    private static final String DIV_TABLE_RESPONSIVE_CSS = "table-responsive";
    private static final String WEEK_TIME_CELL_CSS = CALENDAR_WEEK_CELL + " month_week_cell week_time_cell";
    private static final String TD_HEIGHT_CSS = " tdWeekCellHeight";
    private static final String DIV_CONTAINER_CSS = " divAppContainer";
    private static final String DIV_FLOAT_CSS = " divAppFloat";
    private static final String DIV_APPALLDAY_CSS = " divAppAllDay";
    private static final String SPAN_TIME_LABEL_ICON_CSS = "weeklyIcon";

    public static final String DIV_DOTDOTDOT_KEY_CSS = " divDotdotdotKey";
    public static final String SPAN_DOTDOTDOT_KEY_CSS = " spanDotdotdotKey";

    private int week;
    private int year;

    private String dayClass;
    private static final String DAY_WIDTH = "14%";
    private static final String TIME_WIDTH = "2%";
    private Locale locale;

    public int doStartTag() throws JspException {
        initialize();


        Integer w = (Integer) pageContext.getRequest().getAttribute("week");
        Integer y = (Integer) pageContext.getRequest().getAttribute("year");

        week = w.intValue();
        year = y.intValue();
        //String day_title = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Scheduler.day");
        locale = JSPHelper.getLocale((HttpServletRequest) pageContext.getRequest());
        String weekDatePattern = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Scheduler.calendarView.weekdateformat");
        String weekDateWithYearPattern = null;
        if (week >= 52 || week == 1) {
            weekDateWithYearPattern = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Scheduler.calendarView.weekdatewithyearformat");
        }

        CalendarIU cal = new CalendarIU();
        cal.setWeek(week, year);
        CalendarIU.Week week = cal.getWeek();

        StringBuffer html = new StringBuffer();
        renderScroll(html, false);
        renderOpenDIV(html, DIV_TABLE_RESPONSIVE_CSS, null);
        renderOpenTABLE(html);

//        renderOpenTABLE(html);
        renderOpenTR(html, HEADER_CSS);

        //render time header cell
        String hour_title = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Scheduler.hour");
        renderOpenTH(html, null, null, TIME_WIDTH, true);
        html.append(hour_title);
        renderCloseTH(html);

        for (int i = 0; i < dayNames.length; i++) {
            renderOpenTH(html, null, null, DAY_WIDTH, false);
            html.append(dayNames[i]);
            renderCloseTH(html);
        }
        renderCloseTR(html);


        DateTime startDayOfWeek = week.getStartDate();
        int maxAppointmentsOfWeek = getAppointmentsSizeByDay(DateUtils.dateToInteger(week.getStartDate()));
        for (int i = 1; i <= 6; i++) {
            Integer startWeek = DateUtils.dateToInteger(startDayOfWeek.plus(Period.days(i)));
            int maxAppointments = getAppointmentsSizeByDay(startWeek);
            if (maxAppointments > maxAppointmentsOfWeek) {
                maxAppointmentsOfWeek = maxAppointments;
            }
        }

        //process appointments UI week view
        Map<String, Integer> workHourMap = calculateWeeklyStartEndWorkHours();
        log.debug("Calculated Work Hour:" + workHourMap);

        Integer weeklyWorkStartHour = workHourMap.get("weeklyWorkStartHour");
        Integer weeklyWorkEndHour = workHourMap.get("weeklyWorkEndHour");

        boolean isHeaderRendered = false;
        List<Map> timeIntervalList = getTimeIntervalList();
        Map<String, Map<Integer, List<WeeklyAppointmentUI>>> appointmentsByTimeMap = processAllWeekAppointmentsUI(week, weeklyWorkStartHour);

        for (int i = 0; i < timeIntervalList.size(); i++) {
            Map intervalMap = timeIntervalList.get(i);
            String key = (String) intervalMap.get("key");
            Integer timeHour = (Integer) intervalMap.get("hour");
            String timeLabel = (String) intervalMap.get("label");
            boolean isTimeInterval = "true".equals(intervalMap.get("isTimeInterval"));

            Map<Integer, List<WeeklyAppointmentUI>> appByDateMap = appointmentsByTimeMap.get(key);

            if (!isTimeInterval || (timeHour >= weeklyWorkStartHour && timeHour <= weeklyWorkEndHour)) {

                renderOpenTR(html);

                //render hour label
                String tdTimeCss = WEEK_TIME_CELL_CSS + (isTimeInterval ? TD_HEIGHT_CSS : "");
                renderOpenTD(html, tdTimeCss, null, TIME_WIDTH, true);
                html.append(timeLabel);
                renderCloseTD(html);

                //re-initialize week to iterate days
                CalendarIU.Week tempWeek = reinitializeCalendarWeek();

                while (tempWeek.hasNextDay()) {
                    CalendarIU.Day day = tempWeek.nextDay();

                    String tdCellCss = WEEK_CELL_CSS + (isTimeInterval ? TD_HEIGHT_CSS : "");
                    renderOpenTD(html, getCssColor(day, false, true), tdCellCss, null, DAY_WIDTH, false);

                    if (!isHeaderRendered) {
                        renderCellHeader(html, day, day.getYear() == year ? weekDatePattern : weekDateWithYearPattern);
                    }

                    if (appByDateMap != null) {
                        Integer today = day.toIntegerDate();

                        List<WeeklyAppointmentUI> appointmentUIList = appByDateMap.get(today);

                        if (appointmentUIList != null) {
                            renderWeekAppointments(html, appointmentUIList, today, isTimeInterval);
                        }
                    }
                    renderCloseTD(html);
                }

                isHeaderRendered = true;
                renderCloseTR(html);
            }
        }

        renderCloseTABLE(html);
        renderCloseDIV(html);
        html.append(BR);
        renderScroll(html, true);

        ResponseUtils.write(pageContext, html.toString());
        return SKIP_BODY;
    }


    private void renderCellHeader(StringBuffer html, CalendarIU.Day day, String weekDatePattern) {
        StringBuffer dayWeekTitle = new StringBuffer();
        renderOpenDIV(html, MONTH_DAY_HEADER_CSS, null);

        Date date = DateUtils.integerToDate(DateUtils.getInteger(day.getYear(), day.getMonth(), day.getDay()));
        String dateWeek = DateUtils.parseDate(date, weekDatePattern, locale);
        dayWeekTitle.append(dateWeek);

        renderHREF(html, getUrlDay(day.getYear(), day.getMonth(), day.getDay()),
                dayWeekTitle.toString(), RES_VIEWDAY, null, null);

        html.append(SPACE);

        String startDate = DateUtils.parseDate(date, datePattern);
        String calendarDate = day.toIntegerDate().toString();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        //Generate icon for add apointment
        renderAddURL(request, html, null, -1, null, startDate, calendarDate);

        renderCloseDIV(html);
        renderHolidays(html, day.getDay(), day.getMonth(), true);

    }

    private void renderWeekAppointments(StringBuffer html, List<WeeklyAppointmentUI> appointmentUIList, Integer today, boolean isTimeInterval) {
        int[] start_endWorkHour = processStart_EndWorkHour();

        if (appointmentUIList != null) {

            //render the app container
            renderOpenDIV(html, DIV_CONTAINER_CSS, null);

            for (WeeklyAppointmentUI weeklyAppointmentUI : appointmentUIList) {
                AppointmentCompountView appointmentView = getAppointment(weeklyAppointmentUI.getAppointmentViewId());

                if (appointmentView != null) {
                    String locale = null;
                    locale = appointmentView.getLocation();

                    //check for appointments with more of one day
                    if (!appointmentView.isAllDay() || !appointmentView.isSameDay()) {
                        appointmentView = appointmentView.cloneMe(today.intValue(), start_endWorkHour[0], start_endWorkHour[1]);
                    }
                    if (locale != null) {
                        appointmentView.setLocation(locale);
                    }

                    renderCellWeekAppointment(html, today.toString(), appointmentView, true, true, APPOINTMENT_CSS, weeklyAppointmentUI, isTimeInterval);
                }
            }

            renderCloseDIV(html);
        }
    }

    protected void renderCellWeekAppointment(StringBuffer html, String calendarDate, AppointmentCompountView appointment, boolean chop,
                                             boolean showToolTip, String styleClass, WeeklyAppointmentUI weeklyAppointmentUI, boolean isTimeInterval) {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        boolean onlyAnonymous = hasOnlyAnonymousAppPermission(appointment.isPublic());

        styleClass = composeStyleClassForAppointmentDiv(styleClass, onlyAnonymous, isTimeInterval);
        String styleAttribute = composeStyleAttributeForAppointmentDiv(appointment, onlyAnonymous, weeklyAppointmentUI, null);

        renderOpenDIV(html, styleClass, styleAttribute);

        renderOpenSPAN(html, SPAN_DOTDOTDOT_KEY_CSS);
        renderTimeLabelAndIcons(html, calendarDate, appointment, onlyAnonymous, showToolTip);
        renderCloseSPAN(html);

        html.append(BR);

        if (!onlyAnonymous) {
            StringBuffer titleApp = new StringBuffer();
            // Show the public label only if is PUBLIC
            if (!appointment.isPublic()) {
                renderOpenSPAN(titleApp, "type_appoiment");
                titleApp.append("[")
                        .append(RES_PRIVATE)
                        .append("]");
                renderCloseSPAN(titleApp);

                titleApp.append(BR);
            }

            renderModURL(request, html, titleApp, calendarDate, appointment, chop, showToolTip);
            html.append(SPACE);
        }

        renderCloseDIV(html);
    }

    protected String composeStyleClassForAppointmentDiv(String styleClass, boolean onlyAnonymous, boolean isTimeInterval) {
        StringBuilder value = new StringBuilder();
        if (styleClass != null) {
            value.append(styleClass);
        }

        value.append(" appointment_font");
        value.append(DIV_DOTDOTDOT_KEY_CSS);

        if (onlyAnonymous) {
            value.append(" ").append(APP_ANONYMOUS_CSS);
        }

        if (isTimeInterval) {
            value.append(DIV_FLOAT_CSS);
        } else {
            value.append(DIV_APPALLDAY_CSS);
        }

        return value.toString();
    }

    protected String composeStyleAttributeForAppointmentDiv(AppointmentCompountView appointment, boolean onlyAnonymous, WeeklyAppointmentUI weeklyAppointmentUI, String otherProperties) {
        StringBuilder value = new StringBuilder();
        if (!onlyAnonymous) {
            value.append("background: ").append(appointment.getColor()).append("; ");
        }

        value.append("width: ").append(weeklyAppointmentUI.getWidthInPercent()).append("%").append("; ");
        value.append("left: ").append(weeklyAppointmentUI.getLeftPositionInPercent()).append("%").append("; ");
        value.append("height: ").append(weeklyAppointmentUI.getHeightInPixels()).append("px").append("; ");
        value.append("z-index: ").append(weeklyAppointmentUI.getzIndex()).append("; ");

        if (otherProperties != null) {
            value.append(otherProperties);
        }

        return (value.length() > 0) ? "style=\"" + value.toString() + "\"" : null;
    }


    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getDayClass() {
        return dayClass;
    }

    public void setDayClass(String dayClass) {
        this.dayClass = dayClass;
    }

    protected void renderScrollOtherField(StringBuffer html) {
        renderOpenDIV(html,"row col-xs-12 col-sm-4 col-md-3 col-lg-3 wrapperButton", null);
        renderOpenDIV(html, "input-group", null);
        renderOpenSPAN(html, "input-group-addon input-sm");
        String yearTitle = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Appointment.Recurrence.year");
        html.append(yearTitle);
        renderCloseSPAN(html);
        renderInputText(html, "year", year, "4", "4");
        renderCloseDIV(html);
        renderCloseDIV(html);
    }

    protected String getCalendar() {
        return year + (week < 10 ? "0" : "") + week;
    }

    protected void renderScrollField(StringBuffer html) {
        String weekTitle = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Appointment.Recurrence.week");

        renderOpenSPAN(html, "input-group-addon input-sm");
        html.append(weekTitle);
        renderCloseSPAN(html);

        renderInputText(html, "week", week, "2", "2");
    }

    protected String getNextUrl() {
        int w = week;
        int y = year;
        DateTime today = new DateTime(y, 2, 1, 0, 0, 0, 0);
        if (w + 1 <= today.weekOfWeekyear().getMaximumValue()) {
            w++;
        } else {
            w = 1;
            y++;
        }
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer url = new StringBuffer(request.getContextPath());
        url.append(getUrlByType(new Integer(y * 100 + w)));
        return url.toString();
        /*return getUrlByType(new Integer(y * 100 + w);*/

    }

    protected String getPreviusUrl() {
        int w = week;
        int y = year;
        log.debug("-->W:" + w + "-Y:" + y);
        if (w - 1 > 0) {
            w--;
        } else {
            y--;
            DateTime backYear = new DateTime(y, 2, 1, 0, 0, 0, 0);
            log.debug("NEW DATE:" + backYear + "-MAxWeek:" + backYear.weekOfWeekyear().getMaximumValue());
            w = backYear.weekOfWeekyear().getMaximumValue();
        }
        log.debug("---->W:" + w + "-Y:" + y);
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer url = new StringBuffer(request.getContextPath());
        url.append(getUrlByType(new Integer(y * 100 + w)));
        return url.toString();
        /*return getUrlByType(new Integer(y * 100 + w));*/
    }

    protected int getType() {
        return 2;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Calculate the start an end work hour to weekly view
     * This is based in work day scheduler preferences, but if exist a appointment before or after of this configuration,
     * the appointment hours is take of.
     * @return Map
     */
    private Map<String, Integer> calculateWeeklyStartEndWorkHours() {
        Integer startWorkHour = workStartHour;
        Integer endWorkHour = workEndHour;

        if (startWorkHour > 0 || endWorkHour < 24) {
            CalendarIU.Week week = reinitializeCalendarWeek();
            while (week.hasNextDay()) {
                CalendarIU.Day day = week.nextDay();
                Integer today = day.toIntegerDate();

                List appointmentsDays = getAppointmentByDay(day.toIntegerDate());

                for (Iterator iterator = appointmentsDays.iterator(); iterator.hasNext(); ) {
                    String appointmentViewId = getAppointmentId(iterator.next());

                    AppointmentCompountView appointmentView = getAppointment(appointmentViewId);

                    Integer startDate = appointmentView.getStartDate().toIntegerDate();
                    int startHour = appointmentView.getStartTime().getHour();
                    int startMinute = appointmentView.getStartTime().getMinute();

                    Integer endDate = appointmentView.getEndDate().toIntegerDate();
                    int endHour = appointmentView.getEndTime().getHour();
                    int endMinute = appointmentView.getEndTime().getMinute();

                    DateTime startDateTime = DateUtils.integerToDateTime(startDate, startHour, startMinute);
                    DateTime endDateTime = DateUtils.integerToDateTime(endDate, endHour, endMinute);

                    if (!appointmentView.isAllDay()) {
                        Map infoMap = WeeklyAppointmentUI.calculateRealAppointmentUIStartEndTime(today, startDateTime, endDateTime, appointmentView.isAllDay());
                        Boolean realIsAllDay = (Boolean) infoMap.get("isAllDayAppointment");
                        DateTime realStartDateTime = (DateTime) infoMap.get("startTimeReal");
                        DateTime realEndDateTime = (DateTime) infoMap.get("endTimeReal");

                        if (!realIsAllDay) {
                            if (realStartDateTime.getHourOfDay() < startWorkHour) {
                                startWorkHour = realStartDateTime.getHourOfDay();
                            }

                            if (realEndDateTime.getHourOfDay() > endWorkHour) {
                                endWorkHour = realEndDateTime.getHourOfDay();
                            }
                        }
                    }
                }
            }
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("weeklyWorkStartHour", startWorkHour);
        map.put("weeklyWorkEndHour", endWorkHour);

        return map;
    }


    /**
     * Process all week appointments in view UI
     *
     * @param week week
     * @return Map structure Map<String, Map<Integer, List<WeeklyAppointmentUI>>>
     */
    private Map<String, Map<Integer, List<WeeklyAppointmentUI>>> processAllWeekAppointmentsUI(CalendarIU.Week week, Integer weeklyWorkStartHour) {
        Map<String, Map<Integer, List<WeeklyAppointmentUI>>> appointmentsByTimeMap = new HashMap<String, Map<Integer, List<WeeklyAppointmentUI>>>();

        while (week.hasNextDay()) {
            CalendarIU.Day day = week.nextDay();
            Integer today = day.toIntegerDate();

            List<WeeklyAppointmentUI> weeklyAppointmentUIList = getWeeklyAppointmentUIByDay(day, weeklyWorkStartHour);

            for (WeeklyAppointmentUI weeklyAppointmentUI : weeklyAppointmentUIList) {

                addAppointmentsByTime(appointmentsByTimeMap, weeklyAppointmentUI, today);
            }
        }

        return appointmentsByTimeMap;
    }

    /**
     * Get all AppointmentCompountView of this day and compose the respective WeeklyAppointmentUI
     * @param day day
     * @return List<WeeklyAppointmentUI>
     */
    private List<WeeklyAppointmentUI> getWeeklyAppointmentUIByDay(CalendarIU.Day day, Integer weeklyWorkStartHour) {
        List<WeeklyAppointmentUI> resultList = new ArrayList<WeeklyAppointmentUI>();

        List appointmentsDays = getAppointmentByDay(day.toIntegerDate());
        Integer today = day.toIntegerDate();

        for (Iterator iterator = appointmentsDays.iterator(); iterator.hasNext(); ) {
            String appointmentViewId = getAppointmentId(iterator.next());
            WeeklyAppointmentUI weeklyAppointmentUI = composeWeeklyAppointmentUI(appointmentViewId, today, weeklyWorkStartHour);

            resultList.add(weeklyAppointmentUI);
        }

        resultList = WeeklyAppointmentUI.checkDayOverlapAppointments(resultList);

        return resultList;
    }

    private WeeklyAppointmentUI composeWeeklyAppointmentUI(String appointmentViewId, Integer today, Integer weeklyWorkStartHour) {
        AppointmentCompountView appointmentView = getAppointment(appointmentViewId);

        Integer startDate = appointmentView.getStartDate().toIntegerDate();
        int startHour = appointmentView.getStartTime().getHour();
        int startMinute = appointmentView.getStartTime().getMinute();

        Integer endDate = appointmentView.getEndDate().toIntegerDate();
        int endHour = appointmentView.getEndTime().getHour();
        int endMinute = appointmentView.getEndTime().getMinute();

        DateTime startDateTime = DateUtils.integerToDateTime(startDate, startHour, startMinute);
        DateTime endDateTime = DateUtils.integerToDateTime(endDate, endHour, endMinute);

        WeeklyAppointmentUI weeklyAppointmentUI = new WeeklyAppointmentUI(appointmentViewId, appointmentView.getTitle(), today, startDateTime, endDateTime, appointmentView.isAllDay(), weeklyWorkStartHour);

        return weeklyAppointmentUI;
    }


    /**
     * Add appointment in Map with interval Time as key
     *
     * @param appointmentsByTimeMap map
     * @param weeklyAppointmentUI
     * @param today
     */
    private void addAppointmentsByTime(Map<String, Map<Integer, List<WeeklyAppointmentUI>>> appointmentsByTimeMap, WeeklyAppointmentUI weeklyAppointmentUI, Integer today) {
        AppointmentCompountView appointmentView = getAppointment(weeklyAppointmentUI.getAppointmentViewId());

        if (appointmentView != null) {
            String intervalKey = findTimeIntervalKey(weeklyAppointmentUI, today);

            if (appointmentsByTimeMap.get(intervalKey) != null) {
                Map<Integer, List<WeeklyAppointmentUI>> appByDateMap = appointmentsByTimeMap.get(intervalKey);
                addAppointmentsByDate(appByDateMap, weeklyAppointmentUI, today);
            } else {
                Map<Integer, List<WeeklyAppointmentUI>> appByDateMap = new HashMap<Integer, List<WeeklyAppointmentUI>>();
                addAppointmentsByDate(appByDateMap, weeklyAppointmentUI, today);

                appointmentsByTimeMap.put(intervalKey, appByDateMap);
            }
        }
    }

    /**
     * Add appointment in Map with integer date as key
     *
     * @param appByDateMap        map
     * @param weeklyAppointmentUI
     * @param today
     */
    private void addAppointmentsByDate(Map<Integer, List<WeeklyAppointmentUI>> appByDateMap, WeeklyAppointmentUI weeklyAppointmentUI, Integer today) {

        if (appByDateMap.get(today) != null) {
            appByDateMap.get(today).add(weeklyAppointmentUI);
        } else {
            List<WeeklyAppointmentUI> weeklyAppointmentUIList = new ArrayList<WeeklyAppointmentUI>();
            weeklyAppointmentUIList.add(weeklyAppointmentUI);

            appByDateMap.put(today, weeklyAppointmentUIList);
        }
    }

    /**
     * Find the time interval for this appointment
     *
     * @param weeklyAppointmentUI weeklyAppointmentUI
     * @param today           day
     * @return Time interval
     */
    private String findTimeIntervalKey(WeeklyAppointmentUI weeklyAppointmentUI, Integer today) {
        String intervalKey = null;

        List<Map> timeIntervalList = getTimeIntervalList();

        if (!weeklyAppointmentUI.isAllDay()) {

            Integer appointmentStartDate = DateUtils.dateToInteger(weeklyAppointmentUI.getUiStartDateTime());
            int appointmentStartHour = weeklyAppointmentUI.getUiStartDateTime().getHourOfDay();
            int appointmentStartMinute = weeklyAppointmentUI.getUiStartDateTime().getMinuteOfHour();

            //iterate from index 1 because index 0 is for all day
            for (int i = 1; i < timeIntervalList.size(); i++) {
                Map startIntervalMap = timeIntervalList.get(i);


                int startHour = (Integer) startIntervalMap.get("hour");
                int startMinute = (Integer) startIntervalMap.get("minute");

                if (i < timeIntervalList.size() - 1) {
                    Map endIntervalMap = timeIntervalList.get(i + 1);
                    int endHour = (Integer) endIntervalMap.get("hour");
                    int endMinute = (Integer) endIntervalMap.get("minute");

                    if (appointmentStartDate.equals(today) && appointmentStartHour == startHour && (appointmentStartMinute == startMinute || (appointmentStartMinute > startMinute && appointmentStartMinute < endMinute))) {
                        intervalKey = (String) startIntervalMap.get("key");
                    }
                } else {

                    if (appointmentStartDate.equals(today) && appointmentStartHour == startHour && appointmentStartMinute == startMinute) {
                        intervalKey = (String) startIntervalMap.get("key");
                    }
                }
            }
        }

        if (intervalKey == null) {
            //if not found interval, this is a large appointment of many days
            Map startIntervalMap = timeIntervalList.get(0);
            intervalKey = (String) startIntervalMap.get("key");
        }
        return intervalKey;
    }

    /**
     * Get the time intervals for week UI
     *
     * @return List<Map>
     */
    private List<Map> getTimeIntervalList() {
        List<Map> result = new ArrayList<Map>();

        //set all day interval in position 0
        String allDayMsg = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Scheduler.Appointment.allDay");
        Map allDayMap = new HashMap();
        allDayMap.put("key", "allDays");
        allDayMap.put("label", allDayMsg);
        allDayMap.put("isTimeInterval", "false");
        result.add(allDayMap);

        DateTime currentDateTime = new DateTime();
        DateTime startTime = currentDateTime.withTime(0, 0, 0, 0);
        DateTime endTime = currentDateTime.withTime(23, 59, 59, 0);

        while (startTime.isBefore(endTime)) {
            int hour = startTime.getHourOfDay();
            int minute = startTime.getMinuteOfHour();

            String timeKey = getValidTime(hour) + ":" + getValidTime(minute);
            String timeLabel = "";
            if (minute == 0 || minute == 30) {
                timeLabel = timeKey;
            }

            Map map = new HashMap();
            map.put("key", timeKey);
            map.put("label", timeLabel);
            map.put("hour", hour);
            map.put("minute", minute);
            map.put("isTimeInterval", "true");
            result.add(map);

            startTime = startTime.plusMinutes(WeeklyAppointmentUI.UI_MINUTE_INTERVAL);
        }
        return result;
    }

    private CalendarIU.Week reinitializeCalendarWeek() {
        //re-initialize week to iterate days
        CalendarIU tempCal = new CalendarIU();
        tempCal.setWeek(this.week, year);
        CalendarIU.Week tempWeek = tempCal.getWeek();
        return tempWeek;
    }

    @Override
    protected String getTimeLabelIconClass() {
        return SPAN_TIME_LABEL_ICON_CSS;
    }

}
