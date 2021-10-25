package com.piramide.elwis.cmd.campaignmanager;

import com.jatun.commons.email.parser.HtmlEmailDOMParser;
import com.jatun.commons.email.parser.HtmlEmailParser;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.webmailmanager.ImageStoreCmd;
import com.piramide.elwis.domain.campaignmanager.CampaignTextImg;
import com.piramide.elwis.domain.campaignmanager.CampaignTextImgHome;
import com.piramide.elwis.domain.webmailmanager.ImageStore;
import com.piramide.elwis.domain.webmailmanager.ImageStoreHome;
import com.piramide.elwis.dto.campaignmanager.CampaignTextImgDTO;
import com.piramide.elwis.utils.CampaignConstants;
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
 * @version $Id: CampaignTextImgCmd.java 2009-06-23 05:33:18 PM $
 */
public class CampaignTextImgCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CampaignTextImgCmd....." + paramDTO);

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
        String campaignTextBody = (String) paramDTO.get("campaignTextBody");

        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer languageId = new Integer(paramDTO.get("languageId").toString());
        Integer templateId = new Integer(paramDTO.get("templateId").toString());

        List<Integer> imageStoreIdList = filterByImageStoreId(campaignTextBody);

        if (!imageStoreIdList.isEmpty()) {
            bulkCreate(imageStoreIdList, companyId, languageId, templateId);
        }
    }

    private void update(SessionContext ctx) {
        String campaignTextBody = (String) paramDTO.get("campaignTextBody");

        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer languageId = new Integer(paramDTO.get("languageId").toString());
        Integer templateId = new Integer(paramDTO.get("templateId").toString());

        List<Integer> imageStoreIdList = filterByImageStoreId(campaignTextBody);


        CampaignTextImgHome campaignTextImgHome = (CampaignTextImgHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNTEXTIMG);
        Collection templateTextImgs = null;
        try {
            templateTextImgs = campaignTextImgHome.findByTemplateIdLanguageId(templateId, languageId);
        } catch (FinderException e) {
            templateTextImgs = new ArrayList();
        }

        //clean already related imagestore
        for (Iterator iterator = templateTextImgs.iterator(); iterator.hasNext();) {

            CampaignTextImg campaignTextImg = (CampaignTextImg) iterator.next();
            Integer imageStoreId = campaignTextImg.getImageStoreId();
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
                    campaignTextImg.remove();

                    //remove image store
                    ImageStoreCmd imageStoreCmd = new ImageStoreCmd();
                    imageStoreCmd.putParam("op", "delete");
                    imageStoreCmd.putParam("imageStoreId", imageStoreId);
                    imageStoreCmd.executeInStateless(ctx);
                } catch (RemoveException e) {
                    log.debug("Error in remove....", e);
                }
            }
        }

        if (!imageStoreIdList.isEmpty()) {
            bulkCreate(imageStoreIdList, companyId, languageId, templateId);
        }
    }

    private void bulkCreate(List<Integer> imageStoreIdList, Integer companyId, Integer languageId, Integer templateId) {
        log.debug("Create CampaignTextImg with store image: " + imageStoreIdList);

        ImageStoreHome imageStoreHome = (ImageStoreHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_IMAGESTORE);
        for (Integer imageStoreId : imageStoreIdList) {

            CampaignTextImgDTO textImgDTO = new CampaignTextImgDTO();
            textImgDTO.put("companyId", companyId);
            textImgDTO.put("languageId", languageId);
            textImgDTO.put("templateId", templateId);
            textImgDTO.put("imageStoreId", imageStoreId);

            CampaignTextImg campaignTextImg = (CampaignTextImg) ExtendedCRUDDirector.i.create(textImgDTO, resultDTO, false);

            //update image store type, set as relation
            if (campaignTextImg != null) {
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

        CampaignTextImgHome campaignTextImgHome = (CampaignTextImgHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNTEXTIMG);

        Collection templateTextImgs = null;
        try {
            templateTextImgs = campaignTextImgHome.findByTemplateIdLanguageId(templateId, languageId);
        } catch (FinderException e) {
            templateTextImgs = new ArrayList();
        }

        for (Iterator iterator = templateTextImgs.iterator(); iterator.hasNext();) {
            CampaignTextImg campaignTextImg = (CampaignTextImg) iterator.next();

            Integer imageStoreId = campaignTextImg.getImageStoreId();
            try {
                //remove relation
                campaignTextImg.remove();

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

