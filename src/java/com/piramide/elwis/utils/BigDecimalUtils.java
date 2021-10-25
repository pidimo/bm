package com.piramide.elwis.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Jatun S.R.L.
 * Utils to make arithmetic operations with BigDecimal and round this with scale 2
 *
 * @author Miky
 * @version $Id: BigDecimalUtils.java 12-ene-2009 15:19:50 $
 */
public class BigDecimalUtils {
    private static Log log = LogFactory.getLog(BigDecimalUtils.class);

    public static BigDecimal sum(BigDecimal num1, BigDecimal num2) {
        return roundBigDecimal(num1.add(num2));
    }

    public static BigDecimal sum(BigDecimal num1, BigDecimal num2, int scale) {
        return roundBigDecimal(num1.add(num2), scale);
    }

    public static BigDecimal subtract(BigDecimal amount, BigDecimal subtrahend) {
        return roundBigDecimal(amount.subtract(subtrahend));
    }

    public static BigDecimal subtract(BigDecimal amount, BigDecimal subtrahend, int scale) {
        return roundBigDecimal(amount.subtract(subtrahend), scale);
    }

    public static BigDecimal multiply(BigDecimal amount, BigDecimal multiplicand) {
        return roundBigDecimal(amount.multiply(multiplicand));
    }

    public static BigDecimal multiply(BigDecimal amount, BigDecimal multiplicand, int scale) {
        return roundBigDecimal(amount.multiply(multiplicand), scale);
    }

    public static BigDecimal divide(BigDecimal amount, BigDecimal divisor) {
        return roundBigDecimal(amount.divide(divisor, 2, RoundingMode.HALF_UP));
    }

    public static BigDecimal divide(BigDecimal amount, BigDecimal divisor, int scale) {
        return roundBigDecimal(amount.divide(divisor, scale, RoundingMode.HALF_UP), scale);
    }

    public static BigDecimal roundBigDecimal(BigDecimal num) {
        if (num != null) {
            return num.setScale(2, RoundingMode.HALF_UP);
        }
        return num;
    }

    public static BigDecimal roundBigDecimal(BigDecimal num, int scale) {
        if (num != null) {
            return num.setScale(scale, RoundingMode.HALF_UP);
        }
        return num;
    }

    /**
     * Calculate the percent value, this return with two digits
     * @param amount amount
     * @param percent percent
     * @return BigDecimal with all digits
     */
    public static BigDecimal getPercentage(BigDecimal amount, BigDecimal percent) {
        return divide(amount.multiply(percent), new BigDecimal(100));
    }

    public static BigDecimal getPercentage(BigDecimal amount, BigDecimal percent, int scale) {
        return divide(amount.multiply(percent), new BigDecimal(100), scale);
    }

    /**
     * Calculate the discounted value from final total price
     * i.e. totalPrice=900, percentDiscount=10% => discountedValue = 100  
     * @param totalPrice total
     * @param percentDiscount discount as percent
     * @return BigDecimal
     */
    public static BigDecimal calculateDiscountedValue(BigDecimal totalPrice, BigDecimal percentDiscount) {
        return divide(totalPrice.multiply(percentDiscount), BigDecimal.valueOf(100).subtract(percentDiscount));
    }

}
