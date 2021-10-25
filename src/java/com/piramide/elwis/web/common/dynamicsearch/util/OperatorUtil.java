package com.piramide.elwis.web.common.dynamicsearch.util;

import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchConstants;
import com.piramide.elwis.web.common.dynamicsearch.el.DynamicSearchUtil;
import com.piramide.elwis.web.common.dynamicsearch.structure.Field;
import com.piramide.elwis.web.common.dynamicsearch.structure.FieldOperator;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class OperatorUtil {

    public static List<DynamicSearchConstants.Operator> getDataBaseOperators() {
        List<DynamicSearchConstants.Operator> result = new ArrayList<DynamicSearchConstants.Operator>();

        result.add(DynamicSearchConstants.Operator.IS);
        result.add(DynamicSearchConstants.Operator.IS_NOT);
        return result;
    }

    public static List<DynamicSearchConstants.Operator> getConstantOperators() {
        List<DynamicSearchConstants.Operator> result = new ArrayList<DynamicSearchConstants.Operator>();

        result.add(DynamicSearchConstants.Operator.IS);
        result.add(DynamicSearchConstants.Operator.IS_NOT);
        return result;
    }

    public static List<DynamicSearchConstants.Operator> getTextOperators() {
        List<DynamicSearchConstants.Operator> result = new ArrayList<DynamicSearchConstants.Operator>();

        result.add(DynamicSearchConstants.Operator.EQUAL);
        result.add(DynamicSearchConstants.Operator.CONTAIN);
        result.add(DynamicSearchConstants.Operator.START_WITH);
        result.add(DynamicSearchConstants.Operator.END_WITH);
        result.add(DynamicSearchConstants.Operator.NOT_EQUAL);

        return result;
    }

    public static List<DynamicSearchConstants.Operator> getNumberOperators() {
        List<DynamicSearchConstants.Operator> result = new ArrayList<DynamicSearchConstants.Operator>();

        result.add(DynamicSearchConstants.Operator.EQUAL);
        result.add(DynamicSearchConstants.Operator.LESS_THAN);
        result.add(DynamicSearchConstants.Operator.GREATER_THAN);
        result.add(DynamicSearchConstants.Operator.BETWEEN);
        result.add(DynamicSearchConstants.Operator.NOT_EQUAL);

        return result;
    }

    public static List<DynamicSearchConstants.Operator> getBitwiseOperators() {
        List<DynamicSearchConstants.Operator> result = new ArrayList<DynamicSearchConstants.Operator>();
        result.add(DynamicSearchConstants.Operator.ANDBIT);
        return result;
    }

    public static List<DynamicSearchConstants.Operator> getDateOperators() {
        List<DynamicSearchConstants.Operator> result = new ArrayList<DynamicSearchConstants.Operator>();

        result.add(DynamicSearchConstants.Operator.ON);
        result.add(DynamicSearchConstants.Operator.BEFORE);
        result.add(DynamicSearchConstants.Operator.AFTER);
        result.add(DynamicSearchConstants.Operator.BETWEEN);
        result.add(DynamicSearchConstants.Operator.NOT_ON);
        result.add(DynamicSearchConstants.Operator.YESTERDAY);
        result.add(DynamicSearchConstants.Operator.TODAY);
        result.add(DynamicSearchConstants.Operator.TOMORROW);
        result.add(DynamicSearchConstants.Operator.LAST_7_DAYS);
        result.add(DynamicSearchConstants.Operator.NEXT_7_DAYS);
        result.add(DynamicSearchConstants.Operator.LAST_MONTH);
        result.add(DynamicSearchConstants.Operator.THIS_MONTH);
        result.add(DynamicSearchConstants.Operator.NEXT_MONTH);
        result.add(DynamicSearchConstants.Operator.LAST_30_DAYS);
        result.add(DynamicSearchConstants.Operator.NEXT_30_DAYS);
        result.add(DynamicSearchConstants.Operator.LAST_YEAR);
        result.add(DynamicSearchConstants.Operator.THIS_YEAR);
        result.add(DynamicSearchConstants.Operator.NEXT_YEAR);

        return result;
    }

    public static boolean isOperatorWithoutValue(String operatorConstant) {
        if (DynamicSearchConstants.Operator.YESTERDAY.equal(operatorConstant)
                || DynamicSearchConstants.Operator.TODAY.equal(operatorConstant)
                || DynamicSearchConstants.Operator.TOMORROW.equal(operatorConstant)
                || DynamicSearchConstants.Operator.LAST_7_DAYS.equal(operatorConstant)
                || DynamicSearchConstants.Operator.NEXT_7_DAYS.equal(operatorConstant)
                || DynamicSearchConstants.Operator.LAST_MONTH.equal(operatorConstant)
                || DynamicSearchConstants.Operator.THIS_MONTH.equal(operatorConstant)
                || DynamicSearchConstants.Operator.NEXT_MONTH.equal(operatorConstant)
                || DynamicSearchConstants.Operator.LAST_30_DAYS.equal(operatorConstant)
                || DynamicSearchConstants.Operator.NEXT_30_DAYS.equal(operatorConstant)
                || DynamicSearchConstants.Operator.LAST_YEAR.equal(operatorConstant)
                || DynamicSearchConstants.Operator.THIS_YEAR.equal(operatorConstant)
                || DynamicSearchConstants.Operator.NEXT_YEAR.equal(operatorConstant)
                ) {
            return true;
        }
        return false;
    }

    public static DynamicSearchConstants.Operator getFieldDefaultOperator(String dynamicSearchName, String fieldAlias, HttpServletRequest request) {
        DynamicSearchConstants.Operator defaultOperator = null;

        Field field = DynamicSearchUtil.findField(dynamicSearchName, fieldAlias, request);
        if (field != null) {
            FieldOperator fieldOperator = field.findDefaultFieldOperator();
            if (fieldOperator != null) {
                defaultOperator = fieldOperator.getOperator();
            }
        }
        return defaultOperator;
    }

    public static DynamicSearchConstants.Operator getDefaultOperatorByFieldType(Field field) {
        DynamicSearchConstants.Operator defaultOperator = null;

        if (field != null) {
            if (DynamicSearchConstants.FieldType.STRING.equals(field.getType())
                    || DynamicSearchConstants.FieldType.INTEGER.equals(field.getType())
                    || DynamicSearchConstants.FieldType.DECIMAL.equals(field.getType())
                    ) {
                defaultOperator = DynamicSearchConstants.Operator.EQUAL;
            } else if (DynamicSearchConstants.FieldType.DATE.equals(field.getType())) {
                defaultOperator = DynamicSearchConstants.Operator.ON;
            } else if (DynamicSearchConstants.FieldType.DATABASE.equals(field.getType())
                    || DynamicSearchConstants.FieldType.CONSTANT.equals(field.getType())) {
                defaultOperator = DynamicSearchConstants.Operator.IS;
            } else if (DynamicSearchConstants.FieldType.BITWISE.equals(field.getType())) {
                defaultOperator = DynamicSearchConstants.Operator.ANDBIT;
            }
        }
        return defaultOperator;
    }

}
