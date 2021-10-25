package com.piramide.elwis.cmd.utils;

import com.piramide.elwis.utils.WebMailConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: WebmailHTMLParser.java 10395 2013-10-23 01:32:20Z miguel ${CLASS_NAME}.java,v 1.2 25-04-2005 11:36:10 AM ivan Exp $
 */
public class WebmailHTMLParser {
    private static Log log = LogFactory.getLog(WebmailHTMLParser.class);

    private static boolean evaluateUrl(String url) {
        return null != url &&
                url.indexOf("http://") != 0 &&
                url.indexOf("https://") != 0 &&
                url.indexOf("ftp://") != 0 &&
                url.indexOf("/") != 0;
    }


    private static boolean safeFind(Matcher matcher) {
        try {
            return matcher.find();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static String settingUpApplicationContextInImages(String body, String url, String attachId, String filename) {
        String oldExpression = "(src=\"[a-zA-z0-9.,:_=/\\-]*)\\?dto\\(attachId\\)=" + attachId + "*(&amp;|&)dto\\(filename\\)=" + filename + "\"";
        String newExpression = "(src=\"cid:" + attachId + "\")";

        String oldSustitute = "src=\"" + url + "\" " + "id=\"" + WebMailConstants.attachIdKey + "=" + attachId + "\"";
        String newSustitute = "src=\"" + url + "\"";
        String newBody;
        if (body.indexOf(WebMailConstants.attachIdKey + "=" + attachId) == -1) {
            //process old email
            newBody = body.replaceAll(oldExpression, oldSustitute);
            log.debug("Update URL in body OK \n " +
                    "oldExpression=" + oldExpression + "\n" +
                    "oldSustitute=" + oldSustitute + "\n");
        } else {
            //process new email
            newBody = body.replaceAll(newExpression, newSustitute);
            log.debug("Update URL in body OK \n " +
                    "newExpression=" + newExpression + "\n" +
                    "newSustitute=" + newSustitute + "\n");
        }

        return newBody;
    }


    public static String removeIdAttribute(String body) {
        String[] elements = body.split("<");
        String newBody = "";
        for (String element : elements) {
            if (element.indexOf("img") == 0 || element.indexOf("IMG") == 0) {
                if (element.indexOf("id=") > 0) {
                    String expression = "id=\"[a-zA-Z.,0-9:\\-_=/*;]*\\\"";
                    String sustitute = " ";
                    String newCad = element.replaceAll(expression, sustitute);
                    newBody += "<" + newCad;
                } else {
                    newBody += "<" + element;
                }
            } else {
                if (!"".equals(element.trim()) && !"\n".equals(element)) {
                    newBody += "<" + element;
                }
            }
        }
        return newBody;
    }

    public static String changeEmailCidByElwisCid(String cid,
                                                  Integer attachId,
                                                  String body) {
        String parsedCID = Pattern.quote(cid);
        String expression = "(src=\"cid|CID):" + parsedCID + "\"";
        String sustitutte = "src=\"cid:" + attachId + "\"" +
                " id=\"" + WebMailConstants.attachIdKey + "=" + attachId + "\"";


        body = body.replaceAll(expression, sustitutte);
        log.debug("Replace email cid by elwis cid OK \n" +
                "expression=" + expression + "\n" +
                "sustitute=" + sustitutte + "\n");

        return body;
    }

    public static String changeNewLineByTagBR(String cad) {
        log.debug("Change signature type text to html");

        String newLine = "<br/>";
        String result = "<br/>";
        char[] actualCad = cad.toCharArray();
        for (int i = 0; i < actualCad.length; i++) {
            if (actualCad[i] == '\n') {
                result += newLine;
            } else {
                result += actualCad[i];
            }
        }
        return result;
    }
}