package com.piramide.elwis.web.dashboard.component.configuration.structure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : ivan
 */
public class Component implements Prototype {
    private Log log = LogFactory.getLog(this.getClass());

    private int id;
    private String name;
    private String resourceKey;
    private String functionality = "";
    private String permission = "";


    private List<Action> actions = new ArrayList<Action>();
    private List<Column> columns = new ArrayList<Column>();
    private List<Filter> filters = new ArrayList<Filter>();
    private List<Parameter> parameters = new ArrayList<Parameter>();

    private Configuration dashBoardConfiguration;
    private Configuration componentConfiguration;


    public Component() {
    }

    public Component(int id,
                     String name,
                     List<Column> columns,
                     List<Action> actions,
                     List<Filter> filters,
                     Configuration configuration) {

        log.info("Component: " + name);
        this.id = id;
        this.name = name;
        this.columns = columns;
        this.actions = actions;
        this.filters = filters;
        this.dashBoardConfiguration = configuration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public List<StaticFilter> getStaticFilters() {
        List<StaticFilter> result = new ArrayList<StaticFilter>();
        for (Filter filter : filters) {
            if (filter instanceof StaticFilter) {
                result.add((StaticFilter) filter);
            }
        }

        return result;
    }

    public List<ConfigurableFilter> getConfigurableFilters() {
        List<ConfigurableFilter> result = new ArrayList<ConfigurableFilter>();
        for (Filter filter : filters) {
            if (filter instanceof ConfigurableFilter) {
                result.add((ConfigurableFilter) filter);
            }
        }
        return result;
    }

    public RowCounterFilter getRowCounterFilter() {
        RowCounterFilter rowCounterFilter = null;
        for (Filter filter : filters) {
            if (filter instanceof RowCounterFilter) {
                rowCounterFilter = (RowCounterFilter) filter;
            }
        }
        return rowCounterFilter;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getFunctionality() {
        return functionality;
    }

    public void setFunctionality(String functionality) {
        if (null == functionality) {
            this.functionality = "";
        } else {
            this.functionality = functionality;
        }
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        if (null == permission) {
            this.permission = "";
        } else {
            this.permission = permission;
        }
    }


    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Parameter getParameter(String name) {
        if (null != parameters) {
            for (Parameter parameter : parameters) {
                if (parameter.getName().equals(name)) {
                    return parameter;
                }
            }
        }

        return null;
    }

    public List<Column> getVisibleColumns() {
        List<Column> visibleColumns = new ArrayList<Column>();
        for (Column column : columns) {
            if (!column.getAccessColumn()) {
                visibleColumns.add(column);
            }
        }
        return visibleColumns;
    }

    public List<Column> getDefaultSortableColumns() {
        List<Column> result = new ArrayList<Column>();
        for (Column column : columns) {
            if (column.getAccessColumn() && column.isOrderable()) {
                result.add(column);
            }
        }

        return result;
    }

    public List<Column> getAccessColumns() {
        List<Column> accessColumns = new ArrayList<Column>();
        for (Column column : columns) {
            if (column.getAccessColumn()) {
                accessColumns.add(column);
            }
        }

        return accessColumns;
    }

    public boolean containColumn(Column c) {
        for (Column myColumn : columns) {
            if (myColumn.getId() == c.getId()) {
                return true;
            }
        }
        return false;
    }

    public List<Integer> getColumnIds() {
        List<Integer> result = new ArrayList<Integer>();
        for (Column c : columns) {
            result.add(c.getId());
        }

        return result;
    }

    public List<Column> getOrderableColumns() {
        List<Column> result = new ArrayList<Column>();
        for (Column c : columns) {
            if (c.isOrderable()) {
                result.add(c);
            }
        }

        return result;
    }


    public List<Column> getDefaultColumns() {
        List<Column> result = new ArrayList<Column>();
        for (Column c : columns) {
            if (c.isDefaultColumn()) {
                result.add(c);
            }
        }

        return result;
    }

    public Column getColumn(int columnId) {
        for (Column myColumn : columns) {
            if (myColumn.getId() == columnId) {
                return myColumn;
            }
        }
        return null;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public ConfigurableFilter getConfigurableFilter(String name) {
        for (ConfigurableFilter f : getConfigurableFilters()) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    public StaticFilter getStaticFilter(String name) {
        for (StaticFilter f : getStaticFilters()) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }


    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public void addConfigurableFilter(ConfigurableFilter f) {
        filters.add(f);
    }

    public void addStaticFilter(StaticFilter filter) {
        filters.add(filter);
    }

    public void addRowCounterFilter(RowCounterFilter filter) {
        RowCounterFilter rowCounterFilter = getRowCounterFilter();
        if (null == rowCounterFilter) {
            filters.add(rowCounterFilter);
        }
    }

    public Configuration getDashBoardConfiguration() {
        return dashBoardConfiguration;
    }

    public void setDashBoardConfiguration(Configuration dashBoardConfiguration) {
        this.dashBoardConfiguration = dashBoardConfiguration;
    }

    public Configuration getComponentConfiguration() {
        return componentConfiguration;
    }

    public void setComponentConfiguration(Configuration componentConfiguration) {
        this.componentConfiguration = componentConfiguration;
    }

    public String toString() {
        String cad = "Component(name=" + name + " functionality=" + functionality + " permission=" +
                permission + " configuration=" + componentConfiguration + " filters=" + getConfigurableFilters() + ")";
        return cad;
    }

    public Object clone() {
        Component clone = null;
        try {
            clone = (Component) super.clone();

            List<Column> cloneColumns = new ArrayList<Column>();
            for (Column column : columns) {
                Column cloneColumn = (Column) column.clone();
                cloneColumns.add(cloneColumn);
            }
            clone.setColumns(cloneColumns);

            List<Filter> cloneFilters = new ArrayList<Filter>();
            for (ConfigurableFilter filter : getConfigurableFilters()) {
                if (!(filter instanceof RowCounterFilter)) {
                    ConfigurableFilter cloneFilter = (ConfigurableFilter) filter.clone();
                    cloneFilters.add(cloneFilter);
                }
            }

            for (StaticFilter filter : getStaticFilters()) {
                StaticFilter cloneFilter = (StaticFilter) filter.clone();
                cloneFilters.add(cloneFilter);
            }

            RowCounterFilter rowCounterFilter = getRowCounterFilter();
            if (null != rowCounterFilter) {
                RowCounterFilter cloneFilter = (RowCounterFilter) rowCounterFilter.clone();
                cloneFilters.add(cloneFilter);
            }
            clone.setFilters(cloneFilters);

            List<Parameter> cloneParameters = new ArrayList<Parameter>();
            if (null != parameters) {
                for (Parameter parameter : parameters) {
                    Parameter cloneParameter = (Parameter) parameter.clone();
                    cloneParameters.add(cloneParameter);
                }
            }
            clone.setParameters(cloneParameters);

        } catch (CloneNotSupportedException e) {
            log.error("Cannot clone 'Component' object ", e);
        }

        return clone;
    }
}
