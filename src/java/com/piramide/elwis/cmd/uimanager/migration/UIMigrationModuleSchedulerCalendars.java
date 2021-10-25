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
public class UIMigrationModuleSchedulerCalendars extends UIMigrationModule {
    Log log = LogFactory.getLog(this.getClass());

    public List<Map> migration(String oldClassName, String oldAttributeName, String oldAttributeValue) {
        List<Map> result = new ArrayList<Map>();

        if ("table.calendar_table".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {

                String calendarLineClass = ".calendar_table.table-bordered > tbody > tr > td, .calendar_table.table-bordered > tbody > tr > th";
                String borderValue = oldAttributeValue + UIManagerConstants.SEPARATOR_KEY + "1px solid";

                result.add(composeNewStyle(calendarLineClass, "border", borderValue));
            }
        }

        if ("tr.calendar_header".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                String importantValue = oldAttributeValue + UIManagerConstants.SEPARATOR_KEY + "!important";

                result.add(composeNewStyle(oldClassName, oldAttributeName, importantValue));

                String gradientValue = composeGradientValue(oldAttributeValue);

                result.add(composeNewStyle(oldClassName, "background-image", gradientValue));
                result.add(composeNewStyle("tr.calendar_header th", "background-image", gradientValue));

            }
            if ("font-family".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }

            /*if ("font-size".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }*/

            if ("font-weight".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("tr.calendar_header th", oldAttributeName, oldAttributeValue));
            }

            if ("height".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }

            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("tr.calendar_header th", oldAttributeName, oldAttributeValue));
            }
        }

        if ("td.calendar_cell".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }
        }

        if ("td.day_cell".equals(oldClassName)) {
            if ("font-family".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }

            /*if ("font-size".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }*/

            if ("font-weight".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }

            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.day_cell A", oldAttributeName, oldAttributeValue));
            }
        }

        if ("td.calendar_week_cell".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }

            if ("font-family".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }

            if ("font-weight".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.calendar_week_cell A", oldAttributeName, oldAttributeValue));
            }

            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.calendar_week_cell A", oldAttributeName, oldAttributeValue));
            }
        }

        if ("DIV.month_day_header a".equals(oldClassName)) {

            if ("font-family".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.year_cell A", oldAttributeName, oldAttributeValue));
            }

            /*if ("font-size".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }*/

            if ("font-weight".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }

            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.year_cell A", oldAttributeName, oldAttributeValue));
            }
        }

        if ("DIV.holiday div".equals(oldClassName)) {
            if ("font-family".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }

            /*if ("font-size".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }*/

            if ("font-weight".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }

            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }
        }

        if (".smallcalendar_appointment".equals(oldClassName)) {
            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }
        }

        if (".appointment_font".equals(oldClassName)) {
            if ("font-family".equals(oldAttributeName)) {
                result.add(composeNewStyle(".appointment_font a.colorAHref", oldAttributeName, oldAttributeValue));
            }

           /* if ("font-size".equals(oldAttributeName)) {
                result.add(composeNewStyle(".appointment_font a.colorAHref", oldAttributeName, oldAttributeValue));
            }*/

            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle(".appointment_font a.colorAHref", oldAttributeName, oldAttributeValue));
            }
        }

        if ("td.calendar_cell#outmonthColor".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }
        }

        if ("td.calendar_cell#todayColor".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }
        }

        if ("td.calendar_cell#holidayColor".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }
        }

        if (!result.isEmpty()) {
            log.debug("Migration process in UIMigrationModuleSchedulerCalendars... class= " + oldClassName + "   --   attribute= " + oldAttributeName + "   --   value= " + oldAttributeValue);
            log.debug("Result migration:" + result);
        }
        return result;
    }
}
