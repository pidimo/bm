package com.piramide.elwis.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Set "" string to null in ParamDTO map
 *
 * @author Ernesto
 * @version $Id: HashMapCleaner.java 7936 2007-10-27 16:08:39Z fernando $
 * @deprecated By ExtendedCRUDDirector and ExtendedDTOFactoy have this operations
 */
public class HashMapCleaner {
    private static Log log = LogFactory.getLog(HashMapCleaner.class);

    public static void clean(HashMap map) {

        //log.debug("Content of paramDTO hash: " + map.toString());

        Iterator it = map.values().iterator();
        while (it.hasNext()) {
            Object value = it.next();
            //if(value instanceof String)  TODO:Posible cambio
            if (String.class.equals(value.getClass())) {
                String val = (String) value;
                if (value == null || val.equals("")) {
                    it.remove();
                    it = map.values().iterator();
                }
            }

        }
    }

    public static void clean(Map map) {

        //log.debug("Content of paramDTO hash: " + map.toString());

        Iterator it = map.values().iterator();
        while (it.hasNext()) {
            Object value = it.next();
            if (value instanceof String) {
                String val = (String) value;
                if (value == null || val.equals("")) {
                    it.remove();
                    it = map.values().iterator();
                }
            }

        }
    }
}
