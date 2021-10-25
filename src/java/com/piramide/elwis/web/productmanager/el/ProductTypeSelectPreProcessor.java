package com.piramide.elwis.web.productmanager.el;

import com.piramide.elwis.utils.ProductConstants;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.tag.SelectItemPreProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class ProductTypeSelectPreProcessor implements SelectItemPreProcessor {
    private Log log = LogFactory.getLog(this.getClass());

    public Collection<Map> preProcessListStructureResult(Collection<Map> listResult, Parameters parameters, boolean readOnly, HttpServletRequest request) {
        Collection<Map> processedListResult = listResult;

        if (listResult != null) {
            Map eventTypeItemMap = null;
            for (Iterator<Map> iterator = listResult.iterator(); iterator.hasNext();) {
                Map rowMap = iterator.next();

                String type = rowMap.get("productTypeType").toString();
                if (ProductConstants.ProductTypeType.EVENT.equal(type)) {
                    rowMap.put("name", getEventTypeName(request));
                    eventTypeItemMap = rowMap;
                    iterator.remove();
                    break;
                }
            }

            if (eventTypeItemMap != null) {
                processedListResult = new ArrayList<Map>();
                processedListResult.addAll(listResult);
                processedListResult.add(eventTypeItemMap);
            }
        }

        return processedListResult;
    }

    private String getEventTypeName(HttpServletRequest request) {
        return JSPHelper.getMessage(request, "ProductType.item.event");
    }
}
