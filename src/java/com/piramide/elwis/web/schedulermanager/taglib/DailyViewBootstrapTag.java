package com.piramide.elwis.web.schedulermanager.taglib;

import com.piramide.elwis.dto.schedulermanager.AppointmentCompountView;
import com.piramide.elwis.dto.schedulermanager.AppointmentView;
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

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Apr 11, 2005
 * Time: 6:12:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class DailyViewBootstrapTag extends TableBaseBootstrapTag {

    private static final String DAY_HEADER_WIDTH = "60";
    private static final String APPOINTMENT_HEADER_WIDTH = "100%";
    private static final String APPOINTMENT_CELL_CSS = "appointment_cell";
    private static final String DAY_CELL_CSS = CALENDAR_CELL_CSS + " day_cell";
//    quit and add padding to fix gradient in header
    private static final String QUIT_PADDING_TH ="quitPaddingFixHeaderInTH";
    private static final String ADD_PADDING_TH ="addPaddingFixHeaderInTH";
    Integer date;
    private static final String DATETIME_CSS = "datetime";
    protected static final String APPOINTMENT_TYPE_CSS = "type_appoiment";
    private int colums;
    //private static final String DAILY_TITLE_CSS = "daily_title";


    private void renderEmptyCell(StringBuffer html, String cellClass, int col) {
        renderCell(html, cellClass, null, SPACE, -1, col);
    }

    protected void renderAppointment(StringBuffer html, String cellClass, AppointmentView appointment, int row, int col) {
        StringBuffer appointmentHeader = new StringBuffer();
        renderCellAppointment(appointmentHeader, date.toString(), (AppointmentCompountView) appointment, false, true, false, APPOINTMENT_CSS);

        boolean onlyAnonymous = hasOnlyAnonymousAppPermission(appointment.isPublic());
        cellClass = cellClass + " appointment_font" + (onlyAnonymous ? " " + APP_ANONYMOUS_CSS : "");
        String styleValue = null;
        if (!onlyAnonymous) {
            styleValue = "background-color:" + appointment.getColor();
        }

        renderCell(html, cellClass, styleValue, appointmentHeader.toString(), row, col);
    }

    protected void renderCell(StringBuffer html, String cellClass, String styleValue, String value, int row, int col) {
        String witdh = null;
        if (colums > 1 && col <= 1) {
            witdh = Integer.toString(90 / colums) + "%";
        }

        renderOpenTD(html, col, row, cellClass, styleValue, witdh, true);
        html.append(value);
        renderCloseTD(html);
    }

    protected void renderEspecialAppointment(StringBuffer html, String calendarDate, AppointmentCompountView appointment, String styleClass, boolean showTime) {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        boolean onlyAnonymous = hasOnlyAnonymousAppPermission(appointment.isPublic());

        styleClass = styleClass + (onlyAnonymous ? " " + APP_ANONYMOUS_CSS : "");
        renderOpenDIV(html, styleClass, (onlyAnonymous ? null : "style=\"background: " + appointment.getColor() + ";\""));

        StringBuffer timeLabel = new StringBuffer();

        if (!onlyAnonymous) {
            renderModURL(request, html, timeLabel, date.toString(), appointment, false, true);
            html.append(SPACE);
        }

        renderTimeEspecialAppointment(html, appointment, showTime);
        html.append(SPACE);

        if (!onlyAnonymous) {
            // Show the public label only if is PUBLIC
            if (!appointment.isPublic()) {
                html.append("[");
                renderOpenSPAN(html, APPOINTMENT_TYPE_CSS);
                html.append(RES_PRIVATE);
                renderCloseSPAN(html);
                html.append("]");
            }
            html.append(SPACE);
        }

        renderIcons(html, calendarDate, appointment);

        renderCloseDIV(html);
    }

    protected void renderTimeEspecialAppointment(StringBuffer html, AppointmentCompountView appointment, boolean showTime) {
        if (!appointment.isSameDay() || showTime) {
            renderOpenSPAN(html, DATETIME_CSS);
            //Start date and time
            html.append("(");
            html.append(DateUtils.parseDate(appointment.getStartDate().toDate(), datePattern));
            if (!appointment.isAllDay()) {
                html.append(SPACE).append(appointment.getStartTime().getTimeLabel());
            }

            boolean writeEndDate = true;
            Date endDate = appointment.getEndDate().toDate();
            AppointmentView.AppointmentTime realEndTime = appointment.getRealEndTime();
            if (realEndTime == null) {
                realEndTime = appointment.getEndTime();
            }

            log.debug("all day end DATE TIME:" + appointment.getEndDate() + " " + realEndTime);
            //verify if end date is defined how 00:00, then end date is minus one day
            if (realEndTime.getHour() == 0 && realEndTime.getMinute() == 0) {
                DateTime startDateTime = new DateTime(appointment.getStartDate().toDate());
                DateTime endDateTime = new DateTime(appointment.getEndDate().toDate());
                endDateTime = endDateTime.minus(Period.days(1));
                endDate = endDateTime.toDate();

                log.debug("new END DATE:" + endDateTime);
                //verify if is the same day
                if (startDateTime.getYear() == endDateTime.getYear()
                        && startDateTime.getMonthOfYear() == endDateTime.getMonthOfYear()
                        && startDateTime.getDayOfMonth() == endDateTime.getDayOfMonth()) {

                    writeEndDate = false;
                }
            }

            if (writeEndDate) {
                //Separator "-"
                html.append(SPACE).append("-").append(SPACE);

                //End date and time
                html.append(DateUtils.parseDate(endDate, datePattern));
                if (!appointment.isAllDay()) {
                    html.append(SPACE).append(appointment.getEndTime().getTimeLabel());
                }
            }

            html.append(")");
            renderCloseSPAN(html);
        }
    }

    public AppointmentDay initializeAppointmentDay() {
        date = (Integer) pageContext.getRequest().getAttribute("date");

        Integer mit = (Integer) pageContext.getRequest().getAttribute("minuteIntervalTime");

        int workstartHour = workStartHour.intValue();
        int workendHour = workEndHour.intValue();
        int minuteIntervalTime = mit.intValue();

        AppointmentDay appointmentDay = new AppointmentDay(minuteIntervalTime, date);

        List appointments = getAppointmentByDay(date);
        //log.debug("StartHour:" + workStartHour + " - EndHour:" + workEndHour);
        //Se llena AppointmentDay con los appointments del dia, para que sean procesados
        for (Iterator iterator = appointments.iterator(); iterator.hasNext();) {
            String id = getAppointmentId(iterator.next());
            AppointmentCompountView appointmentView = getAppointment(id);
            //log.debug("is Recurrence:" + appointmentView.isRecurrent());
            int startHour = appointmentView.getStartTime().getHour();
            int endHour = appointmentView.getEndTime().getHour();
            if (!appointmentView.isAllDay()) {
                if (startHour < workstartHour) {
                    workstartHour = startHour;
                } else if (endHour < workstartHour) {
                    workstartHour = endHour;
                }
                if (endHour > workendHour) {
                    workendHour = endHour;
                    if (appointmentView.getEndTime().getMinute() > 0) {
                        workendHour += 1;
                    }
                } else if (startHour > workendHour || (endHour == 0 && appointmentView.getEndTime().getMinute() == 0)) {
                    workendHour = 24;
                }
            }
            if (date.equals(appointmentView.getEndDate().toIntegerDate()) &&
                    (endHour == 0 && appointmentView.getEndTime().getMinute() == 0)) {
                continue;
            }
            appointmentDay.addAppointment(appointmentView);
        }

        log.debug("StartHour:" + workstartHour + " - EndHour:" + workendHour);
        appointmentDay.setIntervalView(workstartHour, workendHour);
        return appointmentDay;
    }

    public int doStartTag() throws JspException {
        initialize();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        /*HttpServletRequest request =  (HttpServletRequest) pageContext.getRequest();
        DateTimeZone schedulerUserTimeZone = (DateTimeZone) request.getAttribute(AbstractAppointmentUIAction.USER_TIMEZONE);
        DateTimeZone userTimeZone = (DateTimeZone) request.getAttribute(AbstractAppointmentUIAction.USER_TIMEZONE);

        int offsetHour = 0;
        if(!userTimeZone.equals(schedulerUserTimeZone)){

        }*/
        String hour_title = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Scheduler.hour");
        String size_appointments = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Scheduler.appointmemtsAmount");
        //String RES_OUTVIEW = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Scheduler.Appointment.outCalendarView");


        AppointmentDay appointmentDay = initializeAppointmentDay();
        appointmentDay.calculateAppointmentPositionProccess();
        colums = appointmentDay.getProcessedColumns();
        StringBuffer html = new StringBuffer();
        renderScroll(html, false);
        renderOpenDIV(html,"table-responsive",null);
        renderOpenTABLE(html);
        // Header of Table;
        renderOpenTR(html, HEADER_CSS);
        renderOpenTH(html, null, null, DAY_HEADER_WIDTH, true);
        //renderOpenSPAN(html, DAILY_TITLE_CSS);
        html.append(hour_title);
        //renderCloseSPAN(html);
        renderCloseTH(html);
        renderOpenTH(html, appointmentDay.getProcessedColumns(), -1, QUIT_PADDING_TH, null, APPOINTMENT_HEADER_WIDTH, true);

        DateTime dayView = DateUtils.integerToDateTime(date);

        //html.append(RES_APPOINTMENT);
        renderOpenTABLE(html, null, "100%");
        renderOpenTR(html, HEADER_CSS);
        renderOpenTH(html, ADD_PADDING_TH, "text-align: right;", "50%", false);
        //renderOpenSPAN(html, DAILY_TITLE_CSS);
        html.append(dayNames[dayView.getDayOfWeek() - 1]);
        //renderCloseSPAN(html);
        renderCloseTH(html);
        renderOpenTH(html, ADD_PADDING_TH, "text-align: right;", "50%", false);
        html.append(size_appointments).append(SPACE).append(appointmentDay.getAppointmentAmount());
        renderCloseTH(html);
        renderCloseTR(html);
        renderCloseTABLE(html);
        int[] daymonth = DateUtils.getYearMonthDay(date);
        if (hasHoliday(daymonth[2], daymonth[1])) {
            renderHolidays(html, daymonth[2], daymonth[1], false);
        }
        renderCloseTH(html);
        renderCloseTR(html);

        //Render all day appointments
        renderOpenTR(html);
        renderOpenTD(html, DAY_CELL_CSS, null, "60", true);
        html.append(RES_ALLDAY);
        renderCloseTD(html);

        renderOpenTD(html, appointmentDay.getProcessedColumns(), -1, DAY_CELL_CSS + " " + CELL_NO_PADDING, null, APPOINTMENT_HEADER_WIDTH, true);

        for (Iterator iterator = appointmentDay.getAllDayAppointments().iterator(); iterator.hasNext();) {
            AppointmentCompountView appointmentView = (AppointmentCompountView) iterator.next();
            renderEspecialAppointment(html, date.toString(), appointmentView, APPOINTMENT_CSS + " appointment_font", false);
        }


        renderCloseTD(html);
        renderCloseTR(html);
        String startDate = DateUtils.parseDate(DateUtils.integerToDate(date), datePattern);

        while (appointmentDay.hasNextRows()) {
            int[] t = appointmentDay.nextRow();


            renderOpenTR(html);

            StringBuffer time = new StringBuffer();

            renderOpenTD(html, DAY_CELL_CSS, null, "60", true);
            if (t != null) {
                String min = getValidTime(t[1]);

                renderAddURL(request, time, getValidTime(t[0]) + ":" + min, t[0], min, startDate, date.toString()); // Hour label with link for Add appoitnment
                html.append(time);
            } else {
                html.append(SPACE);
            }
            renderCloseTD(html);
            //boolean renderIntervals = false;
            while (appointmentDay.hasNextColumn()) {
                AppointmentDay.AppointmentDayCell cell = appointmentDay.nextColumn();
                if (!cell.isFail()) {
                    if (cell.isEmpty()) {
                        renderEmptyCell(html, DAY_CELL_CSS, cell.getAppointmentCols());
                    } else {
                        //if(colums > 1 && cell.getAppointmentCols()<=1)
                        renderAppointment(html, APPOINTMENT_CELL_CSS, cell.get(), cell.getAppointmentRows(), cell.getAppointmentCols());
                    }
                }
            }
            renderCloseTR(html);
        }
        /*renderOpenTR(html);
        renderOpenTD(html, DAY_CELL_CSS, null, "60", true);
        html.append(appointmentDay.getWorkEndHour()).append(":").append("00");
        renderCloseTD(html);
        renderCloseTR(html);*/

        renderCloseTABLE(html);
        renderCloseDIV(html);
        renderScroll(html, true);

        ResponseUtils.write(pageContext, html.toString());
        return SKIP_BODY;
    }

    protected void renderScrollOtherField(StringBuffer html) {
        int[] ymd = DateUtils.getYearMonthDay(date);
        String month = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Appointment.Recurrence.month");
        String year = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Appointment.Recurrence.year");

        renderOpenDIV(html,"row col-xs-12 col-sm-4 col-md-3 col-lg-3 wrapperButton",null);
        renderOpenDIV(html, "input-group", null);
        renderOpenSPAN(html, "input-group-addon input-sm");
        html.append(month);
        renderCloseSPAN(html);
        renderMonthSelect(html, ymd[1]);
        renderCloseDIV(html);
        renderCloseDIV(html);

        renderOpenDIV(html,"row col-xs-12 col-sm-4 col-md-3 col-lg-3 wrapperButton",null);
        renderOpenDIV(html, "input-group", null);
        renderOpenSPAN(html, "input-group-addon input-sm");
        html.append(year);
        renderCloseSPAN(html);
        renderInputText(html, "year", ymd[0], "4", "4");
        renderCloseDIV(html);
        renderCloseDIV(html);
    }

    protected String getCalendar() {
        return date.toString();
    }

    protected void renderScrollField(StringBuffer html) {
        int[] ymd = DateUtils.getYearMonthDay(date);
        String day = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Appointment.Recurrence.day");
        renderOpenSPAN(html, "input-group-addon input-sm");
        html.append(day);
        renderCloseSPAN(html);
        renderInputText(html, "day", ymd[2], "2", "2");
    }

    protected String getNextUrl() { //todo: modificado
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer url = new StringBuffer(request.getContextPath());
        url.append(getUrlByType(DateUtils.dateToInteger(DateUtils.integerToDateTime(date).plus(Period.days(1)))));

        return url.toString();
    }

    protected String getPreviusUrl() {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer url = new StringBuffer(request.getContextPath());
        url.append(getUrlByType(DateUtils.dateToInteger(DateUtils.integerToDateTime(date).minus(Period.days(1)))));

        return url.toString();
        /*return getUrlByType(DateUtils.dateToInteger(DateUtils.integerToDateTime(date).minus(Period.days(1))));*/
    }

    protected int getType() {
        return 1;
    }
}
