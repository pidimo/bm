package com.piramide.elwis.web.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Util to parse html document
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.4.3
 */
public class HTMLParserUtil {

    private static Log log = LogFactory.getLog(HTMLParserUtil.class);

    /**
     * Parse the html document and get only the content the "BODY" tag
     * @param html html document
     * @return String
     */
    public static String getHtmlDocumentBodyContent(String html) {
        String bodyContent = html;

        if (html != null) {
            try {
                Document doc = Jsoup.parse(html);
                //get the inner body elements
                Elements elements = doc.select("body").first().children();
                bodyContent = elements.outerHtml();
            } catch (Exception e) {
                log.debug("Cannot parse html...", e);
            }
        }
        return bodyContent;
    }

}
