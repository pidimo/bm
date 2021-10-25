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
public class UIMigrationModuleLists extends UIMigrationModule {
    Log log = LogFactory.getLog(this.getClass());

    public List<Map> migration(String oldClassName, String oldAttributeName, String oldAttributeValue) {
        List<Map> result = new ArrayList<Map>();

        if ("TH.listHeader".equals(oldClassName)) {
            if ("background".equals(oldAttributeName)) {
                result.add(composeNewStyle("th.listHeader", "background-image", composeGradientValue(oldAttributeValue)));
            }
            if ("font-family".equals(oldAttributeName)) {
                result.add(composeNewStyle("th.listHeader", oldAttributeName, oldAttributeValue));
            }
            if ("font-weight".equals(oldAttributeName)) {
                result.add(composeNewStyle("th.listHeader", oldAttributeName, oldAttributeValue));
            }
            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle("th.listHeader", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("th.listHeader a", oldAttributeName, oldAttributeValue));
            }
        }

        if ("TR.listRow".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(".table > tbody > tr.listRow", oldAttributeName, oldAttributeValue));

                //odd list row
                result.add(composeNewStyle(".table > tbody > tr.listRow:nth-of-type(2n+1)", oldAttributeName, oldAttributeValue));
            }

            String commonClassName = ".table > thead > tr > th.listHeader, .table > tbody > tr > th.listHeader, .table > tfoot > tr > th.listHeader, .table > thead > tr.listRow > td, .table > tbody > tr.listRow > td, .table > tfoot > tr.listRow > td";
            if ("border".equals(oldAttributeName)) {
                result.add(composeNewStyle(".table > tbody > tr.listRow", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(commonClassName, oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("tr.listRowOver:hover td", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.listItem", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.listItem2Center", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.listItem2", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.listItemRight", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.listItem2Right", oldAttributeName, oldAttributeValue));
            }

            if ("font-family".equals(oldAttributeName)) {
                result.add(composeNewStyle(".table > tbody > tr.listRow", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("tr.listRowOver:hover td", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.listItem", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.listItem2Center", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.listItem2", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.listItemRight", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.listItem2Right", oldAttributeName, oldAttributeValue));
            }
            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle(".table > tbody > tr.listRow", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("tr.listRowOver:hover td", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.listItem", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.listItem2Center", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.listItem2", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.listItemRight", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.listItem2Right", oldAttributeName, oldAttributeValue));
            }
        }

        if ("TR.listRowOver".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                String value = oldAttributeValue + UIManagerConstants.SEPARATOR_KEY + "!important";

                result.add(composeNewStyle("tr.listRowOver:hover", oldAttributeName, value));
                result.add(composeNewStyle("tr.listRow:hover", oldAttributeName, value));
            }
        }

        if ("td.listItemHighlight".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(oldClassName, oldAttributeName, oldAttributeValue));
            }
        }

        //pagin bar
        if ("table.pagingBar".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(".table-striped > tbody > tr.listRowLast", oldAttributeName, oldAttributeValue));
            }
            if ("border".equals(oldAttributeName)) {
                result.add(composeNewStyle(".table-striped > tbody > tr.listRowLast", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".table-striped > tbody > tr.listRowLast td", oldAttributeName, oldAttributeValue));
            }
            if ("font-family".equals(oldAttributeName)) {
                result.add(composeNewStyle(".table-striped > tbody > tr.listRowLast", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.pagingBar", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("table.pagingBar", oldAttributeName, oldAttributeValue));
            }
            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle(".table-striped > tbody > tr.listRowLast", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("td.pagingBar", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle("table.pagingBar", oldAttributeName, oldAttributeValue));
            }
        }

        //alphabet
        if ("TD.alpha".equals(oldClassName)) {
            if ("background-color".equals(oldAttributeName)) {
                result.add(composeNewStyle(".alpha table .btn", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".alpha table .btn.active", oldAttributeName, oldAttributeValue));
            }
            if ("font-family".equals(oldAttributeName)) {
                result.add(composeNewStyle(".alpha table .btn", oldAttributeName, oldAttributeValue));
            }
            if ("color".equals(oldAttributeName)) {
                result.add(composeNewStyle(".alpha table .btn", oldAttributeName, oldAttributeValue));
                result.add(composeNewStyle(".alpha table .btn.active", oldAttributeName, oldAttributeValue));
            }
        }



        if (!result.isEmpty()) {
            log.debug("Migration process in UIMigrationModuleLists... class= " + oldClassName + "   --   attribute= " + oldAttributeName + "   --   value= " + oldAttributeValue);
            log.debug("Result migration:" + result);
        }
        return result;
    }
}
