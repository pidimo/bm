package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.CampaignActivity;
import com.piramide.elwis.domain.campaignmanager.CampaignContact;
import com.piramide.elwis.domain.salesmanager.SalesProcess;
import com.piramide.elwis.domain.salesmanager.SalesProcessHome;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityDTO;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 * class to verify if this campaign contact activity has ben created an sales process
 *
 * @author Miky
 * @version $Id: CampaignContactSalesProcessReadCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignContactSalesProcessReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CampaignContactSalesProcessReadCmd......... " + paramDTO);

        if ("readAll".equals(getOp())) {
            readIfAllContacthaveSalesProcess();
        } else {
            readIfThisContactHaveSalesProcess();
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void readIfThisContactHaveSalesProcess() {

        Integer addressId = new Integer(paramDTO.get("addressId").toString());
        Integer activityId = new Integer(paramDTO.get("activityId").toString());

        boolean hasProcess = false;
        SalesProcess salesProcess = null;
        SalesProcessHome salesProcessHome = (SalesProcessHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALESPROCESS);
        try {
            salesProcess = salesProcessHome.findByActivityIdAndAddressId(activityId, addressId);
        } catch (FinderException e) {
            log.debug("Not found sales process...");
        }
        if (salesProcess != null) {
            hasProcess = true;
        }
        resultDTO.put("hasProcess", new Boolean(hasProcess));

    }

    private void readIfAllContacthaveSalesProcess() {
        Integer activityId = new Integer((String) paramDTO.get("activityId"));
        CampaignActivity activity = (CampaignActivity) ExtendedCRUDDirector.i.read(new CampaignActivityDTO(activityId), resultDTO, false);

        if (activity != null) {
            Collection campContacts = activity.getCampaignContacts();
            SalesProcessHome salesProcessHome = (SalesProcessHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALESPROCESS);
            for (Iterator iterator = campContacts.iterator(); iterator.hasNext();) {
                CampaignContact campContact = (CampaignContact) iterator.next();
                SalesProcess salesProcess = null;
                try {
                    salesProcess = salesProcessHome.findByActivityIdAndAddressId(activityId, campContact.getAddressId());
                } catch (FinderException e) {
                    log.debug("Not found sales process...");
                }
                if (salesProcess == null && campContact.getActive() != null && campContact.getActive()) {
                    return;
                }
            }
            resultDTO.put("hasProcess", "true");
        }
    }
}
