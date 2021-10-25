package com.piramide.elwis.utils.configuration;


/**
 * Load the system configuration properties
 *
 * @author Alejandro
 * @author Fernando Montaño
 */
public class ConfigurationFactory {
    private static ConfigurationManager configurationManager;

    public static ConfigurationManager getConfigurationManager() {
        if (configurationManager == null) {
            synchronized (ConfigurationFactory.class) {
                if (configurationManager == null) {
                    configurationManager = new ConfigurationManager();
                }
            }
        }
        return configurationManager;
    }

    public static String getValue(String property) {
        return getConfigurationManager().getValue(property);
    }

}
