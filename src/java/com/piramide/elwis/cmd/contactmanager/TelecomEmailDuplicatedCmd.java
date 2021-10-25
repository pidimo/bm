package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.contactmanager.Telecom;
import com.piramide.elwis.domain.contactmanager.TelecomHome;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;

/**
 * Bussines logic to find duplicated telecom of type email for an address or contact person.
 *
 * @author Fernando Montaño
 * @version $Id: TelecomEmailDuplicatedCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class TelecomEmailDuplicatedCmd extends EJBCommand {

    private Log log = LogFactory.getLog(TelecomEmailDuplicatedCmd.class);
    private boolean hasDuplicates = false;


    public void executeInStateless(SessionContext ctx) {
        log.debug("Checking duplicated email telecoms");

        String email = paramDTO.getAsString("email");
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer telecomId = null;
        Integer addressId = null;
        Integer contactPersonId = null;
        if (paramDTO.get("telecomId") != null && !"".equals(paramDTO.get("telecomId"))) {
            telecomId = new Integer(paramDTO.get("telecomId").toString());
        }
        if (paramDTO.get("addressId") != null && !"".equals(paramDTO.get("addressId"))) {
            addressId = new Integer(paramDTO.get("addressId").toString());
        }
        if (paramDTO.get("contactPersonId") != null && !"".equals(paramDTO.get("contactPersonId"))) {
            contactPersonId = new Integer(paramDTO.get("contactPersonId").toString());
        }

        log.debug("email = " + email);
        log.debug("companyId = " + companyId);
        log.debug("telecomId = " + telecomId);
        log.debug("addressId = " + addressId);
        log.debug("contactPersonId = " + contactPersonId);


        try {
            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
            Collection emails = null;

            if (telecomId != null) {
                emails = telecomHome.findTelecomsWithTelecomNumberWithoutTelecomId(email, telecomId, companyId);
            } else {
                emails = telecomHome.findTelecomsWithTelecomNumber(email, companyId);
            }


            log.debug("Duplicated emails = " + emails);
            if (!emails.isEmpty()) {
                log.debug("There are duplicates");

                Integer telecomAddressId = null;

                for (Iterator iterator = emails.iterator(); iterator.hasNext();) {
                    Telecom telecom = (Telecom) iterator.next();
                    if (telecom.getContactPersonId() != null) {
                        telecomAddressId = telecom.getContactPersonId();
                        break;
                    } else {
                        telecomAddressId = telecom.getAddressId();
                        break;
                    }

                }

                log.debug("telecomAddressId = " + telecomAddressId);

                if (addressId != null && contactPersonId == null && !telecomAddressId.equals(addressId)) {
                    LightlyAddressCmd addressCmd = new LightlyAddressCmd();
                    addressCmd.putParam("addressId", telecomAddressId);
                    addressCmd.executeInStateless(ctx);
                    ResultDTO resultDto = addressCmd.getResultDTO();
                    resultDTO.put("addressName", resultDto.get("addressName"));
                    hasDuplicates = true;
                } else if (addressId != null && contactPersonId != null && !contactPersonId.equals(telecomAddressId)) {
                    LightlyAddressCmd addressCmd = new LightlyAddressCmd();
                    addressCmd.putParam("addressId", telecomAddressId);
                    addressCmd.executeInStateless(ctx);
                    ResultDTO resultDto = addressCmd.getResultDTO();
                    resultDTO.put("addressName", resultDto.get("addressName"));
                    hasDuplicates = true;
                } else if (addressId == null && contactPersonId == null) {
                    LightlyAddressCmd addressCmd = new LightlyAddressCmd();
                    addressCmd.putParam("addressId", telecomAddressId);
                    addressCmd.executeInStateless(ctx);
                    ResultDTO resultDto = addressCmd.getResultDTO();
                    resultDTO.put("addressName", resultDto.get("addressName"));
                    hasDuplicates = true;
                }


            }
        } catch (FinderException e) {
            log.debug("There is no duplicated address");
        }

    }

    public boolean isStateful() {
        return false;
    }

    public boolean hasDuplicates() {
        return hasDuplicates;
    }
}
