package com.piramide.elwis.web.common.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: NumberFormatValidator.java 10109 2011-07-13 22:40:26Z miguel $
 */

public class NumberFormatValidator {
    private Log log = LogFactory.getLog(NumberFormatValidator.class);
    private char decimalSymbol = ' ';
    private char groupSymbol = ' ';
    private char minusSymbol = ' ';
    private NumberFormat numberFormat;
    public static final int VALID = 1;
    public static final int INVALID = 0;
    public static final int OUT_OF_RANGE = -1;
    public static final int ONLY_POSITIVE = -2;
    public static final int ONLY_NEGATIVE = -3;

    /**
     * Singleton instance.
     */

    public static final NumberFormatValidator i = new NumberFormatValidator();

    private NumberFormatValidator() {
    }

    /**
     * Validates String that represents a Number according locale
     *
     * @param source       String that contains the number
     * @param locale       locale that parsing the number
     * @param maxIntPart   the max number of digits that contains integer part
     * @param maxFloatPart the max number of digits that contains float part
     * @return VALID = 1, INVALID = 0, OUT_OF_RANGE = -1
     */

    public int validate(String source, Locale locale, int maxIntPart, int maxFloatPart) {

        log.debug("Executing NumberFormatValidation...");
        settingUpValuesOfNumberFormat(locale, maxIntPart, maxFloatPart);

        if (havingMinusSymbol(source)) {
            source = extractsMinusSymbol(source);
        }

        String intPart = getIntPart(source);

        String floatPart = "";
        try {
            floatPart = removeRightZeros(getFloatPart(source), locale);
        } catch (ParseException e) {
            return INVALID;
        } catch (NumberFormatException e) {
            return INVALID;
        }


        String newIntPart = "";
        String newFloatPart = "";

        String groupingIntPart = getIntPartWhitGroupSymbol(source);

        if ("".equals(intPart)) {
            intPart = "0";
        }
        if ("".equals(floatPart)) {
            floatPart = "0";
        }
        try {
            newIntPart = ((DecimalFormat) numberFormat).format(new Double(intPart).doubleValue());

            String auxFloatpart = String.valueOf((numberFormat.parse(floatPart)).doubleValue());
            newFloatPart = ((DecimalFormat) numberFormat).format(new Double(auxFloatpart).doubleValue());
        } catch (NumberFormatException e) {
            log.debug("Error el numero " + source + " no es valido no se pudo realizar el formateo...");
            return INVALID;
        } catch (ParseException e) {
            log.debug("Error el numero " + source + " no es valido no se pudo realizar el parseo de la parte flotante...");
            return INVALID;
        }
        if (intPart.length() <= maxIntPart) {
            if (!newIntPart.equals(groupingIntPart) && !"0".equals(intPart)) {
                log.debug("Error el numero " + source + " no es valido los signos de agrupacion no son los correctos...");
                return INVALID;
            }
            if (!newFloatPart.equals(floatPart) && !"0".equals(floatPart) && !"0".equals(newFloatPart)) {
                log.debug("Error el numero " + source + " con parte Flotante : " + floatPart + " no es valido la parte flotante no es la correcta...");
                return INVALID;
            }
        } else {
            log.debug("Error el numero " + source + " no es valido el tamao de la parte entera excede a lo permitido...");
            return OUT_OF_RANGE;
        }
        return VALID;
    }

    private String removeRightZeros(String decimalPart, Locale locale) throws NumberFormatException, ParseException {

        int index = decimalPart.indexOf(decimalSymbol);
        String testNumeric = decimalPart.substring(index + 1);
        if (!"".equals(testNumeric) && !StringUtils.isNumeric(testNumeric)) {
            throw new NumberFormatException();
        }

        if (!"".equals(decimalPart)) {
            String withoutZeros = decimalPart;
            while (withoutZeros.endsWith("0") && withoutZeros.length() > index + 2) {
                withoutZeros = withoutZeros.substring(0, withoutZeros.length() - 1);
            }
            return withoutZeros;
        }

        /*//this validation not work with 15.0002 value
        if (!"".equals(decimalPart)) {
            NumberFormat formatter = NumberFormat.getInstance(locale);
            Number n = formatter.parse(decimalPart);
            String tmp = String.valueOf(n.doubleValue());
            tmp = StringUtils.replace(tmp, ".", String.valueOf(decimalSymbol));
            return tmp;
        }*/
        return decimalPart;

    }

