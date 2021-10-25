package com.piramide.elwis.web.common.dynamicsearch.fantabulous;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchConstants;
import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchProperty;
import com.piramide.elwis.web.common.dynamicsearch.el.DynamicSearchUtil;
import com.piramide.elwis.web.common.dynamicsearch.structure.dynamicfield.CategoryField;
import com.piramide.elwis.web.common.dynamicsearch.util.OperatorUtil;
import org.alfacentauro.fantabulous.common.Constants;
import org.alfacentauro.fantabulous.exception.FieldNotFoundException;
import org.alfacentauro.fantabulous.structure.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class FantabulousConditionCompleter {
    protected Log log = LogFactory.getLog(this.getClass());

    private boolean isUnionList = false;
    private String dynamicSearchName;
    private HttpServletRequest request;

    public FantabulousConditionCompleter(String dynamicSearchName, HttpServletRequest request) {
        this.dynamicSearchName = dynamicSearchName;
        this.request = request;
    }

    public ListStructure completeConditions(ListStructure listStructure, List<DynamicSearchProperty> dynamicSearchProperties) {

        if (listStructure.getType() == UnionListStructure.UNIONLIST_TYPE) {
            isUnionList = true;
            UnionListStructure unionListStructure = (UnionListStructure) listStructure;

            Iterator unionLists = unionListStructure.getList().iterator();
            while (unionLists.hasNext()) {
                ListStructure unionList = (ListStructure) unionLists.next();
                processSearchProperties(unionList, dynamicSearchProperties);
            }
            listStructure = unionListStructure;
        } else {
            isUnionList = false;
            listStructure = processSearchProperties(listStructure, dynamicSearchProperties);
        }
        return listStructure;
    }

    private ListStructure processSearchProperties(ListStructure listStructure, List<DynamicSearchProperty> dynamicSearchProperties) {
        for (int i = 0; i < dynamicSearchProperties.size(); i++) {
            DynamicSearchProperty dynamicSearchProperty = dynamicSearchProperties.get(i);

            Condition condition;
            com.piramide.elwis.web.common.dynamicsearch.structure.Field field = DynamicSearchUtil.findField(dynamicSearchName, dynamicSearchProperty.getFieldAlias(), request);
            if (field.isCategoryField()) {
                condition = composeCategoryCondition(listStructure, dynamicSearchProperty, (CategoryField) field, i);
            } else {
                condition = composeCondition(listStructure, dynamicSearchProperty);
            }

            if (condition != null) {
                listStructure = addConditionToListStructure(listStructure, condition);
            }
        }

        return listStructure;
    }

    private Condition composeCondition(ListStructure listStructure, DynamicSearchProperty dynamicSearchProperty) {
        Condition condition = null;

        Field field;
        if (dynamicSearchProperty.isFunctionAlias()) {
            field = listStructure.getFunction(dynamicSearchProperty.getFieldAlias());
        } else {
            field = findFantabulousField(listStructure, dynamicSearchProperty.getFieldAlias());
        }

        DynamicSearchConstants.Operator operator = dynamicSearchProperty.getOperator();

        if (field != null) {
            if (OperatorUtil.isOperatorWithoutValue(operator.getConstant())) {
                condition = composeConditionWithoutValue(field, dynamicSearchProperty);

            } else if (DynamicSearchUtil.isDynamicSearchTwoBoxView(operator.getConstant())) {
                condition = composeBetweenCondition(field, dynamicSearchProperty);

            } else {
                String value = dynamicSearchProperty.getSimpleValue();
                if (value != null) {
                    condition = getSimpleCondition(field, operator.getFantabulousOperator() , value);
                }
            }
        }

        return condition;
    }

    private Condition composeCategoryCondition(ListStructure listStructure, DynamicSearchProperty dynamicSearchProperty, CategoryField categoryField, int index) {
        Condition condition = null;

        CategoryFantabulousCompleter categoryFantabulousCompleter = new CategoryFantabulousCompleter(categoryField, index);
        boolean completeSuccess = categoryFantabulousCompleter.completeListStructure(listStructure);
        if (completeSuccess) {
            DynamicSearchConstants.Operator operator = dynamicSearchProperty.getOperator();
            String realAlias = categoryFantabulousCompleter.getRealCategoryFieldAlias(operator);
            if (realAlias != null) {
                DynamicSearchProperty categoryDynamicSearchProperty = new DynamicSearchProperty(realAlias, operator, dynamicSearchProperty.getValues());
                categoryDynamicSearchProperty.setFunctionAlias(categoryFantabulousCompleter.isFunctionAlias());
                condition = composeCondition(listStructure, categoryDynamicSearchProperty);
            }
        }

        return condition;
    }

    private Field findFantabulousField(ListStructure listStructure, String fieldAlias) {
        Field field = null;
        if (isUnionList) {
            try {
                field = listStructure.getField(fieldAlias);
            } catch (FieldNotFoundException e) {
                log.debug("Not found field in inner union list:" + fieldAlias);
            }
        } else {
            field = listStructure.getField(fieldAlias);
        }
        return field;
    }

    private SimpleCondition getSimpleCondition(Field field, String fantaOperator, String value) {
        SimpleCondition condition = new SimpleCondition();
        condition.setField1(field);
        condition.setOperator(fantaOperator);
        condition.setValue(value);
        return condition;
    }

    private GroupCondition composeBetweenCondition(Field field, DynamicSearchProperty dynamicSearchProperty) {
        GroupCondition groupCondition = null;

        String value1 = dynamicSearchProperty.getValue(0);
        String value2 = dynamicSearchProperty.getValue(1);

        if (value1 != null && value2 != null) {
            groupCondition = composeRangeCondition(field, value1, value2);
        }

        return groupCondition;
    }

    private GroupCondition composeRangeCondition(Field field, String value1, String value2) {
        GroupCondition groupCondition = null;

        if (value1 != null && value2 != null) {
            SimpleCondition min = getSimpleCondition(field, Constants.OPERATOR_GREATER_EQUAL, value1);
            SimpleCondition max = getSimpleCondition(field, Constants.OPERATOR_LESS_EQUAL, value2);

            min.addAndCondition(max);
            groupCondition = new GroupCondition();
            groupCondition.setGroupCondition(min);
        }

        return groupCondition;
    }

    private Condition composeConditionWithoutValue(Field field, DynamicSearchProperty dynamicSearchProperty) {
        Condition condition = null;
        DynamicSearchConstants.Operator operator = dynamicSearchProperty.getOperator();
        DateTime currentDate = currentDateTime();

        if (DynamicSearchConstants.Operator.YESTERDAY.equals(operator)) {
            condition = getSimpleCondition(field, Constants.OPERATOR_EQUAL, getDateAsString(incrementDay(currentDate, -1)));
        } else if (DynamicSearchConstants.Operator.TODAY.equals(operator)) {
            condition = getSimpleCondition(field, Constants.OPERATOR_EQUAL, getDateAsString(currentDate));
        } else if (DynamicSearchConstants.Operator.TOMORROW.equals(operator)) {
            condition = getSimpleCondition(field, Constants.OPERATOR_EQUAL, getDateAsString(incrementDay(currentDate, 1)));

        } else if (DynamicSearchConstants.Operator.LAST_7_DAYS.equals(operator)) {
            List<DateTime> range = daysRange(currentDate, -7);
            condition = composeRangeCondition(field, getDateAsString(range.get(0)), getDateAsString(range.get(1)));

        } else if (DynamicSearchConstants.Operator.NEXT_7_DAYS.equals(operator)) {
            List<DateTime> range = daysRange(currentDate, 7);
            condition = composeRangeCondition(field, getDateAsString(range.get(0)), getDateAsString(range.get(1)));

        } else if (DynamicSearchConstants.Operator.LAST_MONTH.equals(operator)) {
            List<DateTime> range = monthRange(currentDate, -1);
            condition = composeRangeCondition(field, getDateAsString(range.get(0)), getDateAsString(range.get(1)));

        } else if (DynamicSearchConstants.Operator.THIS_MONTH.equals(operator)) {
            List<DateTime> range = monthRange(currentDate, 0);
            condition = composeRangeCondition(field, getDateAsString(range.get(0)), getDateAsString(range.get(1)));

        } else if (DynamicSearchConstants.Operator.NEXT_MONTH.equals(operator)) {
            List<DateTime> range = monthRange(currentDate, 1);
            condition = composeRangeCondition(field, getDateAsString(range.get(0)), getDateAsString(range.get(1)));

        } else if (DynamicSearchConstants.Operator.LAST_30_DAYS.equals(operator)) {
            List<DateTime> range = daysRange(currentDate, -30);
            condition = composeRangeCondition(field, getDateAsString(range.get(0)), getDateAsString(range.get(1)));

        } else if (DynamicSearchConstants.Operator.NEXT_30_DAYS.equals(operator)) {
            List<DateTime> range = daysRange(currentDate, 30);
            condition = composeRangeCondition(field, getDateAsString(range.get(0)), getDateAsString(range.get(1)));

        } else if (DynamicSearchConstants.Operator.LAST_YEAR.equals(operator)) {
            List<DateTime> range = yearRange(currentDate, -1);
            condition = composeRangeCondition(field, getDateAsString(range.get(0)), getDateAsString(range.get(1)));

        } else if (DynamicSearchConstants.Operator.THIS_YEAR.equals(operator)) {
            List<DateTime> range = yearRange(currentDate, 0);
            condition = composeRangeCondition(field, getDateAsString(range.get(0)), getDateAsString(range.get(1)));

        } else if (DynamicSearchConstants.Operator.NEXT_YEAR.equals(operator)) {
            List<DateTime> range = yearRange(currentDate, 1);
            condition = composeRangeCondition(field, getDateAsString(range.get(0)), getDateAsString(range.get(1)));
        }

        return condition;
    }


    /**
     * add fantabulous conditions to list structure with 'and' connector
     *
     * @param listStructure
     * @param fantaCondition
     * @return list structure
     */
    private ListStructure addConditionToListStructure(ListStructure listStructure, Condition fantaCondition) {

        if (listStructure != null && fantaCondition != null) {

            //add fantabulous condition to list structure of fantabulous
            Condition listCondition = listStructure.getCondition();
            if (listCondition != null) {
                getFinalFantaCondition(listCondition).addAndCondition(fantaCondition);
            } else {
                listStructure.setCondition(fantaCondition);
            }
        }

        return listStructure;
    }

    /**
     * get the final condition of the structure de conditions of fantabulous
     *
     * @param fantaCondition fantabulous Condition object
     * @return final Condition fantabulous object
     */
    private Condition getFinalFantaCondition(Condition fantaCondition) {
        while (fantaCondition.hasNext()) {
            fantaCondition = fantaCondition.next();
        }
        return fantaCondition;
    }

    private DateTime currentDateTime() {
        return new DateTime();
    }

    private DateTime incrementDay(DateTime current, int inc) {
        DateTime newDateTime;
        if (inc < 0) {
            inc = inc * (-1);
            newDateTime = current.minusDays(inc);
        } else {
            newDateTime = current.plusDays(inc);
        }
        return newDateTime;
    }

    private List<DateTime> daysRange(DateTime current, int inc) {
        List<DateTime> result = new ArrayList<DateTime>();

        DateTime dateTime2 = incrementDay(current, inc);
        if (current.isBefore(dateTime2)) {
            result.add(current);
            result.add(dateTime2);
        } else {
            result.add(dateTime2);
            result.add(current);
        }
        return result;
    }

    private List<Integer> monthRangeAsInteger(DateTime current, int inc) {
        List<DateTime> rangeList = monthRange(current, inc);

        List<Integer> result = new ArrayList<Integer>();
        result.add(DateUtils.dateToInteger(rangeList.get(0)));
        result.add(DateUtils.dateToInteger(rangeList.get(1)));

        return result;
    }

    private List<DateTime> monthRange(DateTime current, int inc) {
        DateTime month;
        if (inc < 0) {
            inc = inc * (-1);
            month = current.minusMonths(inc);
        } else {
            month = current.plusMonths(inc);
        }
        DateTime min = month.dayOfMonth().withMinimumValue();
        DateTime max = month.dayOfMonth().withMaximumValue();

        List<DateTime> result = new ArrayList<DateTime>();
        result.add(min);
        result.add(max);
        return result;
    }

    private List<DateTime> yearRange(DateTime current, int inc) {
        DateTime year;
        if (inc < 0) {
            inc = inc * (-1);
            year = current.minusYears(inc);
        } else {
            year = current.plusYears(inc);
        }
        DateTime min = year.dayOfYear().withMinimumValue();
        DateTime max = year.dayOfYear().withMaximumValue();

        List<DateTime> result = new ArrayList<DateTime>();
        result.add(min);
        result.add(max);
        return result;
    }

    private String getDateAsString(DateTime dateTime) {
        return DateUtils.dateToInteger(dateTime).toString();
    }

}
