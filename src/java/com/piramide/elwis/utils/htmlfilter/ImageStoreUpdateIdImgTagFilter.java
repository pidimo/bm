package com.piramide.elwis.utils.htmlfilter;

import com.piramide.elwis.utils.WebMailConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.cyberneko.html.filters.DefaultFilter;

import java.util.Map;

/**
 * Html filter for img tag, update the "id" attribute with the new value of image store id. This is used when templates is
 * copied between companies
 * @author Miky
 * @version $Id: ImageStoreUpdateIdImgTagFilter.java 2009-09-24 07:09:23 PM $
 */
public class ImageStoreUpdateIdImgTagFilter extends DefaultFilter {
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * Map that contain old image store id and new image store id
     */
    Map<Integer, Integer> sourceTargetImageStoreIdMap;

    public ImageStoreUpdateIdImgTagFilter(Map<Integer, Integer> sourceTargetImageStoreIdMap) {
        this.sourceTargetImageStoreIdMap = sourceTargetImageStoreIdMap;
    }


    @Override
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {

        if ("img".equalsIgnoreCase(element.rawname)) {

            for (int i = 0; i < attributes.getLength(); i++) {
                if ("id".equalsIgnoreCase(attributes.getLocalName(i))) {
                    String value = attributes.getValue(i);
                    if (value.contains(WebMailConstants.TEMPORALIMAGESTOREID_KEY)) {
                        Integer imageStoreId = getImageStoreId(value);

                        //update old id with new image store id
                        if (imageStoreId != null && sourceTargetImageStoreIdMap.containsKey(imageStoreId)) {
                            String newIdAttribute = WebMailConstants.TEMPORALIMAGESTOREID_KEY + "=" + sourceTargetImageStoreIdMap.get(imageStoreId);
                            attributes.setValue(i, newIdAttribute);
                        }
                    }
                }
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
}
