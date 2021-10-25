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
public class UIMigrationModuleForms extends UIMigrationModule {
    Log log = LogFactory.getLog(this.getClass());

    public List<Map> migration(String oldClassName, String oldAttributeName, String oldAttributeValue) {
        List<Map> result = new ArrayList<Map>();

        if ("TD.title".equals(oldClassName)) {
            if ("background".equals(oldAttributeName)) {
                String hexColor = discomposeOldMosaicColor(oldAttributeValue);

                if (hexColor != null) {
                    //border color
                    String borderValue = hexColor + UIManagerConstants.SEPARATOR_KEY + "4px solid";
                    result.add(composeNewStyle("legend.title", "border-bottom", borderValue));
                }
            }

            if ("font-family".equals(oldAttributeName)) {
                result.add(composeNewStyle("legend.title", oldAttributeName, oldAttributeValue));
            }

            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle("legend.title", oldAttributeName, oldAttributeValue));
            }
        }

        if ("TD.label".equals(oldClassName)) {
            if ("font-family".equals(oldAttributeName)) {
                result.add(composeNewStyle(".control-label", oldAttributeName, oldAttributeValue));
            }

            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle(".control-label", oldAttributeName, oldAttributeValue));
            }
        }

        if ("TD.contain".equals(oldClassName)) {

            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(".formPanelBody", "background", oldAttributeValue));
            }

            //this configuration is to general font color
            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle("body", oldAttributeName, oldAttributeValue));
            }
        }


        if (!result.isEmpty()) {
            log.debug("Migration process in UIMigrationModuleForms... class= " + oldClassName + "   --   attribute= " + oldAttributeName + "   --   value= " + oldAttributeValue);
            log.debug("Result migration:" + result);
        }
        return result;
    }
}
