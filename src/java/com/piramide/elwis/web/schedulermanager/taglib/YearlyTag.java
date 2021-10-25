package com.piramide.elwis.web.schedulermanager.taglib;

import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.schedulermanager.action.AbstractAppointmentUIAction;
import org.apache.struts.util.ResponseUtils;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.Config;
import java.text.DateFormatSymbols;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Apr 11, 2005
 * Time: 6:12:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class YearlyTag extends TableBaseTag {
    private static final String COLUMN_WIDTH = "15";
    private int tableWithPercent;
    private int year;
    private static final String YEAR_WEEK_CSS = CALENDAR_WEEK_CELL + " year_week_cell";
    private static final String YEAR_CELL_CSS = CALENDAR_CELL_CSS + " year_cell";
    private String RES_WEEK_TITLE;
    private String RES_VIEWWEEK;
    private String RES_VIEWMONTH;
    private int[][] appointments;
    private static String RES_LOADING = "";
//    private boolean showOutDays;

    private void renderCalendarHeader(StringBuffer html, Locale locale) {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
        String[] swd = dateFormatSymbols.getShortWeekdays();
        renderOpenTR(html, HEADER_CSS);
        renderOpenTH(html, null, null, COLUMN_WIDTH, true);
        html.append(RES_WEEK_TITLE);
        //renderCloseSPAN(html);
        renderCloseTH(html);
        int i;
        i = beginInMonday ? 2 : 1;
        for (; i < swd.length; i++) {
            char[] c = new char[1];
            swd[i].getChars(0, 1, c, 0);
            renderOpenTH(html, null, null, COLUMN_WIDTH, true);
            html.append(c[0]);
            renderCloseTH(html);
        }
        if (beginInMonday) {
            char[] c = new char[1];
            swd[1].getChars(0, 1, c, 0);
            renderOpenTH(html, null, null, COLUMN_WIDTH, true);
            html.append(c[0]);
            renderCloseTH(html);
        }

        renderCloseTR(html);
    }

    private void renderMonth(StringBuffer html, StringBuffer header, int calendar_month, String title, String with) {
        log.debug(" ... renderMonth function execute ...");
        renderOpenTABLE(html, "month_calendar", with);

        renderOpenTR(html);
        renderOpenTH(html, null, null, "99%", false);
        renderHREF(html, getUrlMonth(year, calendar_month),
                title, RES_VIEWMONTH, null, null);
        renderCloseTH(html);
        renderOpenTH(html, null, null, "1%", false);
        html.append(SPACE);
        renderCloseTH(html);
        renderCloseTR(html);

        //Render el TR que contiene el mini calendario...
        renderOpenTR(html);
        renderOpenTD(html, 2, -1, null, null, "98%", false);
        renderOpenTABLE(html, YEAR_CELL_CSS, null);
        html.append(header);
        CalendarIU cal = new CalendarIU(calendar_month, year);
        Iterator weeks = cal.getIteratorWeek();
        while (weeks.hasNext()) {
            CalendarIU.Week week = (CalendarIU.Week) weeks.next();
            renderOpenTR(html);
            renderOpenTD(html, YEAR_WEEK_CSS, null, null, false);
            //Render number week
            renderHREF(html, getUrlWeek(week.getWeekYearToUI(), week.getWeek()),
                    Integer.toString(week.getWeek()), RES_VIEWWEEK, null, null);
            renderCloseTD(html);
            while (week.hasNextDay()) {
                CalendarIU.Day day = week.nextDay();

                String hrefStyle = null;
                renderOpenTD(html, getCssColor(day, false), YEAR_CELL_CSS, null, null, false);
                String onMouseOver = null;
                String dayValue = null;
                boolean hasAppoingment = dayHasAppointment(day.getMonth(), day.getDay()) && day.getYear() == year;
                if (hasAppoingment) {
                    hrefStyle = "smallcalendar_appointment";
                }

                if (hasAppoingment || hasHoliday(day.getDay(), day.getMonth())) {
                    int intergerDate = day.toIntegerDate().intValue();
                    onMouseOver = new StringBuffer().append(" onmouseover=\"this.T_STICKY=true; this.T_STATIC=true;")
                            .append("return escape('<div id=\\'")
                            .append(intergerDate)
                            .append("\\'>")
                            .append("<span id=\\'").append(intergerDate).append("Load\\'></span>").append(RES_LOADING).append("</div>')\"").toString();

                    StringBuffer sb = new StringBuffer();

                    sb.append("<span onmouseover=\"toolTip('").append(intergerDate).append("')\"");
                    sb.append(" class=\"").append("smallcalendar_appointment").append("\"");
                    sb.append(">");
                    sb.append(day.getDay());
                    sb.append("</span>");
                    dayValue = sb.toString();

                }
                if (!day.isOutMonth()) {
                    renderHREF(html, getUrlDay(day.getYear(), day.getMonth(), day.getDay()),
                            dayValue != null ? dayValue : Integer.toString(day.getDay()),
                            RES_VIEWDAY, hrefStyle, onMouseOver);
                }

                //if (day.isOutMonth()) renderCloseSPAN(html);

                renderCloseTD(html);
            }
            renderCloseTR(html);
        }
        renderCloseTABLE(html);
        renderCloseTD(html);
        renderCloseTR(html);
        renderCloseTABLE(html);
    }

    private boolean dayHasAppointment(int month, int day) {
        return !(appointments[day - 1][month - 1] == 0);
    }

    public int doStartTag() throws JspException {
        initialize();
        appointments = (int[][]) pageContext.getRequest().getAttribute(AbstractAppointmentUIAction.APPOINTMENTS_YEAR);
        RES_WEEK_TITLE = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Scheduler.Appointment.weekTitle");
        RES_VIEWWEEK = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Scheduler.Appointment.viewWeek");
        RES_VIEWMONTH = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Scheduler.Appointment.viewMonth");
        RES_LOADING = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Scheduler.appointment.loading");
        Integer y = (Integer) pageContext.getRequest().getAttribute("year");
        if (y == null) {
            y = new Integer(new DateTime().getYear());
        }
        year = y.intValue();

        StringBuffer html = new StringBuffer();
        renderScroll(html, false);
        renderOpenTABLE(html, null, tableWithPercent + "%");
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols((Locale) Config.get(pageContext.getSession(),
                Config.FMT_LOCALE));
        String[] months = dateFormatSymbols.getMonths();
        StringBuffer header = new StringBuffer();
        renderCalendarHeader(header, (Locale) Config.get(pageContext.getSession(),
                Config.FMT_LOCALE));

        int group = 0;
        for (int i = 1; i < 13; i++) {
            if (group == 0) {
                renderOpenTR(html);
            }
            group++;
            renderOpenTD(html, "smallcalendar_month_cell", null, "16%", false);
            renderMonth(html, header, i, months[i - 1], "70%");
            renderCloseTD(html);
            if (group == 6) {
                renderCloseTR(html);
                group = 0;
            }
        }

        renderCloseTABLE(html);
        html.append(BR);
        renderScroll(html, true);
        
        ResponseUtils.write(pageContext, html.toString());
        return SKIP_BODY;
    }

    /*private void renderCellHeader(StringBuffer html, CalendarIU.Day day) {
        StringBuffer dayTitle = new StringBuffer();
        renderOpenSPAN(html, MONTH_TEXT_CSS);
        dayTitle.append("[")
                .append(day.getDay())
                .append("]");
        renderHREF(html, getUrlDay(day.getYear(), day.getMonth(), day.getDay()),
                   dayTitle.toString(), RES_VIEWDAY, null, null);
        renderCloseSPAN(html);
        html.append(SPACE);

        String startDate = DateUtils.parseDate(DateUtils.integerToDate(DateUtils.getInteger(day.getYear(), day.getMonth(), day.getDay())), datePattern);

        //Generate icon for add apointment
        renderAddURL(html, null, -1, null, startDate);
        html.append(BR);
    }*/

    protected void renderScrollOtherField(StringBuffer html) {
    }

    protected String getCalendar() {
        return null;
    }

    protected void renderScrollField(StringBuffer html) {
        log.debug(" ... renderScrollField function execute ... ");
        String yearTitle = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Appointment.Recurrence.year");
        renderOpenSPAN(html, "scroll_text");
        html.append(yearTitle);
        renderCloseSPAN(html);
        html.append(SPACE);
        renderInputText(html, "year", year, "4", "4");
        html.append(SPACE);
    }

    protected String getNextUrl() {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer url = new StringBuffer(request.getContextPath());
        url.append(getUrlByType(new Integer(year + 1)));

        return url.toString();
        /*return getUrlByType(new Integer(year + 1));*/
    }

    protected String getPreviusUrl() {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer url = new StringBuffer(request.getContextPath());
        url.append(getUrlByType(new Integer(year - 1)));

        return url.toString();
        /*return getUrlByType(new Integer(year - 1));*/
    }

    protected int getType() {
        return 4;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getTableWithPercent() {
        return tableWithPercent;
    }

    public void setTableWithPercent(int tableWithPercent) {
        this.tableWithPercent = tableWithPercent;
    }
}
