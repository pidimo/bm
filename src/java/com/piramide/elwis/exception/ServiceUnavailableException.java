package com.piramide.elwis.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Ivan Alban 12:04:49
 * @version 2.0
 */
public class ServiceUnavailableException extends Exception {
    private Log log = LogFactory.getLog(this.getClass());

    public ServiceUnavailableException(Exception e) {
        super();
        log.error("Service Unavailable ", e);
    }

    public ServiceUnavailableException(String message) {
        super(message);
    }
}
