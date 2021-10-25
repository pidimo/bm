package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ReadTemplateValues;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.domain.catalogmanager.LanguageHome;
import com.piramide.elwis.domain.contactmanager.Employee;
import com.piramide.elwis.dto.campaignmanager.CampaignDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignTemplateDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.CatalogConstants;
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
 * Read campaign document template values
 *
 * @author Miky
 * @version $Id: CampaignDocumentValuesReadCmd.java 10068 2011-07-01 15:57:26Z fernando $
 */
public class CampaignDocumentValuesReadCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.warn("Executing CampaignDocumentValuesReadCmd..... " + paramDTO);

        Integer campaignId = null;
        Integer activityId = null;
        Integer companyId = null;
        Integer userAddressId = null;
        Integer templateId = null;
        Integer senderEmployeeId = null;
        Integer employeeId = null;
        try {
            campaignId = new Integer(paramDTO.get("campaignId").toString());
            activityId = new Integer(paramDTO.get("activityId").toString());
            companyId = new Integer(paramDTO.get("companyId").toString());
            userAddressId = new Integer(paramDTO.get("userAddressId").toString());
            templateId = new Integer(paramDTO.get("templateId").toString());
            senderEmployeeId = new Integer(paramDTO.get("senderEmployeeId").toString());
        } catch (NumberFormatException e) {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("Common.invalid.id");
            return;
        }

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

        if (CampaignConstants.CONTACT_RESPONSIBLE.equals(senderEmployeeId.toString())) {
            //only to initialize
            employeeId = campaign.getEmployeeId();
        } else {
            employeeId = senderEmployeeId;
        }

        //read activity recipientes
        Map<String, List<CampaignContact>> recipientsByLanguageMap = CampaignGenerateUtil.getRecipientsByLanguage(listStructure, campaign, campaignTemplate, activityId);

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
        Integer contactEmployeeId = employeeId;
        List<Map> contactInfoList = new ArrayList<Map>();

        //read template values
        int failCount = 0;
        int totalContacts = 0;
        int totalProcessed = 0;
        List templateTextList = new ArrayList();
        for (Iterator<String> iterator = recipientsByLanguageMap.keySet().iterator(); iterator.hasNext(); ) {
            String languageId = iterator.next();
            List<CampaignContact> recipients = recipientsByLanguageMap.get(languageId);
            List fieldValuesList = new ArrayList();
            totalContacts += recipients.size();

            int successCount = 0;
            try {
                freeText = campaignTextHome.findByPrimaryKey(new CampaignTextPK(new Integer(languageId), templateId)).getCampaignFreeText();
            } catch (FinderException e) {
                resultDTO.setResultAsFailure();
                resultDTO.addResultMessage("Template.error.find");
                return;
            }

            for (Iterator<CampaignContact> iterator1 = recipients.iterator(); iterator1.hasNext(); ) {
                CampaignContact campaignContact = iterator1.next();

                if (CampaignConstants.CONTACT_RESPONSIBLE.equals(senderEmployeeId.toString())) {
                    Employee contactEmployee = CampaignGenerateUtil.getActivityContactResponsible(activityId, campaignContact.getAddressId(), campaignContact.getContactPersonId());
                    if (contactEmployee != null) {
                        try {
                            readTemplateValues = new ReadTemplateValues(companyId, userAddressId, contactEmployee.getEmployeeId(), isEmail, false, requestLocale, true);
                            contactEmployeeId = contactEmployee.getEmployeeId();
                        } catch (FinderException e) {
                            log.debug("Fail in read values for contact responsible..." + e);
                            failCount++;
                            continue;
                        }
                    } else {
                        log.debug("contact responsible not found...");
                        failCount++;
                        continue;
                    }
                }

                try {
                    Object[] addressValues = readTemplateValues.getFieldValues(campaignContact.getAddressId(), campaignContact.getContactPersonId(), new Integer(languageId), null);
                    fieldValuesList.add(addressValues);
                    successCount++;

                    //add contact info, only if success
                    Map contactInfoMap = new HashMap();
                    contactInfoMap.put("addressId", campaignContact.getAddressId());
                    contactInfoMap.put("contactPersonId", campaignContact.getContactPersonId());
                    contactInfoMap.put("employeeId", contactEmployeeId);
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
        resultDTO.put("activityId", activityId);

        log.warn("end Executing CampaignDocumentValuesReadCmd...." + resultDTO);
    }


    public boolean isStateful() {
        return false;
    }
}
