package com.piramide.elwis.web.common.util;

import com.jatun.titus.listgenerator.structure.filter.ResourceBundleWrapper;
import com.piramide.elwis.utils.Constants;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResources;

import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: tayes
 * Date: Feb 24, 2006
 * Time: 5:14:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ElwisBundle implements ResourceBundleWrapper {
    private MessageResources messages;
    private Locale locale;

    public void initialize(Locale locale) {
        this.locale = locale;
        messages = PropertyMessageResources.getMessageResources(Constants.APPLICATION_RESOURCES);
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
}
