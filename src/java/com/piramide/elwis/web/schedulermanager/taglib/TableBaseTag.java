package com.piramide.elwis.web.schedulermanager.taglib;

import com.piramide.elwis.dto.schedulermanager.AppointmentCompountView;
import com.piramide.elwis.dto.schedulermanager.AppointmentView;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SchedulerPermissionUtil;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.urlencrypt.UrlEncryptJScriptUtil;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.schedulermanager.action.AbstractAppointmentUIAction;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.text.DateFormatSymbols;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: May 3, 2005
 * Time: 10:18:37 AM
 * To change this template use File | Settings | File Templates.
 */

public abstract class TableBaseTag extends BodyTagSupport {

    protected static Log log = LogFactory.getLog("TableBaseTag");

    private final static String addIMG = "add.gif";
    private final static String delIMG = "del.gif";
    public final static String ownerIMG = "owner.gif";
    public final static String participantIMG = "participants.gif";
    public final static String recurrenceIMG = "recurrence.gif";
    public final static String reminderIMG = "reminder.gif";

    private static final String scroll_left_IMG = "scroll_left.gif";
    private static final String scroll_right_IMG = "scroll_right.gif";

    private String RES_APPOINTMENT_ADD;
    private String RES_APPOINTMENT_DEL;
    protected String[] RES_REMINDER_TYPE;
    protected String RES_ALLDAY;
    protected String RES_REMINDER;
    protected String RES_RECURRENCE;
    protected String RES_PRIVATE;
    protected String RES_VIEWDAY;
    protected String RES_APPOINTMENT;
    protected String RES_OWNER;
    protected String RES_PARTICIPANT;
    protected String RES_TOOLTIP_TITLE;
    protected String RES_TOOLTIP_TYPE;
    protected String RES_TOOLTIP_PRIORITY;
    protected String RES_TOOLTIP_LOCATION;
    protected String RES_TOOLTIP_DURATION;
    protected String RES_TOOLTIP_LOCATION_UNDEFINED;

    protected static final String SPACE = "&nbsp;";
    protected static final String BR = "<br>";
    private static final String QUOTE = "\"";

    private static final String ATT_NOWRAP = " nowrap=\"";
    private static final String ATT_WITDH = " width=\"";
    private static final String CSS_CLASS = " class=\"";
    private static final String CSS_STYLE = " style=\"";

    private Map appointmentsInRange;
    private Map appointments;
    protected Map holidays;

    // Tag attributes
    protected static final String TABLE_CELLSPACING = "1";
    protected static final String TABLE_CSS = "calendar_table";
    protected static final String CALENDAR_CELL_CSS = "calendar_cell";
    protected static final String CALENDAR_WEEK_CELL = "calendar_week_cell";
    protected static final String HEADER_CSS = "calendar_header";
    protected static final String APPOINTMENT_CSS = "appointment";
    protected static final String CELL_NO_PADDING = "nopadding";
    protected static final String APPOINTMENT_BOTTOM_LINE_CSS = "appointmentBottomLine";
    protected static final String HOLIDAY_CSS = "holiday";
    protected static final String HOLIDAY_TEXT_CSS = "holiday_text";
    private static final String CALENDAR_CELL_OUTMONTH_COLOR_CLASS = "outmonthColor";
    private static final String CALENDAR_CELL_TODAY_COLOR_CLASS = "todayColor";
    private static final String CALENDAR_CELL_HOLIDAY_COLOR_CLASS = "holidayColor";
    public static final String SCROLL_TEXT_CSS = "scroll_text";
    protected static final String APP_ANONYMOUS_CSS = "appointmentAnonymous";

    private String tableWidth;
    private Map<Integer, String> overviewUserColorMap;
    protected String todayColor;
    protected String holidayColor;
    //protected DateTimeZone timeZone;
    protected DateTime todayDate;

    protected Integer workStartHour;
    protected Integer workEndHour;

    protected String emptyCellClass;


    protected String datePattern;

    private boolean rightAddAppointment;
    private boolean rightDelAppointment;
    private Byte privateAppPermission;
    private Byte publicAppPermission;
    private boolean isCalendarOfOtherUser = false;


    private String addURL;
    private String modURL;
    private String delURL;
    //private String viewDayURL;

    private String imgPathScheduler;

    protected String[] dayNames;

    private StringBuffer url;
    protected boolean beginInMonday;
    protected String scrollSufix;
    public static final String toolTipDIV_FIELD = "<div class=\\'field\\'>";
    public static final String toolTipDIV_DATA = "<div class=\\'data\\'>";
    private static final String jsToolTip = "" +
            "function myToolTip(title, at, type, pri,loc){\n" +
            "var html;\n";

    private static final String jsOnChangeMonth = "" +
            "function onChangeMonthGoToPage(type, sufix) {\n" +
            "var week, day, month, year;\n" +
            "var value;\n" +
            "if(sufix == undefined){\n" +
            "   sufix = '';\n" +
            "}\n" +
            "year = document.getElementById('year' + sufix);\n" +
            "value = year.value;\n" +
            "if(type == 2){\n" +
            "   week = document.getElementById('week' + sufix);\n" +
            "   if(week.value.length==1)  value = value + \"0\"\n" +
            "   value = value + week.value;\n" +
            "}else{ \n" +
            "   month=document.getElementById('month' + sufix);\n" +
            "   if(type==1){ //Day\n" +
            "     day=document.getElementById('day' + sufix);\n" +
            "     value = value + month.value; \n" +
            "     if(day.value.length==1) value = value + \"0\";\n" +
            "     value = value + day.value;\n" +
            "   }else if(type==3){\n" +
            "        value = value + month.value;\n" +
            "   }\n" +
            "}\n";

