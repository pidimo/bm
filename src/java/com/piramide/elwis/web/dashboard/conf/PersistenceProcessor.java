package com.piramide.elwis.web.dashboard.conf;

import com.piramide.elwis.cmd.dashboard.ComponentReadCmd;
import com.piramide.elwis.cmd.dashboard.ComponentSaveCmd;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Column;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.configuration.structure.ConfigurableFilter;
import com.piramide.elwis.web.dashboard.component.configuration.structure.StaticFilter;
import com.piramide.elwis.web.dashboard.component.persistence.AbstractDao;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : ivan
 *         Date: Aug 31, 2006
 *         Time: 2:16:46 PM
 */
public class PersistenceProcessor extends AbstractDao {
    private Log log = LogFactory.getLog(this.getClass());

    public void readComponentFromDataBase(Component cloneComponent, Map map, HttpServletRequest request) {
        ComponentReadCmd cmd = new ComponentReadCmd();
        cmd.putParam("dbComponentId", map.get("dbComponentId"));
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);

            List columnsInMap = (List) resultDTO.get("COLUMNS");

            List filtersInMap = (List) resultDTO.get("FILTERS");

            updateComponentColumns(cloneComponent, columnsInMap);
            updateComponentFilters(cloneComponent, filtersInMap);
        } catch (AppLevelException e) {
            log.error("Cannot read Component configuration  from data base", e);
        }
    }

    private void updateComponentColumns(Component cloneComponent, List columnsMaps) {
        List<Column> newColumns = new ArrayList<Column>();
        //if not exists columns stored in database then use only default columns of component
        if (null == columnsMaps || columnsMaps.size() == 0) {
            newColumns.addAll(cloneComponent.getDefaultColumns());
            newColumns.addAll(cloneComponent.getAccessColumns());
            cloneComponent.setColumns(newColumns);
        } else {

            //iterate over all columns stored in data base
            for (int i = 0; i < columnsMaps.size(); i++) {
                Map columnMap = (Map) columnsMaps.get(i);
                int xmlId = (Integer) columnMap.get("XmlColumnId");
                String dbOrder = (String) columnMap.get("order");

                Column newColumn;
                //search in visible columns of cloneComponent the stored component
                if (null != (newColumn = findColumnById(xmlId, cloneComponent.getVisibleColumns()))) {
                    newColumn.setOrder(dbOrder);
                    newColumns.add(newColumn);
                }
            }

            newColumns.addAll(cloneComponent.getAccessColumns());

            //update all columns of cloneComponent only stored columns
            cloneComponent.setColumns(newColumns);
        }
    }

    private void updateComponentFilters(Component cloneComponent, List filterMaps) {

        //for every filter stored in data base update cloneComponent configurable filter
        for (int i = 0; i < filterMaps.size(); i++) {
            Map element = (Map) filterMaps.get(i);
            boolean isRange = (Boolean) element.get("isRange");
            String value = (String) element.get("value");
            String name = (String) element.get("name");

            //search filter in cloneComponent
            ConfigurableFilter configurableFilter = cloneComponent.getConfigurableFilter(name);
            if (null != configurableFilter) {
                if (isRange) {
                    //range filters use ',' separator for values in data base
                    String[] values = value.split(",");
                    configurableFilter.setInitialValue(values[0]);
                    configurableFilter.setFinalValue(values[1]);
                } else {
                    configurableFilter.setInitialValue(value);
                }

            } else {
                StaticFilter staticFilter = cloneComponent.getStaticFilter(name);
                if (staticFilter != null) {
                    if (staticFilter.getMultipleValue()) {
                        String newValue = (staticFilter.getValue() != null) ? staticFilter.getValue() + "," + value : value;
                        staticFilter.setValue(newValue);
                    } else {
                        staticFilter.setValue(value);
                    }
                }
            }
        }
    }

    private Column findColumnById(int columnId, List<Column> columns) {
        for (Column column : columns) {
            if (columnId == column.getId()) {
                return column;
            }
        }
        return null;
    }

    public void saveComponentConfiguration(Map parametersToSave, HttpServletRequest request) {
        log.debug("saveComponentConfiguration('" + parametersToSave + "', javax.servlet.http.HttpServletRequest)");

        ComponentSaveCmd cmd = new ComponentSaveCmd();
        cmd.putParam(parametersToSave);
        try {
            BusinessDelegate.i.execute(cmd, request);
        } catch (AppLevelException e) {
            e.printStackTrace();
        }
    }


    public void saveComponentConfiguration(Component cloneComponent, Map databaseReadParameters, HttpServletRequest request) {
        List<ConfigurableFilter> filters = cloneComponent.getConfigurableFilters();
        List<StaticFilter> staticFilters = cloneComponent.getStaticFilters();

        List<Column> visibleColumns = cloneComponent.getVisibleColumns();
        List<Column> orderableColumns = cloneComponent.getOrderableColumns();

        List<Map> filtersMap = new ArrayList<Map>();
        if (null != filters) {
            for (ConfigurableFilter filter : filters) {
                String value = filter.getInitialValue();
                if (filter.isRangeView()) {
                    value = filter.getInitialValue() + "," + filter.getFinalValue();
                }
                Map map = new HashMap();
                map.put("FILTER_NAME", filter.getName());
                map.put("VALUE", value);
                map.put("ISRANGE", filter.isRangeView());

                filtersMap.add(map);
            }
        }

        if (staticFilters != null) {
            for (StaticFilter staticFilter : staticFilters) {
                String value = staticFilter.getValue();

                if (staticFilter.getMultipleValue()) {
                    String[] valuesArray = value.split(",");

                    for (String v : valuesArray) {
                        if (v != null && !"".equals(v)) {
                            Map map = new HashMap();
                            map.put("FILTER_NAME", staticFilter.getName());
                            map.put("VALUE", v);
                            map.put("ISRANGE", Boolean.FALSE);

                            filtersMap.add(map);
                        }
                    }
                } else {
                    Map map = new HashMap();
                    map.put("FILTER_NAME", staticFilter.getName());
                    map.put("VALUE", value);
                    map.put("ISRANGE", Boolean.FALSE);

                    filtersMap.add(map);
                }
            }
        }

        List<Map> visibleColumnsMap = new ArrayList<Map>();
        if (null != visibleColumns) {
            for (Column column : visibleColumns) {
                int id = column.getId();
                int position = column.getPosition();
                String name = column.getName();

                Map map = new HashMap();
                map.put("XMLID", id);
                map.put("POSITION", position);
                map.put("COLUMN_NAME", name);

                visibleColumnsMap.add(map);
            }
        }

        List<Map> orderableColumnsMap = new ArrayList<Map>();
        if (null != orderableColumns) {
            for (Column column : orderableColumns) {
                int id = column.getId();
                String order = column.getOrder();

                Map map = new HashMap();
                map.put("XMLID", id);
                map.put("ORDER", order);

                orderableColumnsMap.add(map);
            }
        }

        Map dtoParameters = new HashMap();
        dtoParameters.put("FILTERS", filtersMap);
        dtoParameters.put("VISIBLE_COLUMNS", visibleColumnsMap);
        dtoParameters.put("ORDERABLE_COLUMNS", orderableColumnsMap);
        dtoParameters.putAll(databaseReadParameters);

        ComponentSaveCmd cmd = new ComponentSaveCmd();
        cmd.putParam(dtoParameters);
        try {
            BusinessDelegate.i.execute(cmd, request);
        } catch (AppLevelException e) {
            e.printStackTrace();
        }
    }


    /*public Component readComponentConfiguration(Map parametersToRead, HttpServletRequest request) {
        log.debug("readComponentConfiguration('" + parametersToRead + "', javax.servlet.http.HttpServletRequest)");

        ComponentReadCmd cmd = new ComponentReadCmd();
        cmd.putParam("dbComponentId", parametersToRead.get("dbComponentId"));

        Component component = null;
        try {
            ResultDTO myResultDTO = BusinessDelegate.i.execute(cmd, request);
            if (myResultDTO.isFailure())
                return null;


            Map componentMap = (Map) myResultDTO.get("COMPONENT_INFO");

            List columnsInMap = (List) myResultDTO.get("COLUMNS");

            List filtersInMap = (List) myResultDTO.get("FILTERS");


            List<Column> columns = new ArrayList<Column>();
            for (int i = 0; i < columnsInMap.size(); i++) {
                Map element = (Map) columnsInMap.get(i);
                int xmlId = (Integer) element.get("XmlColumnId");
                String order = (String) element.get("order");
                int id = (Integer) element.get("id");

                Column col = new Column(xmlId, "DBColumn" + id, order);
                columns.add(col);
            }

            List<Filter> filters = new ArrayList<Filter>();
            for (int i = 0; i < filtersInMap.size(); i++) {
                Map element = (Map) filtersInMap.get(i);
                boolean isRange = (Boolean) element.get("isRange");
                String value = (String) element.get("value");
                String name = (String) element.get("name");

                List values = new ArrayList();
                if (isRange) {
                    values = parseFilterValue(value, ",");
                } else
                    values.add(value);

                ConfigurableFilter filter = new ConfigurableFilter(name, null, null);
                filter.setDbValues(values);

                filters.add(filter);
            }

            int id = (Integer) componentMap.get("XmlComponentId");
            int dbId = (Integer) componentMap.get("id");
            component = new Component(id, "DBComponent" + dbId, columns, null, filters, null);

        } catch (AppLevelException e) {
            e.printStackTrace();
        }
        return component;
    }

    private List parseFilterValue(String value, String token) {
        String[] result = value.split(token);
        return Arrays.asList(result);
    }

    public void saveComponentConfiguration(Map parametersToSave, HttpServletRequest request) {
        log.debug("saveComponentConfiguration('" + parametersToSave + "', javax.servlet.http.HttpServletRequest)");

        ComponentSaveCmd cmd = new ComponentSaveCmd();
        cmd.putParam(parametersToSave);
        try {
            BusinessDelegate.i.execute(cmd, request);
        } catch (AppLevelException e) {
            e.printStackTrace();
        }
    }*/
}
