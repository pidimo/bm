package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.CampaignCriterionValue;
import com.piramide.elwis.dto.campaignmanager.CampaignCriterionValueDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.8
 */
public class CampaignCriterionValueReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CampaignCriterionValueReadCmd................" + paramDTO);
        Integer campCriterionValueId = new Integer(paramDTO.get("campCriterionValueId").toString());

        CampaignCriterionValue campaignCriterionValue = (CampaignCriterionValue) ExtendedCRUDDirector.i.read(new CampaignCriterionValueDTO(campCriterionValueId), resultDTO, false);
    }

    public boolean isStateful() {
        return false;
    }
}