    private static final String jsGoToPage =
            "function goToPage(type, e, sufix) {\n" +
                    "if((window.event && window.event.keyCode == 13) || e.which==13){\n" +
                    "var week, day, month, year;\n" +
                    "var value;\n" +
                    "if(sufix == undefined){\n" +
                    "   sufix = '';\n" +
                    "}\n" +
                    "year = document.getElementById('year' + sufix);\n" +
                    "value = year.value;\n" +
                    "if(type == 2){\n" +
                    "   week = document.getElementById('week' + sufix);\n" +
                    "   if(week.value.length==1)  value = value + \"0\"\n" +
                    "   value = value + week.value;\n" +
                    "}else{ \n" +
                    "   month=document.getElementById('month' + sufix);\n" +
                    "   if(type==1){ //Day\n" +
                    "     day=document.getElementById('day' + sufix);\n" +
                    "     value = value + month.value; \n" +
                    "     if(day.value.length==1) value = value + \"0\";\n" +
                    "     value = value + day.value;\n" +
                    "   }else if(type==3){\n" +
                    "        value = value + month.value;\n" +
                    "   }\n" +
                    "}\n";

    protected static final String TOOLTIP_WITDH = "150";
    private static final int TOOLTIP_CHOP_LENGHT = 23;

    protected void initializeUrl() {
        beginInMonday = true;
        if (url.indexOf("?") <= 0) {
            url.append("?");
        }
        /*else
            url.append("&");
        url.append("type=");
          */
        /*HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

        addURL = response.encodeURL(addURL);
        modURL = response.encodeURL(modURL);
        delURL = response.encodeURL(delURL);*/
    }

    protected void initializeI18n(HttpServletRequest request) {
        RES_APPOINTMENT_ADD = JSPHelper.getMessage(request, "Scheduler.Appointment.add");
        RES_APPOINTMENT_DEL = JSPHelper.getMessage(request, "Scheduler.Appointment.del");

        RES_REMINDER_TYPE = new String[3];
        RES_REMINDER_TYPE[0] = JSPHelper.getMessage(request, "Appointment.minutes");
        RES_REMINDER_TYPE[1] = JSPHelper.getMessage(request, "Appointment.hours");
        RES_REMINDER_TYPE[2] = JSPHelper.getMessage(request, "Appointment.day");
        RES_REMINDER = JSPHelper.getMessage(request, "Scheduler.Appointment.reminder");

        RES_RECURRENCE = JSPHelper.getMessage(request, "Scheduler.Appointment.recurrence");

        RES_PARTICIPANT = JSPHelper.getMessage(request, "Scheduler.Appointment.participant");
        RES_OWNER = JSPHelper.getMessage(request, "Scheduler.Appointment.owner");
        RES_ALLDAY = JSPHelper.getMessage(request, "Scheduler.Appointment.allDay");

        RES_TOOLTIP_TITLE = JSPHelper.getMessage(request, "Appointment.name");
        RES_TOOLTIP_DURATION = JSPHelper.getMessage(request, "Scheduler.Appointment.duration");
        RES_TOOLTIP_TYPE = JSPHelper.getMessage(request, "Appointment.appType");
        RES_TOOLTIP_LOCATION = JSPHelper.getMessage(request, "Appointment.location");
        RES_TOOLTIP_PRIORITY = JSPHelper.getMessage(request, "Task.priority");
        RES_TOOLTIP_LOCATION_UNDEFINED = JSPHelper.getMessage(request, "Appointment.location.undefined");

        RES_PRIVATE = JSPHelper.getMessage(request, "Scheduler.Appointment.private");

        RES_VIEWDAY = JSPHelper.getMessage(request, "Scheduler.Appointment.viewDay");
        RES_APPOINTMENT = JSPHelper.getMessage(request, "Scheduler.Appointment");
        datePattern = JSPHelper.getMessage(request, "datePattern");


    }

    protected void initializeSchedulerData(HttpServletRequest request) {
        appointmentsInRange = (Map) request.getAttribute(AbstractAppointmentUIAction.APPOINTMENT_IN_RANGE);
        appointments = (Map) request.getAttribute(AbstractAppointmentUIAction.APPOINTMENTS_DATA);
        holidays = (Map) request.getAttribute(AbstractAppointmentUIAction.HOLIDAYS);
        DateTimeZone timeZone = (DateTimeZone) request.getAttribute(AbstractAppointmentUIAction.USER_TIMEZONE);
        todayDate = new DateTime(timeZone);
        workStartHour = (Integer) request.getAttribute("workStartHour");
        workEndHour = (Integer) request.getAttribute("workEndHour");

    }

    protected void initialize() {
        log.debug("________________________________________________");
        initializeUrl();

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        initializeSchedulerData(request);
        initializeI18n(request);


        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols((Locale) Config.get(pageContext.getSession(),
                Config.FMT_LOCALE));
        dayNames = new String[7];
        String[] weekNames = dateFormatSymbols.getWeekdays();

        //System.out.println("BEGININMONDAY:" + beginInMonday);

        if (beginInMonday) {
            System.arraycopy(weekNames, 2, dayNames, 0, 6);
            dayNames[6] = weekNames[1];//Sunday push in last day
        } else {
            System.arraycopy(weekNames, 1, dayNames, 0, 7);
        }

        initializeUserPermission(request);
    }

