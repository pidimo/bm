package com.piramide.elwis.cmd.webmailmanager.util;

import com.jatun.commons.email.parser.HtmlEmailDOMParser;
import com.jatun.commons.email.parser.HtmlEmailParser;
import com.piramide.elwis.utils.webmail.filter.ProcessAttributesForImgTag;
import com.piramide.elwis.utils.webmail.filter.ProcessAttributesForLinkTAG;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class HtmlCodeUtil {
    private static Log log = LogFactory.getLog(HtmlCodeUtil.class);

    public static String processAttributesForLink(String htmlCode) {
        HtmlEmailParser parser = new HtmlEmailDOMParser();
        parser.addFilter(new ProcessAttributesForLinkTAG());

        try {
            parser.parseHtml(htmlCode);
            return parser.getHtml().toString();
        } catch (Exception e) {
            log.debug("-> Process Body Html content FAIL");
        }

        return htmlCode.toString();
    }

    public static Map updateImageCid(String htmlCode, String oldAttachId, String newAttachId) {
        boolean result = false;
        String newBody = htmlCode;
        ProcessAttributesForImgTag filter = new ProcessAttributesForImgTag(newAttachId, oldAttachId);
        HtmlEmailParser parser = new HtmlEmailDOMParser();
        parser.addFilter(filter);
        try {
            parser.parseHtml(htmlCode);
            newBody = parser.getHtml().toString();
            result = filter.getHasChanges();
        } catch (Exception e) {
            log.debug("-> Change src atttibute on IMG tags FAIL");
        }
        Map map = new HashMap();
        map.put("hasUpdated", result);
        map.put("newBody", newBody);
        return map;
    }
}
