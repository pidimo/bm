package com.piramide.elwis.web.common.deduplication;

import no.priv.garshol.duke.Comparator;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2.3
 */
public class PropertyConfig {
    private Comparator comparator;
    private double low;
    private double high;

    public PropertyConfig(Comparator comparator, double low, double high) {
        this.comparator = comparator;
        this.low = low;
        this.high = high;
    }

    public Comparator getComparator() {
        return comparator;
    }

    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }
}
