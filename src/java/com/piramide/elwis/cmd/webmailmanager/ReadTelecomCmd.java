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
import java.util.*;

/**
 * @author : ivan
 * @version : $Id ReadTelecomCmd ${time}
 */
public class ReadTelecomCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        String op = paramDTO.getOp();
        String email = (String) paramDTO.get("email");
        Integer companyId = (Integer) paramDTO.get("companyId");

        if ("readFirstTelecom".equals(op)) {
            getFirstTelecom(email, companyId);
        }

        if ("readAllTelecoms".equals(op)) {
            getAllTelecoms(email, companyId);
        }
        if ("readChiefList".equals(op)) {
            getMyChiefList(email, companyId);
        }

        if ("readTelecomJustOne".equals(op)) {
            getTelecomJustOneCoincidence(email, companyId);
        }
    }

    private void getFirstTelecom(String email, Integer companyId) {
        TelecomHome home = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        Collection telecoms = null;

        try {
            telecoms = home.findTelecomsWithTelecomNumber(email, companyId);
        } catch (FinderException e) {
            log.error("Cannot find telecom with email = " + email + " and companyId = " + companyId + "\n" + e);
            resultDTO.setResultAsFailure();
        }
        if (null != telecoms && !telecoms.isEmpty()) {
            Telecom telecom = (Telecom) ((List) telecoms).get(0);
            Map dto = new HashMap();
            dto.put("addressId", telecom.getAddressId());
            dto.put("contactPersonId", telecom.getContactPersonId());
            dto.put("telecomId", telecom.getTelecomId());
            dto.put("telecomTypeId", telecom.getTelecomTypeId());
            resultDTO.put("telecomDTO", dto);
        }
    }

    private void getAllTelecoms(String email, Integer companyId) {
        TelecomHome home = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        Collection telecoms = null;
        List telecomDTOs = new ArrayList();
        try {
            telecoms = home.findTelecomsWithTelecomNumber(email, companyId);
        } catch (FinderException e) {
            log.error("Cannot find telecom with email = " + email + " and companyId = " + companyId + "\n" + e);
            resultDTO.setResultAsFailure();
        }
        if (null != telecoms && !telecoms.isEmpty()) {
            for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
                Telecom telecom = (Telecom) iterator.next();
                Map dto = new HashMap();
                dto.put("addressId", telecom.getAddressId());
                dto.put("contactPersonId", telecom.getContactPersonId());
                dto.put("data", telecom.getData());

                telecomDTOs.add(dto);
            }
        }
        resultDTO.put("telecoms", telecomDTOs);
    }

    private void getMyChiefList(String email, Integer companyId) {
        TelecomHome home = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        Collection telecoms = null;
        List myChiefList = new ArrayList();
        List myPersonalList = new ArrayList();
        try {
            telecoms = home.findTelecomsWithTelecomNumber(email, companyId);
        } catch (FinderException e) {
            log.error("Cannot find telecom with email = " +
                    email +
                    " and companyId = " +
                    companyId + "\n" + e);
            resultDTO.setResultAsFailure();
        }
        if (null != telecoms && !telecoms.isEmpty()) {

            for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
                Telecom telecom = (Telecom) iterator.next();
                if (null != telecom.getContactPersonId()) {
                    Map map = new HashMap(3);
                    map.put("addressId", telecom.getAddressId());
                    map.put("contactPersonId", telecom.getContactPersonId());
                    map.put("email", telecom.getData());
                    myChiefList.add(map);
                } else {
                    Map map = new HashMap(2);
                    map.put("addressId", telecom.getAddressId());
                    map.put("email", telecom.getData());
                    myPersonalList.add(map);
                }
            }
        }
        resultDTO.put("myChiefList", myChiefList);
        resultDTO.put("myPersonalList", myPersonalList);
    }

    private void getTelecomJustOneCoincidence(String telecomNumber, Integer companyId) {
        TelecomHome home = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

        if (telecomNumber != null) {
            Collection telecoms = null;
            try {
                telecoms = home.findTelecomsWithTelecomNumber(telecomNumber, companyId);
            } catch (FinderException e) {
                log.error("Cannot find telecom with email = " + telecomNumber + " and companyId = " + companyId + "\n" + e);
                resultDTO.setResultAsFailure();
            }
            if (null != telecoms && telecoms.size() == 1) {
                Telecom telecom = (Telecom) ((List) telecoms).get(0);
                Map dto = new HashMap();
                dto.put("addressId", telecom.getAddressId());
                dto.put("contactPersonId", telecom.getContactPersonId());
                dto.put("telecomId", telecom.getTelecomId());
                dto.put("telecomTypeId", telecom.getTelecomTypeId());
                resultDTO.put("telecomMap", dto);
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