    protected void initializeUserPermission(HttpServletRequest request) {
        //view user
        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue("userId");
        Integer schedulerUserId = (Integer) user.getValue("schedulerUserId");

        rightAddAppointment = Functions.hasAccessRight(request, "APPOINTMENT", "CREATE");
        boolean rightModAppointment = Functions.hasAccessRight(request, "APPOINTMENT", "UPDATE");
        rightDelAppointment = Functions.hasAccessRight(request, "APPOINTMENT", "DELETE");
        log.debug("schedulerUserId:" + schedulerUserId);
        log.debug("User:" + userId);

        //process for calendar of other user
        isCalendarOfOtherUser = (!userId.equals(schedulerUserId));
        if (isCalendarOfOtherUser) {
            privateAppPermission = com.piramide.elwis.web.schedulermanager.el.Functions.getPrivatePermission(schedulerUserId, userId);
            publicAppPermission = com.piramide.elwis.web.schedulermanager.el.Functions.getPublicPermission(schedulerUserId, userId);
        }

        log.debug("Final access right: Add:" + rightAddAppointment + " - Mod:" + rightModAppointment + " - Del:" + rightDelAppointment);
    }

    protected int[] processStart_EndWorkHour() {
        return new int[]{0, 24};
    }

    protected String getJSToolTip() {
        StringBuffer jsTooltip = new StringBuffer(jsToolTip);
        jsTooltip.append("html = \"<div>")
                .append(toolTipDIV_FIELD)
                .append(RES_TOOLTIP_TITLE)
                .append("</div>")
                .append(toolTipDIV_DATA)
                .append("\" + title + \"</div>")
                .append(toolTipDIV_FIELD)
                .append(RES_TOOLTIP_DURATION)
                .append("</div>")
                .append(toolTipDIV_DATA)
                .append("\" + at + \"</div>")
                .append(toolTipDIV_FIELD)
                .append(RES_TOOLTIP_TYPE)
                .append("</div>")
                .append(toolTipDIV_DATA)
                .append("\" + type + \"</div>")
                .append(toolTipDIV_FIELD)
                .append(RES_TOOLTIP_PRIORITY)
                .append("</div>")
                .append(toolTipDIV_DATA)
                .append("\" + pri + \"</div>")
                .append(toolTipDIV_FIELD)
                .append(RES_TOOLTIP_LOCATION)
                .append("</div>")
                .append(toolTipDIV_DATA)
                .append("\" + loc + \"</div>")
                .append("</div>\";\n")
                .append("return html;\n")
                .append("}\n");
        return jsTooltip.toString();
    }

    protected String writeAttrib(String attrib, String value) {
        return new StringBuffer(attrib)
                .append(value)
                .append(QUOTE).toString();
    }


    protected void renderOpenTD(StringBuffer html, int colspan, int rowspan, String classStyle, String style, String width, boolean noWrap) {
        renderOpenTDorTH(html, null, colspan, rowspan, classStyle, style, width, noWrap, true);
    }

    protected void renderOpenTD(StringBuffer html, String id, String classStyle, String style, String width, boolean noWrap) {
        renderOpenTDorTH(html, id, -1, -1, classStyle, style, width, noWrap, true);
    }

    protected void renderOpenTD(StringBuffer html, String classStyle, String style, String width, boolean noWrap) {
        renderOpenTDorTH(html, null, -1, -1, classStyle, style, width, noWrap, true);
    }

    protected String renderTitle(String title) {
        return JSPHelper.chopString(title, TOOLTIP_CHOP_LENGHT);
    }

    private void renderOpenTDorTH(StringBuffer html, String id, int colspan, int rowspan, String classStyle, String style, String width, boolean noWrap, boolean isTD) {
        html.append(isTD ? "<td " : "<th ");
        if (colspan > 0) {
            html.append("colspan=\"").append(colspan).append("\" ");
        }
        if (rowspan > 0) {
            html.append("rowspan=\"").append(rowspan).append("\" ");
        }

        if (classStyle != null) {
            html.append(writeAttrib(CSS_CLASS, classStyle));
        }
        if (style != null) {
            html.append(writeAttrib(CSS_STYLE, style));
        }
        if (width != null) {
            html.append(writeAttrib(ATT_WITDH, width));
        }
        if (id != null) {
            html.append(" id=\"").append(id).append("\"");
        }
        if (noWrap) {
            html.append(writeAttrib(ATT_NOWRAP, "nowrap"));
        }
        html.append(">");
    }

    protected void renderOpenP(StringBuffer html, String classStyle) {
        html.append("<P ");
        if (classStyle != null) {
            html.append(writeAttrib(CSS_CLASS, classStyle));
        }
        html.append(">");
    }

    protected void renderCloseP(StringBuffer html) {
        html.append("</p>");
    }

    protected void renderOpenTH(StringBuffer html, int colspan, int rowspan, String classStyle, String style, String width, boolean noWrap) {
        renderOpenTDorTH(html, null, colspan, rowspan, classStyle, style, width, noWrap, false);
    }

    protected void renderOpenTH(StringBuffer html, String classStyle, String style, String width, boolean noWrap) {
        renderOpenTDorTH(html, null, -1, -1, classStyle, style, width, noWrap, false);
    }

    protected void renderComment(StringBuffer html, String comment) {
        html.append("\n<!--    ").append(comment).append("    -->\n");
    }

    private void renderCloseTD(StringBuffer html, boolean isTD) {
        html.append(isTD ? "</td>" : "</th>").append("\n");
    }

    protected void renderCloseTD(StringBuffer html) {
        renderCloseTD(html, true);
    }

    protected void renderCloseTH(StringBuffer html) {
        renderCloseTD(html, false);
    }

    protected String getAppointmentId(Object o) {
        Map map = (Map) o;
        return (String) map.keySet().iterator().next();
    }

    protected AppointmentCompountView getAppointment(String id) {
        return (AppointmentCompountView) appointments.get(id);
    }

    protected int getAppointmentsSizeByDay(Integer day) {
        if (!appointmentsInRange.containsKey(day)) {
            return 0;
        }
        return ((List) appointmentsInRange.get(day)).size();
    }

    protected List getAppointmentByDay(Integer day) {
        if (!appointmentsInRange.containsKey(day)) {
            return new ArrayList();
        }

        List list = (List) appointmentsInRange.get(day);
        Collections.sort(list, getAppointmentComparator());
        return list;
    }

