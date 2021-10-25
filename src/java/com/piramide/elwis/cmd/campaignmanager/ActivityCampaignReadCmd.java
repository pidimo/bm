package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.Campaign;
import com.piramide.elwis.domain.campaignmanager.CampaignActivity;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 * cmd to lightly read campaign from avtivity
 *
 * @author Miky
 * @version $Id: ActivityCampaignReadCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ActivityCampaignReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ActivityCampaignReadCmd................" + paramDTO);
        Integer activityId = new Integer(paramDTO.get("activityId").toString());
        CampaignActivity activity = (CampaignActivity) ExtendedCRUDDirector.i.read(new CampaignActivityDTO(activityId), new ResultDTO(), false);

        if (activity != null) {
            Campaign campaign = (Campaign) ExtendedCRUDDirector.i.read(new CampaignDTO(activity.getCampaignId()), new ResultDTO(), false);
            resultDTO.put("campaignName", campaign.getCampaignName());
            resultDTO.put("campaignId", campaign.getCampaignId());
        }
    }

    public boolean isStateful() {
        return false;
    }
}