package com.piramide.elwis.cmd.utils;

import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Jatun S.R.L.
 * class to manager folders in elwis cache directory
 *
 * @author Miky
 * @version $Id: ElwisCacheManager.java 10517 2015-02-26 01:45:04Z miguel $
 */
public class ElwisCacheManager {
    private final static Log log = LogFactory.getLog(ElwisCacheManager.class);

    public static String PATH_COMPANY_DIR = ElwisCacheManager.init();

    private static final String CAMPAIGN_DIR = "campaign";
    private static final String CAMPAIGN_GENERATION_DIR = "generation";
    private static final String CAMPAIGN_MAIL_DIR = "mail";
    private static final String CAMPAIGN_MAILATTACH_DIR = "attach";
    private static final String CAMPAIGN_DOCUMENT_DIR = "document";
    private static final String INVOICE_DIR = "invoice";
    private static final String INVOICE_GENERATION_DIR = "generation";
    private static final String INVOICE_DOCUMENT_DIR = "document";

    private static String init() {
        try {
            String path = new File(ConfigurationFactory.getValue("elwis.temp.folder") + "/company/").getCanonicalPath() + "/";
            log.debug("Initialize PATH COMPANY:" + path);
            return path;
        } catch (IOException e) {
            log.debug("Error in company path..", e);
        }
        return "/";
    }

    public static String pathCompanyFolder(Object companyId, boolean slash) {
        return PATH_COMPANY_DIR + companyId + (slash ? "/" : "");
    }

    public static String pathCampaignFolder(Object companyId, boolean slash) {
        return pathCompanyFolder(companyId, true) + CAMPAIGN_DIR + (slash ? "/" : "");
    }

    public static String pathCampaignFolder(Object companyId, Object campaignId, boolean slash) {
        return pathCampaignFolder(companyId, true) + campaignId + (slash ? "/" : "");
    }

    public static String pathCampaignGenerationFolder(Object companyId, Object campaignId, boolean slash) {
        return pathCampaignFolder(companyId, campaignId, true) + CAMPAIGN_GENERATION_DIR + (slash ? "/" : "");
    }

    public static String pathCampaignGenerationFolder(Object companyId, Object campaignId, Object userId, boolean slash) {
        return pathCampaignGenerationFolder(companyId, campaignId, true) + userId + (slash ? "/" : "");
    }

    public static String pathCampaignMailFolder(Object companyId, Object campaignId, Object userId, boolean slash) {
        return pathCampaignGenerationFolder(companyId, campaignId, userId, true) + CAMPAIGN_MAIL_DIR + (slash ? "/" : "");
    }

    public static String pathCampaignMailGenerationKeyFolder(Object companyId, Object campaignId, Object userId, Object generationKey, boolean slash) {
        return pathCampaignMailFolder(companyId, campaignId, userId, true) + generationKey + (slash ? "/" : "");
    }

    public static String pathCampaignMailAttachFolder(Object companyId, Object campaignId, Object userId, Object generationKey, boolean slash) {
        return pathCampaignMailGenerationKeyFolder(companyId, campaignId, userId, generationKey, true) + CAMPAIGN_MAILATTACH_DIR + (slash ? "/" : "");
    }

    public static String pathCampaignDocumentFolder(Object companyId, Object campaignId, Object userId, boolean slash) {
        return pathCampaignGenerationFolder(companyId, campaignId, userId, true) + CAMPAIGN_DOCUMENT_DIR + (slash ? "/" : "");
    }

    public static String pathCampaignMailAttachFolder_CreateIfNotExist(Object companyId, Object campaignId, Object userId, Object generationKey, boolean slash) {
        String path = pathCampaignMailAttachFolder(companyId, campaignId, userId, generationKey, slash);
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        return path;
    }

    public static String pathCampaignDocumentFolder_CreateIfNotExist(Object companyId, Object campaignId, Object userId, boolean slash) {
        String path = pathCampaignDocumentFolder(companyId, campaignId, userId, slash);
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        return path;
    }

