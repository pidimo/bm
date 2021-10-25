package com.piramide.elwis.cmd.utils;

import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Feb 10, 2005
 * Time: 5:22:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class CampaignTemplateManager {
    private final Log log = LogFactory.getLog(this.getClass());

    public static final String FIELD_BLOCK = "[FIELD BLOCK]";
    public static final String TAIL_BLOCK = "[TAIL]";
    private HTML2ElwisTemplateData templateData;

    //public static final CampaignTemplateManager i = new CampaignTemplateManager();

    public CampaignTemplateManager(boolean isEmail) {
        templateData = new HTML2ElwisTemplateData();
    }

    public Map loadImageCache(Integer templateId, Integer campaignId) {
        Map images = new HashMap();
        try {
            if (!isCachedImage(templateId, campaignId)) {
                //templateData.initializeImagesCache(campaignId);
                images = templateData.getImages();
            } else {
                BufferedReader in = new BufferedReader(new FileReader(CampaignResultPageList.pathCampaignImagesCacheFile(templateId, campaignId)));
                String str;
                images = new HashMap();
                while ((str = in.readLine()) != null) {
                    String[] value = str.split("=");
                    images.put(value[0], value[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return images;
    }

    public List loadTemplateCache(Integer templateId, Integer campaignId, Integer languageId, String[] fieldNames) {
        List fields = new ArrayList();
        try {
            if (!isCachedTemplate(templateId, campaignId, languageId)) {
                templateData.initializeHtmlCache(templateId, campaignId, languageId, fieldNames);
                fields = templateData.getFields();
            } else {
                fields = new ArrayList();
                BufferedReader in = new BufferedReader(new FileReader(CampaignResultPageList.pathCampaignTemplateCacheFile(templateId, campaignId, languageId)));
                String str;
                boolean first = false;
                boolean isBody = false;
                boolean isTail = false;
                StringBuffer body = new StringBuffer();
                StringBuffer tail = new StringBuffer();
                String field = null;
                while ((str = in.readLine()) != null) {
                    if (TAIL_BLOCK.equals(str.trim())) {
                        first = false;
                        if (isBody) {
                            fields.add(new DefaultKeyValue(field, body.toString()));
                        }
                        isBody = false;
                        isTail = true;
                    } else if (FIELD_BLOCK.equals(str)) {
                        if (isBody) {
                            fields.add(new DefaultKeyValue(field, body.toString()));
                            body = new StringBuffer();
                            isBody = false;
                        }
                        first = true;
                    } else if (isBody) {
                        body.append(str);
                    } else if (first) {
                        field = str;
                        first = false;
                        isBody = true;
                    } else if (isTail) {
                        tail.append(str);
                    }
                }
                fields.add(tail.toString());
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.debug(fields);
        return fields;
    }

    /**
     * Verify if exist a template cache file for campaign and languageid
     *
     * @param campaignId
     * @param languageId
     * @return true if exist
     */
    public boolean isCachedTemplate(Integer templateId, Integer campaignId, Integer languageId) {
        return new File(CampaignResultPageList.pathCampaignTemplateCacheFile(templateId, campaignId, languageId)).exists();
    }

    private boolean isCachedImage(Integer templateId, Integer campaignId) {
        return new File(CampaignResultPageList.pathCampaignImagesCacheFile(templateId, campaignId)).exists();
    }
}
