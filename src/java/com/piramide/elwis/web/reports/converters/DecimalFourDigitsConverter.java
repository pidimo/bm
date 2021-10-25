package com.piramide.elwis.web.reports.converters;

/**
 * Decimal converter to 4 floating point digits
 * @author Miguel A. Rojas Cardenas
 * @version 4.5
 */
public class DecimalFourDigitsConverter extends DecimalConverter {
    @Override
    public String getPatternKey() {
        return "numberFormat.4DecimalPlaces";
    }

    @Override
    protected int getMaxIntPart() {
        return 10;
    }

    @Override
    protected int getMaxFloatPart() {
        return 4;
    }
}
