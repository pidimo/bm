package com.piramide.elwis.cmd.contactmanager.copycatalog;

import com.jatun.commons.email.parser.HtmlEmailDOMParser;
import com.jatun.commons.email.parser.HtmlEmailParser;
import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.catalogmanager.TemplateTextImgCmd;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.domain.webmailmanager.ImageStore;
import com.piramide.elwis.domain.webmailmanager.ImageStoreHome;
import com.piramide.elwis.dto.catalogmanager.TemplateDTO;
import com.piramide.elwis.dto.catalogmanager.TemplateTextDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.htmlfilter.ImageStoreByImgTagFilter;
import com.piramide.elwis.utils.htmlfilter.ImageStoreUpdateIdImgTagFilter;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class CopyContactMediaEditorAndTemplateCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");

        Map<Integer, Integer> templateIdentifierMap = createTemplates(source, target, sessionContext);
    }


    /**
     * This method create templates for target company
     *
     * @param sourceCompany source company to copy templates
     * @param targetCompany target company for create templates
     * @return <code>Map</code> Object can contain Template identifier mapping
     */
    private Map<Integer, Integer> createTemplates(Company sourceCompany,
                                                  Company targetCompany,
                                                  SessionContext ctx) {
        TemplateHome templateHome =
                (TemplateHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TEMPLATE);

        Collection sourceTemplates = null;
        try {
            sourceTemplates = templateHome.findByCompanyId(sourceCompany.getCompanyId());
        } catch (FinderException e) {
            log.debug("Cannot read source Templates with " + sourceCompany.getCompanyId());
        }

        Map<Integer, Integer> templateIdentifierMap = new HashMap<Integer, Integer>();
        if (null != sourceTemplates) {
            for (Object obj : sourceTemplates) {
                Template sourceTemplate = (Template) obj;
                Template targetTemplate = null;
                try {
                    targetTemplate = createTargetTemplate(sourceTemplate, targetCompany);
                } catch (CreateException e) {
                    log.debug("Cannot create target template for source Template " + sourceTemplate.getTemplateId());
                    continue;
                }

                templateIdentifierMap.put(sourceTemplate.getTemplateId(), targetTemplate.getTemplateId());

                List<Map<String, Object>> structure = readSourceTemplateText(sourceTemplate);
                for (Map<String, Object> element : structure) {
                    try {
                        createTargetTemplateText(element, targetTemplate, targetCompany, ctx);
                    } catch (FinderException e) {
                        log.debug("Cannot create template text ", e);
                    } catch (CreateException e) {
                        log.debug("Cannot create template text ", e);
                    }
                }
            }
        }

        return templateIdentifierMap;
    }

    /**
     * This method creates <code>TemplateTex</code> object.
     *
     * @param element          <code>Map</code> object that contain source <code>TemplateText</code> information.
     * @param targetTemplate   target template
     * @param targetCompany    target company.
     * @throws FinderException when cannot search language.
     * @throws CreateException when cannot create <code>TemplateText</code> object.
     */
    private void createTargetTemplateText(Map<String, Object> element,
                                          Template targetTemplate,
                                          Company targetCompany,
                                          SessionContext ctx) throws FinderException, CreateException {
        String languageName = (String) element.get("languageName");
        Boolean byDefault = (Boolean) element.get("byDefault");
        byte[] file = (byte[]) element.get("freetext.value");
        Integer type = (Integer) element.get("freetext.type");

        //search target language
        Integer targetLanguageId = getLanguageId(languageName, targetCompany.getCompanyId());

        //update images only if is html template
        if (CatalogConstants.MediaType.HTML.equal(targetTemplate.getMediaType())) {
            file = updateImagesToHtmlTemplate(file, targetTemplate);
        }

        //create freetext object
        Integer targetfreeTextId = createFreeText(file, targetCompany.getCompanyId(), type);

        TemplateTextHome templateTextHome =
                (TemplateTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TEMPLATETEXT);

        TemplateTextDTO dto = new TemplateTextDTO();
        dto.put("byDefault", byDefault);
        dto.put("companyId", targetCompany.getCompanyId());
        dto.put("freeTextId", targetfreeTextId);
        dto.put("languageId", targetLanguageId);
        dto.put("templateId", targetTemplate.getTemplateId());

        TemplateText templateText = templateTextHome.create(dto);

        //create relation between TemplateText and ImageStore, only if is html temnplate
        if (CatalogConstants.MediaType.HTML.equal(targetTemplate.getMediaType())) {
            createTemplateTextImg(templateText, new String(file), ctx);
        }
    }


    /**
     * This method Creates target template
     *
     * @param sourceTemplate source template to copy values
     * @param targetCompany  target company to create template
     * @return TargetTemplate object
     * @throws CreateException when cannot create template
     */
    private Template createTargetTemplate(Template sourceTemplate,
                                          Company targetCompany) throws CreateException {

        TemplateDTO dto = new TemplateDTO();
        DTOFactory.i.copyToDTO(sourceTemplate, dto);
        dto.put("templateId", null);
        dto.put("companyId", targetCompany.getCompanyId());

        TemplateHome templateHome =
                (TemplateHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TEMPLATE);

        return templateHome.create(dto);
    }


    private List<Map<String, Object>> readSourceTemplateText(Template template) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        Collection templateTexts = template.getTemplateText();

        for (Object obj : templateTexts) {
            Map<String, Object> structure = new HashMap<String, Object>();

            TemplateText templateText = (TemplateText) obj;
            String languageName;
            try {
                languageName = getLanguageName(templateText.getLanguageId());
            } catch (FinderException e) {
                log.debug("Cannot read language for templatetex with templateId: " + templateText.getTemplateId() +
                        " and languageId: " + templateText.getLanguageId());
                continue;
            }

            structure.put("languageName", languageName);
            structure.put("byDefault", templateText.getByDefault());
            structure.put("freetext.value", templateText.getFreeText().getValue());
            structure.put("freetext.type", templateText.getFreeText().getType());

            result.add(structure);
        }
        return result;
    }

    private Integer createFreeText(byte[] file, Integer companyId, Integer type) throws CreateException {
        FreeTextHome freeTextHome =
                (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);

        FreeText freeText = freeTextHome.create(file, companyId, type);
        return freeText.getFreeTextId();
    }

    private String getLanguageName(Integer languageId) throws FinderException {
        LanguageHome languageHome =
                (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);

        Language language = languageHome.findByPrimaryKey(languageId);

        return language.getLanguageName();

    }

    private Integer getLanguageId(String languageName, Integer companyId) throws FinderException {
        LanguageHome languageHome =
                (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);

        Language language = languageHome.findByLanguageByName(companyId, languageName);
        return language.getLanguageId();
    }

    /**
     * Copy images from source template html body and update your id in ing tags. This is only to templates of
     * type HTML
     * @param htmlBody source template body
     * @param targetTemplate target template
     * @return byte[] target template body
     * @throws CreateException
     */
    private byte[] updateImagesToHtmlTemplate(byte[] htmlBody, Template targetTemplate) throws CreateException {
        byte[] newHtmlBody = htmlBody;

        if (CatalogConstants.MediaType.HTML.equal(targetTemplate.getMediaType())) {
            String sourceHtmlBody = new String(htmlBody);
            List<Integer> sourceImageStoreIdList = filterByImageStoreId(sourceHtmlBody);

            Set<Integer> imageStoreIdSet = new HashSet<Integer>(sourceImageStoreIdList); //delete duplicate ids
            Map<Integer, Integer> sourceTargetImageStoreMap = new HashMap<Integer, Integer>();

            //create image stores
            for (Integer sourceImageStoreId : imageStoreIdSet) {
                ImageStore sourceImageStore = readImageStore(sourceImageStoreId);
                if (sourceImageStore != null) {
                    ImageStore targetImageStore = createImageStore(sourceImageStore, targetTemplate.getCompanyId());
                    sourceTargetImageStoreMap.put(sourceImageStore.getImageStoreId(), targetImageStore.getImageStoreId());
                }
            }

            if (!sourceTargetImageStoreMap.isEmpty()) {
                newHtmlBody = updateImgTagIdAttributes(sourceHtmlBody, sourceTargetImageStoreMap).getBytes();
            }
        }
        return newHtmlBody;
    }

    private ImageStore readImageStore(Integer imageStoreId) {
        ImageStore imageStore = null;
        ImageStoreHome imageStoreHome = (ImageStoreHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_IMAGESTORE);
        try {
            imageStore = imageStoreHome.findByPrimaryKey(imageStoreId);
        } catch (FinderException e) {
            log.debug("Not found image store...." + imageStoreId);
        }
        return imageStore;
    }

    private ImageStore createImageStore(ImageStore sourceImageStore, Integer targetCompanyId) throws CreateException {
        ImageStoreHome imageStoreHome = (ImageStoreHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_IMAGESTORE);
        ImageStore imageStore = imageStoreHome.create(sourceImageStore.getFileData(), sourceImageStore.getFileName(), targetCompanyId, sourceImageStore.getSessionId(), sourceImageStore.getImageType());
        return imageStore;
    }

    /**
     * Get image store ids as list from body
     * @param templateBody body of template
     * @return List
     */
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

    /**
     * Update image store ids in body of source template with ids of new image stores
     * @param sourceTemplateBody  source template body
     * @param sourceTargetImageStoreIdMap map with old and new image store id
     * @return String that is the new target template body
     */
    private String updateImgTagIdAttributes(String sourceTemplateBody, Map<Integer, Integer> sourceTargetImageStoreIdMap) {
        String targetTemplateBody;

        //filter html body by store image id
        ImageStoreUpdateIdImgTagFilter updateIdImgTagFilter = new ImageStoreUpdateIdImgTagFilter(sourceTargetImageStoreIdMap);
        HtmlEmailParser parser = new HtmlEmailDOMParser();
        parser.addFilter(updateIdImgTagFilter);
        try {
            parser.parseHtml(sourceTemplateBody);
            targetTemplateBody = parser.getHtml().toString();
        } catch (Exception e) {
            log.debug("filter store image IMG tags FAIL");
            targetTemplateBody = sourceTemplateBody;
        }

        return targetTemplateBody;
    }

    /**
     * Create relations between TemplateText and ImageStore
     *
     * @param templateText
     * @param templateBody
     * @throws CreateException
     */
    private void createTemplateTextImg(TemplateText templateText, String templateBody, SessionContext ctx) throws CreateException {

        TemplateTextImgCmd templateTextImgCmd = new TemplateTextImgCmd();
        templateTextImgCmd.putParam("op", "createFromTemplate");
        templateTextImgCmd.putParam("companyId", templateText.getCompanyId());
        templateTextImgCmd.putParam("templateId", templateText.getTemplateId());
        templateTextImgCmd.putParam("languageId", templateText.getLanguageId());
        templateTextImgCmd.putParam("templateBody", templateBody);

        templateTextImgCmd.executeInStateless(ctx);

        if (templateTextImgCmd.getResultDTO().isFailure()) {
            throw new CreateException("Error in create relation to image store..");
        }
    }
}
