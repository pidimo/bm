package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.cmd.contactmanager.ZipCityCmd;
import com.piramide.elwis.dto.catalogmanager.CityDTO;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import java.util.ArrayList;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public class AdditionalAddressForm extends DefaultForm {
    // the logger
    private Log log = LogFactory.getLog(this.getClass());

    private String countryId;
    private String cityId;
    private String zip;
    private String city;
    private String beforeZip;

    public String getCountryId() {
        if (getDto("countryId") != null) {
            return getDto("countryId").toString();
        } else {
            return "";
        }
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
        super.setDto("countryId", this.countryId);
    }

    public String getCityId() {
        if (getDto("cityId") != null) {
            return getDto("cityId").toString();
        } else {
            return "";
        }
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
        super.setDto("cityId", this.cityId);
    }

    public String getZip() {
        if (getDto("zip") != null) {
            return getDto("zip").toString();
        } else {
            return "";
        }
    }

    public void setZip(String zip) {
        this.zip = zip;
        super.setDto("zip", this.zip);
    }

    public String getBeforeZip() {
        if (getDto("beforeZip") != null) {
            return getDto("beforeZip").toString();
        } else {
            return "";
        }
    }

    public void setBeforeZip(String beforeZip) {
        this.beforeZip = beforeZip;
        super.setDto("beforeZip", this.beforeZip);
    }

    public String getCity() {
        if (getDto("city") != null) {
            return getDto("city").toString();
        } else {
            return "";
        }
    }

    public void setCity(String city) {
        this.city = city;
        super.setDto("city", this.city);
    }


    /**
     * Validate the input fields and set defaults values to dtoMap.
     */

    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
        log.debug("AdditionalAddressForm validation execution...");
        User user = (User) RequestUtils.getUser(request);
        Integer companyId = new Integer(user.getValue("companyId").toString());

        if (super.getDto("isDefault") != null) {
            super.setDto("isDefault", new Boolean(true));
        } else {
            super.setDto("isDefault", new Boolean(false));
        }

        ActionErrors errors = new ActionErrors();

        errors = super.validate(mapping, request);// validating with super class

        // ZIP/CITY MANAGEMENT
        // if zip is not empty or city is not empty, but country is not defined.
        if ((!GenericValidator.isBlankOrNull(this.getZip()) || !GenericValidator.isBlankOrNull(this.getCity())) &&
                GenericValidator.isBlankOrNull(this.getCountryId())) {
            log.debug("Cannot define a city without country...");
            errors.add("city", new ActionError("Address.error.cityWithoutCountry"));
        }

        // Search the city with only country and city name, if found show it, else open window to create it.
        if (!GenericValidator.isBlankOrNull(this.getCountryId()) && !GenericValidator.isBlankOrNull(this.getCity())
                && GenericValidator.isBlankOrNull(this.getZip())) {
            log.debug("Only define the city name without zip code ");
            try {
                ZipCityCmd zipCmd = new ZipCityCmd(); // the command to execute
                zipCmd.putParam("countryId", this.getCountryId());
                zipCmd.putParam("city", this.getCity());
                zipCmd.putParam("companyId", companyId);
                zipCmd.putParam("withoutZip", new Boolean(true));
                ResultDTO resultDTO = BusinessDelegate.i.execute(zipCmd, request);
                if (resultDTO.get("cityId") != null) { //city by country and name has been found
                    this.setCityId(resultDTO.get("cityId").toString());
                    this.setCity(resultDTO.get("city").toString());
                } else { //city by country and name not found, open window  to create it.
                    StringBuffer openUrl = new StringBuffer();
                    openUrl.append("onload=\"")
                            .append("openCitySearch('countryId', 'cityId', 'zipId', 'cityNameId', 'beforeZipId', 'notFound', 'iframeId', 'false')\"");

                    request.setAttribute("jsLoad", new String(openUrl));
                    if (errors.isEmpty()) {

                        errors.add("citySearch", new ActionError("Address.error.enteredCityNotFound"));
                    }

                }

            } catch (AppLevelException e) {
                log.error("Error searching the city name= " + getDto("city"), e);
            }

        }

        // Search the city with zip, cityName and country, if found some show it, else open window to create it
        //  or search for another.
        if (!GenericValidator.isBlankOrNull(this.getCountryId()) && !GenericValidator.isBlankOrNull(this.getCity())
                && !GenericValidator.isBlankOrNull(this.getZip())) {
            log.debug("Searching with country, zip and city defined");
            try {
                ZipCityCmd zipCmd = new ZipCityCmd(); // the command to execute
                zipCmd.putParam("countryId", this.getCountryId());
                zipCmd.putParam("zip", getDto("zip"));
                zipCmd.putParam("city", this.getCity());
                zipCmd.putParam("companyId", companyId);
                zipCmd.putParam("withZipAndCity", new Boolean(true));
                ResultDTO resultDTO = BusinessDelegate.i.execute(zipCmd, request);
                if (resultDTO.get("cityId") != null) { //city found
                    this.setCityId(resultDTO.get("cityId").toString());
                    this.setZip(resultDTO.get("zip").toString());
                    this.setCity(resultDTO.get("city").toString());
                } else { //city by country and name not found, open window  to create it.
                    StringBuffer openUrl = new StringBuffer();
                    openUrl.append("onload=\"")
                            .append("openCitySearch('countryId', 'cityId', 'zipId', 'cityNameId', 'beforeZipId', 'notFoundWithZipCity', 'iframeId', 'false')\"");

                    request.setAttribute("jsLoad", new String(openUrl));
                    if (errors.isEmpty()) {

                        if (Functions.hasAccessRight(request, "CITY", "CREATE")) {
                            errors.add("citySearch", new ActionError("Address.error.enteredCityNotFound.selectOrCreate"));
                        } else {
                            errors.add("citySearch", new ActionError("Address.error.enteredCityNotFound"));
                        }
                    }

                }

            } catch (AppLevelException e) {
                log.error("Error searching the city name= " + getDto("city"), e);
            }
        }

        // Search the city with given country and zip code.
        if (!GenericValidator.isBlankOrNull(this.getCountryId()) &&
                !GenericValidator.isBlankOrNull(this.getZip()) &&
                GenericValidator.isBlankOrNull(this.getCity())) {
            log.debug("Retrieving city or cities with only Zip code entered");
            try {
                ZipCityCmd zipCmd = new ZipCityCmd(); // the command to execute
                zipCmd.putParam("countryId", getDto("countryId"));
                zipCmd.putParam("zip", getDto("zip"));
                zipCmd.putParam("companyId", companyId);
                final ResultDTO resultDTO = BusinessDelegate.i.execute(zipCmd, request);
                ArrayList result = (ArrayList) resultDTO.get("cities");

                if (result.size() == 0) { //if no found cities for given zip code, open create window

                    StringBuffer openUrl = new StringBuffer();
                    openUrl.append("onload=\"")
                            .append("openCitySearch('countryId', 'cityId', 'zipId', 'cityNameId', 'beforeZipId', 'notFound', 'iframeId', 'false')\"");

                    request.setAttribute("jsLoad", new String(openUrl));
                    if (errors.isEmpty()) {
                        errors.add("city", new ActionError("Address.error.enteredCityNotFound"));
                    }

                } else if (result.size() == 1) { // if found only one
                    log.debug("One city found");
                    CityDTO city = (CityDTO) result.get(0);
                    log.debug("city = " + city.get("cityName"));
                    log.debug("cityId = " + city.get("cityId"));
                    this.setCity(String.valueOf(city.get("cityName")));
                    this.setCityId(city.get("cityId").toString());

                } else { //if found more of one cities asociated with entered zip

                    if (!getBeforeZip().equals(getZip()) || GenericValidator.isBlankOrNull(this.getCity())) {

                        log.debug("more of one cities found, load a city search page to choose one");
                        //  setCity("");
                        StringBuffer openUrl = new StringBuffer();
                        openUrl.append("onload=\"")
                                .append("openCitySearch('countryId', 'cityId', 'zipId', 'cityNameId', 'beforeZipId', 'tooMuch', 'iframeId', 'false')\"");

                        request.setAttribute("jsLoad", new String(openUrl));
                        if (errors.isEmpty()) {
                            errors.add("toMuchCities", new ActionError("Address.error.tooMuchCitiesByZip"));
                        }
                    }

                }

            } catch (AppLevelException e) {
                log.error("Error reading cities for zip = " + getDto("zip"), e);
            }
        }

        //if it leaves empty city and zip he don't want a city defined
        if (GenericValidator.isBlankOrNull(this.getZip()) && GenericValidator.isBlankOrNull(this.getCity())) {
            this.setCityId(null);
        }


        return errors;
    }
}
