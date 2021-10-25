package com.piramide.elwis.web.schedulermanager.taglib;

import com.piramide.elwis.dto.schedulermanager.AppointmentCompountView;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: AppointmentComponentTag.java 9703 2009-09-12 15:46:08Z fernando ${CLASS_NAME}.java,v 1.2 01-06-2005 03:31:55 PM ivan Exp $
 */
public class AppointmentComponentTag extends DailyViewTag {
    private static final String WEEK_WIDTH = "14%";
    private static final String DAY_HEADER_WIDTH = "60px";
    private static final String APPOINTMENT_HEADER_WIDTH = "100%";
    private int week;
    private int year;

    private String dayClass;


    public int doStartTag() throws JspException {
        initialize();
        AppointmentDay appointmentDay = initializeAppointmentDay();

        //String day_title = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Scheduler.day");
        Integer dislplay = Integer.valueOf("0");
        if (pageContext.getRequest().getParameter("rowsPerPage") != null) {
            dislplay = Integer.valueOf((String) pageContext.getRequest().getParameter("rowsPerPage"));
        }


        StringBuffer html = new StringBuffer();

        //renderOpenTABLE(html);
        /*html.append("<script>").append(getJSToolTip()).append("</script>");*/
        html.append("<script type=\"text/javascript\">" + "\n").append(getJSToolTip()).append("\n</script>");
        html.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" >");
        // Header of Table;
        renderOpenTR(html, "HeaderDashBoard");

        //APPOINTMENT_HEADER_WIDTH
        renderOpenTH(html, 2, -1, "listHeaderDashBoard", null, null, true);
        html.append(RES_APPOINTMENT);
        renderCloseTH(html);

        renderCloseTR(html);
        int posApp = 0;

        int counter = 0;
        for (Iterator iterator = appointmentDay.getAppointments().iterator(); iterator.hasNext();) {
            AppointmentCompountView appointmentView = (AppointmentCompountView) iterator.next();
            if (appointmentView != null) {
                html.append("<tr class=\"listRow\" >");
                //html.append("<td class=\"listItem2\">");
                this.myRenderCellAppointment(html, appointmentView, false, false, true, "");
                //html.append("</td>");
                html.append("</tr>");
            }
        }

        for (Iterator iterator = appointmentDay.getAllDayAppointments().iterator(); iterator.hasNext();) {
            AppointmentCompountView appointmentView = (AppointmentCompountView) iterator.next();
            if (appointmentView != null) {
                html.append("<tr class=\"listRow\" >");
                //html.append("<td class=\"listItem2\">");
                this.myRenderCellAppointment(html, appointmentView, false, false, true, "");
                //html.append("</td>");
                html.append("</tr>");
            }
        }

        posApp++;
        renderCloseTABLE(html);

        ((HttpServletResponse) pageContext.getResponse()).setCharacterEncoding("UTF-8");
        ((HttpServletResponse) pageContext.getResponse()).setHeader("Cache-Control", "max-age=0");
        JspWriter writer = pageContext.getOut();
        try {
            writer.print(html.toString());
        }
        catch (IOException e) {
            log.error("Cannot render Appointment component ", e);
        }
        /*ResponseUtils.write(pageContext, html.toString());*/
        return SKIP_BODY;
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
        DateTime today = new DateTime(y, 1, 1, 0, 0, 0, 0);
        if (w + 1 < today.weekOfWeekyear().getMaximumValue()) {
            w++;
        } else {
            w = 1;
            y++;
        }
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer url = new StringBuffer(request.getContextPath());
        url.append(getUrlByType(new Integer(y * 100 + w)));
        return url.toString();
        /*return getUrlByType(new Integer(y * 100 + w));*/
    }

    protected String getPreviusUrl() {
        int w = week;
        int y = year;
        log.debug("-->W:" + w + "-Y:" + y);
        if (w - 1 > 1) {
            w--;
        } else {
            y--;
            DateTime backYear = new DateTime(y, 1, 1, 0, 0, 0, 0);
            w = backYear.weekOfWeekyear().getMaximumValue();
        }
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer url = new StringBuffer(request.getContextPath());
        url.append(getUrlByType(new Integer(y * 100 + w)));
        return url.toString();
        /*return getUrlByType(new Integer(y * 100 + w));*/
    }

    protected int getType() {
        return 5;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void myRenderCellAppointment(StringBuffer html,
                                           AppointmentCompountView appointment,
                                           boolean chop,
                                           boolean showToolTip,
                                           boolean withDiv,
                                           String styleClass) {

        StringBuffer appointmentLabel = new StringBuffer();

        renderOpenTD(html, "listItem2", "width:20%;border-right-style:none;padding-right:0px;vertical-align:bottom;", null, true);
        //renderOpenDIV(html, null, null);


        html.append(!appointment.isAllDay() ? appointment.getTimeLabel(true) : RES_ALLDAY);

        renderCloseTD(html);
        renderOpenTD(html, "listItem2", "width:80%;border-left-style:none;padding-left:0px;", null, false);

        // Show the public label only if is PUBLIC
        if (!appointment.isPublic()) {
            appointmentLabel.append("[");
            renderOpenSPAN(appointmentLabel, "type_appoiment");
            appointmentLabel.append(RES_PRIVATE);
            renderCloseSPAN(appointmentLabel);
            appointmentLabel.append("]");
        }
        appointmentLabel.append(SPACE);
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        renderModURL(request, html, appointmentLabel, appointment.getStartDate().toIntegerDate().toString(), appointment, chop, showToolTip);
        html.append(SPACE);
        this.renderIconsForAppointmentComponent(html, appointment);
        renderCloseTD(html);

    }

    protected void renderIcons(StringBuffer html, String calendarDate, AppointmentCompountView appointment) {
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

    protected String getCalendar() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}

