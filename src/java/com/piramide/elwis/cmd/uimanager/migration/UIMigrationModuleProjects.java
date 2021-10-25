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
public class UIMigrationModuleProjects extends UIMigrationModule {
    Log log = LogFactory.getLog(this.getClass());

    public List<Map> migration(String oldClassName, String oldAttributeName, String oldAttributeValue) {
        List<Map> result = new ArrayList<Map>();

        if ("td.projectTime_enteredColor".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }
        }
        if ("td.projectTime_releasedColor".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }
        }
        if ("td.projectTime_confirmedColor".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }
        }
        if ("td.projectTime_notConfirmedColor".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }
        }
        if ("td.projectTime_invoicedColor".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }
        }


        if (!result.isEmpty()) {
            log.debug("Migration process in UIMigrationModuleProjects... class= " + oldClassName + "   --   attribute= " + oldAttributeName + "   --   value= " + oldAttributeValue);
            log.debug("Result migration:" + result);
        }
        return result;
    }
}
