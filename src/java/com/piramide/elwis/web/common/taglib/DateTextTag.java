package com.piramide.elwis.web.common.taglib;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.html.BaseInputTag;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.util.Date;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: DateTextTag.java 11879 2015-12-16 17:51:12Z milver $
 */

public class DateTextTag extends BaseInputTag {
    private Log log = LogFactory.getLog(DateTextTag.class);
    protected String accept;
    protected String name;
    protected boolean redisplay;
    protected String type;
    protected String disabledEL;
    boolean calendarPicker;
    boolean view;
    String datePatternKey;
    boolean withOutYear = false;
    String withOutYearString;
    private boolean today;
    private boolean convert;
    protected boolean currentDate;
    protected DateTimeZone currentDateTimeZone;
    private boolean parseLongAsDate;

    private String mode;
    private String javaScriptDatePattern;
    private String placeHolder;

    public DateTextTag() {
        accept = null;
        name = "org.apache.struts.taglib.html.BEAN";
        today = view = false;
        redisplay = true;
        type = null;
        convert = false;
        calendarPicker = false;
        currentDate = false;
        currentDateTimeZone = null;
        parseLongAsDate = false;
    }

    public DateTimeZone getCurrentDateTimeZone() {
        return currentDateTimeZone;
    }

    public void setCurrentDateTimeZone(DateTimeZone currentDateTimeZone) {
        this.currentDateTimeZone = currentDateTimeZone;
    }

    public boolean isCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(boolean currentDate) {
        this.currentDate = currentDate;
    }

    public boolean isConvert() {
        return convert;
    }

    public void setConvert(boolean convert) {
        this.convert = convert;
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }

    public boolean isCalendarPicker() {
        return calendarPicker;
    }

    public void setCalendarPicker(boolean calendarPicker) {
        this.calendarPicker = calendarPicker;
    }

    public String getWithOutYear() {
        return withOutYearString;
    }

