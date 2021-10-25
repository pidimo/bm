package com.piramide.elwis.utils.logging;

import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Util to print log info if configuration property is enabled
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.1
 */
public class LogInfoFactory {
    public static LogInfoFactory i = new LogInfoFactory();

    public LogInfoFactory() {
    }

    public void log(Class clazz, Object text) {
        log(clazz, text, null);
    }

    public void log(Class clazz, Object text, Throwable e) {
        if (isActiveLog()) {
            Log log = LogFactory.getLog(clazz);
            if (e != null) {
                log.info(text, e);
            } else {
                log.info(text);
            }
        }
    }

    public boolean isActiveLog() {
        return "true".equals(ConfigurationFactory.getValue("elwis.infoLog.enable").trim());
    }
}
