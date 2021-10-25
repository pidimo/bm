package com.piramide.elwis.web.common.dynamicsearch.el;

import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SortUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.catalogmanager.el.Functions;
import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchConstants;
import com.piramide.elwis.web.common.dynamicsearch.form.DynamicSearchForm;
import com.piramide.elwis.web.common.dynamicsearch.structure.DynamicSearch;
import com.piramide.elwis.web.common.dynamicsearch.structure.DynamicSearchStructure;
import com.piramide.elwis.web.common.dynamicsearch.structure.Field;
import com.piramide.elwis.web.common.dynamicsearch.structure.FieldOperator;
import com.piramide.elwis.web.common.dynamicsearch.structure.dynamicfield.CategoryField;
import com.piramide.elwis.web.common.dynamicsearch.util.DynamicSearchFactory;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class DynamicSearchUtil {
    private static Log log = LogFactory.getLog(DynamicSearchUtil.class);

    private static Map getFindFieldParams(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Map params = new HashMap();
        params.put("companyId", Integer.valueOf(user.getValue(Constants.COMPANYID).toString()));
        return params;
    }

    public static String composeFieldLabel(Field field, HttpServletRequest request) {
        String label;
        if (field.isCategoryField()) {
            CategoryField categoryField = (CategoryField) field;
            label = categoryField.getName();

            if (ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY.equals(categoryField.getTable())) {
                label = label + " " + JSPHelper.getMessage(request, categoryField.getCategoryFieldType().getPostfixResource());
            }
        } else {
            label = JSPHelper.getMessage(request, field.getResource());
        }
        return label;
    }

    public static List<LabelValueBean> getDynamicSearchFieldList(String dynamicSearchName, HttpServletRequest request) {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();

        DynamicSearchStructure searchStructure = DynamicSearchFactory.i.getDynamicSearchStructure();
        if (searchStructure != null) {
            Map params = getFindFieldParams(request);
            List<Field> fields = searchStructure.getDynamicSearchFields(dynamicSearchName, params);
            for (Field field : fields) {
                LabelValueBean labelValueBean = new LabelValueBean(composeFieldLabel(field, request), field.getAlias());
                result.add(labelValueBean);
            }
        }

        return SortUtils.orderByProperty((ArrayList) result, "label");
    }

    public static List<Map> getDynamicSearchFieldsAsUI(List<String> searchFieldAliasList, String dynamicSearchName, Map parameterMap, HttpServletRequest request) {
        List<Map> result = new ArrayList<Map>();

        DynamicSearchStructure searchStructure = DynamicSearchFactory.i.getDynamicSearchStructure();
        if (searchStructure != null) {
            DynamicSearch dynamicSearch = searchStructure.findDynamicSearch(dynamicSearchName);
            if (dynamicSearch != null) {
                Map params = getFindFieldParams(request);
                dynamicSearch.resetAllFields();
                for (String fieldAlias : searchFieldAliasList) {
                    Field field = dynamicSearch.findField(fieldAlias, params);

                    if (field != null) {
                        String operatorParameterName = DynamicSearchForm.composeOperatorParameterName(fieldAlias);
                        String operatorConstant = (String) parameterMap.get(operatorParameterName);

                        Map map = new HashMap();
                        map.put("fieldAlias", fieldAlias);
                        map.put("label", composeFieldLabel(field, request));
                        map.put("operator", operatorConstant);

                        result.add(map);
                    }
                }
            }
        }
        return result;
    }

    public static List<LabelValueBean> getDynamicSearchFieldOperatorList(String dynamicSearchName, String alias, HttpServletRequest request) {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();

        DynamicSearchStructure searchStructure = DynamicSearchFactory.i.getDynamicSearchStructure();
        if (searchStructure != null) {
            Map params = getFindFieldParams(request);
            List<FieldOperator> fieldOperators = searchStructure.getDynamicSearchFieldOperators(dynamicSearchName, alias, params);
            for (FieldOperator fieldOperator : fieldOperators) {
                DynamicSearchConstants.Operator operator = fieldOperator.getOperator();
                LabelValueBean labelValueBean = new LabelValueBean(JSPHelper.getMessage(request, operator.getResource()), operator.getConstant());
                result.add(labelValueBean);
            }
        }
        return result;
    }

    public static boolean isDynamicSearchOneBoxView(String operatorConstant) {
        if (DynamicSearchConstants.Operator.EQUAL.equal(operatorConstant)
                || DynamicSearchConstants.Operator.NOT_EQUAL.equal(operatorConstant)
                || DynamicSearchConstants.Operator.CONTAIN.equal(operatorConstant)
                || DynamicSearchConstants.Operator.START_WITH.equal(operatorConstant)
                || DynamicSearchConstants.Operator.END_WITH.equal(operatorConstant)
                || DynamicSearchConstants.Operator.LESS_THAN.equal(operatorConstant)
                || DynamicSearchConstants.Operator.GREATER_THAN.equal(operatorConstant)
                || DynamicSearchConstants.Operator.ON.equal(operatorConstant)
                || DynamicSearchConstants.Operator.NOT_ON.equal(operatorConstant)
                || DynamicSearchConstants.Operator.BEFORE.equal(operatorConstant)
                || DynamicSearchConstants.Operator.AFTER.equal(operatorConstant)
                ) {
            return true;
        }
        return false;
    }

    public static boolean isDynamicSearchTwoBoxView(String operatorConstant) {
        return DynamicSearchConstants.Operator.BETWEEN.equal(operatorConstant);
    }

    public static boolean isDynamicSearchSelectView(String operatorConstant) {
        if (DynamicSearchConstants.Operator.IS.equal(operatorConstant)
                || DynamicSearchConstants.Operator.IS_NOT.equal(operatorConstant)
                ) {
            return true;
        }
        return false;
    }

    public static Field findField(String dynamicSearchName, String fieldAlias, HttpServletRequest request) {
        Map params = getFindFieldParams(request);
        DynamicSearchStructure searchStructure = DynamicSearchFactory.i.getDynamicSearchStructure();
        Field field = searchStructure.getDynamicSearchField(dynamicSearchName, fieldAlias, params);
        if (field != null) {
            return field;
        }
        return null;
    }

    public static FieldOperator findFieldOperator(String dynamicSearchName, String fieldAlias, DynamicSearchConstants.Operator operator, HttpServletRequest request) {
        FieldOperator fieldOperator = null;
        Field field = findField(dynamicSearchName, fieldAlias, request);
        if (field != null) {
            fieldOperator = field.findFieldOperator(operator);
        }
        return fieldOperator;
    }

    public static boolean searchFieldIsDateType(String dynamicSearchName, String fieldAlias, HttpServletRequest request) {
        Field field = findField(dynamicSearchName, fieldAlias, request);
        if (field != null) {
            if (DynamicSearchConstants.FieldType.DATE.equals(field.getType())) {
                return true;
            }
        }
        return false;
    }

    public static boolean searchFieldIsDecimalType(String dynamicSearchName, String fieldAlias, HttpServletRequest request) {
        Field field = findField(dynamicSearchName, fieldAlias, request);
        if (field != null) {
            if (DynamicSearchConstants.FieldType.DECIMAL.equals(field.getType())) {
                return true;
            }
        }
        return false;
    }

    public static boolean searchFieldIsIntegerType(String dynamicSearchName, String fieldAlias, HttpServletRequest request) {
        Field field = findField(dynamicSearchName, fieldAlias, request);
        if (field != null) {
            if (DynamicSearchConstants.FieldType.INTEGER.equals(field.getType())) {
                return true;
            }
        }
        return false;
    }

    public static boolean searchFieldIsNumberType(String dynamicSearchName, String fieldAlias, HttpServletRequest request) {
        Field field = findField(dynamicSearchName, fieldAlias, request);
        if (field != null) {
            if (DynamicSearchConstants.FieldType.INTEGER.equals(field.getType())
                    || DynamicSearchConstants.FieldType.DECIMAL.equals(field.getType())) {
                return true;
            }
        }
        return false;
    }

    public static Map findCategoryFieldWithValues(String dynamicSearchName, String fieldAlias, HttpServletRequest request) {
        Map categoryMap = new HashMap();

        Map params = getFindFieldParams(request);
        DynamicSearchStructure searchStructure = DynamicSearchFactory.i.getDynamicSearchStructure();
        Field field = searchStructure.getDynamicSearchField(dynamicSearchName, fieldAlias, params);
        if (field != null && field.isCategoryField()) {
            CategoryField categoryField = (CategoryField) field;
            CatalogConstants.CategoryType categoryType = categoryField.getCategoryType();

            if (Functions.isCategorySingleSelect(categoryType.getConstant()) || Functions.isCategoryCompoundSelect(categoryType.getConstant())) {
                categoryMap.put("categoryId", categoryField.getCategoryId());
                categoryMap.put("categoryType", categoryType.getConstant());
            }
        }
        return categoryMap;
    }


}
