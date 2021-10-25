package com.piramide.elwis.web.common.util;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * Alfacentauro Team
 * this class help to build the list of time zones, save a instance of time zone
 *
 * @author miky
 * @version $Id: TimeZoneData.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class TimeZoneData implements Comparable {
    static final long cNow = System.currentTimeMillis();
    static final DateTimeFormatter cOffsetFormatter = new DateTimeFormatterBuilder()
            .appendTimeZoneOffset(null, true, 2, 4)
            .toFormatter();

    private final String iID;
    private final DateTimeZone iZone;

    public TimeZoneData(String id, DateTimeZone zone) {
        iID = id;
        iZone = zone;
    }

    public String getID() {
        return iID;
    }

    public String getCanonicalID() {
        return iZone.getID();
    }

    public boolean isCanonical() {
        return getID().equals(getCanonicalID());
    }

    /**
     * obtain the standard offset
     *
     * @return the offset (example: -04:00)
     */
    public String getStandardOffsetStr() {
        long millis = cNow;
        while (iZone.getOffset(millis) != iZone.getStandardOffset(millis)) {
            millis = iZone.nextTransition(millis);
        }
        return cOffsetFormatter.withZone(iZone).print(millis);
    }

    /**
     * to make a sort
     *
     * @param obj
     * @return;
     */
    public int compareTo(Object obj) {
        TimeZoneData other = (TimeZoneData) obj;

        int offsetA = iZone.getStandardOffset(cNow);
        int offsetB = other.iZone.getStandardOffset(cNow);

        if (offsetA < offsetB) {
            return -1;
        }
        if (offsetA > offsetB) {
            return 1;
        }

        int result = getCanonicalID().compareTo(other.getCanonicalID());

        if (result != 0) {
            return result;
        }

        if (isCanonical()) {
            if (!other.isCanonical()) {
                return -1;
            }
        } else if (other.isCanonical()) {
            return 1;
        }

        return getID().compareTo(other.getID());
    }
}