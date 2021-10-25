package com.piramide.elwis.web.reports.el;

import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorConstants;
import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorHelper;
import com.jatun.titus.listgenerator.Titus;
import com.jatun.titus.listgenerator.structure.*;
import com.jatun.titus.listgenerator.structure.converter.Converter;
import com.jatun.titus.listgenerator.structure.converter.ConverterManager;
import com.jatun.titus.listgenerator.structure.converter.FieldValue;
import com.jatun.titus.listgenerator.structure.converter.ResultValue;
import com.jatun.titus.listgenerator.structure.dynamiccolumn.DynamicColumnFieldNotFoundException;
import com.jatun.titus.listgenerator.structure.externalbuilder.CompoundColumn;
import com.jatun.titus.listgenerator.structure.filter.ConstantFilter;
import com.jatun.titus.listgenerator.structure.filter.DBFilter;
import com.jatun.titus.listgenerator.structure.filter.Filter;
import com.jatun.titus.listgenerator.structure.filter.ResourceBundleWrapper;
import com.jatun.titus.listgenerator.structure.type.DBType;
import com.jatun.titus.listgenerator.util.FantabulousExecuteUtil;
import com.jatun.titus.listgenerator.util.TitusPathUtil;
import com.jatun.titus.reportgenerator.util.Pair;
import com.jatun.titus.reportgenerator.util.ReportGeneratorConstants;
import com.jatun.titus.web.StrutsResourceBundle;
import com.jatun.titus.web.util.TitusJSPHelper;
import com.piramide.elwis.cmd.reports.*;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.utils.SortUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.JavaScriptEncoder;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.reports.action.FilterExternalListAction;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.controller.Controller;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.exception.ListStructureNotFoundException;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGenerator;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGeneratorManager;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.joda.time.DateTimeZone;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.util.*;

/**
 * Jatun s.r.l.
 * report manager jstl functions
 *
 * @author miky
 * @version $Id: Functions.java 10342 2013-03-29 00:12:40Z miguel $
 */
public class Functions {
    private static Log log = LogFactory.getLog(Functions.class);

