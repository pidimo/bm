package com.piramide.elwis.web.common.plugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Plugin to load the module params needed to rewrite the URLs for a specific module.
 *
 * @author Fernando Montaño
 * @version $Id: ModuleParamPlugIn.java 9124 2009-04-17 00:35:24Z fernando $
 */

public class ModuleParamPlugIn implements PlugIn {
    /**
     * Commons Logging instance.
     */
    private static Log log = LogFactory.getLog(ModuleParamPlugIn.class);
    private String moduleParams = null;
    /**
     * The module configuration for our owning module.
     */
    private ModuleConfig config = null;

    /**
     * The {@link ActionServlet} owning this application.
     */
    private ActionServlet servlet = null;
    private final static String RESOURCE_DELIM = ",";
    public final static String MODULE_PARAMS_KEY = "com.piramide.elwis.web.common.plugin.MODULE_PARAMS";
    private List params = null;

    public String getModuleParams() {
        return moduleParams;
    }

    public void setModuleParams(String moduleParams) {
        this.moduleParams = moduleParams;
    }

    public void destroy() {
        servlet = null;
        config = null;
        moduleParams = null;
        params = null;

    }

    public void init(ActionServlet servlet, ModuleConfig config) throws ServletException {

        // Remember our associated configuration and servlet
        this.config = config;
        this.servlet = servlet;

        // Load our database from persistent storage
        try {
            initParams();
            servlet.getServletContext().setAttribute(MODULE_PARAMS_KEY + config.getPrefix(),
                    params);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UnavailableException("Cannot load a module params for '" + moduleParams + "'");
        }
    }

    /**
     * Initialize the params for this module.
     */
    private void initParams() {
        this.params = new LinkedList();

        if (moduleParams == null || moduleParams.length() <= 0) {
            return;
        }
        StringTokenizer st = new StringTokenizer(moduleParams, RESOURCE_DELIM);

        while (st.hasMoreTokens()) {
            String moduleParam = st.nextToken().trim();
            log.info("module param = '" + moduleParam + "'");
            params.add(moduleParam);
        }
    }
}