    /**
     * Validates String that represents a Number positive according locale
     *
     * @param source       String that contains the number
     * @param locale       locale that parsing the number
     * @param maxIntPart   the max number of digits that contains integer part
     * @param maxFloatPart the max number of digits that contains float part
     * @return VALID = 1, INVALID = 0, OUT_OF_RANGE = -1, ONLY_POSITIVE = -2
     */
    public int validatePositive(String source, Locale locale, int maxIntPart, int maxFloatPart) {
        settingUpValuesOfNumberFormat(locale, maxIntPart, maxFloatPart);
        if (havingMinusSymbol(source)) {
            return ONLY_POSITIVE;
        } else {
            return validate(source, locale, maxIntPart, maxFloatPart);
        }
    }

    /**
     * Validates String that represents a Number negative according locale
     *
     * @param source       String that contains the number
     * @param locale       locale that parsing the number
     * @param maxIntPart   the max number of digits that contains integer part
     * @param maxFloatPart the max number of digits that contains float part
     * @return VALID = 1, INVALID = 0, OUT_OF_RANGE = -1, ONLY_NEGATIVE = -3
     */
    public int validateNegative(String source, Locale locale, int maxIntPart, int maxFloatPart) {
        settingUpValuesOfNumberFormat(locale, maxIntPart, maxFloatPart);
        if (havingMinusSymbol(source)) {
            return validate(source, locale, maxIntPart, maxFloatPart);
        } else {
            return ONLY_NEGATIVE;
        }
    }

    private void settingUpValuesOfNumberFormat(Locale locale, int maxIntPart, int maxFloatPart) {
        numberFormat = NumberFormat.getInstance(locale);
        numberFormat.setMaximumFractionDigits(maxFloatPart);
        numberFormat.setMaximumIntegerDigits(maxIntPart);

        decimalSymbol = ((DecimalFormat) numberFormat).getDecimalFormatSymbols().getDecimalSeparator();
        minusSymbol = ((DecimalFormat) numberFormat).getDecimalFormatSymbols().getMinusSign();
        groupSymbol = ((DecimalFormat) numberFormat).getDecimalFormatSymbols().getGroupingSeparator();

    }

    private String getFloatPart(String source) {
        int decimalPoint = source.indexOf(decimalSymbol);
        if (decimalPoint == -1) {
            return "";
        } else {
            if (decimalPoint < source.length()) {
                return "0" + source.substring(decimalPoint);
            } else {
                return "";
            }
        }
    }

    private String getIntPart(String source) {
        int decimalPoint = source.indexOf(decimalSymbol);
        if (decimalPoint == -1) {
            int groupPoint = source.indexOf(groupSymbol);
            if (groupPoint == -1) {
                return source;
            } else {
                return extractsGroupSymbol(source);
            }
        } else {
            String newSource = source.substring(0, decimalPoint);
            int groupPoint = newSource.indexOf(groupSymbol);
            if (groupPoint == -1) {
                return newSource;
            } else {
                return extractsGroupSymbol(newSource);
            }
        }
    }

    private String getIntPartWhitGroupSymbol(String source) {
        int gropPoint = source.indexOf(groupSymbol);
        if (gropPoint == -1) {
            int decimalPoint = source.indexOf(decimalSymbol);
            if (decimalPoint == -1) {
                return putsGroupSymbol(source);
            } else {
                return putsGroupSymbol(source.substring(0, decimalPoint));
            }
        } else {
            int decimalPoint = source.indexOf(decimalSymbol);
            if (decimalPoint == -1) {
                return source;
            } else {
                return source.substring(0, decimalPoint);
            }
        }
    }

    private String extractsGroupSymbol(String source) {
        String result = "";
        char[] sourceArray = source.toCharArray();
        for (int j = 0; j < sourceArray.length; j++) {
            char c = sourceArray[j];
            if (c != groupSymbol) {
                result += String.valueOf(c);
            }
        }
        return result;
    }

    private String putsGroupSymbol(String source) {
        String result = "";
        char[] sourceArray = source.toCharArray();
        int arraySize = sourceArray.length;
        int counter = 0;
        if (arraySize >= 4) {
            for (int j = arraySize - 1; j >= 0; j--) {
                char c = sourceArray[j];
                if (counter < 3) {
                    result = String.valueOf(c) + result;
                    counter++;
                }
                if (counter == 3) {
                    counter = 0;
                    result = String.valueOf(groupSymbol) + result;
                }
            }
            if (result.toCharArray()[0] == groupSymbol) {
                result = result.substring(1);
            }
        } else {
            return source;
        }
        return result;
    }

    private boolean havingMinusSymbol(String source) {
        int minusPoint = source.indexOf(minusSymbol);
        if (minusPoint == 0 && source.length() > 1) {
            return true;
        } else {
            return false;
        }
    }

    private String extractsMinusSymbol(String source) {
        String result = "";
        int minusPoint = source.indexOf(minusSymbol);
        try {
            result = source.substring(minusPoint + 1);
        } catch (StringIndexOutOfBoundsException e) {
            return result;
        }
        return result;
    }

}
