package com.piramide.elwis.utils.configuration;

import java.util.Properties;

/**
 * Recovers the EAR name of the application when it is deployed as EAR, otherwise returns the empty value.
 * This is needed because EJB3 has the following pattern for all JNDI: EARNAME/BEANNAME/local, this is when
 * it is deployed within a ear, but if it is deployed just as a jar module, the JNDI is: BEANNAME/local
 *
 * @author Fernando Javier Montaño Torrico
 * @version 1.0
 */
public class EARJndiProperty {
    public static final String PROPERTY_KEY = "earJndiName";
    private static Properties configuration;

    public static String getEarName() {
        if (configuration == null) {
            configuration = new Properties();
            try {
                ClassLoader loader = EARJndiProperty.class.getClassLoader();
                configuration.load(loader.getResourceAsStream("com/piramide/elwis/utils/configuration/ear-jndi.properties"));
            } catch (Exception e) {
                //if the property file is not found means the it was not deployed as an EAR
            }
        }
        if (configuration.containsKey(PROPERTY_KEY)) {
            return configuration.getProperty(PROPERTY_KEY) + "/";
        } else {
            return "";
        }
    }
}
