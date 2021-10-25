package com.piramide.elwis.web.common.accessrightdatalevel.util;

import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelConstants;
import com.piramide.elwis.web.common.accessrightdatalevel.structure.AccessRightDataLevel;
import com.piramide.elwis.web.common.accessrightdatalevel.xml.BuildAccessRightDataLevelStructure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import java.io.InputStream;

/**
 * Singleton to access to the xml configuration structure
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class AccessRightDataLevelFactory {
    private final Log log = LogFactory.getLog(this.getClass());

    public  static AccessRightDataLevelFactory i = new AccessRightDataLevelFactory();
    private AccessRightDataLevel accessRightDataLevel;

    public AccessRightDataLevelFactory() {
        accessRightDataLevel = null;
    }

    public AccessRightDataLevel getAccessRightDataLevel() {
        return accessRightDataLevel;
    }

    public void initializeStructure(ServletContext context) {
        BuildAccessRightDataLevelStructure buildStructure = new BuildAccessRightDataLevelStructure();

        try {
            String configFile = AccessRightDataLevelConstants.CONFIG_FILE;
            log.debug("Loading access right data level config file:" + configFile);

            InputStream inputStream = context.getResourceAsStream(configFile);
            buildStructure.buildStructure(inputStream);
            accessRightDataLevel = buildStructure.getAccessRightDataLevel();
        } catch (Exception e) {
            log.info("Error in initialize access right data level config:", e);
            throw new RuntimeException("Access right data level config initialization failed!!", e);
        }
    }
}
