package com.jatun.bm.integration.udabol.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Fernando Javier Montaño Torrico
 * @version 1.0
 */
public class Messages {

    public static final String BUNDLE = "com.jatun.bm.integration.udabol.resources.messages";

    public static final Messages i = new Messages();

    private Messages() {
    }

    public String getMessage(
            String key,
            Object... parameters) {
        String message = "???" + key + "???";

        ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE, Locale.getDefault(), getClassLoader(parameters));
        try {
            message = resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return message;
        }
        if (parameters != null) {
            StringBuffer stringBuffer = new StringBuffer();
            MessageFormat messageFormat = new MessageFormat(message,
                    Locale.getDefault());
            message = messageFormat.format(parameters, stringBuffer,
                    null).toString();
        }
        return message;
    }

    private ClassLoader getClassLoader(Object defaultObject) {
        ClassLoader loader =
                Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = defaultObject.getClass().getClassLoader();
        }
        return loader;
    }
}
