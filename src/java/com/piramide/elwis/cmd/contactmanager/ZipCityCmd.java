package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.catalogmanager.City;
import com.piramide.elwis.dto.catalogmanager.CityDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Bussines logic for manage Zip and City for an Address
 *
 * @author Fernando Montaño
 * @version $Id: ZipCityCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */
public class ZipCityCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing command with operation = " + getOp());

        log.debug("countryId = " + paramDTO.get("countryId"));
        log.debug("zip = " + paramDTO.get("zip"));
        log.debug("city = " + paramDTO.get("city"));
        log.debug("companyId = " + paramDTO.get("companyId"));
        log.debug("withoutZip = " + paramDTO.get("withoutZip"));
        log.debug("withZipAndCity = " + paramDTO.get("withZipAndCity"));

        CityDTO cityDTO = new CityDTO(paramDTO);
        ///findByZipAndCityNameAndCountry
        if (paramDTO.get("withoutZip") != null) {  //if only write the city name
            log.debug("Searching a city name, without zip code defined");
            try {

                City city = (City) EJBFactory.i.callFinder(new CityDTO(), "findByCityNameAndCountry",
                        new Object[]{cityDTO.get("city").toString(), new Integer(cityDTO.get("countryId").toString()),
                                new Integer(cityDTO.get("companyId").toString())});
                resultDTO.put("cityId", city.getCityId());
                resultDTO.put("city", city.getCityName());

            } catch (EJBFactoryException e) {
                log.debug("Do not found Cities for this Cityname in Country");
                resultDTO.put("cityId", null);
            }

        } else if (paramDTO.get("withZipAndCity") != null) { //searching for city with country, zip, city
            log.debug("Searching a city name, with zip code defined");
            try {

                City city = (City) EJBFactory.i.callFinder(new CityDTO(), "findByZipAndCityNameAndCountry",
                        new Object[]{cityDTO.get("zip").toString(), cityDTO.get("city").toString(),
                                new Integer(cityDTO.get("countryId").toString()),
                                new Integer(cityDTO.get("companyId").toString())});
                resultDTO.put("cityId", city.getCityId());
                resultDTO.put("city", city.getCityName());
                resultDTO.put("zip", city.getCityZip());

            } catch (EJBFactoryException e) {
                log.debug("Do not found city with that zip and city name in defined country");
                resultDTO.put("cityId", null);
            }
        } else {
            try {

                Collection cities = (Collection) EJBFactory.i.callFinder(new CityDTO(), "findByZipAndCountry",
                        new Object[]{cityDTO.get("zip").toString(), new Integer(cityDTO.get("countryId").toString()),
                                new Integer(cityDTO.get("companyId").toString())});

                log.debug("Cities found for ZIP  = " + cities.size());

                Iterator citiesIt = cities.iterator();
                ArrayList citiesResult = new ArrayList();
                CityDTO cityDto = null;
                while (citiesIt.hasNext()) { //filling cities DTO
                    City city = (City) citiesIt.next();
                    cityDto = new CityDTO();
                    cityDto.put("cityId", city.getCityId());
                    cityDto.put("cityName", city.getCityName());
                    citiesResult.add(cityDto);
                }
                log.debug("After iterating the cities");
                resultDTO.put("cities", citiesResult);


            } catch (EJBFactoryException e) {
                log.debug("Do not found Cities for this Zip in Country");
            }
        }


    }

    public boolean isStateful() {
        return false;
    }
}
