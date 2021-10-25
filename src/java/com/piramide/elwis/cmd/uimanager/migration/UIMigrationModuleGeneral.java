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
public class UIMigrationModuleGeneral extends UIMigrationModule {
    Log log = LogFactory.getLog(this.getClass());

    public List<Map> migration(String oldClassName, String oldAttributeName, String oldAttributeValue) {
        List<Map> result = new ArrayList<Map>();

        if ("BODY".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle("body", oldAttributeName, oldAttributeValue));

                //header menu background
                result.add(composeNewStyle(".navbar-default", oldAttributeName, oldAttributeValue));
                String gradientValue = composeGradientValue(oldAttributeValue);
                result.add(composeNewStyle(".navbar-default", "background-image", gradientValue));
            }

            //OBS: font color migration is make in Forms of old contain font color
        }

        if ("A".equals(oldClassName)) {
            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle("a", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".btn-link", oldAttributeName, oldAttributeValue));

                //focus
                result.add(composeNewStyle("a:hover, a:focus", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".btn-link:hover,.btn-link:focus", oldAttributeName, oldAttributeValue));
            }
        }

        if ("INPUT".equals(oldClassName)) {
            if ("background".equals(oldAttributeName)) {
                result.add(composeNewStyle(".form-control", "background-color", oldAttributeValue));
            }
            if ("font-family".equals(oldAttributeName)) {
                result.add(composeNewStyle(".form-control", oldAttributeName, oldAttributeValue));
            }
            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle(".form-control", oldAttributeName, oldAttributeValue));
            }
        }

        if ("INPUT.button".equals(oldClassName)) {
            if ("font-family".equals(oldAttributeName)) {
                result.add(composeNewStyle(".btn", oldAttributeName, oldAttributeValue));
            }

            if ("color".equals(oldAttributeName)) {
                //default button
                result.add(composeNewStyle(".btn-default", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".btn-default, .btn-default:hover", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".btn-default:focus, .btn-default.focus", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".btn-default:hover", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".btn-default:active:hover,.btn-default.active:hover,.btn-default:active:focus,.btn-default.active:focus", oldAttributeName, oldAttributeValue));

                //cancel button
                result.add(composeNewStyle(".btn-primary", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".btn-primary, .btn-primary:hover", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".btn-primary:focus, .btn-primary.focus", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".btn-primary:hover", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".btn-primary:active:hover,.btn-primary.active:hover,.btn-primary:active:focus,.btn-primary.active:focus", oldAttributeName, oldAttributeValue));
            }

            if ("background".equals(oldAttributeName)) {

                String newAttribute = "background-color";
                String hexColor = discomposeOldMosaicColor(oldAttributeValue);
                if (hexColor != null) {
                    //default button
                    result.add(composeNewStyle(".btn-default", newAttribute, hexColor));
                    result.add(composeNewStyle(".btn-default, .btn-default:hover", newAttribute, hexColor));
                    result.add(composeNewStyle(".btn-default:focus, .btn-default.focus", newAttribute, hexColor));
                    result.add(composeNewStyle(".btn-default:hover", newAttribute, hexColor));
                    result.add(composeNewStyle(".btn-default:active:hover,.btn-default.active:hover,.btn-default:active:focus,.btn-default.active:focus", newAttribute, hexColor));

                    //gradient default button
                    String gradientValue = composeGradientValue(hexColor);
                    result.add(composeNewStyle(".btn-default, .btn-default:hover", "background-image", gradientValue));

                    //cancel button
                    result.add(composeNewStyle(".btn-primary", newAttribute, hexColor));
                    result.add(composeNewStyle(".btn-primary, .btn-primary:hover", newAttribute, hexColor));
                    result.add(composeNewStyle(".btn-primary:focus, .btn-primary.focus", newAttribute, hexColor));
                    result.add(composeNewStyle(".btn-primary:hover", newAttribute, hexColor));
                    result.add(composeNewStyle(".btn-primary:active:hover,.btn-primary.active:hover,.btn-primary:active:focus,.btn-primary.active:focus", newAttribute, hexColor));

                    //gradient cancel button
                    result.add(composeNewStyle(".btn-primary, .btn-primary:hover", "background-image", gradientValue));
                }
            }

            if ("border-top".equals(oldAttributeName)) {

                String newAttribute = "border-color";
                String hexColor = discomposeColorWithSeparatorArguments(oldAttributeValue);

                //default button
                result.add(composeNewStyle(".btn-default", newAttribute, hexColor));
                result.add(composeNewStyle(".btn-default, .btn-default:hover", newAttribute, hexColor));
                result.add(composeNewStyle(".btn-default:focus, .btn-default.focus", newAttribute, hexColor));
                result.add(composeNewStyle(".btn-default:hover", newAttribute, hexColor));
                result.add(composeNewStyle(".btn-default:active:hover,.btn-default.active:hover,.btn-default:active:focus,.btn-default.active:focus", newAttribute, hexColor));

                //cancel button
                result.add(composeNewStyle(".btn-primary", newAttribute, hexColor));
                result.add(composeNewStyle(".btn-primary, .btn-primary:hover", newAttribute, hexColor));
                result.add(composeNewStyle(".btn-primary:focus, .btn-primary.focus", newAttribute, hexColor));
                result.add(composeNewStyle(".btn-primary:hover", newAttribute, hexColor));
                result.add(composeNewStyle(".btn-primary:active:hover,.btn-primary.active:hover,.btn-primary:active:focus,.btn-primary.active:focus", newAttribute, hexColor));
            }
        }


        if (!result.isEmpty()) {
            log.debug("Migration process in UIMigrationModuleGeneral... class= " + oldClassName + "   --   attribute= " + oldAttributeName + "   --   value= " + oldAttributeValue);
            log.debug("Result migration:" + result);
        }
        return result;
    }
}
