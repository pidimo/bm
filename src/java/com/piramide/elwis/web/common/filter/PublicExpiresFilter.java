package com.piramide.elwis.web.common.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * This filter lets you define Expires and max-age headers for your output pages. So with this filter you will be able
 * to define a caching time for your pages. The page will be cached until that time expires.
 * Because BM on production works only in https, and becase anything can be cached over https into the disk
 * this filter must be applied only to public contects like public JS, Images or whatever is static and
 * does not share application specific stuff (like user, company information).
 *
 * @author Fernando
 */

public class PublicExpiresFilter implements Filter {

    private static final long DEFAULT_TIME = 3600L;
    private long expiresTime;

    public void init(FilterConfig config) throws ServletException {
        String expires = config.getInitParameter("expires");
        if (expires != null) {
            try {
                expiresTime = Long.parseLong(expires);
            }
            catch (Exception exception) {
                expiresTime = DEFAULT_TIME;
            }
        } else {
            expiresTime = DEFAULT_TIME;
        }

    }

    public void doFilter(ServletRequest req,
                         ServletResponse res,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        Date date = new Date();

        response.setDateHeader("Expires", date.getTime() + expiresTime * 1000L);
        response.setHeader("Cache-Control", (new StringBuilder()).append("max-age=").
                append(expiresTime).append(", public").toString());
        chain.doFilter(req, response);

    }


    public void destroy() {
        //do nothing
    }


}