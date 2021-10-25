package com.piramide.elwis.cmd.webmailmanager;

import com.jatun.commons.email.parser.HtmlEmailDOMParser;
import com.jatun.commons.email.parser.HtmlEmailParser;
import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.dto.webmailmanager.SignatureImageDTO;
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
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.3.6
 */
public class SignatureImageCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        if ("manageImages".equals(getOp())) {
            String htmlSignature = (String) paramDTO.get("htmlSignature");
            Integer signatureId = EJBCommandUtil.i.getValueAsInteger(this, "signatureId");
            manageImages(signatureId, htmlSignature, ctx);
        }
    }

    @Override
    public boolean isStateful() {
        return false;
    }

    private void manageImages(Integer signatureId, String htmlSignature, SessionContext ctx) {
        log.debug("Managing images for signature: " + signatureId);

        Signature signature = getSignature(signatureId);
        if (null != signature) {
            List<Integer> imageIds = new ArrayList<Integer>();
            if (!"".equals(htmlSignature.trim())) {
                imageIds = filterByImageStoreId(htmlSignature);
            }

            manageImages(imageIds, signatureId, signature.getCompanyId(), ctx);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void manageImages(List<Integer> imageIds, Integer signatureId, Integer companyId, SessionContext ctx) {
        List<SignatureImage> currentImages = getImages(signatureId, companyId);

        List<Integer> storedImages = new ArrayList<Integer>();
        List<SignatureImage> imagesToDelete = new ArrayList<SignatureImage>();

        for (SignatureImage image : currentImages) {
            if (!imageIds.contains(image.getImageStoreId())) {
                imagesToDelete.add(image);
            } else {
                storedImages.add(image.getImageStoreId());
            }
        }

        for (Integer imageId : imageIds) {
            if (!storedImages.contains(imageId)) {
                createImage(imageId, signatureId, companyId);
            }
        }

        for (SignatureImage image : imagesToDelete) {
            try {
                Integer imageStoreId = image.getImageStoreId();
                image.remove();

                removeImage(imageStoreId, ctx);
            } catch (RemoveException e) {
                log.error("Cannot remove the image relation ", e);
                resultDTO.setResultAsFailure();
            }
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void createImage(Integer imageStoreId, Integer signatureId, Integer companyId) {
        log.debug("Relating the image: " + imageStoreId + " with signature: " + signatureId);

        SignatureImageDTO dto = new SignatureImageDTO();
        dto.put("imageStoreId", imageStoreId);
        dto.put("signatureId", signatureId);
        dto.put("companyId", companyId);

        ExtendedCRUDDirector.i.create(dto, resultDTO, false);
        updateImageType(imageStoreId);
    }

    private void removeImage(Integer imageStoreId, SessionContext ctx) {
        log.debug("Removing the image: " + imageStoreId);

        ImageStoreCmd imageStoreCmd = new ImageStoreCmd();
        imageStoreCmd.putParam("op", "delete");
        imageStoreCmd.putParam("imageStoreId", imageStoreId);

        imageStoreCmd.executeInStateless(ctx);
    }

    private void updateImageType(Integer storeImageId) {
        ImageStoreHome imageStoreHome =
                (ImageStoreHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_IMAGESTORE);
        try {
            ImageStore imageStore = imageStoreHome.findByPrimaryKey(storeImageId);
            imageStore.setImageType(WebMailConstants.ImageStoreType.RELATION_IMAGE.getConstant());
        } catch (FinderException e) {
            log.error("Cannot establish the relation between signature and image because the image cannot be found ", e);
            resultDTO.setResultAsFailure();
        }
    }

    @SuppressWarnings(value = "unchecked")
    private List<SignatureImage> getImages(Integer signatureId, Integer companyId) {
        SignatureImageHome signatureImageHome =
                (SignatureImageHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_SIGNATURE_IMAGE);

        try {
            return (List<SignatureImage>) signatureImageHome.findBySignatureId(signatureId, companyId);
        } catch (FinderException e) {
            log.debug("Cannot find Images for signature: " + signatureId);
        }

        return new ArrayList<SignatureImage>();
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

    private Signature getSignature(Integer signatureId) {
        SignatureHome signatureHome = (SignatureHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_SIGNATURE);
        try {
            return signatureHome.findByPrimaryKey(signatureId);
        } catch (FinderException e) {
            return null;
        }
    }
}
