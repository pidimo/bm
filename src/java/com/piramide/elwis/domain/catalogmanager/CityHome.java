package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * This Class represents the Home interface of  City Entity Bean
 *
 * @author Ivan
 * @version $Id: CityHome.java 2648 2004-09-30 19:34:46Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface CityHome extends EJBLocalHome {
    public City create(ComponentDTO dto) throws CreateException;

    City findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByCountry(Integer countryId, Integer companyId) throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;

    public Collection findByZipAndCountry(String zip, Integer countryId, Integer companyId) throws FinderException;

    public City findByCityNameAndCountry(String city, Integer countryId, Integer companyId) throws FinderException;

    public City findByZipAndCityNameAndCountry(String zip, String cityName, Integer countryId, Integer companyId) throws FinderException;

}