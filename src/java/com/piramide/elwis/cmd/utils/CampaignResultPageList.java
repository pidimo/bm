package com.piramide.elwis.cmd.utils;

import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

/**
 * AlfaCentauro Team
 *
 * @author Tayes
 * @version $Id: CampaignResultPageList.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class CampaignResultPageList {
    private final static Log log = LogFactory.getLog(CampaignResultPageList.class);
    public static final int STATUS_EXIST = 0;
    public static final int STATUS_CREATE = 1;
    private static final String ext = "temp";

    String path;
    int pageSize;
    String templateId;
    String campaignId;
    int companyId;

    //List resultList;
    Map result;
    Map status;

    public static String PATH_CAMPAIGN_DIR = CampaignResultPageList.init();
    private static final String TEMPLATES_CACHE_FILE = "template";
    private static final String IMAGES_CACHE_FILE = "images.cache";
    private static final String PATH_TEMPLATE_DIR = "template/";
    private static final String CONFIG_FILE = "mailing.conf";
    private static final String TEMPLATES_DOC = "template";

    private static String init() {
        try {
            String path = new File(ConfigurationFactory.getValue("elwis.temp.folder") + "/campaign/").getCanonicalPath() + "/";
            log.debug("Initialize PATH CAMPAIGN:" + path);
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/";
    }

    public static String pathCampaignFolder(Object templateId, Object campaignId, boolean slash) {
        /*if (slash)
            return PATH_CAMPAIGN_DIR + campaignId.toString() + "/" + templateId + "/";*/
        return PATH_CAMPAIGN_DIR + campaignId.toString() + "/" + templateId + (slash ? "/" : "");
    }

    public static String pathCampaignFolder(Object templateId, Object campaignId) {
        return pathCampaignFolder(templateId, campaignId, true);

    }

    public static String pathCampaignTemplateFolder(Object templateId, Object campaignId) {
        //System.out.println("CREATEPATH:" + pathCampaignFolder(campaignId) + PATH_TEMPLATE_DIR);
        return pathCampaignFolder(templateId, campaignId) + PATH_TEMPLATE_DIR;
    }


    public static boolean existCampaignTemplateCacheFile(Object templateId, Object campaignId, Object languageId) {
        return new File(pathCampaignTemplateCacheFile(templateId, campaignId, languageId)).exists();
    }

    public static boolean existCampaignTemplateFolder(Object templateId, Object campaignId) {
        return new File(pathCampaignFolder(templateId, campaignId) + PATH_TEMPLATE_DIR).exists();
    }

    public static boolean existCampaignFolder(Object templateId, Object campaignId) {
        return new File(pathCampaignFolder(templateId, campaignId)).exists();
    }

    public static String pathCampaignTemplateCacheFile(Object templateId, Object campaignId, Object languageId) {
        return pathCampaignTemplateFolder(templateId, campaignId) + TEMPLATES_CACHE_FILE + "-" + languageId.toString() + ".cache";
    }

    public static String pathCampaignTemplateHtml(Object templateId, Object campaignId, Object languageId) {
        return pathCampaignTemplateFolder(templateId, campaignId) + TEMPLATES_CACHE_FILE + "-" + languageId.toString() + ".html";
    }

    public static String pathCampaignTemplateDoc(Object templateId, Object campaignId, Object languageId) {
        return pathCampaignTemplateFolder(templateId, campaignId) + TEMPLATES_DOC + "-" + languageId.toString() + ".doc";
    }

    public static String pathCampaignImagesCacheFile(Object templateId, Object campaignId) {
        return pathCampaignTemplateFolder(templateId, campaignId) + IMAGES_CACHE_FILE;
    }

    /**
     * delete campaign template dir from elwis cache
     *
     * @param campaignId
     * @param templateId
     * @return true if is successful
     */
    public static boolean deleteCampaignTemplateDir(Integer campaignId, Integer templateId) {
        try {
            File dir = new File(CampaignResultPageList.pathCampaignTemplateFolder(templateId, campaignId));
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = new File(dir, children[i]).delete();
                    if (!success) {
                        return false;
                    }
                }
            }
            return dir.delete();

        } catch (Exception e) {
            log.debug("Can't delete cache:" + CampaignResultPageList.pathCampaignTemplateFolder(templateId, campaignId));
        }
        return false;
    }


    public CampaignResultPageList(Integer templateId, Integer campaignId, Integer companyId, int pageSize) {
        this.campaignId = campaignId.toString();
        this.companyId = companyId;
        this.templateId = templateId.toString();
        result = new HashMap();
        status = new HashMap();
        this.pageSize = pageSize;
    }

    public CampaignResultPageList() {
    }

    public CampaignResultPageList(Integer templateId, Integer campaignId, Integer companyId) {
        this.templateId = templateId.toString();
        this.campaignId = campaignId.toString();
        this.companyId = companyId.intValue();
        status = new HashMap();
    }

    private String nameFile(Integer languageId, int page) {
        String fileName = new String(new StringBuffer(CampaignResultPageList.pathCampaignFolder(templateId, new Integer(campaignId))).append(languageId)
                .append("_").append(page)
                .append(".").append(ext));
        log.debug("NAME FILE--->:" + fileName);
        return fileName;
    }

    public void addResult(Integer languageId, Integer addressId, Integer contactPersonId) throws IOException {
        List files = new ArrayList();
        BufferedWriter writer = null;
        StatusResultList statusResultList;
        if (result.containsKey(languageId)) {
            files = (List) result.get(languageId);
            statusResultList = (StatusResultList) status.get(languageId);

        } else {
            files.add(new BufferedWriter(new FileWriter(nameFile(languageId, 0))));
            statusResultList = new StatusResultList(pageSize, languageId);
            result.put(languageId, files);
            status.put(languageId, statusResultList);
        }
        if (statusResultList.incrementPageSizeFromCurrentPage()) {
            BufferedWriter bufferedWriter;
            if (files.size() > 0) {
                bufferedWriter = (BufferedWriter) files.get(statusResultList.getPage() - 1);
                bufferedWriter.flush();
                bufferedWriter.close();
            }
            bufferedWriter = new BufferedWriter(new FileWriter(nameFile(languageId, statusResultList.getPage())));
            files.add(bufferedWriter);
            writer = bufferedWriter;
        } else {
            writer = (BufferedWriter) files.get(statusResultList.getPage());
        }
        writer.write(addressId + "-" + contactPersonId + "\r\n");
    }

    public void addResultNoPages(Integer languageId, Integer addressId, Integer contactPersonId) throws IOException {
        log.debug(" ... addResultNoPages function excute   ... ");
        BufferedWriter writer = null;
        StatusResultList statusResultList;
        if (result.containsKey(languageId)) {
            statusResultList = (StatusResultList) status.get(languageId);
            writer = (BufferedWriter) result.get(languageId);

        } else {
            writer = new BufferedWriter(new FileWriter(nameFile(languageId, 0)));
            statusResultList = new StatusResultList(pageSize, languageId);
            result.put(languageId, writer);
            status.put(languageId, statusResultList);
        }

        statusResultList.incrementPageSizeFromCurrentPageNoPagination();
        writer.write(addressId + "-" + contactPersonId + "\r\n");
    }


    public List getResultPage(Integer languageId, int page) {
        List list = new ArrayList();
        try {
            BufferedReader in = new BufferedReader(new FileReader(nameFile(languageId, page)));
            String str;
            while ((str = in.readLine()) != null) {
                list.add(process(str));
            }
            in.close();
        } catch (IOException e) {
        }
        return list;
    }

    private Integer[] process(String str) {
        Integer[] ids = new Integer[2];
        String addressId;
        String contactPersonId;
        int ini = str.indexOf('-');
        addressId = str.substring(0, ini);
        contactPersonId = str.substring(ini + 1, str.length());
        ids[0] = new Integer(addressId);
        ids[1] = "null".equals(contactPersonId) ? null : new Integer(contactPersonId);
        return ids;
    }

    public Map getStatus() {
        return status;
    }

    public void finish() throws IOException {
        Properties properties = new Properties();
        StringBuffer languages = new StringBuffer();
        for (Iterator iterator = status.values().iterator(); iterator.hasNext();) {
            StatusResultList resultList = (StatusResultList) iterator.next();
            properties.put(resultList.getLanguageId() + "_pages", Integer.toString(resultList.getPage()));
            properties.put(resultList.getLanguageId() + "_size", Integer.toString(resultList.getSize()));
            properties.put(resultList.getLanguageId() + "_version", Integer.toString(resultList.getSize()));
            languages.append(resultList.getLanguageId());
            if (iterator.hasNext()) {
                languages.append(",");
            }
        }
        properties.put("languages", new String(languages));
        properties.store(new FileOutputStream(new File(CampaignResultPageList.pathCampaignFolder(templateId, campaignId) + CONFIG_FILE)), "Campaign Config File");

        for (Iterator iterator = result.values().iterator(); iterator.hasNext();) {
            List files = (List) iterator.next();
            BufferedWriter bufferedWriter = (BufferedWriter) files.get(files.size() - 1);
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }

    public void finishNoPages() throws IOException {
        Properties properties = new Properties();
        StringBuffer languages = new StringBuffer();
        for (Iterator iterator = status.values().iterator(); iterator.hasNext();) {
            StatusResultList resultList = (StatusResultList) iterator.next();
            properties.put(resultList.getLanguageId() + "_pages", Integer.toString(resultList.getPage()));
            properties.put(resultList.getLanguageId() + "_size", Integer.toString(resultList.getSize()));
            languages.append(resultList.getLanguageId());
            if (iterator.hasNext()) {
                languages.append(",");
            }
        }
        properties.put("languages", new String(languages));
        properties.store(new FileOutputStream(new File(CampaignResultPageList.pathCampaignFolder(templateId, campaignId) + CONFIG_FILE)), "Campaign Config File");

        for (Iterator iterator = result.values().iterator(); iterator.hasNext();) {
            BufferedWriter bufferedWriter = (BufferedWriter) iterator.next();
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }

    public int initializeCampaign() {

        //TemplateHTMLUtil
        if (!CampaignResultPageList.existCampaignFolder(templateId, campaignId)) {
            if ((new File(CampaignResultPageList.pathCampaignFolder(templateId, campaignId, false)).mkdirs())) {
                return STATUS_CREATE;
            }
        }
        return STATUS_EXIST;
    }

    public boolean initializeStatusResultList(int templatesSize, int recipients) {
        log.debug(" .... initializeStatusResultLIST function execute .....");
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(CampaignResultPageList.pathCampaignFolder(templateId, campaignId) + CONFIG_FILE)));
        } catch (IOException e) {
            log.debug("Not exist.....");
            return false;
        }
        String languages = properties.getProperty("languages");
        StringTokenizer stringTokenizer = new StringTokenizer(languages, ",");
        log.debug("Validating 1. Templates quantity -> Cache:" + stringTokenizer.countTokens() + " - Current:" + templatesSize);

        if (stringTokenizer.countTokens() != templatesSize) {
            return false;
        }
        int totalRecipientsLoad = 0;
        while (stringTokenizer.hasMoreTokens()) {
            String languageId = stringTokenizer.nextToken();
            StatusResultList resultList = new StatusResultList(0, new Integer(languageId));
            int page = Integer.parseInt(properties.getProperty(languageId + "_pages", "-1"));
            int size = Integer.parseInt(properties.getProperty(languageId + "_size", "-1"));
            totalRecipientsLoad += size;
            resultList.setPage(page);
            resultList.setResultSize(size);
            status.put(new Integer(languageId), resultList);
        }
        log.debug("Validating 2. Recipients quantity -> Cache:" + totalRecipientsLoad + " - Current:" + recipients);
        if (totalRecipientsLoad != recipients) {
            return false;
        }
        return true;
    }

    /*public void initializeStatusResultList() {
        File dir = new File(new String(getCampaignDir()));

        String[] files = dir.list();
        if (files != null)
            for (int i = 0; i < files.length; i++)
                addStatusResult(files[i]);
    }*/

    private void addStatusResult(String file) {
        int languageIdPos = file.indexOf("_");

        String languageId = null;

        if (languageIdPos > 0) {
            languageId = file.substring(0, languageIdPos);
        }


        if (languageId != null) {
            if (status.containsKey(new Integer(languageId))) {
                StatusResultList statusResultList = (StatusResultList) status.get(new Integer(languageId));
                //statusResultList.addPage();
                statusResultList.setPage(statusResultList.getPage() + 1);
                //status.put(languageId, statusResultList);
            } else {
                StatusResultList statusResultList = new StatusResultList(pageSize, new Integer(languageId));
                status.put(new Integer(languageId), statusResultList);
            }
        }
    }

    public static boolean deleteDir(Integer campaignId) {
        File dir = new File(CampaignResultPageList.PATH_CAMPAIGN_DIR + campaignId);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = new File(dir, children[i]).delete();
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

}