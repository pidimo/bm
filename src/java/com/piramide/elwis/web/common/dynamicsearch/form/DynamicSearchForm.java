package com.piramide.elwis.web.common.dynamicsearch.form;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchConstants;
import com.piramide.elwis.web.common.dynamicsearch.el.DynamicSearchUtil;
import com.piramide.elwis.web.common.dynamicsearch.structure.Field;
import com.piramide.elwis.web.common.util.FormatUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.validator.FieldChecks;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class DynamicSearchForm extends SearchForm {
    public static final String dynamicSearchNameParameter = "dynamicSearchName";
    public static final String operatorPostFix = "_operator";
    public static final String isCustomPreFix = "isCustomSearch_";
    public static final String value1PostFix = "_1";
    public static final String value2PostFix = "_2";

    private Object[] searchProperties;
    private Map<String, String> propertyFilterMap;

    public DynamicSearchForm() {
        this.searchProperties = new Object[0];
        this.propertyFilterMap = new HashMap<String, String>();
    }

    public Object[] getSearchProperties() {
        return this.searchProperties;
    }

    public void setSearchProperties(Object[] searchProperties) {
        if (searchProperties != null) {
            this.searchProperties = searchProperties;
        } else {
            this.searchProperties = new Object[0];
        }
    }
    
    public List<String> getSearchPropertiesAsList() {
        List<String> searchPropertiesList = new ArrayList<String>();
        for (int i = 0; i < getSearchProperties().length; i++) {
            Object fieldAlias = getSearchProperties()[i];
            if (fieldAlias != null && !GenericValidator.isBlankOrNull(fieldAlias.toString())) {
                searchPropertiesList.add(fieldAlias.toString());
            }
        }
        return searchPropertiesList;
    }

    public void addPropertyFilter(String key, String value) {
        propertyFilterMap.put(key, value);
    }

    public Map<String, String> getPropertyFilterMap() {
        return propertyFilterMap;
    }

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Executing DynamicSearchForm validation..." + getParams());
        log.debug("Search properties.." + getSearchPropertiesAsList());
        log.debug("request parameter map:"+request.getParameterMap());

        ActionErrors errors = super.validate(mapping, request);

        if (getDynamicSearchName() != null) {
            validateSearchProperties(errors, request);
        }

        //rewrite selected fields
        String dynamicSearchName = getDynamicSearchName();
        Map parameterMap = getParams();
        if (dynamicSearchName != null) {
            request.setAttribute("searchFieldsUIList", DynamicSearchUtil.getDynamicSearchFieldsAsUI(getSearchPropertiesAsList(), dynamicSearchName, parameterMap, request));
        }

        return errors;
    }

    private void validateSearchProperties(ActionErrors errors, HttpServletRequest request) {
        List<String> fieldAliasList = getSearchPropertiesAsList();
        for (String fieldAlias : fieldAliasList) {
            validateSearchProperty(fieldAlias, errors, request);
        }
    }

    private void validateSearchProperty(String fieldAlias, ActionErrors errors, HttpServletRequest request) {
        Map parameterMap = getParams();
        DynamicSearchConstants.Operator operator = findOperator(fieldAlias, parameterMap);

        if (operator != null) {
            if (DynamicSearchUtil.isDynamicSearchOneBoxView(operator.getConstant())) {
                Field field = DynamicSearchUtil.findField(getDynamicSearchName(), fieldAlias, request);
                validateSearchPropertyValue(field, fieldAlias, parameterMap, errors, request);

            } else if (DynamicSearchUtil.isDynamicSearchTwoBoxView(operator.getConstant())) {
                Field field = DynamicSearchUtil.findField(getDynamicSearchName(), fieldAlias, request);
                String parameterAlias1 = DynamicSearchForm.composeValue1ParameterName(fieldAlias);
                String parameterAlias2 = DynamicSearchForm.composeValue2ParameterName(fieldAlias);

                validateSearchPropertyValue(field, parameterAlias1, parameterMap, errors, request);
                validateSearchPropertyValue(field, parameterAlias2, parameterMap, errors, request);
            }
        }
    }

    private String validateSearchPropertyValue(Field field, String parameterAlias, Map parameterMap, ActionErrors errors, HttpServletRequest request) {
        String result = null;
        String value = (String) parameterMap.get(parameterAlias);
        if (!GenericValidator.isBlankOrNull(value)) {

            if (DynamicSearchConstants.FieldType.DATE.equals(field.getType())) {
                result = dateValidation(value, field, request, errors);
                if (result != null) {
                    //redefine date value in parameters
                    setParameter(parameterAlias, result);
                }
            } else if (DynamicSearchConstants.FieldType.INTEGER.equals(field.getType())) {
                result = integerValidation(value, field, request, errors);

            } else if (DynamicSearchConstants.FieldType.DECIMAL.equals(field.getType())) {
                result = decimalValidation(value, field, request, errors);
                if (result != null) {
                    setParameter(parameterAlias, result);
                }
            }
        }

        return result;
    }

    private String dateValidation(String value, Field field, HttpServletRequest request) {
        return dateValidation(value, field, request, null);
    }

    private String dateValidation(String value, Field field, HttpServletRequest request, ActionErrors errors) {
        String result = null;
        String pattern = JSPHelper.getMessage(request, "datePattern").trim();
        if (GenericValidator.isDate(value, pattern, false)) {
            Integer dateInteger = DateUtils.dateToInteger(DateUtils.formatDate(value, pattern));
            result = dateInteger.toString();
        } else {
            if (errors != null) {
                errors.add("errorDate", new ActionError("errors.date", getFieldMessage(field, request), pattern));
            }
        }
        return result;
    }

    private String integerValidation(String value, Field field, HttpServletRequest request, ActionErrors errors) {
        String result = null;
        if (GenericValidator.isInt(value)) {
            result = value;
        } else {
            if (errors != null) {
                errors.add("int", new ActionError("errors.integer", getFieldMessage(field, request)));
            }
        }
        return result;
    }

    private String decimalValidation(String value, Field field, HttpServletRequest request, ActionErrors errors) {
        String result = null;

        int intPart = 10;
        int decimalPart = 2;

        ActionError decimalValidationError = FieldChecks.validateDecimalNumber(value, null, intPart, decimalPart, request);
        if (decimalValidationError == null) {
            result = FormatUtils.unformatDecimalNumber(value, intPart, decimalPart, request);
        } else {
            if (errors != null) {
                errors.add("decimal", new ActionError("error.number.decimal.invalid", getFieldMessage(field, request), MessagesUtil.i.getDecimalNumberFormat(request, decimalPart)));
            }
        }
        return result;
    }

    private String getFieldMessage(Field field, HttpServletRequest request) {
        return DynamicSearchUtil.composeFieldLabel(field, request);
    }

    public String getDynamicSearchName() {
        String name = null;
        Map parameterMap = getParams();
        if (parameterMap.containsKey(dynamicSearchNameParameter)) {
            name = parameterMap.get(dynamicSearchNameParameter).toString();
        }
        return name;
    }

    public static String composeOperatorParameterName(String fieldAlias) {
        return fieldAlias + operatorPostFix;
    }

    public static String composeValue1ParameterName(String fieldAlias) {
        return fieldAlias + value1PostFix;
    }

    public static String composeValue2ParameterName(String fieldAlias) {
        return fieldAlias + value2PostFix;
    }

    public static String composeIsCustomParameterName(String fieldAlias) {
        return isCustomPreFix + fieldAlias;
    }

    public static DynamicSearchConstants.Operator findOperator(String fieldAlias, Map parameterMap) {
        String operatorParameterName = DynamicSearchForm.composeOperatorParameterName(fieldAlias);
        String operatorConstant = (String) parameterMap.get(operatorParameterName);
        return DynamicSearchConstants.Operator.findOperator(operatorConstant);
    }

}
