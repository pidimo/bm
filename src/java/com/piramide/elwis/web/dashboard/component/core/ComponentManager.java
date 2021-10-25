package com.piramide.elwis.web.dashboard.component.core;

import com.piramide.elwis.web.dashboard.component.configuration.reader.Builder;
import com.piramide.elwis.web.dashboard.component.configuration.structure.*;
import com.piramide.elwis.web.dashboard.component.execute.DataProcessor;
import com.piramide.elwis.web.dashboard.component.execute.Executor;
import com.piramide.elwis.web.dashboard.component.execute.FilterPreProcessor;
import com.piramide.elwis.web.dashboard.component.persistence.AbstractDao;
import com.piramide.elwis.web.dashboard.component.persistence.DaoFactory;
import com.piramide.elwis.web.dashboard.component.ui.UiHeaderProcessor;
import com.piramide.elwis.web.dashboard.component.ui.UiListProcessor;
import com.piramide.elwis.web.dashboard.component.util.Constant;
import com.piramide.elwis.web.dashboard.component.web.struts.action.DrawComponentAction;
import com.piramide.elwis.web.dashboard.component.web.util.WebUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : ivan
 */
public class ComponentManager {
    private Log log = LogFactory.getLog(this.getClass());
    public static ComponentManager i = new ComponentManager();

    private ComponentManager() {
    }

    public void readComponentFromDataBase(Map map,
                                          Component xmlComponet,
                                          Component cloneComponent,
                                          HttpServletRequest request) {
        log.debug("\n\n\nxmlComponent \n" + xmlComponet);
        log.debug("===============================================\n\n");
        log.debug("\n\n\ncloneComponent \n" + cloneComponent);
        log.debug("===============================================\n\n");
        String persistentClassName = xmlComponet.getDashBoardConfiguration().getPersistenceProcessor();
        AbstractDao dao = DaoFactory.getDao(persistentClassName);
        dao.readComponentFromDataBase(cloneComponent, map, request);

    }

    public void saveComponentToDB(Map parameters, Map dataBaseReadParameters, int componentId, HttpServletRequest request) {
        log.debug("saveComponentToDB('" + parameters + "', '" + componentId + "')");
        List<Map> filtersMapList = (List<Map>) parameters.get(Constant.FILTERS);
        List<Map> staticFiltersMapList = (List<Map>) parameters.get(Constant.STATIC_FILTERS);

        List<Map> visibleColumns = (List<Map>) parameters.get(Constant.VISIBLE_COLUMNS);
        List<Map> orderableColumns = (List<Map>) parameters.get(Constant.ORDERABLE_COLUMNS);


        Component xmlComponent = Builder.i.findComponentById(componentId);
        Component cloneComponent = (Component) xmlComponent.clone();
        buildFilters(filtersMapList, staticFiltersMapList, cloneComponent);
        buildVisibleColumns(visibleColumns, cloneComponent);
        buildOrderableColums(orderableColumns, cloneComponent);

        log.debug("\n\n\ncloneComponent to save \n" + cloneComponent);
        log.debug("===============================================\n\n");

        String persistenceClassName = xmlComponent.getDashBoardConfiguration().getPersistenceProcessor();
        AbstractDao dao = DaoFactory.getDao(persistenceClassName);
        dao.saveComponentConfiguration(cloneComponent, dataBaseReadParameters, request);


    }

