package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.domain.contactmanager.Telecom;
import com.piramide.elwis.domain.contactmanager.TelecomHome;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author : ivan
 *         Date: Jun 27, 2006
 *         Time: 2:49:24 PM
 */
public class EmailTelecomCheckerCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());
    boolean hasDuplicated = false;

    public void executeInStateless(SessionContext ctx) {
        log.debug("executeInStateless(javax.ejb.SessionContext)");
        log.debug("paramDTO  : " + paramDTO);

        String email = (String) paramDTO.get("email");
        Integer addressId = (Integer) paramDTO.get("addressId");
        Integer contactPersonId = (Integer) paramDTO.get("contactPersonId");
        Integer telecomTypeId = (Integer) paramDTO.get("telecomTypeId");

        TelecomHome home = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

        Collection telecoms = new ArrayList();
        try {
            if (null != contactPersonId) {
                telecoms = home.findAllContactPersonTelecomsByTypeId(contactPersonId, addressId, telecomTypeId);
            } else {
                telecoms = home.findAllAddressTelecomsByTypeId(addressId, telecomTypeId);
            }
        } catch (FinderException fe) {
            log.debug("Cannot find telecoms ", fe);
        }
        for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
            Telecom telecom = (Telecom) iterator.next();
            ;

            if (telecom.getData().equals(email)) {
                hasDuplicated = true;
                break;
            }
        }
    }

    public boolean hasDuplicated() {
        return hasDuplicated;
    }

    public boolean isStateful() {
        return false;
    }
}
