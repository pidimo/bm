package com.piramide.elwis.cmd.campaignmanager.util.htmlfilter;

import com.piramide.elwis.cmd.utils.ElwisCacheManager;
import com.piramide.elwis.cmd.webmailmanager.ImageStoreCmd;
import com.piramide.elwis.utils.ArrayByteWrapper;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * parser img tag by temporal image store id key and set "cid", this is required to send email
 *
 * @author Miky
 * @version $Id: CampaignEmailEmbeddedImageFilter.java 2009-06-29 04:48:38 PM $
 */
public class CampaignEmailEmbeddedImageFilter extends DefaultFilter {
    private Log log = LogFactory.getLog(this.getClass());

    private SessionContext ctx;
    private Integer companyId;
    private Integer campaignId;
    private Integer userId;
    private Long generationKey;

    /**
     * Map<image cid, image path>
     */
    private Map<String, String> embeddedImagesCidMap;
    private Boolean hasChanges = false;

    public CampaignEmailEmbeddedImageFilter(Integer companyId, Integer campaignId, Integer userId, Long generationKey, SessionContext ctx) {
        this.ctx = ctx;
        this.companyId = companyId;
        this.campaignId = campaignId;
        this.userId = userId;
        this.generationKey = generationKey;

        this.embeddedImagesCidMap = new HashMap<String, String>();
    }

    public Boolean getHasChanges() {
        return hasChanges;
    }

    public Map<String, String> getEmbeddedImagesCidMap() {
        return embeddedImagesCidMap;
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
                    if (null != idIndex) {
                        srcIndex = i;
                    }
                }
            }

            if (null != idIndex && null != srcIndex) {
                setImageCidInSrcAndSaveInCache(srcIndex, attributes, imageStoreId);
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
     * parser temporal image img tag, set image cid in src an save in the image in cache.
     *
     * @param srcIndex
     * @param attributes
     * @param imageStoreId
     */
    private void setImageCidInSrcAndSaveInCache(Integer srcIndex, XMLAttributes attributes, Integer imageStoreId) {
        String imageCID = null;

        if (imageStoreId != null) {
            ImageStoreCmd imageStoreCmd = new ImageStoreCmd();
            imageStoreCmd.putParam("op", "download");
            imageStoreCmd.putParam("imageStoreId", imageStoreId);

            imageStoreCmd.executeInStateless(ctx);
            ResultDTO resultDTO = imageStoreCmd.getResultDTO();
            if (!resultDTO.isFailure()) {
                ArrayByteWrapper wrapper = (ArrayByteWrapper) resultDTO.get("imageWrapper");

                //save in cache
                String pathFolderAttach = ElwisCacheManager.pathCampaignMailAttachFolder_CreateIfNotExist(companyId, campaignId, userId, generationKey, true);
                String pathToFile = pathFolderAttach + wrapper.getFileName();
                try {
                    File file = new File(pathToFile);
                    if (!file.isFile()) {
                        FileOutputStream stream = new FileOutputStream(pathToFile);
                        stream.write(wrapper.getFileData());
                        stream.close();
                    }

                    imageCID = imageStoreId.toString();
                    embeddedImagesCidMap.put(imageCID, pathToFile);

                } catch (IOException e) {
                    log.debug("Canot write attach in cache...", e);
                }
            }
        }

        attributes.setValue(srcIndex, (imageCID != null) ? "cid:" + imageCID : "");
    }

}
