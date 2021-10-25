package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.domain.campaignmanager.Campaign;
import com.piramide.elwis.domain.campaignmanager.CampaignContactHome;
import com.piramide.elwis.domain.campaignmanager.CampaignHome;
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
 * @version $Id: CampaignCriterionListCmd.java 10205 2012-05-02 20:01:09Z miguel $
 */
public class CampaignCriterionListCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug(" campaignCriterionListCMD ...  execute .." + paramDTO);

        CampaignHome campaignHome = (CampaignHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN);
        CampaignContactHome campContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);
        try {
            Campaign campaign = campaignHome.findByPrimaryKey(new Integer(paramDTO.get("campaignId").toString()));

            resultDTO.put("isDouble", campaign.getIsDouble());
            resultDTO.put("addressType", campaign.getAddressType());
            resultDTO.put("contactType", campaign.getContactType());
            resultDTO.put("includePartner", campaign.getIncludePartner());
            resultDTO.put("campaignId", campaign.getCampaignId());
            resultDTO.put("totalHits", campaign.getTotalHits());
            resultDTO.put("hasEmail", campaign.getHasEmail());
            resultDTO.put("hasEmailTelecomType", campaign.getHasEmailTelecomType());
            //TODO this should be a COUNT  not the collection at all....poor
            Collection size = campContactHome.findByCampaignIdAndActivityIdNULL(campaign.getCampaignId());
            resultDTO.put("sizeCurrentCampContact", size.size());
        } catch (FinderException e) {
            resultDTO.setResultAsFailure();
        }
    }

    public boolean isStateful() {
        return false;
    }
}
