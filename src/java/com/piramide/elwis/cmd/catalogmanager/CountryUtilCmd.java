package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.domain.catalogmanager.City;
import com.piramide.elwis.domain.catalogmanager.CityHome;
import com.piramide.elwis.dto.catalogmanager.CityDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1
 */
public class CountryUtilCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CountryUtilCmd....." + paramDTO);

        if ("findCountryCities".equals(getOp())) {
            getCountryCities();
        }
    }

    private void getCountryCities() {
        List<CityDTO> result = new ArrayList<CityDTO>();

        Integer countryId = new Integer(paramDTO.get("countryId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        CityHome cityHome = (CityHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CITY);

        Collection<City> cities = null;
        try {
            cities = cityHome.findByCountry(countryId, companyId);
        } catch (FinderException e) {
            cities = new ArrayList<City>();
        }

        for (City city : cities) {
            CityDTO cityDTO = new CityDTO();
            DTOFactory.i.copyToDTO(city, cityDTO);

            result.add(cityDTO);
        }

        resultDTO.put("countryCityDTOList", result);
    }

    public boolean isStateful() {
        return false;
    }
}
