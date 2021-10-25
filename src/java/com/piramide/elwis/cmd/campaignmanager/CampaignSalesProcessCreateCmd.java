package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.salesmanager.SalesProcessCmd;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.campaignmanager.CampaignActivity;
import com.piramide.elwis.domain.campaignmanager.CampaignContact;
import com.piramide.elwis.domain.salesmanager.SalesProcess;
import com.piramide.elwis.domain.salesmanager.SalesProcessHome;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityDTO;
import com.piramide.elwis.dto.salesmanager.SalesProcessDTO;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 * cmd to create sales proces to all campaign contact of an activity
 *
 * @author Miky
 * @version $Id: CampaignSalesProcessCreateCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignSalesProcessCreateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CampaignSalesProcessCreateCmd................" + paramDTO);
        Integer activityId = new Integer((String) paramDTO.get("activityId"));
        CampaignActivity activity = (CampaignActivity) ExtendedCRUDDirector.i.read(new CampaignActivityDTO(activityId), resultDTO, false);

        if (activity != null) {
            Collection campContacts = activity.getCampaignContacts();
            SalesProcessHome salesProcessHome = (SalesProcessHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALESPROCESS);
            for (Iterator iterator = campContacts.iterator(); iterator.hasNext();) {
                CampaignContact campContact = (CampaignContact) iterator.next();
                if (campContact.getActive() != null && campContact.getActive()) {
                    SalesProcess salesProcess = null;
                    try {
                        salesProcess = salesProcessHome.findByActivityIdAndAddressId(activityId, campContact.getAddressId());
                    } catch (FinderException e) {
                        log.debug("Not found sales process...");
                    }
                    if (salesProcess == null) {
                        //create sales proces to this contact
                        SalesProcessDTO processDTO = new SalesProcessDTO(paramDTO);
                        processDTO.put("addressId", campContact.getAddressId());

                        if (campContact.getUserId() != null) {
                            User user = (User) ExtendedCRUDDirector.i.read(new UserDTO(campContact.getUserId()), new ResultDTO(), false);
                            processDTO.put("employeeId", user.getAddressId());
                        }

                        SalesProcessCmd salesProcesCmd = new SalesProcessCmd();
                        salesProcesCmd.putParam(processDTO);
                        salesProcesCmd.setOp(CRUDDirector.OP_CREATE);
                        salesProcesCmd.executeInStateless(ctx);
                    }
                }
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}