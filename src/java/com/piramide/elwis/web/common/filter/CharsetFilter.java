package com.piramide.elwis.web.common.filter;

import com.piramide.elwis.utils.Constants;

import javax.servlet.*;
import java.io.IOException;

/**
 * Defines the character encoding for the requests
 *
 * @author Fernando
 * @version $Id: CharsetFilter.java 9104 2009-04-08 22:47:21Z fernando $
 */

public class CharsetFilter implements Filter {

    public void init(FilterConfig arg0) throws ServletException {
        //do nothing
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding(Constants.CHARSET_ENCODING);
        response.setCharacterEncoding(Constants.CHARSET_ENCODING);
        chain.doFilter(request, response);
    }


    public void destroy() {
        //do nothing
    }


}
