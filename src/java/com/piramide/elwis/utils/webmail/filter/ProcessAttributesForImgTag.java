package com.piramide.elwis.utils.webmail.filter;

import com.piramide.elwis.utils.WebMailConstants;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.cyberneko.html.filters.DefaultFilter;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class ProcessAttributesForImgTag extends DefaultFilter {

    private String newAttachId;
    private String oldAttachId;

    Boolean hasChanges = false;

    public ProcessAttributesForImgTag() {
    }

    public ProcessAttributesForImgTag(String newAttachId, String oldAttachId) {
        this.newAttachId = newAttachId;
        this.oldAttachId = oldAttachId;
    }

    public Boolean getHasChanges() {
        return hasChanges;
    }

    @Override
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {

        if ("img".equalsIgnoreCase(element.rawname)) {
            Integer idIndex = null;
            Integer srcIndex = null;

            for (int i = 0; i < attributes.getLength(); i++) {
                if ("id".equalsIgnoreCase(attributes.getLocalName(i))) {
                    String value = attributes.getValue(i);
                    String key = WebMailConstants.attachIdKey + "=" + oldAttachId;
                    if (value.equals(key)) {
                        idIndex = i;
                    }
                }
                if ("src".equalsIgnoreCase(attributes.getLocalName(i))) {
                    srcIndex = i;
                }
            }

            if (null != idIndex && null != srcIndex) {
                attributes.setValue(idIndex, WebMailConstants.attachIdKey + "=" + newAttachId);
                attributes.setValue(srcIndex, "cid:" + newAttachId);
                hasChanges = true;
            }
        }
        super.emptyElement(element, attributes, augs);
    }
}
