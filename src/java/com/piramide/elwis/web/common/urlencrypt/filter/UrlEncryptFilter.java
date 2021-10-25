package com.piramide.elwis.web.common.urlencrypt.filter;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.common.filter.LogFilter;
import com.piramide.elwis.web.common.urlencrypt.UrlCipherException;
import com.piramide.elwis.web.common.urlencrypt.UrlEncryptCipher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * This Filter is used to recognize and encrypted requested url and convert it to original
 * url. And to encrypt the urls which call the method encodeURL in the response
 *
 * @author Fernando Montaño
 * @version $Id: UrlEncryptFilter.java 10238 2012-06-15 13:30:56Z fernando $
 */

public class UrlEncryptFilter implements Filter {
    private Log log = LogFactory.getLog(this.getClass());
    private String[] excludeExtensions;
    private final String EXCLUDE_EXTENSIONS_PARAM = "excludeExtensions";
    private final String ENABLE_ENCRYPTION_PARAM = "enableEncryption";
    private final String ENABLE_LOG_FOR_EXTENSION = "enableLogForExtension";
    String logExtension;
    /**
     * defines if the url encryption is enabled or not in the application, by default it should be enabled
     */
    public static boolean ENABLE_URL_ENCRYPT = false;

    public static String JS_PARAM_NAME = "AUTHID";
    public static String JS_PARAMS_VALUES_SEPARATOR = "v2XHGrtEcdmNqd2mj7VRw";
    public static String JS_VALUES_SEPARATOR = "4gjttXG2nd5mNqd2mj7VRw";
    public static String JS_END_SEPARATOR = "m1PYECyMJIlxWvtP8Mil6A";


    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("Initializing encrypt filter");
        String excludeStrings = filterConfig.getInitParameter(EXCLUDE_EXTENSIONS_PARAM);
        String enableEncryption = filterConfig.getInitParameter(ENABLE_ENCRYPTION_PARAM);
        logExtension = filterConfig.getInitParameter(ENABLE_LOG_FOR_EXTENSION);


        if (enableEncryption != null) {
            ENABLE_URL_ENCRYPT = Boolean.parseBoolean(enableEncryption);
        } else //if the filter is configured in the web.xml, that means it must be enabled.
        {
            ENABLE_URL_ENCRYPT = true;
        }

        // Load and add the extensions which won't  be encrypted
        if (excludeStrings != null) {
            String[] extensions = excludeStrings.split(",");
            excludeExtensions = new String[extensions.length];
            for (int i = 0; i < extensions.length; i++) {
                String extension = extensions[i];
                excludeExtensions[i] = extension.trim(); //e.g. .gif, .jpg (end extensions)
            }
        }


    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest hsRequest = (HttpServletRequest) request;
        final HttpServletResponse hsResponse = (HttpServletResponse) response;
        final UrlEncryptResponseWrapper urlEncryptResponseWrapper = new UrlEncryptResponseWrapper(hsResponse,
                hsRequest, excludeExtensions);


        String originalUrl = hsRequest.getRequestURI();
        String contextPath = hsRequest.getContextPath();

        if (contextPath != null && originalUrl.startsWith(contextPath)) {
            originalUrl = originalUrl.substring(contextPath.length() + 1); //the 1 is for first /
        }
        String target = null;
        boolean requestRewritten = false;

        if (!(originalUrl.contains("/") || originalUrl.contains("."))) {
            try {
                target = UrlEncryptCipher.i.decrypt(originalUrl);

                if (target != null) {

                    if (hsRequest.getParameter(JS_PARAM_NAME) != null)  //recover ecrypted parameters added with jscript
                    {
                        target = recoverJscriptEncryptedParams(target, hsRequest.getParameter(JS_PARAM_NAME));
                    }
                    //in the server the variable &amp; (client side) must be converted to & (server side)
                    target = target.replaceAll("&amp;", "&");
                    //redirect to the decrypted url
                    final RequestDispatcher rq = getRequestDispatcher(hsRequest, target);
                    rq.forward(hsRequest, urlEncryptResponseWrapper);
                    requestRewritten = true;
                    //Log the request attributes
                    if (log.isDebugEnabled() && logExtension != null && target.contains(logExtension)) {
                        LogFilter.i.logAttributes(hsRequest, getDecryptedURI(target, hsRequest),
                                getDecryptedUrlParams(target));
                    }
                }

            } catch (UrlCipherException e) {
                //error in the decryption of the url
                log.warn("Error in decryption, it seems user is trying to access to an invalid modified url: " + e.getMessage());
                hsResponse.sendError(HttpServletResponse.SC_NOT_FOUND);//url requested not found
                return;
            } catch (JScriptDecryptionException e) {
                //some problems in the javascript params decrypted, it seems it was modified manually
                log.warn("Error decrypting the javascript params:" + e.getMessage());
                hsResponse.sendError(HttpServletResponse.SC_NOT_FOUND);//url requested not found
                return;
            }
        }

