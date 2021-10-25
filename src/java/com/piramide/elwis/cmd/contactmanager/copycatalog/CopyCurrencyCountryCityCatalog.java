package com.piramide.elwis.cmd.contactmanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.dto.catalogmanager.CityDTO;
import com.piramide.elwis.dto.catalogmanager.CountryDTO;
import com.piramide.elwis.dto.catalogmanager.CurrencyDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: ivan
 */
public class CopyCurrencyCountryCityCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        CurrencyHome currencyHome = (CurrencyHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CURRENCY);
        CountryHome countryHome = (CountryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_COUNTRY);
        CityHome cityHome = (CityHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CITY);


        Collection sourceCurrencies = null;
        Map<Integer, Integer> currencyCountryRelationHelper = new HashMap<Integer, Integer>();
        try {
            sourceCurrencies = currencyHome.findByCompanyId(source.getCompanyId());
        } catch (FinderException e) {
            log.warn("Cannot read currencies of source company.");
        }

        if (null != sourceCurrencies && !sourceCurrencies.isEmpty()) {
            for (Object obj : sourceCurrencies) {
                Currency sourceCurrency = (Currency) obj;
                CurrencyDTO dto = new CurrencyDTO();
                DTOFactory.i.copyToDTO(sourceCurrency, dto);
                dto.put("companyId", target.getCompanyId());
                Currency targetCurrency = (Currency) ExtendedCRUDDirector.i.create(dto, new ResultDTO(), false);
                currencyCountryRelationHelper.put(sourceCurrency.getCurrencyId(), targetCurrency.getCurrencyId());
            }
        }


        Collection sourceCountries = null;
        Map<Integer, Integer> countryCityRelationHelper = new HashMap<Integer, Integer>();

        try {
            sourceCountries = countryHome.findByCompanyId(source.getCompanyId());
        } catch (FinderException e) {
            log.warn("Cannot read countries of source company.");
        }

        if (null != sourceCountries && !sourceCountries.isEmpty()) {
            for (Object obj : sourceCountries) {
                Country sourceCountry = (Country) obj;
                CountryDTO dto = new CountryDTO();
                DTOFactory.i.copyToDTO(sourceCountry, dto);
                dto.put("companyId", target.getCompanyId());
                dto.put("currencyId", null);
                if (null != sourceCountry.getCurrencyId()) {
                    Integer targetCurrencyId = currencyCountryRelationHelper.get(sourceCountry.getCurrencyId());
                    if (null != targetCurrencyId) {
                        dto.put("currencyId", targetCurrencyId);
                    }
                }

                Country targetCountry = (Country) ExtendedCRUDDirector.i.create(dto, new ResultDTO(), false);

                countryCityRelationHelper.put(sourceCountry.getCountryId(), targetCountry.getCountryId());
            }
        }

        Collection sourceCities = null;
        try {
            sourceCities = cityHome.findByCompanyId(source.getCompanyId());
        } catch (FinderException e) {
            log.warn("Cannot read cities of source company.");
        }

        if (null != sourceCities && !sourceCities.isEmpty()) {
            for (Object obj : sourceCities) {
                City sourceCity = (City) obj;
                CityDTO dto = new CityDTO();
                DTOFactory.i.copyToDTO(sourceCity, dto);
                dto.put("companyId", target.getCompanyId());
                dto.put("countryId", null);
                if (null != sourceCity.getCountryId()) {
                    Integer targetCountryId = countryCityRelationHelper.get(sourceCity.getCountryId());
                    if (null != targetCountryId) {
                        dto.put("countryId", targetCountryId);
                    }
                }

                ExtendedCRUDDirector.i.create(dto, new ResultDTO(), false);
            }
        }
    }
}
