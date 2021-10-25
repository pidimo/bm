package com.piramide.elwis.cmd.uimanager.migration;

import com.piramide.elwis.utils.UIManagerConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.6
 */
public abstract class UIMigrationModule {

    public abstract List<Map> migration(String oldClassName, String oldAttributeName, String oldAttributeValue);

    protected Map composeNewStyle(String className, String attributeName, String attributeValue) {
        Map map = new HashMap();
        map.put("className", className);
        map.put("attribute", attributeName);
        map.put("value", attributeValue);

        return map;
    }

    protected String discomposeOldMosaicColor(String value) {
        String hexColor = null;
        if (value.startsWith("#")) {
            hexColor = value;
        } else if (value.contains("contacts.gif")) {
            hexColor = "#4697E0";  //blue
        } else if (value.contains("administration.gif")) {
            hexColor = "#A483D3";  //lila
        } else if (value.contains("campaigns.gif")) {
            hexColor = "#64BA83";  //green
        } else if (value.contains("configuration.gif")) {
            hexColor = "#D25C5C"; //red
        } else if (value.contains("principal.gif")) {
            hexColor = "#A1B4B7";  //lead
        } else if (value.contains("products.gif")) {
            hexColor = "#FFB318";  //orange
        }
        return hexColor;
    }

    protected String discomposeColorWithSeparatorArguments(String value) {
        String hexColor = value;

        if (value.indexOf(UIManagerConstants.SEPARATOR_KEY) != -1) {
            hexColor = value.substring(0, value.indexOf(UIManagerConstants.SEPARATOR_KEY)).trim();
        }
        return hexColor;
    }

    protected String composeGradientValue(String hexColor) {
        return hexColor + UIManagerConstants.VALUE_SEPARATOR_KEY + hexColor;
    }
}
