package com.piramide.elwis.web.common.template;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Sep 22, 2005
 * Time: 6:02:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class TemplateResources {
    private MessageResources messages;
    private Locale locale;

    public TemplateResources(HttpServletRequest request) {
        this.messages = PropertyMessageResources.getMessageResources(Constants.APPLICATION_RESOURCES);
        locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getMessage(String key) {
        return returnValidResource(key, messages.getMessage(locale, key));
    }

    public String getMessage(String key, Object[] args) {
        return returnValidResource(key, messages.getMessage(locale, key, args));
    }

    private String returnValidResource(String key, String message) {
        if (message != null) {
            return message.trim();
        } else {
            return "???" + key + "???";
        }
    }

    public String getDateFormatted(Integer date, String keyFormatter) {
        String formatter = getMessage(keyFormatter);
        return DateUtils.parseDate(DateUtils.integerToDate(date), formatter);
    }

    public String getDateMillisFormatted(Long date, String keyFormatter, org.joda.time.DateTimeZone timeZone) {
        String pattern = getMessage(keyFormatter);
        return DateUtils.getFormattedDateTimeWithTimeZone(date.toString(), timeZone, pattern);
    }
}
