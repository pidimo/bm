package com.jatun.bm.integration.udabol.delegate;

import javax.ejb.EJBHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides JNDI lookup and object caching.
 *
 * @author kaz
 * @version $Id: ServiceLocator.java,v 1.1 2004/04/23 08:13:11 kaz Exp $
 */
public class ServiceLocator {

    /**
     * Singleton instance.
     */
    public static final ServiceLocator i = new ServiceLocator();

    private ServiceLocator() {
    }

    private final Map cacheMap = new HashMap();

    /**
     * Looks up an Object on JNDI serivce with specified JNDI name. The retrieved
     * object will be cached.
     *
     * @param jndiName JNDI name of target object
     * @return Object       target object
     * @throws NamingException For any lookup errors
     */
    public synchronized Object lookup(String jndiName) throws NamingException {

        Object obj = cacheMap.get(jndiName);
        if (obj == null) {
            final Context ctx = new InitialContext();
            obj = ctx.lookup(jndiName);
            cacheMap.put(jndiName, obj);
        }
        return obj;
    }

    /**
     * Looks up an EJBHome on JNDI service with specified JNDI name. The
     * retribed EJBHome will be narrowed by PortableRemoteObject.
     *
     * @param jndiName JNDI name of target object
     * @param type     EJBHome class
     * @return Object       target object
     * @throws NamingException For any lookup errors
     */
    public synchronized EJBHome lookupAndNarrow(String jndiName, Class type)
            throws NamingException {

        final Object obj = lookup(jndiName);
        return (EJBHome) PortableRemoteObject.narrow(obj, type);
    }

    /**
     * Clears cache.
     */
    public synchronized void clearCache() {
        cacheMap.clear();
    }

    /**
     * Returns a String that is read from specified environment
     * entry.
     */
    public String getEnvEntry(String jndiName) {
        try {
            return (String) lookup(jndiName);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}