package com.piramide.elwis.web.schedulermanager.taglib;

import com.piramide.elwis.dto.schedulermanager.AppointmentCompountView;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.apache.struts.util.ResponseUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Apr 11, 2005
 * Time: 6:12:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class WeeklyViewTag extends TableBaseTag {
    private static final String WEEK_WIDTH = "14%";
    private static final String DAY_HEADER_WIDTH = "60px";
    private static final String APPOINTMENT_HEADER_WIDTH = "100%";

    private static final String WEEK_CELL_CSS = CALENDAR_CELL_CSS + " month_week_cell";
    private static final String MONTH_DAY_HEADER_CSS = "month_day_header";

    private int week;
    private int year;

    private String dayClass;
    private static final String DAY_WIDTH = "14%";
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
        renderOpenTABLE(html);
        renderOpenTR(html, HEADER_CSS);
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
        renderOpenTR(html);
        int[] start_endWorkHour = processStart_EndWorkHour();

        while (week.hasNextDay()) {
            CalendarIU.Day day = week.nextDay();

            renderOpenTD(html, getCssColor(day, false, true), WEEK_CELL_CSS, null, DAY_WIDTH, false);
            renderCellHeader(html, day, day.getYear() == year ? weekDatePattern : weekDateWithYearPattern);
            List appointmentsDays = getAppointmentByDay(day.toIntegerDate());
            Integer today = day.toIntegerDate();
            for (Iterator iterator = appointmentsDays.iterator(); iterator.hasNext();) {
                String id = getAppointmentId(iterator.next());
                AppointmentCompountView appointmentView = getAppointment(id);
                if (appointmentView != null) {
                    String locale = null;
                    locale = appointmentView.getLocation();

                    if (!appointmentView.isAllDay() || !appointmentView.isSameDay()) {
                        appointmentView = appointmentView.cloneMe(today.intValue(), start_endWorkHour[0], start_endWorkHour[1]);
                    }
                    if (locale != null) {
                        appointmentView.setLocation(locale);
                    }
                    renderCellAppointment(html, today.toString(), appointmentView, true, true, APPOINTMENT_CSS);
                }
            }
            renderCloseTD(html);
        }

        renderCloseTR(html);
        renderCloseTABLE(html);
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
        html.append(SPACE);
        renderOpenSPAN(html, SCROLL_TEXT_CSS);
        String yearTitle = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Appointment.Recurrence.year");
        html.append(yearTitle);
        renderCloseSPAN(html);
        html.append(SPACE);
        renderInputText(html, "year", year, "4", "4");
        html.append(SPACE);
    }

    protected String getCalendar() {
        return year + (week < 10 ? "0" : "") + week;
    }

    protected void renderScrollField(StringBuffer html) {
        String weekTitle = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Appointment.Recurrence.week");
        renderOpenSPAN(html, SCROLL_TEXT_CSS);
        html.append(weekTitle);
        renderCloseSPAN(html);
        html.append(SPACE);
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
}
