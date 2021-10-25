package com.piramide.elwis.utils;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Utilities for sort collections by property.
 *
 * @author Fernando Montaño
 * @version $Id: SortUtils.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class SortUtils {
    private static Log log = LogFactory.getLog(SortUtils.class);

    /**
     * Order an ArrayList by a property.
     *
     * @param items    Collection with items to sort
     * @param property the property to sort
     * @return sorted by property collection.
     */
    public static synchronized ArrayList orderByProperty(ArrayList items, final String property) {
        Iterator it = items.iterator();
        SortedSet byName = new TreeSet(new Comparator() {
            public int compare(Object a, Object b) {
                String aux1 = null;
                String aux2 = null;
                try {
                    aux1 = (String) a.getClass().getMethod("get" + StringUtils.capitalize(property),
                            new Class[]{}).invoke(a, new Class[]{});

                    aux2 = (String) b.getClass().getMethod("get" + StringUtils.capitalize(property),
                            new Class[]{}).invoke(b, new Class[]{});

                } catch (Exception e) {
                    log.error("Unexpected error trying to sort", e);
                }
                return (aux1.compareToIgnoreCase(aux2));
            }
        });
        while (it.hasNext()) {
            Object aux = (Object) it.next();
            byName.add(aux);
        }
        return new ArrayList(byName);
    }

    public static synchronized ArrayList orderByPropertyMap(List itemsMap, final String property) {
        Iterator it = itemsMap.iterator();
        SortedSet byName = new TreeSet(new Comparator() {
            public int compare(Object a, Object b) {
                String aux1 = null;
                String aux2 = null;
                try {
                    aux1 = (String) ((Map) a).get(property);

                    aux2 = (String) ((Map) b).get(property);

                } catch (Exception e) {
                }
                return (aux1.compareToIgnoreCase(aux2));
            }
        });
        while (it.hasNext()) {
            Object aux = (Object) it.next();
            byName.add(aux);
        }
        return new ArrayList(byName);
    }

    /**
     * This method sorts List of maps by <b>orderKey</b> parameter,
     * the object that contains the <b>orderKey</b> can be java.lang.Integer Object
     *
     * @param listOfMaps list that conains Map objects can be sorted
     * @param orderKey   key that the sort the maps, the value of this key can be java.lang.Integer Object
     * @return sorted List of maps
     */
    public static synchronized List orderListOfMapsByProperty(List listOfMaps, final String orderKey) {
        log.debug("orderListOfMapsByProperty(java.util.List, '" + orderKey + "')");

        Comparator c = new Comparator() {
            public int compare(Object a, Object b) {
                int value = 0;
                Integer aux1 = null;
                Integer aux2 = null;
                try {
                    aux1 = new Integer((String) ((Map) a).get(orderKey));

                    aux2 = new Integer((String) ((Map) b).get(orderKey));

                    if (aux1.intValue() > aux2.intValue()) {
                        value = 1;
                    }
                    if (aux1.intValue() == aux2.intValue()) {
                        value = 0;
                    }
                    if (aux1.intValue() < aux2.intValue()) {
                        value = -1;
                    }

                } catch (Exception e) {
                }
                return value;
            }
        };
        Collections.sort(listOfMaps, c);

        return listOfMaps;
    }

    /**
     * Order an ArrayList by a property, this can to contain duplicate property.
     *
     * @param listOfMaps Collection with items to sort
     * @param orderKey   the property to sort
     * @return sorted by property collection.
     */
    public static synchronized List orderListWithDuplicate(List listOfMaps, final String orderKey) {
        log.debug("orderListOfMapsByProperty(java.util.List, '" + orderKey + "')");

        Comparator c = new Comparator() {
            public int compare(Object a, Object b) {
                String aux1 = null;
                String aux2 = null;
                try {
                    aux1 = (String) a.getClass().getMethod("get" + StringUtils.capitalize(orderKey),
                            new Class[]{}).invoke(a, new Class[]{});

                    aux2 = (String) b.getClass().getMethod("get" + StringUtils.capitalize(orderKey),
                            new Class[]{}).invoke(b, new Class[]{});

                } catch (Exception e) {
                    log.error("Unexpected error trying to sort", e);
                }
                return (aux1.compareToIgnoreCase(aux2));
            }
        };
        Collections.sort(listOfMaps, c);

        return listOfMaps;
    }
}
