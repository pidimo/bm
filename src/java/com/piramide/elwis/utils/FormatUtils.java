package com.piramide.elwis.utils;

import org.apache.commons.validator.GenericValidator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: FormatUtils.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class FormatUtils {

    public static String TWO_DECIMAL_PATTERN = "#,##0.00";


    /**
     * Formats a number given a decimal pattern and returns an string with the formatted values.
     *
     * @param number  the number to format
     * @param locale  the locale
     * @param pattern the patter for the formatting
     * @return the string formatted
     */
    public static String formatDecimal(Number number, Locale locale, String pattern) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        NumberFormat formatter = new DecimalFormat(pattern, symbols);
        try {
            return formatter.format(number);
        } catch (NumberFormatException e) {
            return number.toString();
        }
    }


    /**
     * Translate an String to BigDecimal number.
     *
     * @param cadena       the string that contains the number
     * @param locale       the locale
     * @param maxIntPart   this the max of digits on the integer part
     * @param maxFloatPart this the max of digits on the decimal part
     * @return the bigdecimal number
     */
    public static BigDecimal unformatingDecimalNumber(String cadena, Locale locale, int maxIntPart, int maxFloatPart) {
        BigDecimal result = null;
        String value = cadena;

        NumberFormat numberFormat = settingUpValuesOfFormat(locale, maxIntPart, maxFloatPart);

        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                Number number = numberFormat.parse(value);
                result = new BigDecimal(new Double(number.doubleValue()).toString());
            } catch (ParseException e) {
            }
        }
        return result;
    }

    /**
     * Validates if the cadena value represents an BigDecimal positive
     *
     * @param cadena       the string that contains the number
     * @param locale       the locale
     * @param maxIntPart   this the max of digits on the integer part
     * @param maxFloatPart this the max of digits on the decimal part
     * @return true if bigdecimal value is positive, false the other wise
     */
    public static boolean isPositiveDecimalNumber(String cadena, Locale locale, int maxIntPart, int maxFloatPart) {
        boolean isPositive = false;
        BigDecimal number = unformatingDecimalNumber(cadena, locale, maxIntPart, maxFloatPart);
        if (number != null && number.floatValue() >= 0) {
            isPositive = true;
        }
        return isPositive;
    }

    /**
     * formatting the object (obj) to a bigDecimal taking into account the locale
     *
     * @param obj          the object that contains the number
     * @param locale       the locale
     * @param maxIntPart   this the max of digits on the integer part
     * @param maxFloatPart this the max of digits on the decimal part
     * @return Object that contains the bigdecimal formatted for the views
     */
    public static Object formatingDecimalNumber(Object obj, Locale locale, int maxIntPart, int maxFloatPart) {
        Object result = obj;

        NumberFormat numberFormat = settingUpValuesOfFormat(locale, maxIntPart, maxFloatPart);
        try {
            Double number = new Double(obj.toString());
            result = numberFormat.format(number.doubleValue());
        } catch (NumberFormatException e) {
        }
        return result;
    }

    /**
     * Validates if the cadena value represents an percent number
     *
     * @param obj          the object that contains the number
     * @param locale       the locale
     * @param maxIntPart   this the max of digits on the integer part
     * @param maxFloatPart this the max of digits on the decimal part
     * @return Object that contains the bigdecimal formatted for the views
     */
    public static Object formatingPercentNumber(Object obj, Locale locale, int maxIntPart, int maxFloatPart) {
        Object result = obj;

        NumberFormat numberFormat = settingUpValuesOfFormat(locale, maxIntPart, maxFloatPart);
        try {
            Double number = new Double(obj.toString());
            result = numberFormat.format(number.doubleValue() * 100);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    /**
     * Validates if the cadena value represents an BigDecimal is valid
     *
     * @param cadena       the string that contains the number
     * @param locale       the locale
     * @param maxIntPart   this the max of digits on the integer part
     * @param maxFloatPart this the max of digits on the decimal part
     * @return true if bigdecimal value is valid, false the other wise
     */
    public static boolean isValidDecimalNumber(String cadena, Locale locale, int maxIntPart, int maxFloatPart) {
        String value = cadena;
        boolean isValid = false;
        Number numero;

        NumberFormat numberFormat = settingUpValuesOfFormat(locale, maxIntPart, maxFloatPart);
        char decimalSeparator = ((DecimalFormat) numberFormat).getDecimalFormatSymbols().getDecimalSeparator();

        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                numero = numberFormat.parse(value); //parse the number
                float number = numero.floatValue(); //extracts the float value

                String cad = ((DecimalFormat) numberFormat).format(number);   // formatting the parse number

                int intPart = (int) number; //extracts the int part
                Integer integerPart = new Integer(intPart);

                isValid = true;
                if (new Integer(intPart).toString().length() > maxIntPart) {
                    isValid = false;
                }
                if (!isCorrectParsed(cadena, cad)) {
                    if (integerPart.equals(getDecimalAndThousandthParts(cadena, decimalSeparator).get(1))) {
                        isValid = true;
                    } else {
                        isValid = false;
                    }
                }
                if (isDecimalSymbolValid(cadena, cad, decimalSeparator, maxFloatPart) == -1) {
                    isValid = false;
                }
            } catch (ParseException e) {
            }
        }
        return isValid;
    }

    /**
     * validates directly if source an parsedCad
     *
     * @param source    String that contains the source number
     * @param parsedCad Strinf that contains the parsed number
     * @return true if the source and parsedCad are equals false in otherwise
     */
    private static boolean isCorrectParsed(String source, String parsedCad) {
        boolean result = false;
        if (source.equals(parsedCad)) {
            result = true;
        }
        return result;
    }

    /**
     * validates if the decimal separator is valid
     *
     * @param n             the source string that contains the source number
     * @param n2            the parsed string that contains the pased number
     * @param decimalSymbol decimal separator
     * @param maxFloatPart  this the max of digits on the decimal part
     * @return 1 values if the decimal separator is correct -1 if the decimal separator is incorrect
     *         and 0 if decimal separator doesn't exist
     */
    private static int isDecimalSymbolValid(String n, String n2, char decimalSymbol, int maxFloatPart) {
        int result = 0;
        int counter = 0;
        int indexDecimalPoint = n2.indexOf(decimalSymbol);
        if (indexDecimalPoint > 0) {
            result = 1;
            String nDecimalPart = n.substring(indexDecimalPoint);
            StringTokenizer token = new StringTokenizer(nDecimalPart, String.valueOf(decimalSymbol));
            while (token.hasMoreElements()) {
                counter++;
                String aux = token.nextToken();
                if (aux != null || !"".equals(aux)) {
                    nDecimalPart = aux;
                }
            }
            if (counter == 1 && nDecimalPart.length() <= maxFloatPart) {
                result = 1;
            } else {
                result = -1;
            }
        }
        return result;
    }

    /**
     * change the string that represents an decimal number into list decimalpart and intpart
     *
     * @param source        source string that contains the number
     * @param decimalSymbol decimal separator
     * @return an list first element is decimal part and second element is integer part
     */
    private static ArrayList getDecimalAndThousandthParts(String source, char decimalSymbol) {
        ArrayList result = new ArrayList(2);
        String decimalPart = "";
        String intPart = "";

        int decimalPoint = source.indexOf(decimalSymbol);
        if (decimalPoint > 0) {
            //separates the source String into decimal an integer part
            decimalPart = source.substring(decimalPoint + 1);
            intPart = source.substring(0, decimalPoint);
        } else {
            intPart = source; // all source string setting up with intPart
        }
        try {
            result.add(Integer.valueOf(decimalPart));
        } catch (NumberFormatException e) {
            result.add(null); // if the decimal number cannot is parsed
        }
        try {
            result.add(Integer.valueOf(intPart));
        } catch (NumberFormatException e) {
            result.add(null); //if the integer number cannot is parsed
        }
        return result;
    }

    /**
     * Setting up the values for the instance an numberformat
     *
     * @param locale       the locale
     * @param maxIntPart   this the max of digits on the integer part
     * @param maxFloatPart this the max of digits on the decimal part
     * @return numberformat
     */
    private static NumberFormat settingUpValuesOfFormat(Locale locale, int maxIntPart, int maxFloatPart) {
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        numberFormat.setMaximumFractionDigits(maxFloatPart);
        numberFormat.setMaximumIntegerDigits(maxIntPart);
        return numberFormat;
    }


    /**
     * Convert each element of the list to a only one string using the separator(if defined)
     *
     * @param stringlist the list of strings
     * @param separator  the separator to apply, it can be null,if it is not necessary.
     * @return the string formatted
     */
    public static String listAsString(List stringlist, String separator) {
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = stringlist.iterator(); iterator.hasNext();) {
            try {
                sb.append((String) iterator.next());
                if (separator != null && iterator.hasNext()) {
                    sb.append(separator);
                }
            } catch (ClassCastException e) {
                new RuntimeException("Only a list of strings can be sorted by this utility method", e);
            }
        }
        return new String(sb);
    }
}