    protected void renderOpenTR(StringBuffer html) {
        renderOpenTR(html, null);
    }

    protected void renderOpenTR(StringBuffer html, String trClass) {
        html.append("\n<tr");
        if (trClass != null) {
            html.append(" class=\"").append(trClass).append("\"");
        }
        html.append(">");
    }

    protected void renderCloseTR(StringBuffer html) {
        html.append("</tr>\n");
    }

    protected void renderOpenTABLE(StringBuffer html) {
        renderOpenTABLE(html, TABLE_CSS, tableWidth);
    }

    protected void renderOpenTABLE(StringBuffer html, String tableCSS, String with) {
        html.append("<table ");
        if (tableCSS != null) {
            html.append("class=\"").append(tableCSS).append("\" ");
        }
        if (with != null) {
            html.append(writeAttrib(ATT_WITDH, with));
        }
        html.append(" cellspacing=\"" + TABLE_CELLSPACING + "\">\n");
    }

    protected void renderCloseTABLE(StringBuffer html) {
        html.append("</table>\n");
    }

    protected void renderIMG(StringBuffer html, String imgFile, String title) {
        html.append("<img src=\"").append(imgPathScheduler + imgFile).append("\"");
        if (title != null) {
            html.append("title=\"").append(title).append("\"");
        }
        html.append("border=\"0\">\n");
    }

    public String renderIMG(String imgFile, String title) {
        StringBuffer html = new StringBuffer();
        html.append("<img src=\"").append(imgPathScheduler + imgFile).append("\"");
        if (title != null) {
            html.append("title=\"").append(title).append("\"");
        }
        html.append("border=\"0\" align=\"middle\">\n");
        return html.toString();
    }

    protected void renderHREF(StringBuffer html, String url, String text, String tip, String classCSS, String otherAttrib) {

        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        html.append("<a href=\"").append(response.encodeURL(url)).append("\" ");

        if (classCSS != null) {
            html.append("class=\"").append(classCSS).append("\" ");
        }
        if (tip != null) {
            html.append("title=\"").append(tip).append("\" ");
        }
        if (otherAttrib != null) {
            html.append(otherAttrib);
        }
        html.append(">").append(text).append("</a>");
    }

    /*protected void renderCellAppointment(StringBuffer html, AppointmentCompountView appointment, boolean chop, boolean showToolTip) {
        renderCellAppointment(html, appointment, chop, true, APPOINTMENT_CSS);
    }*/

    protected void renderCellAppointment(StringBuffer html, String calendarDate, AppointmentCompountView appointment, boolean chop,
                                         boolean showToolTip, String styleClass) {
        renderCellAppointment(html, calendarDate, appointment, chop, showToolTip, true, styleClass);
    }

    protected void renderCellAppointment(StringBuffer html, String calendarDate, AppointmentCompountView appointment, boolean chop,
                                         boolean showToolTip, boolean withDiv, String styleClass) {

        boolean onlyAnonymous = hasOnlyAnonymousAppPermission(appointment.isPublic());

        if (withDiv) {
            renderOpenDIV(html, styleClass + " appointment_font" + (onlyAnonymous ? " " + APP_ANONYMOUS_CSS : "")
                    , (onlyAnonymous ? null : "style=\"background: " + appointment.getColor() + ";\""));
        }

        // If has reminder show the icon
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        renderIcons(html, calendarDate, appointment);
        html.append(BR);
        StringBuffer timeLabel = new StringBuffer();
        renderOpenSPAN(timeLabel, "month_time");
        timeLabel.append(!appointment.isAllDay() ? appointment.getTimeLabel(true) : RES_ALLDAY);
        renderCloseSPAN(timeLabel);
        timeLabel.append(SPACE);

        if (onlyAnonymous) {
            html.append(timeLabel);
        } else {

            // Show the public label only if is PUBLIC
            if (!appointment.isPublic()) {
                renderOpenSPAN(timeLabel, "type_appoiment");
                timeLabel.append("[")
                        .append(RES_PRIVATE)
                        .append("]");
                renderCloseSPAN(timeLabel);

            }
            timeLabel.append(BR);

            renderModURL(request, html, timeLabel, calendarDate, appointment, chop, showToolTip);
            html.append(SPACE);
        }

        if (withDiv) {
            renderCloseDIV(html);
        }

        //}

    }

    protected void renderIcons(StringBuffer html, String calendarDate, AppointmentCompountView appointment) {
        renderIMG(html, appointment.isOwner() ? ownerIMG : participantIMG, appointment.isOwner() ? RES_OWNER : RES_PARTICIPANT);
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        if (appointment.isReminder()) {
            String tipReminder = new String(RES_REMINDER);
            int remType = appointment.getReminderType() - 1;
            if (remType >= 0 && remType < 3) {
                tipReminder = RES_REMINDER.replaceAll("<type>", RES_REMINDER_TYPE[remType]);
                tipReminder = tipReminder.replaceAll("<time>", appointment.getTimeBefore());
                renderIMG(html, reminderIMG, tipReminder);
            }
        }

        if (appointment.isRecurrent()) {
            renderIMG(html, recurrenceIMG, RES_RECURRENCE);
        }

        renderDelURL(request, html, calendarDate, appointment);
    }

