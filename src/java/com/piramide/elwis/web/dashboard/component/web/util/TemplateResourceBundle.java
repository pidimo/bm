package com.piramide.elwis.web.dashboard.component.web.util;

import com.piramide.elwis.web.dashboard.component.ui.velocity.ResourceBundleManager;
import com.piramide.elwis.web.dashboard.component.util.Constant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.text.*;
import java.util.Locale;

/**
 * @author : ivan
 */
public class TemplateResourceBundle implements ResourceBundleManager {
    private MessageResources messages;
    private Locale locale;
    private Log log = LogFactory.getLog(this.getClass());

    public TemplateResourceBundle(HttpServletRequest request, String applicationResourcesPath) {
        this.messages = PropertyMessageResources.getMessageResources(applicationResourcesPath);
        locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        if (null == locale) {
            locale = new Locale("en");
        }
    }

    public TemplateResourceBundle(String applicationResourcesPath, Locale locale) {
        this.messages = PropertyMessageResources.getMessageResources(applicationResourcesPath);
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


    public String formatColumn(Object value, String pattern) {
        if (pattern.indexOf('#') == -1) {
            return formatDate(value, pattern);
        } else {
            return formatNumber(value, pattern);
        }
    }

    public String calculateSizeColum(Object columnSize) {
        int valueAsInt = -1;
        try {
            valueAsInt = new Integer(columnSize.toString());
        } catch (NumberFormatException nfe) {
            log.error("Cannot calulate Size for Value " + columnSize + ", because ", nfe);
        } catch (NullPointerException npe) {
            log.error("Cannot calulate Size for Value " + columnSize + ", because ", npe);
        }

        if (valueAsInt > -1) {
            float tableSize = Constant.SCREEN_SIZE / 2;
            float factor = tableSize / 100;
            int size = (int) (valueAsInt * factor);
            return String.valueOf(size);
        }
        return "0";
    }

    private String formatDate(Object value, String pattern) {
        DateFormat formatter = new SimpleDateFormat(pattern, locale);
        String formatted;
        formatted = formatter.format(value);
        return formatted;
    }

    private String formatNumber(Object value, String pattern) {
        String aux = value.toString();
        Number n;
        if (aux.indexOf('.') > -1 || aux.indexOf(',') > -1) {
            n = new Double(aux);
        } else {
            n = new Long(aux);
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        NumberFormat formatter = new DecimalFormat(pattern, symbols);
        String localizedPatternFromObject = ((DecimalFormat) formatter).toLocalizedPattern();
        if (null != localizedPatternFromObject && localizedPatternFromObject.indexOf('%') > -1) {
            ((DecimalFormat) formatter).setMultiplier(1);
        }

        String formatted;
        formatted = formatter.format(n);
        log.debug("Formatted value :" + formatted);
        return formatted;
    }

    public Locale getLocale() {
        return this.locale;
    }
}
