package com.piramide.elwis.utils.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Properties;

/**
 * User: alejandro
 * Date: Sep 22, 2005
 * Time: 6:02:47 PM
 */
public class ConfigurationManager {
    private final static Log log = LogFactory.getLog(ConfigurationManager.class);
    private Properties configuration;

    public ConfigurationManager() {
        configuration = new Properties();
        try {
            ClassLoader loader = this.getClass().getClassLoader();
            configuration.load(loader.getResourceAsStream("com/piramide/elwis/utils/configuration/elwis-configuration.properties"));
        } catch (Exception e) {
            log.debug("Cant read the config file..", e);
        }
    }

    public String getValue(String property) {
        //First try to recover from the external configuration
        if (System.getProperties().containsKey(property)) {
            return System.getProperty(property);
        } else if (configuration.containsKey(property))//then for the internal
        {
            return configuration.getProperty(property);
        } else//if not found, something is misconfigured
        {
            throw new RuntimeException("The configuration property: " + property + " does not exists in the" +
                    " configuration file");
        }
    }
}