    protected void renderIconsForAppointmentComponent(StringBuffer html, AppointmentCompountView appointment) {
        renderIMG(html, appointment.isOwner() ? ownerIMG : participantIMG, appointment.isOwner() ? RES_OWNER : RES_PARTICIPANT);
        if (appointment.isReminder()) {
            String tipReminder = new String(RES_REMINDER);
            int remType = appointment.getReminderType() - 1;
            if (remType >= 0 && remType < 3) {
                tipReminder = RES_REMINDER.replaceAll("<type>", RES_REMINDER_TYPE[remType]);
                tipReminder = tipReminder.replaceAll("<time>", appointment.getTimeBefore());
                renderIMG(html, reminderIMG, tipReminder);
            }
        }
        if (appointment.isRecurrent()) {
            renderIMG(html, recurrenceIMG, RES_RECURRENCE);
        }
    }

    protected String getValidTime(int h_m) {
        return (h_m < 10 ? "0" : "") + h_m;
    }

    protected String renderToolTip(String width, AppointmentView appointment) {

        String title = appointment.getTitle().replaceAll("\'", "\\\\\'");
        title = title.replaceAll("\"", "\\\\\'");
        String location;
        String priority = ((AppointmentCompountView) appointment).getPriority();

        if (appointment.getLocation() != null && appointment.getLocation().trim().length() > 0) {
            location = appointment.getLocation().replaceAll("\'", "\\\\\'").replaceAll("\"", "\\\\\'");
        } else {
            location = RES_TOOLTIP_LOCATION_UNDEFINED;
        }

        if (priority != null && priority.trim().length() > 0) {
            priority = priority.replaceAll("\'", "\\\\\'").replaceAll("\"", "\\\\\'");
        } else {
            priority = RES_TOOLTIP_LOCATION_UNDEFINED;
        }

        String type = "";
        if (appointment.getTypeName() != null) {
            type = appointment.getTypeName().replaceAll("\'", "\\\\\'").replaceAll("\"", "\\\\\'");
        }

        StringBuffer tooltip = new StringBuffer("onmouseover=\"this.T_BGCOLOR='#FFFFBB'; return escape(myToolTip('")
                .append(title);

        tooltip.append("', '");

        if (appointment.isSameDay()) {
            tooltip.append(DateUtils.parseDate(appointment.getStartDate().toDate(), datePattern));
            if (!appointment.isAllDay()) {
                tooltip.append(SPACE)
                        .append("(")
                        .append(appointment.getStartTime().getTimeFormat(true))
                        .append("-")
                        .append(appointment.getEndTime().getTimeFormat(true))
                        .append(")");
            }
        } else {
            tooltip.append(DateUtils.parseDate(appointment.getStartDate().toDate(), datePattern));

            AppointmentView.AppointmentTime realEndTime = appointment.getRealEndTime();
            if (realEndTime == null) {
                realEndTime = appointment.getEndTime();
            }

            log.debug("Tooltip end DATE TIME:" + appointment.getEndDate() + " " + realEndTime);
            //verify if end date is defined how 00:00, then end date is minus one day
            if (realEndTime.getHour() == 0 && realEndTime.getMinute() == 0) {
                DateTime startDateTime = new DateTime(appointment.getStartDate().toDate());
                DateTime endDateTime = new DateTime(appointment.getEndDate().toDate());
                endDateTime = endDateTime.minus(Period.days(1));

                log.debug("new END DATE:" + endDateTime);
                //verify if is the same day
                if (!(startDateTime.getYear() == endDateTime.getYear()
                        && startDateTime.getMonthOfYear() == endDateTime.getMonthOfYear()
                        && startDateTime.getDayOfMonth() == endDateTime.getDayOfMonth())) {

                    tooltip.append("-").append(DateUtils.parseDate(endDateTime.toDate(), datePattern));
                }
            } else {
                tooltip.append("-").append(DateUtils.parseDate(appointment.getEndDate().toDate(), datePattern));
            }
        }
        tooltip.append("', '")
                .append(type)
                .append("', '")
                .append(priority)
                .append("', '")
                .append(location)
                .append("'))\"");
        return tooltip.toString();
    }

    protected void renderAddURL(HttpServletRequest req, StringBuffer html, String text, int hour, String min, String date, String calendarDate) {
        if (hasAddAppointmentPermission()) {

            StringBuffer addurl = new StringBuffer(req.getContextPath());
            addurl.append(addURL);

            if (hour != -1) {
                addurl.append("&dto(startHour)=").append(hour).append("&dto(endHour)=").append(hour + 1);
            }
            if (min != null) {
                addurl.append("&dto(startMin)=").append(min).append("&dto(endMin)=").append(min);
            }
            if (date != null) {
                addurl.append("&dto(startDate)=").append(date).append("&dto(endDate)=").append(date);
            }
            if (text == null) {
                text = renderIMG(addIMG, RES_APPOINTMENT_ADD);
            }

            addurl.append("&type=").append(getType());
            addurl.append("&calendar=").append(calendarDate);

            renderHREF(html, addurl.toString(), text, null, null, null);

        } else {
            if (text != null) {
                html.append(text);
            }
        }
    }

    protected abstract String getCalendar();


    protected void renderModURL(HttpServletRequest req, StringBuffer html, StringBuffer time, String calendarDate, AppointmentCompountView appointment, boolean chop, boolean showToolTip) {
        String otherAttrib = null;
        String title = appointment.getTitle();
        StringBuffer modurl = new StringBuffer(req.getContextPath());
        modurl.append(modURL);
        modurl.append("&dto(appointmentId)=").append(appointment.getRealId());
        modurl.append("&type=").append(getType());
        modurl.append("&calendar=").append(calendarDate);
        modurl.append("&dto(private)=").append(!appointment.isPublic());

        if (!appointment.isOwner()) {
            modurl.append("&onlyView=true");
        }
        /*if (!(rightModAppointment && appointment.isOwner()))
        modurl.append("&onlyView=true");*/

        if (showToolTip) {
            otherAttrib = renderToolTip(TOOLTIP_WITDH, appointment);
        }

        time.append(chop ? renderTitle(title) : title);
        renderHREF(html, modurl.toString(), time.toString(), null, null, otherAttrib);
    }


