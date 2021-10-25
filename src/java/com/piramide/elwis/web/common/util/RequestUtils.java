package com.piramide.elwis.web.common.util;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.config.ModuleConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * General purpose utility extension to Struts RequestUtils methods related to processing a servlet request
 * in the Struts controller framework.
 *
 * @author Fernando Montaño
 * @version $Id: RequestUtils.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class RequestUtils {

    private static Log log = LogFactory.getLog(RequestUtils.class);

    /**
     * Return the string action mapping url
     *
     * @param action
     * @param pageContext
     * @param contextRelative
     * @return String
     */

    public static String getActionMappingURL(String action, PageContext pageContext, boolean contextRelative,
                                             boolean addModuleName, boolean addModuleParams) {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer value = new StringBuffer(request.getContextPath());
        ModuleConfig config =
                (ModuleConfig) pageContext.getRequest().getAttribute(Globals.MODULE_KEY);
        if (config != null) {
            if (!contextRelative) {
                value.append(config.getPrefix());
            }
        }

        // Use our servlet mapping, if one is specified
        String servletMapping =
                (String) pageContext.getAttribute(Globals.SERVLET_KEY, PageContext.APPLICATION_SCOPE);
        if (servletMapping != null) {
            String queryString = null;
            int question = action.indexOf("?");
            if (question >= 0) {
                queryString = action.substring(question);
            }
            String actionMapping = org.apache.struts.util.RequestUtils.getActionMappingName(action);
            if (servletMapping.startsWith("*.")) {
                value.append(actionMapping);
                value.append(servletMapping.substring(1));
            } else if (servletMapping.endsWith("/*")) {
                value.append(servletMapping.substring(0, servletMapping.length() - 2));
                value.append(actionMapping);
            } else if (servletMapping.equals("/")) {
                value.append(actionMapping);
            }
            if (queryString != null) {
                value.append(queryString);
            }
        }

        // Otherwise, assume extension mapping is in use and extension is
        // already included in the action property
        else {
            if (!action.startsWith("/")) {
                value.append("/");
            }
            value.append(action);
        }
        //log.debug("Current value = " + value.toString());

        // Return the completed value
        return (value.toString());
        /*return (URLParameterProcessor.addModuleParameters(value.toString(),(HttpServletRequest)pageContext.getRequest(),
                pageContext.getServletContext(), addModuleName, addModuleParams));*/
    }

    public static User getUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(Constants.USER_KEY);
    }

    public static int getIntParameter(HttpServletRequest request, String param, int defaultValue) {
        int value = defaultValue;
        try {
            value = Integer.parseInt(request.getParameter(param));
        } catch (Exception e) {
        }
        return value;
    }

    public static String getStringParameter(HttpServletRequest request, String param, String defaultValue) {
        String value = request.getParameter(param);
        return value != null ? value : defaultValue;
    }
}