    public void setWithOutYear(String withOutYearString) {
        this.withOutYearString = withOutYearString;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getRedisplay() {
        return redisplay;
    }

    public void setRedisplay(boolean redisplay) {
        this.redisplay = redisplay;
    }

    public String getDisabledEL() {
        return disabledEL;
    }

    public void setDisabledEL(String disabledEL) {
        this.disabledEL = disabledEL;
    }

    public String getDatePatternKey() {
        return datePatternKey;
    }

    public void setDatePatternKey(String datePatternKey) {
        this.datePatternKey = datePatternKey;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getJavaScriptDatePattern() {
        return javaScriptDatePattern;
    }

    public void setJavaScriptDatePattern(String javaScriptDatePattern) {
        this.javaScriptDatePattern = javaScriptDatePattern;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    public int doStartTag() throws JspException {


        if ("datePattern".equals(datePatternKey) || "withoutYearPattern".equals(datePatternKey)) {
            datePatternKey = JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), datePatternKey);

        }

        StringBuffer results = null;
        log.debug("view = " + view);
        if (!isView()) {
            results = processAsEditMode();
        } else {
            results = processAsViewMode();
        }

        ResponseUtils.write(pageContext, results.toString());
        return 2;
    }

    private StringBuffer processAsViewMode() throws JspException {
        StringBuffer results;
        results = new StringBuffer("<input style=\"text-align:right\" type=\"");
        results.append("hidden");
        results.append("\" name=\"");
        String val = "";
        if (indexed) {
            prepareIndex(results, name);
        }
        results.append(property);
        results.append("\"");
        results.append(" value=\"");
        if (this.value != null) {
            results.append(ResponseUtils.filter(this.value));
        } else if (redisplay || !"password".equals(type)) {
            Object value = RequestUtils.lookup(pageContext, name, property, null);
            if (value == null) {
                value = "";
            }
            if (!"".equals(value)) {
                String patternSeparator = getPatternSeparator();

                if (null != patternSeparator && value.toString().indexOf(patternSeparator) == -1) {
                    Date date = DateUtils.integerToDate(new Integer(value.toString()));
                    value = DateUtils.parseDate(date, datePatternKey);
                }
            }
            val = ResponseUtils.filter(value.toString());
            results.append(val);
        }

        results.append("\"");
        results.append(prepareEventHandlers());
        results.append(prepareStyles());
        results.append(getElementClose());
        results.append(val);
        return results;
    }

    private StringBuffer processAsEditMode() throws JspException {
        StringBuffer results;
        results = new StringBuffer("<input type=\"");
        results.append("text");
        results.append("\" name=\"");
        if (indexed) {
            prepareIndex(results, name);
        }
        results.append(property);
        results.append("\"");
        if (accesskey != null) {
            results.append(" accesskey=\"");
            results.append(accesskey);
            results.append("\"");
        }
        if (accept != null) {
            results.append(" accept=\"");
            results.append(accept);
            results.append("\"");
        }
        if (maxlength != null) {
            results.append(" maxlength=\"");
            results.append(maxlength);
            results.append("\"");
        }
        if ((Constants.UIMode.BOOTSTRAP.getConstant().equals(mode))&&(placeHolder!=null)){
            results.append(" placeholder=\"");
            results.append(placeHolder);
            results.append("\"");
        }
        if (cols != null) {
            results.append(" size=\"");
            results.append(cols);
            results.append("\"");
        }
        if (tabindex != null) {
            results.append(" tabindex=\"");
            results.append(tabindex);
            results.append("\"");
        }


        User user = (User) pageContext.getSession().getAttribute(Constants.USER_KEY);
        DateTimeZone timeZone = (DateTimeZone) user.getValue("dateTimeZone");
        results.append(" value=\"");
        if (this.value != null) {
            results.append(ResponseUtils.filter(this.value));
        } else if (redisplay || !"password".equals(type)) {
            Object value = RequestUtils.lookup(pageContext, name, property, null);

            if (value == null) {
                value = "";
            }
            if (currentDate && "".equals(value)) {

                if (currentDateTimeZone != null) {
                    value = DateUtils.parseDate(new DateTime(currentDateTimeZone), datePatternKey);
                } else {
                    value = DateUtils.parseDate(new DateTime((DateTimeZone) user.getValue("dateTimeZone")),
                            datePatternKey);
                }
            }

            if (!"".equals(value)) {
                Date date;
                log.debug("VALUE: " + value);
                if (value instanceof String) {
                    if (convert) {
                        try {
                            value = new Integer((String) value);
                        } catch (Exception e) {
                            log.debug("cannot be parsed");
                        }
                    }
                    if (parseLongAsDate) {
                        try {
                            long dateMillis = Long.parseLong((String) value);

                            value = DateUtils.dateToInteger(new DateTime(dateMillis, timeZone));
                        } catch (Exception e) {
                            log.debug("Cannot be parsed");
                        }
                    }
                }
                if (value instanceof Integer) {
                    log.debug("is integer");
                    date = DateUtils.integerToDate((Integer) value);
                    value = DateUtils.parseDate(date, datePatternKey);
                } else if (today) {
                    date = new Date();
                }
            }
            results.append(ResponseUtils.filter(value.toString()));
        }
        results.append("\"");
        results.append(prepareEventHandlers());
        results.append(prepareStyles());
        results.append(getElementClose());
        if (isCalendarPicker()) {
            if (Constants.UIMode.BOOTSTRAP.getConstant().equals(mode)){
                bootstrapDatePicker(results);
            }else {
                defaultCalendarPicker(results);
            }
        }
        return results;
    }

    private void defaultCalendarPicker(StringBuffer results) {
        results.append("<a href=\"javascript:openCalendarPicker(document.getElementById('")
                .append(getStyleId()).append("'));\" title=\"")
                .append(JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Calendar.open"))
                .append("\"");
        if (tabindex != null) {
            results.append(" tabindex=\"").append(tabindex).append("\"");
        }
        results.append(">\n<img src=\"").append(pageContext.getSession().getAttribute("baselayout"))
                .append("/img/calendarPicker.gif\" align=\"middle\" alt=\"")
                .append(JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Calendar.open"))
                .append("\" border=\"0\"/></a>");
    }

    private void bootstrapDatePicker(StringBuffer results) {
        results.append("<span class=\"input-group-addon\" title=\"")
               .append(JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Calendar.open"))
               .append("\"><i class=\"glyphicon glyphicon-calendar\"></i>");
        results.append( "<script>\n" +
                        "  function enableDatePicker() {\n" +
                        "    $('.input-group.date').datepicker({" +
                        "    format: \""+getDatePatternKeyJavaScript()+"\",\n" +
                        "    todayBtn:  \"linked\",\n" +
                        "    todayHighlight: true,\n" +
                        "    showOnFocus: false,\n" +
                        "    clearBtn: true,\n" +
                        "    language: \""+getLanguageLocale()+"\",\n" +
                        "    orientation: \"top left\",\n" +
                        "    autoclose: true});\n" +
                        "  }  \n" +
                        "    $(window).ready(enableDatePicker); $(window).resize(enableDatePicker);\n" +
                        "</script></span>");
    }

    private String getDatePatternKeyJavaScript(){
        if ((getJavaScriptDatePattern() !=null) && !(getJavaScriptDatePattern().isEmpty())){
            return getJavaScriptDatePattern();
        }else {
            return JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "datePattern.javaScript");
        }
    }
    private String getLanguageLocale(){
        User user = com.piramide.elwis.web.common.util.RequestUtils.getUser((HttpServletRequest)pageContext.getRequest());
        if(user.getValue("locale") != null){
            return user.getValue("locale").toString();
        }else {
            return "en";
        }
    }
    private String getPatternSeparator() {
        final String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        char[] patternAsArray = datePatternKey.toCharArray();
        for (char element : patternAsArray) {
            if (!alphabet.contains(String.valueOf(element))) {
                return String.valueOf(element);
            }
        }
        return null;
    }

    public boolean isParseLongAsDate() {
        return parseLongAsDate;
    }

    public void setParseLongAsDate(boolean parseLongAsDate) {
        this.parseLongAsDate = parseLongAsDate;
    }

    public void release() {
        super.release();
        accept = null;
        name = "org.apache.struts.taglib.html.BEAN";
        redisplay = true;
    }
}
