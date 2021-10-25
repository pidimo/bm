package com.piramide.elwis.cmd.campaignmanager;

import com.jatun.commons.email.parser.HtmlEmailDOMParser;
import com.jatun.commons.email.parser.HtmlEmailParser;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.domain.catalogmanager.LanguageHome;
import com.piramide.elwis.domain.webmailmanager.ImageStore;
import com.piramide.elwis.domain.webmailmanager.ImageStoreHome;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.htmlfilter.ImageStoreByImgTagFilter;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Cmd util to manage size of template and campaig text
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.5
 */
public class CampaignTemplateUtilCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        log.debug("Executing CampaignTemplateUtilCmd........." + paramDTO);

        if ("setCampaignTextSize".equals(getOp())) {
            setCampaignTextSize();
        }
        if ("readTemplateBodySize".equals(getOp())) {
            readTemplateBodySize();
        }
        if ("readGeneratedBodySize".equals(getOp())) {
            readGeneratedTemplateBodySize(sessionContext);
        }
    }

    private void setCampaignTextSize() {
        Integer templateId = new Integer(paramDTO.get("templateId").toString());
        Integer languageId = new Integer(paramDTO.get("languageId").toString());

        CampaignText campaignText = findCampaignText(templateId, languageId);

        if (campaignText != null) {
            calculateAndSetCampaignTextSize(campaignText);
        }
    }

    private void readTemplateBodySize() {
        Integer templateId = new Integer(paramDTO.get("templateId").toString());
        String attachIds = paramDTO.get("attachIds").toString();

        List<Map> templateInfoList = new ArrayList<Map>();

        CampaignTemplate campaignTemplate = findCampaignTemplate(templateId);

        if (campaignTemplate != null) {
            Integer attachSize = calculateAllAttachSize(attachIds);

            LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
            for (Iterator iterator = campaignTemplate.getCampaignText().iterator(); iterator.hasNext();) {
                CampaignText campaignText = (CampaignText) iterator.next();

                Language language;
                try {
                    language = languageHome.findByPrimaryKey(campaignText.getLanguageId());
                } catch (FinderException e) {
                    log.debug("language not found...", e);
                    continue;
                }

                Integer campaignTextSize = campaignText.getSize();
                if (campaignTextSize == null) {
                    campaignTextSize = calculateAndSetCampaignTextSize(campaignText);
                }

                Map map = new HashMap();
                map.put("languageIso", language.getLanguageIso());
                map.put("languageName", language.getLanguageName());
                map.put("templateSize", (campaignTextSize + attachSize));

                templateInfoList.add(map);
            }
        }

        resultDTO.put("templateSizeInfoList", templateInfoList);
    }


    private void readGeneratedTemplateBodySize(SessionContext ctx) {
        Integer generationId = new Integer(paramDTO.get("generationId").toString());
        List<Map> templateInfoList = new ArrayList<Map>();

        CampaignGenerationCmd generationCmd = new CampaignGenerationCmd();
        generationCmd.putParam("generationId", generationId);
        generationCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = generationCmd.getResultDTO();

        if (myResultDTO != null && !myResultDTO.isFailure()) {
            //process attach if exist
            Integer attachmentsSize = 0;
            if (myResultDTO.containsKey("genAttachmentList")) {
                List<Map> attachmentList = (List<Map>) myResultDTO.get("genAttachmentList");
                for (Map attachmentMap : attachmentList) {
                    if (attachmentMap.get("fileSize") != null) {
                        attachmentsSize = attachmentsSize + new Integer(attachmentMap.get("fileSize").toString());
                    }
                }
            }

            Integer templateId = new Integer(myResultDTO.get("templateId").toString());
            CampaignTemplate campaignTemplate = findCampaignTemplate(templateId);

            if (campaignTemplate != null) {
                LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
                for (Iterator iterator = campaignTemplate.getCampaignText().iterator(); iterator.hasNext();) {
                    CampaignText campaignText = (CampaignText) iterator.next();

                    Language language;
                    try {
                        language = languageHome.findByPrimaryKey(campaignText.getLanguageId());
                    } catch (FinderException e) {
                        log.debug("language not found...", e);
                        continue;
                    }

                    Integer campaignTextSize = campaignText.getSize();
                    if (campaignTextSize == null) {
                        campaignTextSize = calculateAndSetCampaignTextSize(campaignText);
                    }

                    Map map = new HashMap();
                    map.put("languageIso", language.getLanguageIso());
                    map.put("languageName", language.getLanguageName());
                    map.put("templateSize", (campaignTextSize + attachmentsSize));

                    templateInfoList.add(map);
                }
            }
        }

        resultDTO.put("templateSizeInfoList", templateInfoList);
    }

    private Integer calculateAndSetCampaignTextSize(CampaignText campaignText) {
        Integer size = 0;
        CampaignFreeText freeText = campaignText.getCampaignFreeText();
        if (freeText != null && freeText.getValue() != null) {
            size = freeText.getValue().length;

            CampaignTemplate campaignTemplate = findCampaignTemplate(campaignText.getTemplateId());

            //read image size
            if (campaignTemplate != null && CampaignConstants.DocumentType.HTML.equal(campaignTemplate.getDocumentType())) {

                String campaignTextBody = new String(freeText.getValue());
                List<Integer> imageStoreIdList = filterByImageStoreId(campaignTextBody);

                for (Integer bodyImageStoreId : imageStoreIdList) {
                    ImageStore imageStore = findImageStore(bodyImageStoreId);
                    if (imageStore != null) {
                        size = size + imageStore.getFileData().length;
                    }
                }
            }

            //Set the size
            campaignText.setSize(size);
        }

        return size;
    }

    private Integer calculateAllAttachSize(String attachIds) {
        Integer attachSize = 0;
        if (attachIds != null) {
            String[] ids = attachIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                try {
                    Integer attachId = Integer.valueOf(ids[i]);
                    Attach attach = findAttach(attachId);
                    if (attach != null && attach.getSize() != null) {
                        attachSize = attachSize + attach.getSize();
                    }
                } catch (NumberFormatException ignore) {
                }
            }
        }
        return attachSize;
    }

    private CampaignText findCampaignText(Integer templateId, Integer languageId) {
        CampaignTextHome campaignTextHome = (CampaignTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEXT);

        if (templateId != null && languageId != null) {
            CampaignTextPK pk = new CampaignTextPK(languageId, templateId);
            try {
                return campaignTextHome.findByPrimaryKey(pk);
            } catch (FinderException e) {
                log.debug("Not found CampaignText with: templateId=" + templateId + " languageId=" + languageId, e);
            }
        }
        return null;
    }

    private CampaignTemplate findCampaignTemplate(Integer templateId) {
        CampaignTemplateHome campaignTemplateHome = (CampaignTemplateHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEMPLATE);

        if (templateId != null) {
            try {
                return campaignTemplateHome.findByPrimaryKey(templateId);
            } catch (FinderException e) {
                log.debug("Not found campaign templateId :" + templateId, e);
            }
        }
        return null;
    }

    private Attach findAttach(Integer attachId) {
        AttachHome attachHome  = (AttachHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_ATTACH);
        if (attachId != null) {
            try {
                return attachHome.findByPrimaryKey(attachId);
            } catch (FinderException e) {
                log.debug("Not found campaign Attach id:" + attachId, e);
            }
        }
        return null;
    }

    private ImageStore findImageStore(Integer imageStoreId) {
        ImageStore imageStore = null;

        if (imageStoreId != null) {
            ImageStoreHome imageStoreHome = (ImageStoreHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_IMAGESTORE);
            try {
                imageStore = imageStoreHome.findByPrimaryKey(imageStoreId);
            } catch (FinderException e) {
                log.debug("Not found image store...." + imageStoreId);
            }
        }
        return imageStore;
    }

    private List<Integer> filterByImageStoreId(String templateBody) {
        List<Integer> imageStoreIdList = new ArrayList<Integer>();

        //filter html body by store image id
        ImageStoreByImgTagFilter imgTagFilter = new ImageStoreByImgTagFilter();
        HtmlEmailParser parser = new HtmlEmailDOMParser();
        parser.addFilter(imgTagFilter);
        try {
            parser.parseHtml(templateBody);
            imageStoreIdList = imgTagFilter.getImageStoreIdList();
        } catch (Exception e) {
            log.debug("filter store image IMG tags FAIL");
        }

        return imageStoreIdList;
    }

    public boolean isStateful() {
        return false;
    }
}
