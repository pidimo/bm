package com.piramide.elwis.cmd.uimanager.migration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.6
 */
public class UIMigrationModuleDashboard extends UIMigrationModule {
    Log log = LogFactory.getLog(this.getClass());

    public List<Map> migration(String oldClassName, String oldAttributeName, String oldAttributeValue) {
        List<Map> result = new ArrayList<Map>();

        if (".nowDate".equals(oldClassName)) {
            if ("background".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }
        }


        if (!result.isEmpty()) {
            log.debug("Migration process in UIMigrationModuleDashboard... class= " + oldClassName + "   --   attribute= " + oldAttributeName + "   --   value= " + oldAttributeValue);
            log.debug("Result migration:" + result);
        }
        return result;
    }
}
