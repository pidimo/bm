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
public class ProcessAttributesForLinkTAG extends DefaultFilter {

    @Override
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {

        if ("a".equalsIgnoreCase(element.rawname)) {
            Integer targetAttributeIndex = null;

            boolean requireTargetAttribute = true;
            for (int i = 0; i < attributes.getLength(); i++) {
                if ("href".equalsIgnoreCase(attributes.getLocalName(i))) {
                    String url = attributes.getValue(i);
                    String newUrl = processURL(url);
                    requireTargetAttribute = requireTargetAttribute(newUrl);
                    attributes.setValue(i, newUrl);
                }

                if ("target".equalsIgnoreCase(attributes.getLocalName(i))) {
                    targetAttributeIndex = i;
                }
            }

            if (!requireTargetAttribute && null != targetAttributeIndex) {
                attributes.removeAttributeAt(targetAttributeIndex);
            }

            if (requireTargetAttribute) {
                if (null == targetAttributeIndex) {
                    QName target = new QName("target", null, "target", null);
                    attributes.addAttribute(target, "CDATA", "_blank");
                } else {
                    attributes.setValue(targetAttributeIndex, "_blank");
                }
            }
        }
        super.startElement(element, attributes, augs);
    }

    private boolean requireTargetAttribute(String url) {
        final String anchor = "#";
        final String javascript = "javascript:";
        final String mailto = "mailto:";
        return !(url.indexOf(anchor) == 0 ||
                url.indexOf(javascript) == 0 ||
                url.indexOf(mailto) == 0);

    }

    private String processURL(String url) {
        final String http = "http://";
        final String https = "https://";
        final String ftp = "ftp://";
        final String anchor = "#";
        final String javascript = "javascript:";
        final String mailto = "mailto:";

        String auxUrl = url.toLowerCase();
        if (auxUrl.indexOf(http) == 0 ||
                auxUrl.indexOf(https) == 0 ||
                auxUrl.indexOf(ftp) == 0 ||
                auxUrl.indexOf(anchor) == 0 ||
                auxUrl.indexOf(javascript) == 0 ||
                auxUrl.indexOf(mailto) == 0) {
            return url;
        }

        return http + url;
    }
}