    /*protected void renderModURL(StringBuffer html, AppointmentCompountView appointment, boolean chop, boolean showToolTip) {
        String otherAttrib = null;
        String title = appointment.getTitle();
        StringBuffer modurl = new StringBuffer(modURL);
        modurl.append("&dto(appointmentId)=").append(appointment.getRealId());
        modurl.append("&dto(title)=").append(title);
        modurl.append("&back=").append(getType());
        if (!(rightModAppointment && appointment.isOwner()))
            modurl.append("&onlyView=true");

        if (showToolTip) {
            otherAttrib = renderToolTip(TOOLTIP_WITDH, appointment);
        }
        renderHREF(html, modurl.toString(), chop ? renderTitle(title) : title, null, null, otherAttrib);
    }*/

    protected void renderDelURL(HttpServletRequest req, StringBuffer html, String calendarDate, AppointmentCompountView appointment) {

        if (hasDelAppointmentPermission(appointment.isPublic()) && appointment.isOwner()) {

            StringBuffer delurl = new StringBuffer(req.getContextPath());
            delurl.append(delURL);
            delurl.append("&dto(appointmentId)=").append(appointment.getRealId());
            delurl.append("&dto(title)=").append(appointment.getTitle());
            delurl.append("&type=").append(getType());
            delurl.append("&calendar=").append(calendarDate);
            delurl.append("&operation=").append("delete");
            delurl.append("&isRecurrent=").append(appointment.isRecurrent());
            delurl.append("&currentDate=").append(appointment.getStartDate().toIntegerDate());
            delurl.append("&dto(private)=").append(!appointment.isPublic());
            StringBuffer img = new StringBuffer();
            renderIMG(img, delIMG, RES_APPOINTMENT_DEL);
            renderHREF(html, delurl.toString(), img.toString(), null, null, null);
        }
    }


    protected void renderOpenSPAN(StringBuffer html, String classCSS) {
        html.append("<span class=\"").append(classCSS).append("\">");
    }

    protected void renderOpenSPAN(StringBuffer html, String classCSS, String otherAttribs) {
        html.append("<span ");
        if (classCSS != null) {
            html.append("class=\"").append(classCSS).append("\" ");
        }
        if (otherAttribs != null) {
            html.append(otherAttribs);
        }
        html.append(">");
    }

    protected void renderCloseSPAN(StringBuffer html) {
        html.append("</span>");
    }


    protected void renderOpenDIV(StringBuffer html, String classCSS, String otherAttribs) {
        html.append("<div ");
        if (classCSS != null) {
            html.append("class=\"").append(classCSS).append("\" ");
        }
        if (otherAttribs != null) {
            html.append(otherAttribs);
        }
        html.append(">");
    }

    protected void renderCloseDIV(StringBuffer html) {
        html.append("</div>");
    }


    public String getTableWidth() {
        return tableWidth;
    }

    public void setTableWidth(String tableWidth) {
        this.tableWidth = tableWidth;
    }

    public String getTodayColor() {
        return todayColor;
    }

    public void setTodayColor(String todayColor) {
        this.todayColor = todayColor;
    }

    public String getAddURL() {
        return addURL;
    }

    public void setAddURL(String addURL) {
        this.addURL = addURL;
    }

    public String getModURL() {
        return modURL;
    }

    public void setModURL(String modURL) {
        this.modURL = modURL;
    }

    public String getDelURL() {
        return delURL;
    }

    public void setDelURL(String delURL) {
        this.delURL = delURL;
    }

    public Collection getAppointments() {
        List app = new ArrayList(appointments.values());
        Collections.sort(app, getAppointmentComparator());
        return app;
    }

    private AppointmentComparator getAppointmentComparator() {
        return new AppointmentComparator();
    }

    //todo: URL here !!!!!!

    public void renderScroll(StringBuffer html, boolean isBottomScroll) {
        /*String tipNext = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), tipNextResource);
        String tipPrevius = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), tipPreviusResource);*/
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        LinkedHashMap<String, String> jsParams = new LinkedHashMap<String, String>();
        jsParams.put("calendar", "value");
        jsParams.put("type", String.valueOf(getType()));
        String jsUrlChange = UrlEncryptJScriptUtil.encryptForJScript(url.toString(), jsParams, request, response);
        /*
StringBuffer().append(request.getContextPath()).append(url).append(getType()).append("&calendar=").append(value).toString();
        html.append("<script>\n")
                .append(getJSToolTip())
                .append(jsOnChangeMonth)
                .append("window.location=\"")
                .append(getUrlByType(""))
                .append("\"+value;\n")
                .append("}\n")
                .append(jsGoToPage)
                .append("window.location=\"").
                append(getUrlByType(""))
                .append("\"+value;\n")
                .append("}\n")
                .append("}\n")
                .append("</script>");
       */

        if (isBottomScroll) {
            scrollSufix = "bottomSufix";
        } else {
            scrollSufix = null;

            html.append("<script>\n")
                    .append(getJSToolTip())
                    .append(jsOnChangeMonth)
                    .append("window.location=")
                    .append(jsUrlChange)
                    .append(";\n")
                    .append("}\n")
                    .append(jsGoToPage)
                    .append("window.location=")
                    .append(jsUrlChange)
                    .append(";\n")
                    .append("}\n")
                    .append("}\n")
                    .append("</script>");
        }


        renderOpenDIV(html, "scroll", null);
        //renderOpenTABLE(html);
        //renderOpenTR(html);
        renderHREF(html, getPreviusUrl(), renderIMG(scroll_left_IMG, null), null, null, null);
        renderScrollField(html);
        renderHREF(html, getNextUrl(), renderIMG(scroll_right_IMG, null), null, null, null);
        renderScrollOtherField(html);
        //renderCloseTR(html);
        html.append(SPACE);
        //html.append(getComplementarDate());

        renderCloseDIV(html);
        html.append(BR);

    }