    public static boolean deleteCampaignDocumentFolder(Object companyId, Object campaignId, Object userId) {
        File dir = new File(pathCampaignDocumentFolder(companyId, campaignId, userId, false));
        if (dir.exists() && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = new File(dir, children[i]).delete();
                if (!success) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean deleteCampaignMailAttachFolder(Object companyId, Object campaignId, Object userId, Object generationKey) {
        File dir = new File(pathCampaignMailAttachFolder(companyId, campaignId, userId, generationKey, false));
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = new File(dir, children[i]).delete();
                if (!success) {
                    return false;
                }
            }
            dir.delete();
        }

        //delete also generation key dir
        File generationKeyDir = new File(pathCampaignMailGenerationKeyFolder(companyId, campaignId, userId, generationKey, false));
        if (generationKeyDir.isDirectory()) {
            generationKeyDir.delete();
        }

        return true;
    }

    public static void saveGeneratedCampaignDocument(Object companyId, Object campaignId, Object userId, byte[] wordDoc, Object templateId, Object languageId) throws IOException {
        String pathToFile = pathCampaignDocumentFolder_CreateIfNotExist(companyId, campaignId, userId, true) + composeGeneratedCampaignDocumentName(templateId, languageId);
        FileOutputStream stream = new FileOutputStream(pathToFile);
        stream.write(wordDoc);
        stream.close();
    }

    public static String pathGeneratedCampaignDocumentFile(Object companyId, Object campaignId, Object userId, Object templateId, Object languageId) {
        return pathCampaignDocumentFolder(companyId, campaignId, userId, true) + composeGeneratedCampaignDocumentName(templateId, languageId);
    }

    public static String composeGeneratedCampaignDocumentName(Object templateId, Object languageId) {
        return "output_" + templateId + "_" + languageId + ".doc";
    }

    //invoice cache
    public static String pathInvoiceFolder(Object companyId, boolean slash) {
        return pathCompanyFolder(companyId, true) + INVOICE_DIR + (slash ? "/" : "");
    }

    public static String pathInvoiceFolder(Object companyId, Object invoiceId, boolean slash) {
        return pathInvoiceFolder(companyId, true) + invoiceId + (slash ? "/" : "");
    }

    public static String pathInvoiceGenerationFolder(Object companyId, Object invoiceId, boolean slash) {
        return pathInvoiceFolder(companyId, invoiceId, true) + INVOICE_GENERATION_DIR + (slash ? "/" : "");
    }

    public static String pathInvoiceGenerationFolder(Object companyId, Object invoiceId, Object userId, boolean slash) {
        return pathInvoiceGenerationFolder(companyId, invoiceId, true) + userId + (slash ? "/" : "");
    }

    public static String pathInvoiceDocumentFolder(Object companyId, Object invoiceId, Object userId, boolean slash) {
        return pathInvoiceGenerationFolder(companyId, invoiceId, userId, true) + INVOICE_DOCUMENT_DIR + (slash ? "/" : "");
    }

    public static String pathInvoiceDocumentFolder_CreateIfNotExist(Object companyId, Object invoiceId, Object userId, boolean slash) {
        String path = pathInvoiceDocumentFolder(companyId, invoiceId, userId, slash);
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        return path;
    }

    public static boolean deleteInvoiceDocumentFolder(Object companyId, Object invoiceId, Object userId) {
        File dir = new File(pathInvoiceDocumentFolder(companyId, invoiceId, userId, false));
        if (dir.exists() && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = new File(dir, children[i]).delete();
                if (!success) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static void saveGeneratedInvoiceDocument(Object companyId, Object invoiceId, Object userId, byte[] wordDoc, Object templateId, Object languageId) throws IOException {
        String pathToFile = pathInvoiceDocumentFolder_CreateIfNotExist(companyId, invoiceId, userId, true) + composeInvoiceDocumentName(templateId, languageId);
        FileOutputStream stream = new FileOutputStream(pathToFile);
        stream.write(wordDoc);
        stream.close();
    }

    public static String pathInvoiceDocumentFile(Object companyId, Object invoiceId, Object userId, Object templateId, Object languageId) {
        return pathInvoiceDocumentFolder(companyId, invoiceId, userId, true) + composeInvoiceDocumentName(templateId, languageId);
    }

    public static String composeInvoiceDocumentName(Object templateId, Object languageId) {
        return "output_" + templateId + "_" + languageId + ".doc";
    }

}
