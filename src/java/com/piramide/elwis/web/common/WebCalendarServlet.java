package com.piramide.elwis.web.common;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.schedulermanager.taglib.CalendarIU;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.joda.time.DateTimeZone;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormatSymbols;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Jul 19, 2005
 * Time: 10:54:30 AM
 * To change this template use File | Settings | File Templates.
 */

public class WebCalendarServlet extends HttpServlet {
    protected static Log log = LogFactory.getLog("WebCalendar");
    private final static int CONSTANT_TYPE = 0;
    private final static int REQUEST_TYPE = 1;
    private final static int SESSION_TYPE = 2;
    private final static String IMAGE_PATH_PARAM = "image_path";
    private final static String IMAGE_PATH_ATTRIBUTE_TYPE = "image_path_attribute_type";
    private static String imagePathParam;
    int type;
    int month;
    int year;
    private String dateField_ = null;
    private static final String CELL_WIDTH = "12%";
    private static final String TABLE_CSS = "wc_table";
    private static final String HEADER_CSS = "wc_header";
    private static final String WEEK_CELL_CSS = "wc_week_cell";
    private static final String DAY_CELL_CSS = "wc_day_cell";
    private static final String OUTMONTH_COLOR_CSS = "wc_outmonth_color";
    private static final String TODAY_COLOR_CSS = "wc_today_color";
    private static final String WEBCALENDAR_DIR = "/img/calendar/";

    private static final String HTML_HEADER1 = "<script type=\"text/javascript\">\n" +
            "<!--\n" +
            "function submit(){\n" +
            "    document.forms.wcForm.submit();\n" +
            "    }\n" +
            "    \n" +
            "    function goTo(event){\n" +
            "       if((window.event && window.event.keyCode == 13) || event.which==13){\n" +
            "          submit();\n" +
            "       }\n" +
            "    }" +
            "function select(date) {\n" +
            "  window.opener.document.getElementById('";

    private static final String HTML_HEADER2 = "').value = date;\n" +
            "  self.close();\n" +
            "}\n" +
            "//-->\n" +
            "</script>\n" +
            "<title>";

    private static final String HTML_HEADER2_ = "').value=date;\n" +
            "if(window.opener.document.getElementById('endDate') != null)" +
            "  window.opener.document.getElementById('endDate').value = date;\n" +
            "  self.close();\n" +
            "}\n" +
            "//-->\n" +
            "</script>\n" +
            "<title>";

