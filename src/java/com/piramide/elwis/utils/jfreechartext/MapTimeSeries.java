package com.piramide.elwis.utils.jfreechartext;

import org.jfree.data.time.TimeSeries;

/**
 * Time series wich has a map as values
 *
 * @author Fernando Montaño
 * @version $Id: MapTimeSeries.java 9123 2009-04-17 00:32:52Z fernando $
 */

public class MapTimeSeries extends TimeSeries {
    /**
     * Creates a new (empty) time series.  By default, a daily time series is created.
     * Use one of the other constructors if you require a different time period.
     *
     * @param name the series name (<code>null</code> not permitted).
     */
    public MapTimeSeries(String name) {
        super(name);
    }

    /**
     * Creates a new  (empty) time series.
     *
     * @param name            the series name (<code>null</code> not permitted).
     * @param timePeriodClass the type of time period (<code>null</code> not permitted).
     */
    public MapTimeSeries(String name, Class timePeriodClass) {
        super(name, timePeriodClass);
    }

    /**
     * Creates a new time series that contains no data.
     * <p/>
     * Descriptions can be specified for the domain and range.  One situation
     * where this is helpful is when generating a chart for the time series -
     * axis labels can be taken from the domain and range description.
     *
     * @param name            the name of the series (<code>null</code> not permitted).
     * @param domain          the domain description (<code>null</code> permitted).
     * @param range           the range description (<code>null</code> permitted).
     * @param timePeriodClass the type of time period (<code>null</code> not permitted).
     */
    public MapTimeSeries(String name, String domain, String range, Class timePeriodClass) {
        super(name, domain, range, timePeriodClass);
    }
}