    private void buildFilters(List<Map> filtersMap, List<Map> staticFiltersMap, Component cloneComponent) {
        List<Filter> newFilters = new ArrayList<Filter>();

        if (filtersMap != null) {
            for (Map filterMap : filtersMap) {
                String name = (String) filterMap.get(Constant.FILTER_NAME);
                String value = String.valueOf(filterMap.get(Constant.FILTER_VALUE));
                Boolean isRange = (Boolean) filterMap.get(Constant.FILTER_ISRANGE);

                ConfigurableFilter confFilter = cloneComponent.getConfigurableFilter(name);
                if (null != confFilter) {
                    if (confFilter.isRangeView()) {
                        String[] values = value.split(",");
                        confFilter.setInitialValue(values[0]);
                        confFilter.setFinalValue(values[1]);
                    } else {
                        confFilter.setInitialValue(value);
                    }
                }
                newFilters.add(confFilter);
            }
        }

        if (staticFiltersMap != null) {
            for (Map staticFilterMap : staticFiltersMap) {
                String name = (String) staticFilterMap.get(Constant.FILTER_NAME);
                String value = String.valueOf(staticFilterMap.get(Constant.FILTER_VALUE));

                StaticFilter staticFilter = cloneComponent.getStaticFilter(name);
                if (staticFilter != null) {
                    staticFilter.setValue(value);

                    newFilters.add(staticFilter);
                }
            }
        }

        cloneComponent.setFilters(newFilters);
    }

    private void buildVisibleColumns(List<Map> columnsMap, Component cloneComponent) {
        List<Column> columns = new ArrayList<Column>();

        if (null != columnsMap) {
            for (Map columnMap : columnsMap) {
                int xmlId = new Integer(columnMap.get(Constant.COLUMN_XMLID).toString());
                int position = new Integer(columnMap.get(Constant.COLUMN_POSITION).toString());
                String name = (String) columnMap.get(Constant.COLUMN_NAME);

                Column column = cloneComponent.getColumn(xmlId);
                column.setPosition(position);
                columns.add(column);
            }
        }
        cloneComponent.setColumns(columns);
    }


    private void buildOrderableColums(List<Map> columnsMaps, Component cloneComponent) {
        if (null != columnsMaps) {
            for (Map columnMap : columnsMaps) {
                int xmlId = new Integer(columnMap.get("XMLID").toString());
                String order = (String) columnMap.get("ORDER");

                Column orderableColumn = cloneComponent.getColumn(xmlId);
                if (null != orderableColumn) {
                    orderableColumn.setOrder(order);
                }
            }
        }
    }

    public List<Map> readListComponentContain(Component cloneComponent,
                                              int componentId,
                                              Map searchParameters,
                                              DrawComponentAction action,
                                              HttpServletRequest request,
                                              HttpServletResponse response) {
        log.debug("Read component contain information " + cloneComponent.getId());
        FilterPreProcessor filterPreProcessor = Executor.getFilterPreProcessor(componentId);
        if (null != filterPreProcessor) {
            JspFactory f = JspFactory.getDefaultFactory();
            PageContext pageContext = f.getPageContext(action.getServlet(), request, response, "", true, JspWriter.NO_BUFFER, true);
            List<Parameter> evaluatedParams = WebUtils.evaluateELParameters(pageContext, cloneComponent.getParameters());
            cloneComponent.setParameters(evaluatedParams);
            filterPreProcessor.processConfigurableFilter(cloneComponent, searchParameters);
        }
        DataProcessor dataProcessor = Executor.getDataProcessor(componentId);
        dataProcessor.readComponentContain(cloneComponent, searchParameters, action, request);
        int numberOfallElements = dataProcessor.getNumberOfAllElements();

        cloneComponent.getRowCounterFilter().setNumberOfElements(String.valueOf(numberOfallElements));
        return dataProcessor.getData();
    }


    public StringBuilder buildUIComponent(UIWrapper wrapper, Map map, Map dataBaseParameters, int componentId) {
        StringBuilder uiString;
        UiListProcessor uiProcessor = new UiListProcessor(wrapper, map, dataBaseParameters, componentId);
        uiString = uiProcessor.buildUIComponent();
        return uiString;
    }

    public StringBuilder buildUIComponentHeader(UIWrapper wrapper, Map params, Map dataBaseParameters, int componentId) {
        StringBuilder uiString;
        UiHeaderProcessor uiProcessor = new UiHeaderProcessor(wrapper, params, dataBaseParameters, componentId);
        uiString = uiProcessor.buildUIComponent();
        return uiString;
    }
}
