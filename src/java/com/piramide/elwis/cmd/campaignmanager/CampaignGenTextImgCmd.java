package com.piramide.elwis.cmd.campaignmanager;

import com.jatun.commons.email.parser.HtmlEmailDOMParser;
import com.jatun.commons.email.parser.HtmlEmailParser;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.webmailmanager.ImageStoreCmd;
import com.piramide.elwis.domain.campaignmanager.CampaignGenTextImg;
import com.piramide.elwis.domain.campaignmanager.CampaignGenTextImgHome;
import com.piramide.elwis.domain.webmailmanager.ImageStore;
import com.piramide.elwis.domain.webmailmanager.ImageStoreHome;
import com.piramide.elwis.dto.campaignmanager.CampaignGenTextImgDTO;
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
 * @version $Id: CampaignGenTextImgCmd.java 2009-06-26 06:26:20 PM $
 */
public class CampaignGenTextImgCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CampaignGenTextImgCmd....." + paramDTO);

        if ("createFromTemplate".equals(getOp())) {
            bulkCreate();
        }

        if ("deleteFromTemplate".equals(getOp())) {
            bulkDelete(ctx);
        }

    }

    private void bulkCreate() {
        String campaignTextBody = (String) paramDTO.get("campaignTextBody");

        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer campaignGenTextId = new Integer(paramDTO.get("campaignGenTextId").toString());

        List<Integer> imageStoreIdList = filterByImageStoreId(campaignTextBody);

        if (!imageStoreIdList.isEmpty()) {
            bulkCreate(imageStoreIdList, companyId, campaignGenTextId);
        }
    }


    private void bulkCreate(List<Integer> imageStoreIdList, Integer companyId, Integer campaignGenTextId) {
        log.debug("Create CampaignGenTextImg with store image: " + imageStoreIdList);

        ImageStoreHome imageStoreHome = (ImageStoreHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_IMAGESTORE);
        for (Integer imageStoreId : imageStoreIdList) {

            CampaignGenTextImgDTO textImgDTO = new CampaignGenTextImgDTO();
            textImgDTO.put("companyId", companyId);
            textImgDTO.put("campaignGenTextId", campaignGenTextId);
            textImgDTO.put("imageStoreId", imageStoreId);

            CampaignGenTextImg campaignGenTextImg = (CampaignGenTextImg) ExtendedCRUDDirector.i.create(textImgDTO, resultDTO, false);

            //update image store type, set as relation
            if (campaignGenTextImg != null) {
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
        Integer campaignGenTextId = new Integer(paramDTO.get("campaignGenTextId").toString());

        CampaignGenTextImgHome campaignGenTextImgHome = (CampaignGenTextImgHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNGENTEXTIMG);

        Collection campaignGenTextImgs = null;
        try {
            campaignGenTextImgs = campaignGenTextImgHome.findByCampaignGenTextId(campaignGenTextId);
        } catch (FinderException e) {
            campaignGenTextImgs = new ArrayList();
        }

        for (Iterator iterator = campaignGenTextImgs.iterator(); iterator.hasNext();) {
            CampaignGenTextImg campaignGenTextImg = (CampaignGenTextImg) iterator.next();

            Integer imageStoreId = campaignGenTextImg.getImageStoreId();
            try {
                //remove relation
                campaignGenTextImg.remove();

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

