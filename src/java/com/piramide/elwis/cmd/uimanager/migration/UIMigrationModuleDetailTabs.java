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
public class UIMigrationModuleDetailTabs extends UIMigrationModule {
    Log log = LogFactory.getLog(this.getClass());

    public List<Map> migration(String oldClassName, String oldAttributeName, String oldAttributeValue) {
        List<Map> result = new ArrayList<Map>();
        boolean isMigratedActiveAttr = false;

        if ("TD.folderTab".equals(oldClassName)) {

            if ("font-family".equals(oldAttributeName)) {
                isMigratedActiveAttr = true;
                result.add(composeNewStyle("withoutClassName_tabsGeneral", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".nav-tabs > li.active > a, .nav-tabs > li.active > a:hover, .nav-tabs > li.active > a:focus", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".navTabsDetail", oldAttributeName, oldAttributeValue));
            }

            if ("font-weight".equals(oldAttributeName)) {
                isMigratedActiveAttr = true;
                result.add(composeNewStyle("withoutClassName_tabsGeneral", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".nav-tabs > li.active > a, .nav-tabs > li.active > a:hover, .nav-tabs > li.active > a:focus", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".navTabsDetail", oldAttributeName, oldAttributeValue));
            }

            if ("border".equals(oldAttributeName)) {
                isMigratedActiveAttr = true;
                result.add(composeNewStyle(".nav-tabs > li.active > a, .nav-tabs > li.active > a:hover, .nav-tabs > li.active > a:focus", oldAttributeName, oldAttributeValue));
                //border bottom
                result.add(composeNewStyle(".nav-tabs", "border-bottom", oldAttributeValue));

                //inactive focus border
                String hexColor = discomposeColorWithSeparatorArguments(oldAttributeValue);
                result.add(composeNewStyle("withoutClassName_tabsFocus", "border-color", hexColor));
                result.add(composeNewStyle(".nav-tabs > li > a:hover", "border-color", hexColor));
            }

            //inactive font color
            if ("color".equals(oldAttributeName)) {

                if (oldAttributeValue != null && !oldAttributeValue.toUpperCase().equals("#FFFFFF")) {
                    result.add(composeNewStyle(".navTabsDetail", oldAttributeName, oldAttributeValue));
                }
            }

        }

        //active conf
        if ("TD.folderTab#current".equals(oldClassName)) {

            if ("color".equals(oldAttributeName)) {
                isMigratedActiveAttr = true;
                result.add(composeNewStyle(".nav-tabs > li.active > a, .nav-tabs > li.active > a:hover, .nav-tabs > li.active > a:focus", oldAttributeName, oldAttributeValue));

                //inactive focus
                result.add(composeNewStyle("withoutClassName_tabsFocus", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".navTabsDetail:hover, .navTabsDetail:focus", oldAttributeName, oldAttributeValue));

                //Selected Tab dropdown
                result.add(composeNewStyle(".dropdownTabsDetail > .active > a, .dropdownTabsDetail > .active > a:hover, .dropdownTabsDetail > .active > a:focus", oldAttributeName, oldAttributeValue));
            }
            if ("background-color".equals(oldAttributeName)) {
                isMigratedActiveAttr = true;
                result.add(composeNewStyle(".nav-tabs > li.active > a, .nav-tabs > li.active > a:hover, .nav-tabs > li.active > a:focus", oldAttributeName, oldAttributeValue));

                //gradient
                String gradientValue = composeGradientValue(oldAttributeValue);
                result.add(composeNewStyle(".nav-tabs > li.active > a, .nav-tabs > li.active > a:hover, .nav-tabs > li.active > a:focus", "background-image", gradientValue));

                //inactive focus background
                result.add(composeNewStyle("withoutClassName_tabsFocus", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".nav-tabs > li > a:hover, .nav-tabs > li > a:focus", oldAttributeName, oldAttributeValue));

                result.add(composeNewStyle("withoutClassName_tabsFocus", "background-image", gradientValue));
                result.add(composeNewStyle(".nav-tabs > li > a:hover, .nav-tabs > li > a:focus", "background-image", gradientValue));

                //Selected Tab dropdown
                result.add(composeNewStyle(".dropdownTabsDetail > .active > a, .dropdownTabsDetail > .active > a:hover, .dropdownTabsDetail > .active > a:focus", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".dropdownTabsDetail > .active > a, .dropdownTabsDetail > .active > a:hover, .dropdownTabsDetail > .active > a:focus", "background-image", gradientValue));
            }
        }

        //add constant values if is migrated
        if (isMigratedActiveAttr) {
            Map constantStylemap = composeNewStyle(".nav-tabs > li.active > a, .nav-tabs > li.active > a:hover, .nav-tabs > li.active > a:focus", "border-bottom-color", "transparent");
            constantStylemap.put("isConstantStyle", "true");
            result.add(constantStylemap);
        }




        if (!result.isEmpty()) {
            log.debug("Migration process in UIMigrationModuleDetailTabs... class= " + oldClassName + "   --   attribute= " + oldAttributeName + "   --   value= " + oldAttributeValue);
            log.debug("Result migration:" + result);
        }

        return result;
    }
}
