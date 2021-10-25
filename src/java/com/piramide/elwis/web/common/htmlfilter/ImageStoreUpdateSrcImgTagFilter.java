package com.piramide.elwis.web.common.htmlfilter;

import com.piramide.elwis.utils.WebMailConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.cyberneko.html.filters.DefaultFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  Filter IMG tag by image store id and update the "src" attribute to download the image
 * @author Miky
 * @version $Id: ImageStoreUpdateSrcImgTagFilter.java 2009-09-24 11:22:02 AM $
 */
public class ImageStoreUpdateSrcImgTagFilter extends DefaultFilter {
    private Log log = LogFactory.getLog(this.getClass());

    private HttpServletResponse response;
    private HttpServletRequest request;
    private Boolean hasChanges = false;

    public ImageStoreUpdateSrcImgTagFilter(HttpServletResponse response, HttpServletRequest request) {
        this.response = response;
        this.request = request;
    }

    public Boolean getHasChanges() {
        return hasChanges;
    }

    @Override
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {

        if ("img".equalsIgnoreCase(element.rawname)) {
            Integer srcIndex = null;

            Integer imageStoreId = null;

            for (int i = 0; i < attributes.getLength(); i++) {
                if ("id".equalsIgnoreCase(attributes.getLocalName(i))) {
                    String value = attributes.getValue(i);
                    if (value.contains(WebMailConstants.TEMPORALIMAGESTOREID_KEY)) {
                        imageStoreId = getImageStoreId(value);
                    }
                }
                if ("src".equalsIgnoreCase(attributes.getLocalName(i))) {
                    srcIndex = i;
                }
            }

            if (null != imageStoreId && null != srcIndex) {
                updateSrcUrlToDownloadImage(srcIndex, attributes, imageStoreId);
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
    private Integer getImageStoreId(String idAttributeValue) {
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
     * Update the "src" attribute of img tag with application url to download the store image
     * @param srcIndex src index
     * @param attributes img tag attributes
     * @param imageStoreId image id
     */
    private void updateSrcUrlToDownloadImage(Integer srcIndex, XMLAttributes attributes, Integer imageStoreId) {
        if (imageStoreId != null) {
            String url = request.getContextPath() + "/webmail/TemporalImage/Download.do?imageStoreId=" + imageStoreId;
            String encodedUrl = response.encodeURL(url);

            attributes.setValue(srcIndex, encodedUrl);
        }
    }
}
