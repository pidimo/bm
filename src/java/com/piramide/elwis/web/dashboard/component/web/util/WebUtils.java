package com.piramide.elwis.web.dashboard.component.web.util;

import com.piramide.elwis.web.dashboard.component.configuration.structure.*;
import com.piramide.elwis.web.dashboard.component.execute.DataProcessor;
import com.piramide.elwis.web.dashboard.component.execute.Executor;
import com.piramide.elwis.web.dashboard.component.util.Constant;
import com.piramide.elwis.web.dashboard.component.util.ResourceReader;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.util.*;

/**
 * @author : ivan
 */
public class WebUtils {

    public static String buildUrlAccess(Option opt, Map elUrlParameters, HttpServletRequest request, boolean setContext) {

        if (null == opt) {
            return "";
        }

        String context = request.getContextPath();

        String str = opt.getValue();
        String prms = "";
        List<Parameter> urlParams = opt.getParams();

        if (null != urlParams) {
            for (Parameter p : urlParams) {
                if (elUrlParameters.containsKey(p.getName())) {
                    Object v = elUrlParameters.get(p.getName());
                    prms += p.getName() + "=" + v.toString();
                } else if (null != p.getColumnId() && !"".equals(p.getColumnId())) {
                    prms += p.getName() + "=#" + p.getColumnId();
                } else {
                    prms += p.getName() + "=" + p.getValue();
                }

                if (urlParams.indexOf(p) < (urlParams.size() - 1)) {
                    prms += "&";
                }
            }
        }
        if (!"".equals(prms)) {
            str += "?" + prms;
        }
        if (setContext) {
            return context + str;
        } else {
            return str;
        }
    }


    public static List<Column> getAvailableColumns(Component xmlComponent, Component dbComponent) {
        List<Column> result = new ArrayList<Column>();
        for (Column c : xmlComponent.getVisibleColumns()) {
            if (null != dbComponent.getVisibleColumns() && !dbComponent.getVisibleColumns().isEmpty()) {
                if (!dbComponent.containColumn(c)) {
                    result.add(c);
                }
            } else if (!c.isDefaultColumn()) {
                result.add(c);
            }
        }
        return result;
    }

    public static List<Column> getOrderableColumns(Component xmlComponent, Component dbComponent) {
        List<Column> result = new ArrayList<Column>();
        for (Column col : xmlComponent.getOrderableColumns()) {

            Column myColumn = new Column(col.getId(), col.getName(), col.getOrder());
            myColumn.setResourceKey(col.getResourceKey());
            myColumn.setDefaultColumn(col.isDefaultColumn());
            //myColumn.setOrderable(col.isOrderable());
            if (dbComponent.containColumn(col)) {
                Column dbColumn = dbComponent.getColumn(col.getId());
                myColumn.setOrder(dbColumn.getOrder());
            }
            result.add(myColumn);
        }

        return result;
    }

    public static List<Map> processColumns(List<Map> columnsMap, List<Column> columnsObj) {

        List<Map> orderedColumns = new ArrayList<Map>();
        for (Map element : columnsMap) {
            LinkedHashMap map = new LinkedHashMap();

            for (Column column : columnsObj) {
                String key = column.getName();
                Object value = element.get(key);
                if (null != value) {
                    map.put(key, value);
                }
            }
            orderedColumns.add(map);
        }
        return orderedColumns;
    }

    /*public static List<Map> changeConfigurableFilterObjToMap(List<ConfigurableFilter> list) {
        List<Map> result = new ArrayList<Map>();
        for (ConfigurableFilter f : list) {
            Map element = new HashMap();

            element.put(Constant.FILTER_NAME, f.getName());
            element.put(Constant.FILTER_ISRANGE, f.isRangeView());
            element.put(Constant.FILTER_OPERATE_OVER_VIEW, f.getOperateOverView());

            if (null != f.getDbValues() && !f.getDbValues().isEmpty())
                element.put(Constant.FILTER_VALUE, f.getDbValues());
            else {
                List l = new ArrayList();
                l.add(f.getDefaultValue());
                element.put(Constant.FILTER_VALUE, l);
            }
            result.add(element);
        }

        return result;
    }*/

    public static List<Map> changeColumnObjToMap(List<Column> list) {
        List<Map> result = new ArrayList<Map>();

        if (null != list) {
            for (Column c : list) {
                Map element = new HashMap();
                element.put(Constant.COLUMN_NAME, c.getName());
                element.put(Constant.COLUMN_ORDER, c.getOrder());
                element.put(Constant.COLUMN_XMLID, c.getId());
                element.put(Constant.COLUMN_ACCESS, c.getAccessColumn());
                element.put(Constant.COLUMN_INVERSEORDER, c.isInverseOrder());
                result.add(element);
            }
        }
        return result;
    }

    public static List<LabelValueBean> getValuesFromConstantClass(ConfigurableFilter filter, HttpServletRequest request) {
        List<LabelValueBean> list;
        Object[] obj = new Object[]{request};
        try {
            list = (List<LabelValueBean>) ResourceReader.executeMethodOfClass(filter.getClassName(), filter.getMethodName(), obj);
        } catch (ClassCastException cle) {
            throw new RuntimeException("the method: " + filter.getMethodName() + " must be return List<LabelValue> Object. ", cle);
        }
        return list;
    }

    public static List<LabelValueBean> getValuesFromDB(int componentId, ConfigurableFilter filter, Map parameters, PageContext pageContext) {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        if (null == parameters) {
            parameters = new HashMap();
        }

        Map elParameters = WebUtils.evaluateELParametersOfUrls(pageContext, filter.getParameters());

        DataProcessor processor = Executor.getDataProcessor(componentId);
        processor.readFilterContain(componentId, filter, elParameters, parameters);
        List<Map> r = processor.getData();
        if (!r.isEmpty()) {
            result.add(new LabelValueBean("", ""));
        }

        for (Map m : r) {
            String id = (String) m.get(filter.getSelectId());
            String label = (String) m.get(filter.getSelectValue());

            result.add(new LabelValueBean(label, id));
        }
        return result;
    }

    public static Map evaluateELParametersOfUrls(PageContext pageContext,
                                                 List<Parameter> myParams) {
        Map m = new HashMap();
        List<Parameter> params = myParams;
        if (null != params) {
            for (Parameter p : params) {
                String value = p.getValue();
                if (value.indexOf("$") >= 0) {
                    try {
                        Object obj = ElUtils.evaluate(value, "java.lang.Object", pageContext);
                        if (null != obj) {
                            m.put(p.getName(), obj);
                        }
                    } catch (JspException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return m;
    }

    public static List<Parameter> evaluateELParameters(PageContext pageContext, List<Parameter> parameters) {
        List<Parameter> result = new ArrayList<Parameter>();

        if (null != parameters) {
            for (Parameter parameter : parameters) {
                if (parameter.getValue().indexOf("$") >= 0) {
                    try {
                        Object obj = ElUtils.evaluate(parameter.getValue(), "java.lang.Object", pageContext);
                        if (null != obj) {
                            result.add(new Parameter(parameter.getName(), obj.toString()));
                        }
                    } catch (JspException e) {
                        e.printStackTrace();
                    }
                } else {
                    result.add(parameter);
                }
            }
        }

        return result;
    }
}
