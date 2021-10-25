package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Execute command for read lightly address attributes.
 *
 * @author Fernando Montaño
 * @version $Id: LightlyAddressCmd.java 12586 2016-09-07 20:54:40Z miguel $
 */
public class LightlyAddressCmd extends EJBCommand {

    private Log log = LogFactory.getLog(LightlyAddressCmd.class);

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing LightlyAddressCmd...");
        log.debug("addressId to read = " + paramDTO.get("addressId"));

        //read address bean
        AddressDTO addressDTO = new AddressDTO(paramDTO);
        Address address = null;

        try {
            address = (Address) EJBFactory.i.findEJB(addressDTO);

            //recovering country code
            if (address.getCountryId() != null) {
                resultDTO.put("countryCode", address.getCountry().getCountryAreaCode());
                resultDTO.put("countryName", address.getCountry().getCountryName());
            }
            if (address.getCityId() != null) {
                resultDTO.put("city", address.getCityEntity().getCityName());
                resultDTO.put("zip", address.getCityEntity().getCityZip());
            }

            if (address.getTitle() != null) {
                resultDTO.put("titleName", address.getTitle().getTitleName());
            }

            if (address.getLanguage() != null) {
                resultDTO.put("languageIso", address.getLanguage().getLanguageIso());
            }

            resultDTO.put("cityId", address.getCityId());
            resultDTO.put("countryId", address.getCountryId());
            resultDTO.put("street", address.getStreet());
            resultDTO.put("houseNumber", address.getHouseNumber());
            resultDTO.put("additionalAddressLine", address.getAdditionalAddressLine());

            //aditional data
            resultDTO.put("addressType", address.getAddressType());
            resultDTO.put("code", address.getCode().toString());
            resultDTO.put("name1", address.getName1());
            resultDTO.put("name2", address.getName2());
            resultDTO.put("name3", address.getName3());
            resultDTO.put("recordDate", address.getRecordDate());
            resultDTO.put("recordUserId", address.getRecordUserId());
            resultDTO.put("lastModificationDate", address.getLastModificationDate());
            resultDTO.put("lastModificationUserId", address.getLastModificationUserId());
            resultDTO.put("imageId", address.getImageId());

            resultDTO.put("addressName", address.getName());

            if (address.getWayDescription() != null && address.getWayDescription().getValue() != null) {
                resultDTO.put("wayDescription", new String(address.getWayDescription().getValue()));
            }

            if (address.getFreeText() != null && address.getFreeText().getValue() != null) {
                resultDTO.put("note", new String(address.getFreeText().getValue()));
            }

        } catch (EJBFactoryException e) { // address not found
            addressDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setResultAsFailure();
        }

    }

    public boolean isStateful() {
        return false;
    }
}
