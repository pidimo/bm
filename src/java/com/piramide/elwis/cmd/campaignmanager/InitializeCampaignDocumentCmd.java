package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.utils.CampaignResultPageList;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.domain.catalogmanager.LanguageHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignTemplateDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.io.IOException;
import java.util.*;

/**
 * AlfaCentauro Team
 *
 * @author Tayes
 * @version $Id: InitializeCampaignDocumentCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class InitializeCampaignDocumentCmd extends EJBCommand {
    private final static Log log = LogFactory.getLog("InitializeCampaignDocumentCmd");


    public void executeInStateless(SessionContext ctx) {
        log.debug("Init ... campaignDocumentCMD ... ");

        final String COMPANY = "companyId";
        final String CURRENTUSER = "currentUserId";
        final String CAMPAIGN = "campaignId";
        try {
            /* log.debug("CLASS:COMP =  " + paramDTO.get(COMPANY).getClass() + " : " + paramDTO.get(COMPANY));
 log.debug("CLASS:CURRENT =  " + paramDTO.get(CURRENTUSER).getClass() + " : " + paramDTO.get(CURRENTUSER));*/
            //log.debug("CLASS:CAMP =  " + paramDTO.get(CAMPAIGN).getClass() + " : " + paramDTO.get(CAMPAIGN));

            log.debug("PARAMDTO....................>" + paramDTO);

            Integer templateId = new Integer(paramDTO.get("templateId").toString());

            Campaign campaign = (Campaign) EJBFactory.i.findEJB(new CampaignDTO(new Integer(paramDTO.get(CAMPAIGN).toString())));
            CampaignActivity campaignActivity = (CampaignActivity) EJBFactory.i.findEJB(new CampaignActivityDTO(new Integer(paramDTO.get("activityId").toString())));
            Address currentUser = (Address) EJBFactory.i.findEJB(new AddressDTO(campaign.getEmployeeId()));
            Address company = (Address) EJBFactory.i.findEJB(new AddressDTO(campaign.getCompanyId()));

            CampaignTemplate campaignTemplates = (CampaignTemplate)
                    EJBFactory.i.findEJB(new CampaignTemplateDTO(templateId));

            CampaignContactHome campaignContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);


            log.debug("After extract beans ... ");
            Collection campaignTemplate = campaignTemplates.getCampaignText();

            if (campaignTemplate.size() < 1) {
                resultDTO.addResultMessage("Campaign.error.templates", campaign.getCampaignName());
                resultDTO.setResultAsFailure();
                return;
            } else {

                //List recipients = new ArrayList(campaignActivity.getCampaignContacts());
                List recipients = new ArrayList(campaignContactHome.findEnabledCampaignContactsByActivity(campaignActivity.getActivityId()));
                if (recipients.size() == 0) {
                    if (campaignActivity.getCampaignContacts().size() > 0) {
                        resultDTO.addResultMessage("Campaign.msg.emptyValidRecipients");
                    } else {
                        resultDTO.addResultMessage("Campaign.msg.emptyRecipients");
                    }

                    //resultDTO.setForward("");
                    resultDTO.setResultAsFailure();
                    resultDTO.put("RecipientError", "true");
                    return;
                }
                int pageSize = -1;

                CampaignResultPageList pageList = new CampaignResultPageList(templateId, campaign.getCampaignId(), campaign.getCompanyId(), pageSize);

                pageList.initializeCampaign();
                log.debug("After initialize campaignResultPageList");
                //if (!pageList.initializeStatusResultList(campaignTemplate.size(), recipients.size())) { deleted
                log.debug("         - Re-calculate recipients....");
                ArrayList templates = new ArrayList(campaignTemplate);
                if (campaignTemplate.size() == 1) {
                    log.debug("Casting campaign.templateText");

                    log.debug("call: oneTemplate");
                    oneTemplate(recipients, (CampaignText) templates.get(0), campaign.getCampaignId(), pageList);
                } else {
                    log.debug("call: calculateAddressByLanguage");
                    calculateAddressByLanguage(templateId, templates, recipients, campaign, currentUser, company, pageList);
                }
                log.debug("After calculate values...");
                pageList.finishNoPages();
                //}

                Map documentByLanguage = new HashMap();
                for (Iterator iterator = pageList.getStatus().entrySet().iterator(); iterator.hasNext();) {
                    Map.Entry entry = (Map.Entry) iterator.next();

                    LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
                    Language language = languageHome.findByPrimaryKey((Integer) entry.getKey());
                    documentByLanguage.put(language.getLanguageName(), entry.getValue());
                }
                resultDTO.put("employeeId", campaign.getEmployeeId());
                resultDTO.put("documentByLanguage", documentByLanguage);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("Common.invalid.id");
            return;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FinderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void oneTemplate(List recipients, CampaignText template, Integer campaignId, CampaignResultPageList resultPageList) {
        log.debug("OneTemplate ......");
        try {
            for (Iterator iterator = recipients.iterator(); iterator.hasNext();) {
                CampaignContact campaignContact = (CampaignContact) iterator.next();
                log.debug(" ---- Iteration     :" + campaignContact.getAddressId() + " - " + campaignContact.getContactPersonId());
                resultPageList.addResultNoPages(template.getLanguageId(), campaignContact.getAddressId(), campaignContact.getContactPersonId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // calculate contact by language
    private void calculateAddressByLanguage(Integer templateId, ArrayList templates, List recipients, Campaign campaign, Address currentUser, Address company, CampaignResultPageList pageList) {
        log.debug("[calculateAddressByLanguage]:Init");
        CampaignTextHome campaignTextHome = (CampaignTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNTEXT);

        for (Iterator iterator1 = recipients.iterator(); iterator1.hasNext();) {
            CampaignContact campaignContact = (CampaignContact) iterator1.next();
            log.debug("Iteration:" + campaignContact);
            try {
                Address contactPerson = null;
                if (campaignContact.getContactPersonId() != null) {
                    contactPerson = (Address) EJBFactory.i.findEJB(new AddressDTO(campaignContact.getContactPersonId()));
                }
                Address address = (Address) EJBFactory.i.findEJB(new AddressDTO(campaignContact.getAddressId()));
                log.debug("call:getDefaultLang");

                Integer defaultIdLang = getDefaultLang(contactPerson, address, currentUser, company);
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
                                campaignText = campaignTextHome.findByPrimaryKey(new CampaignTextPK(currentUser.getLanguageId(), templateId));
                            } catch (FinderException e) {
                            }
                            if (campaignText == null) {
                                //List templates = (List) campaign.getCampaignText();
                                campaignText = (CampaignText) templates.get(0);
                            }
                        }
                    }
                }
                log.debug("TemplateTextResultant :::" + campaignText);

                pageList.addResultNoPages(campaignText.getLanguageId(), address.getAddressId(), contactPerson != null ? contactPerson.getAddressId() : null);
            } catch (EJBFactoryException e) {
            } catch (IOException e) {
                log.debug("Error en archivos...");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private Integer getDefaultLang(Address address, Address organization, Address company, Address employee) {

        Integer defaultLang = null;
        if (address != null && address.getLanguageId() != null) {
            defaultLang = address.getLanguageId();
        } else if (organization != null && organization.getLanguageId() != null) {
            defaultLang = organization.getLanguageId();
        } else if (company.getLanguageId() != null) {
            defaultLang = company.getLanguageId();
        } else if (employee.getLanguageId() != null) {
            defaultLang = employee.getLanguageId();
        }

        log.debug(" .. language by default ..." + defaultLang);
        return defaultLang;
    }

    public boolean isStateful() {
        return false;
    }
}
