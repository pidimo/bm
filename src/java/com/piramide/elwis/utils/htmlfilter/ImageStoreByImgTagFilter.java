package com.piramide.elwis.utils.htmlfilter;

import com.piramide.elwis.utils.WebMailConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.cyberneko.html.filters.DefaultFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * filter by img html tag, search for image store id in "id" attribute
 *
 * @author Miky
 * @version $Id: ImageStoreByImgTagFilter.java 2009-06-22 04:25:44 PM $
 */
public class ImageStoreByImgTagFilter extends DefaultFilter {
    private Log log = LogFactory.getLog(this.getClass());

    private List<Integer> imageStoreIdList;

    public ImageStoreByImgTagFilter() {
        imageStoreIdList = new ArrayList<Integer>();
    }

    public List<Integer> getImageStoreIdList() {
        return imageStoreIdList;
    }

    @Override
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {

        if ("img".equalsIgnoreCase(element.rawname)) {

            for (int i = 0; i < attributes.getLength(); i++) {
                if ("id".equalsIgnoreCase(attributes.getLocalName(i))) {
                    String value = attributes.getValue(i);
                    if (value.contains(WebMailConstants.TEMPORALIMAGESTOREID_KEY)) {
                        Integer imageStoreId = getImageStoreId(value);
                        if (imageStoreId != null) {
                            imageStoreIdList.add(imageStoreId);
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
