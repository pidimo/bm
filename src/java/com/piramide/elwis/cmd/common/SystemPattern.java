package com.piramide.elwis.cmd.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Jatun S.R.L.
 * System pattern constant, used to generate document
 *
 * @author Miky
 * @version $Id: SystemPattern.java 22-sep-2008 10:47:23 $
 */
public class SystemPattern {
    public static final String DEFAULT_ISOLANG = "en";

    /**
     * System date pattern map key= language ISO code, value= date pattern
     */
    public static Map<String, String> datePattern = new HashMap<String, String>();

    /**
     * System decimal pattern map key= language ISO code, value= decimal pattern
     */
    public static Map<String, String> decimalPattern = new HashMap<String, String>();

    /**
     * System decimal four digits pattern map key= language ISO code, value= decimal pattern
     */
    public static Map<String, String> decimalPattern4Digits = new HashMap<String, String>();

    public static String getDatePattern(String isoLanguage) {
        if (datePattern.containsKey(isoLanguage)) {
            return datePattern.get(isoLanguage);
        } else {
            return datePattern.get(DEFAULT_ISOLANG);
        }
    }

    public static String getDecimalPattern(String isoLanguage) {
        if (decimalPattern.containsKey(isoLanguage)) {
            return decimalPattern.get(isoLanguage);
        } else {
            return decimalPattern.get(DEFAULT_ISOLANG);
        }
    }

    public static String getDecimalPattern4Digits(String isoLanguage) {
        if (decimalPattern4Digits.containsKey(isoLanguage)) {
            return decimalPattern4Digits.get(isoLanguage);
        } else {
            return decimalPattern4Digits.get(DEFAULT_ISOLANG);
        }
    }

}
