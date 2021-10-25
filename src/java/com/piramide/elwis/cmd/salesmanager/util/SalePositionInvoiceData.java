package com.piramide.elwis.cmd.salesmanager.util;

import java.math.BigDecimal;

/**
 * @author Miky
 * @version $Id: SalePositionInvoiceData.java 2009-08-18 06:04:40 PM $
 */
public class SalePositionInvoiceData extends ContractInvoiceData {
    private BigDecimal unitPriceGross;

    public SalePositionInvoiceData() {
        setUnitPriceDiscounted(BigDecimal.ZERO);
    }

    public BigDecimal getUnitPriceGross() {
        return unitPriceGross;
    }

    public void setUnitPriceGross(BigDecimal unitPriceGross) {
        this.unitPriceGross = unitPriceGross;
    }
}
