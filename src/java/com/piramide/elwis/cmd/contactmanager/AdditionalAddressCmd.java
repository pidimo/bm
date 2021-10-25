package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.FreeTextCmdUtil;
import com.piramide.elwis.domain.catalogmanager.City;
import com.piramide.elwis.domain.contactmanager.AdditionalAddress;
import com.piramide.elwis.domain.contactmanager.AdditionalAddressHome;
import com.piramide.elwis.domain.contactmanager.ContactFreeTextHome;
import com.piramide.elwis.dto.catalogmanager.CityDTO;
import com.piramide.elwis.dto.contactmanager.AdditionalAddressDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public class AdditionalAddressCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing AdditionalAddressCmd................" + paramDTO);
        boolean isRead = true;

        if ("create".equals(getOp())) {
            isRead = false;
            create();
        }
        if ("createMainAddress".equals(getOp())) {
            isRead = false;
            createMainAdditionalAddress();
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update();
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete();
        }

        if ("findDefaultAddAddress".equals(getOp())) {
            isRead = false;
            findDefaultByAddress();
        }

        if (isRead) {
            read();
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void read() {
        boolean checkReferences = ("true".equals(paramDTO.get("withReferences")));

        AdditionalAddressDTO additionalAddressDTO = new AdditionalAddressDTO(paramDTO);
        AdditionalAddress additionalAddress =(AdditionalAddress) ExtendedCRUDDirector.i.read(additionalAddressDTO, resultDTO, checkReferences);
        if (additionalAddress != null) {
            if (additionalAddress.getContactFreeText() != null) {
                resultDTO.put("comment", new String(additionalAddress.getContactFreeText().getValue()));
            }

            readCityData(additionalAddress.getCityId());
        }
    }

    private AdditionalAddress create() {
        AdditionalAddressDTO additionalAddressDTO = new AdditionalAddressDTO(paramDTO);

        AdditionalAddress additionalAddress =(AdditionalAddress) ExtendedCRUDDirector.i.create(additionalAddressDTO, resultDTO, true);

        if (additionalAddress != null) {
            if (hasComment()) {
                FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, additionalAddress, "ContactFreeText", ContactFreeTextHome.class,
                        ContactConstants.JNDI_CONTACTFREETEXT, FreeTextTypes.FREETEXT_CONTACT, "comment");
            }

            if (additionalAddress.getIsDefault()) {
                unSetOtherDefaults(additionalAddress);
            }
        }

        return additionalAddress;
    }

    private void createMainAdditionalAddress() {
        AdditionalAddress additionalAddress = null;
        Integer addressId = new Integer(paramDTO.get("addressId").toString());

        AdditionalAddressHome additionalAddressHome = (AdditionalAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDITIONALADDRESS);
        try {
            Collection additionalAddresses = additionalAddressHome.findByAdditionalAddressType(addressId, ContactConstants.AdditionalAddressType.MAIN.getConstant());
            if (!additionalAddresses.isEmpty()) {
                additionalAddress = (AdditionalAddress) additionalAddresses.iterator().next();
            }
        } catch (FinderException e) {
            log.debug("error in found additional address..", e);
        }

        if (additionalAddress == null) {
            paramDTO.setOp("create"); //this to verify duplicate
            additionalAddress = create();
        }

        if (additionalAddress != null) {
            AdditionalAddressDTO additionalAddressDTO = new AdditionalAddressDTO();
            DTOFactory.i.copyToDTO(additionalAddress, additionalAddressDTO);
            resultDTO.put("mainAddAddressDTO", additionalAddressDTO);
        }
    }

    private void update() {
        AdditionalAddressDTO additionalAddressDTO = new AdditionalAddressDTO(paramDTO);

        AdditionalAddress additionalAddress =(AdditionalAddress)  ExtendedCRUDDirector.i.update(additionalAddressDTO, resultDTO, true, true, true, "Fail");

        if (additionalAddress != null && !resultDTO.isFailure()) {

            if (hasComment() || additionalAddress.getCommentId() != null) {
                FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, additionalAddress, "ContactFreeText", ContactFreeTextHome.class,
                        ContactConstants.JNDI_CONTACTFREETEXT, FreeTextTypes.FREETEXT_CONTACT, "comment");
            }

            if (additionalAddress.getIsDefault()) {
                unSetOtherDefaults(additionalAddress);
            }
        }
    }

    private void delete() {
        AdditionalAddressDTO additionalAddressDTO = new AdditionalAddressDTO(paramDTO);
        ExtendedCRUDDirector.i.delete(additionalAddressDTO, resultDTO, true, "Fail");
    }

    private boolean hasComment() {
        return (paramDTO.get("comment") != null && !"".equals(paramDTO.get("comment").toString()));
    }

    private void readCityData(Integer cityId) {
        if (cityId != null) {
            City city = (City) EJBFactory.i.findEJB(new CityDTO(cityId));
            if (city != null) {
                resultDTO.put("city", city.getCityName());
                resultDTO.put("zip", city.getCityZip());
                resultDTO.put("beforeZip", city.getCityZip());
                resultDTO.put("cityId", city.getCityId());
            }
        }
    }

    private void unSetOtherDefaults(AdditionalAddress additionalAddress) {
        AdditionalAddressHome additionalAddressHome = (AdditionalAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDITIONALADDRESS);

        Collection additionalAddressDefaults = null;
        try {
            additionalAddressDefaults = additionalAddressHome.findByDefault(additionalAddress.getAddressId(), additionalAddress.getCompanyId());
        } catch (FinderException e) {
            additionalAddressDefaults = new ArrayList();
        }

        for (Iterator iterator = additionalAddressDefaults.iterator(); iterator.hasNext();) {
            AdditionalAddress additionalAddressDefault = (AdditionalAddress) iterator.next();
            if (!additionalAddressDefault.getAdditionalAddressId().equals(additionalAddress.getAdditionalAddressId())) {
                additionalAddressDefault.setIsDefault(false);
            }
        }
    }

    private void findDefaultByAddress() {
        AdditionalAddressHome additionalAddressHome = (AdditionalAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDITIONALADDRESS);
        Integer addressId = new Integer(paramDTO.get("addressId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        Collection additionalAddressDefaults = null;
        try {
            additionalAddressDefaults = additionalAddressHome.findByDefault(addressId, companyId);
            if (!additionalAddressDefaults.isEmpty()) {
                AdditionalAddress additionalAddress = (AdditionalAddress) additionalAddressDefaults.iterator().next();
                AdditionalAddressDTO additionalAddressDTO = new AdditionalAddressDTO();
                DTOFactory.i.copyToDTO(additionalAddress, additionalAddressDTO);

                resultDTO.put("defaultAddAddressDTO", additionalAddressDTO);
            }
        } catch (FinderException e) {
            log.debug("error in found the default additional address..", e);
        }
    }
}
