package com.piramide.elwis.web.common.util;

import org.apache.commons.validator.GenericValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: FormatUtils.java 10119 2011-07-18 19:51:12Z miguel $
 */

public class FormatUtils {

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
        try {
            Double number = new Double(obj.toString());
            maxFloatPart = fixFormatMaxFloatPart(number, maxFloatPart);
            NumberFormat numberFormat = settingUpValuesOfFormat(locale, maxIntPart, maxFloatPart);

            result = numberFormat.format(number.doubleValue());
        } catch (NumberFormatException e) {
        }
        return result;
    }


    public static Object formatingDecimalNumber(Object obj, HttpServletRequest request, int maxIntPart, int maxFloatPart) {
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
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
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        numberFormat.setMaximumFractionDigits(maxFloatPart);
        numberFormat.setMaximumIntegerDigits(maxIntPart);
        numberFormat.setMinimumFractionDigits(maxFloatPart);
        return numberFormat;
    }


    public static boolean isPercent(String cad, float maxValue) {
        boolean result = false;
        try {
            Float value = new Float(cad);
            if (value.floatValue() < maxValue) {
                result = true;
            }

        } catch (NumberFormatException e) {
            return result;
        }
        return result;
    }

    public static BigDecimal toPercent(String cad) {
        Float floatValue = new Float(((new Float(cad)).floatValue() / 100.000));
        return new BigDecimal(floatValue.toString());
    }

    public static String unformatDecimalNumber(String number,
                                               int maxIntPart,
                                               int maxFloatPart, HttpServletRequest request) {
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);

        return FormatUtils.unformatingDecimalNumber(number, locale, maxIntPart, maxFloatPart).toString();
    }

    private static int fixFormatMaxFloatPart(Double value, int maxFloatPart) {
        int fixedMaxFloatPart = maxFloatPart;

        if (maxFloatPart > 2) {
            int valueFractionDigits = getFractionDigits(value);
            if (valueFractionDigits < maxFloatPart) {
                if (valueFractionDigits > 2) {
                    fixedMaxFloatPart = valueFractionDigits;
                } else {
                    //by default is 2 digits
                    fixedMaxFloatPart = 2;
                }
            }
        }

        return fixedMaxFloatPart;
    }

    private static int getFractionDigits(Double doubleValue) {
        int fractionDigits = -1;

        if (doubleValue != null) {
            String doubleValueStr = BigDecimal.valueOf(doubleValue).toPlainString();
            int separatorIndex = doubleValueStr.indexOf('.');

            if (separatorIndex > -1) {
                //remove right zeros decimal part
                while (doubleValueStr.endsWith("0") && doubleValueStr.length() > separatorIndex + 2) {
                    doubleValueStr = doubleValueStr.substring(0, doubleValueStr.length() - 1);
                }

                String fraction = doubleValueStr.substring(separatorIndex + 1);
                fractionDigits = fraction.length();
            }
        }
        return fractionDigits;
    }
}

