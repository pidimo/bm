package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.domain.campaignmanager.CampaignTemplateHome;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * Jatun S.R.L.
 * read campaign templates by document type, verify if this campaig has only one template
 *
 * @author Miky
 * @version $Id: CampaignTemplateReadByTypeCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignTemplateReadByTypeCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing CampaignTemplateReadByTypeCmd..... " + paramDTO);

        Integer campaignId = new Integer(paramDTO.get("campaignId").toString());
        Integer documentType = new Integer(paramDTO.get("documentType").toString());
        boolean hasOnlyOneTemplate = false;

        try {
            CampaignTemplateHome campaignTemplateHome = (CampaignTemplateHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEMPLATE);
            Collection campTemplates = campaignTemplateHome.findByCampaignIdAndDocumentType(campaignId, documentType);
            hasOnlyOneTemplate = (campTemplates.size() == 1);
            log.debug("campaing templates:" + campTemplates.size());
        } catch (FinderException e) {
            log.debug("Error in find camapign templates....", e);
            resultDTO.setResultAsFailure();
        }
        resultDTO.put("isOnlyOneTemplate", String.valueOf(hasOnlyOneTemplate));
    }

    public boolean isStateful() {
        return false;
    }
}
