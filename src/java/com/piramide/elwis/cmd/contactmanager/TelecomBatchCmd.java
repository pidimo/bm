package com.piramide.elwis.cmd.contactmanager;

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
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2.4
 */
public class TelecomBatchCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing TelecomBatchCmd......." + paramDTO);

        if ("fixPredetermined".equals(getOp())) {
            fixPredeterminedTelecom();
        }
    }

    private void fixPredeterminedTelecom() {
        List<Map<String, Integer>> addressIdMapList = (List<Map<String,Integer>>) paramDTO.get("listAddressId");
        List<Map<String, Integer>> contactPersonIdMapList = (List<Map<String,Integer>>) paramDTO.get("listContactPersonId");

        for (Map<String, Integer> map : addressIdMapList) {
            fixPredeterminedAddress(map.get("addressId"), null, map.get("telecomTypeId"));
        }

        for (Map<String, Integer> map : contactPersonIdMapList) {
            fixPredeterminedAddress(map.get("addressId"), map.get("contactPersonId"), map.get("telecomTypeId"));
        }
    }

    private void fixPredeterminedAddress(Integer addressId, Integer contactPersonId, Integer telecomTypeId) {
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

        boolean alreadyExistPredetermined = false;
        for (int i = 0; i < telecoms.size(); i++) {
            Telecom telecom = (Telecom) telecoms.get(i);
            if (telecom.getPredetermined() != null && telecom.getPredetermined()) {

                if (alreadyExistPredetermined) {
                    telecom.setPredetermined(false);
                } else {
                    alreadyExistPredetermined = true;
                }
            }
        }
    }
}
