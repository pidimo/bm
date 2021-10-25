package com.piramide.elwis.web.schedulermanager.taglib;

import com.piramide.elwis.dto.schedulermanager.AppointmentCompountView;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.common.el.Functions;
import org.apache.struts.util.ResponseUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: DailyAJAXResponseTag.java 9703 2009-09-12 15:46:08Z fernando ${CLASS_NAME}.java,v 1.2 01-06-2005 03:31:55 PM ivan Exp $
 */

public class DailyAJAXResponseTag extends DailyViewTag {

    protected void renderAppointmentXML(StringBuffer xml, AppointmentCompountView appointmentView, boolean allday) {
        log.debug(" ... renderAppointmentXML function execute ... ");

        String tag = !allday ? "appointment" : "alldayappointment";
        xml.append("\n<").append(tag).append(" ");
        xml.append("time=\"").append(appointmentView.getTimeLabel(true)).append("\" ");
        xml.append("allday=\"").append(appointmentView.isAllDay()).append("\" ");
        xml.append("reminder=\"").append(appointmentView.isReminder()).append("\" ");
        xml.append("remindertype=\"").append(appointmentView.getReminderType()).append("\" ");
        xml.append("remindervalue=\"").append(appointmentView.getTimeBefore()).append("\" ");
        xml.append("recurrent=\"").append(appointmentView.isRecurrent()).append("\" ");
        xml.append("owner=\"").append(appointmentView.isOwner()).append("\" ");
        xml.append("public=\"").append(appointmentView.isPublic()).append("\">\n");

        //xml.append("\n<title>");
        if (hasOnlyAnonymousAppPermission(appointmentView.isPublic())) {
            xml.append(Functions.ajaxResponseFilter(" "));
        } else {
            xml.append(Functions.ajaxResponseFilter(appointmentView.getTitle()));
        }

        //xml.append("</title>\n");
        xml.append("\n</").append(tag).append(">\n");
    }

    private void renderHolidaysXML(StringBuffer xml, String day) {
        Map holidayValues = (Map) holidays.get(day);
        if (holidayValues != null) {
            List holidaysWithoutCountry = (List) holidayValues.remove("");
            if (holidaysWithoutCountry != null) {
                renderHolidayXML("", holidaysWithoutCountry, xml);
            }

            for (Iterator iterator = holidayValues.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry holidayByCountry = (Map.Entry) iterator.next();
                renderHolidayXML((String) holidayByCountry.getKey(), (List) holidayByCountry.getValue(), xml);
            }
        }
    }

    private void renderHolidayXML(String country, List holidays, StringBuffer xml) {
        for (Iterator iterator = holidays.iterator(); iterator.hasNext();) {
            Map.Entry holiday = (Map.Entry) ((Map) iterator.next()).entrySet().iterator().next();
            xml.append("<holiday country=\"").append(country).append("\">\n");
            xml.append(Functions.ajaxResponseFilter((String) holiday.getKey()));
            //System.out.println("ENCODE:"+JavaScriptEncoder.encode((String) holiday.getKey()));

            //xml.append((String) holiday.getKey());
            xml.append("\n</holiday>\n");
        }
    }

    public int doStartTag() throws JspException {
        initializeUserPermission((HttpServletRequest) pageContext.getRequest());
        initializeSchedulerData((HttpServletRequest) pageContext.getRequest());
        AppointmentDay appointmentDay = initializeAppointmentDay();

        StringBuffer xml = new StringBuffer();
        xml.append("\n<scheduler id=\"").append(date).append("\">\n");
        xml.append("<holidays>\n");
        int[] daymonth = DateUtils.getYearMonthDay(date);
        if (hasHoliday(daymonth[2], daymonth[1])) {
            renderHolidaysXML(xml, daymonth[2] + "-" + daymonth[1]);
        }

        xml.append("</holidays>\n");
        xml.append("<appointments>");
        int[] start_endWorkHour = processStart_EndWorkHour();

        for (Iterator iterator = appointmentDay.getAppointments().iterator(); iterator.hasNext();) {
            AppointmentCompountView appointmentView = (AppointmentCompountView) iterator.next();

            if (appointmentView != null) {
                Integer w = todayDate.getWeekOfWeekyear();
                Integer y = todayDate.getYear();
                int week;
                int year;
                week = w.intValue();
                year = y.intValue();
                CalendarIU cal = new CalendarIU();
                cal.setWeek(week, year);
                CalendarIU.Week week1 = cal.getWeek();
                CalendarIU.Day day = week1.nextDay();
                AppointmentCompountView appointmentV = null;

                if (!appointmentView.isSameDay() || !appointmentView.isAllDay()) {
                    appointmentV = appointmentView.cloneMe(new Integer(date.toString()), start_endWorkHour[0], start_endWorkHour[1]);
                } else {
                    appointmentV = appointmentView;
                }

                renderAppointmentXML(xml, appointmentV, false);
            }
        }

        for (Iterator iterator = appointmentDay.getAllDayAppointments().iterator(); iterator.hasNext();) {
            AppointmentCompountView appointmentView = (AppointmentCompountView) iterator.next();

            if (appointmentView != null) {
                renderAppointmentXML(xml, appointmentView, true);
            }
        }
        xml.append("</appointments>\n");
        xml.append("</scheduler>");
        log.debug(xml);
        ResponseUtils.write(pageContext, xml.toString());

        return SKIP_BODY;
    }
}

