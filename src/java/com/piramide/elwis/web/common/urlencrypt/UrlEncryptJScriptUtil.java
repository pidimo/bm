package com.piramide.elwis.web.common.urlencrypt;

import com.piramide.elwis.web.common.urlencrypt.filter.UrlEncryptFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Utility class which generate a URL to be used for window.location
 * and window.open jscript functions.
 * The final url is encrypted.
 *
 * @author Fernando Montaño
 * @version $Id: UrlEncryptJScriptUtil.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class UrlEncryptJScriptUtil {

    /**
     * Used to encrypt a url and add parameters which are readed from jscript functions.
     * e.g. window.location = 'urltogo.jsp?param1=' + document.getElementById('fieldId');
     * Using this utility with window.location is needed to add the ' and ' for the value,
     * this quotes are generate automatically by this method.
     * If you pass as example the url : contacts/new/data.do?param=1&param2=45545
     * and the map with jsparams like: paramName = "type", JsparamValue ="document.getElementById('fieldId')"
     * Then the url encrypted generated will be something like:
     * 'jdfhJsjsdiKUskdnsdkKjdsmdsfldskJSKdksdlKJLj' + '?AUTHID=SDKSHDKJHSDHJS' + document.getElementById('fieldId') + "AJSHJSDJH".
     * This url when is sent to server the url encryptor filter process automtically and generates the original url:
     * contacts/new/data.do?param=1&param2=45545&type=345
     *
     * @param request  the request.
     * @param response the response.
     * @param url      the original url, begining with the root / (but does not including the context path)
     * @param jsParams the jscript params to be readed from jscript function.
     * @return the encrypted url
     */
    public static synchronized String encryptForJScript(String url, LinkedHashMap<String, String> jsParams,
                                                        HttpServletRequest request, HttpServletResponse response) {
        String contextPath = request.getContextPath();
        StringBuilder sb = new StringBuilder();
        String finalUrl = url;
        finalUrl = response.encodeURL(contextPath + url);
        sb.append("'");
        sb.append(finalUrl);
        if (jsParams.size() == 0) {
            sb.append("'");
        } else {
            sb.append(finalUrl.contains("?") ? "&" : "?").append("'");
            if (UrlEncryptFilter.ENABLE_URL_ENCRYPT) {
                sb.append(encryptJsURLParams(jsParams));
            } else {
                int pos = 1;
                for (Iterator<String> iterator = jsParams.keySet().iterator(); iterator.hasNext();) {
                    String param = iterator.next();
                    sb.append(" + ").append((pos > 1) ? "'&" : "'").append(param).append("='").append(" + ")
                            .append(jsParams.get(param));
                    pos++;
                }
            }
        }
        return sb.toString();
    }

    /**
     * Encrypt the params which are added with jscript, using encryption.
     *
     * @param jsParams the jscript params to add
     * @return the encrypted URL param storing the jscript
     */
    private static StringBuilder encryptJsURLParams(LinkedHashMap<String, String> jsParams) {
        StringBuilder sb = new StringBuilder();
        sb.append(" + '");
        sb.append(UrlEncryptFilter.JS_PARAM_NAME).append("=");
        String encryptedParams = "";
        String[] values = new String[jsParams.size()];
        int pos = 0;
        for (Iterator<String> iterator = jsParams.keySet().iterator(); iterator.hasNext();) {
            String param = iterator.next();
            encryptedParams += param;
            if (pos + 1 < jsParams.size()) {
                encryptedParams += "&";//params separator
            }
            values[pos] = jsParams.get(param);
            pos++;
        }
        sb.append(UrlEncryptCipher.i.encrypt(encryptedParams))
                .append(UrlEncryptFilter.JS_PARAMS_VALUES_SEPARATOR);

        sb.append("'");

        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            sb.append(" + ");
            if (i > 0) {
                sb.append("'").append(UrlEncryptFilter.JS_VALUES_SEPARATOR).append("' + ");
            }
            sb.append(value);

        }
        sb.append(" + '").append(UrlEncryptFilter.JS_END_SEPARATOR).append("'");
        return sb;
    }
}
