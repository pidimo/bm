package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.domain.campaignmanager.CampaignTemplate;
import com.piramide.elwis.domain.campaignmanager.CampaignTemplateHome;
import com.piramide.elwis.domain.campaignmanager.CampaignText;
import com.piramide.elwis.domain.campaignmanager.CampaignTextHome;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.contactmanager.ContactHome;
import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;


/**
 * Compose file document name to freetext
 * @author Miky
 * @version $Id: FreeTextComposeNameCmd.java 2009-09-28 06:14:16 PM $
 */
public class FreeTextComposeNameCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing FreeTextComposeNameCmd..... " + paramDTO);

        Integer freeTextId = Integer.valueOf(paramDTO.get("freeTextId").toString());
        String freeTextDocumentName = null;
        if (freeTextId != null) {
            freeTextDocumentName = composeCommunicationDocumentName(freeTextId);
            if (freeTextDocumentName == null) {
                freeTextDocumentName = composeTemplateDocName(freeTextId);
            }
            if (freeTextDocumentName == null) {
                freeTextDocumentName = composeCampaignTemplateDocName(freeTextId);
            }

            if (freeTextDocumentName == null) {
                freeTextDocumentName = composeInvoiceTemplateDocName(freeTextId);
            }

            if (freeTextDocumentName == null) {
                freeTextDocumentName = composeInvoiceReminderLevelDocName(freeTextId);
            }
        }

        if (freeTextDocumentName != null) {
            resultDTO.put("documentName", freeTextDocumentName);
        }
    }

    /**
     * compose document name if freetext is of communication
     * @param freeTextId id
     * @return String
     */
    private String composeCommunicationDocumentName(Integer freeTextId) {
        String communicationDocName = null;
        ContactHome contactHome = (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
        try {
            Contact contact = contactHome.findByDocumentFreeTextId(freeTextId);
            communicationDocName = wordDocumentName(contact.getNote());
        } catch (FinderException e) {
            log.debug("Not found communication with freetext id.." + freeTextId);
        }

        return communicationDocName;
    }

    /**
     * compose document name if freetext is of communication template
     * @param freeTextId id
     * @return String
     */
    private String composeTemplateDocName(Integer freeTextId) {
        String templateDocName = null;
        TemplateTextHome templateTextHome = (TemplateTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TEMPLATETEXT);
        TemplateHome templateHome = (TemplateHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TEMPLATE);
        LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);

        try {
            TemplateText templateText = templateTextHome.findByDocumentFreeTextId(freeTextId);
            Template template = templateHome.findByPrimaryKey(templateText.getTemplateId());
            Language language = languageHome.findByPrimaryKey(templateText.getLanguageId());
            templateDocName = wordDocumentName(template.getDescription() + " (" + language.getLanguageName() + ")");
        } catch (FinderException e) {
            log.debug("Error in find template text with freetext id.." + freeTextId);
        }

        return templateDocName;
    }

    /**
     * compose document name if freetext is of campaign template
     * @param freeTextId id
     * @return Strign
     */
    private String composeCampaignTemplateDocName(Integer freeTextId) {
        String templateDocName = null;
        CampaignTextHome campaignTextHome = (CampaignTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEXT);
        CampaignTemplateHome campaignTemplateHome = (CampaignTemplateHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEMPLATE);
        LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);

        try {
            CampaignText campaignText = campaignTextHome.findByDocumentFreeTextId(freeTextId);
            CampaignTemplate campaignTemplate = campaignTemplateHome.findByPrimaryKey(campaignText.getTemplateId());
            Language language = languageHome.findByPrimaryKey(campaignText.getLanguageId());
            templateDocName = wordDocumentName(campaignTemplate.getDescription() + " (" + language.getLanguageName() + ")");
        } catch (FinderException e) {
            log.debug("Error in find campaign template text with freetext id.." + freeTextId);
        }

        return templateDocName;
    }

    /**
     * compose document name if freetext is of invoice template
     * @param freeTextId id
     * @return String
     */
    private String composeInvoiceTemplateDocName(Integer freeTextId) {
        String templateDocName = null;
        InvoiceTextHome invoiceTextHome = (InvoiceTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICETEXT);
        InvoiceTemplateHome invoiceTemplateHome = (InvoiceTemplateHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICETEMPLATE);
        LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);

        try {
            InvoiceText invoiceText = invoiceTextHome.findByDocumentFreeTextId(freeTextId);
            InvoiceTemplate invoiceTemplate = invoiceTemplateHome.findByPrimaryKey(invoiceText.getTemplateId());
            Language language = languageHome.findByPrimaryKey(invoiceText.getLanguageId());
            templateDocName = wordDocumentName(invoiceTemplate.getTitle() + " (" + language.getLanguageName() + ")");
        } catch (FinderException e) {
            log.debug("Error in find invoice template text with freetext id.." + freeTextId);
        }

        return templateDocName;
    }

    /**
     * compose document name if freetext is of invoice reminder level text
     * @param freeTextId id
     * @return String
     */
    private String composeInvoiceReminderLevelDocName(Integer freeTextId) {
        String templateDocName = null;
        ReminderTextHome reminderTextHome = (ReminderTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_REMINDERTEXT);
        ReminderLevelHome reminderLevelHome = (ReminderLevelHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_REMINDERLEVEL);
        LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);

        try {
            ReminderText reminderText = reminderTextHome.findByDocumentFreeTextId(freeTextId);
            ReminderLevel reminderLevel = reminderLevelHome.findByPrimaryKey(reminderText.getReminderLevelId());
            Language language = languageHome.findByPrimaryKey(reminderText.getLanguageId());
            templateDocName = wordDocumentName(reminderLevel.getName() + " (" + language.getLanguageName() + ")");
        } catch (FinderException e) {
            log.debug("Error in find invoice reminder level text with freetext id.." + freeTextId);
        }

        return templateDocName;
    }

    private String wordDocumentName(String name) {
        return ((name != null && name.trim().length() > 0) ? name.trim() + ".doc" : null);
    }

    public boolean isStateful() {
        return false;
    }
}
