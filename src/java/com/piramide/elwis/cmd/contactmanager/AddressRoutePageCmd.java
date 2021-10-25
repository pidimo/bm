package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.utils.RoutePageField;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Execute command for read and generate the url string for route planning link.
 *
 * @author Fernando Montaño
 * @version $Id: AddressRoutePageCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */
public class AddressRoutePageCmd extends EJBCommand {

    private Log log = LogFactory.getLog(AddressRoutePageCmd.class);

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing AddressRoutePageCmd...");
        log.debug("companyId to read = " + paramDTO.get("companyId"));
        log.debug("addressId to read = " + paramDTO.get("addressId"));
        log.debug("current locale for url = " + paramDTO.get("locale"));

        try {
            String routeURL = null;
            CompanyCmd companyCmd = new CompanyCmd();
            companyCmd.putParam("addressId", paramDTO.get("companyId"));
            companyCmd.executeInStateless(ctx);
            ResultDTO companyDTO = companyCmd.getResultDTO();
            if (companyDTO.get("route") != null) {
                routeURL = companyDTO.getAsString("route");
                routeURL = routeURL.replaceAll("\n", "");
            } else { //if company no have configured the route url
                resultDTO.put("routeURL", null);
                return;
            }

            log.debug("Final company route url = " + routeURL);
            //reading the company address data
            LightlyAddressCmd addressCompanyCmd = new LightlyAddressCmd();
            addressCompanyCmd.putParam("addressId", paramDTO.get("companyId"));
            addressCompanyCmd.executeInStateless(ctx);
            ResultDTO sourceDTO = addressCompanyCmd.getResultDTO();

            //get address destination information
            LightlyAddressCmd addressCmd = new LightlyAddressCmd();
            addressCmd.putParam("addressId", paramDTO.get("addressId"));
            addressCmd.executeInStateless(ctx);
            ResultDTO destinationDTO = addressCmd.getResultDTO();

            routeURL = replaceFields(routeURL, paramDTO.getAsString("locale"), sourceDTO, destinationDTO);

            resultDTO.put("routeURL", routeURL);


        } catch (Exception e) {
            log.error("Error reading address route page information", e);
        }


    }

    /**
     * Replace the fields for route page url, with address values.
     *
     * @param url            the company url configured for route page planning.
     * @param locale         the current user locale
     * @param sourceDTO      the source ResultDTO that contains the address data for replacement
     * @param destinationDTO the destination ResultDTO that contains the address data for replacement
     * @return the URL String with values replaced.
     */
    private String replaceFields(String url, String locale, ResultDTO sourceDTO, ResultDTO destinationDTO) {
        url = url.replaceAll(RoutePageField.FIELD_LOCALE, locale);
        url = url.replaceAll(RoutePageField.FIELD_SOURCE_CITY, sourceDTO.getAsString("city") != null ?
                sourceDTO.getAsString("city") : "");
        url = url.replaceAll(RoutePageField.FIELD_SOURCE_COUNTRY, sourceDTO.getAsString("countryCode") != null ?
                sourceDTO.getAsString("countryCode") : "");
        url = url.replaceAll(RoutePageField.FIELD_SOURCE_STREET, sourceDTO.getAsString("street") != null ?
                sourceDTO.getAsString("street") : "");
        url = url.replaceAll(RoutePageField.FIELD_SOURCE_HOUSE_NUMBER, sourceDTO.getAsString("houseNumber") != null ?
                sourceDTO.getAsString("houseNumber") : "");
        url = url.replaceAll(RoutePageField.FIELD_SOURCE_ZIP, sourceDTO.getAsString("zip") != null ?
                sourceDTO.getAsString("zip") : "");

        url = url.replaceAll(RoutePageField.FIELD_DESTINATION_CITY, destinationDTO.getAsString("city") != null ?
                destinationDTO.getAsString("city") : "");
        url = url.replaceAll(RoutePageField.FIELD_DESTINATION_COUNTRY, destinationDTO.getAsString("countryCode") != null ?
                destinationDTO.getAsString("countryCode") : "");
        url = url.replaceAll(RoutePageField.FIELD_DESTINATION_STREET, destinationDTO.getAsString("street") != null ?
                destinationDTO.getAsString("street") : "");
        url = url.replaceAll(RoutePageField.FIELD_DESTINATION_HOUSE_NUMBER, destinationDTO.getAsString("houseNumber") != null ?
                destinationDTO.getAsString("houseNumber") : "");
        url = url.replaceAll(RoutePageField.FIELD_DESTINATION_ZIP, destinationDTO.getAsString("zip") != null ?
                destinationDTO.getAsString("zip") : "");
        return url;
    }

    public boolean isStateful() {
        return false;
    }
}
