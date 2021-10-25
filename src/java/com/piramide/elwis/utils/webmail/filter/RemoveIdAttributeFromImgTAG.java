package com.piramide.elwis.utils.webmail.filter;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.cyberneko.html.filters.DefaultFilter;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class RemoveIdAttributeFromImgTAG extends DefaultFilter {

    @Override
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {

        if ("img".equalsIgnoreCase(element.rawname)) {
            Integer idIndex = null;
            for (int i = 0; i < attributes.getLength(); i++) {
                if ("id".equalsIgnoreCase(attributes.getLocalName(i))) {
                    idIndex = i;
                    break;
                }
            }
            if (null != idIndex) {
                attributes.removeAttributeAt(idIndex);
            }
        }
        super.emptyElement(element, attributes, augs);
    }
}
