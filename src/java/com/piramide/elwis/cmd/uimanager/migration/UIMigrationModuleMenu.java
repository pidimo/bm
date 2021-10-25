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
public class UIMigrationModuleMenu extends UIMigrationModule {
    Log log = LogFactory.getLog(this.getClass());

    public List<Map> migration(String oldClassName, String oldAttributeName, String oldAttributeValue) {
        List<Map> result = new ArrayList<Map>();

        if ("TD.moduleTab".equals(oldClassName)) {

            if ("font-family".equals(oldAttributeName)) {
                result.add(composeNewStyle(".navbar-inverse", oldAttributeName, oldAttributeValue));

                //drop down menu font
                result.add(composeNewStyle(".navbar-inverse .navbar-nav .open .dropdown-menu > li > a", oldAttributeName, oldAttributeValue));
            }

            if ("font-weight".equals(oldAttributeName)) {
                result.add(composeNewStyle(".navbar-inverse", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".navbar-inverse .navbar-toggle:hover, .navbar-inverse .navbar-toggle:focus", oldAttributeName, oldAttributeValue));

                //drop down menu font weight
                result.add(composeNewStyle(".navbar-inverse .navbar-nav .open .dropdown-menu > li > a", oldAttributeName, oldAttributeValue));
            }

            if ("border".equals(oldAttributeName)) {
                String hexColor = discomposeColorWithSeparatorArguments(oldAttributeValue);
                result.add(composeNewStyle(".navbar-inverse", "border-color", hexColor));

                //mobile toggle and spacer
                //toogle
                result.add(composeNewStyle(".navbar-inverse .navbar-toggle .icon-bar", "background-color", hexColor));
                //spacer border color
                result.add(composeNewStyle(".navbar-inverse .navbar-collapse, .navbar-inverse .navbar-form", "border-color", hexColor));
                result.add(composeNewStyle(".navbar-inverse .navbar-toggle", "border-color", hexColor));
            }

            //inactive background
            if ("background-color".equals(oldAttributeName)) {

                result.add(composeNewStyle(".navbar-inverse", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".navbar-inverse .navbar-toggle:hover, .navbar-inverse .navbar-toggle:focus", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".navbar-inverse .navbar-nav .open .dropdown-menu > li > a:hover, .navbar-inverse .navbar-nav .open .dropdown-menu > li > a:focus", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".dropdown-menu > li > a:hover, .dropdown-menu > li > a:focus", oldAttributeName, oldAttributeValue));

                //gradient
                String gradientValue = composeGradientValue(oldAttributeValue);
                String gradientAttribute = "background-image";

                result.add(composeNewStyle(".navbar-inverse", gradientAttribute, gradientValue));
                result.add(composeNewStyle(".navbar-inverse .navbar-toggle:hover, .navbar-inverse .navbar-toggle:focus", gradientAttribute, gradientValue));
                result.add(composeNewStyle(".navbar-inverse .navbar-nav .open .dropdown-menu > li > a:hover, .navbar-inverse .navbar-nav .open .dropdown-menu > li > a:focus", gradientAttribute, gradientValue));
                result.add(composeNewStyle(".dropdown-menu > li > a:hover, .dropdown-menu > li > a:focus", gradientAttribute, gradientValue));
            }

            //inactive font color
            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle(".navbar-inverse .navbar-nav > li > a", oldAttributeName, oldAttributeValue));
            }
        }

        //active
        if ("TD.moduleTab#currentModule".equals(oldClassName)) {

            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle(".navbar-inverse .navbar-nav > .active > a, .navbar-inverse .navbar-nav > .active > a:hover, .navbar-inverse .navbar-nav > .active > a:focus", oldAttributeName, oldAttributeValue));

                //focus inactive
                result.add(composeNewStyle(".navbar-inverse .navbar-nav > li > a:hover, .navbar-inverse .navbar-nav > li > a:focus", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".navbar-inverse .navbar-nav > .open > a, .navbar-inverse .navbar-nav > .open > a:hover, .navbar-inverse .navbar-nav > .open > a:focus", oldAttributeName, oldAttributeValue));
            }
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(".navbar-inverse .navbar-nav > .active > a, .navbar-inverse .navbar-nav > .active > a:hover, .navbar-inverse .navbar-nav > .active > a:focus", oldAttributeName, oldAttributeValue));

                //focus inactive
                result.add(composeNewStyle(".navbar-inverse .navbar-nav > li > a:hover, .navbar-inverse .navbar-nav > li > a:focus", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".navbar-inverse .navbar-nav > .open > a, .navbar-inverse .navbar-nav > .open > a:hover, .navbar-inverse .navbar-nav > .open > a:focus", oldAttributeName, oldAttributeValue));
            }
        }



        if (!result.isEmpty()) {
            log.debug("Migration process in UIMigrationModuleMenu... class= " + oldClassName + "   --   attribute= " + oldAttributeName + "   --   value= " + oldAttributeValue);
            log.debug("Result migration:" + result);
        }
        return result;
    }
}
