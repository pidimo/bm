package com.piramide.elwis.web.common.urlencrypt.filter;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.urlencrypt.UrlEncryptCipher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

/**
 * Jatun S.R.L.
 * It process the response wrapper of the response, encrypting the URLs if required.
 *
 * @author Fernando Montaño
 * @version $Id: UrlEncryptResponseWrapper.java 10589 2015-05-22 18:36:52Z miguel $
 */

public class UrlEncryptResponseWrapper extends HttpServletResponseWrapper {
    private final HttpServletRequest request;
    private final String[] excludeExtensions;

    public UrlEncryptResponseWrapper(HttpServletResponse servletResponse, HttpServletRequest httpServletRequest,
                                     String[] excludeExtensions) {
        super(servletResponse);
        this.request = httpServletRequest;
        this.excludeExtensions = excludeExtensions.clone();

    }

    public String encodeURL(String s) {
        return processEncryptedEncodeURL(s, false);

    }

    public String encodeRedirectURL(String s) {
        return processEncryptedEncodeURL(s, true);

    }

    public String encodeUrl(String s) {
        return processEncryptedEncodeURL(s, false);
    }

    public String encodeRedirectUrl(String s) {
        return processEncryptedEncodeURL(s, true);
    }


    /**
     * Encrypt the url of the requestes URL called with Responde.encodeURL()
     *
     * @param url        the url response to encrypt
     * @param isRedirect defines if it is a redirect or not
     * @return the encrypted string
     */
    private String processEncryptedEncodeURL(String url, boolean isRedirect) {


        for (String excludeExtension : excludeExtensions) {
            if (!url.contains("?") && url.endsWith(excludeExtension)) {
                return url;
            }
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.USER_KEY);
        if (user != null && UrlEncryptFilter.ENABLE_URL_ENCRYPT) {
            if (url.contains("?")) {
                url += "&";
            } else {
                url += "?";
            }
            url += Constants.REQUEST_COMPANY_ID + "=" + user.getValue(Constants.COMPANYID);
        }

        /**
         * only process external request urls. Internal are not processed.
         * e.g. jsp include or forward, like struts forward.
         *
         */
        String contextPath = request.getContextPath();
        String postfixPath = ("/".equals(request.getContextPath())) ? "" : "/";

        if (url.startsWith(request.getContextPath() + postfixPath) && UrlEncryptFilter.ENABLE_URL_ENCRYPT) {
            url = url.substring(contextPath.length() + 1); //the 1 is for first "/"
            url = UrlEncryptCipher.i.encrypt(url);
            url = "/" + url;//restore the "/" for the context path
            if (isRedirect) {
                return super.encodeRedirectURL(contextPath + url);
            } else {
                return super.encodeURL(contextPath + url);
            }
        }
        if (isRedirect) {
            return super.encodeRedirectURL(url);
        } else {
            return super.encodeURL(url);
        }

    }


}
