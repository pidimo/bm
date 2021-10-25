package com.piramide.elwis.web.common.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

/**
 * Provides logging of HTTP requests and response.
 * It contains some changes adjusted to this application.
 *
 * @author kaz (kaz@dev.java.net)
 * @version $Id: LogFilter.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class LogFilter implements Filter {

    protected final Log log = LogFactory.getLog(getClass());

    public void init(FilterConfig arg0) throws ServletException {
        //do nothing
    }

    public void doFilter(
            ServletRequest req,
            ServletResponse res,
            FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(req, res);
        if (log.isDebugEnabled()) {
            logAttributes((HttpServletRequest) req);
        }
    }

    public static final LogFilter i = new LogFilter();

    public void destroy() {
        //do nothing
    }

    public void logAttributes(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        logMiscInfo(req, sb);
        logParams(req, sb);
        logRequestAttributes(req, sb);
        logSessionAttributes(req, sb);
        sb.append("\n**************************************************************************\n");
        sb.append("\n\n\n");
        log.debug(sb.toString());

    }

    public void logAttributes(HttpServletRequest req, String uri, Map<String, String> additionalParams) {
        StringBuilder sb = new StringBuilder();
        logMiscInfo(req, sb, uri);
        logParams(req, sb, additionalParams);
        logRequestAttributes(req, sb);
        logSessionAttributes(req, sb);
        sb.append("\n**************************************************************************\n");
        sb.append("\n\n\n");
        log.debug(sb.toString());

    }

    private void logMiscInfo(HttpServletRequest req, StringBuilder sb) {
        sb.append("URI=").append(req.getRequestURI());
        sb.append(", UserPrincipal=").append(req.getUserPrincipal());
        sb.append(", Locale=").append(req.getLocale());

    }

    private void logMiscInfo(HttpServletRequest req, StringBuilder sb, String uri) {
        sb.append("URI=").append(uri);
        sb.append(", UserPrincipal=").append(req.getUserPrincipal());
        sb.append(", Locale=").append(req.getLocale());

    }

    private void logRequestAttributes(final HttpServletRequest req, StringBuilder sb) {
        sb.append("\nREQUEST:\n");
        final LogSource rep = new LogSource() {
            public String getPrefix() {
                return " REQ";
            }

            public Enumeration getKeys() {
                return req.getAttributeNames();
            }

            public Object getValue(String key) {
                return req.getAttribute(key);
            }
        };
        printKeysAndValues(rep, sb);
    }

    private void logParams(final HttpServletRequest req, StringBuilder sb) {
        sb.append("\nPARAMETERS:\n");
        final LogSource rep = new LogSource() {
            public String getPrefix() {
                return " PRM";
            }

            public Enumeration getKeys() {
                return req.getParameterNames();
            }

            public Object getValue(String key) {
                return req.getParameter(key);
            }
        };
        printKeysAndValues(rep, sb);
    }

    private void logParams(final HttpServletRequest req, StringBuilder sb, Map<String, String> additionalParams) {
        sb.append("\nPARAMETERS:\n");

        final LogSource rep = new LogSource() {
            public String getPrefix() {
                return " PRM";
            }

            public Enumeration getKeys() {
                return req.getParameterNames();
            }

            public Object getValue(String key) {
                return req.getParameter(key);
            }
        };
        final Hashtable<String, String> params = new Hashtable<String, String>(additionalParams);
        final LogSource repAdd = new LogSource() {
            public String getPrefix() {
                return " PRM";
            }

            public Enumeration getKeys() {
                return params.keys();

            }

            public Object getValue(String key) {
                return params.get(key);
            }
        };
        printKeysAndValues(repAdd, sb);
        printKeysAndValues(rep, sb);
    }

    private void logSessionAttributes(HttpServletRequest req, StringBuilder sb) {
        sb.append("\nSESSION:\n");
        final HttpSession session = req.getSession(false);
        if (session == null) {
            return;
        }

        final LogSource rep = new LogSource() {
            public String getPrefix() {
                return " SES";
            }

            public Enumeration getKeys() {
                return session.getAttributeNames();
            }

            public Object getValue(String key) {
                return session.getAttribute(key);
            }
        };
        printKeysAndValues(rep, sb);
    }

    private void printKeysAndValues(LogSource rep, StringBuilder sb) {

        //retrieve keys
        final Enumeration en = rep.getKeys();
        if (!en.hasMoreElements()) {
            return;
        }

        //print values

        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            sb.append(rep.getPrefix());
            sb.append("[");
            sb.append(key);
            sb.append("]: ");
            sb.append(rep.getValue(key));
            if (en.hasMoreElements()) {
                sb.append("\n");
            }
        }

    }

    private interface LogSource {
        public String getPrefix();

        public Enumeration getKeys();

        public Object getValue(String key);
    }

}