        if (!requestRewritten) {
            chain.doFilter(hsRequest, urlEncryptResponseWrapper);
        }


    }


    public void destroy() {
        excludeExtensions = null;
    }

    /**
     * return the request dispatcher object for the current request.
     *
     * @param hsRequest the request
     * @param toUrl     the url where dispatch
     * @return the dispatcher
     * @throws javax.servlet.ServletException if is not possible to get the dispatcher
     */
    private RequestDispatcher getRequestDispatcher(final HttpServletRequest hsRequest, String toUrl)
            throws ServletException {
        final RequestDispatcher rq = hsRequest.getRequestDispatcher(toUrl);
        if (rq == null) {
            // this might be a 404 possibly something else, could re-throw a 404 but is best to throw servlet exception
            throw new ServletException("unable to get request dispatcher for " + toUrl);
        }
        return rq;
    }

    /**
     * decrypt the params which store the enctypted jscript added params and add as parameters
     *
     * @param url              the destination url
     * @param jsEncryptedValue the param value of the encrypted jscript
     * @return the url with the jscript added params
     */
    private String recoverJscriptEncryptedParams(String url, String jsEncryptedValue) throws JScriptDecryptionException {
        StringBuilder sb = new StringBuilder(url);
        try {

            if (!jsEncryptedValue.endsWith(JS_END_SEPARATOR)) {
                throw new JScriptDecryptionException("jscript params: the jscript param must finish with " +
                        "JS_END_SEPARATOR, it seems it was modified manually");
            }

            String[] paramsAndValues = jsEncryptedValue.split(JS_PARAMS_VALUES_SEPARATOR);
            String[] params = UrlEncryptCipher.i.decrypt(paramsAndValues[0]).split("&");
            String[] values = paramsAndValues[1].split(JS_VALUES_SEPARATOR);

            if (params.length != values.length) {
                throw new JScriptDecryptionException("jscript params: The number of params decrypted are not equals " +
                        "to values params");
            }

            if (url.contains("?")) {
                sb.append("&");
            } else {
                sb.append("?");
            }


            for (int i = 0; i < params.length; i++) {
                String param = params[i];
                String value = values[i];
                if (!value.equals(JS_END_SEPARATOR)) {
                    sb.append(param).append("=");
                    if (value.endsWith(JS_END_SEPARATOR)) {
                        sb.append(value.substring(0, value.indexOf(JS_END_SEPARATOR)));
                    } else {
                        sb.append(value);
                    }
                } else {
                    sb.append(param).append("=").append("");
                }

                if (i + 1 < params.length) {
                    sb.append("&");
                }

            }
        } catch (Exception e) {
            throw new JScriptDecryptionException(e);
        }
        return sb.toString();

    }

    /**
     * recover the parms send in the url after decryption and before forward such url
     * this is done for log values purposes.
     *
     * @param target the decrypted url
     * @return the map with the params sent by the url.
     */
    private Map<String, String> getDecryptedUrlParams(String target) {
        Map<String, String> params = new HashMap<String, String>();

        try {
            if (target.indexOf("?") != -1) {
                String queryString = target.substring(target.indexOf("?") + 1);
                String[] queryStrings = queryString.split("&");
                for (int i = 0; i < queryStrings.length; i++) {
                    String query = queryStrings[i];
                    query += " ";// ensure the split by =
                    String[] paramValue = query.split("=");
                    params.put(paramValue[0], URLDecoder.decode(paramValue[1], Constants.CHARSET_ENCODING));
                }
            }
        } catch (Exception e) {
            log.error("Error getting the url decrypted params", e);
        }
        return params;
    }

    /**
     * Get the URI of the decrypted URL.
     *
     * @param target the target url
     * @param rq     the request
     * @return the URI obtained from url decrypted.
     */
    private String getDecryptedURI(String target, final HttpServletRequest rq) {
        if (target.indexOf("?") != -1) {
            return rq.getContextPath() + "/" + target.substring(0, target.indexOf("?"));
        }
        return target;
    }


}

class JScriptDecryptionException extends Exception {
    public JScriptDecryptionException(String message) {
        super(message);
    }

    public JScriptDecryptionException(Throwable cause) {
        super(cause);
    }
}
