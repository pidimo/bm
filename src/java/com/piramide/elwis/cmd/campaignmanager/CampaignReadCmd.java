package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.campaignmanager.Campaign;
import com.piramide.elwis.domain.campaignmanager.CampaignContactHome;
import com.piramide.elwis.dto.campaignmanager.CampaignDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CampaignReadCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class CampaignReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing read campaign command");
        log.debug("Operation = " + getOp());

        boolean checkReferences = false;

        if (null != paramDTO.get("withReferences")) {
            checkReferences = Boolean.valueOf(paramDTO.get("withReferences").toString());
        }

        Campaign campaign = (Campaign) ExtendedCRUDDirector.i.read(new CampaignDTO(paramDTO), resultDTO, checkReferences);


        if (campaign != null) {
            CampaignContactHome ccHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);
            try {
                Collection campContacts = ccHome.findByCampaignIdAndActivityIdNULL(campaign.getCampaignId());
                resultDTO.put("campContactSize", campContacts.size());
            } catch (FinderException e) {
                log.debug(" ... campContact not found ...");
            }


            if (checkReferences) {
                log.debug("Check campaignActivity relations");
                CampaignActivityCmd cmde = new CampaignActivityCmd();
                cmde.putParam("campaignId", campaign.getCampaignId());
                cmde.setOp("checkRelations");
                cmde.executeInStateless(ctx);

                Boolean hasRelations = (Boolean) cmde.getResultDTO().get("hasRelations");

                if (hasRelations) {
                    resultDTO.addResultMessage("Campaign.error.delete");
                    resultDTO.setForward("fail");
                }
            }

            if (campaign.getCampaignFreeText() != null) {
                resultDTO.put("remarkValue", new String(campaign.getCampaignFreeText().getValue()));
                resultDTO.put("campaignName", campaign.getCampaignName());
            }
            resultDTO.put("campaignId", campaign.getCampaignId());

            if ("checkIsInternalUser".equals(getOp())) {
                isInternalUser(campaign.getEmployeeId(), campaign.getCompanyId());
            }
        }

    }

    private void isInternalUser(Integer employeeId, Integer companyId) {
        if (null == employeeId) {
            resultDTO.put("isInternalUser", false);
        } else {
            UserHome uh = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
            try {
                User usr = uh.findByAddressId(companyId, employeeId);
                if (AdminConstants.INTERNAL_USER.equals(usr.getType().toString()) && usr.getActive()) {
                    resultDTO.put("userId", usr.getUserId());
                    resultDTO.put("isInternalUser", true);
                } else {
                    resultDTO.put("isInternalUser", false);
                }
            } catch (FinderException e) {
                resultDTO.put("isInternalUser", false);
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
