package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * This Class represents the Home interface of Country Entity Bean
 *
 * @author Ivan
 * @version $Id: CountryHome.java 8043 2008-02-26 17:20:30Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface CountryHome extends EJBLocalHome {
    public Country create(ComponentDTO dto) throws CreateException;

    Country findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;

    public Country findByCountryName(String countryName, Integer companyId) throws FinderException;
}
