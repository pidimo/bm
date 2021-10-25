package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.Campaign;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityContact;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityContactHome;
import com.piramide.elwis.dto.campaignmanager.CampaignDTO;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 * Cmd to verify if communication is created from campaign generation
 *
 * @author Miky
 * @version $Id: CommunicationFromCampaignGenerationCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CommunicationFromCampaignGenerationCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());
    private CampaignActivityContact campaignActivityContact;

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CommunicationFromCampaignGenerationCmd........" + paramDTO);

        Integer contactId = new Integer(paramDTO.get("contactId").toString());
        CampaignActivityContactHome campaignActivityContactHome = (CampaignActivityContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNACTIVITYCONTACT);

        try {
            campaignActivityContact = campaignActivityContactHome.findByContactIdAndGenerationIdISNOTNULL(contactId);
        } catch (FinderException e) {
            log.debug("Can't find campaign activity contact...");
        }
        boolean isCampaignGeneration = campaignActivityContact != null;
        if (isCampaignGeneration) {
            resultDTO.put("entity", campaignActivityContact);
            //read campaign info
            Campaign campaign = (Campaign) ExtendedCRUDDirector.i.read(new CampaignDTO(campaignActivityContact.getCampaignId()), new ResultDTO(), false);
            if (campaign != null) {
                resultDTO.put("campaignId", campaign.getCampaignId());
                resultDTO.put("campaignName", campaign.getCampaignName());
            }
        }

        resultDTO.put("isFromGeneration", String.valueOf(isCampaignGeneration));
    }

    public boolean isStateful() {
        return false;
    }

    public CampaignActivityContact getCampaignActivityContact() {
        return campaignActivityContact;
    }

    public boolean isCampaignGenerationCommunication() {
        return campaignActivityContact != null;
    }
}
