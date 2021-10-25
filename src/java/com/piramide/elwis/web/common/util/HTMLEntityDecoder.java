package com.piramide.elwis.web.common.util;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Jatun S.R.L.
 * Decode most common HTML entities such as '&gt;' in '>'
 *
 * @author Miky
 * @version $Id: HTMLEntityDecoder.java 9756 2009-09-28 21:18:59Z miguel $
 */
public class HTMLEntityDecoder {

    /**
     * Decodes the HTML string, which can contain replacement characters for HTML tags such as &lt;, &gt;, &quot;, or &amp;.
     * Is here used jakarta commons StringEscapeUtils util
     *
     * @param content
     * @return String
     */
    public static String decode(String content) {

        if (content == null) {
            return null;
        }

        return StringEscapeUtils.unescapeHtml(content);
    }
}