    public boolean hasHoliday(int day, int month) {
        return holidays.containsKey(day + "-" + month);
    }

    public void renderHolidays(StringBuffer html, int day, int month, boolean chop) {
        Map holidayValues = (Map) holidays.get(day + "-" + month);
        if (holidayValues != null) {
            html.append("\n");
            renderOpenDIV(html, HOLIDAY_CSS, null);
            List holidaysWithoutCountry = (List) holidayValues.remove("");
            if (holidaysWithoutCountry != null) {
                renderHolidayHtml("", html, holidaysWithoutCountry, chop);
            }

            for (Iterator iterator = holidayValues.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry holidayByCountry = (Map.Entry) iterator.next();
                renderHolidayHtml((String) holidayByCountry.getKey(), html, (List) holidayByCountry.getValue(), chop);
            }
            renderCloseDIV(html);
        }
    }

    private void renderHolidayHtml(String country, StringBuffer html, List holidays, boolean chop) {
        for (Iterator iterator = holidays.iterator(); iterator.hasNext(); ) {
            Map.Entry holiday = (Map.Entry) ((Map) iterator.next()).entrySet().iterator().next();
            String title = (String) holiday.getKey();
            String choptitle = !chop ? title : JSPHelper.chopString(title, 21);
            renderOpenDIV(html, null, "title=\"" + title + "\"");
            html.append(choptitle);

            if (country.length() > 0) {
                html.append(SPACE).append("(").append(country).append(")");
            }
            renderCloseDIV(html);
            //renderCelebrate(html, (Boolean) holiday.getValue());
        }
    }

    protected abstract void renderScrollField(StringBuffer html);

    protected abstract void renderScrollOtherField(StringBuffer html);

    protected abstract String getNextUrl();

    protected abstract String getPreviusUrl();


    protected void renderInputText(StringBuffer html, String name, int value, String size, String maxLength) {

        if (hasScrollSufix()) {
            name = name + scrollSufix;
        }

        html.append("<input onKeyPress=\"goToPage(").append(getType()).append(", event").append(hasScrollSufix() ? ",\'" + scrollSufix + "\'" : "").append(")\"");
        html.append(" type=\"text\" class=\"scroll\"");
        html.append(" name=\"").append(name).append("\"").append(" id=\"").append(name).append("\"");
        if (value > 0) {
            html.append(" value=\"").append(value).append("\"");
        }
        if (size != null) {
            html.append(" size=\"").append(size).append("\"");
        }
        if (maxLength != null) {
            html.append(" maxlength=\"").append(maxLength).append("\"");
        }
        html.append("/>");
    }

    protected void renderMonthSelect(StringBuffer html, int selected) {
        String name = "month";
        if (hasScrollSufix()) {
            name = name + scrollSufix;
        }


        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols((Locale) Config.get(pageContext.getSession(),
                Config.FMT_LOCALE));
        String[] a = dateFormatSymbols.getMonths();

        html.append("<select onKeyPress=\"goToPage(").append(getType()).append(", event").append(hasScrollSufix() ? ",\'" + scrollSufix + "\'" : "").append(")\"");
        html.append(" onChange=\"onChangeMonthGoToPage(").append(getType()).append(hasScrollSufix() ? ",\'" + scrollSufix + "\'" : "").append(")\"");
        html.append(" name=\"").append(name).append("\"").append(" id=\"").append(name).append("\"").append(" class=\"scroll\" >\n");

        for (int i = 0; i < a.length - 1; i++) {
            html.append("<option");
            int val = i + 1;
            if (val == selected) {
                html.append(" selected");
            }
            html.append(" value=\"").append(val < 10 ? "0" : "").append(val).append("\">")
                    .append(StringUtils.capitalize(a[i])).append("</option>");
        }
        html.append("</select>");
    }

    protected abstract int getType();


    public class AppointmentComparator implements Comparator {

        public int compare(Object o1, Object o2) {

            AppointmentView a1;
            AppointmentView a2;

            AppointmentView.AppointmentTime time1;
            AppointmentView.AppointmentTime time2;

            if (o1 instanceof Map) {
                Map view1 = (Map) o1;
                Map view2 = (Map) o2;
                a1 = getAppointment(view1.keySet().iterator().next().toString());
                a2 = getAppointment(view2.keySet().iterator().next().toString());
                time1 = (AppointmentView.AppointmentTime) view1.values().iterator().next();
                time2 = (AppointmentView.AppointmentTime) view2.values().iterator().next();
            } else {
                a1 = (AppointmentView) o1;
                a2 = (AppointmentView) o2;
                time1 = a1.getStartTime();
                time2 = a2.getStartTime();
            }

            int res = 0;

            int h1 = time1.getHour();
            int m1 = time1.getMinute();
            int h2 = time2.getHour();
            int m2 = time2.getMinute();

            if (a1.getStartDate() != null && a2.getStartDate() != null) {
                res = a1.getStartDate().compareTo(a2.getStartDate().toIntegerDate());
            }

            if (res == 0) {
                if (h1 == h2 && m1 == m2) {
                    res = 0;
                } else if (h1 == h2) {
                    if (m1 < m2) {
                        res = -1;
                    } else {
                        res = 1;
                    }
                } else if (h1 < h2) {
                    res = -1;
                } else {
                    res = 1;
                }
            }

            // If this < o, return a negative value
            // If this = o, return 0
            // If this > o, return a positive value
            return res;
        }
    }

    public String getImgPathScheduler() {
        return imgPathScheduler;
    }

    public void setImgPathScheduler(String imgPathScheduler) {
        this.imgPathScheduler = imgPathScheduler + "/img/scheduler/";
    }
//todo: aqui quite el contextPath

