package com.piramide.elwis.web.contactmanager.util;

import com.piramide.elwis.cmd.contactmanager.TelecomBatchCmd;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.controller.Controller;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.exception.ListStructureNotFoundException;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

import javax.servlet.ServletContext;
import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2.4
 */
public class TelecomBatchProcessUtil {
    private Log log = LogFactory.getLog(this.getClass());
    private FantabulousManager fantabulousManager;

    public TelecomBatchProcessUtil(ServletContext servletContext) {
        fantabulousManager = FantabulousManager.loadFantabulousManager(servletContext, "/contacts");
        if (fantabulousManager == null) {
            throw new RuntimeException("Fantabulos manager canot be loaded...");
        }
    }

    public void fixPredeterminedTelecomError() {
        log.info("Start fix predetermined telecom batch process..");

        TelecomBatchCmd cmd = new TelecomBatchCmd();
        cmd.setOp("fixPredetermined");
        cmd.putParam("listAddressId", readTelecomPredeterminedErrorAddressIds());
        cmd.putParam("listContactPersonId", readTelecomPredeterminedErrorContactPersonIds());

        try {
            BusinessDelegate.i.execute(cmd, null);
        } catch (AppLevelException e) {
            log.error("FAil execute bussines delegate", e);
        }

        log.info("End fix predetermined telecom batch process..");
    }

    private List<Map<String, Integer>> readTelecomPredeterminedErrorAddressIds() {
        log.debug("Read telecom predetermined address IDS........");

        List<Map<String, Integer>> addressIdList = new ArrayList<Map<String, Integer>>();
        String listName = "telecomAddressPredeterminedBatchList";

        //parameters to execute list of manual form
        Parameters parameters = new Parameters();

        //Execute the list
        ListStructure list = null;
        try {
            list = fantabulousManager.getList(listName);
        } catch (ListStructureNotFoundException e) {
            throw new RuntimeException("Read List " + listName + " In Fantabulous structure Fail", e);
        }

        if (list != null) {
            Collection result = Controller.getList(list, parameters);
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
                org.alfacentauro.fantabulous.result.FieldHash fieldHash = (org.alfacentauro.fantabulous.result.FieldHash) iterator.next();
                //get column defined in fantabulous list
                String countResult = (String) fieldHash.get("addressIdCount");
                String addressId = (String) fieldHash.get("addressId");
                String telecomTypeId = (String) fieldHash.get("telecomTypeId");

                if (!GenericValidator.isBlankOrNull(countResult) && !GenericValidator.isBlankOrNull(addressId)) {
                    Integer count = Integer.valueOf(countResult);
                    if (count > 1) {
                        Map<String, Integer> map = new HashMap<String, Integer>();
                        map.put("addressId", Integer.valueOf(addressId));
                        map.put("telecomTypeId", Integer.valueOf(telecomTypeId));

                        addressIdList.add(map);
                    }
                }
            }
        }
        return addressIdList;
    }

    private List<Map<String, Integer>> readTelecomPredeterminedErrorContactPersonIds() {
        log.debug("Read telecom predetermined contact person IDS........");

        List<Map<String, Integer>> idMapList = new ArrayList<Map<String, Integer>>();
        String listName = "telecomContactPersonPredeterminedBatchList";

        //parameters to execute list of manual form
        Parameters parameters = new Parameters();

        //Execute the list
        ListStructure list = null;
        try {
            list = fantabulousManager.getList(listName);
        } catch (ListStructureNotFoundException e) {
            throw new RuntimeException("Read List " + listName + " In Fantabulous structure Fail", e);
        }

        if (list != null) {
            Collection result = Controller.getList(list, parameters);
            for (Iterator iterator = result.iterator(); iterator.hasNext(); ) {
                org.alfacentauro.fantabulous.result.FieldHash fieldHash = (org.alfacentauro.fantabulous.result.FieldHash) iterator.next();
                //get column defined in fantabulous list
                String countResult = (String) fieldHash.get("addressIdCount");
                String addressId = (String) fieldHash.get("addressId");
                String contactPersonId = (String) fieldHash.get("contactPersonId");
                String telecomTypeId = (String) fieldHash.get("telecomTypeId");

                if (!GenericValidator.isBlankOrNull(countResult) && !GenericValidator.isBlankOrNull(addressId) && !GenericValidator.isBlankOrNull(contactPersonId)) {
                    Integer count = Integer.valueOf(countResult);
                    if (count > 1) {
                        Map<String, Integer> map = new HashMap<String, Integer>();
                        map.put("addressId", Integer.valueOf(addressId));
                        map.put("contactPersonId", Integer.valueOf(contactPersonId));
                        map.put("telecomTypeId", Integer.valueOf(telecomTypeId));
                        idMapList.add(map);
                    }
                }
            }
        }
        return idMapList;
    }
}
