package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the Country Entity Bean
 *
 * @author Ivan
 * @version $Id: Country.java 1922 2004-07-19 21:20:07Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface Country extends EJBLocalObject {
    String getCountryAreaCode();

    void setCountryAreaCode(String areaCode);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getCountryId();

    void setCountryId(Integer countryId);

    String getCountryName();

    void setCountryName(String nameId);

    Integer getVersion();

    void setVersion(Integer versionId);

    Currency getCurrency();

    void setCurrency(Currency currency);

    Integer getCurrencyId();

    void setCurrencyId(Integer currencyId);

    Integer getCountryPrefix();

    void setCountryPrefix(Integer prefix);

}
