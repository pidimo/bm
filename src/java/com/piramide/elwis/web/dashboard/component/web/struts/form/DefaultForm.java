package com.piramide.elwis.web.dashboard.component.web.struts.form;

import com.piramide.elwis.web.dashboard.component.configuration.reader.Builder;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Column;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.configuration.structure.ConfigurableFilter;
import com.piramide.elwis.web.dashboard.component.configuration.structure.StaticFilter;
import com.piramide.elwis.web.dashboard.component.util.Constant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author : ivan
 */
public class DefaultForm extends ValidatorForm {
    private Log log = LogFactory.getLog(this.getClass());

    private Map dto;
    private int xmlComponentId;

    private List<Column> visibleColumns = null;

    public DefaultForm() {
        dto = new HashMap();
    }

    public void setDto(String key, Object value) {
        dto.put(key, value);
    }

    public Object getDto(String key) {
        return dto.get(key);
    }

    public Map getDtoMap() {
        return dto;
    }


    public int getXmlComponentId() {
        return xmlComponentId;
    }

    public void setXmlComponentId(int xmlComponentId) {
        this.xmlComponentId = xmlComponentId;
    }

    public List<Column> getVisibleColumns() {
        return visibleColumns;
    }

    public void setVisibleColumns(List<Column> visibleColumns) {
        this.visibleColumns = visibleColumns;
    }

    public void clearEmptyValues() {
        List keyList = new ArrayList();

        for (Iterator it = dto.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            String value = entry.getValue().toString();
            if ("".equals(value.trim())) {
                keyList.add(entry.getKey());
            }
        }

        for (int i = 0; i < keyList.size(); i++) {
            String key = keyList.get(i).toString();
            dto.remove(key);
        }
    }

    public void removeDefaultValues() {
        clearEmptyValues();
        Component xmlComponent = Builder.i.findComponentById(xmlComponentId);

        List<ConfigurableFilter> filters = xmlComponent.getConfigurableFilters();
        for (ConfigurableFilter filter : filters) {
            if (filter.isRangeView()) {
                String filterName1 = filter.getName() + "_0";
                String filterName2 = filter.getName() + "_1";

                String value1 = String.valueOf(getDto(filterName1));
                String value2 = String.valueOf(getDto(filterName2));

                boolean isEqualToDefaultValues = filter.getInitialValue().equals(value1) &&
                        filter.getFinalValue().equals(value2);

                if (isEqualToDefaultValues) {
                    getDtoMap().remove(filterName1);
                    getDtoMap().remove(filterName2);
                }
            } else {
                String value = String.valueOf(getDto(filter.getName()));
                if (null != filter.getInitialValue() && filter.getInitialValue().equals(value)) {
                    getDtoMap().remove(filter.getName());
                }
            }
        }
    }

    public void addVisibleColumnsToDto() {
        List<Map> l = new ArrayList<Map>();
        int i = 0;
        if (null != getVisibleColumns() && !getVisibleColumns().isEmpty()) {
            for (Column c : getVisibleColumns()) {
                Map map = new HashMap();
                map.put(Constant.COLUMN_XMLID, c.getId());
                map.put(Constant.COLUMN_NAME, c.getName());
                map.put(Constant.COLUMN_POSITION, i);
                l.add(map);
                i++;
            }
        } else {
            log.warn("It did not put values variable visibleColumns " +
                    "in com.piramide.elwis.web.dashboard.component.web.struts.form.DefaultForm");
        }

        if (!l.isEmpty()) {
            setDto(Constant.VISIBLE_COLUMNS, l);
        }
    }

    public void addOrderableColumnsToDto() {
        Component xmlComponent = Builder.i.findComponentById(xmlComponentId);
        List<Column> columns = xmlComponent.getVisibleColumns();

        List<Map> l = new ArrayList<Map>();
        for (Column column : columns) {
            Map map = new HashMap();
            if (column.isOrderable() && getDtoMap().containsKey(column.getName())) {
                String value = getDto(column.getName()).toString();
                if (null != value && "".equals(value.trim())) {
                    value = "NONE";
                }
                map.put(Constant.COLUMN_NAME, column.getName());
                map.put(Constant.COLUMN_ORDER, value);
                map.put(Constant.COLUMN_XMLID, column.getId());

                l.add(map);
            }
        }

        setDto(Constant.ORDERABLE_COLUMNS, l);
    }

    public void addFiltersToDto() {
        Component xmlComponent = Builder.i.findComponentById(xmlComponentId);
        List<ConfigurableFilter> filters = xmlComponent.getConfigurableFilters();
        List<StaticFilter> staticFilters = xmlComponent.getStaticFilters();

        List<Map> filtersMapList = new ArrayList<Map>();
        for (ConfigurableFilter filter : filters) {
            Map map = new HashMap();
            if (filter.isRangeView()) {
                if (getDtoMap().containsKey(filter.getName() + "_0") &&
                        getDtoMap().containsKey(filter.getName() + "_1")) {
                    map.put(Constant.FILTER_NAME, filter.getName());
                    String v1 = (String) getDto(filter.getName() + "_0");
                    String v2 = (String) getDto(filter.getName() + "_1");
                    map.put(Constant.FILTER_VALUE, v1 + "," + v2);
                    map.put(Constant.FILTER_ISRANGE, true);
                }
            } else {
                if (getDtoMap().containsKey(filter.getName())) {
                    map.put(Constant.FILTER_NAME, filter.getName());
                    map.put(Constant.FILTER_VALUE, getDto(filter.getName()));
                    map.put(Constant.FILTER_ISRANGE, false);
                }
            }

            if (!map.isEmpty()) {
                filtersMapList.add(map);
            }
        }

        //static filters
        List<Map> staticFiltersMapList = new ArrayList<Map>();
        for (StaticFilter staticFilter : staticFilters) {
            if (getDtoMap().containsKey(staticFilter.getName())) {
                Map map = new HashMap();
                map.put(Constant.FILTER_NAME, staticFilter.getName());
                map.put(Constant.FILTER_VALUE, getDto(staticFilter.getName()));

                staticFiltersMapList.add(map);
            }
        }

        setDto(Constant.FILTERS, filtersMapList);
        setDto(Constant.STATIC_FILTERS, staticFiltersMapList);

    }


    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.xmlComponentId = new Integer(request.getParameter("componentId"));
    }
}
