package com.piramide.elwis.cmd.uimanager.migration;

import com.piramide.elwis.utils.UIManagerConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.6
 */
public class UIMigrationModuleSchedulerTask extends UIMigrationModule {
    Log log = LogFactory.getLog(this.getClass());

    public List<Map> migration(String oldClassName, String oldAttributeName, String oldAttributeValue) {
        List<Map> result = new ArrayList<Map>();

        if ("td.task_expireColor".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                String value = oldAttributeValue + UIManagerConstants.SEPARATOR_KEY + "!important";
                result.add(composeNewStyle(oldClassName, oldAttributeName, value));
            }
        }


        if (!result.isEmpty()) {
            log.debug("Migration process in UIMigrationModuleSchedulerTask... class= " + oldClassName + "   --   attribute= " + oldAttributeName + "   --   value= " + oldAttributeValue);
            log.debug("Result migration:" + result);
        }
        return result;
    }
}
