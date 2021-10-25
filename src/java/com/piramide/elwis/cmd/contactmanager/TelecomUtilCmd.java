package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.catalogmanager.TelecomTypeSelectCmd;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.contactmanager.Telecom;
import com.piramide.elwis.domain.contactmanager.TelecomHome;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.dto.contactmanager.TelecomWrapperDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.TelecomType;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class TelecomUtilCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing TelecomUtilCmd......." + paramDTO);

        if ("defaultEmailTelecom".equals(getOp())) {
            findDefaultEmailTelecom();
        }
        if ("readAddressTelecom".equals(getOp())) {
            readAllAddressTelecoms(ctx);
        }
        if ("defaultTelecomByType".equals(getOp())) {
            findDefaultTelecomByType();
        }
    }

    private void findDefaultEmailTelecom() {
        Integer addressId = new Integer(paramDTO.get("addressId").toString());
        Integer contactPersonId = null;
        if (paramDTO.get("contactPersonId") != null) {
            contactPersonId = new Integer(paramDTO.get("contactPersonId").toString());
        }

        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        List telecoms = null;
        try {
            if (contactPersonId != null) {
                telecoms = (List) telecomHome.findContactPersonTelecomsByTelecomTypeType(addressId, contactPersonId, TelecomType.EMAIL_TYPE);
            } else {
                telecoms = (List) telecomHome.findAddressTelecomsByTelecomTypeType(addressId, TelecomType.EMAIL_TYPE);
            }

        } catch (FinderException e) {
            telecoms = new ArrayList();
        }

        Telecom defaultTelecom = null;
        for (int i = 0; i < telecoms.size(); i++) {
            Telecom telecom = (Telecom) telecoms.get(i);
            if (i == 0) {
                defaultTelecom = telecom;
            }
            if (telecom.getPredetermined() != null && telecom.getPredetermined()) {
                defaultTelecom = telecom;
                break;
            }
        }

        if (defaultTelecom != null) {
            TelecomDTO telecomDTO = new TelecomDTO(defaultTelecom.getTelecomId().toString(), defaultTelecom.getData(), defaultTelecom.getDescription(),
                    (defaultTelecom.getPredetermined() != null) ? defaultTelecom.getPredetermined() : false);
            telecomDTO.setAddressId(defaultTelecom.getAddressId());
            telecomDTO.setContactPersonId(defaultTelecom.getContactPersonId());

            resultDTO.put("defaultTelecomDTO", telecomDTO);
        } else {
            resultDTO.setResultAsFailure();
        }
    }

    private void readAllAddressTelecoms(SessionContext ctx) {
        Integer addressId = new Integer(paramDTO.get("addressId").toString());
        Address address = findAddress(addressId);

        if (address != null) {
            //reading telecoms
            Collection telecoms = address.getTelecoms();
            Map telecomMap = new LinkedHashMap();
            TelecomDTO telecomDTO = null;
            for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
                Telecom telecom = (Telecom) iterator.next();
                //add telecoms of address
                if (telecom.getContactPersonId() == null) {
                    TelecomTypeSelectCmd cmd = new TelecomTypeSelectCmd();
                    cmd.putParam(TelecomTypeSelectCmd.SELECT_TYPE, TelecomTypeSelectCmd.TYPE_SINGLE);
                    cmd.putParam("companyId", telecom.getCompanyId());
                    cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, paramDTO.get("locale"));
                    cmd.putParam("telecomTypeId", telecom.getTelecomTypeId());
                    cmd.executeInStateless(ctx);
                    TelecomTypeDTO telecomTypeDTO = (TelecomTypeDTO) cmd.getResultDTO().get(TelecomTypeSelectCmd.RESULT);
                    telecomDTO = new TelecomDTO(telecom.getTelecomId().toString(), telecom.getData(),
                            telecom.getDescription(), telecom.getPredetermined().booleanValue());
                    TelecomWrapperDTO.addToMapTelecomDTO(telecomMap, telecomDTO, telecomTypeDTO);
                }
            }

            resultDTO.put("addressTelecomInfoMap", telecomMap);
        }
    }

    private void findDefaultTelecomByType() {
        Integer telecomTypeId = new Integer(paramDTO.get("telecomTypeId").toString());
        Integer addressId = new Integer(paramDTO.get("addressId").toString());
        Integer contactPersonId = null;
        if (paramDTO.get("contactPersonId") != null) {
            contactPersonId = new Integer(paramDTO.get("contactPersonId").toString());
        }

        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        List telecoms = null;
        try {
            if (contactPersonId != null) {
                telecoms = (List) telecomHome.findAllContactPersonTelecomsByTypeId(addressId, contactPersonId, telecomTypeId);
            } else {
                telecoms = (List) telecomHome.findAllAddressTelecomsByTypeId(addressId, telecomTypeId);
            }

        } catch (FinderException e) {
            telecoms = new ArrayList();
        }

        Telecom defaultTelecom = null;
        for (int i = 0; i < telecoms.size(); i++) {
            Telecom telecom = (Telecom) telecoms.get(i);
            if (i == 0) {
                defaultTelecom = telecom;
            }
            if (telecom.getPredetermined() != null && telecom.getPredetermined()) {
                defaultTelecom = telecom;
                break;
            }
        }

        if (defaultTelecom != null) {
            TelecomDTO telecomDTO = composeTelecomDTO(defaultTelecom);
            resultDTO.put("telecomDTOByType", telecomDTO);
        } else {
            resultDTO.setResultAsFailure();
        }
    }

    private Address findAddress(Integer addressId) {
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        if (addressId != null) {
            try {
                return addressHome.findByPrimaryKey(addressId);
            } catch (FinderException e) {
                log.debug("Not found Address with id: " + addressId, e);
            }
        }
        return null;
    }

    private TelecomDTO composeTelecomDTO(Telecom telecom) {
        TelecomDTO telecomDTO = null;
        if (telecom != null) {
            telecomDTO = new TelecomDTO(telecom.getTelecomId().toString(), telecom.getData(), telecom.getDescription(),
                    (telecom.getPredetermined() != null) ? telecom.getPredetermined() : false);
            telecomDTO.setAddressId(telecom.getAddressId());
            telecomDTO.setContactPersonId(telecom.getContactPersonId());
        }
        return telecomDTO;
    }

}
