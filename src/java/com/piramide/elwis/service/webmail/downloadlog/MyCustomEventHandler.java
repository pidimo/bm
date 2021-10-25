package com.piramide.elwis.service.webmail.downloadlog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class MyCustomEventHandler {
    private static final Log log = LogFactory.getLog(MyCustomEventHandler.class);

    private static Map<String, Set> observables = new LinkedHashMap();

    public static void fire(MyCustomEvent event) {
        //log.trace("fire(" + event + ")");
        try {
            Set<MyCustomListener> observers = getObservables(event.getSourceId());
            if (observers != null) {
                for (final MyCustomListener pl : observers) {
                    try {
                        pl.fire(event);
                    } catch (Throwable t) {
                        log.error(t, t);
                    }
                }
            }
        } catch (Throwable t) {
            log.error(t, t);
        }
    }

    private synchronized static Set<MyCustomListener> getObservables(String sourceId) {
        return observables.get(sourceId);
    }

    /**
     * Registra a observer para avisarle cuando observable se modifique
     *
     * @param observer
     * @param observable
     */
    public static synchronized void register(MyCustomListener observer,
                                             String observable) {
        Set observers = getObservables(observable);
        if (observers == null) {
            observers = new HashSet();
        }
        observers.add(observer);
        observables.put(observable, observers);
    }

    /**
     * Deregistra a observer para no volverle a avisar cuando observable se
     * modifique
     *
     * @param observer
     * @param observable
     */
    public static synchronized void deregister(MyCustomListener observer,
                                               String observable) {
        Set observers = getObservables(observable);
        if (observers == null) {
            observers = new HashSet();
        }
        observers.remove(observer);
        observables.put(observable, observers);
    }

}