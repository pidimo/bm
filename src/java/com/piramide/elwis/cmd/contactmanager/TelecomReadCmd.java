package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.contactmanager.Telecom;
import com.piramide.elwis.domain.contactmanager.TelecomHome;
import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.7
 */
public class TelecomReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing TelecomReadCmd......." + paramDTO);
        Integer telecomId = null;
        TelecomDTO telecomDTO = null;
        if (paramDTO.get("telecomId") != null) {
            telecomId = new Integer(paramDTO.get("telecomId").toString());
        }

        if (telecomId != null) {
            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
            try {
                Telecom telecom = telecomHome.findByPrimaryKey(telecomId);
                if (telecom != null) {
                    telecomDTO = new TelecomDTO(telecom.getTelecomId().toString(), telecom.getData(),
                            telecom.getDescription(), telecom.getPredetermined().booleanValue());
                    telecomDTO.setAddressId(telecom.getAddressId());
                    telecomDTO.setContactPersonId(telecom.getContactPersonId());
                }
            } catch (FinderException e) {
                log.debug("Telecom not found......" + telecomId);
            }
        }

        if (telecomDTO != null) {
            resultDTO.put("telecomDTO", telecomDTO);
        } else {
            resultDTO.setResultAsFailure();
        }
    }
}
