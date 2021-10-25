package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.domain.campaignmanager.Campaign;
import com.piramide.elwis.dto.campaignmanager.CampaignDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Jan 7, 2005
 * Time: 3:51:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class LightlyCampaignCmd extends EJBCommand {
    private Log log = LogFactory.getLog(com.piramide.elwis.cmd.campaignmanager.LightlyCampaignCmd.class);

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing read campaign command ... ");
        log.debug("campaignId to read = " + paramDTO.get("campaignId"));
        //Read the product
        CampaignDTO campaignDTO = new CampaignDTO(paramDTO);
        Campaign campaign;
        try {
            campaign = (Campaign) EJBFactory.i.findEJB(campaignDTO);
            resultDTO.put("campaignName", campaign.getCampaignName());

        } catch (EJBFactoryException ex) {
            campaignDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setResultAsFailure();
        }
    }

    public boolean isStateful() {
        return false;
    }
}
