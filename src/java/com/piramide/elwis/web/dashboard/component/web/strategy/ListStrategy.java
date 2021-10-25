package com.piramide.elwis.web.dashboard.component.web.strategy;

import com.piramide.elwis.web.dashboard.component.configuration.reader.Builder;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Column;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.core.AbstractStrategy;
import com.piramide.elwis.web.dashboard.component.core.ComponentManager;
import com.piramide.elwis.web.dashboard.component.core.UIWrapper;
import com.piramide.elwis.web.dashboard.component.web.struts.action.DrawComponentAction;
import com.piramide.elwis.web.dashboard.component.web.util.TemplateResourceBundle;
import com.piramide.elwis.web.dashboard.component.web.util.TemplateResourceBundleFactory;
import com.piramide.elwis.web.dashboard.component.web.util.WebUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.util.*;

/**
 * @author : ivan
 */
public class ListStrategy extends AbstractStrategy {
    private String componentView = "";
    private Log log = LogFactory.getLog(this.getClass());

    public ListStrategy(DrawComponentAction action,
                        int componentId,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Locale locale) {

        log.debug("Build List Strategy...");
        Component xmlComponent = Builder.i.findComponentById(componentId);

        Component cloneComponent = (Component) xmlComponent.clone();

        Map dataBaseParameters = action.dataBaseReadParameters(request);

        //read component configuration from data base
        ComponentManager.i.readComponentFromDataBase(
                dataBaseParameters,
                xmlComponent,
                cloneComponent,
                request);

        //read component contain
        Map searchParameters = action.getSearchParameters(request);
        searchParameters.putAll(dataBaseParameters);

        List<Map> result = ComponentManager.i.readListComponentContain(cloneComponent,
                componentId,
                searchParameters,
                action,
                request,
                response);

        Integer numberOfElements = new Integer(cloneComponent.getRowCounterFilter().getNumberOfElements());
        Integer numberOfShowedElements = new Integer(cloneComponent.getRowCounterFilter().getInitialValue());
        if (numberOfElements < numberOfShowedElements) {
            numberOfShowedElements = numberOfElements;
        }

        Map uiParameters = new HashMap();
        uiParameters.put("NUMBER_OF_ELEMENTS", numberOfElements);
        uiParameters.put("NUMBER_OF_SHOWED_ELEMENTS", numberOfShowedElements);

        TemplateResourceBundle resources = TemplateResourceBundleFactory.i.getTemplateResourceBundle(
                action.getApplicationResourcesPath(), locale);

        List<Map> processColumns = WebUtils.processColumns(result, cloneComponent.getVisibleColumns());
        List<Map> accessColumns = WebUtils.processColumns(result, xmlComponent.getAccessColumns());

        Map elParams = new HashMap();
        if (null != xmlComponent.getComponentConfiguration().getAccessUrl()) {
            JspFactory f = JspFactory.getDefaultFactory();
            PageContext pageContext = f.getPageContext(action.getServlet(), request, response, "", true, JspWriter.NO_BUFFER, true);
            elParams = WebUtils.evaluateELParametersOfUrls(pageContext,
                    xmlComponent.getComponentConfiguration().getAccessUrl().getParams());
        }

        List<Column> selColumns = verifySizeOfColumns(cloneComponent.getVisibleColumns());

        //build wrapper object for draw component
        Map converterParams = action.getConverterParameters(request);
        UIWrapper uiWrapper = new UIWrapper(selColumns,
                accessColumns,
                processColumns,
                xmlComponent.getAccessColumns(),
                resources,
                elParams,
                converterParams,
                request,
                response);

        //build component HTML view
        StringBuilder uiComponent = ComponentManager.i.buildUIComponent(
                uiWrapper,
                uiParameters,
                dataBaseParameters,
                componentId);

        componentView = uiComponent.toString();
    }

    public String buildComponent() {
        return componentView;
    }

    private List<Column> verifySizeOfColumns(List<Column> selectedColumns) {
        List<Column> result = new ArrayList<Column>();
        int totalSize = 0;

        for (Column c : selectedColumns) {
            Column mySColumn = new Column(c);
            result.add(mySColumn);
            if (null != c.getSize()) {
                totalSize += new Integer(c.getSize());
            }
        }
        int unUsedSize = 95 - totalSize;

        if (unUsedSize > 0) {
            int idealSize = unUsedSize / selectedColumns.size();
            for (Column c : result) {
                if (null != c.getSize()) {
                    int actualSize = new Integer(c.getSize());
                    c.setSize(String.valueOf((actualSize + idealSize)));
                } else {
                    c.setSize(String.valueOf(idealSize));
                }
            }
        }
        return result;
    }
}