    protected String getUrlByType(Object value) {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        return new StringBuffer().append(url).append("&type=").append(getType()).append("&calendar=").append(value).toString();
        /*return new StringBuffer().append(request.getContextPath()).append(url).append(getType()).append("&calendar=").append(value).toString();*/
    }

    //todo: aqui adicionando contextPath

    protected String getUrlDay(int year, int month, int day) {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer s = new StringBuffer().append(request.getContextPath()).append(url).append("&type=").append(1).append("&calendar=").append(year);
        if (month < 10) {
            s.append("0");
        }
        s.append(month);
        if (day < 10) {
            s.append("0");
        }
        s.append(day);

        return s.toString();
    }

    protected String getUrlWeek(int year, int week) {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer s = new StringBuffer().append(request.getContextPath()).append(url).append("&type=").append(2).append("&calendar=").append(year);
        if (week < 10) {
            s.append("0");
        }
        s.append(week);
        return s.toString();
    }

    protected String getUrlMonth(int year, int month) {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer s = new StringBuffer().append(request.getContextPath()).append(url).append("&type=").append(3).append("&calendar=").append(year);
        if (month < 10) {
            s.append("0");
        }
        s.append(month);
        return s.toString();
    }

    protected String getCssColor(CalendarIU.Day day) {
        return getCssColor(day, true);
    }

    protected String getCssColor(CalendarIU.Day day, boolean withOutMonth) {
        return getCssColor(day, withOutMonth, false);
    }

    protected String getCssColor(CalendarIU.Day day, boolean withOutMonth, boolean alwaysEvaluate) {
        String dayColor = null;
        if (day.isOutMonth()) {
            if (withOutMonth) {
                dayColor = !day.equalTo(todayDate) ? CALENDAR_CELL_OUTMONTH_COLOR_CLASS : CALENDAR_CELL_TODAY_COLOR_CLASS;
            }
        }

        if (!day.isOutMonth() || alwaysEvaluate) {
            if (day.equalTo(todayDate)) {
                dayColor = CALENDAR_CELL_TODAY_COLOR_CLASS;
            } else if (hasHoliday(day.getDay(), day.getMonth())) {
                dayColor = CALENDAR_CELL_HOLIDAY_COLOR_CLASS;
            }
        }
        return dayColor;
    }

    public String getUrl() {
        return url.toString();
    }

    public void setUrl(String url) {

        this.url = new StringBuffer(url);
    }

    public String getHeaderClass() {
        return HEADER_CSS;
    }


    public String getHolidayColor() {
        return holidayColor;
    }

    public void setHolidayColor(String holidayColor) {
        this.holidayColor = holidayColor;
    }

    private boolean hasAddAppointmentPermission() {
        if (isCalendarOfOtherUser) {
            return rightAddAppointment && com.piramide.elwis.web.schedulermanager.el.Functions.hasAddAppointmentPermission(publicAppPermission, privateAppPermission);
        } else {
            return rightAddAppointment;
        }
    }

    private boolean hasDelAppointmentPermission(boolean isPublicAppointment) {
        boolean delPermission = rightDelAppointment;
        if (isCalendarOfOtherUser) {
            delPermission = rightDelAppointment &&
                    (isPublicAppointment ? SchedulerPermissionUtil.hasSchedulerAccessRight(publicAppPermission, SchedulerPermissionUtil.DELETE)
                            : SchedulerPermissionUtil.hasSchedulerAccessRight(privateAppPermission, SchedulerPermissionUtil.DELETE));
        }
        return delPermission;
    }

    /**
     * verify is has only anonymous permission
     *
     * @param isPublicAppointment true=public, false=private
     * @return true or false
     */
    protected boolean hasOnlyAnonymousAppPermission(boolean isPublicAppointment) {
        boolean onlyAnonymous = false;
        if (isCalendarOfOtherUser) {
            onlyAnonymous = (isPublicAppointment ? SchedulerPermissionUtil.hasOnlyAnonymousPermission(publicAppPermission)
                    : SchedulerPermissionUtil.hasOnlyAnonymousPermission(privateAppPermission));
        }
        return onlyAnonymous;
    }

    protected void initializeOverviewUserColorMap() {
        overviewUserColorMap = new HashMap<Integer, String>();
    }

    /**
     * get identify user color to overview calendar
     *
     * @param userId
     * @return String
     */
    protected String getOverviewUserColor(Integer userId) {
        String color;
        if (overviewUserColorMap == null) {
            initializeOverviewUserColorMap();
        }

        if (overviewUserColorMap.containsKey(userId)) {
            color = overviewUserColorMap.get(userId);
        } else {

            String userLogin = com.piramide.elwis.web.schedulermanager.el.Functions.getUserLogin(userId);
            int loginCode = userLogin.hashCode();
            if (loginCode < 0) {
                loginCode = (-1) * loginCode;
            }

            color = "#";
            while (loginCode > 16 && color.length() <= 5) {
                String hexValue = Integer.toHexString(loginCode % 16);
                color = color + hexValue + hexValue;
                loginCode = Math.round(loginCode / 16);
            }

            String hexArray[] = {"0", "3", "9", "C", "F"};
            int i = 0;
            while (color.length() <= 5) {
                color = color + hexArray[i] + hexArray[i];
                i++;
            }

            overviewUserColorMap.put(userId, color);
            //log.debug("userColor Map:" + overviewUserColorMap);
        }
        return color;
    }

    protected String renderBorderStyleToOverviewApp(Integer userId) {
        return " border-left-color: " + getOverviewUserColor(userId) + "; "
                + " border-left-width: 8px; border-left-style: solid;";
    }

    protected boolean hasScrollSufix() {
        return scrollSufix != null;
    }

}
