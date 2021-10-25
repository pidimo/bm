package com.piramide.elwis.web.dashboard.component.web.plugin.struts;

import com.piramide.elwis.web.dashboard.component.configuration.reader.Builder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.jdom.JDOMException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author : ivan
 */
public class DashboardPlugIn implements PlugIn {
    private static Log log = LogFactory.getLog(DashboardPlugIn.class);


    private ActionServlet servlet;
    private ModuleConfig config;
    String configFile;

    public void destroy() {
        this.servlet = null;
        this.config = null;
    }

    public void init(ActionServlet actionServlet, ModuleConfig moduleConfig) throws ServletException {
        this.servlet = actionServlet;
        this.config = moduleConfig;

        try {
            InputStream inputStream = servlet.getServletContext().getResourceAsStream(configFile);
            Builder.i.initialize(inputStream);

        } catch (IOException e) {
            log.debug(" IO ", e);
        } catch (JDOMException e) {
            log.debug(" JDOM ", e);
        }
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }
}
