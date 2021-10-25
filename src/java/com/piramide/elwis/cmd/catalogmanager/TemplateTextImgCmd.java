package com.piramide.elwis.cmd.catalogmanager;

import com.jatun.commons.email.parser.HtmlEmailDOMParser;
import com.jatun.commons.email.parser.HtmlEmailParser;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.webmailmanager.ImageStoreCmd;
import com.piramide.elwis.domain.catalogmanager.TemplateTextImg;
import com.piramide.elwis.domain.catalogmanager.TemplateTextImgHome;
import com.piramide.elwis.domain.webmailmanager.ImageStore;
import com.piramide.elwis.domain.webmailmanager.ImageStoreHome;
import com.piramide.elwis.dto.catalogmanager.TemplateTextImgDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.htmlfilter.ImageStoreByImgTagFilter;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Miky
 * @version $Id: TemplateTextImgCmd.java 2009-06-19 07:24:06 PM $
 */
public class TemplateTextImgCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing TemplateTextImgCmd....." + paramDTO);

        if ("createFromTemplate".equals(getOp())) {
            bulkCreate();
        }

        if ("updateFromTemplate".equals(getOp())) {
            update(ctx);
        }

        if ("deleteFromTemplate".equals(getOp())) {
            bulkDelete(ctx);
        }

    }

    private void bulkCreate() {
        String templateTextBody = (String) paramDTO.get("templateBody");

        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer languageId = new Integer(paramDTO.get("languageId").toString());
        Integer templateId = new Integer(paramDTO.get("templateId").toString());

        List<Integer> imageStoreIdList = filterByImageStoreId(templateTextBody);

        if (!imageStoreIdList.isEmpty()) {
            bulkCreate(imageStoreIdList, companyId, languageId, templateId);
        }
    }

    private void update(SessionContext ctx) {
        String templateTextBody = (String) paramDTO.get("templateBody");

        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer languageId = new Integer(paramDTO.get("languageId").toString());
        Integer templateId = new Integer(paramDTO.get("templateId").toString());

        List<Integer> imageStoreIdList = filterByImageStoreId(templateTextBody);

        TemplateTextImgHome templateTextImgHome = (TemplateTextImgHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TEMPLATETEXTIMG);
        Collection templateTextImgs = null;
        try {
            templateTextImgs = templateTextImgHome.findByTemplateIdLanguageId(templateId, languageId);
        } catch (FinderException e) {
            templateTextImgs = new ArrayList();
        }

        //clean already related imagestore
        for (Iterator iterator = templateTextImgs.iterator(); iterator.hasNext();) {
            TemplateTextImg templateTextImg = (TemplateTextImg) iterator.next();
            Integer imageStoreId = templateTextImg.getImageStoreId();
            boolean existInBody = false;

            for (int i = 0; i < imageStoreIdList.size(); i++) {
                Integer bodyImageStoreId = imageStoreIdList.get(i);
                if (bodyImageStoreId.equals(imageStoreId)) {
                    imageStoreIdList.remove(i);
                    existInBody = true;
                    break;
                }
            }

            if (!existInBody) {
                try {
                    //remove this
                    templateTextImg.remove();

                    //remove image store
                    ImageStoreCmd imageStoreCmd = new ImageStoreCmd();
                    imageStoreCmd.putParam("op", "delete");
                    imageStoreCmd.putParam("imageStoreId", imageStoreId);
                    imageStoreCmd.executeInStateless(ctx);
                } catch (RemoveException e) {
                    log.debug("Error in remove TemplateTextImg...", e);
                }
            }
        }

        if (!imageStoreIdList.isEmpty()) {
            bulkCreate(imageStoreIdList, companyId, languageId, templateId);
        }
    }

    private void bulkCreate(List<Integer> imageStoreIdList, Integer companyId, Integer languageId, Integer templateId) {
        log.debug("Create TemplateTextImg with store image: " + imageStoreIdList);

        ImageStoreHome imageStoreHome = (ImageStoreHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_IMAGESTORE);
        for (Integer imageStoreId : imageStoreIdList) {
            TemplateTextImgDTO textImgDTO = new TemplateTextImgDTO();
            textImgDTO.put("companyId", companyId);
            textImgDTO.put("languageId", languageId);
            textImgDTO.put("templateId", templateId);
            textImgDTO.put("imageStoreId", imageStoreId);

            TemplateTextImg templateTextImg = (TemplateTextImg) ExtendedCRUDDirector.i.create(textImgDTO, resultDTO, false);

            //update image store type, set as relation
            if (templateTextImg != null) {
                try {
                    ImageStore imageStore = imageStoreHome.findByPrimaryKey(imageStoreId);
                    imageStore.setImageType(WebMailConstants.ImageStoreType.RELATION_IMAGE.getConstant());
                } catch (FinderException e) {
                    log.debug("Not found image store... " + imageStoreId);
                }
            }
        }
    }

    private void bulkDelete(SessionContext ctx) {
        Integer languageId = new Integer(paramDTO.get("languageId").toString());
        Integer templateId = new Integer(paramDTO.get("templateId").toString());

        TemplateTextImgHome templateTextImgHome = (TemplateTextImgHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TEMPLATETEXTIMG);

        Collection templateTextImgs = null;
        try {
            templateTextImgs = templateTextImgHome.findByTemplateIdLanguageId(templateId, languageId);
        } catch (FinderException e) {
            templateTextImgs = new ArrayList();
        }

        for (Iterator iterator = templateTextImgs.iterator(); iterator.hasNext();) {
            TemplateTextImg templateTextImg = (TemplateTextImg) iterator.next();

            Integer imageStoreId = templateTextImg.getImageStoreId();
            try {
                //remove relation
                templateTextImg.remove();

                //remove image store
                ImageStoreCmd imageStoreCmd = new ImageStoreCmd();
                imageStoreCmd.putParam("op", "delete");
                imageStoreCmd.putParam("imageStoreId", imageStoreId);
                imageStoreCmd.executeInStateless(ctx);
            } catch (RemoveException e) {
                log.debug("Error in delete.....");
                resultDTO.setResultAsFailure();
                return;
            }
        }
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
}

