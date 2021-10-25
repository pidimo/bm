package com.piramide.elwis.web.schedulermanager.taglib;

import com.piramide.elwis.dto.schedulermanager.AppointmentCompountView;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.apache.struts.util.ResponseUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Apr 11, 2005
 * Time: 6:12:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class MondlyViewBootstrapTag extends TableBaseBootstrapTag {
    private static final String DAY_WIDTH = "14%";
    private static final String WEEK_WIDTH = "2%";
    private static final String MONTH_WEEK_CELL_CSS = CALENDAR_WEEK_CELL + " month_week_cell";
    private static final String MONTH_CELL_CSS = CALENDAR_CELL_CSS + " month_week_cell";
    private static final String MONTH_DAY_HEADER_CSS = "month_day_header";

    private int month;
    private int year;


    public int doStartTag() throws JspException {
        initialize();
        String week_title = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Scheduler.week");
        String RES_VIEWWEEK = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Scheduler.Appointment.viewWeek");
        Integer m = (Integer) pageContext.getRequest().getAttribute("month");
        Integer y = (Integer) pageContext.getRequest().getAttribute("year");
        month = m.intValue();
        year = y.intValue();
        StringBuffer html = new StringBuffer();
        renderScroll(html, false);
        renderOpenDIV(html,"table-responsive",null);
        renderOpenTABLE(html);
        renderOpenTR(html, HEADER_CSS);
        renderOpenTH(html, null, null, WEEK_WIDTH, false);
        html.append(week_title);
        renderCloseTH(html);

        for (int i = 0; i < dayNames.length; i++) {
            renderOpenTH(html, null, null, DAY_WIDTH, false);
            html.append(dayNames[i]);
            renderCloseTH(html);
        }
        renderCloseTR(html);
        CalendarIU cal = new CalendarIU(month, year);
        Iterator weeks = cal.getIteratorWeek();
        int[] start_endWorkHour = processStart_EndWorkHour();

        while (weeks.hasNext()) {
            CalendarIU.Week week = (CalendarIU.Week) weeks.next();
            renderOpenTR(html);
            renderOpenTD(html, MONTH_WEEK_CELL_CSS, null, WEEK_WIDTH, false);
            //Render number week
            renderHREF(html, getUrlWeek(week.getWeekYearToUI(), week.getWeek()),
                    Integer.toString(week.getWeek()), RES_VIEWWEEK, null, null);

            renderCloseTD(html);
            DateTime startDayOfWeek = week.getStartDate();
            int maxAppointmentsOfWeek = getAppointmentsSizeByDay(DateUtils.dateToInteger(week.getStartDate()));
            for (int i = 1; i <= 6; i++) {
                Integer startWeek = DateUtils.dateToInteger(startDayOfWeek.plus(Period.days(i)));
                int maxAppointments = getAppointmentsSizeByDay(startWeek);
                if (maxAppointments > maxAppointmentsOfWeek) {
                    maxAppointmentsOfWeek = maxAppointments;
                }
            }

            while (week.hasNextDay()) {
                CalendarIU.Day day = week.nextDay();
                renderOpenTD(html, getCssColor(day), MONTH_CELL_CSS, null, DAY_WIDTH, false);
                renderCellHeader(html, day);

                List appointmentsDays = getAppointmentByDay(day.toIntegerDate());
                Integer today = day.toIntegerDate();

                for (Iterator iterator = appointmentsDays.iterator(); iterator.hasNext();) {
                    String id = getAppointmentId(iterator.next());
                    AppointmentCompountView appointmentView = getAppointment(id);
                    log.debug("APPOINTMNET:\n" + appointmentView);
                    if (appointmentView != null) {
                        String locale = null;
                        locale = appointmentView.getLocation();
                        if (!appointmentView.isAllDay() || !appointmentView.isSameDay()) {
                            appointmentView = appointmentView.cloneMe(today.intValue(), start_endWorkHour[0], start_endWorkHour[1]);
                        }
                        if (locale != null) {
                            appointmentView.setLocation(locale);
                        }
                        renderCellAppointment(html, day.toIntegerDate().toString(), appointmentView, true, true, APPOINTMENT_CSS);
                    }
                }
                renderCloseTD(html);
            }
            renderCloseTR(html);
        }
        renderCloseTABLE(html);
        renderCloseDIV(html);
        renderScroll(html, true);
        
        ResponseUtils.write(pageContext, html.toString());

        return SKIP_BODY;
    }

    private void updateAppointments(int today, List appointmentsDays, int startWorkHour, int endWorkHour) {
        for (Iterator iterator = appointmentsDays.iterator(); iterator.hasNext();) {
            String id = getAppointmentId(iterator.next());
            AppointmentCompountView appointmentView = getAppointment(id);
            log.debug("APPOINTMENT:\n" + appointmentView);
            if (appointmentView != null) {
                if (!appointmentView.isAllDay() || !appointmentView.isSameDay()) {
                    appointmentView = appointmentView.cloneMe(today, startWorkHour, endWorkHour);
                }
            }
        }
    }

    private void renderCellHeader(StringBuffer html, CalendarIU.Day day) {
        StringBuffer dayTitle = new StringBuffer();
        renderOpenDIV(html, MONTH_DAY_HEADER_CSS, null);
        //dayTitle.append("[")
        //renderOpenSPAN(dayTitle,null);
        dayTitle.append(day.getDay());
        //renderCloseSPAN(dayTitle);
        //.append("]");
        renderHREF(html, getUrlDay(day.getYear(), day.getMonth(), day.getDay()),
                dayTitle.toString(), RES_VIEWDAY, null, null);

        html.append(SPACE);

        String startDate = DateUtils.parseDate(DateUtils.integerToDate(DateUtils.getInteger(day.getYear(), day.getMonth(), day.getDay())), datePattern);
        String calendarDate = day.toIntegerDate().toString();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        //Generate icon for add apointment
        renderAddURL(request, html, null, -1, null, startDate, calendarDate);

        renderCloseDIV(html);
        renderHolidays(html, day.getDay(), day.getMonth(), true);
    }

    protected void renderScrollOtherField(StringBuffer html) {
        String yearTitle = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Appointment.Recurrence.year");
        renderOpenDIV(html,"row col-xs-12 col-sm-4 col-md-3 col-lg-3 wrapperButton", null);
        renderOpenDIV(html, "input-group", null);
        renderOpenSPAN(html, "input-group-addon input-sm");
        html.append(yearTitle);
        renderCloseSPAN(html);
        renderInputText(html, "year", year, "4", "4");
        renderCloseDIV(html);
        renderCloseDIV(html);
    }

    protected String getCalendar() {
        return year + (month < 10 ? "0" : "") + month;
    }

    protected void renderScrollField(StringBuffer html) {
        String monthTitle = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Appointment.Recurrence.month");
        renderOpenSPAN(html, "input-group-addon input-sm");
        html.append(monthTitle);
        renderCloseSPAN(html);
        renderMonthSelect(html, month);
    }

    protected String getNextUrl() {
        int m = month;
        int y = year;
        if (m + 1 <= 12) {
            m++;
        } else {
            m = 1;
            y++;
        }
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer url = new StringBuffer(request.getContextPath());
        url.append(getUrlByType(new Integer(y * 100 + m)));

        return url.toString();
        /*return getUrlByType(new Integer(y * 100 + m));*/
    }

    protected String getPreviusUrl() {
        int m = month;
        int y = year;
        if (m - 1 > 0) {
            m--;
        } else {
            m = 12;
            y--;
        }
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer url = new StringBuffer(request.getContextPath());
        url.append(getUrlByType(new Integer(y * 100 + m)));

        return url.toString();

/*        return getUrlByType(new Integer(y * 100 + m));*/
    }

    protected int getType() {
        return 3;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
