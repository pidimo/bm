package com.piramide.elwis.cmd.campaignmanager;

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
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignLightDocumentValuesReadCmd.java 06-may-2009 15:38:32 $
 */
public class CampaignLightDocumentValuesReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.warn("Executing CampaignLightDocumentValuesReadCmd..... " + paramDTO);

        Integer employeeId = null;
        Integer campaignId = new Integer(paramDTO.get("campaignId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer userAddressId = new Integer(paramDTO.get("userAddressId").toString());
        Integer templateId = new Integer(paramDTO.get("templateId").toString());
        Integer senderEmployeeId = new Integer(paramDTO.get("senderEmployeeId").toString());

        String requestLocale = paramDTO.get("requestLocale").toString();
        //get fantabulous list structure, this is ordered by organization and contact person name
        ListStructure listStructure = (ListStructure) paramDTO.get("FantaStructure");

        log.debug("Find Entities...");
        Campaign campaign = null;
        CampaignTemplate campaignTemplate;
        try {
            campaign = (Campaign) EJBFactory.i.findEJB(new CampaignDTO(campaignId));
            campaignTemplate = (CampaignTemplate) EJBFactory.i.findEJB(new CampaignTemplateDTO(templateId));
        } catch (EJBFactoryException e) {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("Common.invalid.id");
            log.debug("Fail... Entity...\n:" + e);
            return;
        }

        log.debug("After load entities....");
        boolean isEmail = false;
        employeeId = senderEmployeeId;

        //read activity recipientes
        Map<String, List<CampaignContact>> recipientsByLanguageMap = CampaignGenerateUtil.getRecipientsByLanguage(listStructure, campaign, campaignTemplate);

        //validate templates
        if (recipientsByLanguageMap.isEmpty()) {
            resultDTO.addResultMessage("Campaign.error.templates", campaign.getCampaignName());
            resultDTO.setResultAsFailure();
            return;
        }

        ReadTemplateValues readTemplateValues = null;
        try {
            readTemplateValues = new ReadTemplateValues(companyId, userAddressId, employeeId, isEmail, false, requestLocale, true);
        } catch (FinderException e) {
            log.debug("Error in read values for template...", e);
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("Common.invalid.id");
            return;
        }
        String[] fieldNames = readTemplateValues.getFieldNames();

        CampaignTextHome campaignTextHome = (CampaignTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEXT);
        CampaignFreeText freeText = null;

        //initialize contact info to communications
        List<Map> contactInfoList = new ArrayList<Map>();

        //read template values
        int failCount = 0;
        int totalContacts = 0;
        int totalProcessed = 0;
        List templateTextList = new ArrayList();
        for (Iterator<String> iterator = recipientsByLanguageMap.keySet().iterator(); iterator.hasNext();) {
            String languageId = iterator.next();
            List<CampaignContact> recipients = recipientsByLanguageMap.get(languageId);
            List fieldValuesList = new ArrayList();
            totalContacts += recipients.size();

            int successCount = 0;
            try {
                log.warn("before read text");
                freeText = campaignTextHome.findByPrimaryKey(new CampaignTextPK(new Integer(languageId), templateId)).getCampaignFreeText();
                log.warn("after read text " + freeText);
            } catch (FinderException e) {
                resultDTO.setResultAsFailure();
                resultDTO.addResultMessage("Template.error.find");
                return;
            }

            for (Iterator<CampaignContact> iterator1 = recipients.iterator(); iterator1.hasNext();) {
                CampaignContact campaignContact = iterator1.next();

                try {
                    Object[] addressValues = readTemplateValues.getFieldValues(campaignContact.getAddressId(), campaignContact.getContactPersonId(), new Integer(languageId), null);
                    fieldValuesList.add(addressValues);
                    successCount++;

                    //add contact info, only if success
                    Map contactInfoMap = new HashMap();
                    contactInfoMap.put("addressId", campaignContact.getAddressId());
                    contactInfoMap.put("contactPersonId", campaignContact.getContactPersonId());
                    contactInfoMap.put("employeeId", employeeId);
                    contactInfoList.add(contactInfoMap);

                } catch (FinderException e) {
                    log.debug("Fail in read beans to get values for template...", e);
                    failCount++;
                    continue;
                }
            }


            //generation resume
            LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
            try {
                Language language = languageHome.findByPrimaryKey(new Integer(languageId));

                //template values data
                Map documentValuesMap = new HashMap();
                documentValuesMap.put("languageId", languageId);
                documentValuesMap.put("languageName", language.getLanguageName());
                documentValuesMap.put("recipientCount", String.valueOf(successCount));
                documentValuesMap.put("valuesList", fieldValuesList);
                documentValuesMap.put("freeText", new ArrayByteWrapper(freeText.getValue()));
                templateTextList.add(documentValuesMap);

                totalProcessed += successCount;
            } catch (FinderException e) {
                log.debug("Finder exception, not found language....", e);
            }
        }

        //set in resultDTO
        resultDTO.put("totalContact", totalContacts);
        resultDTO.put("totalProcessed", totalProcessed);
        resultDTO.put("totalFail", failCount);

        resultDTO.put("templateId", templateId);
        resultDTO.put("templateName", campaignTemplate.getDescription());
        resultDTO.put("companyId", companyId);
        resultDTO.put("campaignId", campaignId);
        resultDTO.put("fieldNames", fieldNames);
        resultDTO.put("templateValuesList", templateTextList);

        //to create communications
        resultDTO.put("contactInfoList", contactInfoList);

        log.warn("end Executing ...." + resultDTO);
    }


    public boolean isStateful() {
        return false;
    }
}
