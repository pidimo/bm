package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.Campaign;
import com.piramide.elwis.domain.campaignmanager.CampaignTemplate;
import com.piramide.elwis.dto.campaignmanager.CampaignDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignTemplateDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Jatun S.R.L.
 * cmd to read templates of an campaign by document type
 *
 * @author Miky
 * @version $Id: ReaderCampaignTemplateCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ReaderCampaignTemplateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ReaderCampaignTemplateCmd................" + paramDTO);
        Integer campaignId = new Integer(paramDTO.get("campaignId").toString());
        Object type = paramDTO.get("documentType");

        Campaign campaign = (Campaign) ExtendedCRUDDirector.i.read(new CampaignDTO(campaignId), new ResultDTO(), false);
        List resultTemplates = new ArrayList();
        if (campaign != null) {
            Collection templates = campaign.getCampaignTemplate();
            for (Iterator iterator = templates.iterator(); iterator.hasNext();) {
                CampaignTemplate template = (CampaignTemplate) iterator.next();
                if (type != null && template.getDocumentType().toString().equals(type.toString())) {
                    CampaignTemplateDTO templateDTO = new CampaignTemplateDTO();
                    DTOFactory.i.copyToDTO(template, templateDTO);
                    resultTemplates.add(templateDTO);
                }
            }
        }
        resultDTO.put("listTemplate", resultTemplates);
    }

    public boolean isStateful() {
        return false;
    }
}
