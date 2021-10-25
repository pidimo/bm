package com.piramide.elwis.cmd.campaignmanager;

import com.jatun.commons.email.parser.HtmlEmailDOMParser;
import com.jatun.commons.email.parser.HtmlEmailParser;
import com.piramide.elwis.cmd.campaignmanager.util.CampaignRecipientWrapper;
import com.piramide.elwis.cmd.campaignmanager.util.htmlfilter.CampaignEmailEmbeddedImageFilter;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Employee;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignContactDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.EmployeeDTO;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.alfacentauro.fantabulous.controller.Controller;
import org.alfacentauro.fantabulous.controller.Parameters;
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
 * @version $Id: CampaignGenerateUtil.java 05-may-2009 11:45:30 $
 */
public class CampaignGenerateUtil {
    private static Log log = LogFactory.getLog(CampaignGenerateUtil.class);

    public static Integer countCampaignRecipients(Integer campaignId) {
        Integer count;
        CampaignContactHome campaignContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);
        try {
            count = campaignContactHome.selectCountByCampaignIdAndActivityIdNULL(campaignId);
        } catch (FinderException e) {
            count = 0;
        }

        return count;
    }

    public static Integer countEnabledActivityRecipients(Integer activityId) {
        Integer count;
        CampaignContactHome campaignContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);
        try {
            count = campaignContactHome.selectCountEnabledCampaignContactsByActivity(activityId);
        } catch (FinderException e) {
            count = 0;
        }
        return count;
    }

    public static Map<String, List<CampaignRecipientWrapper>> getRecipientsByLanguage(Campaign campaign, CampaignTemplate campaignTemplate) {
        CampaignContactHome campaignContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);
        List<CampaignContact> recipients = null;
        try {
            recipients = new ArrayList<CampaignContact>(campaignContactHome.findByCampaignIdAndActivityIdNULL(campaign.getCampaignId()));
        } catch (FinderException e) {
            log.debug("contact finder exception", e);
            recipients = new ArrayList<CampaignContact>();
        }

        return getRecipientsByLanguage(campaign, campaignTemplate, recipients);
    }

    public static Map<String, List<CampaignRecipientWrapper>> getRecipientsByLanguage(Campaign campaign, CampaignTemplate campaignTemplate, Integer activityId) {
        CampaignContactHome campaignContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);
        List<CampaignContact> recipients = null;
        try {
            recipients = new ArrayList<CampaignContact>(campaignContactHome.findEnabledCampaignContactsByActivity(activityId));
        } catch (FinderException e) {
            log.debug("contact finder exception", e);
            recipients = new ArrayList<CampaignContact>();
        }

        return getRecipientsByLanguage(campaign, campaignTemplate, recipients);

    }

    public static Integer countFailedCampaignSentLogRecipients(Integer campaignSentLogId) {
        Integer count;
        SentLogContactHome sentLogContactHome = (SentLogContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_SENTLOGCONTACT);
        try {
            count = sentLogContactHome.selectCountByFailedCampaignSentLogId(campaignSentLogId);
        } catch (FinderException e) {
            count = 0;
        }

        return count;
    }

    public static Integer countInBackgroundProcessCampaignSentLogRecipients(Integer campaignSentLogId) {
        Integer count;
        SentLogContactHome sentLogContactHome = (SentLogContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_SENTLOGCONTACT);
        try {
            count = sentLogContactHome.selectCountByCampaignSentLogIdProcessInBackground(campaignSentLogId);
        } catch (FinderException e) {
            count = 0;
        }

        return count;
    }

    public static Map<String, List<CampaignRecipientWrapper>> getSentLogRecipientsFailedByLanguage(CampaignSentLog campaignSentLog, CampaignTemplate campaignTemplate) {
        SentLogContactHome sentLogContactHome = (SentLogContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_SENTLOGCONTACT);
        List<SentLogContact> recipients = null;
        try {
            recipients = new ArrayList<SentLogContact>(sentLogContactHome.findByFailedCampaignSentLogId(campaignSentLog.getCampaignSentLogId()));
        } catch (FinderException e) {
            log.debug("contact finder exception", e);
            recipients = new ArrayList<SentLogContact>();
        }

        return getSentLogRecipientsByLanguage(campaignSentLog, campaignTemplate, recipients);
    }

    public static Map<String, List<CampaignRecipientWrapper>> getSentLogContactRecipientByLanguage(Integer sentLogContactId, CampaignTemplate campaignTemplate) {
        SentLogContactHome sentLogContactHome = (SentLogContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_SENTLOGCONTACT);
        CampaignSentLogHome campaignSentLogHome = (CampaignSentLogHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNSENTLOG);

        List<SentLogContact> recipients = new ArrayList<SentLogContact>();
        CampaignSentLog campaignSentLog = null;
        try {
            SentLogContact sentLogContact = sentLogContactHome.findByPrimaryKey(sentLogContactId);
            recipients.add(sentLogContact);

            campaignSentLog = campaignSentLogHome.findByPrimaryKey(sentLogContact.getCampaignSentLogId());
        } catch (FinderException e) {
            log.debug("sent log contact finder exception", e);
        }

        return getSentLogRecipientsByLanguage(campaignSentLog, campaignTemplate, recipients);
    }

    private static Map<String, List<CampaignRecipientWrapper>> getSentLogRecipientsByLanguage(CampaignSentLog campaignSentLog, CampaignTemplate campaignTemplate, List<SentLogContact> recipients) {

        Map<String, List<CampaignRecipientWrapper>> resultMap = new HashMap<String, List<CampaignRecipientWrapper>>();

        if (recipients.size() > 0) {
            List<CampaignText> templates = findCampaignTemplateTexts(campaignTemplate.getTemplateId(), campaignTemplate.getCompanyId());

            Address campaignResponsible = (Address) EJBFactory.i.findEJB(new AddressDTO(campaignSentLog.getCampaignGeneration().getCampaign().getEmployeeId()));
            Address company = (Address) EJBFactory.i.findEJB(new AddressDTO(campaignSentLog.getCompanyId()));
            Integer templateId = campaignTemplate.getTemplateId();

            for (Iterator<SentLogContact> iterator = recipients.iterator(); iterator.hasNext();) {
                SentLogContact sentLogContact = iterator.next();

                CampaignRecipientWrapper recipientWrapper = composeRecipientWrapper(sentLogContact);

                Integer languageId;
                if (templates.size() == 1) {
                    languageId = templates.get(0).getLanguageId();
                } else {
                    languageId = findRecipientTemplateLanguage(sentLogContact.getAddressId(), sentLogContact.getContactPersonId(), campaignResponsible, company, templateId);
                    if (languageId == null) {
                        languageId = templates.get(0).getLanguageId();
                    }
                }

                //add recipient in language
                if (resultMap.containsKey(languageId.toString())) {
                    resultMap.get(languageId.toString()).add(recipientWrapper);
                } else {
                    List<CampaignRecipientWrapper> campContactList = new ArrayList<CampaignRecipientWrapper>();
                    campContactList.add(recipientWrapper);
                    resultMap.put(languageId.toString(), campContactList);
                }
            }
        }

        return resultMap;
    }

    /**
     * get activity recipients by language
     *
     * @param campaign
     * @param campaignTemplate
     * @param recipients
     * @return Map
     */
    private static Map<String, List<CampaignRecipientWrapper>> getRecipientsByLanguage(Campaign campaign, CampaignTemplate campaignTemplate, List<CampaignContact> recipients) {

        Map<String, List<CampaignRecipientWrapper>> resultMap = new HashMap<String, List<CampaignRecipientWrapper>>();

        if (recipients.size() > 0) {
            List<CampaignText> templates = findCampaignTemplateTexts(campaignTemplate.getTemplateId(), campaignTemplate.getCompanyId());

            Address campaignResponsible = (Address) EJBFactory.i.findEJB(new AddressDTO(campaign.getEmployeeId()));
            Address company = (Address) EJBFactory.i.findEJB(new AddressDTO(campaign.getCompanyId()));
            Integer templateId = campaignTemplate.getTemplateId();

            for (Iterator<CampaignContact> iterator = recipients.iterator(); iterator.hasNext();) {
                CampaignContact campaignContact = iterator.next();

                CampaignRecipientWrapper recipientWrapper = composeRecipientWrapper(campaignContact);

                Integer languageId;
                if (templates.size() == 1) {
                    languageId = templates.get(0).getLanguageId();
                } else {
                    languageId = findRecipientTemplateLanguage(campaignContact.getAddressId(), campaignContact.getContactPersonId(), campaignResponsible, company, templateId);
                    if (languageId == null) {
                        languageId = templates.get(0).getLanguageId();
                    }
                }

                //add recipient in language
                if (resultMap.containsKey(languageId.toString())) {
                    resultMap.get(languageId.toString()).add(recipientWrapper);
                } else {
                    List<CampaignRecipientWrapper> campContactList = new ArrayList<CampaignRecipientWrapper>();
                    campContactList.add(recipientWrapper);
                    resultMap.put(languageId.toString(), campContactList);
                }
            }
        }

        return resultMap;
    }

    private static Integer findRecipientTemplateLanguage(Integer addressId, Integer contactPersonId, Address campaignResponsible, Address company, Integer templateId) {
        Integer languageId = null;

        CampaignTextHome campaignTextHome = (CampaignTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNTEXT);

        try {
            Address contactPerson = null;
            if (contactPersonId != null) {
                contactPerson = (Address) EJBFactory.i.findEJB(new AddressDTO(contactPersonId));
            }
            Address address = (Address) EJBFactory.i.findEJB(new AddressDTO(addressId));

            Integer defaultIdLang = getDefaultLang(contactPerson, address, campaignResponsible, company);
            log.debug("DefaultLang    :" + defaultIdLang);

            CampaignText campaignText = null;
            try {
                campaignText = campaignTextHome.findByPrimaryKey(new CampaignTextPK(defaultIdLang, templateId));
            } catch (FinderException e) {
            }

            if (campaignText == null) {
                try {
                    campaignText = campaignTextHome.findDefaultByTemplate(templateId, company.getCompanyId());
                } catch (FinderException e) {
                }
                if (campaignText == null) {
                    try {
                        campaignText = campaignTextHome.findByPrimaryKey(new CampaignTextPK(company.getLanguageId(), templateId));
                    } catch (FinderException e) {
                    }
                    if (campaignText == null) {
                        try {
                            campaignText = campaignTextHome.findByPrimaryKey(new CampaignTextPK(campaignResponsible.getLanguageId(), templateId));
                        } catch (FinderException e) {
                        }
                    }
                }
            }

            log.debug("TemplateTextResultant :::" + campaignText);
            //add recipient in language
            if (campaignText != null) {
                languageId = campaignText.getLanguageId();
            }
        } catch (EJBFactoryException e) {
            log.debug("Address not found...");
        }

        return languageId;
    }

    private static CampaignRecipientWrapper composeRecipientWrapper(CampaignContact campaignContact) {
        CampaignRecipientWrapper recipientWrapper = null;
        if (campaignContact != null) {
            recipientWrapper = new CampaignRecipientWrapper();
            recipientWrapper.setRecipientType(CampaignRecipientWrapper.RecipientType.CAMPAIGN_CONTACT);
            recipientWrapper.setCampaignId(campaignContact.getCampaignId());
            recipientWrapper.setCampaignContactId(campaignContact.getCampaignContactId());

            recipientWrapper.setAddressId(campaignContact.getAddressId());
            recipientWrapper.setContactPersonId(campaignContact.getContactPersonId());
            recipientWrapper.setActivityId(campaignContact.getActivityId());
            recipientWrapper.setUserId(campaignContact.getUserId());
        }
        return recipientWrapper;
    }

    private static CampaignRecipientWrapper composeRecipientWrapper(SentLogContact sentLogContact) {
        CampaignRecipientWrapper recipientWrapper = null;
        if (sentLogContact != null) {
            recipientWrapper = new CampaignRecipientWrapper();
            recipientWrapper.setRecipientType(CampaignRecipientWrapper.RecipientType.SENT_LOG_CONTACT);
            recipientWrapper.setSentLogContactId(sentLogContact.getSentLogContactId());

            recipientWrapper.setAddressId(sentLogContact.getAddressId());
            recipientWrapper.setContactPersonId(sentLogContact.getContactPersonId());
            recipientWrapper.setUserId(sentLogContact.getUserId());
        }
        return recipientWrapper;
    }

    public static Map<String, List<CampaignContact>> getRecipientsByLanguage(ListStructure listStructure, Campaign campaign, CampaignTemplate campaignTemplate) {
        Parameters params = new Parameters();
        params.addSearchParameter("campaignId", campaign.getCampaignId().toString());
        return getRecipientsByLanguage(listStructure, params, campaign, campaignTemplate);
    }

    public static Map<String, List<CampaignContact>> getRecipientsByLanguage(ListStructure listStructure, Campaign campaign, CampaignTemplate campaignTemplate, Integer activityId) {
        Parameters params = new Parameters();
        params.addSearchParameter("activityId", activityId.toString());
        return getRecipientsByLanguage(listStructure, params, campaign, campaignTemplate);
    }

    private static Map<String, List<CampaignContact>> getRecipientsByLanguage(ListStructure listStructure, Parameters params, Campaign campaign, CampaignTemplate campaignTemplate) {
        log.warn("Excecuting overwrite getRecipientsByLanguage....");
        Map<String, List<CampaignContact>> resultMap = new HashMap<String, List<CampaignContact>>();

        if (listStructure != null) {
            //get fantabulous list structure, this is ordered by organization and contact person name
            //execute list
            log.debug("Executing fantabulous structure....");

            listStructure.setExecuteFirstTime(true);
            Collection resultList = Controller.getList(listStructure, params);
            log.debug("RESULT--------------" + resultList);

            if (resultList.size() > 0) {
                List<CampaignText> templates = findCampaignTemplateTexts(campaignTemplate.getTemplateId(), campaignTemplate.getCompanyId());

                CampaignTextHome campaignTextHome = (CampaignTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNTEXT);

                Address campaignResponsible = (Address) EJBFactory.i.findEJB(new AddressDTO(campaign.getEmployeeId()));
                Address company = (Address) EJBFactory.i.findEJB(new AddressDTO(campaign.getCompanyId()));
                Integer templateId = campaignTemplate.getTemplateId();

                for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
                    Map map = (Map) iterator.next();
                    //read camp contact
                    Integer campContactId = new Integer(map.get("campaignContactId").toString());
                    Integer campaignId = new Integer(map.get("campaignId").toString());

                    CampaignContactPK pk = new CampaignContactPK(campContactId, campaignId);
                    CampaignContactDTO campContactDTO = new CampaignContactDTO();
                    campContactDTO.setPrimKey(pk);
                    ResultDTO contResultDTO = new ResultDTO();
                    CampaignContact campaignContact = (CampaignContact) ExtendedCRUDDirector.i.read(campContactDTO, contResultDTO, false);

                    if (campaignContact != null) {

                        if (templates.size() == 1) {
                            //if exist only one template
                            String languageIdKey = templates.get(0).getLanguageId().toString();
                            if (resultMap.containsKey(languageIdKey)) {
                                resultMap.get(languageIdKey).add(campaignContact);
                            } else {
                                List<CampaignContact> campContactList = new ArrayList<CampaignContact>();
                                campContactList.add(campaignContact);
                                resultMap.put(languageIdKey, campContactList);
                            }

                        } else {
                            try {
                                Address contactPerson = null;
                                if (campaignContact.getContactPersonId() != null) {
                                    contactPerson = (Address) EJBFactory.i.findEJB(new AddressDTO(campaignContact.getContactPersonId()));
                                }
                                Address address = (Address) EJBFactory.i.findEJB(new AddressDTO(campaignContact.getAddressId()));

                                Integer defaultIdLang = getDefaultLang(contactPerson, address, campaignResponsible, company);
                                log.debug("DefaultLang    :" + defaultIdLang);

                                CampaignText campaignText = null;
                                try {
                                    campaignText = campaignTextHome.findByPrimaryKey(new CampaignTextPK(defaultIdLang, templateId));
                                } catch (FinderException e) {
                                }
                                if (campaignText == null) {
                                    try {
                                        campaignText = campaignTextHome.findDefaultByTemplate(templateId, campaign.getCompanyId());
                                    } catch (FinderException e) {
                                    }
                                    if (campaignText == null) {
                                        try {
                                            campaignText = campaignTextHome.findByPrimaryKey(new CampaignTextPK(company.getLanguageId(), templateId));
                                        } catch (FinderException e) {
                                        }
                                        if (campaignText == null) {
                                            try {
                                                campaignText = campaignTextHome.findByPrimaryKey(new CampaignTextPK(campaignResponsible.getLanguageId(), templateId));
                                            } catch (FinderException e) {
                                            }
                                            if (campaignText == null) {
                                                campaignText = (CampaignText) templates.get(0);
                                            }
                                        }
                                    }
                                }
                                log.debug("TemplateTextResultant :::" + campaignText);
                                //add recipient in language
                                if (campaignText != null) {
                                    if (resultMap.containsKey(campaignText.getLanguageId().toString())) {
                                        resultMap.get(campaignText.getLanguageId().toString()).add(campaignContact);
                                    } else {
                                        List<CampaignContact> campContactList = new ArrayList<CampaignContact>();
                                        campContactList.add(campaignContact);
                                        resultMap.put(campaignText.getLanguageId().toString(), campContactList);
                                    }
                                }
                            } catch (EJBFactoryException e) {
                                log.debug("Address not found...");
                            }

                        }
                    }
                }
            }
        }

        return resultMap;
    }


    /**
     * get languageId for address recipient
     *
     * @param address
     * @param organization
     * @param employee
     * @param company
     * @return languageId
     */
    public static Integer getDefaultLang(Address address, Address organization, Address employee, Address company) {

        Integer defaultLang = null;
        if (address != null && address.getLanguageId() != null) {
            defaultLang = address.getLanguageId();
        } else if (organization != null && organization.getLanguageId() != null) {
            defaultLang = organization.getLanguageId();
        } else if (employee.getLanguageId() != null) {
            defaultLang = employee.getLanguageId();
        } else if (company.getLanguageId() != null) {
            defaultLang = company.getLanguageId();
        }

        log.debug(" .. language by default ..." + defaultLang);
        return defaultLang;
    }

    /**
     * get responsible of an activity contact as Employee
     *
     * @param activityId
     * @param addressId
     * @param contactPersonId
     * @return Employee
     */
    public static Employee getActivityContactResponsible(Integer activityId, Integer addressId, Integer contactPersonId) {
        Employee contactEmployee = null;
        CampaignContactHome campaignContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);
        CampaignContact activityContact = null;
        try {
            if (contactPersonId != null) {
                activityContact = campaignContactHome.findByActivityIdAddressIdContactPersonId(activityId, addressId, contactPersonId);
            } else {
                activityContact = campaignContactHome.findByActivityIdAddressIdContactPersonNULL(activityId, addressId);
            }
        } catch (FinderException e) {
            log.debug("Fail find activity contact......." + e);
        }

        if (activityContact != null) {
            User contactUser = null;
            try {
                if (activityContact.getUserId() != null) {
                    contactUser = (User) EJBFactory.i.findEJB(new UserDTO(activityContact.getUserId()));
                    contactEmployee = (Employee) EJBFactory.i.findEJB(new EmployeeDTO(contactUser.getAddressId()));
                }
            } catch (EJBFactoryException e) {
                log.debug("Fail find contact employee......." + e);
            }
        }
        return contactEmployee;
    }


    /**
     * process embedded images in campaign email body
     *
     * @param emailBody
     * @param companyId
     * @param campaignId
     * @param userId
     * @param generationKey
     * @param ctx
     * @return Map
     */
    public static Map processEmailEmbeddedImages(String emailBody, Integer companyId, Integer campaignId, Integer userId, Long generationKey, SessionContext ctx) {

        String newBody = emailBody;
        Map embeddedImagesCidMap = new HashMap();

        //filter html body by store image id
        CampaignEmailEmbeddedImageFilter imgTagFilter = new CampaignEmailEmbeddedImageFilter(companyId, campaignId, userId, generationKey, ctx);
        HtmlEmailParser parser = new HtmlEmailDOMParser();
        parser.addFilter(imgTagFilter);
        try {
            parser.parseHtml(emailBody);
            newBody = parser.getHtml().toString();
            embeddedImagesCidMap = imgTagFilter.getEmbeddedImagesCidMap();
        } catch (Exception e) {
            log.debug("filter embedded image IMG tags FAIL", e);
        }

        Map resultMap = new HashMap();
        resultMap.put("updatedBody", newBody);
        resultMap.put("imagesCidMap", embeddedImagesCidMap);

        return resultMap;
    }

    private static List<CampaignText> findCampaignTemplateTexts(Integer templateId, Integer companyId) {
        List<CampaignText> campaignTextList;
        CampaignTextHome campaignTextHome = (CampaignTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEXT);
        try {
            Collection campaignTexts = campaignTextHome.findByTemplateId(templateId, companyId);
            campaignTextList = new ArrayList<CampaignText>(campaignTexts);
        } catch (FinderException e) {
            campaignTextList = new ArrayList<CampaignText>();
        }
        return campaignTextList;
    }

}
