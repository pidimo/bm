package com.piramide.elwis.cmd.campaignmanager;

import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.piramide.elwis.cmd.campaignmanager.util.CampaignRecipientWrapper;
import com.piramide.elwis.cmd.campaignmanager.util.HtmlTemplateProcessor;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.ReadTemplateValues;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionBusinessDelegate;
import com.piramide.elwis.cmd.utils.ElwisCacheManager;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.domain.catalogmanager.LanguageHome;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.dto.campaignmanager.AttachDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignTemplateDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Jatun S.R.L.
 * Send emails to campaign activity recipients
 *
 * @author Miky
 * @version $Id: CampaignEmailSendCmd.java 10517 2015-02-26 01:45:04Z miguel $
 */
public class CampaignEmailSendCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());
    private Map contactResponsibleEmailMap;

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing CampaignEmailSendCmd..... " + paramDTO);

        if ("true".equals(paramDTO.get("sendInBackground"))) {
            saveCampaignGenerationToSendInBackground();
        } else {
            sendCampaignGeneration(ctx);
        }
    }

    private void sendCampaignGeneration(SessionContext ctx) {

        Long generationKey = null;
        Integer campaignId = null;
        Integer activityId = null;
        Integer companyId = null;
        Integer userId = null;
        Integer userAddressId = null;
        Integer templateId = null;
        Integer senderEmployeeId = null;
        Integer employeeId = null;
        Integer telecomTypeId = null;
        try {
            generationKey = new Long(paramDTO.get("generationKey").toString());
            campaignId = new Integer(paramDTO.get("campaignId").toString());
            activityId = new Integer(paramDTO.get("activityId").toString());
            companyId = new Integer(paramDTO.get("companyId").toString());
            userId = new Integer(paramDTO.get("userId").toString());
            userAddressId = new Integer(paramDTO.get("userAddressId").toString());
            templateId = new Integer(paramDTO.get("templateId").toString());
            senderEmployeeId = new Integer(paramDTO.get("senderEmployeeId").toString());
            telecomTypeId = new Integer(paramDTO.get("telecomTypeId").toString());

        } catch (NumberFormatException e) {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("Common.invalid.id");
            return;
        }

        boolean isEmail = true;
        String requestLocale = paramDTO.get("requestLocale").toString();
        String employeeMail = paramDTO.get("employeeMail").toString();
        Integer senderPrefixType = new Integer(paramDTO.get("senderPrefixType").toString());
        String senderPrefix = (String) paramDTO.get("senderPrefix");

        String fromEmail = employeeMail;
        String subject = paramDTO.getAsString("subject");
        Map generateResumeMap = new HashMap();

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
        if (CampaignConstants.CONTACT_RESPONSIBLE.equals(senderEmployeeId.toString())) {
            //only to initialize
            employeeId = campaign.getEmployeeId();
        } else {
            employeeId = senderEmployeeId;
        }

        String fromPersonal = composeEmailFromPersonal(senderPrefixType, senderPrefix, employeeId, companyId, ctx);

        //read activity recipientes
        Map<String, List<CampaignRecipientWrapper>> recipientsByLanguageMap = findSendRecipients(campaign, campaignTemplate, activityId);

        //validate templates
        if (recipientsByLanguageMap.isEmpty()) {
            resultDTO.addResultMessage("Campaign.error.html.template");
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

        //validate sender email
        if (CampaignConstants.DEFAULT_ADDRESS_OF_CONTACTRESPONSIBLE.equals(employeeMail)) {
            log.debug("Validate responsible sender email....");
            if (!allContactResponsiblesHasEmail(activityId, companyId, telecomTypeId)) {
                resultDTO.setResultAsFailure();
                return;
            }
        }

        //email attach
        String listIdAttach = (String) paramDTO.get("listIdAttach");
        List<ArrayByteWrapper> newAttachWrapperList = new ArrayList<ArrayByteWrapper>();
        if (paramDTO.get("newAttachments") != null) {
            newAttachWrapperList = (List<ArrayByteWrapper>) paramDTO.get("newAttachments");
        }

        Map<Integer, String> attachInfoMap = processAttach(listIdAttach, newAttachWrapperList, campaign, userId, generationKey);
        List attachTempPaths = new ArrayList(attachInfoMap.values());
        List<Integer> attachIdList = new ArrayList<Integer>(attachInfoMap.keySet());

        //read template values
        CampaignTextHome campaignTextHome = (CampaignTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEXT);
        CampaignFreeText freeText = null;

        //initialize contact info to communications
        Integer contactEmployeeId = employeeId;
        List<Map> contactInfoList = new ArrayList<Map>();

        int totalRecipients = 0;
        int totalMailSent = 0;
        int totalMailNotSent = 0;
        List<Map> recipientsWithoutMailList = new ArrayList<Map>();
        List<Map> recipientsFailList = new ArrayList<Map>();

        CampaignMailer mailer;
        try {
            mailer = new CampaignMailer();
            //start SMTP connection
            mailer.startConnection();
        } catch (Exception e) {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("Common.defaultSTMP.inaccessible");
            return;
        }
        long startTime = System.currentTimeMillis();

        //process campaign generation
        Integer generationId = processAndGetGenerationId(campaign, activityId, campaignTemplate, userId, attachIdList);
        Integer campaignSentLogId = processAndGetCampaignSentLogId(generationId, generationKey, activityId, companyId);

        try {

            for (Iterator<String> iterator = recipientsByLanguageMap.keySet().iterator(); iterator.hasNext();) {
                String languageId = iterator.next();
                List<CampaignRecipientWrapper> recipients = recipientsByLanguageMap.get(languageId);
                totalRecipients += recipients.size();

                int successCount = 0;
                try {
                    log.debug("before read text");
                    freeText = campaignTextHome.findByPrimaryKey(new CampaignTextPK(new Integer(languageId), templateId)).getCampaignFreeText();
                    log.debug("after read text" + freeText);
                } catch (FinderException e) {
                    resultDTO.setResultAsFailure();
                    resultDTO.addResultMessage("Template.error.find");
                    return;
                }

                //process email embedded images
                Map imageProcessResultMap = CampaignGenerateUtil.processEmailEmbeddedImages(new String(freeText.getValue()), companyId, campaignId, userId, generationKey, ctx);
                String mailBody = imageProcessResultMap.get("updatedBody").toString();
                Map emailImagesMap = (Map) imageProcessResultMap.get("imagesCidMap");

                for (Iterator<CampaignRecipientWrapper> iterator1 = recipients.iterator(); iterator1.hasNext();) {
                    CampaignRecipientWrapper recipientWrapper = iterator1.next();

                    //recipient info
                    Map contactInfoMap = new HashMap();
                    contactInfoMap.put("addressId", recipientWrapper.getAddressId());
                    contactInfoMap.put("contactPersonId", recipientWrapper.getContactPersonId());
                    contactInfoMap.put("userId", recipientWrapper.getUserId());

                    if (isRetrySendOperation()) {
                        contactInfoMap.put("sentLogContactId", recipientWrapper.getSentLogContactId());
                    }

                    if (CampaignConstants.CONTACT_RESPONSIBLE.equals(senderEmployeeId.toString())) {
                        Employee contactEmployee = CampaignGenerateUtil.getActivityContactResponsible(activityId, recipientWrapper.getAddressId(), recipientWrapper.getContactPersonId());
                        if (contactEmployee != null) {
                            try {
                                readTemplateValues = new ReadTemplateValues(companyId, userAddressId, contactEmployee.getEmployeeId(), isEmail, false, requestLocale, true);
                                contactEmployeeId = contactEmployee.getEmployeeId();
                                fromPersonal = composeEmailFromPersonal(senderPrefixType, senderPrefix, contactEmployeeId, companyId, ctx);
                            } catch (FinderException e) {
                                log.error("Fail in read values for contact responsible..." + e);

                                contactInfoMap.put("status", CampaignConstants.SentLogContactStatus.FAILED_RESPONSIBLE.getConstant());
                                addRecipientInfoInList(recipientsFailList, contactInfoMap, recipientWrapper, true);
                                totalMailNotSent++;

                                //create sent log contact
                                processSentLogContact(campaignSentLogId, generationKey, companyId, contactInfoMap);
                                continue;
                            }
                        } else {
                            log.debug("contact responsible not found...");

                            contactInfoMap.put("status", CampaignConstants.SentLogContactStatus.FAILED_RESPONSIBLE.getConstant());
                            addRecipientInfoInList(recipientsFailList, contactInfoMap, recipientWrapper, true);
                            totalMailNotSent++;

                            //create sent log contact
                            processSentLogContact(campaignSentLogId, generationKey, companyId, contactInfoMap);
                            continue;
                        }
                    }

                    try {
                        //send email
                        Map templateFieldValueMap = readTemplateValues.getFieldValuesAsMap(recipientWrapper.getAddressId(), recipientWrapper.getContactPersonId(), new Integer(languageId), telecomTypeId);
                        if (readTemplateValues.getAddressEmail() != null && readTemplateValues.getAddressEmail().trim().length() > 3) {
                            //create mail body
                            HtmlTemplateProcessor htmlTemplateProcessor = new HtmlTemplateProcessor(new StringBuilder(mailBody));
                            StringBuilder finalMailBody = htmlTemplateProcessor.buildHtmlDocument(templateFieldValueMap);

                            if (CampaignConstants.DEFAULT_ADDRESS_OF_CONTACTRESPONSIBLE.equals(employeeMail)) {
                                fromEmail = (String) contactResponsibleEmailMap.get(recipientWrapper.getUserId());
                            }


                            try {
                                mailer.sendMail(fromEmail, fromPersonal, readTemplateValues.getAddressEmail(), subject, finalMailBody.toString(), attachTempPaths, userId, emailImagesMap);

                                successCount++;
                                //add contact info, only if success
                                contactInfoMap.put("employeeId", contactEmployeeId);
                                contactInfoMap.put("fromEmail", fromEmail);
                                contactInfoMap.put("toEmail", readTemplateValues.getAddressEmail());
                                contactInfoMap.put("status", CampaignConstants.SentLogContactStatus.SUCCESS.getConstant());
                                contactInfoList.add(contactInfoMap);

                                //create communications
                                if (isCreateCommunications(paramDTO)) {
                                    createEmailSentCommunications(generationId, campaign, activityId, userId, subject, contactInfoMap);
                                }

                            } catch (ConnectionException e) {
                                log.debug("It was not possible to send the email using the local SMTP server..", e);

                                contactInfoMap.put("status", CampaignConstants.SentLogContactStatus.FAILED.getConstant());
                                contactInfoMap.put("errorMessage", composeSentEmailErrorMessage(e));
                                addRecipientInfoInList(recipientsFailList, contactInfoMap, recipientWrapper, true);
                                totalMailNotSent++;
                            } catch (Exception e1) {
                                log.error("Unexpected exception trying to send the email", e1);

                                contactInfoMap.put("status", CampaignConstants.SentLogContactStatus.FAILED_UNKNOWN.getConstant());
                                addRecipientInfoInList(recipientsFailList, contactInfoMap, recipientWrapper, true);
                                totalMailNotSent++;
                            }

                        } else {
                            log.debug("address has not email..." + recipientWrapper.getAddressId() + "-" + recipientWrapper.getContactPersonId());

                            contactInfoMap.put("status", CampaignConstants.SentLogContactStatus.FAILED_WITHOUT_EMAIL.getConstant());
                            addRecipientInfoInList(recipientsWithoutMailList, contactInfoMap, recipientWrapper, true);
                            totalMailNotSent++;
                        }
                    } catch (FinderException e) {
                        log.debug("Fail in read beans to get values for template...", e);

                        contactInfoMap.put("status", CampaignConstants.SentLogContactStatus.FAILED_UNKNOWN.getConstant());
                        addRecipientInfoInList(recipientsFailList, contactInfoMap, recipientWrapper, true);
                        totalMailNotSent++;
                    }

                    //create sent log contact
                    processSentLogContact(campaignSentLogId, generationKey, companyId, contactInfoMap);
                }

                //generation resume
                LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
                try {
                    Language language = languageHome.findByPrimaryKey(new Integer(languageId));
                    generateResumeMap.put(language.getLanguageName(), String.valueOf(successCount));
                    totalMailSent += successCount;
                } catch (FinderException e) {
                    log.debug("Finder exception, not found language....", e);
                }
            }
        } finally {
            //close mailer's smtp
            mailer.closeConnection();
        }
        log.info("Time taken sending the campaign's emails (seconds): " + (System.currentTimeMillis() - startTime) / 1000);
        log.debug("Generation fail count:" + totalMailNotSent);


        //set resume in resulDTO
        resultDTO.put("generationResume", generateResumeMap);
        resultDTO.put("totalFail", totalMailNotSent);
        resultDTO.put("totalSuccess", totalMailSent);
        resultDTO.put("totalRecipients", totalRecipients);
        resultDTO.put("withoutMailList", recipientsWithoutMailList);
        resultDTO.put("failSentList", recipientsFailList);
        resultDTO.put("templateId", templateId);

        //update totals
        updateCampaignSentLogTotals(campaignSentLogId);

        //delete cache
        deleteCampaignSendMailAttachCache(campaign, userId, generationKey);

        log.debug("end send email cmd...." + resultDTO);
    }

    private void saveCampaignGenerationToSendInBackground() {

        Long generationKey = null;
        Integer campaignId = null;
        Integer activityId = null;
        Integer companyId = null;
        Integer userId = null;
        Integer templateId = null;
        Integer telecomTypeId = null;
        try {
            generationKey = new Long(paramDTO.get("generationKey").toString());
            campaignId = new Integer(paramDTO.get("campaignId").toString());
            activityId = new Integer(paramDTO.get("activityId").toString());
            companyId = new Integer(paramDTO.get("companyId").toString());
            userId = new Integer(paramDTO.get("userId").toString());
            templateId = new Integer(paramDTO.get("templateId").toString());
            telecomTypeId = new Integer(paramDTO.get("telecomTypeId").toString());

        } catch (NumberFormatException e) {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("Common.invalid.id");
            return;
        }

        String employeeMail = paramDTO.get("employeeMail").toString();


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

        //read activity recipientes
        Map<String, List<CampaignRecipientWrapper>> recipientsByLanguageMap = findSendRecipients(campaign, campaignTemplate, activityId);

        //validate templates
        if (recipientsByLanguageMap.isEmpty()) {
            resultDTO.addResultMessage("Campaign.error.html.template");
            resultDTO.setResultAsFailure();
            return;
        }

        //validate sender email
        if (CampaignConstants.DEFAULT_ADDRESS_OF_CONTACTRESPONSIBLE.equals(employeeMail)) {
            log.debug("Validate responsible sender email....");
            if (!allContactResponsiblesHasEmail(activityId, companyId, telecomTypeId)) {
                resultDTO.setResultAsFailure();
                return;
            }
        }

        //validate smtp connection
        try {
            CampaignMailer mailer = new CampaignMailer();
            mailer.startConnection();
            mailer.closeConnection();
        } catch (Exception e) {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("Common.defaultSTMP.inaccessible");
            return;
        }

        //email attach
        String listIdAttach = (String) paramDTO.get("listIdAttach");
        List<ArrayByteWrapper> newAttachWrapperList = new ArrayList<ArrayByteWrapper>();
        if (paramDTO.get("newAttachments") != null) {
            newAttachWrapperList = (List<ArrayByteWrapper>) paramDTO.get("newAttachments");
        }

        Map<Integer, String> attachInfoMap = processAttach(listIdAttach, newAttachWrapperList, campaign, userId, generationKey);
        List<Integer> attachIdList = new ArrayList<Integer>(attachInfoMap.keySet());

        //process campaign generation
        Integer generationId = processAndGetGenerationId(campaign, activityId, campaignTemplate, userId, attachIdList);
        Integer campaignSentLogId = processAndGetCampaignSentLogId(generationId, generationKey, activityId, companyId);

        for (Iterator<String> iterator = recipientsByLanguageMap.keySet().iterator(); iterator.hasNext(); ) {
            String languageId = iterator.next();
            List<CampaignRecipientWrapper> recipients = recipientsByLanguageMap.get(languageId);

            for (Iterator<CampaignRecipientWrapper> iterator1 = recipients.iterator(); iterator1.hasNext(); ) {
                CampaignRecipientWrapper recipientWrapper = iterator1.next();

                //recipient info
                Map contactInfoMap = new HashMap();
                contactInfoMap.put("addressId", recipientWrapper.getAddressId());
                contactInfoMap.put("contactPersonId", recipientWrapper.getContactPersonId());
                contactInfoMap.put("userId", recipientWrapper.getUserId());
                contactInfoMap.put("status", CampaignConstants.SentLogContactStatus.WAITING_TO_BE_SENT_IN_BACKGROUND.getConstant());

                if (isRetrySendOperation()) {
                    contactInfoMap.put("sentLogContactId", recipientWrapper.getSentLogContactId());
                }
                //process sent log contact
                processSentLogContact(campaignSentLogId, generationKey, companyId, contactInfoMap);
            }
        }

        //update totals
        updateCampaignSentLogTotals(campaignSentLogId);
    }

    protected boolean isRetrySendOperation() {
        return false;
    }

    protected Map<String, List<CampaignRecipientWrapper>> findSendRecipients(Campaign campaign, CampaignTemplate campaignTemplate, Integer activityId) {
        return CampaignGenerateUtil.getRecipientsByLanguage(campaign, campaignTemplate, activityId);
    }

    protected Integer processAndGetGenerationId(Campaign campaign, Integer activityId, CampaignTemplate campaignTemplate, Integer userId, List attachIdList) {
        Integer generationId = null;

        CampaignGenerationCmd generationCmd = new CampaignGenerationCmd();
        generationCmd.putParam(paramDTO);
        generationCmd.putParam("op", "create");
        generationCmd.putParam("campaignId", campaign.getCampaignId());
        generationCmd.putParam("activityId", activityId);
        generationCmd.putParam("templateId", campaignTemplate.getTemplateId());
        generationCmd.putParam("companyId", campaign.getCompanyId());
        generationCmd.putParam("userId", userId);
        generationCmd.putParam("campAttachIdList", attachIdList);

        try {
            BeanTransactionBusinessDelegate.i.execute(generationCmd);
        } catch (AppLevelException e) {
            log.error("Error in execute CampaignGenerationCmd cmd..", e);
        }

        if (generationCmd.getResultDTO().isFailure()) {
            //failure messages
            if (generationCmd.getResultDTO().hasResultMessage()) {
                for (Iterator iterator = generationCmd.getResultDTO().getResultMessages(); iterator.hasNext();) {
                    resultDTO.addResultMessage((ResultMessage) iterator.next());
                }
            }
        } else {
            generationId = new Integer(generationCmd.getResultDTO().get("generationId").toString());
        }

        return generationId;
    }

    protected Integer processAndGetCampaignSentLogId(Integer generationId, Long generationKey, Integer activityId, Integer companyId) {
        Integer campaignSentLogId = null;

        CampaignSentLogCmd campaignSentLogCmd = new CampaignSentLogCmd();
        campaignSentLogCmd.putParam("op", "create");
        campaignSentLogCmd.putParam("generationId", generationId);
        campaignSentLogCmd.putParam("generationKey", generationKey);
        campaignSentLogCmd.putParam("companyId", companyId);
        campaignSentLogCmd.putParam("totalSent", CampaignGenerateUtil.countEnabledActivityRecipients(activityId));
        campaignSentLogCmd.putParam("totalSuccess", Integer.valueOf(0));

        try {
            BeanTransactionBusinessDelegate.i.execute(campaignSentLogCmd);
        } catch (AppLevelException e) {
            log.error("Error in execute CampaignSentLogCmd cmd..", e);
        }

        if (!campaignSentLogCmd.getResultDTO().isFailure()) {
            campaignSentLogId = new Integer(campaignSentLogCmd.getResultDTO().get("campaignSentLogId").toString());
        }

        return campaignSentLogId;
    }

    protected void processSentLogContact(Integer campaignSentLogId, Long generationKey, Integer companyId, Map contactInfoMap) {

        SentLogContactCmd sentLogContactCmd = new SentLogContactCmd();
        sentLogContactCmd.putParam("op", "create");
        sentLogContactCmd.putParam("campaignSentLogId", campaignSentLogId);
        sentLogContactCmd.putParam("generationKey", generationKey);
        sentLogContactCmd.putParam("companyId", companyId);
        sentLogContactCmd.putParam("addressId", contactInfoMap.get("addressId"));
        sentLogContactCmd.putParam("contactPersonId", contactInfoMap.get("contactPersonId"));
        sentLogContactCmd.putParam("errorMessage",contactInfoMap.get("errorMessage") );
        sentLogContactCmd.putParam("status", contactInfoMap.get("status"));
        sentLogContactCmd.putParam("userId", contactInfoMap.get("userId"));

        try {
            BeanTransactionBusinessDelegate.i.execute(sentLogContactCmd);
        } catch (AppLevelException e) {
            log.error("Error in execute SentLogContactCmd cmd..", e);
        }
    }

    private void updateCampaignSentLogTotals(Integer campaignSentLogId) {

        CampaignSentLogCmd campaignSentLogCmd = new CampaignSentLogCmd();
        campaignSentLogCmd.putParam("op", "update");
        campaignSentLogCmd.putParam("campaignSentLogId", campaignSentLogId);

        try {
            BeanTransactionBusinessDelegate.i.execute(campaignSentLogCmd);
        } catch (AppLevelException e) {
            log.error("Error in execute campaignSentLogCmd cmd..", e);
        }
    }

    private String composeSentEmailErrorMessage(ConnectionException e) {
        String errorMessage;
        if (e.getCause() != null && e.getCause().getMessage() != null) {
            errorMessage = e.getCause().getMessage();
        } else {
            errorMessage = e.getMessage();
        }

        if (errorMessage != null && errorMessage.length() > 250) {
            errorMessage = errorMessage.substring(0, 250);
        }
        return errorMessage;
    }

    private void addRecipientInfoInList(List<Map> list, Map contactInfoMap, CampaignRecipientWrapper recipientWrapper, boolean addNames) {
        if (addNames) {
            Map recipientNamesMap = getRecipientFailSentNames(recipientWrapper.getAddressId(), recipientWrapper.getContactPersonId());
            if (recipientNamesMap != null) {
                contactInfoMap.putAll(recipientNamesMap);
                list.add(contactInfoMap);
            }
        } else {
            list.add(contactInfoMap);
        }
    }

    /**
     * get and verify if all contact responsibles has email, set error message if has not.
     * The emails of contact responsible be saved in contactResponsibleEmailMap
     *
     * @param activityId
     * @param companyId
     * @param telecomTypeId
     * @return boolean
     */
    private boolean allContactResponsiblesHasEmail(Integer activityId, Integer companyId, Integer telecomTypeId) {
        boolean allHasEmail = true;
        contactResponsibleEmailMap = new HashMap();

        CampaignContactHome campaignContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);
        Collection<Integer> campContactResponsibles;
        try {
            campContactResponsibles = campaignContactHome.findUserIdsDISTINTCForEnabledCampaignContactsByActivity(activityId);
        } catch (FinderException e) {
            log.debug("Finder exception...", e);
            campContactResponsibles = new ArrayList();
        }
        log.debug("responsible user ids:" + campContactResponsibles);

        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        for (Iterator<Integer> iterator = campContactResponsibles.iterator(); iterator.hasNext();) {
            Integer responsibleUserId = iterator.next();
            if (!contactResponsibleEmailMap.containsKey(responsibleUserId)) {
                User campContactResponsibleUser = null;
                try {
                    campContactResponsibleUser = userHome.findByPrimaryKey(responsibleUserId);
                    Telecom telecom = telecomHome.findContactPersonDefaultTelecomsByTypeId(companyId, campContactResponsibleUser.getAddressId(), telecomTypeId);
                    contactResponsibleEmailMap.put(responsibleUserId, telecom.getData());
                } catch (FinderException e) {
                    log.debug("Error in get responsible telecom..." + e);
                    allHasEmail = false;
                    Address responsibleAddress = (Address) ExtendedCRUDDirector.i.read(new AddressDTO(campContactResponsibleUser.getAddressId()), new ResultDTO(), false);
                    resultDTO.setResultAsFailure();
                    resultDTO.addResultMessage("Campaign.sendEmails.senderEmailNotFound", responsibleAddress.getName());
                }
            }
        }
        return allHasEmail;
    }


    /**
     * read recipient address and contact person and put in Map, return null if read is fail
     *
     * @param addressId
     * @param contactPersonId
     * @return Map
     */
    private Map getRecipientFailSentNames(Integer addressId, Integer contactPersonId) {
        Map recipientNamesMap = new HashMap();
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        try {
            Address address = addressHome.findByPrimaryKey(addressId);
            recipientNamesMap.put("contactName", address.getName());
            if (contactPersonId != null) {
                Address contactPersonAsAddress = addressHome.findByPrimaryKey(contactPersonId);
                recipientNamesMap.put("contactPersonName", contactPersonAsAddress.getName());

                //if address is organization, contact person is without mail
                if (ContactConstants.ADDRESSTYPE_ORGANIZATION.equals(address.getAddressType())) {
                    recipientNamesMap.put("addressWithoutMail", CampaignConstants.CONTACTPERSON_WITHOUT_MAIL);
                } else {
                    recipientNamesMap.put("addressWithoutMail", CampaignConstants.ADDRESS_WITHOUT_MAIL);
                }
            }
        } catch (FinderException e) {
            log.debug("Fail in read address.....", e);
            return null;
        }
        return recipientNamesMap;
    }

    private void createEmailSentCommunications(Integer generationId, Campaign campaign, Integer activityId, Integer userId, String subject, Map contactInfoMap) {
        log.debug("Create email sent communications....");
        List<Map> contactInfoList = new ArrayList<Map>();
        contactInfoList.add(contactInfoMap);

        GenerationCommunicationCreateCmd commCreateCmd = new GenerationCommunicationCreateCmd();
        commCreateCmd.putParam("isEmailComm", String.valueOf(true));
        commCreateCmd.putParam("generationId", generationId);
        commCreateCmd.putParam("campaignId", campaign.getCampaignId());
        commCreateCmd.putParam("activityId", activityId);
        commCreateCmd.putParam("companyId", campaign.getCompanyId());
        commCreateCmd.putParam("userId", userId);
        commCreateCmd.putParam("note", subject);
        commCreateCmd.putParam("addressInfoList", contactInfoList);

        try {
            BeanTransactionBusinessDelegate.i.execute(commCreateCmd);
        } catch (AppLevelException e) {
            log.error("Error in execute GenerationCommunicationCreateCmd cmd..", e);
        }

        if (commCreateCmd.getResultDTO().isFailure()) {
            //only communications is failure seet messages
            if (commCreateCmd.getResultDTO().hasResultMessage()) {
                for (Iterator iterator = commCreateCmd.getResultDTO().getResultMessages(); iterator.hasNext();) {
                    resultDTO.addResultMessage((ResultMessage) iterator.next());
                }
            }
        }
    }

    /**
     * @param listIdAttach
     * @param newAttachWrapperList
     * @param campaign
     * @param userId
     * @return Map{attachId, attachTempPath}
     */
    protected Map<Integer, String> processAttach(String listIdAttach, List<ArrayByteWrapper> newAttachWrapperList, Campaign campaign, Integer userId, Long generationKey) {
        Map<Integer, String> attachInfoMap = new HashMap<Integer, String>();

        String pathFolderAttach = ElwisCacheManager.pathCampaignMailAttachFolder_CreateIfNotExist(campaign.getCompanyId(), campaign.getCampaignId(), userId, generationKey, true);

        //process campaign attach
        if (listIdAttach != null && listIdAttach.trim().length() > 0) {
            String[] attachIDs = listIdAttach.split(",");

            for (int i = 0; i < attachIDs.length; i++) {
                String attachID = attachIDs[i].trim();
                Attach attach = (Attach) ExtendedCRUDDirector.i.read(new AttachDTO(new Integer(attachID)), new ResultDTO(), false);
                if (attach != null) {
                    String pathToFile = pathFolderAttach + attach.getFilename();
                    try {
                        FileOutputStream stream = new FileOutputStream(pathToFile);
                        stream.write(attach.getCampaignFreeText().getValue());
                        stream.close();

                        attachInfoMap.put(attach.getAttachId(), pathToFile);
                    } catch (IOException e) {
                        log.debug("Canot write attach in cache...", e);
                    }
                }
            }
        }

        //process new Attachs
        for (ArrayByteWrapper arrayByteWrapper : newAttachWrapperList) {
            AttachCmd attachCmd = new AttachCmd();
            attachCmd.putParam("op", "create");
            attachCmd.putParam("campaignId", campaign.getCampaignId());
            attachCmd.putParam("companyId", campaign.getCompanyId());
            attachCmd.putParam("comment", arrayByteWrapper.getFileName());
            attachCmd.putParam("filename", arrayByteWrapper.getFileName());
            attachCmd.putParam("size", arrayByteWrapper.getFileData().length);
            attachCmd.putParam("file", arrayByteWrapper);

            try {
                BeanTransactionBusinessDelegate.i.execute(attachCmd);
            } catch (AppLevelException e) {
                log.error("Error in execute AttachCmd cmd..", e);
            }

            ResultDTO attachResultDTO = attachCmd.getResultDTO();
            if (!attachResultDTO.isFailure()) {
                String pathToFile = pathFolderAttach + arrayByteWrapper.getFileName();
                try {
                    FileOutputStream stream = new FileOutputStream(pathToFile);
                    stream.write(arrayByteWrapper.getFileData());
                    stream.close();

                    attachInfoMap.put(new Integer(attachResultDTO.get("attachId").toString()), pathToFile);
                } catch (IOException e) {
                    log.debug("Canot write attach in cache...", e);
                }
            }
        }

        return attachInfoMap;
    }

    protected void deleteCampaignSendMailAttachCache(Campaign campaign, Integer userId, Long generationKey) {
        ElwisCacheManager.deleteCampaignMailAttachFolder(campaign.getCompanyId(), campaign.getCampaignId(), userId, generationKey);
    }

    private String composeEmailFromPersonal(Integer senderPrefixType, String senderPrefix, Integer employeeId, Integer companyId, SessionContext ctx) {
        String fromPersonal = null;

        if (senderPrefixType != null) {
            if (CampaignConstants.SenderPrefixType.DEFINEDBYUSER.equal(senderPrefixType)) {
                fromPersonal = senderPrefix;

            } else if (CampaignConstants.SenderPrefixType.SENDERNAME.equal(senderPrefixType)
                    || CampaignConstants.SenderPrefixType.MAILACCOUNTPREFIX.equal(senderPrefixType)) {

                CampaignEmailSendPrefixReadCmd prefixReadCmd = new CampaignEmailSendPrefixReadCmd();
                prefixReadCmd.putParam("employeeId", employeeId);
                prefixReadCmd.putParam("companyId", companyId);
                prefixReadCmd.putParam("senderPrefixType", senderPrefixType);

                prefixReadCmd.executeInStateless(ctx);
                ResultDTO myResultDTO = prefixReadCmd.getResultDTO();
                if (!myResultDTO.isFailure()) {
                    fromPersonal = (String) myResultDTO.get("senderPrefixRead");
                }
            }
        }

        if (fromPersonal != null && "".equals(fromPersonal.trim())) {
            fromPersonal = null;
        }

        return fromPersonal;
    }

    private boolean isCreateCommunications(ParamDTO paramDTO) {
        Boolean result = paramDTO.get("createComm") != null;
        if (paramDTO.get("createComm") instanceof Boolean) {
            result = (Boolean) paramDTO.get("createComm");
        }
        return result;
    }
}
