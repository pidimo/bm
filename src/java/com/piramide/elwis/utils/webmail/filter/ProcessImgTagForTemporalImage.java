package com.piramide.elwis.utils.webmail.filter;

import com.piramide.elwis.cmd.webmailmanager.ImageStoreCmd;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.cyberneko.html.filters.DefaultFilter;

import javax.ejb.SessionContext;

/**
 * filter to process mail temporal images from img tags
 *
 * @author Miky
 * @version $Id: ProcessImgTagForTemporalImage.java 2009-05-26 02:16:59 PM $
 */
public class ProcessImgTagForTemporalImage extends DefaultFilter {
    private Log log = LogFactory.getLog(this.getClass());

    private Integer mailId;
    private SessionContext ctx;
    private Integer bytesAttachSize;


    Boolean hasChanges = false;

    public ProcessImgTagForTemporalImage(SessionContext ctx, Integer mailId) {
        this.ctx = ctx;
        this.mailId = mailId;
        bytesAttachSize = 0;
    }

    public Boolean getHasChanges() {
        return hasChanges;
    }

    public Integer getBytesAttachSize() {
        return bytesAttachSize;
    }

    @Override
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {

        if ("img".equalsIgnoreCase(element.rawname)) {
            Integer idIndex = null;
            Integer srcIndex = null;

            Integer imageStoreId = null;

            for (int i = 0; i < attributes.getLength(); i++) {
                if ("id".equalsIgnoreCase(attributes.getLocalName(i))) {
                    String value = attributes.getValue(i);
                    if (value.contains(WebMailConstants.TEMPORALIMAGESTOREID_KEY)) {
                        idIndex = i;
                        imageStoreId = getTemporalImageId(value);
                    }
                }
                if ("src".equalsIgnoreCase(attributes.getLocalName(i))) {
                    srcIndex = i;
                }
            }

            if (null != idIndex && null != srcIndex) {
                createMailAttachForTemporalImage(idIndex, srcIndex, attributes, imageStoreId);
                hasChanges = true;
            }
        }
        super.emptyElement(element, attributes, augs);
    }

    /**
     * get imageStoreId from img tag id attribute. This is ie:  temporalImageStoreId=15
     *
     * @param idAttributeValue attribute id value
     * @return integer
     */
    private Integer getTemporalImageId(String idAttributeValue) {
        log.debug("process temporal image for.......:" + idAttributeValue);
        Integer imageStoreId = null;

        int index = idAttributeValue.indexOf("=");
        if (index != -1) {
            try {
                imageStoreId = new Integer(idAttributeValue.substring(index + 1).trim());
            } catch (NumberFormatException e) {
                log.debug("Error in parser temporal image Id:" + idAttributeValue, e);
            }
        }
        return imageStoreId;
    }

    /**
     * parser temporal image img tag, create mail attach for this.
     *
     * @param idIndex
     * @param srcIndex
     * @param attributes
     * @param imageStoreId
     */
    private void createMailAttachForTemporalImage(Integer idIndex, Integer srcIndex, XMLAttributes attributes, Integer imageStoreId) {
        String attachId = null;

        if (imageStoreId != null) {
            ImageStoreCmd imageStoreCmd = new ImageStoreCmd();
            imageStoreCmd.putParam("op", "createMailAttach");
            imageStoreCmd.putParam("imageStoreId", imageStoreId);
            imageStoreCmd.putParam("mailId", mailId);

            imageStoreCmd.executeInStateless(ctx);
            ResultDTO resultDTO = imageStoreCmd.getResultDTO();
            if (!resultDTO.isFailure()) {
                attachId = resultDTO.get("attachId").toString();
                bytesAttachSize += new Integer(resultDTO.get("attachSize").toString());
            }
        }

        attributes.setValue(idIndex, (attachId != null) ? WebMailConstants.attachIdKey + "=" + attachId : "");
        attributes.setValue(srcIndex, (attachId != null) ? "cid:" + attachId : "");
    }


}
