package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the Currency Entity Bean
 *
 * @author Ivan
 * @version $Id: Currency.java 1950 2004-07-23 14:40:50Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface Currency extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getCurrencyId();

    void setCurrencyId(Integer currencyId);

    Boolean getIsBasicCurrency();

    void setIsBasicCurrency(Boolean isBasicCurrency);

    java.math.BigDecimal getUnit();

    void setUnit(java.math.BigDecimal unitId);

    String getCurrencySymbol();

    void setCurrencySymbol(String symbol);

    Integer getVersion();

    void setVersion(Integer versionId);

    String getCurrencyName();

    void setCurrencyName(String nameId);

    String getCurrencyLabel();

    void setCurrencyLabel(String label);

}
