package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.ReadTemplateValues;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.domain.catalogmanager.LanguageHome;
import com.piramide.elwis.dto.campaignmanager.CampaignDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignTemplateDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L.
 * Read all campaign text of campaign template and read template field names
 *
 * @author Miky
 * @version $Id: ReadAllCampaignTemplateFreeTextCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ReadAllCampaignTemplateFreeTextCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ReadAllCampaignTemplateFreeTextCmd..... " + paramDTO);

        Integer templateId = new Integer(paramDTO.get("templateId").toString());

        CampaignTemplate campaignTemplate = (CampaignTemplate) ExtendedCRUDDirector.i.read(new CampaignTemplateDTO(templateId), resultDTO, false);

        if (campaignTemplate != null && !resultDTO.isFailure()) {

            Campaign campaign = (Campaign) ExtendedCRUDDirector.i.read(new CampaignDTO(campaignTemplate.getCampaignId()), new ResultDTO(), false);
            String[] fieldNames = new String[0];
            try {
                fieldNames = ReadTemplateValues.getFieldNames(campaign.getCompanyId(), campaign.getEmployeeId(), false);
                log.debug("after read field names:" + fieldNames);
            } catch (FinderException e) {
                //it would not have to happen
                log.warn("Fail in read Field Names...");
            }

            List templateTextList = new ArrayList();
            LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
            CampaignFreeTextHome campaignFreeTextHome = (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);
            for (Iterator iterator = campaignTemplate.getCampaignText().iterator(); iterator.hasNext();) {
                CampaignText campaignText = (CampaignText) iterator.next();

                Language language;
                try {
                    language = languageHome.findByPrimaryKey(campaignText.getLanguageId());
                } catch (FinderException e) {
                    log.debug("language not found...", e);
                    continue;
                }

//                log.warn("camp template description:"+campaignTemplate.getDescription());
//                log.warn("language name:"+language.getLanguageName());
//                log.warn("language id:"+language.getLanguageId());
//                log.warn("freetext id:" + campaignText.getFreeTextId());
//                log.warn("camp text Freee text(relation):" + campaignText.getCampaignFreeText());

                ///////////////////////////////////////////////
                CampaignFreeText campaignFreeText = null;
                try {
                    campaignFreeText = campaignFreeTextHome.findByPrimaryKey(campaignText.getFreeTextId());
                    log.debug("camp text Freee text(finder):" + campaignFreeText);
                    log.debug("value != null :" + (campaignFreeText.getValue() != null));
                } catch (FinderException e) {
                    log.debug("Not found freetext... " + e);
                    ////continue; //todo:enable this
                }
                ///////////////////////////////////////////////

                Map templateTextMap = new HashMap();
                templateTextMap.put("templateName", campaignTemplate.getDescription());
                templateTextMap.put("languageName", language.getLanguageName());
                templateTextMap.put("languageId", language.getLanguageId());
                //////////templateTextMap.put("byteWrapper", new ArrayByteWrapper(campaignText.getCampaignFreeText().getValue()));
                templateTextMap.put("byteWrapper", new ArrayByteWrapper(campaignFreeText.getValue()));

                templateTextList.add(templateTextMap);
                log.debug("end for iteration:");
            }
            resultDTO.put("templateTextList", templateTextList);
            resultDTO.put("fieldNames", fieldNames);

        }
    }

    public boolean isStateful() {
        return false;
    }
}
