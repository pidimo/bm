package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.utils.HashMapCleaner;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Bussines logic to find duplicated addresses
 * This class looking for coincidences in name1, name2 and name3 names for one address.
 *
 * @author Fernando Montaño
 * @version $Id: AddressDuplicatedCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */
public class AddressDuplicatedCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        HashMapCleaner.clean(paramDTO); //please do not remove this although is deprecated, this is required.
        log.debug("name1= " + paramDTO.get("name1"));
        log.debug("name2= " + paramDTO.get("name2"));
        log.debug("name3= " + paramDTO.get("name3"));
        log.debug("addressType= " + paramDTO.get("addressType"));
        log.debug("companyId= " + paramDTO.get("companyId"));
        log.debug("userId= " + paramDTO.get("userId"));

        try {

            Collection addresses = (Collection) EJBFactory.i.callFinder(new AddressDTO(), "selectAddressesByNames",
                    new Object[]{paramDTO.get("name1"), paramDTO.get("name2"),
                            paramDTO.get("name3"), paramDTO.get("addressType").toString(),
                            new Integer(paramDTO.get("companyId").toString()),
                            new Integer(paramDTO.get("userId").toString())});

            List duplicatedAddresses = new ArrayList();
            AddressDTO addressDTO = null;
            if (addresses.size() > 0) {
                for (Iterator iterator = addresses.iterator(); iterator.hasNext();) {
                    Address address = (Address) iterator.next();
                    addressDTO = new AddressDTO();
                    addressDTO.put("addressId", address.getAddressId());
                    addressDTO.put("addressName", address.getName());
                    addressDTO.put("name1", address.getName1());
                    addressDTO.put("name2", address.getName2());
                    addressDTO.put("name3", address.getName3());
                    addressDTO.put("addressType", address.getAddressType());
                    if (address.getCountryId() != null) {
                        addressDTO.put("countryCode", address.getCountry().getCountryAreaCode());
                    }
                    if (address.getCityId() != null) {
                        addressDTO.put("city", address.getCityEntity().getCityName());
                        addressDTO.put("zip", address.getCityEntity().getCityZip());
                    }
                    addressDTO.put("street", address.getStreet());
                    duplicatedAddresses.add(addressDTO);
                }

            }

            resultDTO.put("duplicatedAddresses", duplicatedAddresses);


        } catch (EJBFactoryException e) {
            if (e.getCause() instanceof FinderException) {
                log.debug("Do not found addresses with the same name");
            } else {
                log.error("Error calling the finder", e);
            }

        }

    }

    public boolean isStateful() {
        return false;
    }
}
