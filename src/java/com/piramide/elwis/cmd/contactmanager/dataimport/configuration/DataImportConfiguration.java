package com.piramide.elwis.cmd.contactmanager.dataimport.configuration;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class DataImportConfiguration {
    private boolean skipHeader = false;
    private Integer type;
    private String datePattern;
    private String decimalPattern;
    private String locale;

    public DataImportConfiguration(boolean skipHeader, Integer type, String datePattern, String decimalPattern, String locale) {
        this.skipHeader = skipHeader;
        this.type = type;
        this.datePattern = datePattern;
        this.decimalPattern = decimalPattern;
        this.locale = locale;
    }

    public boolean isSkipHeader() {
        return skipHeader;
    }

    public Integer getType() {
        return type;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public String getDecimalPattern() {
        return decimalPattern;
    }

    public String getLocale() {
        return locale;
    }
}