    /**
     * show view operators to filter value
     *
     * @return map
     */
    public static Map getOpViewsMap() {
        Map map;

        map = new HashMap();
        //common
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_EQUAL), ReportConstants.SHOW_ONE_BOX);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_NOT_EQUAL), ReportConstants.SHOW_ONE_BOX);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_BETWEEN), ReportConstants.SHOW_TWO_BOX);
        //text
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_CONTAIN), ReportConstants.SHOW_ONE_BOX);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_START_WITH), ReportConstants.SHOW_ONE_BOX);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_END_WITH), ReportConstants.SHOW_ONE_BOX);
        //number
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_LESS_THAN), ReportConstants.SHOW_ONE_BOX);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_GREATER_THAN), ReportConstants.SHOW_ONE_BOX);
        //relation
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_IS), ReportConstants.SHOW_SELECT);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_ONE_OF), ReportConstants.SHOW_MULTIPLESELECT);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_IS_NOT), ReportConstants.SHOW_SELECT);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_NOT_ONE_OF), ReportConstants.SHOW_MULTIPLESELECT);
        //date
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_ON), ReportConstants.SHOW_ONE_BOX);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_BEFORE), ReportConstants.SHOW_ONE_BOX);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_AFTER), ReportConstants.SHOW_ONE_BOX);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_NOT_ON), ReportConstants.SHOW_ONE_BOX);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_YESTERDAY), ReportConstants.SHOW_EMPTY);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_TODAY), ReportConstants.SHOW_EMPTY);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_TOMORROW), ReportConstants.SHOW_EMPTY);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_LAST_7_DAYS), ReportConstants.SHOW_EMPTY);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_NEXT_7_DAYS), ReportConstants.SHOW_EMPTY);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_LAST_MONTH), ReportConstants.SHOW_EMPTY);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_THIS_MONTH), ReportConstants.SHOW_EMPTY);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_NEXT_MONTH), ReportConstants.SHOW_EMPTY);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_LAST_30_DAYS), ReportConstants.SHOW_EMPTY);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_NEXT_30_DAYS), ReportConstants.SHOW_EMPTY);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_LAST_YEAR), ReportConstants.SHOW_EMPTY);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_THIS_YEAR), ReportConstants.SHOW_EMPTY);
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_NEXT_YEAR), ReportConstants.SHOW_EMPTY);

        return map;
    }

    /**
     * get resource operators
     *
     * @return map
     */
    public static Map getOpResourceMap() {
        Map map;
        map = new HashMap();
        //common
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_EQUAL), "Report.filter.op.equal");//common
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_NOT_EQUAL), "Report.filter.op.notEqual");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_BETWEEN), "Report.filter.op.between");
        //text
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_CONTAIN), "Report.filter.op.contain");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_START_WITH), "Report.filter.op.startWith");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_END_WITH), "Report.filter.op.endWith");
        //number
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_LESS_THAN), "Report.filter.op.lessThan");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_GREATER_THAN), "Report.filter.op.greaterThan");
        //relation
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_IS), "Report.filter.op.is");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_ONE_OF), "Report.filter.op.oneOf");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_IS_NOT), "Report.filter.op.isNot");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_NOT_ONE_OF), "Report.filter.op.notOneOf");
        //date
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_ON), "Report.filter.op.on");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_BEFORE), "Report.filter.op.before");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_AFTER), "Report.filter.op.after");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_NOT_ON), "Report.filter.op.notOn");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_YESTERDAY), "Report.filter.op.yesterday");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_TODAY), "Report.filter.op.today");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_TOMORROW), "Report.filter.op.tomorrow");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_LAST_7_DAYS), "Report.filter.op.last7Days");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_NEXT_7_DAYS), "Report.filter.op.next7Days");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_LAST_MONTH), "Report.filter.op.lastMonth");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_THIS_MONTH), "Report.filter.op.thisMonth");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_NEXT_MONTH), "Report.filter.op.nextMonth");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_LAST_30_DAYS), "Report.filter.op.last30Days");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_NEXT_30_DAYS), "Report.filter.op.next30Days");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_LAST_YEAR), "Report.filter.op.lastYear");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_THIS_YEAR), "Report.filter.op.thisYear");
        map.put(String.valueOf(StructureConstants.FilterTitusType.OP_NEXT_YEAR), "Report.filter.op.nextYear");

        return map;
    }

    /**
     * put message to columns
     *
     * @param columnMapList columns of execute ColumnReadCmd
     * @param sRequest
     * @return List of columns with message
     */
    public static List getColumnsWithMessage(List columnMapList, ServletRequest sRequest) {
        log.debug("Excecuting getColumnsWithMessage method......");
        HttpServletRequest request = (HttpServletRequest) sRequest;
        List result = new ArrayList();
        for (Iterator iterator = columnMapList.iterator(); iterator.hasNext();) {
            Map map = (Map) iterator.next();
            String titusPath = map.get("path").toString();
            String columnLabel = (map.containsKey("label") ? (String) map.get("label") : null);
            String label = composeColumnLabelByTitusPath(titusPath, columnLabel, request);

            if (map.containsKey("columnOrder")) {
                Boolean order = (Boolean) map.get("columnOrder");
                String orderMessage = null;
                for (Iterator iterator2 = JSPHelper.getColumnGroupOrder(request).iterator(); iterator2.hasNext();) {
                    LabelValueBean item = (LabelValueBean) iterator2.next();
                    if (item.getValue().equals(order.toString())) {
                        orderMessage = item.getLabel();
                        break;
                    }
                }
                if (orderMessage != null) {
                    label = label + "   (" + orderMessage + ")";
                }
            }
            result.add(new LabelValueBean(label, map.get("value").toString()));
        }

        return result;
    }

    /**
     * compose colun label with strucuture resources by titus path
     *
     * @param titusPath
     * @param label
     * @param request
     * @return compose label
     */
    public static String composeColumnLabelByTitusPath(String titusPath, String label, HttpServletRequest request) {

        log.debug("Executing composeColumnLabelByTitusPath('" + titusPath + "', '" + label + "', javax.servlet.http.HttpServletRequest)");
        //log.debug("Executing composeColumnLabelByTitusPath........"+titusPath);

        StringBuffer res = new StringBuffer();
        StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());

        String tableMsg;
        String columnMsg;
        if (TitusPathUtil.isDynamicColumn(titusPath)) {
            Map tableResourceFieldLabel = null;
            try {
                tableResourceFieldLabel = TitusPathUtil.getResourceTableAndFieldLabelDynamicColumn(titusPath, structureManager, getCategoryDynamicColumnParams(request));
                tableMsg = JSPHelper.getMessage(request, String.valueOf(tableResourceFieldLabel.get(TitusPathUtil.TABLERESOURCE_KEY)));
                columnMsg = String.valueOf(tableResourceFieldLabel.get(TitusPathUtil.FIELDLABEL));
                if (label != null && label.equals(columnMsg)) {
                    label = null;
                }
            } catch (DynamicColumnFieldNotFoundException e) {
                log.debug("Error in found dynamic field..." + e);
                return JSPHelper.getMessage(request, "Report.columns.dynamicColumn.deleted");
            }

        } else {
            Map tableFieldResource = TitusPathUtil.getResourceToTableAndField(titusPath, structureManager);
            //set messages
            tableMsg = JSPHelper.getMessage(request, String.valueOf(tableFieldResource.get(TitusPathUtil.TABLERESOURCE_KEY)));
            columnMsg = JSPHelper.getMessage(request, String.valueOf(tableFieldResource.get(TitusPathUtil.FIELDRESOURCE_KEY)));
        }

        if (label != null && label.trim().length() > 0) {
            res.append(label);
            res.append(" [");
            res.append(tableMsg + ":" + columnMsg);
            res.append("]");
        } else {
            res.append(tableMsg + ":" + columnMsg);
        }
        return res.toString();
    }

    /**
     * compose report filter operator value to management in the view
     *
     * @param operator
     * @return String
     */
    public static String composeOperatorValue(String operator, String tableName, String fieldName, ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        StringBuffer value = new StringBuffer();
        StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
        Field field = structureManager.getField(tableName, fieldName);
        Filter filter = field.getFilter();

        Map opViewMap = getOpViewsMap();
        value.append(ReportConstants.KEY_SEPARATOR_OP).
                append(operator).
                append(ReportConstants.KEY_SEPARATOR_OP).

                append(ReportConstants.KEY_SEPARATOR_SHOWVIEW);
        if (isDbFilter(filter) && !isDbFilterInSelect(filter)
                && (operator.equals(String.valueOf(StructureConstants.FilterTitusType.OP_IS))
                || operator.equals(String.valueOf(StructureConstants.FilterTitusType.OP_IS_NOT)))) {
            value.append(ReportConstants.SHOW_POPUP);
        } else {
            value.append(opViewMap.get(operator));
        }
        value.append(ReportConstants.KEY_SEPARATOR_SHOWVIEW);

        //additional values
        if (field.getType().getBaseType() == DBType.DBBaseType.BASETYPE_DATE) {
            value.append(ReportConstants.KEY_ISDATETYPE);
        }
        if (field.getType().getBaseType() == DBType.DBBaseType.BASETYPE_NUMERIC) {
            value.append(ReportConstants.KEY_ISNUMERICTYPE);
        }

        return value.toString();
    }

    /**
     * get operator message
     *
     * @param operator
     * @param servletRequest
     * @return String
     */
    public static String getOperatorMenssage(String operator, ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String msg;
        Map resourceMap = getOpResourceMap();
        msg = (resourceMap.containsKey(operator) ? JSPHelper.getMessage(request, resourceMap.get(operator).toString()) : "");
        return msg;
    }

    /**
     * compose filter value to show in the view
     *
     * @param filterValue
     * @param operator
     * @param tableName
     * @param fieldName
     * @param fieldPath
     * @param columnType
     * @param filterType
     * @param servletRequest
     * @return String
     */
    public static String composeFilterValueToView(String filterValue, String operator, String tableName, String fieldName, String fieldPath, String columnType, String filterType, ServletRequest servletRequest) {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String res = "";

        if (!GenericValidator.isBlankOrNull(filterValue)) {
            //separator
            String separator = JSPHelper.getMessage(request, "Common.and");
            if (isOperatorWithMultipleValues(operator)) {
                separator = ",";
            }

            if (filterType.equals(ReportConstants.FILTER_WITH_DB_VALUE.toString())) {
                //is db filter
                StructureManager manager = Titus.getStructureManager(request.getSession().getServletContext());

                String[] valuesArray = filterValue.split(ReportConstants.FILTERVALUE_SEPARATOR_REGULAREXP);
                List valuesList = Arrays.asList(valuesArray);

                for (Iterator iterator = valuesList.iterator(); iterator.hasNext();) {
                    String pkValues = (String) iterator.next();
                    res += parserDbFilterValue(pkValues, tableName, fieldName, fieldPath, manager, false, request);
                    if (iterator.hasNext()) {
                        res += " " + separator + " ";
                    }
                }

            } else {

                filterValue = parserFilterValue(filterValue, fieldPath, filterType, true, request);
                boolean isfilterConstant = isConstantFilter(tableName, fieldName, request);

                String[] valuesArray = filterValue.split(ReportConstants.FILTERVALUE_SEPARATOR_REGULAREXP);
                List valuesList = Arrays.asList(valuesArray);

                for (Iterator iterator = valuesList.iterator(); iterator.hasNext();) {
                    String strValue = (String) iterator.next();
                    if (isfilterConstant) {
                        List filterConstantList = getFilterConstantValues(tableName, fieldName, request);
                        for (Iterator iterator2 = filterConstantList.iterator(); iterator2.hasNext();) {
                            LabelValueBean labelValueBean = (LabelValueBean) iterator2.next();
                            if (strValue.equals(labelValueBean.getValue())) {
                                strValue = labelValueBean.getLabel();
                                break;
                            }
                        }
                    }
                    res += strValue;
                    if (iterator.hasNext()) {
                        res += " " + separator + " ";
                    }
                }
            }
        }

        return res;
    }

    /**
     * parser filter value for column type
     *
     * @param filterValue
     * @param fieldPath
     * @param servletRequest
     * @return String
     */

    public static String parserFilterValue(String filterValue, String fieldPath, String filterType, boolean isToView, ServletRequest servletRequest) {
        log.debug("Executing parserFilterValue method..............." + filterValue);

        String newValue = "";

        if (!GenericValidator.isBlankOrNull(filterValue)) {

            HttpServletRequest request = (HttpServletRequest) servletRequest;
            StructureManager manager = Titus.getStructureManager(request.getSession().getServletContext());
            Field field = manager.getFieldByTitusPath(fieldPath);
            if (filterType.equals(ReportConstants.FILTER_WITH_DB_VALUE.toString())) {
                //is db filter
                if (filterValue.indexOf(ReportConstants.FILTERVALUE_SEPARATOR) == -1) {
                    //only to dbfilter with operator is and in popup parser the value of field (label)
                    newValue = parserDbFilterValue(filterValue, field.getParentTable().getName(), field.getName(), fieldPath, manager, true, request);
                } else {
                    newValue = filterValue;
                }

            } else if (isConstantFilter(field.getParentTable().getName(), field.getName(), request)) {
                newValue = filterValue;
            } else {

                int baseType = field.getType().getBaseType();
                if (field.getConverter() != null && (isToView || baseType != DBType.DBBaseType.BASETYPE_NUMERIC ||
                        field.getType().getType() == DBType.DBTypeNameAsInt.DECIMAL)) {

                    if (filterValue.indexOf(ReportConstants.FILTERVALUE_SEPARATOR) != -1) {
                        String[] valuesArray = filterValue.split(ReportConstants.FILTERVALUE_SEPARATOR_REGULAREXP);
                        List valuesList = Arrays.asList(valuesArray);
                        for (Iterator iterator = valuesList.iterator(); iterator.hasNext();) {
                            String strValue = (String) iterator.next();
                            newValue += applyConverterDbToViewInSimpleField(fieldPath, strValue, request);

                            if (iterator.hasNext()) {
                                newValue += ReportConstants.FILTERVALUE_SEPARATOR;
                            }
                        }
                    } else {
                        newValue = applyConverterDbToViewInSimpleField(fieldPath, filterValue, request);
                    }
                } else {
                    newValue = filterValue;
                }
            }
        }

        log.debug("NEW value parsed......:" + newValue);
        return newValue;
    }

    /**
     * get view filter value of an operator
     *
     * @param operator
     * @return String
     */
    public static String getViewFilterValue(String operator) {
        return (String) getOpViewsMap().get(operator);
    }

    /**
     * parser integer to date with an date pattern
     *
     * @param integerDate
     * @param datePattern
     * @return String
     */
    public static String parseIntegerToDate(String integerDate, String datePattern) {
        String value = null;
        Date date;
        log.debug("date as integer..... " + integerDate);
        if (!GenericValidator.isBlankOrNull(integerDate)) {
            date = DateUtils.integerToDate(new Integer(integerDate));
            value = DateUtils.parseDate(date, datePattern);
        }
        return value;
    }

    /**
     * get constant filter values of an field from the structure
     *
     * @param tableName
     * @param fieldName
     * @param servletRequest
     * @return list
     */
    public static List getFilterConstantValues(String tableName, String fieldName, ServletRequest servletRequest) {
        List result = new ArrayList();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
        Field field = structureManager.getField(tableName, fieldName);

        Filter filter = field.getFilter();
        if (filter != null && filter.getType() == ConstantFilter.CONSTANT_FILTER_TYPE) {
            ConstantFilter constantFilter = (ConstantFilter) filter;
            Map valuesMap = (Map) constantFilter.getValue();

            for (Iterator iterator = valuesMap.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                String viewLabel;
                if (constantFilter.getConstantValueType() == StructureConstants.FilterTypeValueAsInt.RESOURCE.intValue()) {
                    viewLabel = JSPHelper.getMessage(request, valuesMap.get(key).toString());
                } else {
                    viewLabel = valuesMap.get(key).toString();
                }

                result.add(new LabelValueBean(viewLabel, key));
            }
        }
        return result;
    }

    /**
     * verif if an field have an constant filter object
     *
     * @param tableName
     * @param fieldName
     * @return true or false
     */
    public static boolean isConstantFilter(String tableName, String fieldName, ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
        Field field = structureManager.getField(tableName, fieldName);
        Filter filter = field.getFilter();
        if (filter != null && filter.getType() == ConstantFilter.CONSTANT_FILTER_TYPE) {
            return true;
        }
        return false;
    }

    /**
     * verif if is an DBFilter filter object
     *
     * @param filter
     * @return true or false
     */
    public static boolean isDbFilter(Filter filter) {
        if (filter != null && filter.getType() == DBFilter.DB_FILTER_TYPE) {
            return true;
        }
        return false;
    }

    /**
     * verif if is an DBFilter filter object
     *
     * @param tableName
     * @param fieldName
     * @return true or false
     */
    public static boolean isDbFilter(String tableName, String fieldName, ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
        Field field = structureManager.getField(tableName, fieldName);
        Filter filter = field.getFilter();
        if (filter != null && filter.getType() == DBFilter.DB_FILTER_TYPE) {
            return true;
        }
        return false;
    }

    /**
     * get filter operators for an column type
     *
     * @param tableName
     * @param fieldName
     * @param request
     * @return List of operators to this field
     */

    public static List getReportFilterOperators(String tableName, String fieldName, HttpServletRequest request) {
        log.debug("#######################################");
        log.debug("tableName  " + tableName);
        log.debug("FieldName  " + fieldName);
        List result = new ArrayList();
        StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
        Field field = structureManager.getField(tableName, fieldName);
        List operators = composeOperators(field, request);

        for (Iterator iterator = operators.iterator(); iterator.hasNext();) {
            Map operatorMap = (Map) iterator.next();
            String label = (String) operatorMap.get(ReportConstants.KEY_OPLABEL);
            String value = composeOperatorValue(operatorMap.get(ReportConstants.KEY_OPERATOR).toString(), tableName, fieldName, request);
            result.add(new LabelValueBean(label, value));
        }

        return result;
    }

    public static List getReportQueryParamOperators(String tableName, String fieldName, HttpServletRequest request) {
        List<LabelValueBean> queryParamOperators = new ArrayList<LabelValueBean>();
        List<LabelValueBean> filterOperators = getReportFilterOperators(tableName, fieldName, request);

        for (LabelValueBean labelValueBean : filterOperators) {

            String operator = getValueByKey(labelValueBean.getValue(), ReportConstants.KEY_SEPARATOR_OP);
            if (String.valueOf(StructureConstants.FilterTitusType.OP_EQUAL).equals(operator)
                || String.valueOf(StructureConstants.FilterTitusType.OP_IS).equals(operator)
                || String.valueOf(StructureConstants.FilterTitusType.OP_ON).equals(operator)) {

                queryParamOperators.add(labelValueBean);
            }
        }
        return queryParamOperators;
    }

    /**
     * text parser, get substring that be between the key text
     *
     * @param text text to parser
     * @param key  key to parser
     * @return String data
     */
    public static String getValueByKey(String text, String key) {
        String res = null;
        int firstIndex = text.indexOf(key);
        int lastIindex = text.lastIndexOf(key);
        if (firstIndex != lastIindex) {
            res = text.substring((firstIndex + key.length()), lastIindex).trim();
        }
        return res;
    }

    /**
     * compose column operator for base type
     *
     * @param field
     * @param request
     * @return List
     */

    private static List composeOperators(Field field, HttpServletRequest request) {
        List result = new ArrayList();
        Filter filter = field.getFilter();
        Map opResourceMap = getOpResourceMap();
        Map opViewMap = getOpViewsMap();
        boolean addCommonOperators = true;

        if (isDbFilter(filter)) {

            if (isDbFilterInSelect(filter)) {
                result.addAll(getSelectViewOperators(opResourceMap, request));
            } else {
                Map map = new HashMap();
                map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_IS));
                map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_IS)).toString()));
                result.add(map);

                map = new HashMap();
                map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_IS_NOT));
                map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_IS_NOT)).toString()));
                result.add(map);
            }

            if (field.getClass().equals(CompoundField.class)) {
                addCommonOperators = false;
            }
        }

        if (isConstantFilter(field.getParentTable().getName(), field.getName(), request)) {
            result.addAll(getSelectViewOperators(opResourceMap, request));
            addCommonOperators = false;
        }

        if (addCommonOperators) {
            if (field.getType().getBaseType() == DBType.DBBaseType.BASETYPE_STRING) {
                result.addAll(getTextOperators(opResourceMap, opViewMap, request));
            } else if (field.getType().getBaseType() == DBType.DBBaseType.BASETYPE_NUMERIC) {
                result.addAll(getNumberOperators(opResourceMap, opViewMap, request));
            } else if (field.getType().getBaseType() == DBType.DBBaseType.BASETYPE_DATE) {
                result.addAll(getDateOperators(opResourceMap, opViewMap, request));
            }
        }
        return result;
    }

    /**
     * Select view operators
     *
     * @param opResourceMap
     * @param request
     * @return List
     */
    private static List getSelectViewOperators(Map opResourceMap, HttpServletRequest request) {
        List result = new ArrayList();
        Map map;

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_IS));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_IS)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_IS_NOT));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_IS_NOT)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_ONE_OF));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_ONE_OF)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_NOT_ONE_OF));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_NOT_ONE_OF)).toString()));
        result.add(map);

        return result;
    }

    /**
     * text operators
     *
     * @param request
     * @return List
     */
    private static List getTextOperators(Map opResourceMap, Map opViewMap, HttpServletRequest request) {

        List result = new ArrayList();
        Map map;

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_EQUAL));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_EQUAL)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_CONTAIN));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_CONTAIN)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_START_WITH));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_START_WITH)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_END_WITH));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_END_WITH)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_NOT_EQUAL));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_NOT_EQUAL)).toString()));
        result.add(map);

        return result;
    }

    /**
     * number operators
     *
     * @param request
     * @return List
     */
    private static List getNumberOperators(Map opResourceMap, Map opViewMap, HttpServletRequest request) {
        List result = new ArrayList();
        Map map;

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_EQUAL));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_EQUAL)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_LESS_THAN));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_LESS_THAN)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_GREATER_THAN));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_GREATER_THAN)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_BETWEEN));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_BETWEEN)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_NOT_EQUAL));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_NOT_EQUAL)).toString()));
        result.add(map);

        return result;
    }

    /**
     * date operators
     *
     * @param request
     * @return List
     */
    private static List getDateOperators(Map opResourceMap, Map opViewMap, HttpServletRequest request) {
        List result = new ArrayList();
        Map map;

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_ON));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_ON)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_BEFORE));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_BEFORE)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_AFTER));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_AFTER)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_BETWEEN));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_BETWEEN)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_NOT_ON));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_NOT_ON)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_YESTERDAY));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_YESTERDAY)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_TODAY));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_TODAY)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_TOMORROW));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_TOMORROW)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_LAST_7_DAYS));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_LAST_7_DAYS)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_NEXT_7_DAYS));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_NEXT_7_DAYS)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_LAST_MONTH));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_LAST_MONTH)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_THIS_MONTH));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_THIS_MONTH)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_NEXT_MONTH));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_NEXT_MONTH)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_LAST_30_DAYS));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_LAST_30_DAYS)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_NEXT_30_DAYS));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_NEXT_30_DAYS)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_LAST_YEAR));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_LAST_YEAR)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_THIS_YEAR));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_THIS_YEAR)).toString()));
        result.add(map);

        map = new HashMap();
        map.put(ReportConstants.KEY_OPERATOR, new Integer(StructureConstants.FilterTitusType.OP_NEXT_YEAR));
        map.put(ReportConstants.KEY_OPLABEL, JSPHelper.getMessage(request, opResourceMap.get(String.valueOf(StructureConstants.FilterTitusType.OP_NEXT_YEAR)).toString()));
        result.add(map);

        return result;
    }

    /**
     * get map value by key
     *
     * @param map
     * @param key
     * @return String
     */
    public static String getMapValueByKey(Map map, Object key) {
        log.debug("Executing method getMapValueByKey............................................" + key);
        String res = null;
        if (map != null && map.containsKey(key)) {
            res = map.get(key).toString();
        }
        return res;
    }

    /**
     * get primary key value to an db filter
     *
     * @param resultMap
     * @param primaryKeyAliasList list of alias of primary key
     * @return String, compound primary key value
     */
    public static String getPrimaryKeyValue(Map resultMap, List primaryKeyAliasList) {
        log.debug("Executing method getPrimaryKeyValue............................................" + primaryKeyAliasList);
        log.debug("RESULTMAP:" + resultMap);
        String res = null;
        if (resultMap != null) {
            res = "";
            for (Iterator iterator = primaryKeyAliasList.iterator(); iterator.hasNext();) {
                String key = iterator.next().toString();
                if (resultMap.containsKey(key)) {
                    res = res + resultMap.get(key);
                    if (iterator.hasNext()) {
                        res = res + ReportConstants.PRIMARYKEY_SEPARATOR;
                    }
                }
            }
        }
        return res;
    }

    /**
     * encode characters '<' '>'
     *
     * @param path
     * @return String
     */
    public static String encodeFieldPath(String path) {
        path = path.replaceAll("<", "&lt;");
        path = path.replaceAll(">", "&gt;");
        return path;
    }

    /**
     * decode charaters '&lt;' '&gt;'
     *
     * @param path
     * @return String
     */
    public static String decodeFieldPath(String path) {
        path = path.replaceAll("&lt;", "<");
        path = path.replaceAll("&gt;", ">");
        return path;
    }

    /**
     * get the value in base to id of an column of the data base, filterValue save the id value of the column
     *
     * @param filterValue
     * @param tableName
     * @param fieldName
     * @param manager
     * @param withIdValues
     * @return String
     */
    private static String parserDbFilterValue(String filterValue, String tableName, String fieldName, String fieldPath, StructureManager manager, boolean withIdValues, HttpServletRequest request) {
        log.debug("Executing parserDbFilterValue......................" + filterValue);

        String res = null;

        List idValuesList = new ArrayList();
        if (filterValue.indexOf(ReportConstants.PRIMARYKEY_SEPARATOR) != -1) {
            String[] pkValues = filterValue.split(ReportConstants.PRIMARYKEY_SEPARATOR_REGULAREXP);
            idValuesList = Arrays.asList(pkValues);
        } else {
            idValuesList.add(filterValue);
        }
        log.debug("IDVALUES::" + idValuesList);

        Table mainTable;
        Table table = manager.getTable(tableName);
        Field field = table.getField(fieldName);
        String relationFilterTableName = TitusPathUtil.getTableNameOwner(fieldPath);
        //verif if is relation filter
        boolean isRelationFilterTable;
        if (!relationFilterTableName.equals(table.getName())) {
            isRelationFilterTable = true;
            mainTable = manager.getTable(relationFilterTableName);
            log.debug("PATH-ANTES:" + fieldPath);
            fieldPath = TitusPathUtil.getTablePathOwner(fieldPath);
            log.debug("PATH-DESPUES:" + fieldPath);
        } else {
            isRelationFilterTable = false;
            fieldPath = TitusPathUtil.titusFieldPathCompleter(field.getName(), field.getParentTable().getName());
            mainTable = table;
        }

        FantabulousExecuteUtil fantabulousExecuteUtil = new FantabulousExecuteUtil();
        List resultList = fantabulousExecuteUtil.getColumnsByPrimaryKeyIdValues(field, fieldPath, mainTable, idValuesList, manager, isRelationFilterTable);

        log.debug("EXECUTED-RESULTTTT-LIST:" + resultList);

        if (resultList.size() > 0) {
            if (field.getConverter() != null) {

                //apply converter
                Map converterParamsMap = new HashMap();
                Pair[] aliasPairs = new Pair[resultList.size()];
                int cont = 0;
                for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
                    Map resultMap = (Map) iterator.next();
                    converterParamsMap.put(resultMap.get("columnPath"), resultMap.get("columnAlias"));

                    aliasPairs[cont] = new Pair(resultMap.get("columnAlias"), resultMap.get("columnValue"));
                    cont++;
                }

                ConverterManager converterManager = Titus.getConverterManager(request.getSession().getServletContext());
                Converter converter = converterManager.newConverterInstance(field.getConverter());

                Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
                User user = RequestUtils.getUser(request);
                DateTimeZone timeZone = (DateTimeZone) user.getValue("dateTimeZone");
                ResourceBundleWrapper resourceBundleWrapper = new StrutsResourceBundle(Constants.APPLICATION_RESOURCES, locale);

                Map parameters = new HashMap();
                parameters.put("locale", locale);
                parameters.put("timezone", timeZone);
                parameters.put(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP, converterParamsMap);
                if (field.getType().getType() == DBType.DBTypeNameAsInt.DATETIME) {
                    parameters.put(ReportConstants.LONGDATE_AS_SHORT_KEY, ReportConstants.LONGDATE_AS_SHORT_VALUE);
                }

                FieldValue fieldValue = converter.dbToView(field, parameters, aliasPairs, resourceBundleWrapper, fieldPath);
                res = fieldValue.getShowable();

            } else if (resultList.size() == 1) {

                Map map = (Map) resultList.get(0);
                res = (String) map.get("columnValue");
            }
        }

        if (res != null) {
            if (withIdValues) {
                StringBuffer sb = new StringBuffer();
                sb.append(ReportConstants.KEY_SEPARATOR_LABEL)
                        .append(JavaScriptEncoder.encode(res))
                        .append(ReportConstants.KEY_SEPARATOR_LABEL)
                        .append(ReportConstants.KEY_SEPARATOR_VALUE)
                        .append(filterValue)
                        .append(ReportConstants.KEY_SEPARATOR_VALUE);
                res = sb.toString();
            }
        }
        return res;
    }

    /**
     * get report page sizes from TitusJSPHelper
     *
     * @param servletRequest
     * @return List
     */
    public static List getReportPageSizes(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String locale = request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString();
        return TitusJSPHelper.getReportPageSizes(locale);
    }

    /**
     * get report document formats from TitusJSPHelper
     *
     * @param servletRequest
     * @return List
     */
    public static List getReportFormats(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String locale = request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString();
        //remove rtf format
        List reportFormats = TitusJSPHelper.getReportFormats(locale);
        for (Iterator iterator = reportFormats.iterator(); iterator.hasNext();) {
            Map map = (Map) iterator.next();
            if (ReportGeneratorConstants.REPORT_FORMAT_RTF.equals(map.get("reportFormat"))) {
                iterator.remove();
            }
        }
        return reportFormats;
    }

    /**
     * Gets the page orientations
     *
     * @param servletRequest
     * @return List
     */
    public static List getReportPageOrientations(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String locale = request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString();
        return TitusJSPHelper.getReportPageOrientations(locale);
    }

    public static List getCsvFieldDelimiters() {
        ArrayList list = new ArrayList();

        for (int i = 0; i < ReportConstants.CSVFieldDelimiter.values().length; i++) {
            ReportConstants.CSVFieldDelimiter csvFieldDelimiter = ReportConstants.CSVFieldDelimiter.values()[i];
            list.add(new LabelValueBean(csvFieldDelimiter.getDelimiter(), csvFieldDelimiter.getDelimiter()));
        }
        return list;
    }

    public static List getReportCharsetList(HttpServletRequest request) {
        ArrayList list = new ArrayList();

        for (int i = 0; i < ReportConstants.ReportCharset.values().length; i++) {
            ReportConstants.ReportCharset charset = ReportConstants.ReportCharset.values()[i];
            list.add(new LabelValueBean(JSPHelper.getMessage(request, charset.getResource()), charset.getConstant()));
        }
        return list;
    }

    public static String readUserCsvDelimiterConfig(HttpServletRequest request) {
        return ReportSettingUtil.readUserCsvDelimiterConfig(request);
    }

    public static String readUserReportCharsetConfig(HttpServletRequest request) {
        return ReportSettingUtil.readUserReportCharsetConfig(request);
    }

    /**
     * applied compoun column converter
     *
     * @param ListResultMap     result map of an fantabulous list
     * @param compoundColumnObj compoun column object
     * @param aliasMapping      field alias
     * @param servletRequest
     * @return String
     */
    public static String compoundColumnConverter(Map ListResultMap, Object compoundColumnObj, Map aliasMapping, ServletRequest servletRequest) {
        log.debug("Executing method compoundColumnConverter............................................");

        String res = "";
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (ListResultMap != null) {
            CompoundColumn compoundColumn = (CompoundColumn) compoundColumnObj;
            CompoundField compounField = compoundColumn.getCompoundField();

            Map pathsOfCompoundField = TitusPathUtil.getTitusPathFieldsFromCompoundField(compounField, compoundColumn.getFieldPath());

            Map converterParamsMap = new HashMap();
            Pair[] aliasPairs = new Pair[pathsOfCompoundField.size()];
            int cont = 0;
            for (Iterator iterator2 = pathsOfCompoundField.keySet().iterator(); iterator2.hasNext();) {
                String path = (String) iterator2.next();

                String aliasColumn = aliasMapping.get(path).toString();
                String columnValue = (String) ListResultMap.get(aliasColumn);

                converterParamsMap.put(path, aliasColumn);

                aliasPairs[cont] = new Pair(aliasColumn, columnValue);
                cont++;
            }


            ConverterManager converterManager = Titus.getConverterManager(request.getSession().getServletContext());
            Converter converter = converterManager.newConverterInstance(compounField.getConverter());

            Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
            User user = RequestUtils.getUser(request);
            DateTimeZone timeZone = (DateTimeZone) user.getValue("dateTimeZone");
            ResourceBundleWrapper resourceBundleWrapper = new StrutsResourceBundle(Constants.APPLICATION_RESOURCES, locale);

            Map parameters = new HashMap();
            parameters.put("locale", locale);
            parameters.put("timezone", timeZone);
            parameters.put(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP, converterParamsMap);

            FieldValue fieldValue = converter.dbToView(compounField, parameters, aliasPairs, resourceBundleWrapper, compoundColumn.getFieldPath());
            res = fieldValue.getShowable();
        }
        return res;
    }


    public static String getFunctionnalityResource(String functionality, HttpServletRequest request) {
        StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
        Table table = structureManager.getTable(functionality);
        return table.getResource();
    }

    public static String getModuleResource(String moduleName, HttpServletRequest request) {
        StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
        Module module = structureManager.getModule(moduleName);

        return module.getResource();
    }

    /**
     * apply converter if an field have defined
     *
     * @param fieldPath
     * @param columnValue
     * @param servletRequest
     * @return String
     */
    public static String applyConverterDbToViewInSimpleField(String fieldPath, String columnValue, ServletRequest servletRequest) {
        String res = null;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
        Field field = structureManager.getFieldByTitusPath(fieldPath);

        if (field.getConverter() != null) {

            Map converterParamsMap = new HashMap();
            Pair[] aliasPairs = new Pair[1];

            String aliasColumn = "field001";
            converterParamsMap.put(fieldPath, aliasColumn);
            aliasPairs[0] = new Pair(aliasColumn, columnValue);

            ConverterManager converterManager = Titus.getConverterManager(request.getSession().getServletContext());
            Converter converter = converterManager.newConverterInstance(field.getConverter());

            Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
            User user = RequestUtils.getUser(request);
            DateTimeZone timeZone = (DateTimeZone) user.getValue("dateTimeZone");
            ResourceBundleWrapper resourceBundleWrapper = new StrutsResourceBundle(Constants.APPLICATION_RESOURCES, locale);

            Map parameters = new HashMap();
            parameters.put("locale", locale);
            parameters.put("timezone", timeZone);
            parameters.put(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP, converterParamsMap);
            if (field.getType().getType() == DBType.DBTypeNameAsInt.DATETIME) {
                parameters.put(ReportConstants.LONGDATE_AS_SHORT_KEY, ReportConstants.LONGDATE_AS_SHORT_VALUE);
            }

            FieldValue fieldValue = converter.dbToView(field, parameters, aliasPairs, resourceBundleWrapper, fieldPath);
            log.debug("FIELD-VALUE" + fieldValue);
            if (fieldValue != null) {
                res = fieldValue.getShowable();
            }
        } else {
            res = columnValue;
        }
        return res;
    }

    /**
     * apply viewToDb converter, return null if the converter have not applied
     *
     * @param fieldPath
     * @param fieldValue
     * @param servletRequest
     * @return ResultValue object
     */
    public static ResultValue viewToDbConverterInSimpleField(String fieldPath, String fieldValue, ServletRequest servletRequest) {
        ResultValue resultValue;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
        Field field = structureManager.getFieldByTitusPath(fieldPath);

        if (field.getConverter() != null) {

            ConverterManager converterManager = Titus.getConverterManager(request.getSession().getServletContext());
            Converter converter = converterManager.newConverterInstance(field.getConverter());

            Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
            User user = RequestUtils.getUser(request);
            DateTimeZone timeZone = (DateTimeZone) user.getValue("dateTimeZone");
            ResourceBundleWrapper resourceBundleWrapper = new StrutsResourceBundle(Constants.APPLICATION_RESOURCES, locale);

            Map parameters = new HashMap();
            parameters.put("locale", locale);
            parameters.put("timezone", timeZone);

            resultValue = converter.viewToDb(parameters, fieldValue, resourceBundleWrapper);
        } else {
            resultValue = new ResultValue(fieldValue);
        }

        return resultValue;
    }

    /**
     * verif if this is an dbd filter and if should show in select
     *
     * @param fieldPath
     * @param servletRequest
     * @return true or false
     */
    public static boolean isDbFilterInSelect(String fieldPath, ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
        Field field = structureManager.getFieldByTitusPath(fieldPath);
        return isDbFilterInSelect(field.getFilter());
    }

    /**
     * verif if this filter should show in an select
     *
     * @param filter
     * @return true or false
     */
    public static boolean isDbFilterInSelect(Filter filter) {
        boolean res = false;
        if (isDbFilter(filter)) {
            DBFilter dbFilter = (DBFilter) filter;
            if (dbFilter.getShowIn() != null && dbFilter.getShowIn().equals(ReportConstants.DBFILTER_SHOWINSELECT)) {
                res = true;
            }
        }
        return res;
    }

    /**
     * execute FilterExternalListAction to obtain values of an db filter
     *
     * @param filterFieldPath
     * @param servletRequest
     * @return List of result values of an db filter
     */
    public static List getValuesOfDbFilterToSelect(String filterFieldPath, ServletRequest servletRequest) {
        List labelValueList = new ArrayList();
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        request.setAttribute("viewInSelect", Boolean.TRUE);

        //set in request required data to execute action
        request.setAttribute("sFilterPath", filterFieldPath);

        ActionMapping tempMapping = new ActionMapping();
        /*ModuleConfig moduleConfig = new ModuleConfigImpl("/reports");
        tempMapping.setParameter("EXTERNALLIST");
        tempMapping.setModuleConfig(moduleConfig);*/

        FilterExternalListAction listAction = new FilterExternalListAction();

        try {
            listAction.execute(tempMapping, new SearchForm(), request, null);
            labelValueList = (List) request.getAttribute("selectData");
        } catch (Exception e) {
            log.debug("ERRORR IN EXECUTE FANTA LIST----------------------------------------");
            e.printStackTrace();
        }
        return labelValueList;
    }

    /**
     * execute fantabulous list to get filter values
     *
     * @param filterId
     * @param filterType
     * @param servletRequest
     * @return String values
     */
    public static String getValuesOfFilter(String filterId, String filterType, ServletRequest servletRequest) {
        log.debug("executing method getValuesOfFilter..................................................");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        StringBuffer valueBuffer = new StringBuffer();

        FantabulousManager fantabulousManager = FantabulousManager.loadFantabulousManager(request.getSession().getServletContext(), "/reports");
        ListStructure listStructure = null;
        try {
            listStructure = fantabulousManager.getList("especialFilterValueList");
        } catch (ListStructureNotFoundException e) {
            log.debug("Not found list........... especialFilterValueList");
            e.printStackTrace();
        }

        if (listStructure != null) {

            Parameters params = new Parameters();
            params.addSearchParameter("filterId", filterId);

            SqlGenerator generator = SqlGeneratorManager.newInstance();
            String sqlQuery = generator.generate(listStructure, params);
            log.debug("SQL:::::" + sqlQuery);

            //execute list
            listStructure.setExecuteFirstTime(true);
            Collection resultList = Controller.getList(listStructure, params);
            log.debug("RESULT--------------" + resultList);

            LinkedList valuesList = new LinkedList(resultList);

            for (ListIterator iterator = valuesList.listIterator(); iterator.hasNext();) {
                Map map = (Map) iterator.next();
                String value = (String) map.get("value");
                String pkSequence = (String) map.get("pkSequence");

                if (filterType.equals(ReportConstants.FILTER_WITH_DB_VALUE.toString())) {
                    if (!"".equals(pkSequence.trim())) {
                        valueBuffer.append(value);
                        if (iterator.hasNext()) {
                            Map nextValueMap = (Map) valuesList.get(iterator.nextIndex());
                            if (pkSequence.equals(nextValueMap.get("pkSequence"))) {
                                valueBuffer.append(ReportConstants.PRIMARYKEY_SEPARATOR);
                            } else {
                                valueBuffer.append(ReportConstants.FILTERVALUE_SEPARATOR);
                            }
                        }
                    } else {
                        valueBuffer.append(value);
                        if (iterator.hasNext()) {
                            valueBuffer.append(ReportConstants.FILTERVALUE_SEPARATOR);
                        }
                    }
                } else {
                    valueBuffer.append(value);
                    if (iterator.hasNext()) {
                        valueBuffer.append(ReportConstants.FILTERVALUE_SEPARATOR);
                    }
                }
            }
        }
        return valueBuffer.toString();
    }

    /**
     * get all report filters as FilterDTO list
     *
     * @param reportId
     * @param servletRequest
     * @return list
     */
    public static List getAllReportFilters(String reportId, ServletRequest servletRequest) {
        List filterDTOs = new ArrayList();
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Integer companyId = getReportCompanyId(reportId);
        if (companyId == null) {
            User user = RequestUtils.getUser(request);
            companyId = new Integer(user.getValue("companyId").toString());
        }


        FilterAllReadCmd readCmd = new FilterAllReadCmd();
        readCmd.putParam("reportId", reportId);
        readCmd.putParam("companyId", companyId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(readCmd, request);
            if (!resultDTO.isFailure()) {
                filterDTOs = (List) resultDTO.get("filterDTOList");
            }
        } catch (AppLevelException e) {
            //this will not happen never
            log.debug("Error in execute cmd..", e);
        }
        return filterDTOs;
    }

    //start chart functions//

    /**
     * report chart types
     *
     * @param servletRequest
     * @return List
     */
    public static List getChartTypeList(ServletRequest servletRequest) {
        ArrayList list = new ArrayList();
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.type.area"), String.valueOf(CustomReportGeneratorConstants.CHART_TYPE_AREA)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.type.bar"), String.valueOf(CustomReportGeneratorConstants.CHART_TYPE_BAR)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.type.bar3D"), String.valueOf(CustomReportGeneratorConstants.CHART_TYPE_BAR3D)));
        //list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.type.bubble"), String.valueOf(CustomReportGeneratorConstants.CHART_TYPE_BUBBLE)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.type.line"), String.valueOf(CustomReportGeneratorConstants.CHART_TYPE_LINE)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.type.pie"), String.valueOf(CustomReportGeneratorConstants.CHART_TYPE_PIE)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.type.pie3D"), String.valueOf(CustomReportGeneratorConstants.CHART_TYPE_PIE3D)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.type.scatter"), String.valueOf(CustomReportGeneratorConstants.CHART_TYPE_SCATTER)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.type.stackedBar"), String.valueOf(CustomReportGeneratorConstants.CHART_TYPE_STACKEDBAR)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.type.stackedBar3D"), String.valueOf(CustomReportGeneratorConstants.CHART_TYPE_STACKEDBAR3D)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.type.timeSeries"), String.valueOf(CustomReportGeneratorConstants.CHART_TYPE_TIMESERIES)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.type.xyArea"), String.valueOf(CustomReportGeneratorConstants.CHART_TYPE_XYAREA)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.type.xyBar"), String.valueOf(CustomReportGeneratorConstants.CHART_TYPE_XYBAR)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.type.xyLine"), String.valueOf(CustomReportGeneratorConstants.CHART_TYPE_XYLINE)));

        return SortUtils.orderByProperty(list, "label");
    }

    /**
     * report chart orientation
     *
     * @param servletRequest
     * @return List
     */
    public static List getChartOrientationList(ServletRequest servletRequest) {
        ArrayList list = new ArrayList();
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.orientation.horizontal"), String.valueOf(CustomReportGeneratorConstants.CHART_ORIENTATION_HORIZONTAL)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.orientation.vertical"), String.valueOf(CustomReportGeneratorConstants.CHART_ORIENTATION_VERTICAL)));

        return SortUtils.orderByProperty(list, "label");
    }

    /**
     * report chart position
     *
     * @param servletRequest
     * @return list
     */
    public static List getChartPositionList(ServletRequest servletRequest) {
        ArrayList list = new ArrayList();
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.position.top"), String.valueOf(CustomReportGeneratorConstants.CHART_POSITION_START)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Report.chart.position.bottom"), String.valueOf(CustomReportGeneratorConstants.CHART_POSITION_END)));

        return SortUtils.orderByProperty(list, "label");
    }

    /**
     * compose an string to process i the view of chart
     *
     * @param chartType
     * @param orientation
     * @param servletRequest
     * @return string
     */
    public static String composeChartTypeView(int chartType, String orientation, ServletRequest servletRequest) {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        int chartGroupType = CustomReportGeneratorHelper.getChartGroupType(chartType);
        StringBuffer value = new StringBuffer();

        if (orientation == null || orientation.equals("")) {
            //default orientation
            orientation = String.valueOf(CustomReportGeneratorConstants.CHART_ORIENTATION_HORIZONTAL);
        }

        if (CustomReportGeneratorConstants.CHARTGROUP_CATEGORIES == chartGroupType) {

            Map resourcesMap = getResourcesOfChartGroupTypeValues(chartGroupType, orientation, request);

            value.append(ReportConstants.KEY_CHARTVIEW_SERIE)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_SERIE))
                    .append(ReportConstants.KEY_CHARTVIEW_SERIE)

                    .append(ReportConstants.KEY_CHARTVIEW_YVALUE)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YVALUE))
                    .append(ReportConstants.KEY_CHARTVIEW_YVALUE)

                    .append(ReportConstants.KEY_CHARTVIEW_CATEGORY)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_CATEGORY))
                    .append(ReportConstants.KEY_CHARTVIEW_CATEGORY)

                    .append(ReportConstants.KEY_CHARTVIEW_XLABEL)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_XLABEL))
                    .append(ReportConstants.KEY_CHARTVIEW_XLABEL)

                    .append(ReportConstants.KEY_CHARTVIEW_YLABEL)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YLABEL))
                    .append(ReportConstants.KEY_CHARTVIEW_YLABEL);

        } else if (CustomReportGeneratorConstants.CHARTGROUP_BUBBLES == chartGroupType) {

            Map resourcesMap = getResourcesOfChartGroupTypeValues(chartGroupType, orientation, request);
            value.append(ReportConstants.KEY_CHARTVIEW_SERIE)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_SERIE))
                    .append(ReportConstants.KEY_CHARTVIEW_SERIE)

                    .append(ReportConstants.KEY_CHARTVIEW_XVALUE)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_XVALUE))
                    .append(ReportConstants.KEY_CHARTVIEW_XVALUE)

                    .append(ReportConstants.KEY_CHARTVIEW_YVALUE)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YVALUE))
                    .append(ReportConstants.KEY_CHARTVIEW_YVALUE)

                    .append(ReportConstants.KEY_CHARTVIEW_ZVALUE)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_ZVALUE))
                    .append(ReportConstants.KEY_CHARTVIEW_ZVALUE)

                    .append(ReportConstants.KEY_CHARTVIEW_XLABEL)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_XLABEL))
                    .append(ReportConstants.KEY_CHARTVIEW_XLABEL)

                    .append(ReportConstants.KEY_CHARTVIEW_YLABEL)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YLABEL))
                    .append(ReportConstants.KEY_CHARTVIEW_YLABEL);

        } else if (CustomReportGeneratorConstants.CHARTGROUP_PIES == chartGroupType) {

            Map resourcesMap = getResourcesOfChartGroupTypeValues(chartGroupType, orientation, request);
            value.append(ReportConstants.KEY_CHARTVIEW_SERIE)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_SERIE))
                    .append(ReportConstants.KEY_CHARTVIEW_SERIE)

                    .append(ReportConstants.KEY_CHARTVIEW_YVALUE)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YVALUE))
                    .append(ReportConstants.KEY_CHARTVIEW_YVALUE);

            //value.append(ReportConstants.KEY_CHARTVIEW_NONE_ORIENTATION);

        } else if (CustomReportGeneratorConstants.CHARTGROUP_VALUESXY == chartGroupType) {

            Map resourcesMap = getResourcesOfChartGroupTypeValues(chartGroupType, orientation, request);
            value.append(ReportConstants.KEY_CHARTVIEW_SERIE)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_SERIE))
                    .append(ReportConstants.KEY_CHARTVIEW_SERIE)

                    .append(ReportConstants.KEY_CHARTVIEW_XVALUE)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_XVALUE))
                    .append(ReportConstants.KEY_CHARTVIEW_XVALUE)

                    .append(ReportConstants.KEY_CHARTVIEW_YVALUE)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YVALUE))
                    .append(ReportConstants.KEY_CHARTVIEW_YVALUE)

                    .append(ReportConstants.KEY_CHARTVIEW_XLABEL)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_XLABEL))
                    .append(ReportConstants.KEY_CHARTVIEW_XLABEL)

                    .append(ReportConstants.KEY_CHARTVIEW_YLABEL)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YLABEL))
                    .append(ReportConstants.KEY_CHARTVIEW_YLABEL);

        } else if (CustomReportGeneratorConstants.CHARTGROUP_TIMESERIES == chartGroupType) {

            Map resourcesMap = getResourcesOfChartGroupTypeValues(chartGroupType, orientation, request);
            value.append(ReportConstants.KEY_CHARTVIEW_SERIE)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_SERIE))
                    .append(ReportConstants.KEY_CHARTVIEW_SERIE)

                    .append(ReportConstants.KEY_CHARTVIEW_YVALUE)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YVALUE))
                    .append(ReportConstants.KEY_CHARTVIEW_YVALUE)

                    .append(ReportConstants.KEY_CHARTVIEW_CATEGORY)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_CATEGORY))
                    .append(ReportConstants.KEY_CHARTVIEW_CATEGORY)

                    .append(ReportConstants.KEY_CHARTVIEW_XLABEL)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_XLABEL))
                    .append(ReportConstants.KEY_CHARTVIEW_XLABEL)

                    .append(ReportConstants.KEY_CHARTVIEW_YLABEL)
                    .append(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YLABEL))
                    .append(ReportConstants.KEY_CHARTVIEW_YLABEL);
        } else {
            value = new StringBuffer();
        }

        return value.toString();
    }

    /**
     * get resources for an determined chart group type view
     *
     * @param chartGroupType
     * @param orientation
     * @param servletRequest
     * @return map with resources
     */
    public static Map getResourcesOfChartGroupTypeValues(int chartGroupType, String orientation, ServletRequest servletRequest) {
        Map resultMap = new HashMap();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        StringBuffer value;
        Map resourcesMap = chartOrientationConfigResources();

        if (CustomReportGeneratorConstants.CHARTGROUP_CATEGORIES == chartGroupType) {
            resultMap = new HashMap();

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.serie"));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_SERIE, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.value"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YVALUE), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_YVALUE, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.category"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_CATEGORY), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_CATEGORY, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.label"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_XLABEL), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_XLABEL, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.label"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YLABEL), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_YLABEL, value.toString());

        } else if (CustomReportGeneratorConstants.CHARTGROUP_BUBBLES == chartGroupType) {
            resultMap = new HashMap();

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.serie"));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_SERIE, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.value"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_XVALUE), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_XVALUE, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.value"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YVALUE), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_YVALUE, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.value"))
                    .append(JSPHelper.getMessage(request, "Report.chart.conf.zAxis"));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_ZVALUE, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.label"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_XLABEL), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_XLABEL, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.label"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YLABEL), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_YLABEL, value.toString());

        } else if (CustomReportGeneratorConstants.CHARTGROUP_PIES == chartGroupType) {
            resultMap = new HashMap();

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.serie"));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_SERIE, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.value"));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_YVALUE, value.toString());

        } else if (CustomReportGeneratorConstants.CHARTGROUP_VALUESXY == chartGroupType) {
            resultMap = new HashMap();

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.serie"));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_SERIE, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.value"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_XVALUE), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_XVALUE, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.value"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YVALUE), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_YVALUE, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.label"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_XLABEL), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_XLABEL, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.label"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YLABEL), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_YLABEL, value.toString());

        } else if (CustomReportGeneratorConstants.CHARTGROUP_TIMESERIES == chartGroupType) {
            resultMap = new HashMap();

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.serie"));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_SERIE, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.value"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YVALUE), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_YVALUE, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.time"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_CATEGORY), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_CATEGORY, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.label"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_XLABEL), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_XLABEL, value.toString());

            value = new StringBuffer();
            value.append(JSPHelper.getMessage(request, "Report.chart.conf.label"))
                    .append(JSPHelper.getMessage(request, getChartViewResource((Map) resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YLABEL), orientation)));
            resultMap.put(ReportConstants.KEY_CHARTVIEW_YLABEL, value.toString());
        }
        return resultMap;
    }

    /**
     * chart orientation resources to all view configuration
     *
     * @return map
     */
    private static Map chartOrientationConfigResources() {
        Map confMap = new HashMap();

        //default orientation in chart is vertical
        Map xyMap = new HashMap();
        xyMap.put(String.valueOf(CustomReportGeneratorConstants.CHART_ORIENTATION_HORIZONTAL), "Report.chart.conf.xAxis");
        xyMap.put(String.valueOf(CustomReportGeneratorConstants.CHART_ORIENTATION_VERTICAL), "Report.chart.conf.yAxis");

        Map yxMap = new HashMap();
        yxMap.put(String.valueOf(CustomReportGeneratorConstants.CHART_ORIENTATION_HORIZONTAL), "Report.chart.conf.yAxis");
        yxMap.put(String.valueOf(CustomReportGeneratorConstants.CHART_ORIENTATION_VERTICAL), "Report.chart.conf.xAxis");

        confMap.put(ReportConstants.KEY_CHARTVIEW_SERIE, yxMap);
        confMap.put(ReportConstants.KEY_CHARTVIEW_CATEGORY, yxMap);
        confMap.put(ReportConstants.KEY_CHARTVIEW_XVALUE, yxMap);
        confMap.put(ReportConstants.KEY_CHARTVIEW_YVALUE, xyMap);

        confMap.put(ReportConstants.KEY_CHARTVIEW_XLABEL, yxMap);
        confMap.put(ReportConstants.KEY_CHARTVIEW_YLABEL, xyMap);

        return confMap;
    }

    /**
     * get resource of an map
     *
     * @param map
     * @param key
     * @return string resource
     */
    private static String getChartViewResource(Map map, String key) {
        return String.valueOf(map.get(key));
    }

    /**
     * set resources in totalizers dtos
     *
     * @param totalizeDtoList
     * @param servletRequest
     * @return List
     */
    public static List getChartTotalizersWithResource(List totalizeDtoList, ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        ArrayList result = new ArrayList();
        for (Iterator iterator = totalizeDtoList.iterator(); iterator.hasNext();) {
            Map dtoMap = (Map) iterator.next();

            String totalizeName = "";
            Integer totalizeType = new Integer(dtoMap.get("totalizeType").toString());
            if (!totalizeType.equals(ReportConstants.TOTALIZER_TYPE_CUSTOM)) {
                String columnName = "";
                if (dtoMap.containsKey("path")) {
                    columnName = composeColumnLabelByTitusPath(dtoMap.get("path").toString(), null, request);
                }
                String totalizeTypeName = getTotalizeTypeResource(totalizeType, request);
                totalizeName = columnName + " [" + totalizeTypeName + "]";

            } else if (dtoMap.containsKey("name")) {
                totalizeName = dtoMap.get("name").toString();
            }

            result.add(new LabelValueBean(totalizeName, dtoMap.get("totalizeId").toString()));
        }
        return SortUtils.orderByProperty(result, "label");
    }

    /**
     * get totalizer type resource
     *
     * @param type
     * @param request
     * @return String
     */
    private static String getTotalizeTypeResource(Integer type, HttpServletRequest request) {
        String name = "";
        if (ReportConstants.TOTALIZER_TYPE_SUM.equals(type)) {
            name = JSPHelper.getMessage(request, "Totalize.sum");
        } else if (ReportConstants.TOTALIZER_TYPE_AVERAGE.equals(type)) {
            name = JSPHelper.getMessage(request, "Totalize.average");
        } else if (ReportConstants.TOTALIZER_TYPE_LARGUESTVALUE.equals(type)) {
            name = JSPHelper.getMessage(request, "Totalize.largestValue");
        } else if (ReportConstants.TOTALIZER_TYPE_SMALLESTVALUE.equals(type)) {
            name = JSPHelper.getMessage(request, "Totalize.smallestValue");
        } else if (ReportConstants.TOTALIZER_TYPE_SUMRECORDS.equals(type)) {
            name = JSPHelper.getMessage(request, "Totalize.recordCount");
        }

        return name;
    }

    /**
     * set resource in column dtos
     *
     * @param columnDtoList
     * @param chartType
     * @param sRequest
     * @return List
     */
    public static List getChartColumnGroupsWithResource(List columnDtoList, String chartType, ServletRequest sRequest) {
        HttpServletRequest request = (HttpServletRequest) sRequest;
        StructureManager manager = Titus.getStructureManager(request.getSession().getServletContext());
        ArrayList result = new ArrayList();


        for (Iterator iterator = columnDtoList.iterator(); iterator.hasNext();) {
            Map map = (Map) iterator.next();
            String titusPath = map.get("path").toString();
            if (!GenericValidator.isBlankOrNull(chartType) &&
                    CustomReportGeneratorConstants.CHARTGROUP_TIMESERIES == CustomReportGeneratorHelper.getChartGroupType(Integer.parseInt(chartType))) {
                Field field = manager.getFieldByTitusPath(titusPath);
                if (field.getType().getBaseType() == DBType.DBBaseType.BASETYPE_DATE) {
                    String label = composeColumnLabelByTitusPath(titusPath, null, request);
                    result.add(new LabelValueBean(label, map.get("columnGroupId").toString()));
                }
            } else {
                String label = composeColumnLabelByTitusPath(titusPath, null, request);
                result.add(new LabelValueBean(label, map.get("columnGroupId").toString()));
            }
        }
        return SortUtils.orderByProperty(result, "label");
    }
    //end chart functions//

    /**
     * Get report columns size
     *
     * @param reportId
     * @return int
     */
    public static int getReportColumnsSize(Integer reportId) {
        int reportColumnSize = 0;
        ReportColumnCountCmd columnCountCmd = new ReportColumnCountCmd();
        columnCountCmd.putParam("reportId", reportId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(columnCountCmd, null);
            if (!resultDTO.isFailure() && resultDTO.containsKey("columnsSize")) {
                reportColumnSize = Integer.parseInt(resultDTO.get("columnsSize").toString());
            }
        } catch (AppLevelException e) {
            //this will not happen never
            log.debug("Error in execute cmd..", e);
        }
        return reportColumnSize;
    }

    /**
     * Get report column groups size
     *
     * @param reportId
     * @return int
     */
    public static int getReportColumnGroupsSize(Integer reportId) {
        int columnGroupSize = 0;
        ReportColumnGroupCountCmd columnGroupCountCmd = new ReportColumnGroupCountCmd();
        columnGroupCountCmd.putParam("reportId", reportId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(columnGroupCountCmd, null);
            if (!resultDTO.isFailure() && resultDTO.containsKey("columnGroupsSize")) {
                columnGroupSize = Integer.parseInt(resultDTO.get("columnGroupsSize").toString());
            }
        } catch (AppLevelException e) {
            //this will not happen never
            log.debug("Error in execute cmd..", e);
        }
        return columnGroupSize;
    }

    /**
     * Get category dynamic columns parameters
     *
     * @param servletRequest servlet reques
     * @return Map
     */
    public static Map getCategoryDynamicColumnParams(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        //dynamic columns parameters
        User user = RequestUtils.getUser(request);
        Map dynamicColumnParameters = new HashMap();
        dynamicColumnParameters.put("companyId", user.getValue("companyId"));

        return dynamicColumnParameters;
    }

    /**
     * Verifiy if operator is to multiple values
     *
     * @param operator op
     * @return true or false
     */
    public static boolean isOperatorWithMultipleValues(String operator) {
        return (Integer.parseInt(operator) == StructureConstants.FilterTitusType.OP_ONE_OF
                || Integer.parseInt(operator) == StructureConstants.FilterTitusType.OP_NOT_ONE_OF);
    }

    /**
     * Verify if filter operator si of BD Filter
     *
     * @param operator filter operator
     * @return true or false
     */
    public static boolean isDBFilterOperator(String operator) {
        return (String.valueOf(StructureConstants.FilterTitusType.OP_IS).equals(operator)
                || String.valueOf(StructureConstants.FilterTitusType.OP_IS_NOT).equals(operator)
                || String.valueOf(StructureConstants.FilterTitusType.OP_ONE_OF).equals(operator)
                || String.valueOf(StructureConstants.FilterTitusType.OP_NOT_ONE_OF).equals(operator));
    }

    public static boolean isJrxmlSourceTypeReport(String reportId) {
        boolean isJrxmlSource = false;

        if (!GenericValidator.isBlankOrNull(reportId)) {
            LightlyReportCmd cmd = new LightlyReportCmd();
            cmd.putParam("reportId", reportId);
            cmd.setOp("read");

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, null);
                if (!resultDTO.isFailure() && resultDTO.get("sourceType") != null) {
                    isJrxmlSource = ReportConstants.SourceType.JRXML.equal(resultDTO.get("sourceType").toString());
                }
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd..", e);
            }
        }
        return isJrxmlSource;
    }

    public static Map getReportQueryParamRelatedToFilter(String filterId) {
        Map reportQueryParamDto = new HashMap();

        if (!GenericValidator.isBlankOrNull(filterId)) {
            ReportQueryParamCmd cmd = new ReportQueryParamCmd();
            cmd.setOp("queryParamFromFilter");
            cmd.putParam("filterId", filterId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, null);
                if (!resultDTO.isFailure() && resultDTO.get("reportQueryParamMap") != null) {
                    reportQueryParamDto = (Map) resultDTO.get("reportQueryParamMap");
                }
            } catch (AppLevelException e) {
                //this will not happen never
                log.debug("Error in execute cmd..", e);
            }
        }

        return reportQueryParamDto;
    }

    public static Integer getReportCompanyId(String reportId) {
        Integer reportCompanyId = null;

        if (!GenericValidator.isBlankOrNull(reportId)) {
            LightlyReportCmd cmd = new LightlyReportCmd();
            cmd.setOp("read");
            cmd.putParam("reportId", Integer.valueOf(reportId));

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, null);
                if (!resultDTO.isFailure() && resultDTO.get("companyId") != null) {
                    reportCompanyId = new Integer(resultDTO.get("companyId").toString());
                }
            } catch (AppLevelException e) {
                //this will not happen never
                log.debug("Error in execute cmd..", e);
            }
        }
        return reportCompanyId;
    }


}
