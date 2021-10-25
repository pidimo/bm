package com.piramide.elwis.cmd.salesmanager.util;

import com.piramide.elwis.domain.salesmanager.ActionPosition;
import com.piramide.elwis.domain.salesmanager.SalePosition;
import com.piramide.elwis.utils.BigDecimalUtils;

import java.math.BigDecimal;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class SaleUtil {
    public static SaleUtil i = new SaleUtil();

    private SaleUtil() {

    }

    public BigDecimal calculateTotalPrice(BigDecimal discountAsPercentage,
                                          BigDecimal quantity,
                                          BigDecimal price) {
        BigDecimal totalWithOutDiscount = BigDecimalUtils.multiply(quantity, price);
        BigDecimal discountValue = new BigDecimal(0);
        if (null != discountAsPercentage) {
            discountValue = BigDecimalUtils.getPercentage(totalWithOutDiscount, discountAsPercentage);
        }

        return BigDecimalUtils.subtract(totalWithOutDiscount, discountValue);
    }

    public void calculateActionPositionTotalPrices(ActionPosition actionPosition) {
        BigDecimal discount = actionPosition.getDiscount();
        if (null == actionPosition.getPrice()) {
            actionPosition.setTotalPrice(null);
        } else {
            actionPosition.setTotalPrice(calculateTotalPrice(discount,
                    actionPosition.getAmount(),
                    actionPosition.getPrice()));
        }

        if (null == actionPosition.getUnitPriceGross()) {
            actionPosition.setTotalPriceGross(null);
        } else {
            actionPosition.setTotalPriceGross(calculateTotalPrice(discount,
                    actionPosition.getAmount(),
                    actionPosition.getUnitPriceGross()));
        }
    }

    public void calculateSalePositionTotalPrices(SalePosition salePosition) {
        BigDecimal discount = salePosition.getDiscount();
        if (null == salePosition.getUnitPrice()) {
            salePosition.setTotalPrice(null);
        } else {
            salePosition.setTotalPrice(calculateTotalPrice(discount,
                    salePosition.getQuantity(),
                    salePosition.getUnitPrice()));
        }

        if (null == salePosition.getUnitPriceGross()) {
            salePosition.setTotalPriceGross(null);
        } else {
            salePosition.setTotalPriceGross(calculateTotalPrice(discount,
                    salePosition.getQuantity(),
                    salePosition.getUnitPriceGross()));
        }
    }
}
