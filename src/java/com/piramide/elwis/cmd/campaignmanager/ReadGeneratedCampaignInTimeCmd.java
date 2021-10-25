package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.CampaignGenerationHome;
import com.piramide.elwis.domain.campaignmanager.CampaignTemplate;
import com.piramide.elwis.dto.campaignmanager.CampaignTemplateDTO;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Jatun S.R.L.
 * Cmd to read generated campaigns in an time, this is to validate communication create
 *
 * @author Miky
 * @version $Id: ReadGeneratedCampaignInTimeCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ReadGeneratedCampaignInTimeCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ReadGeneratedCampaignInTimeCmd................" + paramDTO);

        Integer templateId = new Integer(paramDTO.get("templateId").toString());
        Long generationTime = new Long(paramDTO.get("generationTime").toString());

        if ("campaignLightGenerationTime".equals(getOp())) {
            verifyGenerationTime(templateId, generationTime);
        } else {
            Integer activityId = new Integer(paramDTO.get("activityId").toString());
            verifyGenerationTime(activityId, templateId, generationTime);
        }
    }

    private void verifyGenerationTime(Integer templateId, Long generationTime) {
        verifyGenerationTime(null, templateId, generationTime);
    }

    private void verifyGenerationTime(Integer activityId, Integer templateId, Long generationTime) {
        boolean alreadyGenerated = false;

        CampaignGenerationHome generationHome = (CampaignGenerationHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNGENERATION);
        Collection campGenerations = null;
        try {
            if (activityId != null) {
                campGenerations = generationHome.findByGenerationTime(activityId, templateId, generationTime);
            } else {
                campGenerations = generationHome.findByGenerationTime(templateId, generationTime);
            }

            alreadyGenerated = !campGenerations.isEmpty();
        } catch (FinderException e) {
            log.debug("Error in execute finder...", e);
            campGenerations = new ArrayList();
        }

        if (alreadyGenerated) {
            CampaignTemplate campaignTemplate = (CampaignTemplate) ExtendedCRUDDirector.i.read(new CampaignTemplateDTO(templateId), new ResultDTO(), false);
            if (campaignTemplate != null) {
                resultDTO.put("templateDescription", campaignTemplate.getDescription());
            }
        }
        resultDTO.put("alreadyGenerated", String.valueOf(alreadyGenerated));
    }

    public boolean isStateful() {
        return false;
    }
}
