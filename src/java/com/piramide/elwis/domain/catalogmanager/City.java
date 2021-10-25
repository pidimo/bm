package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the City Entity Bean
 *
 * @author Ivan
 * @version $Id: City.java 1922 2004-07-19 21:20:07Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface City extends EJBLocalObject {

    Integer getCityId();

    void setCityId(Integer cityId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer versionId);

    String getCityZip();

    void setCityZip(String zip);

    String getCityName();

    void setCityName(String nameId);

    Country getCountry();

    void setCountry(Country country);

    Integer getCountryId();

    void setCountryId(Integer countryId);

}