    private static final String HTML_NO_SESSION = "<html>\n" +
            "<script type=\"text/javascript\">\n" +
            "<!--\n" +
            "function nosession() {\n" +
            "  window.opener.location=\"{url}\";\n" +
            "  self.close();\n" +
            "}\n" +
            "//-->\n" +
            "</script>\n" +
            "<body onload=\"nosession()\" ></body>\n" +
            "</html>";

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        try {
            type = Integer.parseInt(getInitParameter(IMAGE_PATH_ATTRIBUTE_TYPE));
        } catch (Exception e) {
            type = CONSTANT_TYPE;
        }
        imagePathParam = getInitParameter(IMAGE_PATH_PARAM);
    }

    private String getImagePath(HttpServletRequest request) {
        String path = null;
        switch (type) {
            case CONSTANT_TYPE:
                path = imagePathParam;
                break;
            case REQUEST_TYPE:
                path = (String) request.getAttribute(imagePathParam);
                break;
            case SESSION_TYPE:
                path = (String) request.getSession().getAttribute(imagePathParam);
                break;
        }
        return path + WEBCALENDAR_DIR;
    }

    private void renderImportCss(StringBuffer html, User user, HttpServletRequest request, HttpServletResponse response) {
        String companyId = user.getValue("companyId").toString();
        Object companyChange = request.getSession().getServletContext().getAttribute("companyStyleStatus_" + companyId);
        Object logonChange = request.getSession().getAttribute("logonStyleStatus");
        String urlCssCompany = response.encodeURL(request.getContextPath() + "/UIManager/Put/StyleSheet.do?companyChangeCount=" + companyChange + "&logonChangeCount=" + logonChange);
        String urlCssUser = response.encodeURL(request.getContextPath() + "/UIManager/Put/StyleSheet.do?userChangeCount=" + request.getSession().getAttribute("userStyleStatus") + "&logonChangeCount=" + logonChange);

        html.append("\n<LINK rel=\"stylesheet\" href=\"").append(response.encodeURL(request.getContextPath() + "/elwis_style.css")).append("\" type=\"text/css\">");
        html.append("\n<LINK rel=\"stylesheet\" href=\"").append(urlCssCompany).append("\" type=\"text/css\">");
        html.append("\n<LINK rel=\"stylesheet\" href=\"").append(urlCssUser).append("\" type=\"text/css\">");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doIt(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doIt(request, response);
    }


    private void doIt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String RES_CALENDAR_TITLE = JSPHelper.getMessage(request, "Webcalendar.title");
        String RES_WEEK_TITLE = JSPHelper.getMessage(request, "Webcalendar.week.title");
        String RES_TODAY_TITLE = JSPHelper.getMessage(request, "Common.today");
        String dateValue = request.getParameter("date");
        String dateField = request.getParameter("field");
        dateField_ = request.getParameter("field_");

        log.debug("DATEFIELD:  " + dateField + " - Date:  " + dateValue + "DATE_FIELD:  " + dateField_);

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(Constants.USER_KEY);
        if (user == null) {
            //response.getOutputStream().print(HTML_NO_SESSION.replaceAll("{url}", request.getContextPath()));
            response.sendRedirect(request.getContextPath());
            return;
        }

        DateTimeZone timeZone = (DateTimeZone) user.getValue("dateTimeZone");
        //if (timeZone == null) timeZone = DateTimeZone.getDefault();
        Calendar calendar = new GregorianCalendar(timeZone.toTimeZone());
        int[] today = {calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)};
        String pattern = JSPHelper.getMessage(request, "datePattern").trim();
        //trying to recover the pattern when the value is not null
        if (!GenericValidator.isBlankOrNull(dateValue)) {
            Date test = null;
            if (dateValue.length() < 10) {
                test = GenericTypeValidator.formatDate(dateValue, JSPHelper.getMessage(request,
                        "withoutYearPattern").trim(), true);
            }
            if (test != null) {
                pattern = JSPHelper.getMessage(request, "withoutYearPattern");
            }
        }

        try {
            month = Integer.parseInt(request.getParameter("month"));
            year = Integer.parseInt(request.getParameter("year"));
        } catch (Exception e) {
            Date date = GenericTypeValidator.formatDate(dateValue, pattern.trim(), true);
            if (date != null) {
                calendar.setTime(date);
            }
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
        }

        StringBuffer html = new StringBuffer();

        CalendarIU calendarIU = new CalendarIU(month, year);

        Iterator weeks = calendarIU.getIteratorWeek();

        String[] dayNames = new String[7];
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols((Locale) Config.get(request.getSession(), Config.FMT_LOCALE));
        String[] weekNames = dateFormatSymbols.getShortWeekdays();

        boolean beginInMonday = true;
        if (beginInMonday) {
            System.arraycopy(weekNames, 2, dayNames, 0, 6);
            dayNames[6] = weekNames[1];//Sunday push in last day
        } else {
            System.arraycopy(weekNames, 1, dayNames, 0, 7);
        }
        html.append("<html>\n<head>\n");
        renderImportCss(html, user, request, response);

        html.append(HTML_HEADER1)
                .append(dateField);
        if (dateField_ != null) {
            html.append(HTML_HEADER2_);
        } else {
            html.append(HTML_HEADER2);
        }

        html.append(RES_CALENDAR_TITLE);
        html.append("</title><body>\n");

        renderScroll(html, request, dateField);
        renderOpenTABLE(html, null, null, TABLE_CSS, "100%");
        renderOpenTR(html, null);
        renderOpenTH(html, HEADER_CSS, null, CELL_WIDTH, false);
        html.append(RES_WEEK_TITLE);
        renderCloseTH(html);

        for (int i = 0; i < dayNames.length; i++) {
            renderOpenTH(html, HEADER_CSS, null, CELL_WIDTH, false);
            html.append(dayNames[i].substring(0, 2));
            renderCloseTH(html);
        }
        renderCloseTR(html);
        while (weeks.hasNext()) {
            CalendarIU.Week week = (CalendarIU.Week) weeks.next();
            renderOpenTR(html);
            renderOpenTD(html, WEEK_CELL_CSS, null, CELL_WIDTH, false);
            //Render number week
            html.append(week.getWeek());
            renderCloseTD(html);

            while (week.hasNextDay()) {
                CalendarIU.Day day = week.nextDay();

                renderOpenTD(html, DAY_CELL_CSS + getCssColor(day, today, true), null, CELL_WIDTH, false);
                renderHREF(html, getUrlCalendar(day, pattern), Integer.toString(day.getDay()), null);
                renderCloseTD(html);
            }
            renderCloseTR(html);
        }

        renderOpenTR(html);
        renderOpenTD(html, 8, -1, null, null, null, false);
        html.append("<center>");
        renderHREF(html, getUrlCalendar(today[0], today[1], today[2], pattern), RES_TODAY_TITLE, null);
        html.append("</center>");
        renderCloseTD(html);
        renderCloseTR(html);
        renderCloseTABLE(html);

        html.append("</body></html>");
        response.setContentType("text/html" + "; charset=" + Constants.CHARSET_ENCODING);

        PrintWriter write = response.getWriter();
        write.write(html.toString());
    }

    public String renderIMG(String imgFile, String title, HttpServletRequest request) {
        StringBuffer html = new StringBuffer();
        html.append("<img src=\"").append(getImagePath(request) + imgFile).append("\"");
        if (title != null) {
            html.append("title=\"").append(title).append("\"");
        }
        html.append("border=\"0\" align=\"absbottom\">\n");
        return html.toString();
    }

    private void renderScroll(StringBuffer html, HttpServletRequest request, String field) {
        html.append("<form action=\"?").append("field=").append(field).append("\" id=\"wcForm\">");
        html.append("<input type=\"hidden\" name=\"field\" value=\"").append(field).append("\">");
        html.append(renderDateField_AsHidden());
        html.append("<div style=\"vertical-align: middle\">");
        renderMonthSelect(html, month, request);
        html.append("&nbsp");
        renderHREF(html, getPreviusUrl(field), renderIMG("left.gif", JSPHelper.getMessage(request, "Webcalendar.previousYear"), request), null);
        renderInputText(html, "year", year, "4", "4");
        renderHREF(html, getNextUrl(field), renderIMG("right.gif", JSPHelper.getMessage(request, "Webcalendar.nextYear"), request), null);
        html.append("</div></form>");
    }

    protected void renderInputText(StringBuffer html, String name, int value, String size, String maxLength) {
        html.append("<input onKeyPress=\"goTo(event)\"");
        writeAttrib(html, "type", "text");
        writeAttrib(html, "class", "scroll");
        writeAttrib(html, "name", name);
        writeAttrib(html, "id", name);
        writeAttrib(html, "value", value);
        writeAttrib(html, "size", size);
        writeAttrib(html, "maxlength", maxLength);
        html.append("/>");
    }

    protected void renderMonthSelect(StringBuffer html, int selected, HttpServletRequest request) {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols((Locale) Config.get(request.getSession(), Config.FMT_LOCALE));
        String[] a = dateFormatSymbols.getMonths();
        html.append("<select name=\"month\" id=\"month\" class=\"scroll\" onChange=\"document.forms.wcForm.submit()\">\n");
        for (int i = 0; i < a.length - 1; i++) {
            html.append("<option");
            int val = i + 1;
            if (val == selected) {
                html.append(" selected");
            }

            html.append(" value=\"").append(val).append("\">")
                    .append(StringUtils.capitalize(a[i])).append("</option>");
        }
        html.append("</select>");
    }

    protected String getCalendar() {
        return year + (month < 10 ? "0" : "") + month;
    }


    protected String getNextUrl(String field) {
        int y = year;
        y++;
        return getUrl(month, y, field);
    }


    private String getUrl(int month, int year, String field) {
        return "?month=" + month + "&year=" + year + "&field=" + field + renderDateField_AsParameter();
    }

    protected String getPreviusUrl(String field) {
        int y = year;
        y--;
        return getUrl(month, y, field);
    }


    private String getUrlCalendar(CalendarIU.Day day, String pattern) {
        return getUrlCalendar(day.getYear(), day.getMonth(), day.getDay(), pattern);

    }

    private String getUrlCalendar(int year, int month, int day, String pattern) {
        Date date = DateUtils.intToDate(year, month, day);
        String dateStr = DateUtils.parseDate(date, pattern);
        return "javascript:select('" + dateStr + "')";
    }

    protected String getCssColor(CalendarIU.Day day, int[] today, boolean withOutMonth) {
        String dayColor = null;
        if (day.isOutMonth()) {
            if (withOutMonth) {
                dayColor = OUTMONTH_COLOR_CSS;
            }
        } else if (today[2] == day.getDay() &&
                today[1] == day.getMonth() &&
                today[0] == day.getYear()) {
            dayColor = TODAY_COLOR_CSS;
        }
        return dayColor != null ? " " + dayColor : "";
    }

    protected void writeAttrib(StringBuffer html, String attrib, String value) {
        if (value != null) {
            html.append(" ").append(attrib).append("=\"").append(value).append("\"");
        }
    }

    protected void writeAttrib(StringBuffer html, String attrib, int value) {
        if (value > 0) {
            html.append(" ").append(attrib).append("=\"").append(value).append("\"");
        }
    }

    protected void renderOpenTD(StringBuffer html, int colspan, int rowspan, String classStyle, String style, String width, boolean noWrap) {
        renderOpenTDorTH(html, colspan, rowspan, classStyle, style, width, noWrap, true);
    }

    protected void renderOpenTD(StringBuffer html, String classStyle, String style, String width, boolean noWrap) {
        renderOpenTDorTH(html, -1, -1, classStyle, style, width, noWrap, true);
    }


    private void renderOpenTDorTH(StringBuffer html, int colspan, int rowspan, String classStyle, String style, String width, boolean noWrap, boolean isTD) {
        html.append(isTD ? "<td " : "<th ");
        writeAttrib(html, "colspan", colspan);
        writeAttrib(html, "rowspan", rowspan);
        writeAttrib(html, "class", classStyle);
        writeAttrib(html, "style", style);
        writeAttrib(html, "width", width);
        if (noWrap) {
            writeAttrib(html, "nowrap", "nowrap");
        }
        html.append(">");
    }

    protected void renderOpenTH(StringBuffer html, int colspan, int rowspan, String classStyle, String style, String width, boolean noWrap) {
        renderOpenTDorTH(html, colspan, rowspan, classStyle, style, width, noWrap, false);
    }

    protected void renderOpenTH(StringBuffer html, String classStyle, String style, String width, boolean noWrap) {
        renderOpenTDorTH(html, -1, -1, classStyle, style, width, noWrap, false);
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

    protected void renderOpenTR(StringBuffer html) {
        renderOpenTR(html, null);
    }

    protected void renderOpenTR(StringBuffer html, String trClass) {
        html.append("\n<tr");
        writeAttrib(html, "class", trClass);
        html.append(">");
    }

    protected void renderCloseTR(StringBuffer html) {
        html.append("</tr>\n");
    }

    protected void renderOpenTABLE(StringBuffer html, String cellspacing, String cellpadding, String tableCSS, String with) {
        html.append("<table");
        writeAttrib(html, "class", tableCSS);
        writeAttrib(html, "width", with);
        writeAttrib(html, "cellspacing", cellspacing);
        writeAttrib(html, "cellpadding", cellpadding);
    }

    protected void renderCloseTABLE(StringBuffer html) {
        html.append("</table>\n");
    }

    protected void renderHREF(StringBuffer html, String url, String text, String classCSS) {
        html.append("<a");
        writeAttrib(html, "href", url);
        writeAttrib(html, "class", classCSS);

        html.append(">")
                .append(text).append("</a>");

    }

    private String renderDateField_AsParameter() {
        String param = "";
        if (dateField_ != null) {
            param = "&field_=" + dateField_;
        }
        return param;
    }

    private String renderDateField_AsHidden() {
        String hidden = "";
        if (dateField_ != null) {
            hidden = "<input type=\"hidden\" name=\"field_\" value=\"" + dateField_ + "\">";
        }
        return hidden;
    }

}
